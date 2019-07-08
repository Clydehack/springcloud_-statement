package com.uzone.settlement.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName GeneralLedgerModel
 * @Description 统一总账模型，所有账单都转为此类型进行对账
 * @Author 赵丹青
 * @Date 2019/6/18 14:25
 * @Version 1.0
 * @copyright: Copyright(c) 2019 Uzone Co. Ltd. All rights resrved.
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("统一总账模型，所有账单都转为此类型进行对账")
public class GeneralLedgerModel {
	@ApiModelProperty(value = "通联订单号", required = true)
	private String allinpay_order_no;
	@ApiModelProperty(value = "订单类型 - 类型统一为：充值、提现、转账", required = true)
	private String transfer_type;
	@ApiModelProperty(value = "交易金额", required = true)
	private String trans_amount;
	@ApiModelProperty(value = "有容向用户收取的手续费", required = true)
	private String fee;
	@ApiModelProperty(value = "有容的创建时间 - 通联的交易时间 (注：小心订单时间和支付时间有间隔，就得填坑了)", required = true)
	private String create_time;
	@ApiModelProperty(value = "商户订单号，有容指定的订单号", required = true)
	private String biz_order_no;
	@ApiModelProperty(value = "支付类型 - 网关支付、微信支付、支付宝支付、账户内转账等", required = true)
	private String pay_method;
}
