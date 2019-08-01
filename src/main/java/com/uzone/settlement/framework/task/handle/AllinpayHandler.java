package com.uzone.settlement.framework.task.handle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uzone.settlement.framework.util.AccessInterface;
import com.uzone.settlement.framework.util.CustomException;
import com.uzone.settlement.framework.util.RedisUtil;
import com.uzone.settlement.model.GeneralModel;

/**
 * 拉取通联的数据，放入redis
 */
@Configuration
public class AllinpayHandler {

	/** 通联支付接口 */
    @Value("${uzone.allinpay.baseUrl}")
    private String baseUrl;
	
    @Autowired
	RedisUtil redisUtil;
    
    public List<GeneralModel> processingTask(String date, String key, List<String> userIdList) throws CustomException{
    	List<GeneralModel> allinpay = new ArrayList<>();
    	for (String userId : userIdList) {
    		List<GeneralModel> allinpayBizOrderNo = queryDetail(date, "uzone" + userId);
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
    private List<GeneralModel> queryDetail(String date, String userId) throws CustomException{
    	List<GeneralModel> list = new ArrayList<>();
	    String depositUrl = baseUrl + "/order/accountRevenueandExpenditureDetail";
	    int i = 1,j = 20;
	    while(true) {
	    	Map<String,String> map = new HashMap<>();
	    	map.put("bizUserId", userId);
	    	map.put("accountSetNo", "");
	    	map.put("dateStart", date);
	    	map.put("dateEnd", date);
	    	map.put("startPosition", i + "");
	    	map.put("queryNum", j + "");
	    	String resultStr = AccessInterface.sendPost(depositUrl, map);
	    	System.out.println("查询账户收支明细-String返回值：" + resultStr);
	    	if("".equals(resultStr) || null == resultStr) {throw new CustomException("21", "调用通联API返回值空");}
	    	
	    	List<GeneralModel> lgm = analysisDetail(resultStr);
	    	list.addAll(lgm);
	    	if(lgm.size() < 20) {
	    		break;
	    	}
	    	i = i + j;
	    	j = j + 20;
	    }
	    return list;
	}

    /** 负责解析API应答  */
	private List<GeneralModel> analysisDetail(String resultStr){
		List<GeneralModel> list = new ArrayList<>();
		JSONObject obj = JSON.parseObject(resultStr);
		JSONArray array = obj.getJSONArray("inExpDetail");
		for (int i = 0; i < array.size(); i++) {
			GeneralModel gm = new GeneralModel();
            JSONObject jo = array.getJSONObject(i);
            gm.setBizOrderNo(jo.getString("bizOrderNo"));
            gm.setTransAmount(jo.getLongValue("chgAmount"));
            gm.setBalanceAmount(jo.getLongValue("curAmount"));
            gm.setCreateTime(jo.getString("changeTime"));
            list.add(gm);
		}
		return list;
	}
}
