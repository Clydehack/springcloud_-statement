package com.uzone.settlement.framework.task.handle;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.uzone.settlement.framework.util.AccessInterface;
import com.uzone.settlement.framework.util.RedisUtil;
import com.uzone.settlement.model.GeneralModel;

/**
 * 拉取通联的数据，放入redis
 */
public class AllinpayHandler {

	/** 通联支付接口 */
    @Value("${uzone.allinpay.baseUrl}")
    private String baseUrl;
	
    @Autowired
	RedisUtil redisUtil;
    
    public void processingTask(String date, String key, Map<String,String> map, List<GeneralModel> uzone){
    	Map<String, GeneralModel> allinpay_map = queryDetail(map);
    	/** 对账数据下载成功，放入redis */
    	long flag = redisUtil.sSet(key, allinpay_map);
    	if(flag != allinpay_map.size()) {
    		// 如果拉取的帐单数，和存入redis的帐单数不相符，重新来一遍
    	}
    }
    
    /** 调用通联API - 4.2.22 查询账户收支明细 */
    private Map<String, GeneralModel> queryDetail(Map<String,String> map){
	    String depositUrl = baseUrl+"/order/accountRevenueandExpenditureDetail";
	    map.put("bizUserId", "");
	    map.put("accountSetNo", "");
	    map.put("dateStart", "");
	    map.put("dateEnd", "");
	    map.put("startPosition", "");
	    map.put("queryNum", "");
	    String resultStr = AccessInterface.sendPost(depositUrl, map);
	    System.out.println("查询账户收支明细-String返回值："+resultStr);
	    return null;
	}
	
    /** 解析  */
	private Map<String, GeneralModel> analysisDetail(String data){
		if("".equals(data) || null == data) {
			
		}
		return null;
	}
}
