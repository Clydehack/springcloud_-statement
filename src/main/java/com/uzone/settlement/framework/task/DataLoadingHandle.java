package com.uzone.settlement.framework.task;

/**
 * 一、先加载对账数据
 */
public interface DataLoadingHandle {

	/**
	 * 加载本地对账数据，得到本地账单集合-A
	 */
	public Boolean dataLoadLocalCheckAccount();
	
	/**
	 * 加载对方对账数据，得到对方账单集合-B
	 */
	public Boolean dataLoadOtherCheckAccount();
}
