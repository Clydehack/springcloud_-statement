package com.uzone.settlement.framework.task.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.uzone.settlement.model.BuCheckaccountLogPO;
import com.uzone.settlement.model.GeneralModel;

@Mapper
public interface DispatchTaskMapper {
	
	/** 查询对账日志 */
	@Select("select * from bu_checkaccount_log where checkaccount_time = #{date}")
	BuCheckaccountLogPO queryLog(@Param("date") String date);
	
	/** 取出本地昨日参与交易的userId */
	@Select("select user_id from fin_cash_acc where date_format(create_time,'%Y-%m-%d') = #{yesterday} "// 现金表明细
			+ "union "
			+ "select user_id from fin_bill_acc where date_format(create_time,'%Y-%m-%d') = #{yesterday} "// 汇票表明细
			+ "union "
			+ "select user_id from fin_listing_acc where date_format(create_time,'%Y-%m-%d') = #{yesterday} "// 挂牌汇票表明细
			+ "group by user_id "
			+ "order by user_id "
			+ "desc")
	List<String> queryUserId(@Param("yesterday") String yesterday);
	
	/** 取出本地昨日的交易数据 TODO ①需要判断借+贷-方向 ②要查所有明细表才行 ③通联的余额是咱们的什么余额？汇票实质上已经成为有容的余额了吧？ ④sql查出的数据优点乱，暂时未理明白 */
	@Select("select user_id, biz_order_no as bizOrderNo, trans_amount as transAmount, balance as balanceAmount, create_time as createTime "
			+ " from fin_cash_acc where date_format(create_time,'%Y-%m-%d') = #{yesterday} and user_id = #{userId} "// 现金表明细
			+ "union "
			+ "select user_id, biz_order_no as bizOrderNo, trans_amount as transAmount, bill_balance as balanceAmount, create_time as createTime "
			+ " from fin_bill_acc where date_format(create_time,'%Y-%m-%d') = #{yesterday} and user_id = #{userId} "// 汇票表明细
			+ "union "
			+ "select user_id, biz_order_no as bizOrderNo, trans_amount as transAmount, bill_balance as balanceAmount, create_time as createTime "
			+ " from fin_listing_acc where date_format(create_time,'%Y-%m-%d') = #{yesterday} and user_id = #{userId} "// 挂牌汇票表明细
			+ "group by user_id "
			+ "order by user_id, createTime "
			+ "desc")
	List<GeneralModel> queryLocalData(@Param("yesterday") String yesterday, @Param("userId") String userId);
}
