package com.sqx.modules.utils;

import com.alibaba.fastjson.JSON;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.sqx.common.utils.Result;
import com.sqx.modules.common.service.CommonInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

@Slf4j
@Component
public class SearchCompanyUtil {

    // 这里使用静态，让 service 属于类
    private static CommonInfoService commonInfoService;

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setCommonInfoService(CommonInfoService commonInfoService) {
        SearchCompanyUtil.commonInfoService = commonInfoService;
    }

    public static String calcAuthorization(String secretId, String secretKey, String datetime)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String signStr = "x-date: " +  datetime;
        Mac mac = Mac.getInstance("HmacSHA1");
        Key sKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), mac.getAlgorithm());
        mac.init(sKey);
        byte[] hash = mac.doFinal(signStr.getBytes("UTF-8"));
        String sig = new BASE64Encoder().encode(hash);

        String auth = "{\"id\":\"" + secretId + "\", \"x-date\":\"" + datetime + "\", \"signature\":\"" + sig + "\"}";
        return auth;
    }

    public static String urlencode(Map<?, ?> map) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    URLEncoder.encode(entry.getKey().toString(), "UTF-8"),
                    URLEncoder.encode(entry.getValue().toString(), "UTF-8")
            ));
        }
        return sb.toString();
    }

    public static String search(String companyName)  {
        //云市场分配的密钥Id
        String secretId = commonInfoService.findOne(469).getValue();
        //云市场分配的密钥Key
        String secretKey = commonInfoService.findOne(470).getValue();

        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String datetime = sdf.format(cd.getTime());
        String auth = null;
        // 签名
        try {
             auth = calcAuthorization(secretId, secretKey, datetime);
        }catch (Exception e){
            log.info("企业信息签名加密失败："+e.getMessage(),e);
        }

        // 请求方法
        String method = "GET";
        // 请求头
        String uuid = UUID.randomUUID().toString();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("request-id", uuid);
        headers.put("Authorization", auth);

        // 查询参数
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("keyword",companyName);
        queryParams.put("pageNo","1");
        queryParams.put("pageSize","20");
        // body参数
        Map<String, String> bodyParams = new HashMap<String, String>();

        String bodyParamStr = null;
        try {
            bodyParamStr = urlencode(bodyParams);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // url参数拼接
        String url = "https://ap-shanghai.cloudmarket-apigw.com/services-o4act7vf8/v1/enterprise_like/query";
        if (!queryParams.isEmpty()) {
            try {
                url += "?" + urlencode(queryParams);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod(method);

            // request headers
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // request body
            Map<String, Boolean> methods = new HashMap<>();
            methods.put("POST", true);
            methods.put("PUT", true);
            methods.put("PATCH", true);
            Boolean hasBody = methods.get(method);
            if (hasBody != null) {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                conn.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(bodyParamStr);
                out.flush();
                out.close();
            }

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String result = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            log.info("企业信息查询返回值："+result);
            return result;
        } catch (Exception e) {
            log.info("企业信息查询报错："+e.getMessage(),e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

}
