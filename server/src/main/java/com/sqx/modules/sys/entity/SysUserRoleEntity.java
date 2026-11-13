package com.sqx.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户与角色对应关系
 *
 */
@Data
@TableName("sys_user_role")
@ApiModel("用户与角色对应关系")
public class SysUserRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId
	@ApiModelProperty("用户与角色对应关系id")
	private Long id;

	/**
	 * 用户ID
	 */
	@ApiModelProperty("用户id")
	private Long userId;

	/**
	 * 角色ID
	 */
	@ApiModelProperty("角色id")
	private Long roleId;

	
}
