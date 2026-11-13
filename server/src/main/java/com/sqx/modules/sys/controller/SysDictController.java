package com.sqx.modules.sys.controller;

import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.common.validator.ValidatorUtils;
import com.sqx.modules.sys.entity.SysDictEntity;
import com.sqx.modules.sys.service.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 数据字典
 *
 */
@RestController
@RequestMapping("sys/dict")
@Api(value = "管理端-数据字典",tags = {"管理端-数据字典"})
public class SysDictController {
    @Autowired
    private SysDictService sysDictService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:dict:list")
    @ApiOperation("数据字典列表")
    public Result list(@ApiParam("name,可传可不传")@RequestParam Map<String, Object> params){
        PageUtils page = sysDictService.queryPage(params);

        return Result.success().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:dict:info")
    @ApiOperation("数据字典信息")
    public Result info(@ApiParam("id")@PathVariable("id") Long id){
        SysDictEntity dict = sysDictService.getById(id);

        return Result.success().put("dict", dict);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:dict:save")
    @ApiOperation("数据字典保存")
    public Result save(@RequestBody SysDictEntity dict){
        //校验类型
        ValidatorUtils.validateEntity(dict);

        sysDictService.save(dict);

        return Result.success();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:dict:update")
    @ApiOperation("数据字典修改")
    public Result update(@RequestBody SysDictEntity dict){
        //校验类型
        ValidatorUtils.validateEntity(dict);

        sysDictService.updateById(dict);

        return Result.success();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:dict:delete")
    @ApiOperation("数据字典删除")
    public Result delete(@RequestBody Long[] ids){
        sysDictService.removeByIds(Arrays.asList(ids));

        return Result.success();
    }

}
