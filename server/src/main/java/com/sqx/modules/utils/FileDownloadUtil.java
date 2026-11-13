package com.sqx.modules.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2026/3/10 10:09
 */
public class FileDownloadUtil {

    public static Path downloadFileFromUrl(String fileUrl) throws IOException, URISyntaxException {
        // 验证是否是合法 URL（可选安全校验）
        if (!fileUrl.startsWith("http://") && !fileUrl.startsWith("https://")) {
            throw new IllegalArgumentException("仅支持 HTTP/HTTPS URL");
        }
        // 提取原始文件名（含扩展名）
        String urlFileName = Paths.get(new URI(fileUrl).getPath()).getFileName().toString();
        String suffix = "." + urlFileName.substring(urlFileName.lastIndexOf('.') + 1);
        // 创建临时文件，保留原始扩展名
        Path tempFile = Files.createTempFile("downloaded_", suffix);
        try (InputStream in = new URL(fileUrl).openStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        return tempFile;
    }
}
