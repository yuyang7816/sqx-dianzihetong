package com.sqx.modules.contract.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.modules.contract.entity.ContractAndUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/8/12 10:23
 */
@Data
@TableName(autoResultMap = true)//是否构建自定义typeHandler,否则List<Object>无法自动转为字符串
@ApiModel("合同表VO")
public class ContractVO extends ContractAndUser implements Serializable {

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

    /**
     * 合同模板word文档路径
     */
    @ApiModelProperty("合同模板word文档路径")
    private String templatePath;

    /**
     * 合同名称
     */
    @ApiModelProperty("合同名称")
    private String contractName;

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
    private Integer contractType;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

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
     * 签署人或审批人或收件人id
     */
    @ApiModelProperty("签署人或审批人或收件人id")
    private Long signedBy;


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
     * 是否签署：0不签署  1签署
     */
    @ApiModelProperty("是否签署：0不签署  1签署")
    private Integer isSign;

    /**
     * 添加的最晚审批或签署的时间
     */
    @ApiModelProperty("添加的最晚审批或签署的时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastDate;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("其他字段")
    private String dataJson;

    // 前端需要传过来的字段值,方式为："fieldValues": {
    //    "fieldName1": "value1",
    //    "fieldName2": "value2",
    //    "fieldName3": "value3"
    //  }这样才可以修改合同中额外的字段的值
    @ApiModelProperty("额外字段值")
    private Map<String, String> fieldValues; // 字段值映射

    public String getColumnComment(String fieldName) {
        // 返回对应字段名的值，如果找不到则返回默认值 ""
        return fieldValues.getOrDefault(fieldName, "");
    }


    /**
     * 是签署还是审批： 0签署  1审批 2不签署
     */
    @ApiModelProperty("是签署还是审批： 0签署  1审批 2不签署")
    private Integer signOrApproved;

    /**
     * 生成pdf后转的图片路径
     */
    @ApiModelProperty("生成pdf后转的图片路径")
    private String pdfImageUrl;

    /**
     * 最晚签署天数
     */
    @ApiModelProperty("最晚签署天数")
    private Integer lateSignDays;

    /**
     * 合同发起人姓名
     */
    @ApiModelProperty("合同发起人姓名")
    private String contractUserName;
}
