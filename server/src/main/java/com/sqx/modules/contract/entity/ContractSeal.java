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
 * 
 * @TableName contract_seal
 */
@Data
@ApiModel("合同印章")
public class ContractSeal implements Serializable {
    /**
     * 合同印章id
     */
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("合同印章id")
    private Long id;

    /**
     * 个人印章路径
     */
    @ApiModelProperty("个人印章路径")
    private String sealUrl;

    /**
     * 个人签名路径
     */
    @ApiModelProperty("个人签名路径")
    private String signUrl;


    /**
     * 公司印章路径
     */
    @ApiModelProperty("公司印章路径")
    private String companySeal;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    @ApiModelProperty("用户名称")
    private String userName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 1手动签名 2系统生成签名 3全部
     */
    @ApiModelProperty("1手动签名 2系统生成签名 3全部")
    private Integer signType;

    private static final long serialVersionUID = 1L;
}