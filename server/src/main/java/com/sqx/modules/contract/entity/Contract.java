package com.sqx.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.modules.contract.vo.DataJsonVo;
import com.sqx.modules.utils.typeHandler.DataJsonListTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @TableName contract
 */
@Data
@ApiModel("合同")
@TableName(autoResultMap = true)//是否构建自定义typeHandler,否则List<Object>无法自动转为字符串
public class Contract implements Serializable {
    /**
     * 合同id
     */
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("合同id")
    private Long id;

    /**
     * 合同模板id
     */
    @ApiModelProperty(name = "合同模板id")
    private Long templateId;

    /**
     * 合同名称
     */
    @ApiModelProperty(name = "合同名称")
    private String contractName;

    /**
     * 合同编号
     */
    @ApiModelProperty(name = "合同编号")
    private String contractNumber;

    /**
     * 1模板创建  2微信创建  3草稿箱创建 4个人签署合同
     */
    @ApiModelProperty(name = "1模板创建  2微信创建  3草稿箱创建  4个人签署合同")
    private Integer contractType;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(exist = false)
    @ApiModelProperty(name = "发起人和合同关联列表")
    private List<ContractAndUser> contractAndUsers;



    @TableField(exist = false)
    @ApiModelProperty(name = "合同扩展字段列表")
    private List<ContractAdditionalFields> contractAdditionalFields;

//    @TableField(typeHandler = DataJsonListTypeHandler.class)
    @ApiModelProperty(name = "其他字段")
    private String dataJson;

    /**
     * 合同模板图片
     */
    @ApiModelProperty("合同模板图片")
    @TableField(exist = false)
    private String templateUrl;


    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String annexUrl;

    /**
     * 上传的文件路径
     */
    @ApiModelProperty("上传的文件路径")
    private String fileUrl;

    /**
     * 发起人名称
     */
    @TableField(exist = false)
    @ApiModelProperty("发起人名称")
    private String userName;

    /**
     * 发起人头像
     */
    @TableField(exist = false)
    @ApiModelProperty("发起人头像")
    private String userAvatar;

    /**
     * 签署人名称
     */
    @TableField(exist = false)
    @ApiModelProperty("签署人名称")
    private String signUserName;

    /**
     * 抄送人名称
     */
    @TableField(exist = false)
    @ApiModelProperty("抄送人名称")
    private String copyUserName;

    /**
     * 合同是否已签署完成：0未完成 1已完成 2已审批 3已过期
     */
    @ApiModelProperty("合同是否已签署完成：0未完成 1已完成 2已审批 3已过期")
    private Integer isComplete;

    /**
     * 合同是否加入草稿箱：0未加入  1已加入
     */
    @ApiModelProperty("合同是否加入草稿箱：0未加入  1已加入")
    private Integer draftStatus;

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


    /**
     * 设置合同扩展字段
     * dataJson
     */
//    public void setDataJson(List<DataJsonVo> dataJson) {
//        this.dataJson = dataJson;
//    }

    private static final long serialVersionUID = 1L;

}