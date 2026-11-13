package com.sqx.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 合同模板表
 * @TableName contract_template
 */
@Data
@ApiModel("合同模板表")
public class ContractTemplate implements Serializable {
    /**
     * 合同模板id
     */
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("合同模板id")
    private Long id;

    /**
     * 合同编号
     */
    @ApiModelProperty("合同编号")
    private String conNumber;

    /**
     * 合同模板名称
     */
    @ApiModelProperty("合同模板名称")
    private String templateName;

    /**
     * 合同模板文档
     */
    @ApiModelProperty("合同模板文档")
    private String contractUrl;

    /**
     * 合同模板图片
     */
    @ApiModelProperty("合同模板图片")
    private String contractImg;

    /**
     * 模版类型id
     */
    @ApiModelProperty("模版类型id")
    private Long typeId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人id
     */
    @ApiModelProperty("创建人id")
    private Long createUserId;

    /**
     * 修改人id
     */
    @ApiModelProperty("修改人id")
    private Long updateUserId;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 合同模板额外字段名
     */
    @TableField(exist = false)
    @ApiModelProperty("合同模板额外字段名")
    private String fieldName;

    /**
     * 合同模板额外字段值
     */
    @TableField(exist = false)
    @ApiModelProperty("合同模板额外字段值")
    private String filedValue;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @TableField(exist = false)
    private Long userId;

    @TableField(exist = false)
    @ApiModelProperty("合同模板额外字段列表")
    private List<ContractTemplateFields> templateFields;

    private static final long serialVersionUID = 1L;

}