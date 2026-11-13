package com.sqx.modules.app.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.IAppleService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.utils.UserConstantInterface;
import com.sqx.modules.app.utils.WxPhone;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.sys.service.SysCaptchaService;
import com.sqx.modules.utils.HttpClientUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;

import java.util.HashMap;
import java.util.Map;

/**
 * APP登录授权
 */
@RestController
@RequestMapping("/app/Login")
@Api(value = "用户端-登录相关", tags = {"用户端-登录相关"})
@Slf4j
public class AppLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private IAppleService appleService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private SysCaptchaService sysCaptchaService;


    @ApiOperation("微信小程序登陆")
    @RequestMapping(value = "/wxLogin", method = RequestMethod.GET)
    public Result wxLogin(@RequestParam(required = false) @ApiParam("小程序code码") String code) {
        return userService.wxLogin(code);
    }


    @ApiOperation("小程序登录新增或修改个人信息")
    @RequestMapping(value = "/insertWxUser", method = RequestMethod.POST)
    public Result insertWxUser(@RequestBody UserEntity userInfo) {
        return userService.wxRegister(userInfo);
    }


    @RequestMapping(value = "/appleLogin", method = RequestMethod.GET)
    @ApiOperation("苹果登陆获取appleUserId")
    public Result loginVerify(@ApiParam("苹果身份token") @RequestParam("identityToken") String identityToken) {
        try {
            log.info("苹果token：{}", identityToken);
            JSONObject jsonObject = JSON.parseObject(identityToken);
            JSONObject userInfo = jsonObject.getJSONObject("userInfo");
            String identityTokens = userInfo.getString("identityToken");
            return appleService.getAppleUserInfo(identityTokens);
        } catch (Exception e) {
            log.error("苹果token校验失败：{}", identityToken, e);
            return Result.error("苹果账号验证失败，请退出重试！");
        }
    }


    @ApiOperation("苹果登录")
    @RequestMapping(value = "/insertAppleUser", method = RequestMethod.GET)
    public Result insertAppleUser(@ApiParam("苹果id") @RequestParam String appleId) {
        return userService.iosRegister(appleId);
    }

    @RequestMapping(value = "/iosBindMobile", method = RequestMethod.POST)
    @ApiOperation("苹果登录绑定手机号")
    @ResponseBody
    public Result iosBindMobile(@ApiParam("手机号") @RequestParam(required = false) String phone,
                                @ApiParam("验证码") @RequestParam(required = false) String code,
                                @ApiParam("苹果id") @RequestParam(required = false) String appleId,
                                @ApiParam("账号来源") @RequestParam(required = false) String platform,
                                @ApiParam("手机类型 1安卓 2ios") @RequestParam(required = false) Integer sysPhone) {
        return userService.iosBindMobile(phone, code, appleId, platform, sysPhone);
    }


    @RequestMapping(value = "/wxAppLogin", method = RequestMethod.POST)
    @ApiOperation("微信APP登录")
    @ResponseBody
    public Result wxAppLogin(@ApiParam("微信openId") @RequestParam(required = false) String wxOpenId,
                             @ApiParam("微信登录token") @RequestParam(required = false) String token) {
        return userService.wxAppLogin(wxOpenId, token);
    }


    @RequestMapping(value = "/wxBindMobile", method = RequestMethod.POST)
    @ApiOperation("微信登录绑定手机号")
    @ResponseBody
    public Result wxBindMobile(@ApiParam("手机号") @RequestParam(required = false) String phone,
                               @ApiParam("验证码") @RequestParam(required = false) String code,
                               @ApiParam("微信openId") @RequestParam(required = false) String wxOpenId,
                               @ApiParam("微信登录token") @RequestParam(required = false) String token,
                               @ApiParam("账号来源") @RequestParam(required = false) String platform,
                               @ApiParam("手机类型 1安卓 2ios") @RequestParam(required = false) Integer sysPhone) {
        return userService.wxBindMobile(phone, code, wxOpenId, token, platform, sysPhone);
    }

    @ApiOperation("用户端openid登录呢")
    @RequestMapping(value = "/openid/login", method = RequestMethod.GET)
    @ResponseBody
    public Result loginByOpenId(@ApiParam("openId") @RequestParam(required = false) String openId) {
        return userService.loginByOpenId(openId);
    }


    @RequestMapping(value = "/registerCode", method = RequestMethod.POST)
    @ApiOperation("app或h5注册或登录")
    @ResponseBody
    public Result registerCode(@ApiParam("手机号") @RequestParam(required = false) String phone,
                               @ApiParam("验证码") @RequestParam(required = false) String msg,
                               @ApiParam("账号来源") @RequestParam(required = false) String platform,
                               @ApiParam("手机类型 1安卓 2ios") @RequestParam(required = false) Integer sysPhone,
                               @ApiParam("openId") @RequestParam(required = false) String openId,
                               @ApiParam("邀请码") @RequestParam(required = false) String inviterCode,
                               @ApiParam("密码") @RequestParam(required = false) String password) {
        return userService.registerCode(phone, msg, platform, sysPhone, openId, inviterCode, password);
    }

    @ApiOperation("用户端发送验证码")
    @RequestMapping(value = "/sendMsg/{phone}/{state}", method = RequestMethod.GET)
    @ResponseBody
    public Result sendMsg(@ApiParam("手机号") @PathVariable String phone,
                          @ApiParam("验证码类型") @PathVariable String state,
                          @RequestParam(required = false) @ApiParam("uuid") String uuid,
                          @RequestParam(required = false) @ApiParam("验证码") String captcha) {
        boolean captcha1 = sysCaptchaService.validate(uuid, captcha);
        if (!captcha1) {
            return Result.error("图形验证码不正确");
        }
        return userService.sendMsg(phone, state);
    }

    @ApiOperation("解密手机号")
    @RequestMapping(value = "/selectPhone", method = RequestMethod.POST)
    public Result getPhoneNumberBeanS5(@RequestBody WxPhone wxPhone) {
        return UserConstantInterface.decryptS5(wxPhone.getDecryptData(), wxPhone.getKey(), wxPhone.getIv());
    }

    @ApiParam("登录app")
    @RequestMapping(value = "/loginApp", method = RequestMethod.POST)
    public Result loginApp(@ApiParam("手机号") @RequestParam(required = false) String phone,
                           @ApiParam("密码") @RequestParam(required = false) String password) {
        return userService.loginApp(phone, password);
    }

    @CrossOrigin
    @ApiParam("注册app")
    @RequestMapping(value = "/registApp", method = RequestMethod.POST)
    public Result registApp(@ApiParam("用户名") @RequestParam(required = false) String userName,
                            @ApiParam("手机号") @RequestParam(required = false) String phone,
                            @ApiParam("密码") @RequestParam(required = false) String password,
                            @ApiParam("验证码") @RequestParam(required = false) String msg,
                            @ApiParam("账号来源") @RequestParam(required = false) String platform,
                            @ApiParam("邀请码") @RequestParam(required = false) String invitation) {
        return userService.registApp(userName, phone, password, msg, platform, invitation);
    }

    @ApiOperation("用户端忘记密码")
    @RequestMapping(value = "/forgetPwd", method = RequestMethod.POST)
    @ResponseBody
    public Result forgetPwd(@ApiParam("密码") @RequestParam(required = false) String pwd,
                            @ApiParam("手机号") @RequestParam(required = false) String phone,
                            @ApiParam("验证码") @RequestParam(required = false) String msg) {
        return userService.forgetPwd(pwd, phone, msg);
    }


    @GetMapping("/selectCity")
    @ApiOperation("根据经纬度获取城市")
    public Result selectCity(@ApiParam("纬度") @RequestParam(required = false) String lat,
                             @ApiParam("经度") @RequestParam(required = false) String lng) {
        CommonInfo one = commonInfoService.findOne(217);
        String url = "https://apis.map.qq.com/ws/geocoder/v1/";
        Map<String, String> maps = new HashMap<>();
        maps.put("location", lat + "," + lng);
        maps.put("key", one.getValue());
        String data = HttpClientUtil.doGet(url, maps);
        JSONObject jsonObject = JSON.parseObject(data);
        String status = jsonObject.getString("status");
        if ("0".equals(status)) {
            JSONObject result = jsonObject.getJSONObject("result");
            JSONObject adInfo = result.getJSONObject("ad_info");
            String city = adInfo.getString("city");
            return Result.success().put("data", city);
        } else {
            log.error("转换失败！！！原因:" + jsonObject.getString("message"));
        }
        return Result.error("获取定位失败！");
    }


    @GetMapping("/getOpenId")
    @ApiOperation("公众号根据code换取openId")
    public Result getOpenId(@ApiParam("微信code") @RequestParam(required = false) String code,
                            @ApiParam("用户id（暂未使用）") @RequestParam(required = false) Long userId) {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            //微信秘钥
            CommonInfo two = commonInfoService.findOne(21);
            SnsToken snsToken = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code);
            String openid = snsToken.getOpenid();
            return Result.success().put("data", openid);
        } catch (Exception e) {
            throw new RuntimeException("GET_OPENID_FAIL");
        }

    }


    @GetMapping("/bindOpenId")
    @ApiOperation("用户绑定公众号openId")
    public Result bindOpenId(@ApiParam("用户id") @RequestParam(required = false) Long userId,
                             @ApiParam("openId") @RequestParam(required = false) String openId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setWxOpenId(openId);
        userService.updateById(userEntity);
        return Result.success();
    }


}
