package com.sqx.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 合同模板额外字段表
 * @TableName contract_template_fields
 */
@Data
@TableName(autoResultMap = true)//是否构建自定义typeHandler,否则List<Object>无法自动转为字符串
@ApiModel("合同模板额外字段表")
public class ContractTemplateFields implements Serializable {
    /**
     * 合同模板额外字段id
     */
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("合同模板额外字段id")
    private Integer id;

    /**
     * 合同模板id
     */
    @ApiModelProperty(name = "合同模板id")
    private Long templateId;

    /**
     * 合同额外字段名
     */
    @ApiModelProperty(name = "合同额外字段名")
    private String fieldName;

    /**
     * 合同额外字段值
     */
    @ApiModelProperty(name = "合同额外字段值")
    private String filedValue;

    /** 是否必填（1是） */
    @ApiModelProperty(name = "是否必填")
    private Integer isRequired;

    /** 显示类型（1文本框、2文本域、3下拉框、4复选框、5单选框、6日期控件） */
    @ApiModelProperty(name = "显示类型")
    private Integer htmlType;

    /** 选项值 */
    @ApiModelProperty(name = "选项值")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> optionJson;

    /** 展示类型 1仅用户端展示 2仅管理端展示 3都展示 */
    @ApiModelProperty(name = "展示类型 1仅用户端展示 2仅管理端展示 3都展示")
    private Integer showType;

    /** 排序 */
    @ApiModelProperty(name = "排序")
    private Integer sort;

    @ApiModelProperty(name = "列名称")
    private String columnName;

    /** 列描述 */
    @ApiModelProperty(name = "列描述")
    private String columnComment;

    /** 列长度 */
    @ApiModelProperty(name = "列长度")
    private Integer columnLength;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}