package com.sqx.modules.enterprise.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业和用户关联表
 * @TableName enterprise_and_user
 */
@Data
@ApiModel("企业和用户关联表")
public class EnterpriseAndUser implements Serializable {
    /**
     * 企业和用户关联表id
     */
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("企业和用户关联表id")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    private Long enterpriseId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}