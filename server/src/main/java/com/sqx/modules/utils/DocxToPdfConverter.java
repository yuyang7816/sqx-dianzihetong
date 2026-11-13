package com.sqx.modules.utils;

import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2026/3/9 17:27
 */
@Component
public class DocxToPdfConverter {

    private final CommonInfoService commonInfoService;

    public DocxToPdfConverter(CommonInfoService commonInfoService) {
        this.commonInfoService = commonInfoService;
    }

    public void convertDocxToPdf(String inputDocx, String outputPdf) throws Exception {
        File inputFile = new File(inputDocx);
        File outputFile = new File(outputPdf);
        // 检查输入文件是否存在
        if (!inputFile.exists()) {
            throw new FileNotFoundException("输入文件不存在: " + inputDocx);
        }
        // 创建输出目录
        File outputDir = outputFile.getParentFile();
        if (outputDir != null && !outputDir.exists()) {
            if (!outputDir.mkdirs()) {
                throw new IOException("无法创建输出目录: " + outputDir.getAbsolutePath());
            }
        }
        // libreOffice工具，1线上 2本地
        String libreOfficeType = commonInfoService.findOne(428).getValue();
        String sofficePath;
        if ("1".equals(libreOfficeType)) {
            // LibreOffice 命令（Linux）
            sofficePath = "/usr/bin/soffice";

        } else {
            // Windows: 使用完整路径调用 soffice.exe
            sofficePath = "\"C:\\Program Files\\LibreOffice\\program\\soffice.exe\"";
        }

        // 参数分开传，避免空格和引号问题
        ProcessBuilder pb = new ProcessBuilder(
                sofficePath,
                "--headless",
                "--convert-to", "pdf",
                "--outdir", outputDir.getAbsolutePath(), // 关键：只指定目录，不指定文件名
                inputFile.getAbsolutePath()
        );
        if ("1".equals(libreOfficeType)) {
            pb.environment().put("HOME", System.getProperty("user.home"));
            pb.environment().put("LANG", "zh_CN.UTF-8"); // 或 en_US.UTF-8
            pb.environment().put("XDG_RUNTIME_DIR", "/tmp"); // 某些新版 LO 需要
        }
        System.out.println("执行转换命令: " + String.join(" ", pb.command()));
        Process process = pb.start();
        // 4. 等待执行完成 等待上传文件
        int exitCode = process.waitFor();
        // 5. 获取错误日志
        String stdErr = IOUtils.toString(process.getErrorStream(), StandardCharsets.UTF_8);
        String stdOut = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
        if (exitCode != 0) {
            // 读取错误信息
            System.err.println("LibreOffice 标准输出: " + stdOut);
            System.err.println("LibreOffice 错误输出: " + stdErr);
            throw new RuntimeException("LibreOffice 转换失败，退出码: " + exitCode + "\n错误详情: " + stdErr);
        }
        // 6. 确定生成的文件路径
        // LibreOffice 逻辑：在 outdir 目录下生成与输入文件同名的 .pdf 文件
        String expectedPdfName = inputFile.getName().replaceAll("\\.docx$", ".pdf").replaceAll("\\.DOCX$", ".pdf");
        File generatedPdfFile = new File(outputDir, expectedPdfName);

        // 7. 验证文件是否生成
        if (!generatedPdfFile.exists()) {
            System.err.println("LibreOffice 输出日志: " + stdOut);
            System.err.println("LibreOffice 错误日志: " + stdErr);
            throw new FileNotFoundException("转换后文件未生成！预期路径: " + generatedPdfFile.getAbsolutePath() +
                    "\n可能原因：字体缺失、文档损坏或权限不足。");
        }
        // 8. 重命名（如果期望的输出文件名与生成的不一致）
        if (!generatedPdfFile.getAbsolutePath().equals(outputFile.getAbsolutePath())) {
            if (outputFile.exists()) {
                outputFile.delete(); // 删除旧文件以防 renameTo 失败
            }
            boolean renamed = generatedPdfFile.renameTo(outputFile);
            if (!renamed) {
                // 如果跨分区移动失败，尝试手动拷贝+删除
                try {
                    java.nio.file.Files.copy(generatedPdfFile.toPath(), outputFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    generatedPdfFile.delete();
                } catch (IOException e) {
                    throw new RuntimeException("无法重命名或移动 PDF 文件: " + generatedPdfFile.getName() + " -> " + outputFile.getName(), e);
                }
            }
        }
        System.out.println("PDF 转换成功: " + outputFile.getAbsolutePath());
    }
}
