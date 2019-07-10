package com.uzone.settlement.framework.task.handle;

import com.uzone.settlement.framework.task.mapper.DispatchTaskMapper;
import com.uzone.settlement.framework.util.CustomException;

/**
 * 初始化
 */
public class ConstructionHandler {

	DispatchTaskMapper dispatchTaskMapper;
	
	public void initTask(String date) throws CustomException {
		int i = dispatchTaskMapper.exitColumn(date);
		if(i == 0) {throw new CustomException("00", "已完成对账");}
	}
}
