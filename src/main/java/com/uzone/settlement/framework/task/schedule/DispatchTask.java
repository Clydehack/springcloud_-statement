package com.uzone.settlement.framework.task.schedule;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.uzone.settlement.framework.task.mapper.DispatchTaskMapper;
import com.uzone.settlement.framework.util.CsvFileUtil;
import com.uzone.settlement.framework.util.RedisUtil;
import com.uzone.settlement.model.BillModel;

/**
 * 任务调度
 */
public class DispatchTask {
	
	private Logger logger = LoggerFactory.getLogger(DispatchTask.class);
	
//	@Autowired
//	DataLoadingHandle dataLoadingHandle = new DataLoadingRedisImpl();	// 注入redis的实现
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	DispatchTaskMapper dispatchTaskMapper;
	
	public void task() {
		/**
		 * 0.定时启动，先任务初始化
		 */
		String yesterday = LocalDate.now().minusDays(+1).toString();
//		int count = dispatchTaskMapper.exitColumn(yesterday);
//		if(count == 0) {
//			dispatchTaskMapper.insertYesterdayInit();// 昨天的账还没对，第一次初始化
//		} else {
			boolean ok = dispatchTaskMapper.queryReconciliationStatus(yesterday);
			if(ok) {return;}// 账已经对过了，直接跳出
			// 清洗当天对账的脏数据 TODO
//		}
		
		/**
		 * 1.下载云商通账单数据，如果失败，间隔30s后重试，如果失败将异常记录在日志表中
		 */
		Map<String, BillModel> allinpay_map = null;
		
		try {
			allinpay_map = CsvFileUtil.analysisCsvInputStreamByJavaCsvUtil("", '|');
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("下载通联账单失败");
			// 失败的处理逻辑，重试机制，完全失败后的处理
		}
		/**
		 * 下载成功，放入redis
		 */
		long flag = redisUtil.sSet("localmap", allinpay_map);
		if(flag != allinpay_map.size()) {
			logger.error("加载通联账单失败");
			// 如果拉取的帐单数，和存入redis的帐单数不相符，重新来一遍
		}
		
		/**
		 * 从库里加载本地账单数据
		 */
		Map<String, BillModel> uzone_map = null;
		dispatchTaskMapper.queryReconciliationStatus(yesterday);
		
		/**
		 * 加载成功，放入redis
		 */
		flag = redisUtil.sSet("localmap", uzone_map);
		if(flag != allinpay_map.size()) {
			logger.error("加载通联账单失败");
			// 如果加载的帐单数，和存入redis的帐单数不相符，重新来一遍
		}
		
		/**
		 * 2.将加载的数据进行比对
		 */
		
		/**
		 * 3.将对账结果入库，异常的进异常表，正常的进明细表，然后清理redis缓存
		 */
		
		/**
		 * 4.进行到这一步，更新对账日志表对账字段为成功
		 */
	}
}
