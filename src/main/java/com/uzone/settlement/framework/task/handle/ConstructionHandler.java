package com.uzone.settlement.framework.task.handle;

import com.uzone.settlement.framework.task.mapper.DispatchTaskMapper;
import com.uzone.settlement.framework.util.CustomException;
import com.uzone.settlement.model.BuCheckaccountLogPO;

/**
 * 初始化判断
 */
public class ConstructionHandler {

	DispatchTaskMapper dispatchTaskMapper;

	public void initTask(String date) throws CustomException {
		BuCheckaccountLogPO log = dispatchTaskMapper.queryLog(date);
		if (null != log && "S".equals(log.getCheckaccountStatus())) {throw new CustomException("00", "对账已完成");}
	}
}
