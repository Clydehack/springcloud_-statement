package com.uzone.settlement.allinpay_reconciliation.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 人工处理账单接口（需要人工介入解决的问题都在此API中开放，例：查询对账结果，下载异常账单，上传补充数据等）
 */
@RestController
@RequestMapping("ExpAccounts")
public class ExpAccountsHandleController {

	/** 设置响应,跨域 */
	private void setHeader(HttpServletResponse response) {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setDateHeader("Expires", 0);
	}
	
	/**
	 * 查询对账结果
	 */
	@RequestMapping(value = "/queryData", method = {RequestMethod.GET, RequestMethod.POST})
	public String queryData(HttpServletResponse response) {
		setHeader(response);
		return null;
	}
	
	/**
	 * 异常账单结果查询（也可导出为Excel）
	 */
	@RequestMapping(value = "/queryErrData", method = {RequestMethod.GET, RequestMethod.POST})
	public String queryErrData(HttpServletResponse response) {
		setHeader(response);
		return null;
	}
	
	/**
	 * 上传补充账单数据（因为云商通API不提供这种手续费的对账数据，需要人工在云商通-收银宝平台手动下载对账单。下载后再人工上传到此接口，补全云商通收取的手续费数据）
	 */
	@PostMapping(value = "/queryErrData")
	public String addImage(HttpServletRequest request,HttpServletResponse response) {
		setHeader(response);
		MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		// 拿参数
		String name = params.getParameter("name");
		System.out.println(name);
		// 拿文件
		String fileName = "";
		MultipartFile file = null;
        if (!files.isEmpty()) {
        	for (int i = 0; i < files.size(); ++i) {
        		if (!file.isEmpty()) {
        			fileName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('\\')+1);
        			try {
						String file_name = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						
					}
        		}
        	}
        }
		return null;
	}
}
