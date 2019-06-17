package com.uzone.settlement.allinpay_reconciliation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人工处理账单接口（需要人工介入解决的问题都在此API中开放，例：查询对账结果，下载异常账单，上传补充数据等）
 */
@RestController
@RequestMapping("ExpAccounts")
public class ExpAccountsHandleController {

	/**
	 * 查询对账结果
	 */
	
	
	/**
	 * 异常账单结果查询（也可导出为Excel）
	 */
	
	
	/**
	 * 上传补充账单数据（因为云商通API不提供这种手续费的对账数据，需要人工在云商通-收银宝平台手动下载对账单。下载后再人工上传到此接口，补全云商通收取的手续费数据）
	 */
	
}
