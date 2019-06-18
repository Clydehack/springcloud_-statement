package com.uzone.settlement.framework.task;

import java.util.Map;

import com.uzone.settlement.model.BillModel;

/**
 * 一、先加载对账数据
 */
public interface DataLoadingHandle {

	/**
	 * 加载本地对账数据，得到本地账单集合-A
	 */
	public long dataLoadLocalCheckAccount(Map<String, BillModel> map);
	
	/**
	 * 加载对方对账数据，得到对方账单集合-B
	 */
	public long dataLoadOtherCheckAccount(Map<String, BillModel> map);
}