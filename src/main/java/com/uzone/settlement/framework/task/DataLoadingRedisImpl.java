package com.uzone.settlement.framework.task;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.uzone.settlement.framework.util.RedisUtil;
import com.uzone.settlement.model.BillModel;

/**
 * 使用redis实现的求集合交集
 */
public class DataLoadingRedisImpl implements DataLoadingHandle {

	@Autowired
	RedisUtil redisUtil;
	
	/**
	 * TODO 先实现普通集合比对的，后面再改字符串就是了
	 */
	@Override
	public long dataLoadLocalCheckAccount(Map<String, BillModel> map) {
		// 1.取出本地库里的数据并格式化为对账模型
		
		// 2.本地对账模型存入redis
		return redisUtil.sSet("localmap", map);
	}

	@Override
	public long dataLoadOtherCheckAccount(Map<String, BillModel> map) {
		// 1.取出本地库里的数据并格式化为对账模型
		
		// 2.对方对账模型存入redis
		return redisUtil.sSet("localmap", map);
	}

}
