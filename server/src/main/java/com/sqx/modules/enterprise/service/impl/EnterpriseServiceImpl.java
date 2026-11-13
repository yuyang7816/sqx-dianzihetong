package com.sqx.modules.enterprise.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.dao.CommonInfoDao;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.contract.dao.ContractAndUserDao;
import com.sqx.modules.contract.entity.ContractAndUser;
import com.sqx.modules.contract.service.ContractAndUserService;
import com.sqx.modules.enterprise.dao.EnterpriseAndUserDao;
import com.sqx.modules.enterprise.dao.EnterpriseDao;
import com.sqx.modules.enterprise.entity.Enterprise;
import com.sqx.modules.enterprise.entity.EnterpriseAndUser;
import com.sqx.modules.enterprise.service.EnterpriseService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.sys.entity.SysUserEntity;
import com.sqx.modules.utils.HttpUtil;
import com.sqx.modules.utils.HttpUtils;
import com.sqx.modules.utils.SearchCompanyUtil;
import com.sqx.modules.utils.SenInfoCheckUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.net.URLEncoder;
import java.util.*;

/**
 * @author Admin
 * @description 针对表【enterprise】的数据库操作Service实现
 * @createDate 2024-08-08 15:48:49
 */
@Service
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseDao, Enterprise>
        implements EnterpriseService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private EnterpriseAndUserDao enterpriseAndUserDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ContractAndUserService contractAndUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonInfoDao commonInfoDao;

    @Autowired
    private ContractAndUserDao contractAndUserDao;

    @Override
    public Result addEnterprise(Enterprise enterprise) {
        UserEntity user = userDao.selectById(enterprise.getUserId());
        if (user == null) {
            return Result.error("该用户不存在或未登录，请重新登录");
        }
        QueryWrapper<Enterprise> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enterprise_name", enterprise.getEnterpriseName());
        if (enterprise.getId() != null) {
            queryWrapper.ne("id", enterprise.getId());
        }
        int count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            return Result.error("企业名称已存在");
        }
        if (enterprise.getCreditCode() != null) {
            queryWrapper.eq("credit_code", enterprise.getCreditCode());
            if (enterprise.getId() != null) {
                queryWrapper.ne("id", enterprise.getId());
            }
            int count1 = baseMapper.selectCount(queryWrapper);
            if (count1 > 0) {
                return Result.error("纳税号已存在");
            }
        }
        // 是否自动通过
        String autoPass = commonInfoDao.findOne(433).getValue();
        if ("2".equals(autoPass)) {
            if (enterprise.getId() != null) {
                enterprise.setStatus(1);
                enterprise.setIsReal(1);
                enterprise.setUserId(user.getUserId());
                enterprise.setUpdateTime(new Date());
                enterprise.setUpdateUserId(user.getUserId());
                enterprise.setCreditCode(enterprise.getCreditCode());
                baseMapper.updateById(enterprise);
            } else {
                enterprise.setUserId(user.getUserId());
                enterprise.setStatus(1);
                enterprise.setIsReal(1);
                enterprise.setCreateTime(new Date());
                enterprise.setCreateUserId(user.getUserId());
                enterprise.setCreditCode(enterprise.getCreditCode());
                baseMapper.insert(enterprise);
                EnterpriseAndUser enterpriseAndUser = new EnterpriseAndUser();
                enterpriseAndUser.setUserId(user.getUserId());
                enterpriseAndUser.setEnterpriseId(enterprise.getId());
                enterpriseAndUser.setCreateTime(new Date());
                enterpriseAndUserDao.insert(enterpriseAndUser);
            }
            List<ContractAndUser> contractAndUsers = contractAndUserService.list(new LambdaQueryWrapper<ContractAndUser>()
                    .like(ContractAndUser::getSignUserName, enterprise.getEnterpriseName()));
            if (contractAndUsers != null && !contractAndUsers.isEmpty()) {
                contractAndUsers.forEach(record -> {
                    record.setSignedBy(enterprise.getUserId());
                });
                contractAndUserService.updateBatchById(contractAndUsers);
            }
            Enterprise enterprise1 = baseMapper.selectById(enterprise.getId());
            if (enterprise1 != null && enterprise.getUserId() != null) {
                UserEntity userEntity = userDao.selectById(enterprise1.getUserId());
                if (userEntity != null) {
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setTitle("企业认证结果通知");
                    messageInfo.setContent("您的企业认证结果为：" + (enterprise.getStatus() == 1 ? "通过" : "未通过"));
                    messageInfo.setUserId(String.valueOf(enterprise1.getUserId()));
                    messageInfo.setUserName(userEntity.getUserName());
                    messageInfo.setState(String.valueOf(5));
                    messageInfo.setIsSee("0");
                    messageService.saveBody(messageInfo);
                    if (userEntity.getOpenId() != null) {
                        List<String> msgList = new ArrayList<>();
                        msgList.add("企业认证结果通知");
                        msgList.add("您的企业认证结果为：" + (enterprise.getStatus() == 1 ? "通过" : "未通过"));
                        CommonInfo one = commonInfoDao.findOne(240);
                        SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), one.getValue(), msgList, 3);
                    }
                    if (StringUtils.isNotEmpty(userEntity.getClientid())) {
                        userService.pushToSingle("企业认证结果通知", "您的企业认证结果为：" + (enterprise.getStatus() == 1 ? "通过" : "未通过"), userEntity.getClientid());
                    }
                }
            }
            return Result.success();
        } else {
            if (enterprise.getId() != null) {
                Enterprise enterprise1 = baseMapper.selectById(enterprise.getId());
                if (enterprise1.getStatus() == 2) {
                    enterprise.setStatus(0);
                }
                enterprise.setUserId(user.getUserId());
                enterprise.setUpdateTime(new Date());
                enterprise.setUpdateUserId(user.getUserId());
                enterprise.setCreditCode(enterprise.getCreditCode());
                baseMapper.updateById(enterprise);
            } else {
                enterprise.setUserId(user.getUserId());
                enterprise.setStatus(0);
                enterprise.setIsReal(0);
                enterprise.setCreateTime(new Date());
                enterprise.setCreateUserId(user.getUserId());
                enterprise.setCreditCode(enterprise.getCreditCode());
                baseMapper.insert(enterprise);
                EnterpriseAndUser enterpriseAndUser = new EnterpriseAndUser();
                enterpriseAndUser.setUserId(user.getUserId());
                enterpriseAndUser.setEnterpriseId(enterprise.getId());
                enterpriseAndUser.setCreateTime(new Date());
                enterpriseAndUserDao.insert(enterpriseAndUser);
            }
        }
        return Result.success();
    }

    @Override
    public Result authEnterprise(Enterprise enterprise) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.error("该用户不存在或未登录,请重新登录");
        }
        Enterprise enterprise1 = baseMapper.selectById(enterprise.getId());
        if (enterprise1 == null) {
            return Result.error("该企业不存在");
        }
        if (enterprise1.getStatus() != 0) {
            return Result.error("该企业审批状态已改，请刷新后重新");
        }
        if (StringUtils.isNotBlank(enterprise.getAuditReason())){
            enterprise1.setAuditReason(enterprise.getAuditReason());
        }
        if (enterprise.getStatus() == 1) {
            enterprise1.setStatus(1);
            enterprise1.setIsReal(1);
            List<ContractAndUser> contractAndUsers = contractAndUserService.list(new LambdaQueryWrapper<ContractAndUser>()
                    .like(ContractAndUser::getSignUserName, enterprise.getEnterpriseName()));
            if (contractAndUsers != null && !contractAndUsers.isEmpty()) {
                contractAndUsers.forEach(record -> {
                    record.setSignedBy(enterprise1.getUserId());
                });
                contractAndUserService.updateBatchById(contractAndUsers);
            }
        } else if (enterprise.getStatus() == 2) {
            enterprise1.setStatus(2);
            enterprise1.setIsReal(0);
        }
        baseMapper.updateById(enterprise1);
        if (enterprise.getUserId() != null) {
            UserEntity user = userDao.selectById(enterprise1.getUserId());
            if (user != null) {
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setTitle("企业认证结果通知");
                messageInfo.setContent("您的企业认证结果为：" + (enterprise.getStatus() == 1 ? "通过" : "未通过"));
                messageInfo.setUserId(String.valueOf(enterprise1.getUserId()));
                messageInfo.setUserName(user.getUserName());
                messageInfo.setState(String.valueOf(5));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
                if (user.getOpenId() != null) {
                    List<String> msgList = new ArrayList<>();
                    msgList.add("企业认证结果通知");
                    msgList.add("您的企业认证结果为：" + (enterprise.getStatus() == 1 ? "通过" : "未通过"));
                    CommonInfo one = commonInfoDao.findOne(240);
                    SenInfoCheckUtil.sendMsg(user.getOpenId(), one.getValue(), msgList, 3);
                }
                if (StringUtils.isNotEmpty(user.getClientid())) {
                    userService.pushToSingle("企业认证结果通知", "您的企业认证结果为：" + (enterprise.getStatus() == 1 ? "通过" : "未通过"), user.getClientid());
                }
            }
        }
        return Result.success();
    }

    @Override
    public Result getEnterpriseList(Integer page, Integer limit, Enterprise enterprise) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.error("该用户不存在或未登录,请重新登录");
        }

        Page<Enterprise> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        IPage<Enterprise> list = baseMapper.selectByAllList(pages, enterprise);
        return Result.success().put("data", list);
    }

    @Override
    public Result selectEnterpriseList(Integer page, Integer limit, Enterprise enterprise, Long userId) {
        Page<Enterprise> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("该用户不存在或未登录,请重新登录");
        }
        enterprise.setUserId(userId);
        IPage<Enterprise> list = baseMapper.selectByAllList(pages, enterprise);
        return Result.success().put("data", list);
    }


    @Override
    public Result getEnterpriseInfo(String enterpriseName) {
        String condition = SearchCompanyUtil.search(enterpriseName);
        return Result.success().put("data", condition);
    }



//    @Override
//    public Result getEnterpriseInfo(String enterpriseName) {
//        String host = "https://api.81api.com";
//        String path = "/getCompanyBaseInfo/"+enterpriseName+"/";
//        String method = "GET";
//        String appcode = commonInfoDao.findOne(434).getValue();
//        System.out.println("打印一下++++++++++++++"+ appcode);
//        Map<String, String> headers = new HashMap<String, String>();
//        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
//        headers.put("Authorization", "APPCODE " + appcode);
//        Map<String, String> querys = new HashMap<String, String>();
//        querys.put("isRaiseErrorCode", "0");
//        /**
//         * 重要提示如下:
//         * HttpUtils请从\r\n\t    \t* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java\r\n\t    \t* 下载
//         *
//         * 相应的依赖请参照
//         * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
//         */
//        HttpResponse response = null;
//        try {
//            response = HttpUtils.doGet(host, path, method, headers, querys);
//            // 1. 获取响应体字符串 (必须用 EntityUtils)
//            String jsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
//            JSONObject rootObject = JSONObject.parseObject(jsonString);
//            boolean isSuccess = rootObject.getBooleanValue("status");
//            if (isSuccess) {
//                JSONObject dataObject = rootObject.getJSONObject("data");
//                if (dataObject != null && dataObject.containsKey("list")) {
//                    JSONArray listArray = dataObject.getJSONArray("list");
//                    // 返回成功结果
//                    log.error("查询企业名称返回的数据: " + listArray);
//                    return Result.success().put("data", listArray);
//                } else {
//                    // 状态成功但数据结构不对
//                    return Result.error("接口返回成功，但未找到 list 数据");
//                }
//            } else if (rootObject.getString("errorCode").equals("1101")) {
//                return Result.error("查询关键字至少需要3个汉字");
//            } else if (rootObject.getString("errorCode").equals("1102")) {
//                return Result.error("查询超时,请稍后再试");
//            } else if (rootObject.getString("errorCode").equals("1103")) {
//                return Result.error("阿里服务器异常");
//            }
//        } catch (Exception e) {
//            return Result.error("服务暂时不可用，请稍后重试 (" + e.getMessage() + ")");
//        } finally {
//            // 释放资源
//            if (response != null && response.getEntity() != null) {
//                try {
//                    EntityUtils.consume(response.getEntity());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return Result.error("服务暂时不可用，请稍后重试");
//    }

    @Override
    public Result deleteEnterprise(Long id) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.error("该用户不存在或未登录,请重新登录");
        }
        Enterprise enterprise = baseMapper.selectById(id);
        if (enterprise != null) {
            baseMapper.deleteById(id);
            EnterpriseAndUser enterpriseAndUser = enterpriseAndUserDao.selectOne(new QueryWrapper<EnterpriseAndUser>().eq("enterprise_id", id));
            if (enterpriseAndUser != null) {
                enterpriseAndUserDao.deleteById(enterpriseAndUser.getId());
            }
            return Result.success();
        }
        return Result.error("删除失败");
    }

    @Override
    public Result getEnterpriseDetail(Long id) {
        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (ObjectUtils.isEmpty(principal)) {
            return Result.error("该用户不存在或未登录,请重新登录");
        }
        if (id != null) {
            Enterprise enterprise = baseMapper.selectById(id);
            if (enterprise != null) {
                return Result.success().put("data", enterprise);
            }
        }
        return Result.error("详情查看失败");
    }

    @Override
    public Result appGetEnterpriseDetail(Long id, Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("该用户不存在或未登录,请重新登录");
        }
        if (id != null) {
            Enterprise enterprise = baseMapper.selectByEnterprise(id);
            if (enterprise != null) {
                return Result.success().put("data", enterprise);
            } else {
                return Result.error("企业不存在");
            }
        }
        return Result.error("详情查看失败");
    }
}




