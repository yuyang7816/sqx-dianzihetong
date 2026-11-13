package com.sqx.modules.utils;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2026/3/9 17:26
 */
public class WordFiller {

    public static void fillWordTemplate(String inputPath, String outputPath, Map<String, String> replacements) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputPath);
             XWPFDocument doc = new XWPFDocument(fis);
             FileOutputStream fos = new FileOutputStream(outputPath)) {

            replaceInAllElements(doc, replacements);
            setA4PageSize(doc);
            doc.write(fos);
        }
    }

    private static void replaceInAllElements(XWPFDocument doc, Map<String, String> replacements) {
        // 替换段落
        for (XWPFParagraph p : doc.getParagraphs()) {
            replaceInParagraph(p, replacements);
        }
        // 替换表格
        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        replaceInParagraph(p, replacements);
                    }
                }
            }
        }
    }

    private static void replaceInParagraph(XWPFParagraph paragraph, Map<String, String> replacements) {
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null) {
                String result = text;
                for (Map.Entry<String, String> entry : replacements.entrySet()) {
                    result = result.replace(entry.getKey(), entry.getValue());
                }
                if (!result.equals(text)) {
                    run.setText(result, 0);
                }
            }
        }
    }

    // 提取所有 {xxx} 字段（用于前端展示）
    public static java.util.Set<String> extractPlaceholders(String docxPath) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(docxPath))) {
            java.util.Set<String> placeholders = new java.util.HashSet<>();
            Pattern pattern = Pattern.compile("\\{[^}]+\\}");

            for (XWPFParagraph p : doc.getParagraphs()) {
                extractFromText(p.getText(), pattern, placeholders);
            }
            for (XWPFTable t : doc.getTables()) {
                for (XWPFTableRow r : t.getRows()) {
                    for (XWPFTableCell c : r.getTableCells()) {
                        extractFromText(c.getText(), pattern, placeholders);
                    }
                }
            }
            return placeholders;
        }
    }


    private static void extractFromText(String text, Pattern pattern, java.util.Set<String> set) {
        if (text == null) return;
        Matcher m = pattern.matcher(text);
        while (m.find()) {
            set.add(m.group());
        }
    }

    /**
     * 设置 Word 文档为 A4 纵向
     */
    public static void setA4PageSize(XWPFDocument document) {
        // 1. 获取或创建节属性 (SectPr)
        // 注意：SectPr 通常位于文档 Body 的末尾
        CTSectPr sectPr = document.getDocument().getBody().getSectPr();
        if (sectPr == null) {
            sectPr = document.getDocument().getBody().addNewSectPr();
        }

        // 2. 获取或创建页面尺寸设置 (PgSz)
        CTPageSz pageSize = sectPr.getPgSz();
        if (pageSize == null) {
            pageSize = sectPr.addNewPgSz();
        }

        // 3. 设置 A4 尺寸 (单位：dxa)
        // 宽：210mm -> 11906
        // 高：297mm -> 16838
        pageSize.setW(BigInteger.valueOf(11906));
        pageSize.setH(BigInteger.valueOf(16838));

        // 4. 设置方向为纵向 (可选，默认即为纵向)
        pageSize.setOrient(STPageOrientation.PORTRAIT);
    }

}
