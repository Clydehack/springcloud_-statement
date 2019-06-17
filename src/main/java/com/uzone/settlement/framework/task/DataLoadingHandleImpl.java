package com.uzone.settlement.framework.task;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.uzone.settlement.framework.util.RedisUtil;

public class DataLoadingHandleImpl implements DataLoadingHandle {

	@Autowired
	RedisUtil redisUtil;
	
	private Map<String, Object> map;
	
	/**
	 * 通过redis实现求集合交集
	 */
	@Override
	public Boolean dataLoadLocalCheckAccount() {
		// 1.取出本地库里的数据并格式化为对账模型
		
		// 2.本地对账模型存入redis
		redisUtil.sSet("localmap", map);
		return true;
	}

	@Override
	public Boolean dataLoadOtherCheckAccount() {
		// 1.取出本地库里的数据并格式化为对账模型
		
		// 2.对方对账模型存入redis
		redisUtil.sSet("localmap", map);
		return true;
	}

}
