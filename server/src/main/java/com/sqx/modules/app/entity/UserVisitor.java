package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description user_visitor
 * @author liyuan
 * @date 2021-08-23
 */
@Data
@ApiModel("用户访客相关")
public class UserVisitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 访客id
     */
    @ApiModelProperty("访客id")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 访问用户id
     */
    @ApiModelProperty("访问用户id")
    private Long byUserId;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private String updateTime;

    public UserVisitor() {}
}
