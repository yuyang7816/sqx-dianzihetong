package com.sqx.modules.app.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.service.UserCertificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/app/userCertification")
@Api(value = "用户端-实名认证", tags = {"用户端-实名认证"})

public class AppUserCertificationController {

    private UserCertificationService userCertificationService;

    /**
     * 实名认证
     **/
    @PostMapping("/insert")
    @Login
    @ApiOperation("实名认证")
    public Result insert(@ApiParam("拦截器获取的用户id") @RequestAttribute Long userId,
                         @ApiParam("真实姓名") @RequestParam(required = false) String name,
                         @ApiParam("身份证号") @RequestParam(required = false) String idNumber,
                         @ApiParam("手机号码") @RequestParam(required = false) String phone,
                         @ApiParam("正面") @RequestParam(required = false) String front,
                         @ApiParam("反面") @RequestParam(required = false) String back,
                         @ApiParam("详细地址") @RequestParam(required = false) String address) {
        if (name == null || name.isEmpty()) {
            return Result.error("真实姓名为空！");
        } else if (idNumber == null || idNumber.isEmpty()) {
            return Result.error("身份证号为空！");
        } else if (phone == null || phone.isEmpty()) {
            return Result.error("手机号码为空！");
        } else if (front == null || front.isEmpty()) {
            return Result.error("身份证正面照为空！");
        } else if (back == null || back.isEmpty()) {
            return Result.error("身份证反面照为空！");
        } else {
            return userCertificationService.insert(userId, name, idNumber, front, back, address, phone);
        }

    }

    /**
     * 是否进行实名
     */
    @GetMapping("/isInsert")
    @Login
    @ApiOperation("是否进行实名")
    public Result isInsert(@ApiParam("拦截器获取的用户id") @RequestAttribute Long userId) {
        return userCertificationService.isInsert(userId);
    }

    /**
     * 查询自己的实名信息
     */
    @GetMapping("/queryInsert")
    @Login
    @ApiOperation("查询自己的实名信息")
    public Result queryInsert(@ApiParam("拦截器获取的用户id") @RequestAttribute Long userId) {
        return userCertificationService.queryInsert(userId);
    }
}
