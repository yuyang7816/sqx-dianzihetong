package com.sqx.modules.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2026/3/12 14:41
 */
public class PdfToImageConverter {

    /**
     * 将 PDF 每一页转为 PNG 图片，保存到指定目录
     *
     * @param pdfPath   PDF 文件路径
     * @param outputDir 输出目录
     * @param dpi       渲染分辨率（默认 100~200 足够清晰）
     * @return 生成的图片文件列表
     */
    public static List<Path> convertPdfToImages(Path pdfPath, Path outputDir, int dpi) throws IOException {
        List<Path> imagePaths = new ArrayList<>();
        try (PDDocument document = PDDocument.load(pdfPath.toFile())) {
            PDFRenderer renderer = new PDFRenderer(document);
            Files.createDirectories(outputDir);
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, dpi, ImageType.RGB);
                String imageName = "page_" + (i + 1) + ".png";
                Path imagePath = outputDir.resolve(imageName);
                ImageIO.write(image, "PNG", imagePath.toFile());
                imagePaths.add(imagePath);
            }
        }
        return imagePaths;
    }
}
