package com.sqx.modules.sys.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 密码表单
 *
 */
@Data
@ApiModel("密码表单")
public class PasswordForm {
    /**
     * 原密码
     */
    @ApiModelProperty("原密码")
    private String password;
    /**
     * 新密码
     */
    @ApiModelProperty("新密码")
    private String newPassword;

}
