package com.uzone.settlement.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName FeeModel
 * @Description 手续费对账
 * @Author 赵丹青
 * @Date 2019/6/18 14:25
 * @Version 1.0
 * @copyright: Copyright(c) 2019 Uzone Co. Ltd. All rights resrved.
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("手续费对账，网关支付时(GATEWAY_IN)，通联向有容收取的手续费，不在通联指定的交易业务对账的范畴内，故单独提出此数据对账")
public class FeeModel {
	@ApiModelProperty(value = "商户订单号，有容指定的订单号", required = true)
	private String bizOrderNo;
	@ApiModelProperty(value = "交易金额", required = true)
	private String amount;
	@ApiModelProperty(value = "有容向通联交纳的手续费", required = true)
	private String fee;
//	@ApiModelProperty(value = "订单创建时间", required = true)
//	private String createTime;
}
