package com.sqx.modules.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2026/4/8 20:12
 */
@Component
public class BatchImageUtil {
    // 1. 创建一个固定大小的线程池（比如 5 个线程），防止线程太多把服务器撑爆
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    /**
     * 批量下载图片并计算哈希（并发版）
     *
     * @param imageUrls 图片链接列表
     * @return 包含所有图片哈希值的列表
     */

    public List<String> batchDownloadAndHash(List<String> imageUrls) throws InterruptedException {
        List<Future<String>> futures = new ArrayList<>();
        List<String> results = new ArrayList<>();

        // 2. 提交任务到线程池
        for (String url : imageUrls) {
            Callable<String> task = () -> downloadAndHashSingle(url);
            Future<String> future = executorService.submit(task);
            futures.add(future);
        }
        // 3. 获取结果（这里会等待所有任务完成）
        for (Future<String> future : futures) {
            try {
                // get() 方法会阻塞，直到该任务完成
                String hash = future.get(30, TimeUnit.SECONDS); // 单张图片超时限制
                if (hash != null) {
                    results.add(hash);
                }
            } catch (Exception e) {
                // 记录错误，但不要中断整个流程
                System.err.println("某张图片处理失败: " + e.getMessage());
                // 你可以选择抛异常，或者返回一个错误标记
            }
        }
        return results;
    }

    /**
     * 单张图片下载 + 流式哈希计算
     */
    private String downloadAndHashSingle(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); // 连接超时 5秒
            conn.setReadTimeout(10000);   // 读取超时 10秒

            if (conn.getResponseCode() == 200) {
                try (InputStream is = conn.getInputStream()) {
                    // 关键点：使用 DigestInputStream 边读边算，不占内存
                    return DigestUtils.sha256Hex(is);
                }
            } else {
                System.err.println("下载失败，HTTP状态码: " + conn.getResponseCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
