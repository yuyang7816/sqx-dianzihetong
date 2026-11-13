package com.sqx.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色
 *
 */
@Data
@TableName("sys_role")
@ApiModel("角色")
public class SysRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 角色ID
	 */
	@TableId
	@ApiModelProperty("角色id")
	private Long roleId;

	/**
	 * 角色名称
	 */
	@NotBlank(message="角色名称不能为空")
	@ApiModelProperty("角色名称")
	private String roleName;

	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;
	
	/**
	 * 创建者ID
	 */
	@ApiModelProperty("创建者id")
	private Long createUserId;

	@TableField(exist=false)
	@ApiModelProperty("菜单id列表")
	private List<Long> menuIdList;
	
	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	private Date createTime;

	
}
