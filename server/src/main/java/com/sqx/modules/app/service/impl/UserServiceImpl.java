package com.sqx.modules.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.message.PushChannel;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.AndroidDTO;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.getui.push.v2.sdk.dto.req.message.android.ThirdNotification;
import com.getui.push.v2.sdk.dto.req.message.android.Ups;
import com.getui.push.v2.sdk.dto.req.message.harmony.HarmonyDTO;
import com.getui.push.v2.sdk.dto.req.message.harmony.HarmonyNotification;
import com.getui.push.v2.sdk.dto.req.message.ios.Alert;
import com.getui.push.v2.sdk.dto.req.message.ios.Aps;
import com.getui.push.v2.sdk.dto.req.message.ios.IosDTO;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.*;
import com.sqx.modules.app.entity.*;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.utils.JwtUtils;
import com.sqx.modules.app.utils.UserConstantInterface;
import com.sqx.modules.common.dao.CommonInfoDao;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.contract.dao.ContractDao;
import com.sqx.modules.contract.entity.Contract;
import com.sqx.modules.enterprise.dao.EnterpriseDao;
import com.sqx.modules.file.utils.Md5Utils;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.utils.HttpClientUtil;
import com.sqx.modules.utils.InvitationCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.api.SnsAPI;
import weixin.popular.util.JsonUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户
 *
 * @author fang
 * @date 2021/2/27
 */

@Service("userService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private MsgDao msgDao;
    @Autowired
    private JwtUtils jwtUtils;
    private int number = 1;
    //    @Autowired
//    private InviteService inviteService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserCertificationDao userCertificationDao;
    @Autowired
    private CommonInfoDao commonInfoDao;
    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private UserMoneyDetailsService detailsService;
    @Autowired
    private UserMoneyDetailsDao detailsDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private EnterpriseDao enterpriseDao;


    @Override
    public UserEntity queryByPhone(String phone) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", phone));
    }

    @Override
    public UserEntity queryByOpenId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("open_id", openId));
    }

    @Override
    public UserEntity queryByWxOpenId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("wx_open_id", openId));
    }

    @Override
    public UserEntity queryByAppleId(String appleId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("apple_id", appleId));
    }

    @Override
    public UserEntity queryByUserId(Long userId) {
        UserEntity userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
        if (userEntity.getIsAuthentication() == null || userEntity.getIsAuthentication() != 1) {
//            DlUser dlUser = dlUserDao.selectOne(new QueryWrapper<DlUser>().eq("user_id", userId));
            UserCertification userCertification = userCertificationDao.selectOne(new QueryWrapper<UserCertification>().eq("user_id", userId));
            if (userCertification != null && userCertification.getStatus() == 1) {
                userEntity.setIsAuthentication(1);
                baseMapper.updateById(userEntity);
            }
        }
        return userEntity;
    }

    @Override
    public UserEntity queryByInvitationCode(String invitationCode) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", invitationCode));
    }

    @Override
    public Result updatePhone(String phone, String msg, Long userId) {
        Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
        //校验短信验证码
        if (msg1 != null) {
            UserEntity userInfo = queryByPhone(phone);
            if (userInfo != null) {
                return Result.error("手机号已经被其他账号绑定");
            } else {
                UserEntity one = baseMapper.selectById(userId);
                one.setPhone(phone);
                baseMapper.updateById(one);
                return Result.success();
            }
        }
        return Result.error("验证码不正确");
    }

    @Override
    public Result iosRegister(String appleId) {
        if (StringUtils.isEmpty(appleId)) {
            return Result.error("账号信息获取失败，请退出重试！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        UserEntity userInfo = queryByAppleId(appleId);
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }
            userInfo.setUpdateTime(date);
            baseMapper.updateById(userInfo);
            //返回用户信息
            UserEntity user = queryByAppleId(appleId);
            return getResult(user);
        } else {
            return Result.error(-200, "请先绑定手机号账号！");
        }
    }

    @Override
    public Result wxLogin(String code) {
        try {
            //微信小程序APPID
            String appid = commonInfoService.findOne(45).getValue();
            //微信小程序秘钥
            String secret = commonInfoService.findOne(46).getValue();
            // 配置请求参数
            Map<String, String> param = new HashMap<>();
            param.put("appid", appid);
            param.put("secret", secret);
            param.put("js_code", code);
            param.put("grant_type", UserConstantInterface.WX_LOGIN_GRANT_TYPE);
            param.put("scope", "snsapi_userinfo");
            // 发送请求
            String wxResult = HttpClientUtil.doGet(UserConstantInterface.WX_LOGIN_URL, param);
            log.info(wxResult);
            JSONObject jsonObject = JSONObject.parseObject(wxResult);
            // 获取参数返回的
            String session_key = jsonObject.get("session_key").toString();
            //返回微信小程序openId
            String open_id = jsonObject.get("openid").toString();
            //判断是否注册过
            UserEntity userEntity = queryByOpenId(open_id);
            Map<String, String> map = new HashMap<>();
            // 封装返回小程序
            map.put("session_key", session_key);
            map.put("open_id", open_id);
            if (jsonObject.get("unionid") != null) {
                String unionid = jsonObject.get("unionid").toString();
                map.put("unionid", unionid);
            } else {
                map.put("unionid", "-1");
            }

            if (userEntity == null) {
                map.put("flag", "1");
            } else {
                map.put("flag", userEntity.getPhone());
            }
            return Result.success("登陆成功").put("data", map);
        } catch (Exception e) {
            log.error("登陆失败：" + e.getMessage(), e);
            return Result.success("登录失败！");
        }
    }


    @Override
    public Result wxRegister(UserEntity userInfo1) {
        if (StringUtils.isEmpty(userInfo1.getOpenId())) {
            return Result.error("账号信息获取失败，请退出重试！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        UserEntity userInfo = queryByOpenId(userInfo1.getOpenId());
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }
            if (StringUtils.isNotEmpty(userInfo1.getUserName())) {
                userInfo.setUserName(userInfo1.getUserName());
            }
            if (StringUtils.isNotEmpty(userInfo1.getAvatar())) {
                userInfo.setAvatar(userInfo1.getAvatar());
            }
            if (StringUtils.isNotEmpty(userInfo1.getPhone())) {
                userInfo.setPhone(userInfo1.getPhone());
            }
            userInfo.setUpdateTime(date);
            baseMapper.updateById(userInfo);
        } else {
            //判断是否在app登陆过  手机号是否有账号
            UserEntity userByMobile = queryByPhone(userInfo1.getPhone());
            if (userByMobile != null) {
                //有账号则绑定账号
                userByMobile.setOpenId(userInfo1.getOpenId());
                baseMapper.updateById(userByMobile);
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("小程序账号绑定成功！");
                messageInfo.setTitle("系统通知");
                messageInfo.setState(String.valueOf(5));
                messageInfo.setUserName(userByMobile.getUserName());
                messageInfo.setUserId(String.valueOf(userByMobile.getUserId()));
                messageInfo.setCreateAt(sdf.format(new Date()));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
                if (userByMobile.getStatus().equals(2)) {
                    return Result.error("账号已被封禁，请联系客服处理！");
                }
            } else {
                if (StringUtils.isNotBlank(userInfo1.getInviterCode())) {
                    UserEntity inviter = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", userInfo1.getInviterCode()));
                    if (inviter != null) {
                        userInfo1.setInviterCode(userInfo1.getInviterCode());
                    } else {
                        return Result.error("邀请码的邀请人不存在，请检查！");
                    }
                    if (inviter.getInviterCode() != null) {
                        UserEntity inviterInviter = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", inviter.getInviterCode()));
                        if (inviterInviter != null) {
                            userInfo1.setInviterInviterCode(inviterInviter.getInvitationCode());
                        }
                    }
                } else {
                    userInfo1.setInviterCode(commonInfoDao.findOne(88).getValue());
                }
                String rate = commonInfoDao.findOne(303).getValue();
                BigDecimal oneRate = new BigDecimal(commonInfoDao.findOne(304).getValue());
                BigDecimal twoRate = new BigDecimal(commonInfoDao.findOne(305).getValue());
                if ("是".equals(rate)) {
                    userInfo1.setZhiRate(oneRate);
                    userInfo1.setFeiRate(twoRate);
                }
                if (StringUtils.isNotBlank(userInfo1.getPassword())) {
                    userInfo1.setPassword(DigestUtils.sha256Hex(userInfo1.getPassword()));
                } else {
                    userInfo1.setPassword(DigestUtils.sha256Hex("123456"));
                }
                // 获取系统默认头像
                String avatar = commonInfoDao.findOne(430).getValue();
                userInfo1.setAvatar(avatar);
                if (userInfo1.getPhone() != null) {
                    userInfo1.setUserName(userInfo1.getPhone().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
                }
                if (userInfo1.getPlatform() != null) {
                    userInfo1.setPlatform(userInfo1.getPlatform());
                }
                if (userInfo1.getSysPhone() != null) {
                    userInfo1.setSysPhone(userInfo1.getSysPhone());
                }
                if (userInfo1.getOpenId() != null) {
                    userInfo1.setOpenId(userInfo1.getOpenId());
                }
                //没有则生成新账号
                userInfo1.setCreateTime(date);
                userInfo1.setPlatform("小程序");
                userInfo1.setStatus(1);
                userInfo1.setJifen(100);
                userInfo1.setInvitationCode(InvitationCodeUtil.toSerialCode());
                baseMapper.insert(userInfo1);
                //给用户创建钱包
                UserMoney userMoney = new UserMoney();
                userMoney.setUserId(userInfo1.getUserId());
                userMoney.setMoney(new BigDecimal(0));
                userMoneyDao.insert(userMoney);
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("恭喜您，账号注册成功！");
                messageInfo.setTitle("系统通知");
                messageInfo.setState(String.valueOf(5));
                messageInfo.setUserName(userInfo1.getUserName());
                messageInfo.setUserId(String.valueOf(userInfo1.getUserId()));
                messageInfo.setCreateAt(sdf.format(new Date()));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
                UserEntity userEntity = queryByInvitationCode(userInfo1.getInviterCode());
//                if (userEntity != null) {
//                    inviteService.saveBody(userInfo1.getUserId(), userEntity);
//                }
            }
        }
        //返回用户信息
        UserEntity user = queryByOpenId(userInfo1.getOpenId());
        return getResult(user);
    }


    @Override
    public Result wxBindMobile(String phone, String code, String wxOpenId, String token, String platform, Integer sysPhone) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, code);
        if (byPhoneAndCode == null) {
            return Result.error("验证码错误");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(userInfo.getWxOpenId())) {
                return Result.error("当前手机号已经被其他微信绑定");
            }
            //小程序登陆过
            userInfo.setWxOpenId(wxOpenId);
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {
                if (user.getHeadimgurl() != null) {
                    userInfo.setAvatar(user.getHeadimgurl());
                }
                userInfo.setSex(user.getSex());
                if (user.getNickname() != null) {
                    userInfo.setUserName(user.getNickname().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
                }
            }
            baseMapper.updateById(userInfo);
        } else {
            //小程序没有登陆过
            userInfo = new UserEntity();
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {
                if (user.getHeadimgurl() != null) {
                    userInfo.setAvatar(user.getHeadimgurl());
                }
                userInfo.setSex(user.getSex());
                if (user.getNickname() != null) {
                    userInfo.setUserName(user.getNickname().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
                }
            }
            userInfo.setWxOpenId(wxOpenId);
            userInfo.setPhone(phone);
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setSysPhone(sysPhone);
            userInfo.setStatus(1);
            userInfo.setUpdateTime(time);
            String rate = commonInfoDao.findOne(303).getValue();
            BigDecimal oneRate = new BigDecimal(commonInfoDao.findOne(304).getValue());
            BigDecimal twoRate = new BigDecimal(commonInfoDao.findOne(305).getValue());
            if ("是".equals(rate)) {
                userInfo.setZhiRate(oneRate);
                userInfo.setFeiRate(twoRate);
            }
            baseMapper.insert(userInfo);
        }
        UserEntity userEntity = queryByWxOpenId(userInfo.getWxOpenId());
        return getResult(userEntity);
    }

    @Override
    public Result iosBindMobile(String phone, String code, String appleId, String platform, Integer sysPhone) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, code);
        if (byPhoneAndCode == null) {
            return Result.error("验证码错误");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(userInfo.getAppleId())) {
                return Result.error("当前手机号已经被其他苹果绑定");
            }
            userInfo.setAppleId(appleId);
            userInfo.setUpdateTime(simpleDateFormat.format(new Date()));
            baseMapper.updateById(userInfo);
        } else {
            userInfo = new UserEntity();
            userInfo.setSex(0);
            userInfo.setUserName(phone.replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
            userInfo.setPhone(phone);
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setSysPhone(sysPhone);
            userInfo.setStatus(1);
            userInfo.setUpdateTime(time);
            String rate = commonInfoDao.findOne(303).getValue();
            BigDecimal oneRate = new BigDecimal(commonInfoDao.findOne(304).getValue());
            BigDecimal twoRate = new BigDecimal(commonInfoDao.findOne(305).getValue());
            if ("是".equals(rate)) {
                userInfo.setZhiRate(oneRate);
                userInfo.setFeiRate(twoRate);
            }
            baseMapper.insert(userInfo);
        }
        UserEntity userEntity = queryByAppleId(userInfo.getAppleId());
        return getResult(userEntity);
    }


    @Override
    public Result wxAppLogin(String wxOpenId, String token) {
        UserEntity userEntity = queryByWxOpenId(wxOpenId);
        if (userEntity != null) {
            if (userEntity.getStatus().equals(2)) {
                return Result.error("账号已被禁用，请联系客服处理！");
            }
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {
                if (user.getHeadimgurl() != null) {
                    userEntity.setAvatar(user.getHeadimgurl());
                }
                userEntity.setSex(user.getSex());
                if (user.getNickname() != null) {
                    userEntity.setUserName(user.getNickname().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userEntity.setUpdateTime(sdf.format(new Date()));
            baseMapper.updateById(userEntity);
            return getResult(userEntity);
        } else {
            return Result.error(-200, "请先绑定手机号账号！");
        }
    }


    @Override
    public Result loginByOpenId(String openId) {
        UserEntity userEntity = queryByWxOpenId(openId);
        if (userEntity == null) {
            return Result.error(-200, "未注册！");
        }
        String token = jwtUtils.generateToken(userEntity.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("user", userEntity);
        return Result.success(map);
    }


    @Override
    public Result registerCode(String phone, String msg, String platform, Integer sysPhone, String openId, String inviterCode, String password) {
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(password) && StringUtils.isEmpty(msg)) {
                if (StringUtils.isEmpty(userInfo.getPassword())) {
                    return Result.error("当前账号未设置密码，请使用短信登录或忘记密码去重置密码");
                }
                //密码登录  判断输入密码加密之后是否和数据库的加密密码相同
                String pwd = DigestUtils.sha256Hex(password);
                if (!userInfo.getPassword().equals(pwd)) {
                    return Result.error("密码不正确");
                }
            } else {
                Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
                //校验短信验证码
                if (msg1 == null) {
                    return Result.error("验证码不正确");
                }
                msgDao.deleteById(msg1.getId());
            }
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被禁用，请联系客服处理！");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setUpdateTime(sdf.format(new Date()));
            baseMapper.updateById(userInfo);
            return getResult(userInfo);
        } else {
            // 判断是否账号密码登录并且账号是否存在
            if (phone != null && StringUtils.isNotBlank(password) && StringUtils.isBlank(msg)) {
                //校验手机号是否存在
                UserEntity userInfos = queryByPhone(phone);
                if (userInfos == null) {
                    return Result.error("请手机验证码登录！");
                }
            }
            Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
            if (msg1 == null) {
                return Result.error("验证码不正确");
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(new Date());
            userInfo = new UserEntity();
            userInfo.setPhone(phone);
            if (StringUtils.isNotBlank(inviterCode)) {
                UserEntity inviter = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", inviterCode));
                if (inviter != null) {
                    userInfo.setInviterCode(inviterCode);
                } else {
                    return Result.error("邀请码的邀请人不存在，请检查！");
                }
                if (inviter.getInviterCode() != null) {
                    UserEntity inviterInviter = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", inviter.getInviterCode()));
                    if (inviterInviter != null) {
                        userInfo.setInviterInviterCode(inviterInviter.getInvitationCode());
                    }
                }
            } else {
                userInfo.setInviterCode(commonInfoDao.findOne(88).getValue());
            }
            String rate = commonInfoDao.findOne(303).getValue();
            BigDecimal oneRate = new BigDecimal(commonInfoDao.findOne(304).getValue());
            BigDecimal twoRate = new BigDecimal(commonInfoDao.findOne(305).getValue());
            if ("是".equals(rate)) {
                userInfo.setZhiRate(oneRate);
                userInfo.setFeiRate(twoRate);
            }
            if (StringUtils.isNotBlank(password)) {
                userInfo.setPassword(DigestUtils.sha256Hex(password));
            } else {
                userInfo.setPassword(DigestUtils.sha256Hex("123456"));
            }
            // 获取系统默认头像
            String avatar = commonInfoDao.findOne(430).getValue();
            userInfo.setAvatar(avatar);
            if (phone != null) {
                userInfo.setUserName(phone.replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
            }
            if (platform != null) {
                userInfo.setPlatform(platform);
            }
            userInfo.setCreateTime(time);
            if (sysPhone != null) {
                userInfo.setSysPhone(sysPhone);
            }
            userInfo.setStatus(1);
            if (openId != null) {
                userInfo.setWxOpenId(openId);
            }
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode());
            userInfo.setJifen(100); //默认积分100分
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setUpdateTime(sdf.format(new Date()));
            baseMapper.insert(userInfo);
            msgDao.deleteById(msg1.getId());
            //给用户创建钱包
            UserMoney userMoney = new UserMoney();
            userMoney.setMoney(BigDecimal.valueOf(0));
            userMoney.setUserId(userInfo.getUserId());
            userMoneyDao.insert(userMoney);
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setContent("恭喜您，账号注册成功！");
            messageInfo.setTitle("系统通知");
            messageInfo.setState(String.valueOf(5));
            messageInfo.setUserName(userInfo.getUserName());
            messageInfo.setUserId(String.valueOf(userInfo.getUserId()));
            messageInfo.setCreateAt(sdf.format(new Date()));
            messageInfo.setIsSee("0");
            messageService.saveBody(messageInfo);
            UserEntity userEntity = queryByInvitationCode(userInfo.getInviterCode());
//            if (userEntity != null) {
//                inviteService.saveBody(userInfo.getUserId(), userEntity);
//            }
            return getResult(userInfo);
        }
    }


    @Override
    public Result login(String phone, String pwd) {
        UserEntity userEntity = queryByPhone(phone);
        if (userEntity == null) {
            return Result.error("手机号未注册！");
        }
        if (!userEntity.getPassword().equals(DigestUtils.sha256Hex(pwd))) {
            return Result.error("密码不正确！");
        }
        if (userEntity.getStatus().equals(2)) {
            return Result.error("账号已被禁用，请联系客服处理！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userEntity.setUpdateTime(sdf.format(new Date()));
        baseMapper.updateById(userEntity);
        return getResult(userEntity);
    }


    @Override
    public Result getResult(UserEntity user) {
        //生成token
        String token = jwtUtils.generateToken(user.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("user", user);
        return Result.success(map);
    }


    @Override
    public Result sendMsg(String phone, String state) {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        System.out.println("sendMsg code is " + code);
        SmsSingleSenderResult result = null;
        /*if ("bindWx".equals(state)) {
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null && StringUtils.isNotEmpty(userByPhone.getWxOpenId())) {
                return Result.error("当前手机号已被其他微信账号绑定");
            }
        } else if ("bindIos".equals(state)) {
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null && StringUtils.isNotEmpty(userByPhone.getAppleId())) {
                return Result.error("当前手机号已被其他苹果账号绑定");
            }
        }*/
        if (phone != null) {
            if ("forget".equals(state)) {
                UserEntity userByPhone = queryByPhone(phone);
                if (userByPhone == null) {
                    return Result.error("手机号未注册");
                }
            }
        }
        CommonInfo three = commonInfoService.findOne(79);
        //默认使用腾讯云
        if (three == null || "1".equals(three.getValue())) {
            //腾讯云短信发送
            return sendMsgTencent(phone, state, code);
        } else if ("2".equals(three.getValue())) {
            //阿里云短信发送
            return sendMsgAlibaba(phone, code, state);
        } else {
            return sendMsgDXB(phone, state, code);
        }
    }

    @Override
    public Result sendMsg(String phone, String state, Integer code) {
        CommonInfo three = commonInfoService.findOne(79);
        //默认使用腾讯云
        if (three == null || "1".equals(three.getValue())) {
            //腾讯云短信发送
            return sendMsgTencent(phone, state, code);
        } else if ("2".equals(three.getValue())) {
            //阿里云短信发送
            return sendMsgAlibaba(phone, code, state);
        } else {
            return sendMsgDXB(phone, state, code);
        }
    }


    private Result sendMsgAlibaba(String phone, int code, String state) {
        //阿里云短信accessKeyId
        CommonInfo three = commonInfoService.findOne(83);
        String accessKeyId = three.getValue();
        //阿里云短信accessSecret
        CommonInfo four = commonInfoService.findOne(84);
        String accessSecret = four.getValue();
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonInfo name = commonInfoService.findOne(6);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", name.getValue());
        String value;
        switch (state) {
            case "register":
                value = commonInfoService.findOne(80).getValue();
                break;
            case "forget":
                value = commonInfoService.findOne(81).getValue();
                break;
            case "bind":
                value = commonInfoService.findOne(82).getValue();
                break;
            case "contract":
                value = commonInfoService.findOne(85).getValue();
                break;
            case "copyContract":
                value = commonInfoService.findOne(86).getValue();
                break;
            default:
                value = commonInfoService.findOne(80).getValue();
                break;
        }
        request.putQueryParameter("TemplateCode", value);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            String data = response.getData();
            JSONObject jsonObject = JSON.parseObject(data);
            if ("OK".equals(jsonObject.get("Code"))) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
                return Result.success("login");
            } else {
                if (jsonObject.get("Message").toString().contains("分钟")) {
                    return Result.error("短信发送过于频繁，请一分钟后再试！");
                } else if (jsonObject.get("Message").toString().contains("小时")) {
                    return Result.error("短信发送过于频繁，请一小时后再试！");
                } else if (jsonObject.get("Message").toString().contains("天")) {
                    return Result.error("短信发送过于频繁，请明天再试！");
                }
                log.info(jsonObject.get("Message").toString());
                return Result.error("短信发送失败！");
            }
        } catch (ClientException | com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
        return Result.error("验证码发送失败");
    }


    private Result sendMsgTencent(String phone, String state, int code) {
        SmsSingleSenderResult result = null;
        try {
            CommonInfo three = commonInfoService.findOne(31);
            String clientId = three.getValue();

            CommonInfo four = commonInfoService.findOne(32);
            String clientSecret = four.getValue();
            CommonInfo name = commonInfoService.findOne(6);
            /**
             * 发送短信验证码的状态、
             *
             * 在h5登录环境中 传的状态不是以下三种状态
             */
            SmsSingleSender ssender = new SmsSingleSender(Integer.valueOf(clientId), clientSecret);
            switch (state) {
                case "register":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
                case "forget":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，您正在执行找回密码操作，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
                case "bind":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，您正在执行绑定手机号操作，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
                case "contract":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】，您有一份需要签署的合同，请及时上线查看并签署！", "", "");
                    break;
                case "copyContract":
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】，您有一份抄送合同，请及时上线查看并签署！", "", "");
                    break;
                default:
                    result = ssender.send(0, "86", phone, "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息", "", "");
                    break;
            }


            System.out.println(result);
            if (result.result == 0) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
                UserEntity userByPhone = queryByPhone(phone);
                if (userByPhone != null) {
                    return Result.success("login");
                } else {
                    return Result.success("register");
                }
            } else {
                return Result.error(6, result.errMsg);
            }
        } catch (HTTPException | JSONException | IOException e) {
            // HTTP 响应码错误
            e.printStackTrace();
        }
        return Result.error("验证码发送失败");
    }


    private Result sendMsgDXB(String phone, String state, int code) {
        CommonInfo three = commonInfoService.findOne(164);
        CommonInfo four = commonInfoService.findOne(165);
        CommonInfo name = commonInfoService.findOne(6);
        String testUsername = three.getValue(); //在短信宝注册的用户名
        String testPassword = four.getValue(); //在短信宝注册的密码
        String value = "";
        switch (state) {
            case "register":
                value = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "forget":
                value = "【" + name.getValue() + "】验证码: " + code + "，您正在执行找回密码操作，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "bind":
                value = "【" + name.getValue() + "】验证码: " + code + "，您正在执行绑定手机号操作，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "dx":
                value = "【" + name.getValue() + "】您有" + code + "条未读消息，赶快上线查看吧！";
                break;
            case "contract":
                value = "【" + name.getValue() + "】您有一份需要签署的合同，请及时上线查看并签署！";
                break;
            case "copyContract":
                value = "【" + name.getValue() + "】您有一份抄送的合同，请及时上线查看！";
                break;
            default:
                value = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
        }
        StringBuilder httpArg = new StringBuilder();
        httpArg.append("u=").append(testUsername).append("&");
        httpArg.append("p=").append(Md5Utils.md5s(testPassword)).append("&");
        httpArg.append("m=").append(phone).append("&");
        httpArg.append("c=").append(Md5Utils.encodeUrlString(value, "UTF-8"));
        String result = Md5Utils.request("https://api.smsbao.com/sms", httpArg.toString());
        log.error("短信包返回值：" + result);
        if ("0".equals(result)) {
            Msg byPhone = msgDao.findByPhone(phone);
            if (byPhone != null) {
                byPhone.setCode(String.valueOf(code));
                byPhone.setPhone(phone);
                msgDao.updateById(byPhone);
            } else {
                Msg msg = new Msg();
                msg.setCode(String.valueOf(code));
                msg.setPhone(phone);
                msgDao.insert(msg);
            }
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null) {
                return Result.success("login");
            } else {
                return Result.success("register");
            }
        } else {
//            return ResultUtil.error(6, result.errMsg);
            if ("30".equals(result)) {
                return Result.error("错误密码");
            } else if ("40".equals(result)) {
                return Result.error("账号不存在");
            } else if ("41".equals(result)) {
                return Result.error("余额不足");
            } else if ("43".equals(result)) {
                return Result.error("IP地址限制");
            } else if ("50".equals(result)) {
                return Result.error("内容含有敏感词");
            } else if ("51".equals(result)) {
                return Result.error("手机号码不正确");
            }
        }

        return Result.error("验证码发送失败");
    }


    @Override
    public Result getOpenId(String code, Long userId) {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            //微信秘钥
            CommonInfo two = commonInfoService.findOne(21);
            String openid = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code).getOpenid();
            if (StringUtils.isNotEmpty(openid)) {
                UserEntity userEntity = new UserEntity();
                userEntity.setUserId(userId);
                userEntity.setOpenId(openid);
                baseMapper.updateById(userEntity);
                return Result.success().put("data", openid);
            }
            return Result.error("获取失败");
        } catch (Exception e) {
            log.error("GET_OPENID_FAIL");
            return Result.error("获取失败,出错了！");
        }
    }

    @Override
    public UserEntity selectUserById(Long userId) {
        return baseMapper.selectById(userId);
    }


    //    @Override
//    public PageUtils selectUserPage(Integer page, Integer limit, String search, Integer sex, String platform, String sysPhone, Integer status, String startTime, String endTime, String userName, String inviterCode) {
//        Page<UserEntity> pages = new Page<>(page, limit);
//        return new PageUtils(baseMapper.selectUserPage(pages, search, sex, platform, sysPhone, status,member,startTime,endTime,userName,inviterCode,isAuthentication));
//    }
    @Override
    public PageUtils selectUserPage(Integer page, Integer limit, String search, Integer sex, String platform, String sysPhone, Integer status, String startTime, String endTime, String userName, String inviterCode, Integer statuss) {
        Page<UserEntity> pages = new Page<>(page, limit);
        IPage<UserEntity> userEntityIPage = baseMapper.selectUserPage(pages, search, sex, platform, sysPhone, status, startTime, endTime, userName, inviterCode, statuss);
        List<UserEntity> records = userEntityIPage.getRecords();
        for (UserEntity a : records) {
            if (a.getStatuss() == null || a.getStatuss() != 1) {
                a.setReal(false);
            } else {
                a.setReal(true);
            }
        }
        return new PageUtils(userEntityIPage);
    }

    @Override
    public int queryInviterCount(String inviterCode) {
        return baseMapper.queryInviterCount(inviterCode);
    }

    @Override
    public int queryUserCount(int type, String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        if (date == null || date == "") {
            date = simpleDateFormat.format(new Date());
        }
        return baseMapper.queryUserCount(type, date);
    }

    @Override
    public Double queryPayMoney(int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String date = simpleDateFormat.format(new Date());
        return baseMapper.queryPayMoney(type, date);
    }

    @Override
    public IPage<Map<String, Object>> queryCourseOrder(Page<Map<String, Object>> iPage, int type, String date) {
        return baseMapper.queryCourseOrder(iPage, type, date);
    }

    @Override
    public int userMessage(String date, int type) {
        return baseMapper.userMessage(date, type);
    }


    @Override
    public void pushToSingle(String title, String content, String clientId) {
        try {
            if (StringUtils.isNotEmpty(clientId)) {
                UserEntity userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("clientid", clientId));
                GtApiConfiguration apiConfiguration = new GtApiConfiguration();
                //填写应用配置
                apiConfiguration.setAppId(commonInfoService.findOne(61).getValue());
                apiConfiguration.setAppKey(commonInfoService.findOne(60).getValue());
                apiConfiguration.setMasterSecret(commonInfoService.findOne(62).getValue());
                // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
                apiConfiguration.setDomain("https://restapi.getui.com/v2/");
                // 实例化ApiHelper对象，用于创建接口对象
                ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
                // 创建对象，建议复用。目前有PushApi、StatisticApi、UserApi
                PushApi pushApi = apiHelper.creatApi(PushApi.class);
                //根据cid进行单推
                PushDTO<Audience> pushDTO = new PushDTO<Audience>();
                // 设置推送参数
                pushDTO.setRequestId(System.currentTimeMillis() + "");
                PushMessage pushMessage = new PushMessage();
                if (userEntity == null || userEntity.getSysPhone() == null || userEntity.getSysPhone() == 1) {
                    //安卓推送
                    GTNotification notification = new GTNotification();
                    pushDTO.setPushMessage(pushMessage);
                    // 配置通知栏图标
                    notification.setLogo(commonInfoService.findOne(19).getValue() + "/logo.png"); //配置通知栏图标，需要在客户端开发时嵌入，默认为push.png
                    // 配置通知栏网络图标
                    notification.setLogoUrl(commonInfoService.findOne(19).getValue() + "/logo.png");
                    notification.setTitle(title);
                    notification.setBody(content);
                    notification.setClickType("startapp");
                    notification.setUrl(commonInfoService.findOne(19).getValue());
                    notification.setChannelLevel("3");
                    pushMessage.setNotification(notification);

                    PushChannel pushChannel = new PushChannel();
                    AndroidDTO androidDTO = new AndroidDTO();
                    Ups ups = new Ups();
                    ThirdNotification thirdNotification = new ThirdNotification();
                    thirdNotification.setTitle(title);
                    thirdNotification.setBody(content);
                    thirdNotification.setClickType("startapp");
                    ups.setNotification(thirdNotification);
                    Map<String, Map<String, Object>> options = new HashMap<>();
                    Map<String, Object> hw = new HashMap<>();
                    hw.put("/message/android/notification/badge/class", "pages/index/index");
                    hw.put("/message/android/notification/badge/add_num", 1);
                    hw.put("/message/android/notification/badge/set_num", 1);

                    hw.put("/message/android/notification/image", commonInfoService.findOne(19).getValue() + "/logo.png");
                    hw.put("/message/android/notification/style", 0);
                    hw.put("/message/android/notification/big_title", title);
                    hw.put("/message/android/notification/big_body", content);
                    hw.put("/message/android/category", "IM");

                    hw.put("/message/android/notification/default_sound", false);
                    hw.put("/message/android/notification/sound", "");
                    hw.put("/message/android/notification/importance", "NORMAL");

                    hw.put("/message/android/ttl", "86400s");
                    options.put("HW", hw);

                    Map<String, Object> ry = new HashMap<>();
                    ry.put("/android/notification/badge/addNum", 1);
                    ry.put("/android/notification/badge/setNum", 1);
                    ry.put("/android/notification/badge/badgeClass", "pages/index/index");

//        ry.put("/android/notification/icon","https://zbb.dailianbao.net/logo.png");
                    ry.put("/android/notification/image", commonInfoService.findOne(19).getValue() + "/logo.png");
                    ry.put("/android/notification/style", "pages/index/index");
                    ry.put("/android/notification/importance", "NORMAL");
                    ry.put("/android/ttl", "86400s");
                    options.put("HO", ry);

                    Map<String, Object> xm = new HashMap<>();
                    xm.put("/extra.channel_id", 142840);
                    xm.put("/extra.locale", "zh_CN");
                    xm.put("/time_to_live", 86400000);
                    options.put("XM", xm);

                    Map<String, Object> OP = new HashMap<>();
                    Map<String, Object> OPG = new HashMap<>();
                    OP.put("/channel_id", "push_oplus_category_content");
                    OPG.put("/channel_id", "push_oplus_category_content");
                    OP.put("/category", "SOCIAL");
                    OP.put("/notify_level", 1);
                    OP.put("/app_message_id", UUID.randomUUID().toString());
                    OPG.put("/app_message_id", UUID.randomUUID().toString());
                    OP.put("/style", 1);
//                    OP.put("/small_picture_id",1);
                    OP.put("/off_line", true);
                    OP.put("/off_line_ttl", 86400);
                    OPG.put("/off_line", true);
                    OPG.put("/off_line_ttl", 86400);
                    options.put("OP", OP);
                    options.put("OPG", OPG);

                    Map<String, Object> VV = new HashMap<>();
                    VV.put("/category", "systemId");
                    VV.put("/notifyType", 4);
                    VV.put("/networkType", -1);
                    VV.put("/timeToLive", 86400);
                    options.put("VV", VV);

                    Map<String, Object> MZ = new HashMap<>();
                    MZ.put("/noticeMsgType", 0);
                    options.put("MZ", MZ);

                    ups.setOptions(options);
                    androidDTO.setUps(ups);
                    pushChannel.setAndroid(androidDTO);
                    HarmonyDTO harmony = new HarmonyDTO();
                    HarmonyNotification harmonyNotification = new HarmonyNotification();
                    harmonyNotification.setTitle(title);
                    harmonyNotification.setBody(content);
                    harmonyNotification.setClickType("startapp");
                    harmonyNotification.setCategory("IM");
                    harmony.setNotification(harmonyNotification);

                    Map<String, Map<String, Object>> harmonyOptions = new HashMap<>();
                    Map<String, Object> hwHarmony = new HashMap<>();
                    //{"addNum":1}
                    JSONObject badge = new JSONObject();
                    badge.put("addNum", 1);
                    hwHarmony.put("/payload/notification/badge", badge);
                    hwHarmony.put("/payload/notification/image", commonInfoService.findOne(19).getValue() + "/logo.png");
                    hwHarmony.put("/payload/notification/style", 0);
                    hwHarmony.put("/payload/notification/inboxContent", badge);
                    harmonyOptions.put("HW", hwHarmony);
                    harmony.setOptions(harmonyOptions);
                    pushChannel.setHarmony(harmony);

                    pushDTO.setPushChannel(pushChannel);

                } else {
                    pushMessage.setTransmission(title);
                    pushDTO.setPushMessage(pushMessage);
                    PushChannel pushChannel = new PushChannel();
                    IosDTO iosDTO = new IosDTO();
                    Aps aps = new Aps();
                    Alert alert = new Alert();
                    alert.setTitle(title);
                    alert.setBody(content);
                    aps.setAlert(alert);
                    aps.setSound("default");
                    iosDTO.setAps(aps);
                    pushChannel.setIos(iosDTO);
                    pushDTO.setPushChannel(pushChannel);
                }
                // 设置接收人信息
                Audience audience = new Audience();
                audience.addCid(clientId);
                pushDTO.setAudience(audience);
                // 进行cid单推
                ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
                if (apiResult.isSuccess()) {
                    // success
                    log.error("消息推送成功：" + apiResult.getData());
                } else {
                    // failed
                    log.error("消息推送成功失败：code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("消息推送异常：" + e.getMessage(), e);
        }
    }

    @Override
    public Result loginApp(String phone, String password) {
        //md5加密
        String pwd = DigestUtils.sha256Hex(password);
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        UserEntity userEntity = baseMapper.selectOne(queryWrapper);
        if (userEntity == null) {
            return Result.error("手机号未注册！");
        }
        if (!userEntity.getPassword().equals(pwd)) {
            return Result.error("密码不正确！");
        }
        if (userEntity.getStatus().equals(2)) {
            return Result.error("账号已被禁用，请联系客服处理！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userEntity.setUpdateTime(sdf.format(new Date()));
        baseMapper.updateById(userEntity);
        return getResult(userEntity);

    }

    @Override
    public Result registApp(String userName, String phone, String password, String msg, String platform, String inviterCode) {
        Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
        //校验短信验证码
        if (msg1 == null) {
            return Result.error("验证码不正确");
        }
        msgDao.deleteById(msg1.getId());
        //校验手机号是否存在
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            return Result.error("手机号已经被注册！");

        } else {
            userInfo = new UserEntity();
            if (StringUtils.isNotBlank(inviterCode)) {
                UserEntity inviter = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", inviterCode));
                if (inviter != null) {
                    userInfo.setInviterCode(inviterCode);
                } else {
                    return Result.error("邀请码的邀请人不存在，请检查！");
                }
                if (inviter.getInviterCode() != null) {
                    UserEntity inviterInviter = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", inviter.getInviterCode()));
                    if (inviterInviter != null) {
                        userInfo.setInviterInviterCode(inviterInviter.getInvitationCode());
                    }
                }
            } else {
                userInfo.setInviterCode(commonInfoDao.findOne(88).getValue());
            }
            String rate = commonInfoDao.findOne(303).getValue();
            BigDecimal oneRate = new BigDecimal(commonInfoDao.findOne(304).getValue());
            BigDecimal twoRate = new BigDecimal(commonInfoDao.findOne(305).getValue());
            if ("是".equals(rate)) {
                userInfo.setZhiRate(oneRate);
                userInfo.setFeiRate(twoRate);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(new Date());
            userInfo.setPhone(phone);
            userInfo.setUserName(userName);
            //对密码进行MD5加密
            if (password != null) {
                userInfo.setPassword(DigestUtils.sha256Hex(password));
            } else {
                userInfo.setPassword(DigestUtils.sha256Hex("123456"));
            }
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setStatus(1);
            userInfo.setInviterCode(inviterCode);
            userInfo.setJifen(100); //默认积分100分
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setCreateTime(sdf.format(new Date()));
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode());
            baseMapper.insert(userInfo);
            //给用户创建钱包
            String format = simpleDateFormat.format(new Date());
            UserMoney userMoney = new UserMoney();
            userMoney.setUserId(userInfo.getUserId());
            userMoney.setMoney(BigDecimal.valueOf(0));
            userMoneyDao.insert(userMoney);
//            dlMoney.setCreateTime(format);
//            dlMoney.setUserId(userInfo.getUserId());
//            dlMoney.setMoney(BigDecimal.valueOf(0));
//            dlMoney.setRozen(BigDecimal.valueOf(0));
//            dlMoneyDao.insert(dlMoney);
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setContent("恭喜您，账号注册成功！");
            messageInfo.setTitle("系统通知");
            messageInfo.setState(String.valueOf(5));
            messageInfo.setUserName(userInfo.getUserName());
            messageInfo.setUserId(String.valueOf(userInfo.getUserId()));
            messageInfo.setCreateAt(sdf.format(new Date()));
            messageInfo.setIsSee("0");
            messageService.saveBody(messageInfo);
//            inviteService.saveBody(userInfo.getUserId(), userEntity);
            return getResult(userInfo);
        }

    }


    @Override
    public Result forgetPwd(String pwd, String phone, String msg) {
        try {
            Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, msg);
            //校验短信验证码
            if (byPhoneAndCode == null) {
                return Result.error("验证码不正确");
            }
            UserEntity userByPhone = queryByPhone(phone);
            userByPhone.setPassword(DigestUtils.sha256Hex(pwd));
            msgDao.deleteById(byPhoneAndCode.getId());
            baseMapper.updateById(userByPhone);
            return Result.success();
        } catch (Exception e) {
            log.error("服务器内部错误", e);
            return Result.error("服务器内部错误");
        }
    }

    @Override
    public Result takingOrdersMessage(Page<Map<String, Object>> iPage, Long type, String date) {
        //接单分析
        return Result.success().put("data", new PageUtils(baseMapper.takingOrdersMessage(iPage, type, date)));
    }

    @Override
    public Result selectUserByUserId(Long userId, Long byUserId) {
        UserEntity userEntity = baseMapper.selectById(userId);
        UserCertification userCertification = userCertificationDao.selectOne(new QueryWrapper<UserCertification>()
                .eq("user_id", userId));
        if (userCertification != null && userCertification.getStatus() == 1) {
            userEntity.setUserName(userCertification.getName());
            return Result.success().put("data", userEntity);
        } else {
            return Result.success().put("data", userEntity);
        }
    }

    @Override
    public HashMap<String, Object> getExtensionData(Long userId) {

        UserEntity userEntity = baseMapper.selectById(userId);
        HashMap<String, Object> map = new HashMap<>();
        Integer countChildren = baseMapper.selectCount(new QueryWrapper<UserEntity>().eq("inviter_code", userEntity.getInvitationCode()));
        Integer countChildrenChildren = baseMapper.selectCount(new QueryWrapper<UserEntity>().eq("inviter_inviter_code", userEntity.getInvitationCode()));
        BigDecimal oneMoney = detailsService.getOneProfit(userId, 1);
        BigDecimal twoMoney = detailsService.getOneProfit(userId, 2);
        map.put("countChildren", countChildren);
        map.put("countChildrenChildren", countChildrenChildren);
        map.put("extensionProfit", oneMoney.add(twoMoney));
        map.put("oneMoney", oneMoney);
        map.put("twoMoney", twoMoney);
        return map;
    }

    @Override
    public IPage<UserEntity> getExtensionList(Long userId, Integer type, Integer page, Integer limit) {
        Page<UserEntity> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        UserEntity loginEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
        if (loginEntity != null) {
            QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
            if (type == 1) {
                queryWrapper.eq("inviter_code", loginEntity.getInvitationCode());
//                return userService.page(pages, new QueryWrapper<UserEntity>().eq("inviter_code", loginEntity.getInvitationCode()));
            } else if (type == 2) {
                queryWrapper.eq("inviter_inviter_code", loginEntity.getInvitationCode());
//                return userService.page(pages, new QueryWrapper<UserEntity>().eq("inviter_inviter_code", loginEntity.getInvitationCode()));
            } else {
                // 如果type为null，返回一级和二级团队成员
                queryWrapper.or().eq("inviter_code", loginEntity.getInvitationCode())
                        .or().eq("inviter_inviter_code", loginEntity.getInvitationCode());
//                return null;
            }
            IPage<UserEntity> userEntityIPage = baseMapper.selectPage(pages, queryWrapper);
            for (UserEntity user : userEntityIPage.getRecords()) {
                UserCertification userCertification = userCertificationDao.selectOne(new QueryWrapper<UserCertification>().eq("user_id", user.getUserId()));
                if (userCertification != null) {
                    user.setStatus1(userCertification.getStatus());
                } else {
                    user.setStatus1(0);
                }
            }
            return userEntityIPage;
        } else {
            return null;
        }
    }

    @Override
    public IPage<UserEntity> getAuthExtensionList(Long userId, Integer page, Integer limit) {

        Page<UserEntity> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        UserEntity loginEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
        return baseMapper.getAuthExtensionList(pages, loginEntity.getInvitationCode());
    }


    @Override
    public IPage<HashMap<String, Object>> getProfitList(Long userId, Integer type, Integer page, Integer limit) {

        Page<HashMap<String, Object>> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return detailsDao.getProfitList(pages, userId, type);


    }

    @Override
    public Result contractAnalysis(String date, int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null || date.isEmpty()) {
            date = simpleDateFormat.format(new Date());
        }
        // 根据时间查出所有新增合同数量
        int count = contractDao.selectAllCount(date, type);
        // 根据时间查出所有新增合同签署完成的数量
        int count1 = contractDao.selectAllCount1(date, type);
        Map<String, Integer> map = new HashMap<>();
        // 根据时间查出所有新增合同数量
        map.put("count", count);
        // 根据时间查出所有新增合同签署完成的数量
        map.put("count1", count1);
        return Result.success().put("data", map);
    }

    @Override
    public Result enterpriseAnalysis(String date, int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null || date.isEmpty()) {
            date = simpleDateFormat.format(new Date());
        }
        // 根据时间查出所有新增企业数量
        int count = enterpriseDao.selectAllCount(date, type);
        return Result.success().put("data", count);
    }

    @Override
    public IPage<HashMap<String, Object>> getUserIncomeList(Long userId, Integer type, Integer page, Integer limit) {
        Page<HashMap<String, Object>> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        UserEntity userEntity = baseMapper.selectById(userId);
        return baseMapper.getUserIncomeList(pages, type, userEntity.getInvitationCode(), userEntity.getUserId());
    }


}
