package com.sqx.modules.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @TableName pay_classify
 */
@TableName(value ="pay_classify")
@Data
@ApiModel("合同签署套餐")
public class PayClassify implements Serializable {
    /**
     * 充值分类id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("合同签署套餐id")
    private Integer payClassifyId;

    /**
     * 售价
     */
    @ApiModelProperty("售价")
    private BigDecimal price;

    /**
     * 合同签署套餐名称
     */
    @ApiModelProperty("合同签署套餐名称")
    private String executed;

    /**
     * 份数
     */
    @ApiModelProperty("份数")
    private Integer numberSign;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 时间
     */
    @ApiModelProperty("时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}