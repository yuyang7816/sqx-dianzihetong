package com.sqx.modules.contract.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/8/15 11:40
 */
@Data
@ApiModel("合同签署VO")
public class ContractSignVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private Long contractId;

    /**
     * 合同模板id
     */
    @ApiModelProperty("合同模板id")
    private Long templateId;

    /** 列名称 */
    @ApiModelProperty("列名称")
    private String columnName;

    /** 列描述 */
    @ApiModelProperty("列描述")
    private String columnComment;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 合同编号
     */
    @ApiModelProperty("合同编号")
    private String contractNumber;

    /**
     * 1模板创建  2微信创建  3草稿箱创建
     */
    @ApiModelProperty("1模板创建  2微信创建  3草稿箱创建")
    private Integer type;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 是签署还是审批： 0签署  1审批
     */
    @ApiModelProperty("是签署还是审批： 0签署  1审批")
    private Integer signOrApproved;

    /**
     * 签章或审批或公司章图片
     */
    @ApiModelProperty("签章或审批或公司章图片")
    private String signUrl;

    /**
     * 生成pdf后转的图片路径
     */
    @ApiModelProperty("生成pdf后转的图片路径")
    private String pdfImageUrl;

}
