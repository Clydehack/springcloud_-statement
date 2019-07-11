package com.uzone.settlement.framework.task.handle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.uzone.settlement.framework.util.AccessInterface;
import com.uzone.settlement.framework.util.CustomException;
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
    
    public List<GeneralModel> processingTask(String date, String key, List<String> userIdList) throws CustomException{
    	List<GeneralModel> allinpay = new ArrayList<>();
    	for (String userId : userIdList) {
    		List<GeneralModel> allinpayBizOrderNo = queryDetail(userId);
    		allinpay.addAll(allinpayBizOrderNo);
		}
    	
    	/** 
    	 * 对账数据下载成功，放入redis
    	 */
    	long flag = redisUtil.sSet(key, allinpay);
    	if(flag != allinpay.size()) {
    		// 如果拉取的帐单数，和存入redis的帐单数不相符，重新来一遍，目前直接抛异常去清洗等待定时重来吧
    		throw new CustomException("22", "本地账单数量和redis加载的账单数量不一样");
    	}
		return allinpay;
    }
    
    /** 负责调用通联API - 4.2.22 查询账户收支明细 */
    private List<GeneralModel> queryDetail(String userId) throws CustomException{
	    String depositUrl = baseUrl + "/order/accountRevenueandExpenditureDetail";
	    int i = 20;
//	    for(;) {
//	    	
//	    }
	    
	    Map<String,String> map = new HashMap<>();
	    map.put("bizUserId", userId);
	    map.put("accountSetNo", "");
	    map.put("dateStart", "");
	    map.put("dateEnd", "");
	    map.put("startPosition", "");
	    map.put("queryNum", "");
	    String resultStr = AccessInterface.sendPost(depositUrl, map);
	    System.out.println("查询账户收支明细-String返回值：" + resultStr);
	    if("".equals(resultStr) || null == resultStr) {throw new CustomException("21", "调用通联API返回值空");}
	    
	    List<GeneralModel> listUserIdGeneralModel = new ArrayList<>();
	    boolean ok = analysisDetail(resultStr, listUserIdGeneralModel);
	    
	    return null;
	}
	
    /** 负责解析API应答  */
	private boolean analysisDetail(String data, List<GeneralModel> listUserIdGeneralModel){
		return true;
	}
}
