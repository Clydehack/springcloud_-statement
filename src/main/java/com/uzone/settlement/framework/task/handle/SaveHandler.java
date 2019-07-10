package com.uzone.settlement.framework.task.handle;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.uzone.settlement.framework.util.RedisUtil;
import com.uzone.settlement.model.GeneralModel;

public class SaveHandler {

	@Autowired
	RedisUtil redisUtil;
	
	@Transactional
	public void saveData(String key1, String key2, String key3) {
		// 对平的放入明细表
		// Map<String, GeneralModel> intersection = (Map<String, GeneralModel>) redisUtil.sGet(key1);
		// 差错放入异常表
		Map<String, GeneralModel> err1 = (Map<String, GeneralModel>) redisUtil.sGet(key2);
		Map<String, GeneralModel> err2 = (Map<String, GeneralModel>) redisUtil.sGet(key3);
	}

}
