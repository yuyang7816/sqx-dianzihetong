package com.sqx.modules.invite.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "管理端-邀请收益", tags = {"管理端-邀请收益"})
@RequestMapping(value = "/admin/invite")
public class InviteController {

    @Autowired
    private UserService userService;
    @ApiOperation("用户推广信息")
    @GetMapping(value = "getExtensionData")
    public Result getExtensionData(@ApiParam("用户id")Long userId) {
        return Result.success().put("data", userService.getExtensionData(userId));
    }

    @ApiOperation("获取推广用户列表")
    @GetMapping(value = "getExtensionList")
    public Result getExtensionList(@ApiParam("用户id")Long userId,
                                   @ApiParam("1是一级用户  2是二级用户")Integer type,
                                   @ApiParam("页码")Integer page,
                                   @ApiParam("条数")Integer limit) {
        return Result.success().put("data", userService.getExtensionList(userId,type, page, limit));
    }

    @ApiOperation("获取推广用户收益列表")
    @GetMapping(value = "getUserIncomeList")
    public Result getUserIncomeList(@ApiParam("用户id")Long userId,
                                    @ApiParam("1 是一级用户  2是二级用户")Integer type,
                                    @ApiParam("页码")Integer page,
                                    @ApiParam("条数")Integer limit) {
        return Result.success().put("data", userService.getUserIncomeList(userId,type, page, limit));
    }

    @ApiOperation("获取邀请的用户收益列表")
    @GetMapping(value = "getProfitList")
    public Result getProfitList(@ApiParam("用户id")Long userId,
                                @ApiParam("1 是一级用户  2是二级用户")Integer type,
                                @ApiParam("页码")Integer page,
                                @ApiParam("条数")Integer limit) {
        return Result.success().put("data", userService.getProfitList(userId,type, page, limit));
    }


}