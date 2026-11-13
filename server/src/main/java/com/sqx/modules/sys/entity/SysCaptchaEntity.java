package com.sqx.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 系统验证码
 *
 */
@Data
@TableName("sys_captcha")
@ApiModel("系统验证码")
public class SysCaptchaEntity {
    @TableId(type = IdType.INPUT)
    @ApiModelProperty("验证码uuid")
    private String uuid;
    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    private String code;
    /**
     * 过期时间
     */
    @ApiModelProperty("过期时间")
    private Date expireTime;

}
