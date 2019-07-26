package com.uzone.settlement.framework.task.schedule;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.uzone.settlement.framework.task.handle.AllinpayHandler;
import com.uzone.settlement.framework.task.handle.ConstructionHandler;
import com.uzone.settlement.framework.task.handle.DestructionHandler;
import com.uzone.settlement.framework.task.handle.LocalDataHandler;
import com.uzone.settlement.framework.task.handle.SaveHandler;
import com.uzone.settlement.framework.util.CustomException;
import com.uzone.settlement.framework.util.RedisUtil;

/**
 * 任务调度
 * 
 * TODO
 * 2019-7-9 依赖分析：通联提供的对账数据查询接口，必须依赖商户的用户id，所以必须先查本地
 * 2019-7-10 最优方案（大概吧）
 * 		①抽象出n方对账，之后扩展其他对账方只需实现对账方公司的对象就行了；
 * 		②各个公司的对象只要负责加载自己的数据就行了，这样抽象出一个统一加载方法，只加载对账关心的数据；
 * 		③redis的key优化，自动扩展新的对账方的key
 * 		④统一为T+1对账，为以后兼容
 */
@Configuration
@EnableScheduling
public class DispatchTask {
	
	private Logger logger = LoggerFactory.getLogger(DispatchTask.class);
	
	@Autowired 
	ConstructionHandler constructionHandler;
	@Autowired 
	LocalDataHandler localDataHandler;
	@Autowired 
	AllinpayHandler allinpayHandler;
	@Autowired 
	SaveHandler saveHandler;
	@Autowired 
	DestructionHandler destructionHandler;
	@Autowired
	RedisUtil redisUtil;
	/** 对账key */
	public static final String SETTLEMENT = "SETTLEMENT";	// 对账的redis的主Key
	
	private final String LOCAL_SET = "localSet";			// 本地账单集合key
	private final String OUTER_SET = "outerSet";			// 对方账单集合key
	private final String INTERSECTION = "intersection";		// 双方账单的交集key
	private final String LOCAL_DIFF_SET = "localDiffSet";	// 本地账单和交集的差集key，通常是本地长款差错，也会有金额错误
	private final String OUTER_DIFF_SET = "outerDiffSet";	// 对方账单和交集的差集key，通常是对方短款差错，也会有金额错误
	private final String yesterday = LocalDate.now().minusDays(+1).toString();	// 获取前一天的日期
	
	/** 每半小时启动一次 */
	@Scheduled(cron = "0/5 * * * * ?")
	public void task() {
		try {
			List<String> userIdList = constructionHandler.initTask(yesterday);			// 统一通过用户id获取格式化数据
			logger.info("初始化完毕");
			
			localDataHandler.processingTask(yesterday, LOCAL_SET, userIdList);
			allinpayHandler.processingTask(yesterday, OUTER_SET, userIdList);
			logger.info("数据加载完毕");
			
			redisUtil.sGetIntersectAndStore(LOCAL_SET, OUTER_SET, INTERSECTION);		// 本地和对方求交集
			redisUtil.sGetDifferenceAndStore(LOCAL_SET, INTERSECTION, LOCAL_DIFF_SET);	// 本地和交集求差集
			redisUtil.sGetDifferenceAndStore(OUTER_SET, INTERSECTION, OUTER_DIFF_SET);	// 对方和交集求差集
			logger.info("数据核对完毕");
			
			saveHandler.saveData(INTERSECTION, LOCAL_DIFF_SET, OUTER_DIFF_SET);
			logger.info("数据落库完毕");
		} catch (CustomException e) {
			logger.info("code==>{},msg==>{}", e.getErrorCode(), e.getMessage());
		} catch (Exception e) {
			destructionHandler.errHandleTask(yesterday);
			logger.info("其他异常，数据清洗完毕");
		} finally {
			destructionHandler.closeTask(LOCAL_SET, OUTER_SET, INTERSECTION, LOCAL_DIFF_SET, OUTER_DIFF_SET);
			logger.info("收尾完毕");
		}
	}
}
