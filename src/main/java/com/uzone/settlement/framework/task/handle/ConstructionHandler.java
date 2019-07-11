package com.uzone.settlement.framework.task.handle;

import java.util.List;

import com.uzone.settlement.framework.task.mapper.DispatchTaskMapper;
import com.uzone.settlement.framework.util.CustomException;
import com.uzone.settlement.model.BuCheckaccountLogPO;

/**
 * 初始化判断
 */
public class ConstructionHandler {

	DispatchTaskMapper dispatchTaskMapper;

	public List<String> initTask(String date) throws CustomException {
		BuCheckaccountLogPO log = dispatchTaskMapper.queryLog(date);
		if (null != log && "S".equals(log.getCheckaccountStatus())) {throw new CustomException("00", "对账已完成");}
		// 查出所有要对账的userid
		List<String> userIdList = dispatchTaskMapper.queryUserId(date);
		if (null == userIdList || userIdList.size() == 0) {throw new CustomException("01", date + " - 无交易数据");}
		return userIdList;
	}
}
