package com.sqx.modules.sys.controller;

import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.sys.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * 系统日志
 *
 */
@Controller
@RequestMapping("/sys/log")
@Api(value = "管理端-系统日志",tags = "管理端-系统日志")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("sys:log:list")
	@ApiOperation("系统日志列表")
	public Result list(@ApiParam("key(也是查的用户名),可传可不传")@RequestParam Map<String, Object> params){
		PageUtils page = sysLogService.queryPage(params);

		return Result.success().put("page", page);
	}
	
}
