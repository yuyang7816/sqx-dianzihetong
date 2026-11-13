package com.sqx.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName contract_additional_fields
 */
@Data
@ApiModel("合同实例额外表额外字段")
public class ContractAdditionalFields implements Serializable {
    /**
     * 额外字段id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 合同实例表额外字段名
     */
    @ApiModelProperty("合同实例表额外字段名")
    private String listedName;

    /**
     * 合同实例表额外字段值
     */
    @ApiModelProperty("合同实例表额外字段值")
    private String columnComment;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "创建时间")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}