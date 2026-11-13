package com.sqx.modules.sys.controller;

import com.sqx.common.annotation.SysLog;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.common.validator.ValidatorUtils;
import com.sqx.modules.sys.entity.SysRoleEntity;
import com.sqx.modules.sys.service.SysRoleMenuService;
import com.sqx.modules.sys.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 */
@RestController
@RequestMapping("/sys/role")
@Api(value = "管理端-角色管理",tags = "管理端-角色管理")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	/**
	 * 角色列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:role:list")
	@ApiOperation("角色列表")
	public Result list(@ApiParam("参数:roleName,createUserId,可传可不传") @RequestParam Map<String, Object> params){
		//如果不是超级管理员，则只查询自己创建的角色列表
		/*if(getUserId() != Constant.SUPER_ADMIN){
			params.put("createUserId", getUserId());
		}*/
		PageUtils page = sysRoleService.queryPage(params);
		return Result.success().put("page", page);
	}

	/**
	 * 角色列表
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:role:select")
	@ApiOperation("角色列表")
	public Result select(){
		Map<String, Object> map = new HashMap<>();

		//如果不是超级管理员，则只查询自己所拥有的角色列表
		/*if(getUserId() != Constant.SUPER_ADMIN){
			map.put("create_user_id", getUserId());
		}*/
		List<SysRoleEntity> list = (List<SysRoleEntity>) sysRoleService.listByMap(map);

		return Result.success().put("list", list);
	}

	/**
	 * 角色信息
	 */
	@GetMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	@ApiOperation("角色信息")
	public Result info(@ApiParam("角色id")@PathVariable("roleId") Long roleId){
		SysRoleEntity role = sysRoleService.getById(roleId);

		//查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);

		return Result.success().put("role", role);
	}

	/**
	 * 保存角色
	 */
	@SysLog("保存角色")
	@PostMapping("/save")
	@RequiresPermissions("sys:role:save")
	@ApiOperation("保存角色")
	public Result save(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);

		role.setCreateUserId(getUserId());
		sysRoleService.saveRole(role);

		return Result.success();
	}

	/**
	 * 修改角色
	 */
	@SysLog("修改角色")
	@PostMapping("/update")
	@RequiresPermissions("sys:role:update")
	@ApiOperation("修改角色")
	public Result update(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);

		role.setCreateUserId(getUserId());
		sysRoleService.update(role);

		return Result.success();
	}

	/**
	 * 删除角色
	 */
	@SysLog("删除角色")
	@PostMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	@ApiOperation("删除角色")
	public Result delete(@RequestBody Long[] roleIds){
		sysRoleService.deleteBatch(roleIds);

		return Result.success();
	}
}
