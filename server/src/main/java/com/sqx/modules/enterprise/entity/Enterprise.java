package com.sqx.modules.enterprise.entity;

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
 * @TableName enterprise
 */
@Data
@ApiModel("企业信息")
public class Enterprise implements Serializable {
    /**
     * 企业信息id
     */
    @ApiModelProperty("企业信息id")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String enterpriseName;

    /**
     * 企业所在位置
     */
    @ApiModelProperty("企业所在位置")
    private String companyAddress;

    /**
     * 法人
     */
    @ApiModelProperty("法人")
    private String legalPerson;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 注册资金
     */
    @ApiModelProperty("注册资金")
    private String registeredCapital;

    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registrationTime;

    /**
     * 营业开始时间
     */
    @ApiModelProperty("营业开始时间")
    @JsonFormat(pattern = "HH:mm")
    @DateTimeFormat(pattern = "HH:mm")
    private Date startTime;

    /**
     * 营业结束时间
     */
    @ApiModelProperty("营业结束时间")
    @JsonFormat(pattern = "HH:mm")
    @DateTimeFormat(pattern = "HH:mm")
    private Date endTime;

    /**
     * 营业执照正面照片
     */
    @ApiModelProperty("营业执照正面照片")
    private String frontPhotoBusinessLicense;

    /**
     * 营业执照反面照片
     */
    @ApiModelProperty("营业执照反面照片")
    private String backPhotoBusinessLicense;

    /**
     * 审批状态：0待审核 1审核通过 2已拒绝
     */
    @ApiModelProperty("审批状态：0待审核 1审核通过 2已拒绝")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人id
     */
    @ApiModelProperty("创建人id")
    private Long createUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 修改人id
     */
    @ApiModelProperty("修改人id")
    private Long updateUserId;

    /**
     * 实名认证状态：0未认证  1已认证
     */
    @ApiModelProperty("实名认证状态：0未认证  1已认证")
    private Integer isReal;

    /**
     * 审核理由
     */
    @ApiModelProperty("审核理由")
    private String auditReason;

    /**
     * 纳税号
     */
    @ApiModelProperty("纳税号")
    private String creditCode;

//    /**
//     * 是签署还是审批： 0签署  1审批
//     */
//    private Integer signOrApproved;

    private static final long serialVersionUID = 1L;
}