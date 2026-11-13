package com.sqx.modules.utils;

import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2026/3/10 13:27
 */
public class TemplateProcessor {

    /**
     * 将远程 Word 模板中的下划线替换为 {field_1}, {field_2}, ...
     * @param templateUrl 远程 URL
     * @return 处理后的新本地文件路径
     */

    public static Path convertUnderlinesToPlaceholders(String templateUrl, List<String> fieldNames) throws Exception {
        // 1. 下载远程模板到本地临时文件
        Path downloaded = downloadFile(templateUrl);

        // 2. 读取 Word 文档
        XWPFDocument doc = new XWPFDocument(Files.newInputStream(downloaded));

        // 3. 替换所有下划线为 {field_N}
        replaceUnderlines(doc, fieldNames);

        // 4. 保存为新文件
        Path processed = Files.createTempFile("processed_template_", ".docx");
        try (OutputStream out = Files.newOutputStream(processed)) {
            doc.write(out);
        }
        doc.close();

        // 可选：删除原始下载文件
        Files.deleteIfExists(downloaded);

        return processed;
    }

    private static Path downloadFile(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        Path temp = Files.createTempFile("downloaded_tpl_", ".docx");
        try (InputStream in = url.openStream()) {
            Files.copy(in, temp, StandardCopyOption.REPLACE_EXISTING);
        }
        return temp;
    }

    private static void replaceUnderlines(XWPFDocument doc, List<String> fieldNames) {
        final int[] index = {0};

        // 处理段落
        for (XWPFParagraph p : doc.getParagraphs()) {
            replaceInParagraph(p, fieldNames, index);
        }

        // 处理表格
        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        replaceInParagraph(p, fieldNames, index);
                    }
                }
            }
        }
        // 可选：检查字段数量是否匹配
//        if (index[0] < fieldNames.size()) {
//            throw new IllegalArgumentException(
//                    "模板中下划线数量不足！需要 " + fieldNames.size() + " 个，只找到 " + index[0] + " 个"
//            );
//        }
    }

    private static void  replaceInParagraph(XWPFParagraph paragraph, List<String> fieldNames, int[] index) {
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null) {
                // 循环替换所有连续下划线（每次替换一个）
                while (index[0] < fieldNames.size() && text.matches(".*_{5,}.*")) {
                    String fieldName = fieldNames.get(index[0]);
                    String placeholder = "{" + fieldName + "}";
                    // 只替换第一个匹配的长下划线
                    text = text.replaceFirst("_{5,}", placeholder);
                    index[0]++;
                }
                run.setText(text, 0);
            }
        }
    }
}
