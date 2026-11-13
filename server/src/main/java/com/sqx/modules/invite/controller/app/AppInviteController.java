package com.sqx.modules.invite.controller.app;


import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.utils.SenInfoCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "用户端-邀请收益", tags = {"用户端-邀请收益"})
@RequestMapping(value = "/app/invite")
public class AppInviteController {

    @Autowired
    private UserService userService;
    @Autowired
    private CommonInfoService commonInfoService;

    @Login
    @RequestMapping(value = "/selectInviteAndPoster", method = RequestMethod.GET)
    @ApiOperation("查看我的邀请码和海报二维码")
    @ResponseBody
    public Result selectInviteAndPoster(@ApiParam("拦截器获取的用户id") @RequestAttribute Long userId) {
        UserEntity userEntity = userService.queryByUserId(userId);
        CommonInfo one = commonInfoService.findOne(19);
        Map<String, Object> map = new HashMap<>();
        map.put("url", one.getValue());
        map.put("user", userEntity);
        return Result.success().put("data", map);
    }


    @GetMapping("/mpCreateQr")
    @ApiOperation("小程序推广二维码")
    public void mpCreateQr(@ApiParam("推广码") @RequestParam String invitationCode,
                           @ApiParam("页面跳转路径") String page, HttpServletResponse response) {
        SenInfoCheckUtil.getImg(invitationCode, page, response);
    }


    @Login
    @ApiOperation("用户推广信息")
    @GetMapping(value = "getExtensionData")
    public Result getExtensionData(@ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return Result.success().put("data", userService.getExtensionData(userId));
    }

    @Login
    @ApiOperation("获取推广用户列表")
    @GetMapping(value = "getExtensionList")
    public Result getExtensionList(@ApiParam("用户id") @RequestAttribute("userId") Long userId,
                                   @ApiParam("1是一级用户  2是二级用户") Integer type,
                                   @ApiParam("页码") Integer page,
                                   @ApiParam("条数") Integer limit) {
        return Result.success().put("data", userService.getExtensionList(userId, type, page, limit));
    }

    @Login
    @ApiOperation("获取推广用户列表")
    @GetMapping(value = "getAuthExtensionList")
    public Result getAuthExtensionList(@ApiParam("用户id") @RequestAttribute("userId") Long userId,
                                       @ApiParam("页码") Integer page,
                                       @ApiParam("条数") Integer limit) {
        return Result.success().put("data", userService.getAuthExtensionList(userId, page, limit));
    }


    @Login
    @ApiOperation("获取邀请的用户收益列表")
    @GetMapping(value = "getProfitList")
    public Result getProfitList(@ApiParam("用户id") @RequestAttribute("userId") Long userId,
                                @ApiParam("1 一级用户  2二级用户") Integer type,
                                @ApiParam("页码") Integer page,
                                @ApiParam("条数") Integer limit) {
        return Result.success().put("data", userService.getProfitList(userId, type, page, limit));
    }

}