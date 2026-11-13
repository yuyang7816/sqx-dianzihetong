package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fang
 * @date 2020/7/10
 */
@Data
@TableName("msg")
@ApiModel("短信验证码")
public class Msg implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("手机号")
    private String phone;


}