package com.sqx.modules.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;


/**
 * 文件工具类
 * @author fang
 * @date 2021-03-4
 */
public class FileUtils {

    /**
     * 根据文件地址下载文件
     * @param url  文件地址
     * @param fileName  文件名
     * @return
     */
    public static MultipartFile createFileItem(String url, String fileName) {
        FileItem item = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            //设置应用程序要从网络连接读取数据
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();

                FileItemFactory factory = new DiskFileItemFactory(16, null);
                String textFieldName = "uploadfile";
                item = factory.createItem(textFieldName, ContentType.APPLICATION_OCTET_STREAM.toString(), false, fileName);
                OutputStream os = item.getOutputStream();

                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                is.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败", e);
        }

        return new CommonsMultipartFile(item);
    }

    /**
     * 从dataUrl创建MultipartFile
     * @param dataUrl
     * @return
     * @throws IOException
     */
    public static MultipartFile createMockMultipartFileFromDataUrl(String dataUrl) throws IOException {
        String[] parts = dataUrl.split(";base64,");
        String contentType = parts[0].replace("data:", "");
        byte[] bytes = Base64.getDecoder().decode(parts[1]);

        // 创建一个新的临时文件
        Path tempFile = Files.createTempFile("signature-", ".png");
        Files.write(tempFile, bytes,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);

        return new MockMultipartFile(
                "signature.png",
                "signature.png",
                contentType,
                Files.newInputStream(tempFile)
        );
    }


}
