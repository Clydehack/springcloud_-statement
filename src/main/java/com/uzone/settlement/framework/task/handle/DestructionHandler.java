package com.uzone.settlement.framework.task.handle;

/**
 * 析构处理
 */
public class DestructionHandler {

	public void closeTask(String lOCAL_SET, String oUTER_SET, String INTERSECTION, 
			String lOCAL_DIFF_SET, String oUTER_DIFF_SET) {
		// 清理redis的key，value

	}

	public void errHandleTask(String date) {
		// 清理date对应的mysql记录
		
	}
}
