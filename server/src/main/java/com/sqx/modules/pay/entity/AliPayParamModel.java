package com.sqx.modules.pay.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author WALKMAN
 * @Description 支付宝订单参数
 **/
@Data
@ApiModel("支付宝订单参数")
public class AliPayParamModel {

    /**
     * 系统交易订单号
     */
    @ApiModelProperty("系统交易订单号")
    private String out_trade_no;

    /**
     * 订单金额
     */
    @ApiModelProperty("订单金额")
    private String total_amount;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String subject;

    /**
     * 商品标签-固定值
     */
    @ApiModelProperty("商品标签-固定值")
    private String product_code;


}
