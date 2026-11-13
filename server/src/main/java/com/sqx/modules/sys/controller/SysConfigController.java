package com.sqx.modules.sys.controller;


import com.sqx.common.annotation.SysLog;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.common.validator.ValidatorUtils;
import com.sqx.modules.sys.entity.SysConfigEntity;
import com.sqx.modules.sys.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统配置信息
 *
 */
@RestController
@RequestMapping("/sys/config")
@Api(value = "管理端-系统配置信息",tags = "管理端-系统配置信息")
public class SysConfigController extends AbstractController {
	@Autowired
	private SysConfigService sysConfigService;

	/**
	 * 所有配置列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:config:list")
	@ApiOperation("所有配置列表")
	public Result list(@ApiParam("paramKey,可传可不传")@RequestParam Map<String, Object> params){
		PageUtils page = sysConfigService.queryPage(params);

		return Result.success().put("page", page);
	}


	/**
	 * 配置信息
	 */
	@GetMapping("/info/{id}")
	@RequiresPermissions("sys:config:info")
	@ApiOperation("配置信息")
	public Result info(@ApiParam("id") @PathVariable("id") Long id){
		SysConfigEntity config = sysConfigService.getById(id);

		return Result.success().put("config", config);
	}

	/**
	 * 保存配置
	 */
	@SysLog("保存配置")
	@PostMapping("/save")
	@RequiresPermissions("sys:config:save")
	@ApiOperation("保存配置")
	public Result save(@RequestBody SysConfigEntity config){
		ValidatorUtils.validateEntity(config);

		sysConfigService.saveConfig(config);

		return Result.success();
	}

	/**
	 * 修改配置
	 */
	@SysLog("修改配置")
	@PostMapping("/update")
	@RequiresPermissions("sys:config:update")
	@ApiOperation("修改配置")
	public Result update(@RequestBody SysConfigEntity config){
		ValidatorUtils.validateEntity(config);

		sysConfigService.update(config);

		return Result.success();
	}

	/**
	 * 删除配置
	 */
	@SysLog("删除配置")
	@PostMapping("/delete")
	@RequiresPermissions("sys:config:delete")
	@ApiOperation("删除配置")
	public Result delete(@RequestBody Long[] ids){
		sysConfigService.deleteBatch(ids);

		return Result.success();
	}

}
