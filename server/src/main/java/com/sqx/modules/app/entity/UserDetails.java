package com.sqx.modules.app.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("用户详情")
public class UserDetails {
    /**
     * 本月订单数量
      */
    @ApiModelProperty("本月订单数量")
    private int monthlyOrderNum;
    /**
     * 本月充值金额
     */
    @ApiModelProperty("本月充值金额")
    private BigDecimal monthlyRechargeMoney;
    /**
     *本月提现数量
     */
    @ApiModelProperty("本月提现数量")
    private int monthWithdrawalNum;
    /**
     * 本月提现金额
     */
    @ApiModelProperty("本月提现金额")
    private  BigDecimal monthlyWithdrawalMoney;

    //用户详情id
    @ApiModelProperty("用户详情id")
    private Long id;
    //用户id
    @ApiModelProperty("用户id")
    private Long userId;
    //1用户足迹 2收藏
    @ApiModelProperty("1用户足迹 2收藏")
    private Integer classify;
    //订单id
    @ApiModelProperty("订单id")
    private Long orderId;
    //创建时间
    @ApiModelProperty("创建时间")
    private String createTime;


}
