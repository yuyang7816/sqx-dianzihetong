package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_money_details")
@ApiModel("钱包详情")
public class UserMoneyDetails implements Serializable {
    /**
     * 钱包详情id
     */
    @ApiModelProperty("钱包详情id")
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty("用户id")
    private Long userId;
    /**
     * 对应用户id
     */
    @TableField("by_user_id")
    @ApiModelProperty("对应用户id")
    private Long byUserId;
    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;
    /**
     * 1注册   2购买 3提现 5购买签署合同套餐 6佣金分成
     */
    @ApiModelProperty("1注册  2购买 3提现 5购买签署合同套餐 6佣金分成")
    private Integer classify;
    /**
     * 类型 1增加 2减少
     */
    @ApiModelProperty("类型 1增加 2减少")
    private Integer type;
    /**
     * 状态 1待支付 2已到账 3取消
     */
    @ApiModelProperty("状态 1待支付 2已到账 3取消")
    private Integer state;
    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private BigDecimal money;
    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private String createTime;


    /**
     * 下级等级(1:一级用户 2:二级用户)
     */
    @ApiModelProperty(value = "下级等级(1:一级用户 2:二级用户)")
    private Integer userLevel;

    /**
     * 关联id
     */
    @ApiModelProperty(value = "关联id")
    private Long relationId;

}
