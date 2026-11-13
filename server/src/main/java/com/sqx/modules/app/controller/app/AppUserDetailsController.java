package com.sqx.modules.app.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/app/userBrowse")
@Api(value = "用户端-获取用户详情", tags = {"用户端-获取用户详情"})
@Slf4j
public class AppUserDetailsController {
    @Autowired
    private UserService userService;
    /**
     * 用户详情
     * */
    @ApiOperation("用户详情")
    @GetMapping("/userInfo")
    @Login
    public Result userInfo(@LoginUser UserEntity user){
        return Result.success().put("data",userService.getOne(new QueryWrapper<UserEntity>()
                .eq("user_id",user.getUserId())));
    }

}
