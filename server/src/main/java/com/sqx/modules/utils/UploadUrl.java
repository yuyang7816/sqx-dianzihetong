package com.sqx.modules.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.file.utils.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2026/3/12 13:59
 */
@Slf4j
@Component
public class UploadUrl {

    @Autowired
    private CommonInfoService commonInfoService;

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
            String isLocal = commonInfoService.findOne(428).getValue();
            // 复用你的 FileUploadUtils
            String fileName = null;
            String[] allowedExtensions = {"docx", "doc", "pdf", "png", "jpg"};
            if ("1".equals(isLocal)) {
                // === 本地存储 ===
                String http = commonInfoService.findOne(19).getValue();
                String webBaseUrl = "/file/uploadPath/";
                String[] split = http.split("://");
                String domain = split[1];
                String filePath = "/www/wwwroot/" + domain + webBaseUrl;
                try {
                    fileName = FileUploadUtils.upload1(filePath, localFile.toFile(), originalFilename,allowedExtensions);
                } catch (IOException e) {
                    log.error("新的文件上传失败", e);
                }
                return http + webBaseUrl + fileName;
            } else {
                String filePath = "C:/tmp/uploaded_files";
                try {
//                String originalFilename1 = getFileNameFromUrl(originalFilename);
                    fileName = FileUploadUtils.upload1(filePath, localFile.toFile(), originalFilename, allowedExtensions);
                } catch (IOException e) {
                    log.error("新的文件上传失败", e);
                }
                return "http://localhost:8180/uploaded/" + fileName;
            }
        }
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
}
