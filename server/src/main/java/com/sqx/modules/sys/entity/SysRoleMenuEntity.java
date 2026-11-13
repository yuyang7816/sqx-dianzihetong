package com.sqx.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色与菜单对应关系
 *
 */
@Data
@TableName("sys_role_menu")
@ApiModel("角色与菜单对应关系")
public class SysRoleMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	@ApiModelProperty("角色与菜单对应关系ID")
	private Long id;

	/**
	 * 角色ID
	 */
	@ApiModelProperty("角色id")
	private Long roleId;

	/**
	 * 菜单ID
	 */
	@ApiModelProperty("菜单id")
	private Long menuId;
	
}
