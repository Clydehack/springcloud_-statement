package com.uzone.settlement.framework.task.handle;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.uzone.settlement.framework.util.AccessInterface;

public class AllinpayAPIHandler {

	/** 通联支付接口. */
    @Value("${uzone.allinpay.baseUrl}")
    private String baseUrl;
	
	public String depositApplyGateWay(Long userId, BigDecimal amount, String bizOrderNo){
	    String depositUrl = baseUrl+"/order/accountRevenueandExpenditureDetail";
	    
	    Map<String,String> map = new HashMap<String,String>(6);
	    map.put("bizUserId", "");
	    map.put("accountSetNo", "");
	    map.put("dateStart", "");
	    map.put("dateEnd", "");
	    map.put("startPosition", "");
	    map.put("queryNum", "");
	    
	    // 请求通联接口
	    String resultStr = AccessInterface.sendPost(depositUrl,map);
	    System.out.println("查询账户收支明细-queryInExpDetail接口String返回值："+resultStr);
	    
	    // 解析获取数据
	    
	    return resultStr;
	}
}
