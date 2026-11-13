package com.sqx.modules.contract.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.contract.dao.ContractSealDao;
import com.sqx.modules.contract.entity.ContractSeal;
import com.sqx.modules.contract.service.ContractSealService;
import com.sqx.modules.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/8/15 17:22
 */
@Service
@Slf4j
public class ContractSealServiceImpl extends ServiceImpl<ContractSealDao, ContractSeal> implements ContractSealService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommonInfoService commonInfoService;

    @Override
    public Result addContractSeal(ContractSeal contractSeal) {
        UserEntity user = userDao.selectById(contractSeal.getUserId());
        if (user == null) {
            return Result.error("用户不存在或未登录，请重新登录！");
        }
        if (contractSeal.getId() != null) {
            ContractSeal contractSeal1 = baseMapper.selectById(contractSeal.getId());
            if (contractSeal1 != null) {
                if (baseMapper.updateById(contractSeal) > 0) {
                    return Result.success();
                }
            } else {
                return Result.error("修改失败！");
            }
        } else {
            contractSeal.setUserId(user.getUserId());
            contractSeal.setCreateTime(new Date());
            baseMapper.insert(contractSeal);
            return Result.success();
        }
        return Result.error();
    }

    @Override
    public Result addContractSeal1(ContractSeal contractSeal) {
        if (contractSeal.getUserId() != null) {
            UserEntity user = userDao.selectById(contractSeal.getUserId());
            if (user == null) {
                return Result.error("该用户不存在！");
            }
        }
        if (contractSeal.getId() != null) {
            ContractSeal contractSeal1 = baseMapper.selectById(contractSeal.getId());
            if (contractSeal1 != null) {
                if (baseMapper.updateById(contractSeal) > 0) {
                    return Result.success();
                }
            } else {
                return Result.error("修改失败！");
            }
        } else {
            contractSeal.setCreateTime(new Date());
            baseMapper.insert(contractSeal);
            return Result.success();
        }
        return Result.error();
    }


    @Override
    public Result deleteContractSeal(Long id, Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在或未登录，请重新登录！");
        }
        ContractSeal contractSeal1 = baseMapper.selectById(id);
        if (contractSeal1 == null) {
            return Result.error("删除失败！");
        }
        if (baseMapper.deleteById(id) > 0) {
            return Result.success();
        }
        return Result.error("删除失败！");
    }

    @Override
    public Result selectContractSealDetails(Long id, Long userId) {
        UserEntity user = userDao.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在或未登录，请重新登录！");
        }
        ContractSeal contractSeal = baseMapper.selectById(id);
        if (contractSeal != null) {
            return Result.success().put("data", contractSeal);
        }
        return Result.error("暂无查询结果");
    }

    @Override
    public Result selectContractSealList(Integer page, Integer limit, ContractSeal contractSeal) {
        IPage<ContractSeal> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        if (contractSeal != null) {
            IPage<ContractSeal> page1 = baseMapper.selectContractSealList(pages, contractSeal);
            return Result.success().put("data", page1);
        }
        return Result.success().put("data", Collections.emptyList());
    }

    @Override
    public Result selectContractSealList1(Integer page, Integer limit, ContractSeal contractSeal) {
        IPage<ContractSeal> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        if (contractSeal != null) {
            IPage<ContractSeal> page1 = baseMapper.selectContractSealList(pages, contractSeal);
            return Result.success().put("data", page1);
        }
        return Result.success().put("data", Collections.emptyList());
    }

    @Override
    public Result deductImage(String content, UserEntity userEntity) {
        String url = "https://api.remove.bg/v1.0/removebg";
        String key = commonInfoService.findOne(183).getValue();
        try {
            JSONObject json = new JSONObject();
            json.put("image_url", content);
            json.put("size", "preview");
            json.put("semitransparency", "true");
            json.put("position", "original");
            json.put("scale", "original");
            json.put("roi", "0% 0% 100% 100%");
            json.put("crop", "false");
            json.put("channels", "rgba");
            json.put("format", "auto");
            json.put("type", "auto");
            json.put("crop_margin", "0");
            json.put("add_shadow", "false");
            json.put("type_level", "1");

            String s = HttpClientUtil.doPostJsonImage(url, json.toJSONString(), key);
            JSONObject jsonObject = JSONObject.parseObject(s);
            log.error("扣除背景图:" + jsonObject.toJSONString());

            JSONObject data = jsonObject.getJSONObject("data");
            if (data == null) {
                return Result.error("生成失败！");
            }
            String base64 = data.getString("result_b64");
            byte[] buffer = Base64.getDecoder().decode(base64);
            ByteArrayInputStream pictureStream = new ByteArrayInputStream(buffer);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(pictureStream);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String dateDir = LocalDate.now().format(formatter);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileSuffix = ".png";
            String fileFullName = "image/" + dateDir + "/" + uuid + fileSuffix;
            String fileAccessUrl = "";
            String storeType = commonInfoService.findOne(234).getValue();
            // 阿里云
            if ("1".equals(storeType)) {
                String endpoint = commonInfoService.findOne(68).getValue();
                String accessKeyId = commonInfoService.findOne(69).getValue();
                String accessKeySecret = commonInfoService.findOne(70).getValue();
                String bucketName = commonInfoService.findOne(71).getValue();
                String ossDomain = commonInfoService.findOne(72).getValue();

                com.aliyun.oss.ClientBuilderConfiguration ossConfig = new com.aliyun.oss.ClientBuilderConfiguration();
                ossConfig.setMaxConnections(100);
                ossConfig.setConnectionTimeout(5000);
                ossConfig.setSocketTimeout(30000);
                ossConfig.setConnectionRequestTimeout(1000);
                ossConfig.setIdleConnectionTime(60);
                ossConfig.setMaxErrorRetry(3);
                ossConfig.setProtocol(com.aliyun.oss.common.comm.Protocol.HTTPS);

                OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, ossConfig);
                ossClient.putObject(bucketName, fileFullName, bufferedInputStream);
                ossClient.shutdown();
                fileAccessUrl = ossDomain + "/" + fileFullName;
                // 本地
            } else if ("2".equals(storeType)) {
                String httpPrefix = commonInfoService.findOne(19).getValue();
                String[] split1 = httpPrefix.split("://");
                if (split1.length < 2) {
                    log.error("本地域名配置错误：{}",httpPrefix);
                    return null;
                }
                String localRootPath = "/www/wwwroot/" + split1[1] + "/file/uploadPath/";
                File targetDir = new File(localRootPath + "image/" + dateDir);
                if (!targetDir.exists()) targetDir.mkdirs();

                File targetFile = new File(targetDir, uuid + fileSuffix);
                try (OutputStream outputStream = new FileOutputStream(targetFile)) {
//                    outputStream.write(buffer);
                    buffer = new byte[1024 * 8];
                    int len;
                    while ((len = bufferedInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                }
                fileAccessUrl = httpPrefix + "/file/uploadPath/" + fileFullName;
                // 腾讯云
            } else {
                String accessKey = commonInfoService.findOne(800).getValue();
                String secretKey = commonInfoService.findOne(801).getValue();
                String bucketRegion = commonInfoService.findOne(802).getValue();
                String bucketName = commonInfoService.findOne(803).getValue();
                String path = commonInfoService.findOne(804).getValue();

                COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
                ClientConfig clientConfig = new ClientConfig(new Region(bucketRegion));
                COSClient cosClient = new COSClient(cred, clientConfig);

                File tempFile = null;
                try {
                    tempFile = File.createTempFile("cos_", ".jpg");
                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                        buffer = new byte[1024 * 8];
                        int len;
                        while ((len = bufferedInputStream.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                    }
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH) + 1;
                    int day = cal.get(Calendar.DATE);
                    String key1 = "/image/" + year + "/" + month + "/" + day + "/" + uuid + fileSuffix;
                    PutObjectRequest request = new PutObjectRequest(bucketName, key1, tempFile);
                    cosClient.putObject(request);
                    fileAccessUrl = path + key1;
                } finally {
                    if (tempFile != null) tempFile.delete();
                    cosClient.shutdown();
                }
            }
            return Result.success().put("data", fileAccessUrl);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("扣除背景图失败" + e.getMessage(), e);
        }
        return Result.error("生成失败");
    }
}
