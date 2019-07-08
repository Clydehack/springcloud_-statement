package com.uzone.settlement.framework.task.schedule;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.uzone.settlement.framework.task.handle.AllinpayAPIHandler;
import com.uzone.settlement.framework.task.mapper.DispatchTaskMapper;
import com.uzone.settlement.framework.util.CsvFileUtil;
import com.uzone.settlement.framework.util.RedisUtil;
import com.uzone.settlement.model.GeneralLedgerModel;

/**
 * 任务调度
 */
@Configuration
@EnableScheduling
public class DispatchTask {
	
	private Logger logger = LoggerFactory.getLogger(DispatchTask.class);
	
	@Autowired
	AllinpayAPIHandler allinpayAPIHandler;
//	@Autowired
//	DataLoadingHandle dataLoadingHandle = new DataLoadingRedisImpl();	// 注入redis的实现
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	DispatchTaskMapper dispatchTaskMapper;
	
	private final String LOCAL_SET = "localSet";			// 本地账单集合key
	private final String OUTER_SET = "outerSet";			// 对方账单集合key
	private final String INTERSECTION = "intersection";		// 双方账单的交集key
	private final String LOCAL_DIFF_SET = "localDiffSet";	// 本地账单和交集的差集key，通常是本地长款差错，也会有金额错误
	private final String OUTER_DIFF_SET = "outerDiffSet";	// 对方账单和交集的差集key，通常是对方短款差错，也会有金额错误
	
	@Scheduled(cron = "0/5 * * * * ?")
	public void task() {
		/**
		 * 0.每30min定时启动，先判断是否已对账，未对账就任务初始化
		 */
		String yesterday = LocalDate.now().minusDays(+1).toString();	// 获取前一天的日期
		int count = dispatchTaskMapper.exitColumn(yesterday);
		if(count == 0) {
			dispatchTaskMapper.insertYesterdayInit();// 昨天的账还没对，第一次初始化
		} else {
			//int flag = dispatchTaskMapper.queryReconciliationStatus(yesterday); TODO 测试如果 成功就只用这一句就好了 查看有没有记录，有记录的话是成功还是失败
			boolean ok = dispatchTaskMapper.queryReconciliationStatus(yesterday);
			if(ok) {return;}// 账已经对过了，直接跳出
			// 清洗当天对账的脏数据 TODO
		}
		
		/**
		 * 下载云商通账单数据，如果失败，间隔30s后重试，如果失败将异常记录在日志表中
		 */
		// 调用通联接口，获取url（调用代理，不引入通联SDK）
		String url = allinpayAPIHandler.depositApplyGateWay(userId, amount, bizOrderNo);
		
		Map<String, GeneralLedgerModel> allinpay_map = null;
		try {
			allinpay_map = CsvFileUtil.analysisAllinpayCsvUtil(url, '|');
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("下载通联账单失败");
			// 失败的处理逻辑，重试机制，完全失败后的处理
		}
		/**
		 * 下载成功，放入redis
		 */
		long flag = redisUtil.sSet(OUTER_SET, allinpay_map);
		if(flag != allinpay_map.size()) {
			logger.error("加载通联账单失败");
			// 如果拉取的帐单数，和存入redis的帐单数不相符，重新来一遍
		}
		
		/**
		 * 从库里加载本地账单数据
		 */
		Map<String, GeneralLedgerModel> uzone_map = null;
		uzone_map = dispatchTaskMapper.queryLocalData(yesterday);
		if(uzone_map == null || uzone_map.size() == 0) {
			logger.error("加载本地账单失败");
			// 加载本地账单失败的处理
		}
		
		/**
		 * 加载成功，放入redis
		 */
		flag = redisUtil.sSet(LOCAL_SET, uzone_map);
		if(flag != uzone_map.size()) {
			logger.error("加载本地账单失败");
			// 如果加载的帐单数，和存入redis的帐单数不相符，重新来一遍
		}
		
		/**
		 * 将加载的集合进行处理
		 */
		redisUtil.sGetIntersectAndStore(LOCAL_SET, OUTER_SET, INTERSECTION);		// 本地和对方求交集
		
		redisUtil.sGetDifferenceAndStore(LOCAL_SET, INTERSECTION, LOCAL_DIFF_SET);	// 本地和交集求差集
		redisUtil.sGetDifferenceAndStore(OUTER_SET, INTERSECTION, OUTER_DIFF_SET);	// 对方和交集求差集
		
		/**
		 * 3.将对账结果入库，异常的进异常表，正常的进明细表，然后清理redis缓存
		 */
		Map<String, GeneralLedgerModel> intersection = (Map<String, GeneralLedgerModel>) redisUtil.sGet(INTERSECTION);
		// 对平的放入明细表，然后清洗
		redisUtil.sGet(LOCAL_DIFF_SET);
		// 本地放入异常，然后清洗
		redisUtil.sGet(OUTER_DIFF_SET);
		// 对方对唱
		/**
		 * 4.进行到这一步，更新对账日志表对账字段为成功
		 */
	}
}
