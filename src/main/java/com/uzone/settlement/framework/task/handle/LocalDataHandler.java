package com.uzone.settlement.framework.task.handle;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.uzone.settlement.framework.task.mapper.DispatchTaskMapper;
import com.uzone.settlement.framework.task.schedule.DispatchTask;
import com.uzone.settlement.framework.util.CustomException;
import com.uzone.settlement.framework.util.RedisUtil;
import com.uzone.settlement.model.GeneralModel;

public class LocalDataHandler {

	@Autowired RedisUtil redisUtil;
	@Autowired DispatchTaskMapper dispatchTaskMapper;
	
	public List<GeneralModel> processingTask(String date, String key, List<String> userIdList) throws CustomException{
		/** 从库里加载本地账单数据 */
		List<GeneralModel> uzone = new ArrayList<>();
    	for (String userId : userIdList) {
    		List<GeneralModel> uzoneBizOrderNo = dispatchTaskMapper.queryLocalData(date, userId);
    		if(null == uzoneBizOrderNo || uzoneBizOrderNo.size() == 0) {throw new CustomException("11", "未获取到本地账单");}
    		uzone.addAll(uzoneBizOrderNo);
		}
		
		/**
		 * 加载成功，放入redis
		 */
		long flag = redisUtil.sSet(DispatchTask.SETTLEMENT, key, uzone);
		if(flag != uzone.size()) {
			// 如果加载的帐单数，和存入redis的帐单数不相符，重新来一遍，目前直接抛异常去清洗等待定时重来吧
			throw new CustomException("12", "本地账单数量和redis加载的账单数量不一样");
		}
		return uzone;
    }
}
