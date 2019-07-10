package com.uzone.settlement.framework.task.handle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.uzone.settlement.framework.task.mapper.DispatchTaskMapper;
import com.uzone.settlement.framework.util.CustomException;
import com.uzone.settlement.framework.util.RedisUtil;
import com.uzone.settlement.model.GeneralModel;

public class LocalDataHandler {

	@Autowired RedisUtil redisUtil;
	@Autowired DispatchTaskMapper dispatchTaskMapper;
	
	public List<GeneralModel> processingTask(String date, String key) throws CustomException{
		/** 从库里加载本地账单数据 */
		List<GeneralModel> uzone = dispatchTaskMapper.queryLocalData(date);
		if(null == uzone || uzone.size() == 0) {throw new CustomException("01", "本地无账单");}
		
		/**
		 * 加载成功，放入redis
		 */
		long flag = redisUtil.sSet(key, uzone);
		if(flag != uzone.size()) {
			// 如果加载的帐单数，和存入redis的帐单数不相符，重新来一遍
		}
		return uzone;
    }
}
