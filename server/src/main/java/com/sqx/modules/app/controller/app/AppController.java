package com.sqx.modules.app.controller.app;


import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.AppService;
import com.sqx.modules.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * APP登录授权
 */
@RestController
@RequestMapping("/app/user")
@Api(value = "用户端-用户管理相关", tags = {"用户端-用户管理相关"})
public class AppController {

    @Autowired
    private UserService userService;
    @Autowired
    private AppService appService;

    @Login
    @PostMapping("/updateSendMsg")
    @ApiOperation("修改是否接受消息推送")
    public Result updateSendMsg(@LoginUser UserEntity user,
                                @RequestParam(required = false) @ApiParam (value = "是否接受消息推送 1是 2否") Integer isSendMsg){
        user.setIsSendMsg(isSendMsg);
        userService.updateById(user);
        return Result.success();
    }


    @Login
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("用户端修改密码")
    public Result updatePwd(@LoginUser UserEntity user,
                            @ApiParam ("新密码")@RequestParam(required = false) String pwd,
                            @ApiParam ("旧密码")@RequestParam(required = false) String oldPwd) {
        if (!user.getPassword().equals(DigestUtils.sha256Hex(oldPwd))) {
            return Result.error("原始密码不正确！");
        }
        if (pwd.equals(oldPwd)) {
            return Result.error("新密码不能与旧密码相同！");
        }
        user.setPassword(DigestUtils.sha256Hex(pwd));
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updatePhone", method = RequestMethod.POST)
    @ApiOperation("用户端换绑手机号")
    @ResponseBody
    public Result updatePhone(@ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId,
                              @ApiParam("手机号") @RequestParam String phone,
                              @ApiParam("验证码") @RequestParam String msg) {
        return userService.updatePhone(phone, msg, userId);
    }

    @Login
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ApiOperation("用户修改个人信息")
    @ResponseBody
    public Result updateUser(@RequestBody UserEntity userEntity,
                             @RequestParam(required = false) @ApiParam("拦截器获取的用户id")@RequestAttribute("userId") Long userId) {
        userEntity.setUserId(userId);
        userService.updateById(userEntity);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updateUserImageUrl", method = RequestMethod.POST)
    @ApiOperation("用户修改头像")
    @ResponseBody
    public Result updateUserImageUrl(@LoginUser UserEntity user,
                                     @RequestParam(required = false)@ApiParam("头像")String avatar) {
        user.setAvatar(avatar);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
    @ApiOperation("用户修改昵称")
    @ResponseBody
    public Result updateUserName(@LoginUser UserEntity user,
                                 @RequestParam(required = false)@ApiParam("昵称")String userName) {
        user.setUserName(userName);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/selectUserById", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@LoginUser UserEntity user) {
        return Result.success().put("data", user);
    }


    @RequestMapping(value = "/selectNewApp", method = RequestMethod.GET)
    @ApiOperation("升级检测")
    @ResponseBody
    public Result selectNewApp() {
        return Result.success().put("data", appService.selectNewApp());
    }

    @RequestMapping(value = "/updateClientId", method = RequestMethod.GET)
    @ApiOperation("绑定ClientId")
    @ResponseBody
    public Result updateClientId(@RequestParam(required = false) @ApiParam("发送消息的clientId") String clientId,
                                 @RequestParam(required = false) @ApiParam("用户id") Long userId,
                                 @RequestParam(required = false) @ApiParam("手机类型 1安卓 2ios") Integer sysPhone) {
        UserEntity userEntity=new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setClientid(clientId);
        userEntity.setSysPhone(sysPhone);
        userService.updateById(userEntity);
        return Result.success();
    }

    @GetMapping("/selectUserByUserId")
    @ApiOperation("查看用户信息")
    @ResponseBody
    @Login
    public Result selectUserByUserId(@LoginUser UserEntity user,
                                     @RequestParam(required = false) @ApiParam("用户id") Long userId){
        return userService.selectUserByUserId(userId,user.getUserId());
    }

}
