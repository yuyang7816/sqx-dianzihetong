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
 * 签署或审批人与合同相关表
 * @TableName contract_and_signed
 */
@Data
@ApiModel("签署或审批人与合同相关表")
public class ContractAndSigned implements Serializable {
    /**
     * 签署或审批人与合同相关表id
     */
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("签署或审批人与合同相关表id")
    private Integer id;

    /**
     * 模板合同、微信合同、草稿箱合同id
     */
    @ApiModelProperty("模板合同、微信合同、草稿箱合同id")
    private Integer contractId;

    /**
     * 签署人或审批人id
     */
    @ApiModelProperty("签署人或审批人id")
    private Integer signedBy;

    /**
     * 0 未签署 1未审批 2已签署  3已审批
     */
    @ApiModelProperty("0 未签署 1未审批 2已签署  3已审批")
    private Integer status;

    /**
     * 合同开始时间
     */
    @ApiModelProperty("合同开始时间")
    private Date startTime;

    /**
     * 合同结束时间
     */
    @ApiModelProperty("合同结束时间")
    private Date endTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 是签署还是审批： 0签署  1审批
     */
    @ApiModelProperty("是签署还是审批： 0签署  1审批")
    private Integer signOrApproved;

    /**
     * 抄送人id
     */
    @ApiModelProperty("抄送人id")
    private Integer copyUser;

    private static final long serialVersionUID = 1L;
}