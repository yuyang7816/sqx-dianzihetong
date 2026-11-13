package com.sqx.modules.sys.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录表单
 *
 */
@Data
@ApiModel("登录表单")
public class SysLoginForm {
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("验证码")
    private String captcha;
    @ApiModelProperty("验证码uuid")
    private String uuid;


}
