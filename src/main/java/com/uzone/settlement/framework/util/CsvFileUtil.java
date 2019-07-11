package com.uzone.settlement.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.csvreader.CsvReader;
import com.uzone.settlement.model.GeneralModel;

/**
 * 使用JavaCSV工具处理csv文件
 */
public class CsvFileUtil {
	
	public static void main(String[] args) {
		try {
			analysisAllinpayCsvUtil("http://116.228.64.55:9093/IMEServer/docroot/attachments/merchantCheck/2019/06/100009001000_20190610_1_allinpay.txt", '|');
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 给到“文件地址”和“分隔符”，获取数据封装成“对账模型”（失败向上抛出异常）
	 */
	public static Map<String, GeneralModel> analysisAllinpayCsvUtil(String fileUrl, char delimiter) throws IOException {
		Map<String, GeneralModel> map = new HashMap<>();
		InputStream in = null;
		try {
			URL url = new URL(fileUrl);
			URLConnection connection = url.openConnection(); 			// openConnection()
			in = connection.getInputStream(); 							// getInputStream()
			Charset charset = Charset.forName("UTF-8");					// 字符集
			CsvReader csvReader = new CsvReader(in, delimiter, charset);// 创建CSV读对象
			/*
				csvReader.readHeaders();	读表头
				csvReader.getHeaderCount();	获取首行的字段数
				csvReader.getRawRecord();	读一整行
				csvReader.get("列名");		读这行的某一列
			*/
			csvReader.readHeaders();
			while (csvReader.readRecord()) {
				//GeneralModel billModel = new GeneralModel();
				csvReader.get("订单类型");
				csvReader.get("支付方式");
				
//				billModel.setTransfer_type(csvReader.get("订单类型"));
//				billModel.setAllinpay_order_no(csvReader.get("通联订单号"));
//				billModel.setTrans_amount(csvReader.get("交易金额(单位:分)"));
//				billModel.setFee(csvReader.get("手续费金额(单位:分)"));
//				billModel.setCreate_time(csvReader.get("交易时间"));
//				billModel.setBiz_order_no(csvReader.get("商户订单编号"));
//				map.put(csvReader.get("商户订单编号"), billModel);
			}
			System.out.println("");
		} finally {
			in.close();
		}
		return map;
	}
}
