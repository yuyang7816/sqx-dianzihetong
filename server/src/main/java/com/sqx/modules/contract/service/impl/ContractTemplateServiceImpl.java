package com.sqx.modules.contract.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itextpdf.text.DocumentException;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserCertificationDao;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserCertification;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.contract.dao.*;
import com.sqx.modules.contract.entity.*;
import com.sqx.modules.contract.service.ContractTemplateService;
import com.sqx.modules.contract.vo.ContractSignVO;
import com.sqx.modules.contract.vo.ContractUserVO;
import com.sqx.modules.contract.vo.ContractVO;
import com.sqx.modules.contract.vo.DataJsonVo;
import com.sqx.modules.enterprise.dao.EnterpriseDao;
import com.sqx.modules.enterprise.entity.Enterprise;
import com.sqx.modules.file.utils.FileUploadUtils;
import com.sqx.modules.message.dao.MessageInfoDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.sys.entity.SysUserEntity;
import com.sqx.modules.utils.SenInfoCheckUtil;
import com.sqx.modules.utils.TemplateProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Admin
 * @description 针对表【contract_template(合同模板表)】的数据库操作Service实现
 * @createDate 2024-08-08 18:13:51
 */
@Service
@Slf4j
public class ContractTemplateServiceImpl extends ServiceImpl<ContractTemplateDao, ContractTemplate> implements ContractTemplateService {

    @Autowired
    private ContractTypeDao contractTypeDao;
    @Autowired
    private ContractTemplateFieldsDao contractTemplateFieldsDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ContractAdditionalFieldsDao contractAdditionalFieldsDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ContractAndUserDao contractAndUserDao;
    @Autowired
    private UserCertificationDao userCertificationDao;
    private final TemplateEngine templateEngine;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private EnterpriseDao enterpriseDao;

    public ContractTemplateServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public Result addContractTemplate(ContractTemplate contractTemplate) {
        SysUserEntity sysUser = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (sysUser == null) {
            return Result.error("用户未登录，请重新登录");
        }
        QueryWrapper<ContractTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_name", contractTemplate.getTemplateName());
        if (contractTemplate.getId() != null) {
            queryWrapper.ne("id", contractTemplate.getId());
        }
        int count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            return Result.error("模板名称重复");
        }
        if (contractTemplate.getId() != null) {
            contractTemplate.setUpdateUserId(sysUser.getUserId());
            contractTemplate.setUpdateTime(new Date());
            if (contractTemplate.getSort() != null) {
                contractTemplate.setSort(contractTemplate.getSort());
            }
            baseMapper.updateById(contractTemplate);
            // 这里是除了模板以外额外的一些字段数据操作，例如：是买卖合同包括物品名称、物品数量等字段
            List<ContractTemplateFields> templateFields = contractTemplate.getTemplateFields();
            // 提取 fieldKey 列表（按顺序）
            List<String> fieldKeys = new ArrayList<>();
            if (templateFields != null) {
                contractTemplateFieldsDao.delete(new QueryWrapper<ContractTemplateFields>().eq("template_id", contractTemplate.getId()));
                for (ContractTemplateFields templateField : templateFields) {
                    templateField.setTemplateId(contractTemplate.getId());
                    contractTemplateFieldsDao.insert(templateField);
                    fieldKeys.add(templateField.getFiledValue());
                }
                String originalTemplateUrl = contractTemplate.getContractUrl();
                String originalFilename = getFileNameFromUrl(originalTemplateUrl);
                if (!fieldKeys.isEmpty()) {
                    // 执行替换：下划线 → {fieldKey}
                    Path processedTemplate = null;
                    try {
                        processedTemplate = TemplateProcessor.convertUnderlinesToPlaceholders(
                                originalTemplateUrl, fieldKeys
                        );
                    } catch (Exception e) {
                        log.error("替换Word模板占位符失败", e);
                        return Result.error("模板处理失败：" + e.getMessage());
                    }
                    // 上传回你的文件服务器（或保存到本地固定目录）
                    String newTemplatePath = uploadProcessedTemplate(processedTemplate, originalTemplateUrl);
                    // 更新数据库中的 template_path 为新文件路径
                    contractTemplate.setContractUrl(newTemplatePath);
                    baseMapper.updateById(contractTemplate);
                }
            }
        } else {
            contractTemplate.setCreateUserId(sysUser.getUserId());
            contractTemplate.setCreateTime(new Date());
            if (contractTemplate.getSort() != null) {
                contractTemplate.setSort(contractTemplate.getSort());
            }
            baseMapper.insert(contractTemplate);
            // 这里是除了模板以外额外的一些字段数据操作，例如：是买卖合同包括物品名称、物品数量等字段
            List<ContractTemplateFields> templateFields = contractTemplate.getTemplateFields();
            // 提取 fieldKey 列表（按顺序）
            List<String> fieldKeys = new ArrayList<>();
            if (templateFields != null) {
                contractTemplateFieldsDao.delete(new QueryWrapper<ContractTemplateFields>().eq("template_id", contractTemplate.getId()));
                for (ContractTemplateFields templateField : templateFields) {
                    templateField.setTemplateId(contractTemplate.getId());
                    contractTemplateFieldsDao.insert(templateField);
                    fieldKeys.add(templateField.getFiledValue());
                }
                String originalTemplateUrl = contractTemplate.getContractUrl();
                String originalFilename = getFileNameFromUrl(originalTemplateUrl);
                if (!fieldKeys.isEmpty()) {
                    // 执行替换：下划线 → {fieldKey}
                    Path processedTemplate = null;
                    try {
                        processedTemplate = TemplateProcessor.convertUnderlinesToPlaceholders(
                                originalFilename, fieldKeys
                        );
                    } catch (Exception e) {
                        log.error("替换Word模板占位符失败", e);
                        return Result.error("模板处理失败：" + e.getMessage());
                    }
                    // 上传回你的文件服务器（或保存到本地固定目录）
                    String newTemplatePath = uploadProcessedTemplate(processedTemplate, originalFilename);
                    // 更新数据库中的 template_path 为新文件路径
                    contractTemplate.setContractUrl(newTemplatePath);
                    baseMapper.updateById(contractTemplate);
                }
            }
        }
        return Result.success();
    }

    private String getFileNameFromUrl(String originalTemplateUrl) {
        try {
            URL uri = new URL(originalTemplateUrl);
            String path = uri.getPath();
            return path.substring(path.lastIndexOf("/") + 1);
        } catch (MalformedURLException e) {
            int q = originalTemplateUrl.indexOf("?");
            if (q > 0) originalTemplateUrl = originalTemplateUrl.substring(0, q);
            return originalTemplateUrl.substring(originalTemplateUrl.indexOf("/") + 1);
        }
    }

    private String uploadProcessedTemplate(Path processedTemplate, String originalTemplateUrl) {
        return this.uploadFile(processedTemplate, originalTemplateUrl);
    }

    public String uploadFile(Path localFile, String originalFilename) {
        String value = commonInfoService.findOne(234).getValue();
        if ("1".equals(value)) {
            // === 阿里云 OSS ===
            String endpoint = commonInfoService.findOne(68).getValue();
            String accessKeyId = commonInfoService.findOne(69).getValue();
            String accessKeySecret = commonInfoService.findOne(70).getValue();
            String bucketName = commonInfoService.findOne(71).getValue();
            String suffix;
            int dotIndex = originalFilename.lastIndexOf(".");
            if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
                suffix = originalFilename.substring(dotIndex);
            } else {
                suffix = ".docx";
            }
            String completePath = getPath(suffix);
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            InputStream inputStream = null;
            try {
                inputStream = Files.newInputStream(localFile);
            } catch (IOException e) {
                log.error("创建文件输入流失败", e);
            }
            ossClient.putObject(bucketName, completePath, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            return commonInfoService.findOne(19).getValue() + "/img/" + completePath;
        } else {
            // === 本地存储 ===
            String http = commonInfoService.findOne(19).getValue();
            String[] split = http.split("://");
            String domain = split[1];
            String filePath = "/www/wwwroot/" + domain + "/file/uploadPath/";
            String webBaseUrl = "/file/uploadPath/";
            String[] allowedExtensions = {"docx", "doc", "pdf", "jpg", "png"};
            // 复用 FileUploadUtils
            String fileName = null;
            try {
                String originalFilename1 = getFileNameFromUrl(originalFilename);
                fileName = FileUploadUtils.upload1(filePath, localFile.toFile(), originalFilename1, allowedExtensions);
            } catch (IOException e) {
                log.error("新的文件上传失败", e);
            }
            return http + webBaseUrl + fileName;
//            return http + fileName;
        }
    }


    private String getPath(String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path = format(new Date()) + "/" + uuid;
        return path + suffix;
    }

    private String format(Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            return df.format(date);
        }
        return null;
    }

    private String getFiledValueFromJson(Integer id, List<ContractTemplateFields> templateFields) {
        for (ContractTemplateFields templateField : templateFields) {
            if (templateField.getId().equals(id)) {
                return templateField.getFieldName();
            }
        }
        return null;
    }

    private String getFieldNameFromJson(Integer id, List<ContractTemplateFields> templateFields) {
        for (ContractTemplateFields templateField : templateFields) {
            if (templateField.getId().equals(id)) {
                return templateField.getFiledValue();
            }
        }
        return null;
    }

    @Override
    public Result deleteContractTemplate(Long id) {
        SysUserEntity sysUser = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (sysUser == null) {
            return Result.error("用户未登录，请重新登录");
        }
        if (id != null) {
            ContractTemplate contractTemplate = baseMapper.selectById(id);
            if (contractTemplate != null) {
                baseMapper.deleteById(id);
                List<ContractTemplateFields> templateFields = contractTemplateFieldsDao.selectByContractId(id);
                if (templateFields != null) {
                    contractTemplateFieldsDao.delete(new QueryWrapper<ContractTemplateFields>().eq("template_id", id));
                }
                return Result.success();
            }
        }
        return Result.error("删除失败");
    }

    @Override
    public Result getContractTemplateDetail(Long id) {
        SysUserEntity sysUser = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (sysUser == null) {
            return Result.error("用户未登录，请重新登录");
        }
        if (id != null) {
            ContractTemplate contractTemplate = baseMapper.selectById(id);
            if (contractTemplate != null) {
                contractTemplate.setTemplateFields(contractTemplateFieldsDao.selectByTemplateId(id));
                return Result.success().put("data", contractTemplate);
            }
        }
        return Result.error("查询失败,未获取到模板");
    }

    @Override
    public Result getContractTemplateList(Integer page, Integer limit, ContractTemplate contractTemplate, Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在或未登录，请重新登录");
        }
        IPage<ContractType> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        List<ContractType> contractTypes = contractTypeDao.selectByAll();
        for (ContractType contractType : contractTypes) {
            LambdaQueryWrapper<ContractTemplate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ContractTemplate::getTypeId, contractType.getId());
            List<ContractTemplate> templates = baseMapper.selectList(wrapper);
            contractType.setContractTemplateList(templates);
        }
        return Result.success().put("data", contractTypes);
    }

    @Override
    public Result selectContractTemplateList(Integer page, Integer limit, ContractTemplate contractTemplate) {
        IPage<ContractTemplate> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        IPage<ContractTemplate> contractTemplateList = baseMapper.selectByAllList(pages, contractTemplate);
        List<Long> templateIds = contractTemplateList.getRecords().stream()
                .map(ContractTemplate::getId)
                .collect(Collectors.toList());
        List<ContractTemplateFields> templateFields = new ArrayList<>();
        if (!templateIds.isEmpty()) {
            templateFields = contractTemplateFieldsDao.selectList(new QueryWrapper<ContractTemplateFields>()
                    .in("template_id", templateIds));
        }
        final List<ContractTemplateFields> finalTemplateFields = templateFields;
        contractTemplateList.getRecords().forEach(contractTemplate1 -> {
            contractTemplate1.setTemplateFields(finalTemplateFields.stream()
                    .filter(templateField -> templateField.getTemplateId().equals(contractTemplate1.getId()))
                    .collect(Collectors.toList()));
        });
        return Result.success().put("data", contractTemplateList);
    }

    @Override
    public Result selectContractTemplateList1(ContractTemplate contractTemplate) {
        Long templateIds = contractTemplate.getId();
        if (templateIds == null) {
            return Result.success().put("data", new ArrayList<>());
        }
        List<ContractTemplateFields> templateFields = contractTemplateFieldsDao.selectList(new QueryWrapper<ContractTemplateFields>()
                .in("template_id", templateIds));
        return Result.success().put("data", templateFields);
    }

    private String generateOrder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
        int i = (int) ((Math.random() * 9 + 1) * 10000);
        return format + i;
    }

    @Override
    public Result initiatingContract(ContractVO contractVO) {
        if (contractVO != null) {
            UserEntity user = userDao.selectById(contractVO.getUserId());
            if (user == null) {
                return Result.error("用户不存在或未登录，请重新登录");
            }
            // 合同编号前缀
            String contractNumberNO = commonInfoService.findOne(427).getValue();
            UserCertification userCertification = userCertificationDao.selectOne(new QueryWrapper<UserCertification>().eq("user_id", contractVO.getUserId()));
            if (userCertification == null || userCertification.getStatus() != 1) {
                return Result.error("请先完成实名认证");
            }
            if (user.getFreeTimes() > 0) {
                user.setFreeTimes(user.getFreeTimes() - 1);
                ContractTemplate contractTemplate = baseMapper.selectById(contractVO.getTemplateId());
                if (contractTemplate != null) {
                    Contract contract = new Contract();
                    if (contractVO.getLateSignDays() != null) {
                        contract.setLateSignDays(contractVO.getLateSignDays());
                    } else {
                        int day = Integer.parseInt(commonInfoService.findOne(429).getValue());
                        contract.setLateSignDays(day);
                    }
                    contract.setIsComplete(0);
                    contract.setTemplateId(contractTemplate.getId());
                    contract.setContractName(contractTemplate.getTemplateName());
                    if (contractVO.getContractNumber() != null) {
                        contract.setContractNumber(contractNumberNO + contractVO.getContractNumber());
                    } else {
                        contract.setContractNumber(contractNumberNO + contractTemplate.getTemplateName() + contractTemplate.getId());
                    }
                    contract.setContractType(1);
//                    List<ContractTemplateFields> classList = contractTemplateFieldsDao.selectList(new LambdaQueryWrapper<ContractTemplateFields>()
//                            .eq(ContractTemplateFields::getTemplateId, contractTemplate.getId()));
//                    if (contractVO.getDataJson() != null) {
//                        List<DataJsonVo> dataJson = contractVO.getDataJson();
//                        for (DataJsonVo dataJsonVo : dataJson) {
//                            for (ContractTemplateFields tableColumn : classList) {
//                                if (dataJsonVo.getHtmlType() == 2 || dataJsonVo.getHtmlType() == 3 || dataJsonVo.getHtmlType() == 4 || dataJsonVo.getHtmlType() == 5) {
//                                    if (dataJsonVo.getColumnName().equals(tableColumn.getColumnName())) {
////                                    dataJsonVo.setOptionJson(tableColumn.getOptionJson());
//                                    }
//                                }
//                            }
//                        }
//                        contract.setDataJson(dataJson);
//                    }
                    contract.setContractNumber(contractNumberNO + generateOrder());
                    contract.setUserId(user.getUserId());
                    contract.setCreateTime(new Date());
                    contractDao.insert(contract);
                    contractVO.setContractId(contract.getId());
                    userDao.updateById(user);
//                    if (contractVO.getType() == 1) {
                    if (contractVO.getUserName() != null && (contractVO.getIsPrincipal() == 1 || contractVO.getIsPrincipal() == 2)) {
                        contractVO.setIsPrincipal(contractVO.getIsPrincipal());
                        addContractAndUser(contractVO, user.getUserId());
                    }
                    if (contractVO.getSignUserName() != null) {
                        addContractSignUser(contractVO);
                    } else {
                        return Result.error("请填写签署人信息");
                    }
//                        return Result.success().put("data", contractVO.getContractId());
//                    } else if (contractVO.getType() == 2) {
//                        if (contractVO.getUserName() != null && (contractVO.getIsPrincipal() == 1 || contractVO.getIsPrincipal() == 2)) {
//                            contractVO.setIsPrincipal(contractVO.getIsPrincipal());
//                            addContractAndUser(contractVO, user.getUserId());
//                        }
//                        if (contractVO.getSignUserName() != null) {
//                            addContractSignUser(contractVO);
//                        } else {
//                            return Result.error("请填写签署人信息");
//                        }
//                    }
                    contractVO.setTemplatePath(contractTemplate.getContractUrl());
                    return Result.success().put("data", contractVO);
                } else {
                    return Result.error("查询失败,未获取到模板");
                }
            } else {
                return Result.error("您的次数已用完，请先购买合同套餐");
            }
        }
        return Result.error("参数为空，请检查后重试");
    }

    private void sendMsgByContract(Contract contract) {
        List<ContractAndUser> contractAndUserList = contractAndUserDao.selectList(new QueryWrapper<ContractAndUser>()
                .eq("contract_id", contract.getId()));
        for (ContractAndUser contractAndUser1 : contractAndUserList) {
            if (contractAndUser1.getSignedBy() != null) {
                UserEntity userEntity = userDao.selectById(contractAndUser1.getSignedBy());
                if (userEntity != null) {
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                    messageInfo.setTitle("合同签署通知");
                    messageInfo.setContent("您有要签署的合同，合同号为：" + contract.getContractNumber());
                    messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    messageInfo.setUserName(userEntity.getUserName());
                    messageInfo.setIsSee("0");
                    messageInfo.setState(String.valueOf(5));
                    messageInfoDao.insert(messageInfo);
                    if (userEntity.getOpenId() != null) {
                        List<String> msgList = new ArrayList<>();
                        msgList.add("合同签署通知");
                        msgList.add("您有要签署的合同，合同号为：" + contract.getContractNumber());
                        String value = commonInfoService.findOne(240).getValue();
                        SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), value, msgList, 3);
                    }
                    if (userEntity.getClientid() != null) {
                        userService.pushToSingle("合同签署通知", "您有要签署的合同，合同号为：" + contract.getContractNumber(), userEntity.getClientid());
                    }
                    if (userEntity.getPhone() != null) {
                        userService.sendMsg(userEntity.getPhone(), "contract");
                    }
                }
            }
        }
        ContractAndUser contractAndUser1 = contractAndUserList.get(0);
        if (contractAndUser1.getCopyUser() != null) {
            UserEntity userEntity1 = userDao.selectById(contractAndUser1.getCopyUser());
            if (userEntity1 != null) {
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setUserId(String.valueOf(userEntity1.getUserId()));
                messageInfo.setTitle("合同抄送通知");
                messageInfo.setContent("您有一份抄送合同，合同号为：" + contract.getContractNumber());
                messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                messageInfo.setUserName(userEntity1.getUserName());
                messageInfo.setIsSee("0");
                messageInfo.setState(String.valueOf(5));
                messageInfoDao.insert(messageInfo);
                if (userEntity1.getOpenId() != null) {
                    List<String> msgList = new ArrayList<>();
                    msgList.add("合同抄送通知");
                    msgList.add("您有一份抄送合同，合同号为：" + contract.getContractNumber());
                    String value = commonInfoService.findOne(240).getValue();
                    SenInfoCheckUtil.sendMsg(userEntity1.getOpenId(), value, msgList, 3);
                }
                if (userEntity1.getClientid() != null) {
                    userService.pushToSingle("合同抄送通知", "您有一份抄送合同，合同号为：" + contract.getContractNumber(), userEntity1.getClientid());
                }
            }
        }

    }

    private void addContractSignUser(ContractVO contractVO) {
        ContractAndUser contractAndUser1 = contractAndUserDao.selectOne(new LambdaQueryWrapper<ContractAndUser>()
                .eq(ContractAndUser::getContractId, contractVO.getContractId())
                .isNotNull(ContractAndUser::getSignUserName));
        Contract contract = contractDao.selectById(contractVO.getContractId());
        if (contractAndUser1 != null) {
            List<ContractAndUser> contractAndUser2 = contractAndUserDao.selectList(new QueryWrapper<ContractAndUser>()
                    .eq("contract_id", contractVO.getContractId()));
            if (contractAndUser2.get(0).getIsPrincipal() == 1) {
                contractAndUser1.setIsPrincipal(2);
            } else if ((contractAndUser2.get(0).getIsPrincipal() == 2)) {
                contractAndUser1.setIsPrincipal(1);
            }
            contractAndUser1.setType(contractVO.getType());
            contractAndUser1.setContractId(contractVO.getContractId());
            contractAndUser1.setUpdateTime(new Date());
            contractAndUser1.setAgreementType(contractVO.getAgreementType());
            if (contract.getLateSignDays() != null) {
                contractAndUser1.setLastDate(DateUtils.addDateDays(new Date(), contract.getLateSignDays()));
            } else {
                int day = Integer.parseInt(commonInfoService.findOne(429).getValue());
                contractAndUser1.setLastDate(DateUtils.addDateDays(new Date(), day));
            }
            contractAndUser1.setStartTime(new Date());
            contractAndUser1.setSignUserName(contractVO.getSignUserName());
            if (StringUtils.isNotBlank(contractVO.getSignUserPhone())) {
                contractAndUser1.setSignUserPhone(contractVO.getSignUserPhone());
                UserEntity userEntity = userService.getOne(new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getPhone, contractVO.getSignUserPhone()));
                if (userEntity != null) {
                    contractAndUser1.setSignedBy(userEntity.getUserId());
                }
            } else {
                Enterprise enterprise = enterpriseDao.selectOne(new LambdaQueryWrapper<Enterprise>()
                        .eq(Enterprise::getEnterpriseName, contractVO.getSignUserName()));
                if (enterprise != null) {
                    contractAndUser1.setSignedBy(enterprise.getUserId());
                }
            }
            if (contractVO.getIdCardNumber() != null) {
                contractAndUser1.setIdCardNumber(contractVO.getIdCardNumber());
            }
            contractAndUser1.setStatus(0);
            contractAndUser1.setSignOrApproved(0);
            contractAndUserDao.updateById(contractAndUser1);
        } else {
            List<ContractAndUser> contractAndUser2 = contractAndUserDao.selectList(new QueryWrapper<ContractAndUser>()
                    .eq("contract_id", contractVO.getContractId()));
            ContractAndUser contractAndUser = new ContractAndUser();
            if (contractAndUser2.get(0).getIsPrincipal() == 1) {
                contractAndUser.setIsPrincipal(2);
            } else if ((contractAndUser2.get(0).getIsPrincipal() == 2)) {
                contractAndUser.setIsPrincipal(1);
            }
            contractAndUser.setAgreementType(contractVO.getAgreementType());
            contractAndUser.setType(contractVO.getType());
            contractAndUser.setContractId(contractVO.getContractId());
            contractAndUser.setCreateTime(new Date());
            if (contract.getLateSignDays() != null) {
                contractAndUser.setLastDate(DateUtils.addDateDays(new Date(), contract.getLateSignDays()));
            } else {
                int day = Integer.parseInt(commonInfoService.findOne(429).getValue());
                contractAndUser.setLastDate(DateUtils.addDateDays(new Date(), day));
            }
            contractAndUser.setStartTime(new Date());
            contractAndUser.setSignUserName(contractVO.getSignUserName());
            if (StringUtils.isNotBlank(contractVO.getSignUserPhone())) {
                contractAndUser.setSignUserPhone(contractVO.getSignUserPhone());
                UserEntity userEntity = userService.getOne(new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getPhone, contractVO.getSignUserPhone()));
                if (userEntity != null) {
                    contractAndUser.setSignedBy(userEntity.getUserId());
                }
            } else {
                Enterprise enterprise = enterpriseDao.selectOne(new LambdaQueryWrapper<Enterprise>()
                        .eq(Enterprise::getEnterpriseName, contractVO.getSignUserName()));
                if (enterprise != null) {
                    contractAndUser.setSignedBy(enterprise.getUserId());
                }
            }
            if (contractVO.getIdCardNumber() != null) {
                contractAndUser.setIdCardNumber(contractVO.getIdCardNumber());
            }
            contractAndUser.setStatus(0);
            contractAndUser.setSignOrApproved(0);
            contractAndUserDao.insert(contractAndUser);
        }
    }

    private void addContractAndUser(ContractVO contractVO, Long userId) {
        ContractAndUser contractAndUser1 = contractAndUserDao.selectOne(new LambdaQueryWrapper<ContractAndUser>()
                .eq(ContractAndUser::getContractId, contractVO.getContractId())
                .eq(ContractAndUser::getUserId, userId));
        Contract contract = contractDao.selectById(contractVO.getContractId());
        if (contractAndUser1 != null) {
            contractAndUser1.setContractId(contractVO.getContractId());
            contractAndUser1.setUpdateTime(new Date());
            contractAndUser1.setUserId(userId);
            contractAndUser1.setIsPrincipal(contractVO.getIsPrincipal());
            contractAndUser1.setUserName(contractVO.getUserName());
            contractAndUser1.setType(contractVO.getType());
            contractAndUser1.setStartTime(new Date());
            if (contract.getLateSignDays() != null) {
                contractAndUser1.setLastDate(DateUtils.addDateDays(new Date(), contract.getLateSignDays()));
            } else {
                int day = Integer.parseInt(commonInfoService.findOne(429).getValue());
                contractAndUser1.setLastDate(DateUtils.addDateDays(new Date(), day));
            }
            if (contractVO.getUserLegalPerson() != null) {
                contractAndUser1.setUserLegalPerson(contractVO.getUserLegalPerson());
            }
            if (contractVO.getUserPhone() != null) {
                contractAndUser1.setUserPhone(contractVO.getUserPhone());
            }
            if (contractVO.getIdCard() != null) {
                contractAndUser1.setIdCard(contractVO.getIdCard());
            }
            contractAndUser1.setStatus(0);
            contractAndUser1.setSignOrApproved(0);
            contractAndUserDao.updateById(contractAndUser1);
        } else {
            ContractAndUser contractAndUser = new ContractAndUser();
            contractAndUser.setContractId(contractVO.getContractId());
            contractAndUser.setCreateTime(new Date());
            contractAndUser.setUserId(userId);
            contractAndUser.setIsPrincipal(contractVO.getIsPrincipal());
            contractAndUser.setUserName(contractVO.getUserName());
            contractAndUser.setType(contractVO.getType());
            contractAndUser.setStartTime(new Date());
            if (contract.getLateSignDays() != null) {
                contractAndUser.setLastDate(DateUtils.addDateDays(new Date(), contract.getLateSignDays()));
            } else {
                int day = Integer.parseInt(commonInfoService.findOne(429).getValue());
                contractAndUser.setLastDate(DateUtils.addDateDays(new Date(), day));
            }
            if (contractVO.getUserLegalPerson() != null) {
                contractAndUser.setUserLegalPerson(contractVO.getUserLegalPerson());
            }
            if (contractVO.getUserPhone() != null) {
                contractAndUser.setUserPhone(contractVO.getUserPhone());
            }
            if (contractVO.getIdCard() != null) {
                contractAndUser.setIdCard(contractVO.getIdCard());
            }
            contractAndUser.setStatus(0);
            contractAndUser.setSignOrApproved(0);
            contractAndUserDao.insert(contractAndUser);
        }
    }

    @Override
    public Result selectSignatory(ContractUserVO contractUserVO) {
        if (contractUserVO != null) {
            UserEntity user = userDao.selectById(contractUserVO.getUserId());
            if (user == null) {
                return Result.error("用户不存在或未登录，请重新登录");
            }
            Contract contract = contractDao.selectById(contractUserVO.getContractId());
            List<ContractAndUser> contractAndUsers = new ArrayList<>();
            if (contract != null) {
                if (contractUserVO.getType() == 1) {
                    for (UserEntity userEntity : contractUserVO.getUserList()) {
                        ContractAndUser contractAndUser = new ContractAndUser();
                        contractAndUser.setContractId(contract.getId());
                        contractAndUser.setCreateTime(new Date());
                        int count = contractAndUserDao.selectCount(new QueryWrapper<ContractAndUser>()
                                .eq("contract_id", contract.getId())
                                .eq("user_id", user.getUserId()));
                        if (count > 0) {
                            return Result.error("该用户已参与该合同,请勿重复添加");
                        }
                        contractAndUser.setUserId(user.getUserId());
                        int countResult = contractAndUserDao.selectCount(new QueryWrapper<ContractAndUser>()
                                .eq("contract_id", contract.getId())
                                .eq("user_id", userEntity.getUserId()));
                        if (countResult > 0) {
                            return Result.error("该用户已参与该合同,请勿重复添加");
                        }
                        contractAndUser.setSignedBy(userEntity.getUserId());
                        contractAndUser.setStatus(1);
                        contractAndUser.setSignOrApproved(1);
//                    }
                        if (contractUserVO.getCopyUser() != null) {
                            contractAndUser.setCopyUser(contractUserVO.getCopyUser());
                        }
                        contractAndUsers.add(contractAndUser);
                    }
                    contractAndUserDao.insertBatch(contractAndUsers);
                    List<ContractAndUser> contractAndUser1 = contractAndUserDao.selectList(new QueryWrapper<ContractAndUser>()
                            .eq("contract_id", contract.getId()));
                    for (ContractAndUser contractAndUser : contractAndUser1) {
                        UserEntity userEntity = userDao.selectById(contractAndUser.getUserId());
                        if (userEntity != null) {
                            MessageInfo messageInfo = new MessageInfo();
                            messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                            messageInfo.setTitle("合同签署通知");
                            messageInfo.setContent("您有要签署的合同，合同号为：" + contract.getContractNumber());
                            messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            messageInfo.setUserName(userEntity.getUserName());
                            messageInfo.setIsSee("0");
                            messageInfo.setState(String.valueOf(5));
                            messageInfoDao.insert(messageInfo);
                            if (userEntity.getOpenId() != null) {
                                List<String> msgList = new ArrayList<>();
                                msgList.add("合同签署通知");
                                msgList.add("您有要签署的合同，合同号为：" + contract.getContractNumber());
                                String value = commonInfoService.findOne(240).getValue();
                                SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), value, msgList, 3);
                            }
                            if (userEntity.getClientid() != null) {
                                userService.pushToSingle("合同签署通知", "您有要签署的合同，合同号为：" + contract.getContractNumber(), userEntity.getClientid());
                            }
                        }
                    }
                    ContractAndUser contractAndUser = contractAndUser1.get(0);
                    UserEntity userEntity1 = userDao.selectById(contractAndUser.getCopyUser());
                    if (userEntity1 != null) {
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setUserId(String.valueOf(userEntity1.getUserId()));
                        messageInfo.setTitle("合同抄送通知");
                        messageInfo.setContent("您有一份抄送合同，合同号为：" + contract.getContractNumber());
                        messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        messageInfo.setUserName(userEntity1.getUserName());
                        messageInfo.setIsSee("0");
                        messageInfo.setState(String.valueOf(5));
                        messageInfoDao.insert(messageInfo);
                        if (userEntity1.getOpenId() != null) {
                            List<String> msgList = new ArrayList<>();
                            msgList.add("合同抄送通知");
                            msgList.add("您有一份抄送合同，合同号为：" + contract.getContractNumber());
                            String value = commonInfoService.findOne(240).getValue();
                            SenInfoCheckUtil.sendMsg(userEntity1.getOpenId(), value, msgList, 3);
                        }
                        if (userEntity1.getClientid() != null) {
                            userService.pushToSingle("合同抄送通知", "您有一份抄送合同，合同号为：" + contract.getContractNumber(), userEntity1.getClientid());
                        }
                    }
                    return Result.success();
                } else if (contractUserVO.getType() == 2) {
                    for (UserEntity userEntity : contractUserVO.getUserList()) {
                        ContractAndUser contractAndUser = new ContractAndUser();
                        contractAndUser.setContractId(contract.getId());
                        contractAndUser.setCreateTime(new Date());
                        int count = contractAndUserDao.selectCount(new QueryWrapper<ContractAndUser>().eq("contract_id", contract.getId())
                                .eq("user_id", user.getUserId()));
                        if (count > 0) {
                            return Result.error("该用户已参与该合同,请勿重复添加");
                        }
                        contractAndUser.setUserId(user.getUserId());
                        int countResult = contractAndUserDao.selectCount(new QueryWrapper<ContractAndUser>()
                                .eq("contract_id", contract.getId())
                                .eq("user_id", userEntity.getUserId()));
                        if (countResult > 0) {
                            return Result.error("该用户已参与该合同,请勿重复添加");
                        }
                        contractAndUser.setSignedBy(contractUserVO.getSignedBy());
                        contractAndUser.setStatus(0);
                        contractAndUser.setSignOrApproved(0);
                        if (contractUserVO.getCopyUser() != null) {
                            contractAndUser.setCopyUser(contractAndUser.getCopyUser());
                        }
                        contractAndUsers.add(contractAndUser);
                    }
                    contractAndUserDao.insertBatch(contractAndUsers);
                }
                List<ContractAndUser> contractAndUserList = contractAndUserDao.selectList(new QueryWrapper<ContractAndUser>()
                        .eq("contract_id", contract.getId()));
                for (ContractAndUser contractAndUser1 : contractAndUserList) {
                    UserEntity userEntity = userDao.selectById(contractAndUser1.getSignedBy());
                    if (userEntity != null) {
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                        messageInfo.setTitle("合同签署通知");
                        messageInfo.setContent("您有要签署的合同，合同号为：" + contract.getContractNumber());
                        messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        messageInfo.setUserName(userEntity.getUserName());
                        messageInfo.setIsSee("0");
                        messageInfo.setState(String.valueOf(5));
                        messageInfoDao.insert(messageInfo);
                        if (userEntity.getOpenId() != null) {
                            List<String> msgList = new ArrayList<>();
                            msgList.add("合同签署通知");
                            msgList.add("您有要签署的合同，合同号为：" + contract.getContractNumber());
                            String value = commonInfoService.findOne(240).getValue();
                            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), value, msgList, 3);
                        }
                        if (userEntity.getClientid() != null) {
                            userService.pushToSingle("合同签署通知", "您有要签署的合同，合同号为：" + contract.getContractNumber(), userEntity.getClientid());
                        }
                    }
                }
                ContractAndUser contractAndUser1 = contractAndUserList.get(0);
                UserEntity userEntity1 = userDao.selectById(contractAndUser1.getCopyUser());
                if (userEntity1 != null) {
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setUserId(String.valueOf(userEntity1.getUserId()));
                    messageInfo.setTitle("合同抄送通知");
                    messageInfo.setContent("您有一份抄送合同，合同号为：" + contract.getContractNumber());
                    messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    messageInfo.setUserName(userEntity1.getUserName());
                    messageInfo.setIsSee("0");
                    messageInfo.setState(String.valueOf(5));
                    messageInfoDao.insert(messageInfo);
                    if (userEntity1.getOpenId() != null) {
                        List<String> msgList = new ArrayList<>();
                        msgList.add("合同抄送通知");
                        msgList.add("您有一份抄送合同，合同号为：" + contract.getContractNumber());
                        String value = commonInfoService.findOne(240).getValue();
                        SenInfoCheckUtil.sendMsg(userEntity1.getOpenId(), value, msgList, 3);
                    }
                    if (userEntity1.getClientid() != null) {
                        userService.pushToSingle("合同抄送通知", "您有一份抄送合同，合同号为：" + contract.getContractNumber(), userEntity1.getClientid());
                    }
                }
                return Result.success();
            } else {
                return Result.error("该合同不存在");
//                }
            }
        }
        return Result.error("参数为空，请检查后重试");
    }

    @Override
    public Result selectMySigned(Integer page, Integer limit, Long userId) {
        IPage<Contract> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        if (userId != null) {
            UserEntity user = userDao.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在或未登录，请重新登录");
            }
            Page<Contract> list = contractDao.selectMySigned(pages, userId);
            if (!list.getRecords().isEmpty()) {
                for (Contract contract : list.getRecords()) {
//                    contract.setContractAndUsers(contractAndUserDao.selectByContractId(contract.getId()));
//                contract.setContractAdditionalFields(contractAdditionalFieldsDao.selectByContractId(contract.getId()));
                    List<ContractAndUser> caList = contractAndUserDao.selectByContractId1(contract.getId());
                    contract.setContractAndUsers(caList);
                    ContractAndUser initiator = caList.stream()
                            .findFirst()
                            .orElse(null);
                    if (initiator != null) {
                        contract.setUserName(initiator.getUserName());
                    }
                }
            }
            return Result.success().put("data", list);
        } else {
            return Result.error("未获取到当前用户id！");
        }
    }

    @Override
    public Result selectMyReceive(Integer page, Integer limit, Long userId) {
        IPage<Contract> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        Page<Contract> list = contractDao.selectMyReceive(pages, userId);
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在或未登录，请重新登录");
        }
        if (!list.getRecords().isEmpty()) {
            for (Contract contract : list.getRecords()) {
                // 查出发起人和合同关联表
                List<ContractAndUser> caList = contractAndUserDao.selectByContractId1(contract.getId());
                contract.setContractAndUsers(caList);
                ContractAndUser initiator = caList.stream()
                        .findFirst()
                        .orElse(null);
                if (initiator != null) {
                    contract.setUserName(initiator.getUserName());
                }
//                contract.setContractAndUsers(contractAndUserDao.selectByContractId1(contract.getId()));
                // 查出合同附加字段
//            contract.setContractAdditionalFields(contractAdditionalFieldsDao.selectByContractId(contract.getId()));
            }
        }
        return Result.success().put("data", list);
    }

    @Override
    public Result selectByContractDetail(Long id, Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在或未登录，请重新登录");
        }
        Contract contract = contractDao.selectById(id);
        if (contract != null) {
            ContractTemplate contractTemplate = baseMapper.selectById(contract.getTemplateId());
            if (contractTemplate != null) {
                contract.setTemplateUrl(contractTemplate.getContractImg());
            }
            UserEntity user1 = userDao.selectById(contract.getUserId());
            contract.setUserAvatar(user1.getAvatar());
            // 查出发起人和合同关联表
            List<ContractAndUser> caList = contractAndUserDao.selectByContractId1(contract.getId());
            contract.setContractAndUsers(caList);
            ContractAndUser initiator = caList.stream()
                    .findFirst()
                    .orElse(null);
            if (initiator != null) {
                contract.setUserName(initiator.getUserName());
            }
            // 查出合同附加字段
//            contract.setContractAdditionalFields(contractAdditionalFieldsDao.selectByContractId(contract.getId()));
            return Result.success().put("data", contract);
        } else {
            return Result.error("合同不存在");
        }
    }

    @Override
    public Result selectAllContractList(Integer page, Integer limit, Long userId, String contractName, Integer isComplete) {
        IPage<Contract> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("该用户不存在！");
        }
        if (contractName != null) {
            contractName = contractName.trim();
        }
        Page<Contract> list = contractDao.selectMyAllContractList(pages, userId, contractName, isComplete);
        if (list.getRecords().isEmpty()) {
            return Result.success().put("data", list);
        }
        for (Contract contract : list.getRecords()) {
            // 查出发起人和合同关联表
            List<ContractAndUser> caList = contractAndUserDao.selectByContractId1(contract.getId());
            contract.setContractAndUsers(caList);
            ContractAndUser initiator = caList.stream()
                    .findFirst()
                    .orElse(null);
            if (initiator != null) {
                contract.setUserName(initiator.getUserName());
            }
            // 查出合同附加字段
//            contract.setContractAdditionalFields(contractAdditionalFieldsDao.selectByContractId(contract.getId()));
        }
        return Result.success().put("data", list);
    }

    @Override
    public Result userReceivedContract(Integer page, Integer limit, Long userId) {
        IPage<Contract> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>(1, 100);
        }
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("该用户不存在！");
        }
        IPage<Contract> list;
        List<ContractAndUser> contractAndUser = contractAndUserDao.selectList(new LambdaQueryWrapper<ContractAndUser>()
                .eq(ContractAndUser::getSignedBy, userId));
        Set<Long> contractIds = contractAndUser.stream()
                .map(ContractAndUser::getContractId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (!contractIds.isEmpty()) {
            list = contractDao.selectPage(pages, new LambdaQueryWrapper<Contract>()
                    .in(Contract::getId, contractIds));
            if (list.getRecords().isEmpty()) {
                return Result.success().put("data", list);
            }
            for (Contract contract : list.getRecords()) {
                // 查出发起人和合同关联表
                List<ContractAndUser> caList = contractAndUserDao.selectByContractId(contract.getId());
                contract.setContractAndUsers(caList);
                ContractAndUser initiator = caList.stream()
                        .findFirst()
                        .orElse(null);
                if (initiator != null) {
                    contract.setUserName(initiator.getUserName());
                }
                // 查出合同附加字段
//                contract.setContractAdditionalFields(contractAdditionalFieldsDao.selectByContractId(contract.getId()));
            }
            return Result.success().put("data", list);
        } else {
            list = new Page<>(1, 100);
            list.setRecords(Collections.emptyList());
            list.setTotal(0);
            return Result.success().put("data", list);
        }
    }

    @Override
    public Result copyReceivedContract(Integer page, Integer limit, Long userId) {
        IPage<Contract> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>(1, 100);
        }
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("该用户不存在！");
        }
        IPage<Contract> list;
        List<ContractAndUser> contractAndUser = contractAndUserDao.selectList(new LambdaQueryWrapper<ContractAndUser>()
                .eq(ContractAndUser::getCopyUser, userId));
        Set<Long> contractIds = contractAndUser.stream()
                .map(ContractAndUser::getContractId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (!contractIds.isEmpty()) {
            list = contractDao.selectPage(pages, new LambdaQueryWrapper<Contract>()
                    .in(Contract::getId, contractIds));
            if (list.getRecords().isEmpty()) {
                return Result.success().put("data", list);
            }
            for (Contract contract : list.getRecords()) {
                // 查出发起人和合同关联表
                List<ContractAndUser> caList = contractAndUserDao.selectByContractId(contract.getId());
                contract.setContractAndUsers(caList);
                ContractAndUser initiator = caList.stream()
                        .findFirst()
                        .orElse(null);
                if (initiator != null) {
                    contract.setUserName(initiator.getUserName());
                }

                // 查出合同附加字段
//                contract.setContractAdditionalFields(contractAdditionalFieldsDao.selectByContractId(contract.getId()));
            }
            return Result.success().put("data", list);
        } else {
            list = new Page<>(1, 100);
            list.setRecords(Collections.emptyList());
            list.setTotal(0);
            return Result.success().put("data", list);
        }
    }

    @Override
    public Result getAllContractList(Integer page, Integer limit, Contract contract) {
        IPage<Contract> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>(1, 100);
        }
        IPage<Contract> list = contractDao.selectAllContractList(pages, contract);
        if (list.getRecords().isEmpty()) {
            return Result.success().put("data", list);
        }
        for (Contract contract1 : list.getRecords()) {
            // 查出合同关联表
            List<ContractAndUser> contractAndUser = contractAndUserDao.selectByContractId1(contract1.getId());
            contract1.setContractAndUsers(contractAndUser);
            ContractAndUser initiator = contractAndUser.stream()
                    .findFirst()
                    .orElse(null);
            if (initiator != null) {
                contract1.setUserName(initiator.getUserName());
            }
        }
        return Result.success().put("data", list);
    }

    @Override
    public Result updateContract(ContractVO contractVO, Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在或未登录，请重新登录！");
        }
        Contract contract = contractDao.selectById(contractVO.getContractId());
        if (contract != null) {
            if (contract.getIsComplete() == 3) {
                return Result.error("合同已过期，请重新发起！");
            }
            if (contract.getUserId().equals(userId)) {
                ContractAndUser contractAndUser = contractAndUserDao.selectOne(new LambdaQueryWrapper<ContractAndUser>()
                        .eq(ContractAndUser::getContractId, contractVO.getContractId())
                        .eq(ContractAndUser::getUserId, userId));
                if (contractAndUser != null) {
                    contractAndUser.setStatus(2);
                    contractAndUser.setIsSign(1);
                    contractAndUser.setSignDate(new Date());
                    contractAndUserDao.updateById(contractAndUser);
                    sendMsgByContract(contract);
                }
                contract.setFileUrl(contractVO.getPdfImageUrl());
                if (contract.getContractType() == 4) {
                    contract.setIsComplete(1);
                }
                contractDao.updateById(contract);
                return Result.success();
            } else {
                ContractAndUser contractAndUser = contractAndUserDao.selectOne(new LambdaQueryWrapper<ContractAndUser>()
                        .eq(ContractAndUser::getContractId, contractVO.getContractId())
                        .eq(ContractAndUser::getSignedBy, userId));
                if (contractAndUser != null) {
                    contractAndUser.setStatus(2);
                    contractAndUser.setIsSign(1);
                    contractAndUser.setSignDate(new Date());
                    contractAndUserDao.updateById(contractAndUser);
                }
                contract.setIsComplete(1);
                contract.setFileUrl(contractVO.getPdfImageUrl());
                contractDao.updateById(contract);
                return Result.success();
            }
        }
        return Result.error("合同不存在！");
    }

    @Override
    public Result selectRecipients(ContractUserVO contractVO, Long userId) {
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity == null) {
            return Result.error("用户不存在或未登录，请重新登录！");
        }
        Contract contract = contractDao.selectById(contractVO.getContractId());
        if (contract == null) {
            return Result.error("合同不存在！");
        }
        List<ContractAndUser> contractAndUsers = contractAndUserDao.selectList(new LambdaQueryWrapper<ContractAndUser>()
                .eq(ContractAndUser::getContractId, contractVO.getContractId()));
        ContractAndUser contractAndUser1 = contractAndUsers.get(0);
        ContractAndUser contractAndUser = new ContractAndUser();
        contractAndUser.setContractId(contractVO.getContractId());
        contractAndUser.setCreateTime(new Date());
        contractAndUser.setType(contractAndUser1.getType());
        contractAndUser.setStartTime(new Date());
        if (StringUtils.isBlank(contractVO.getPhone()) || StringUtils.isBlank(contractVO.getName())) {
            return Result.error("收件人名称或手机号不能为空");
        }
        UserEntity user = userDao.selectOne(new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getPhone, contractVO.getPhone()));
        if (user != null) {
            contractAndUser.setCopyUser(user.getUserId());
        }
        contractAndUser.setReceivingUserName(contractVO.getName());
        contractAndUser.setReceivingEmail(contractVO.getPhone());
        contractAndUserDao.insert(contractAndUser);
        if (user != null) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setUserId(String.valueOf(user.getUserId()));
            messageInfo.setTitle("合同抄送通知");
            messageInfo.setContent("您有一份抄送合同，合同号为：" + contract.getContractNumber());
            messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            messageInfo.setUserName(user.getUserName());
            messageInfo.setIsSee("0");
            messageInfo.setState(String.valueOf(5));
            messageInfoDao.insert(messageInfo);
            if (user.getOpenId() != null) {
                List<String> msgList = new ArrayList<>();
                msgList.add("合同抄送通知");
                msgList.add("您有一份抄送合同，合同号为：" + contract.getContractNumber());
                String value = commonInfoService.findOne(240).getValue();
                SenInfoCheckUtil.sendMsg(user.getOpenId(), value, msgList, 3);
            }
            if (user.getClientid() != null) {
                userService.pushToSingle("合同抄送通知", "您有一份抄送合同，合同号为：" + contract.getContractNumber(), user.getClientid());
            }
            if (user.getPhone() != null) {
                userService.sendMsg(user.getPhone(), "copyContract");
            }
        }
        return Result.success();
    }

    @Override
    public Result deleteDraftBox(ContractVO contractVO, Long userId) {
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity == null) {
            return Result.error("用户不存在，请重新登录！");
        }
        Contract contract = contractDao.selectById(contractVO.getContractId());
        if (contract == null) {
            return Result.error("合同不存在！");
        }
        if (!contract.getUserId().equals(userId)) {
            return Result.error("您没有权限删除该合同！");
        }
        contractDao.deleteById(contractVO.getContractId());
        contractAndUserDao.delete(new LambdaQueryWrapper<ContractAndUser>()
                .eq(ContractAndUser::getContractId, contractVO.getContractId()));
        return Result.success("删除成功！");
    }

    @Override
    public Result selectAllContractList1(Integer page, Integer limit, Long userId) {
        IPage<Contract> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("该用户不存在！");
        }
        IPage<Contract> list = contractDao.selectPage(pages, new LambdaQueryWrapper<Contract>()
                .eq(Contract::getUserId, userId));
        if (list.getRecords().isEmpty()) {
            return Result.success().put("data", list);
        }
        for (Contract contract : list.getRecords()) {
            // 查出发起人和合同关联表
            List<ContractAndUser> caList = contractAndUserDao.selectByContractId(contract.getId());
            contract.setContractAndUsers(caList);
            ContractAndUser initiator = caList.stream()
                    .findFirst()
                    .orElse(null);
            if (initiator != null) {
                contract.setUserName(initiator.getUserName());
            }
            // 查出合同附加字段
//            contract.setContractAdditionalFields(contractAdditionalFieldsDao.selectByContractId(contract.getId()));
        }
        return Result.success().put("data", list);
    }

    @Override
    public Result selectByMyCopyList(Integer page, Integer limit, Long userId) {
        IPage<Contract> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在或未登录，请重新登录");
        }
        Page<Contract> list = contractDao.selectMyCopyList(pages, userId);
        if (userId != null) {
            if (!list.getRecords().isEmpty()) {
                for (Contract contract : list.getRecords()) {
                    // 查出发起人和合同关联表
                    contract.setContractAndUsers(contractAndUserDao.selectByContractId1(contract.getId()));
                    // 查出合同附加字段
//                contract.setContractAdditionalFields(contractAdditionalFieldsDao.selectByContractId(contract.getId()));
                }
            }
            return Result.success().put("data", list);
        }
        return Result.error("合同不存在");
    }

    @Override
    public Result selectStatistics(Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在或未登录，请重新登录");
        }
        Map<String, Object> map = new HashMap<>();
        if (userId != null) {
            // 查询我收到的合同数量
            Integer myReceive = contractDao.selectCountByMyReceive(userId);
            map.put("myReceive", myReceive == null ? 0 : myReceive);
            // 查询我发起的合同数量
            Integer mySigned = contractDao.selectCountByMySigned(userId);
            map.put("mySigned", mySigned == null ? 0 : mySigned);
            // 查询抄送我的合同的数量
            Integer myCopy = contractDao.selectCountByMyCopy(userId);
            map.put("myCopy", myCopy == null ? 0 : myCopy);
            // 查询待我操作的合同数量
            Integer myOperate = contractDao.selectCountByMyOperate(userId);
            map.put("myOperate", myOperate == null ? 0 : myOperate);
            // 查询待他人操作的合同数量
            Integer otherOperate = contractDao.selectCountByOtherOperate(userId);
            map.put("otherOperate", otherOperate == null ? 0 : otherOperate);
            // 查询已完成的合同数量
            Integer myFinish = contractDao.selectCountByMyFinish(userId);
            map.put("myFinish", myFinish == null ? 0 : myFinish);
        }
        return Result.success().put("data", map);
    }

    @Override
    public Result selectMySignature(Long userId) {
        if (userId != null) {
            UserEntity user = userDao.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在或未登录，请重新登录");
            }
            Map<String, Object> map = new HashMap<>();
            // 待我签署的合同数量
            Integer WaitForSign = contractDao.selectCountByWaitForSign(userId);
            map.put("WaitForSign", WaitForSign == null ? 0 : WaitForSign);
//            // 待我审批的合同数量
//            Integer WaitForApproved = contractDao.selectCountByWaitForApproved(userId);
//            map.put("WaitForApproved", WaitForApproved == null ? 0 : WaitForApproved);
            // 草稿箱的合同数量
            Integer DraftBox = contractDao.selectCountByDraftBox(userId);
            map.put("DraftBox", DraftBox == null ? 0 : DraftBox);
            // 我发起的合同数量
            Integer mySigned = contractDao.selectCount(new LambdaQueryWrapper<Contract>()
                    .eq(Contract::getUserId, userId)
                    .eq(Contract::getDraftStatus, 0));
            map.put("mySigned", mySigned == null ? 0 : mySigned);
            // 我收到的合同数量
            Integer myReceive = contractDao.selectCountByMyReceive(userId);
            map.put("myReceive", myReceive == null ? 0 : myReceive);
            return Result.success().put("data", map);
        }
        return Result.error("用户不存在或未登录，请重新登录");
    }

    @Override
    public Result goSignContract(ContractSignVO contractSignVO) {
        if (contractSignVO != null) {
            UserEntity user = userDao.selectById(contractSignVO.getUserId());
            if (user == null) {
                return Result.error("用户不存在或未登录，请重新登录");
            }
            // 当前日期只包含年月日
            Date now = getCurrentDateOnlyYearMonthDay();
            ContractAndUser contractAndUser = contractAndUserDao.selectByUserIdOrSignedBy(contractSignVO.getContractId(), contractSignVO.getUserId());
            if (contractAndUser != null) {
                if (contractAndUser.getLastDate().before(now)) {
                    return Result.error("合同已过期,请勿操作");
                }
                if (contractSignVO.getSignOrApproved() == 1) {
                    contractAndUser.setStatus(3);
                    contractAndUser.setIsSign(1);
                    contractAndUser.setSignDate(new Date());
                    if (contractAndUserDao.updateById(contractAndUser) > 0) {
                        Contract contract = contractDao.selectById(contractSignVO.getContractId());
                        contract.setIsComplete(2);
                        if (contractSignVO.getPdfImageUrl() != null) {
                            contract.setFileUrl(contractSignVO.getPdfImageUrl());
                        }
                        contractDao.updateById(contract);
                        return Result.success();
                    } else {
                        return Result.error("操作失败");
                    }
                } else {
                    contractAndUser.setStatus(2);
                    contractAndUser.setIsSign(1);
                    contractAndUser.setSignDate(new Date());
                    contractAndUser.setSignUrl(contractSignVO.getSignUrl());
                    if (contractAndUserDao.updateById(contractAndUser) > 0) {
                        Contract contract = contractDao.selectById(contractSignVO.getContractId());
                        contract.setIsComplete(1);
                        if (contractSignVO.getPdfImageUrl() != null) {
                            contract.setFileUrl(contractSignVO.getPdfImageUrl());
                        }
                        contractDao.updateById(contract);
                        return Result.success();
                    } else {
                        return Result.error("操作失败");
                    }
                }
            }
        }
        return Result.error("参数有误，请检查后再试！");
    }

    @Override
    public Result selectDraftBox(Integer page, Integer limit, Long userId) {
        if (userId != null) {
            UserEntity user = userDao.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在或未登录，请重新登录");
            }
            IPage<Contract> pages;
            if (page != null && limit != null) {
                pages = new Page<>(page, limit);
            } else {
                pages = new Page<>();
                pages.setSize(-1);
            }
            Page<Contract> list = contractDao.selectDraftBoxList(pages, userId);
            if (!list.getRecords().isEmpty()) {
                for (Contract contract : list.getRecords()) {
                    // 查出发起人和合同关联表
                    List<ContractAndUser> caList = contractAndUserDao.selectByContractId1(contract.getId());
                    contract.setContractAndUsers(caList);
                    ContractAndUser initiator = caList.stream()
                            .findFirst()
                            .orElse(null);
                    if (initiator != null) {
                        contract.setUserName(initiator.getUserName());
                    }
//                    contract.setContractAndUsers(contractAndUserDao.selectByContractId1(contract.getId()));
                    // 查出合同附加字段
//                contract.setContractAdditionalFields(contractAdditionalFieldsDao.selectByContractId(contract.getId()));
                }
            }
            return Result.success().put("data", list);
        }
        return Result.error("合同不存在");
    }


    @Override
    public Result addSingleDraftBox(ContractVO contractVO, Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在或未登录，请重新登录");
        }
        UserCertification userCertification = userCertificationDao.selectOne(new QueryWrapper<UserCertification>().eq("user_id", user.getUserId()));
        if (userCertification == null || userCertification.getStatus() != 1) {
            return Result.error("请先完成实名认证");
        }
        if (contractVO != null) {
            if (contractVO.getContractId() != null) {
                Contract contract = contractDao.selectById(contractVO.getContractId());
                if (contract != null) {
                    if (contractVO.getContractName() != null) {
                        contract.setContractName(contractVO.getContractName());
                    }
                    contract.setCreateTime(new Date());
                    contract.setDraftStatus(1);
                    if (contractVO.getDataJson() != null) {
                        contract.setDataJson(contractVO.getDataJson());
                    }
                    if (contractVO.getLateSignDays() != null) {
                        contract.setLateSignDays(contractVO.getLateSignDays());
                    } else {
                        int day = Integer.parseInt(commonInfoService.findOne(429).getValue());
                        contract.setLateSignDays(day);
                    }
                    if (contractVO.getPdfImageUrl() != null) {
                        contract.setFileUrl(contractVO.getPdfImageUrl());
                    }
                    contractDao.updateById(contract);
                    contractVO.setContractId(contract.getId());
                    contractVO.setUserId(userId);
                    if (contractVO.getType() == 1) {
                        if (contractVO.getUserName() != null && (contractVO.getIsPrincipal() == 1 || contractVO.getIsPrincipal() == 2)) {
                            contractVO.setIsPrincipal(contractVO.getIsPrincipal());
                            addContractAndUser(contractVO, user.getUserId());
                        }
                        return Result.success().put("data", contractVO);
                    } else if (contractVO.getType() == 2) {
                        if (contractVO.getUserName() != null && (contractVO.getIsPrincipal() == 1 || contractVO.getIsPrincipal() == 2)) {
                            contractVO.setIsPrincipal(contractVO.getIsPrincipal());
                            addContractAndUser(contractVO, user.getUserId());
                        }
                    }
                    ContractTemplate contractTemplate = baseMapper.selectById(contractVO.getTemplateId());
                    if (contractTemplate != null) {
                        contractVO.setTemplatePath(contractTemplate.getContractUrl());
                    }
                    return Result.success().put("data", contractVO);
                }
            } else {
                // 合同编号前缀
                String contractNumberNO = commonInfoService.findOne(427).getValue();
                ContractTemplate contractTemplate = baseMapper.selectById(contractVO.getTemplateId());
                Contract contract = new Contract();
                if (contractTemplate != null) {
                    contract.setContractType(1);
                    contract.setTemplateId(contractTemplate.getId());
                    contract.setContractName(contractTemplate.getTemplateName());
                    contractVO.setTemplatePath(contractTemplate.getContractUrl());
                } else {
                    contract.setContractType(2);
                }
                if (contractVO.getContractName() != null) {
                    contract.setContractName(contractVO.getContractName());
                }
                if (contractVO.getLateSignDays() != null) {
                    contract.setLateSignDays(contractVO.getLateSignDays());
                } else {
                    int day = Integer.parseInt(commonInfoService.findOne(429).getValue());
                    contract.setLateSignDays(day);
                }
                contract.setIsComplete(0);
                contract.setDraftStatus(1);
                if (contractVO.getDataJson() != null) {
                    contract.setDataJson(contractVO.getDataJson());
                }
                if (contractVO.getPdfImageUrl() != null) {
                    contract.setFileUrl(contractVO.getPdfImageUrl());
                }
                contract.setContractNumber(contractNumberNO + generateOrder());
                contract.setUserId(user.getUserId());
                contract.setCreateTime(new Date());
                contractDao.insert(contract);
                contractVO.setContractId(contract.getId());
                if (contractVO.getType() == 1) {
                    if (contractVO.getUserName() != null && (contractVO.getIsPrincipal() == 1 || contractVO.getIsPrincipal() == 2)) {
                        contractVO.setIsPrincipal(contractVO.getIsPrincipal());
                        addContractAndUser(contractVO, user.getUserId());
                    }
                    return Result.success().put("data", contractVO);
                } else if (contractVO.getType() == 2) {
                    if (contractVO.getUserName() != null && (contractVO.getIsPrincipal() == 1 || contractVO.getIsPrincipal() == 2)) {
                        contractVO.setIsPrincipal(contractVO.getIsPrincipal());
                        addContractAndUser(contractVO, user.getUserId());
                    }
                }
                return Result.success().put("data", contractVO);
            }
        }
        return Result.error("参数为空，请检查后重试");
    }

    @Override
    public Result retractContract(ContractVO contractVO, Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在或未登录，请重新登录");
        }
        Contract contract = contractDao.selectById(contractVO.getContractId());
        if (contract == null) {
            return Result.error("合同不存在");
        }
        if (!contract.getUserId().equals(userId)) {
            return Result.error("您没有权限操作此合同");
        }
        if (contract.getIsComplete() != 0) {
            return Result.error("当前合同不允许撤回，请检查");
        }
        List<ContractAndUser> list = contractAndUserDao.selectList(new QueryWrapper<ContractAndUser>().eq("contract_id", contractVO.getContractId()));
        for (ContractAndUser contractAndUser : list) {
            if (contractAndUser.getSignUserName() != null || contractAndUser.getSignedBy() != null) {
                contractAndUserDao.deleteById(contractAndUser.getId());
            }
            if (contractAndUser.getCopyUser() != null || contractAndUser.getCopyUserName() != null || contractAndUser.getReceivingUserName() != null) {
                contractAndUserDao.deleteById(contractAndUser.getId());
            }
        }
        contract.setDraftStatus(1);
        contractDao.updateById(contract);
        return Result.success();
    }

    @Override
    public Result addDraftBox(ContractVO contractVO, Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在或未登录，请重新登录");
        }
        UserCertification userCertification = userCertificationDao.selectOne(new QueryWrapper<UserCertification>().eq("user_id", user.getUserId()));
        if (userCertification == null || userCertification.getStatus() != 1) {
            return Result.error("请先完成实名认证");
        }
        if (contractVO != null) {
            if (contractVO.getContractId() != null) {
                Contract contract = contractDao.selectById(contractVO.getContractId());
                if (contract != null) {
                    if (contractVO.getContractName() != null) {
                        contract.setContractName(contractVO.getContractName());
                    }
                    contract.setCreateTime(new Date());
                    contract.setDraftStatus(1);
                    if (contractVO.getDataJson() != null) {
                        contract.setDataJson(contractVO.getDataJson());
                    }
                    if (contractVO.getLateSignDays() != null) {
                        contract.setLateSignDays(contractVO.getLateSignDays());
                    } else {
                        int day = Integer.parseInt(commonInfoService.findOne(429).getValue());
                        contract.setLateSignDays(day);
                    }
                    if (contractVO.getPdfImageUrl() != null) {
                        contract.setFileUrl(contractVO.getPdfImageUrl());
                    }
                    contractDao.updateById(contract);
                    contractVO.setContractId(contract.getId());
                    contractVO.setUserId(userId);
//                    if (contractVO.getType() == 1) {
                    if (contractVO.getUserName() != null && (contractVO.getIsPrincipal() == 1 || contractVO.getIsPrincipal() == 2)) {
                        contractVO.setIsPrincipal(contractVO.getIsPrincipal());
                        addContractAndUser(contractVO, user.getUserId());
                    }
                    if (contractVO.getSignUserName() != null) {
                        addContractSignUser(contractVO);
                    } else {
                        return Result.error("请填写签署人信息");
                    }
//                        return Result.success().put("data", contractVO);
//                    } else if (contractVO.getType() == 2) {
//                        if (contractVO.getUserName() != null && (contractVO.getIsPrincipal() == 1 || contractVO.getIsPrincipal() == 2)) {
//                            contractVO.setIsPrincipal(contractVO.getIsPrincipal());
//                            addContractAndUser(contractVO, user.getUserId());
//                        }
//                        if (contractVO.getSignUserName() != null) {
//                            addContractSignUser(contractVO);
//                        } else {
//                            return Result.error("请填写签署人信息");
//                        }
//                    }
                    ContractTemplate contractTemplate = baseMapper.selectById(contractVO.getTemplateId());
                    if (contractTemplate != null) {
                        contractVO.setTemplatePath(contractTemplate.getContractUrl());
                    }
                    return Result.success().put("data", contractVO);
                }
            } else {
                // 合同编号前缀
                String contractNumberNO = commonInfoService.findOne(427).getValue();
                ContractTemplate contractTemplate = baseMapper.selectById(contractVO.getTemplateId());
                Contract contract = new Contract();
                if (contractTemplate != null) {
                    contract.setContractType(1);
                    contract.setTemplateId(contractTemplate.getId());
                    contract.setContractName(contractTemplate.getTemplateName());
                    contractVO.setTemplatePath(contractTemplate.getContractUrl());
                } else {
                    contract.setContractType(2);
                }
                if (contractVO.getContractName() != null) {
                    contract.setContractName(contractVO.getContractName());
                }
                if (contractVO.getLateSignDays() != null) {
                    contract.setLateSignDays(contractVO.getLateSignDays());
                } else {
                    int day = Integer.parseInt(commonInfoService.findOne(429).getValue());
                    contract.setLateSignDays(day);
                }
                contract.setIsComplete(0);
                contract.setDraftStatus(1);
                if (contractVO.getDataJson() != null) {
                    contract.setDataJson(contractVO.getDataJson());
                }
                if (contractVO.getPdfImageUrl() != null) {
                    contract.setFileUrl(contractVO.getPdfImageUrl());
                }
                contract.setContractNumber(contractNumberNO + generateOrder());
                contract.setUserId(user.getUserId());
                contract.setCreateTime(new Date());
                contractDao.insert(contract);
                contractVO.setContractId(contract.getId());
                if (contractVO.getType() == 1) {
                    if (contractVO.getUserName() != null && (contractVO.getIsPrincipal() == 1 || contractVO.getIsPrincipal() == 2)) {
                        contractVO.setIsPrincipal(contractVO.getIsPrincipal());
                        addContractAndUser(contractVO, user.getUserId());
                    }
                    if (contractVO.getSignUserName() != null) {
                        addContractSignUser(contractVO);
                    } else {
                        return Result.error("请填写签署人信息");
                    }
                    return Result.success().put("data", contractVO);
                } else if (contractVO.getType() == 2) {
                    if (contractVO.getUserName() != null && (contractVO.getIsPrincipal() == 1 || contractVO.getIsPrincipal() == 2)) {
                        contractVO.setIsPrincipal(contractVO.getIsPrincipal());
                        addContractAndUser(contractVO, user.getUserId());
                    }
                    if (contractVO.getSignUserName() != null) {
                        addContractSignUser(contractVO);
                    } else {
                        return Result.error("请填写签署人信息");
                    }
                }
                return Result.success().put("data", contractVO);
            }
        }
        return Result.error("参数为空，请检查后重试");
    }

    @Override
    public Result selectSignWait(Integer page, Integer limit, Long userId) {
        if (userId != null) {
            UserEntity user = userDao.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在或未登录，请重新登录");
            }
            IPage<Contract> pages;
            if (page != null && limit != null) {
                pages = new Page<>(page, limit);
            } else {
                pages = new Page<>();
                pages.setSize(-1);
            }
            Page<Contract> list = contractDao.selectSignWaitList(pages, userId);
            if (!list.getRecords().isEmpty()) {
                for (Contract contract : list.getRecords()) {
                    // 查出发起人和合同关联表
                    List<ContractAndUser> caList = contractAndUserDao.selectByContractId1(contract.getId());
                    contract.setContractAndUsers(caList);
                    ContractAndUser initiator = caList.stream()
                            .findFirst()
                            .orElse(null);
                    if (initiator != null) {
                        contract.setUserName(initiator.getUserName());
                    }
//                    contract.setContractAndUsers(contractAndUserDao.selectByContractId1(contract.getId()));
                    // 查出合同附加字段
//                contract.setContractAdditionalFields(contractAdditionalFieldsDao.selectByContractId(contract.getId()));
                }
            }
            return Result.success().put("data", list);
        }
        return Result.error("合同不存在");
    }

    @Override
    public Result selectMyApproval(Integer page, Integer limit, Long userId) {
        if (userId != null) {
            UserEntity user = userDao.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在或未登录，请重新登录");
            }
            IPage<Contract> pages;
            if (page != null && limit != null) {
                pages = new Page<>(page, limit);
            } else {
                pages = new Page<>();
                pages.setSize(-1);
            }
            List<Contract> list = contractDao.selectApprovalList(pages, userId);
            for (Contract contract : list) {
                // 查出发起人和合同关联表
                contract.setContractAndUsers(contractAndUserDao.selectByContractId(contract.getId()));
                // 查出合同附加字段
//                contract.setContractAdditionalFields(contractAdditionalFieldsDao.selectByContractId(contract.getId()));
            }
            return Result.success().put("data", list);
        }
        return Result.error("合同不存在");
    }

    @Override
    public Result initiateSingleContract(ContractVO contractVO) {
        if (contractVO != null) {
            UserEntity user = userDao.selectById(contractVO.getUserId());
            if (user == null) {
                return Result.error("用户不存在或未登录，请重新登录");
            }
            UserCertification userCertification = userCertificationDao.selectOne(new LambdaQueryWrapper<UserCertification>()
                    .eq(UserCertification::getUserId, user.getUserId()));
            if (userCertification == null) {
                return Result.error("请先实名认证");
            }
            if (userCertification.getStatus() != 1) {
                return Result.error("请实名认证通过后再来发起合同");
            }
            if (user.getFreeTimes() <= 0) {
                return Result.error("您的次数已用完，请先购买合同套餐");
            }
            // 获取合同编号的前缀
            String contractNumberNO = commonInfoService.findOne(427).getValue();
            Contract contract = new Contract();
            contract.setIsComplete(0);
            if (contractVO.getContractName() != null) {
                contract.setContractName(contractVO.getContractName());
            }
            if (contractVO.getLateSignDays() != null) {
                contract.setLateSignDays(contractVO.getLateSignDays());
            } else {
                int day = Integer.parseInt(commonInfoService.findOne(429).getValue());
                contract.setLateSignDays(day);
            }
            if (contractVO.getPdfImageUrl() != null) {
                contract.setFileUrl(contractVO.getPdfImageUrl());
            }
            contract.setContractType(4);
            contract.setCreateTime(new Date());
            contract.setUserId(contractVO.getUserId());
            contract.setContractNumber(contractNumberNO + generateOrder());
            contractDao.insert(contract);
            contractVO.setContractId(contract.getId());
            if (contractVO.getType() == 1) {
                if (contractVO.getUserName() != null && (contractVO.getIsPrincipal() == 1 || contractVO.getIsPrincipal() == 2)) {
                    contractVO.setIsPrincipal(contractVO.getIsPrincipal());
                    addContractAndUser(contractVO, user.getUserId());
                }
                return Result.success().put("data", contractVO.getContractId());
            } else if (contractVO.getType() == 2) {
                if (contractVO.getUserName() != null && (contractVO.getIsPrincipal() == 1 || contractVO.getIsPrincipal() == 2)) {
                    contractVO.setIsPrincipal(contractVO.getIsPrincipal());
                    addContractAndUser(contractVO, user.getUserId());
                }
            }
            user.setFreeTimes(user.getFreeTimes() - 1);
            userDao.updateById(user);
            return Result.success().put("data", contractVO.getContractId());
        }
        return Result.error("参数有误，请核对后重新发起");
    }

    @Override
    public Result initiateByWx(ContractVO contractVO) {
        if (contractVO != null) {
            UserEntity user = userDao.selectById(contractVO.getUserId());
            if (user == null) {
                return Result.error("用户不存在或未登录，请重新登录");
            }
            UserCertification userCertification = userCertificationDao.selectOne(new LambdaQueryWrapper<UserCertification>()
                    .eq(UserCertification::getUserId, user.getUserId()));
            if (userCertification == null) {
                return Result.error("请先实名认证");
            }
            if (userCertification.getStatus() != 1) {
                return Result.error("请实名认证通过后再来发起合同");
            }
            if (user.getFreeTimes() <= 0) {
                return Result.error("您的次数已用完，请先购买合同套餐");
            }
            // 获取合同编号的前缀
            String contractNumberNO = commonInfoService.findOne(427).getValue();
            Contract contract = new Contract();
            contract.setIsComplete(0);
            if (contractVO.getContractName() != null) {
                contract.setContractName(contractVO.getContractName());
            }
            if (contractVO.getLateSignDays() != null) {
                contract.setLateSignDays(contractVO.getLateSignDays());
            } else {
                int day = Integer.parseInt(commonInfoService.findOne(429).getValue());
                contract.setLateSignDays(day);
            }
            if (contractVO.getPdfImageUrl() != null) {
                contract.setFileUrl(contractVO.getPdfImageUrl());
            }
            contract.setContractType(2);
            contract.setCreateTime(new Date());
            contract.setUserId(contractVO.getUserId());
            contract.setContractNumber(contractNumberNO + generateOrder());
            contractDao.insert(contract);
            contractVO.setContractId(contract.getId());
            if (contractVO.getType() == 1) {
                if (contractVO.getUserName() != null && (contractVO.getIsPrincipal() == 1 || contractVO.getIsPrincipal() == 2)) {
                    contractVO.setIsPrincipal(contractVO.getIsPrincipal());
                    addContractAndUser(contractVO, user.getUserId());
                }
                if (contractVO.getSignUserName() != null) {
                    addContractSignUser(contractVO);
                } else {
                    return Result.error("请填写签署人信息");
                }
                return Result.success().put("data", contractVO.getContractId());
            } else if (contractVO.getType() == 2) {
                if (contractVO.getUserName() != null && (contractVO.getIsPrincipal() == 1 || contractVO.getIsPrincipal() == 2)) {
                    contractVO.setIsPrincipal(contractVO.getIsPrincipal());
                    addContractAndUser(contractVO, user.getUserId());
                }
                if (contractVO.getSignUserName() != null) {
                    addContractSignUser(contractVO);
                } else {
                    return Result.error("请填写签署人信息");
                }
            }
            user.setFreeTimes(user.getFreeTimes() - 1);
            userDao.updateById(user);
            return Result.success().put("data", contractVO.getContractId());
        }
        return Result.error("参数有误，请核对后重新发起");
    }

    @Override
    public Result draftBoxReceiving(ContractVO contractVO) {
        if (contractVO != null) {
            UserEntity user = userDao.selectById(contractVO.getUserId());
            if (user == null) {
                return Result.error("用户不存在或未登录，请重新登录");
            }
            if (user.getFreeTimes() <= 0) {
                return Result.error("您的次数已用完，请先购买合同套餐");
            }
            Contract contract = contractDao.selectOne(new QueryWrapper<Contract>().eq("id", contractVO.getContractId()));
            if (contract != null) {
                contract.setContractType(3);
                contract.setDraftStatus(0);
                ContractAndUser contractAndUser = contractAndUserDao.selectByContractAndUser(contract.getId(), contractVO.getUserId());
                if (contractAndUser != null) {
                    if (contractAndUser.getStatus() == 2 || contractAndUser.getStatus() == 3 || contractAndUser.getStatus() == 4) {
                        return Result.error("当前合同状态不允许重新发起合同");
                    }
                    contractAndUser.setDraftStatus(0);
                    if (contractAndUserDao.updateById(contractAndUser) > 0) {
                        log.info("合同从草稿箱重新发起成功，合同id为：" + contract.getId());
                        contractDao.updateById(contract);
                        user.setFreeTimes(user.getFreeTimes() - 1);
                        userDao.updateById(user);
                        return Result.success();
                    }
                }
            }
        }
        return Result.error("草稿箱发起失败！");
    }

    @Override
    public byte[] generatePDF(ContractVO contractVO) throws DocumentException, IOException {
        Context context = new Context();
        context.setVariable("contractVO", contractVO);
        String processedHtml = templateEngine.process("contract", context);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument((Document) new StringReader(processedHtml), "");
        renderer.layout();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        renderer.createPDF(out);
        renderer.finishPDF();
        return out.toByteArray();
    }

    /**
     * 定时任务，查询合同状态，如果合同过期，则将合同状态改为已过期
     */
    @Scheduled(fixedRate = 3000, zone = "Asia/Shanghai")
    public void selectContractStatus() {
        List<Contract> list = contractDao.selectList(new LambdaQueryWrapper<Contract>()
                .eq(Contract::getIsComplete, 0));
        for (Contract contract : list) {
            // 查出未签署和未审批的合同
            List<ContractAndUser> contractAndUser = contractAndUserDao.selectByStatus(contract.getId());
            if (contractAndUser != null) {
                // 当前日期只包含年月日
                Date now = getCurrentDateOnlyYearMonthDay();
                for (ContractAndUser andUser : contractAndUser) {
                    if (andUser.getLastDate() != null) {
                        if (andUser.getLastDate().before(now)) {
                            andUser.setStatus(4);
                            contract.setIsComplete(3);
                            if (contractAndUserDao.updateById(andUser) > 0) {
                                contractDao.updateById(contract);
                                log.error("合同已过期，合同id为：" + contract.getId());
                            }
                        }
                    }
                }
            }
        }
    }

    private Date getCurrentDateOnlyYearMonthDay() {
        return Date.from(
                LocalDate.now()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        );
    }
}
