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

/**
 * 发起人和合同关联表
 *
 * @TableName contract_and_user
 */
@Data
@ApiModel("发起人和合同关联表")
public class ContractAndUser implements Serializable {
    /**
     * 发起人和合同关联表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("发起人和合同关联表id")
    private Long id;

    /**
     * 模板合同、草稿箱合同id
     */
    @ApiModelProperty("模板合同、草稿箱合同id")
    private Long contractId;

    /**
     * 发起人id
     */
    @ApiModelProperty("发起人id")
    private Long userId;

    /**
     * 发起人姓名(或者自己的企业名称)
     */
    @ApiModelProperty("发起人姓名")
    private String userName;

    /**
     * 发起人电话
     */
    @ApiModelProperty("发起人电话")
    private String userPhone;

    /**
     * 发起人头像
     */
    @TableField(exist = false)
    @ApiModelProperty("发起人头像")
    private String userAvatar;

    /**
     * 自己填写自己的企业法人
     */
    @ApiModelProperty("自己填写自己的企业法人")
    private String userLegalPerson;

    /**
     * 自己填写的对方的企业法人
     */
    @ApiModelProperty("自己填写的对方企业的法人")
    private String signLegalPerson;

    /**
     * 发起人身份证
     */
    @ApiModelProperty("发起人身份证号")
    private String idCard;

    /**
     * 签署人或审批人或收件人id
     */
    @ApiModelProperty("签署人或审批人id")
    private Long signedBy;

    /**
     * 签署人名称(或者对方企业名称)
     */
    @ApiModelProperty("签署人名称")
    private String signUserName;

    /**
     * 签署人身份证号
     */
    @ApiModelProperty("签署人身份证号")
    private String idCardNumber;

    /**
     * 签署人手机号
     */
    @ApiModelProperty("签署人手机号")
    private String signUserPhone;

    /**
     * 签署人头像
     */
    @TableField(exist = false)
    @ApiModelProperty("签署人头像")
    private String signUserHeadImg;

    /**
     * 0 未签署 1未审批 2已签署  3已审批 4已过期
     */
    @ApiModelProperty("0 未签署 1未审批 2已签署  3已审批 4已过期")
    private Integer status;

    /**
     * 是否加入草稿箱：0未加入  1已加入
     */
    @ApiModelProperty("是否加入草稿箱：0未加入  1已加入")
    private Integer draftStatus;

    /**
     * 合同开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("合同开始时间")
    private Date startTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUserId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人id
     */
    @ApiModelProperty("修改人id")
    private Long updateUserId;

    /**
     * 合同结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 是签署还是审批： 0签署  1审批 2不签署
     */
    @ApiModelProperty("是签署还是审批： 0签署  1审批 2不签署")
    private Integer signOrApproved;

    /**
     * 抄送人id
     */
    @ApiModelProperty("抄送人id")
    private Long copyUser;

    /**
     * 合同类型：1企业  2个人
     */
    @ApiModelProperty("合同类型：1企业  2个人")
    private Integer type;

    /**
     * 合同类型：1企业 2个人
     */
    @ApiModelProperty("合同类型：1企业 2个人")
    private Integer agreementType;

    /**
     * 签章或审批或公司章图片
     */
    @ApiModelProperty("签章或审批或公司章图片")
    private String signUrl;

    /**
     * 签章或审批或公司的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("签章或审批或公司的时间")
    private Date signDate;

    /**
     * 添加的最晚审批或签署的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("添加的最晚审批或签署的时间")
    private Date lastDate;

    /**
     * 微信发起的合同文件
     */
    @ApiModelProperty("微信发起的合同文件")
    private String documentsUrl;

    /**
     * 微信发起的附件
     */
    @ApiModelProperty("微信发起的附件")
    private String annexUrl;

    /**
     * 收件人姓名
     */
    @ApiModelProperty("收件人姓名")
    private String receivingUserName;

    /**
     * 收件人邮箱
     */
    @ApiModelProperty("收件人邮箱")
    private String receivingEmail;

    /**
     * 是否签署：0未签署  1签署
     */
    @ApiModelProperty("是否签署：0未签署  1签署")
    private Integer isSign;

    /**
     * 抄送人名称
     */
    @ApiModelProperty("抄送人名称")
    private String copyUserName;

    /**
     * 抄送人头像
     */
    @ApiModelProperty("抄送人头像")
    @TableField(exist = false)
    private String copyUserHeadImg;

    /**
     * 甲乙方标识：1甲方  2乙方
     */
    @ApiModelProperty("甲方乙方标识：1甲方  2乙方")
    private Integer isPrincipal;


    private static final long serialVersionUID = 1L;
}