package com.uzone.settlement.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName GeneralModel
 * @Description 基本对账
 * @Author zhaodanqing
 * @Date 2019/6/18 14:25
 * @Version 1.0
 * @copyright: Copyright(c) 2019 Uzone Co. Ltd. All rights resrved.
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("基本对账类型，所有账单都转为此类型进行对账")
public class GeneralModel {
	
	@ApiModelProperty(value = "商户订单号，有容指定的订单号", required = true)
	private String bizOrderNo;
	
	@ApiModelProperty(value = "交易金额", required = true)
	private Long transAmount;
	
	@ApiModelProperty(value = "现有金额", required = true)
	private Long balanceAmount;
	
	@ApiModelProperty(value = "订单创建时间", required = true)
	private String createTime;
}
