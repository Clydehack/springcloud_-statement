package com.uzone.settlement.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName FeeModel
 * @Description 被第三方收取的手续费对账 -- 特殊对账
 * @Author 赵丹青
 * @Date 2019/6/18 14:25
 * @Version 1.0
 * @copyright: Copyright(c) 2019 Uzone Co. Ltd. All rights resrved.
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("手续费对账，第三方公司向用户收取的手续费，不在交易业务对账的范畴内，故单独提出此数据对账")
public class FeeModel {
	@ApiModelProperty(value = "通联订单号", required = true)
	private String allinpay_order_no;
	@ApiModelProperty(value = "订单类型 - 类型统一为：充值、提现、转账", required = true)
	private String transfer_type;
	@ApiModelProperty(value = "交易金额", required = true)
	private String amount;
	@ApiModelProperty(value = "有容向通联交纳的手续费", required = true)
	private String fee;
	@ApiModelProperty(value = "有容的创建时间 - 通联的交易时间 (注：小心订单时间和支付时间有间隔，就得填坑了)", required = true)
	private String create_time;
	@ApiModelProperty(value = "商户订单号，有容指定的订单号", required = true)
	private String biz_order_no;
	@ApiModelProperty(value = "支付类型 - 网关支付、微信支付、支付宝支付、账户内转账等", required = true)
	private String pay_method;
}
