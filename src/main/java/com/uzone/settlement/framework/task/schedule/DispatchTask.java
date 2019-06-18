package com.uzone.settlement.framework.task.schedule;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.uzone.settlement.framework.task.DataLoadingHandle;
import com.uzone.settlement.framework.task.DataLoadingRedisImpl;
import com.uzone.settlement.framework.util.CsvFileUtil;
import com.uzone.settlement.model.BillModel;

/**
 * 任务调度
 */
public class DispatchTask {
	
	private Logger logger = LoggerFactory.getLogger(DispatchTask.class);
	
	@Autowired
	DataLoadingHandle dataLoadingHandle = new DataLoadingRedisImpl();	// 注入redis的实现
	
	public void task() {
		/**
		 * 0.定时开启，初始化对账日志表
		 */
		
		
		
		/**
		 * 1.加载双方账单数据到redis，如果失败，间隔30s后重试，如果失败将异常记录在日志表中
		 */
		Map<String, BillModel> uzone_map = null;
		Map<String, BillModel> allinpay_map = null;
		
		
		try {
			allinpay_map = CsvFileUtil.analysisCsvInputStreamByJavaCsvUtil("", '|');
		} catch (IOException e) {
			logger.error("加载通联账单失败");
			e.printStackTrace();
		}
		long flag = dataLoadingHandle.dataLoadOtherCheckAccount(allinpay_map);
		if(flag != allinpay_map.size()) {
			// 如果拉取的帐单数，和存入redis的帐单数不相符，重新来一遍
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
