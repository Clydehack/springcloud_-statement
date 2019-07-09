package com.uzone.settlement.framework.task.schedule;

import java.time.LocalDate;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.uzone.settlement.framework.task.handle.AllinpayHandler;
import com.uzone.settlement.framework.task.handle.InitHandler;
import com.uzone.settlement.framework.task.mapper.DispatchTaskMapper;
import com.uzone.settlement.framework.util.RedisUtil;
import com.uzone.settlement.model.GeneralModel;

/**
 * 任务调度
 */
@Configuration
@EnableScheduling
public class DispatchTask {
	
	private Logger logger = LoggerFactory.getLogger(DispatchTask.class);
	
	@Autowired
	InitHandler initHandler;
	@Autowired
	AllinpayHandler allinpayHandler;
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	DispatchTaskMapper dispatchTaskMapper;
	
	private final String LOCAL_SET = "localSet";			// 本地账单集合key
	private final String OUTER_SET = "outerSet";			// 对方账单集合key
	private final String INTERSECTION = "intersection";		// 双方账单的交集key
	private final String LOCAL_DIFF_SET = "localDiffSet";	// 本地账单和交集的差集key，通常是本地长款差错，也会有金额错误
	private final String OUTER_DIFF_SET = "outerDiffSet";	// 对方账单和交集的差集key，通常是对方短款差错，也会有金额错误
	private final String yesterday = LocalDate.now().minusDays(+1).toString();	// 获取前一天的日期
	
	/** 每半小时启动一次 */
	@Scheduled(cron = "0/5 * * * * ?")
	public void task() {
		
		
		initHandler.initTask(yesterday);
		
		long allinpayFlag = allinpayHandler.processingTask(yesterday, OUTER_SET);
		
		/**
		 * 从库里加载本地账单数据
		 */
		Map<String, GeneralModel> uzone_map = null;
		uzone_map = dispatchTaskMapper.queryLocalData(yesterday);
		if(uzone_map == null || uzone_map.size() == 0) {
			logger.error("加载本地账单失败");
			// 加载本地账单失败的处理
		}
		
		/**
		 * 加载成功，放入redis
		 */
		long flag = redisUtil.sSet(LOCAL_SET, uzone_map);
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
		Map<String, GeneralModel> intersection = (Map<String, GeneralModel>) redisUtil.sGet(INTERSECTION);
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
