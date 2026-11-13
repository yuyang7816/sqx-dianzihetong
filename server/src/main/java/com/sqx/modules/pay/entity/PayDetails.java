package com.sqx.modules.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *  充值记录
 * @author fang 2020-05-14
 */
@Data
@TableName("pay_details")
@ApiModel("支付记录")
public class PayDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 充值记录id
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty("支付记录id")
    private Long id;

    /**
     * 分类（1微信 1app微信 2微信公众号 3微信小程序 4app支付宝支付 5H5支付宝支付）
     */
    @ApiModelProperty("分类（1微信 1app微信 2微信公众号 3微信小程序 4app支付宝支付 5H5支付宝支付）")
    private Integer classify;

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private String orderId;

    /**
     * 支付宝交易订单号
     */
    @ApiModelProperty("支付宝交易订单号")
    private String tradeNo;

    /**
     * 充值金额
     */
    @ApiModelProperty("充值金额")
    private BigDecimal money;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 0待支付 1支付成功 2失败
     */
    @ApiModelProperty("0待支付 1支付成功 2失败")
    private Integer state;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createTime;

    /**
     * 支付时间
     */
    @ApiModelProperty("支付时间")
    private String payTime;

    /**
     * 支付类型 1 订单  2会员 3合同签署套餐
     */
    @ApiModelProperty("支付类型 1 订单  2会员 3合同签署套餐")
    private Integer type;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    @TableField(exist = false)
    @ApiModelProperty("退款内容")
    private String refundContent;

    @TableField(exist = false)
    @ApiModelProperty("商户退款单号")
    private String outRequestNo;

    /**
     * 关联id(如果是购买会员则是会员类型)
     */
    @ApiModelProperty("关联id(如果是购买会员则是会员类型)")
    private Long parentId;

}
