package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserCertificationDao;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserCertification;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserCertificationService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.utils.IdentityCardUtil;
import com.sqx.modules.app.utils.VerifyIdCardUtils;
import com.sqx.modules.common.dao.CommonInfoDao;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.contract.entity.ContractAndUser;
import com.sqx.modules.contract.service.ContractAndUserService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.utils.SenInfoCheckUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class UserCertificationImpl extends ServiceImpl<UserCertificationDao, UserCertification> implements UserCertificationService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonInfoDao commonInfoDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ContractAndUserService contractAndUserService;


    @Override
    public Result insert(Long userId, String name, String idNumber, String front, String back, String address, String phone) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //查询身份证是否被绑定
        UserCertification userCertification = baseMapper.selectOne(new QueryWrapper<UserCertification>()
                .eq("id_number", idNumber)
                .eq("status", 1));
        if (userCertification != null) {
            return Result.error("身份证号已经被绑定，请勿重复认证！");
        } else {
            // 首次实名认证过赠送的签署次数
            int times = Integer.parseInt(commonInfoDao.findOne(424).getValue());
            // 1人工审核  2自动审核
            String value = commonInfoDao.findOne(425).getValue();
            if ("1".equals(value)) {
                //查询是否实名过
                UserCertification userCertification1 = baseMapper.selectOne(new QueryWrapper<UserCertification>().eq("user_id", userId));
                if (userCertification1 == null) {
                    //没有实名过
                    UserCertification userCertification2 = new UserCertification();
                    userCertification2.setRealPhone(phone);
                    userCertification2.setUserId(userId);
                    userCertification2.setIdNumber(idNumber);
                    userCertification2.setName(name);
                    userCertification2.setCreateTime(simpleDateFormat.format(new Date()));
                    userCertification2.setFront(front);
                    if (StringUtils.isNotBlank(address)) {
                        userCertification2.setAddress(address);
                    }
                    userCertification2.setBack(back);
                    userCertification2.setStatus(0);
                    baseMapper.insert(userCertification2);
                    UserEntity userEntity = userDao.selectById(userId);
                    userEntity.setSex(IdentityCardUtil.getSex(idNumber));
                    userEntity.setAge(IdentityCardUtil.getAge(idNumber));
                    userDao.updateById(userEntity);
                    return Result.success("提交成功");
                } else {
                    if (userCertification1.getIsSubmit() != null && userCertification1.getIsSubmit() != 0) {
                        return Result.error("您不可以再次提交，请联系管理员！");
                    }
                    //实名过
                    userCertification1.setName(name);
                    userCertification1.setIdNumber(idNumber);
                    userCertification1.setFront(front);
                    userCertification1.setBack(back);
                    userCertification1.setRealPhone(phone);
                    if (StringUtils.isNotBlank(address)) {
                        userCertification1.setAddress(address);
                    }
                    userCertification1.setStatus(0);
                    userCertification1.setUpdateTime(simpleDateFormat.format(new Date()));
                    baseMapper.update(userCertification1, new QueryWrapper<UserCertification>().eq("user_id", userId));
                    UserEntity userEntity = userDao.selectById(userId);
                    userEntity.setSex(IdentityCardUtil.getSex(idNumber));
                    userEntity.setAge(IdentityCardUtil.getAge(idNumber));
                    userDao.updateById(userEntity);
                    return Result.success("提交成功");
                }
            } else {
                String result = VerifyIdCardUtils.verifyIdCard(idNumber, name);
                if ("ok".equals(result)) {
                    //查询是否实名过
                    UserCertification userCertification1 = baseMapper.selectOne(new QueryWrapper<UserCertification>().eq("user_id", userId));
                    if (userCertification1 == null) {
                        //没有实名过
                        UserCertification userCertification2 = new UserCertification();
                        userCertification2.setUserId(userId);
                        userCertification2.setIdNumber(idNumber);
                        userCertification2.setName(name);
                        userCertification2.setCreateTime(simpleDateFormat.format(new Date()));
                        userCertification2.setFront(front);
                        userCertification2.setRealPhone(phone);
                        if (StringUtils.isNotBlank(address)) {
                            userCertification2.setAddress(address);
                        }
                        userCertification2.setBack(back);
                        userCertification2.setStatus(1);
                        baseMapper.insert(userCertification2);
                        List<ContractAndUser> contractAndUsers = contractAndUserService.list(new LambdaQueryWrapper<ContractAndUser>()
                                .eq(ContractAndUser::getSignUserPhone, phone));
                        if (contractAndUsers != null) {
                            contractAndUsers.forEach(contractAndUser -> {
                                contractAndUser.setSignedBy(userId);
                            });
                        }
                        contractAndUserService.updateBatchById(contractAndUsers);
                        UserEntity userEntity = userDao.selectById(userId);
                        userEntity.setUserName(name);
                        userEntity.setSex(IdentityCardUtil.getSex(idNumber));
                        userEntity.setAge(IdentityCardUtil.getAge(idNumber));
                        userEntity.setIsAuthentication(1);
                        userEntity.setFreeTimes(times);
                        userDao.updateById(userEntity);
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setContent("您的实名认证审核已通过");
                        messageInfo.setTitle("审核通知");
                        messageInfo.setState(String.valueOf(5));
                        messageInfo.setUserName(userEntity.getUserName());
                        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                        messageInfo.setCreateAt(DateUtils.format(new Date()));
                        messageInfo.setIsSee("0");
                        messageService.saveBody(messageInfo);
                        if (userEntity.getOpenId() != null) {
                            List<String> msgList = new ArrayList<>();
                            msgList.add("实名认证消息提醒");
                            msgList.add("您的实名认证审核已通过");
                            CommonInfo one = commonInfoDao.findOne(240);
                            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 3);
                        }
                        if (StringUtils.isNotEmpty(userEntity.getClientid())) {
                            userService.pushToSingle("审核通知", "您的实名认证审核已通过", userEntity.getClientid());
                        }
                        return Result.success("提交成功");
                    } else {
                        if (userCertification1.getIsSubmit() != null && userCertification1.getIsSubmit() != 0) {
                            return Result.error("您不可以再次提交，请联系管理员！");
                        }
                        //实名过
                        userCertification1.setName(name);
                        userCertification1.setIdNumber(idNumber);
                        userCertification1.setFront(front);
                        userCertification1.setBack(back);
                        if (StringUtils.isNotBlank(address)) {
                            userCertification1.setAddress(address);
                        }
                        userCertification1.setRealPhone(phone);
                        userCertification1.setStatus(1);
                        userCertification1.setUpdateTime(simpleDateFormat.format(new Date()));
                        baseMapper.update(userCertification1, new QueryWrapper<UserCertification>().eq("user_id", userId));
                        List<ContractAndUser> contractAndUsers = contractAndUserService.list(new LambdaQueryWrapper<ContractAndUser>()
                                .eq(ContractAndUser::getSignUserPhone, phone));
                        if (contractAndUsers != null) {
                            contractAndUsers.forEach(contractAndUser -> {
                                contractAndUser.setSignedBy(userId);
                            });
                            contractAndUserService.updateBatchById(contractAndUsers);
                        }
                        UserEntity userEntity = userDao.selectById(userId);
                        userEntity.setSex(IdentityCardUtil.getSex(idNumber));
                        userEntity.setAge(IdentityCardUtil.getAge(idNumber));
                        userEntity.setIsAuthentication(1);
                        userEntity.setUserName(name);
                        userEntity.setFreeTimes(times);
                        userDao.updateById(userEntity);
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setContent("您的实名认证审核已通过");
                        messageInfo.setTitle("审核通知");
                        messageInfo.setState(String.valueOf(5));
                        messageInfo.setUserName(userEntity.getUserName());
                        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                        messageInfo.setCreateAt(DateUtils.format(new Date()));
                        messageInfo.setIsSee("0");
                        messageService.saveBody(messageInfo);
                        if (userEntity.getOpenId() != null) {
                            List<String> msgList = new ArrayList<>();
                            msgList.add("实名认证消息提醒");
                            msgList.add("您的实名认证审核已通过");
                            CommonInfo one = commonInfoDao.findOne(240);
                            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 3);
                        }
                        if (StringUtils.isNotEmpty(userEntity.getClientid())) {
                            userService.pushToSingle("审核通知", "您的实名认证审核已通过", userEntity.getClientid());
                        }
                        return Result.success("提交成功");
                    }
                } else {
                    return Result.error("您填写的信息有误，请检查后重试！");
                }
            }
        }
    }

    @Override
    public Result isInsert(Long userId) {
        UserCertification userCertification = baseMapper.selectOne(new QueryWrapper<UserCertification>()
                .eq("user_id", userId).eq("status", 1));
        if (userCertification != null) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    public static Integer getAgeByCertId(String certId) {
        String birthday = "";
        if (certId.length() == 18) {
            birthday = certId.substring(6, 10) + "/"
                    + certId.substring(10, 12) + "/"
                    + certId.substring(12, 14);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date now = new Date();
        Date birth = new Date();
        try {
            birth = sdf.parse(birthday);
        } catch (ParseException e) {
        }
        long intervalMilli = now.getTime() - birth.getTime();
        int age = (int) (intervalMilli / (24 * 60 * 60 * 1000)) / 365;
        return age;
    }


    @Override
    public Result queryCertification(Long page, Long limit, String status, String name) {

        if (page == null || limit == null) {
            return Result.error("分页参数为空");
        } else {
            IPage<UserCertification> iPage = new Page(page, limit);
            String nname = null;
            if (name != null && !(name.equals(""))) {
                nname = "%" + name + "%";
            }
            IPage iPage1 = baseMapper.queryCertification(iPage, status, nname);
            List<UserCertification> userCertifications = iPage1.getRecords();
            for (UserCertification userCertification : userCertifications) {
                if (userCertification != null) {
                    //关联用户
                    userCertification.setUserEntity(userDao.selectById(userCertification.getUserId()));
                }
            }
            return Result.success().put("data", new PageUtils(iPage1));
        }
    }

    @Override
    public Result queryUserCertification(IPage iPage, String name, String phone) {
        String nname = null;
        if (name != null && !(name.equals(""))) {
            nname = "%" + name + "%";
        }
        String nphone = null;
        if (phone != null && !(phone.equals(""))) {
            nphone = "%" + phone + "%";
        }

        return Result.success().put("data", baseMapper.queryUserCertification(iPage, nname, nphone));
    }

    @Override
    public Result auditorUserCertification(Integer status, Long userId, String remek, Long id, Integer isSubmit) {
        // 首次实名认证过赠送的签署次数
        int times = Integer.parseInt(commonInfoDao.findOne(424).getValue());
        UserCertification userCertification = baseMapper.selectOne(new QueryWrapper<UserCertification>().eq("id", id));
        if (userCertification != null) {
            userId = userCertification.getUserId();
            if (status == 1) {
                UserEntity user = new UserEntity();
                user.setUserId(userId);
                user.setIsAuthentication(1);
                user.setFreeTimes(times);
                user.setUserName(userCertification.getName());
                userDao.updateById(user);
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("您的实名认证审核已通过");
                messageInfo.setTitle("审核通知");
                messageInfo.setState(String.valueOf(5));
                messageInfo.setUserName(user.getUserName());
                messageInfo.setUserId(String.valueOf(user.getUserId()));
                messageInfo.setCreateAt(DateUtils.format(new Date()));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
                if (user.getOpenId() != null) {
                    List<String> msgList = new ArrayList<>();
                    msgList.add("实名认证消息提醒");
                    msgList.add("您的实名认证审核已通过");
                    CommonInfo one = commonInfoDao.findOne(240);
                    SenInfoCheckUtil.sendMsg(user.getOpenId(), one.getValue(), msgList, 3);
                }
                if (StringUtils.isNotEmpty(user.getClientid())) {
                    userService.pushToSingle("审核通知", "您的实名认证审核已通过", user.getClientid());
                }
                userCertification.setStatus(1);
                if (StringUtils.isNotBlank(remek)) {
                    userCertification.setRemark(remek);
                }
                if (isSubmit != null) {
                    userCertification.setIsSubmit(isSubmit);
                }
                baseMapper.update(userCertification, new QueryWrapper<UserCertification>().eq("user_id", userId));
                List<ContractAndUser> contractAndUsers = contractAndUserService.list(new LambdaQueryWrapper<ContractAndUser>()
                        .eq(ContractAndUser::getSignUserPhone, userCertification.getRealPhone()));
                if (contractAndUsers != null) {
                    contractAndUsers.forEach(contractAndUser -> {
                        contractAndUser.setSignedBy(userCertification.getUserId());
                    });
                    contractAndUserService.updateBatchById(contractAndUsers);
                }
                return Result.success();
            } else {
                UserEntity user = new UserEntity();
                user.setUserId(userId);
                user.setUpdateTime(DateUtils.format(new Date()));
                userDao.updateById(user);
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("您的实名认证审核未通过");
                messageInfo.setTitle("审核通知");
                messageInfo.setState(String.valueOf(5));
                messageInfo.setUserName(user.getUserName());
                messageInfo.setUserId(String.valueOf(user.getUserId()));
                messageInfo.setCreateAt(DateUtils.format(new Date()));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
                if (user.getOpenId() != null) {
                    List<String> msgList = new ArrayList<>();
                    msgList.add("实名认证消息提醒");
                    msgList.add("您的实名认证审核未通过");
                    CommonInfo one = commonInfoDao.findOne(240);
                    SenInfoCheckUtil.sendMsg(user.getOpenId(), one.getValue(), msgList, 3);
                }
                if (StringUtils.isNotEmpty(user.getClientid())) {
                    userService.pushToSingle("审核通知", "您的实名认证审核未通过", user.getClientid());
                }
                userCertification.setStatus(2);
                if (StringUtils.isNotBlank(remek)) {
                    userCertification.setRemark(remek);
                }
                if (isSubmit != null) {
                    userCertification.setIsSubmit(isSubmit);
                }
                baseMapper.update(userCertification, new QueryWrapper<UserCertification>().eq("user_id", userId));
                return Result.success();
            }
        } else {
            return Result.error("要审核的信息不存在！");
        }

    }

    @Override
    public Result queryInsert(Long userId) {
        return Result.success().put("data", baseMapper.selectOne(new QueryWrapper<UserCertification>().eq("user_id", userId)));
    }


}
