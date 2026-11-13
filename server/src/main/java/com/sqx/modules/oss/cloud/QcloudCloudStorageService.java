package com.sqx.modules.oss.cloud;

import com.alibaba.fastjson.JSONObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.auth.COSStaticCredentialsProvider;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.sqx.common.exception.SqxException;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 腾讯云存储
 *
 */
public class QcloudCloudStorageService extends CloudStorageService {
    private COSClient client;

    public QcloudCloudStorageService(CloudStorageConfig config){
        this.config = config;

        //初始化
        init();
    }

    private void init(){
//    	Credentials credentials = new Credentials(config.getQcloudAppId(), config.getQcloudSecretId(),
//                config.getQcloudSecretKey());
        //初始化客户端配置
        COSCredentials cred = new BasicCOSCredentials(config.getQcloudSecretId(),config.getQcloudSecretKey());
        //设置bucket所在的区域，华南：gz 华北：tj 华东：sh
        ClientConfig clientConfig = new ClientConfig(new Region(config.getQcloudRegion()));
        COSClient cosClient = new COSClient(cred, clientConfig);

//        ClientConfig clientConfig = new ClientConfig();
//
//        clientConfig.setRegion(config.getQcloudRegion());
//
//    	client = new COSClient(clientConfig, credentials);
    }

    @Override
    public String upload(byte[] data, String path) {
        //腾讯云必需要以"/"开头
        if(!path.startsWith("/")) {
            path = "/" + path;
        }
        
        //上传到腾讯云
//        UploadFileRequest request = new UploadFileRequest(config.getQcloudBucketName(), path, data);
//        String response = client.uploadFile(request);
        InputStream inputStream = new ByteArrayInputStream(data);
        ObjectMetadata metadata = new ObjectMetadata();
        try {
        PutObjectRequest request = new PutObjectRequest(config.getQcloudBucketName(), path, inputStream, metadata);
        client.putObject(request);
        } catch (Exception e) {
            throw new SqxException("文件上传失败，" + e.getMessage(), e);
        }

        return config.getQcloudDomain() + path;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
    	try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new SqxException("上传文件失败", e);
        }
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getQcloudPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getQcloudPrefix(), suffix));
    }
}
