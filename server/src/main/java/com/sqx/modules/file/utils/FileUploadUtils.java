package com.sqx.modules.file.utils;

import com.sqx.modules.common.service.CommonInfoService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static com.google.common.io.Files.getFileExtension;

/**
 * 文件上传工具类
 *
 * @author ruoyi
 */
public class FileUploadUtils {
    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    private static int counter = 0;

    private static CommonInfoService commonRepository;

    @Autowired
    public void setCommonRepository(CommonInfoService commonRepository) {
        FileUploadUtils.commonRepository = commonRepository;
    }

    public static String getDefaultBaseDir() {
        return commonRepository.findOne(19).getValue();
    }

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件名称
     * @throws Exception
     */
    public static final String upload(MultipartFile file) throws IOException {
        try {
            return upload(getDefaultBaseDir(), file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file) throws IOException {
        try {
            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 返回上传成功的文件名
     */
    public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension)
            throws DescribeException, IOException {
        int fileNameLength = file.getOriginalFilename().length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new DescribeException("文件名太长", -100);
        }

        assertAllowed(file, allowedExtension);

        String fileName = extractFilename(file);

        File desc = getAbsoluteFile(baseDir, fileName);
        file.transferTo(desc);
        String pathFileName = getPathFileName(baseDir, fileName);
        return pathFileName;
    }

    /**
     * 重载：从本地 File 上传（用于后台处理，如模板替换后重新保存）
     */
    public static final String upload(String baseDir, File sourceFile, String originalFilename, String[] allowedExtension)
            throws DescribeException, IOException {

        // 1. 文件名校验长度
        int fileNameLength = originalFilename.length();
        if (fileNameLength > DEFAULT_FILE_NAME_LENGTH) {
            throw new DescribeException("文件名太长", -100);
        }

        // 2. 扩展名校验
        assertAllowed(originalFilename, allowedExtension); // 新增一个重载的 assertAllowed

        // 3. 生成目标文件名（复用 extractFilename 逻辑）
        String fileName = extractFilename(originalFilename); // 新增重载

        // 4. 获取绝对路径
        File destFile = getAbsoluteFile(baseDir, fileName);

        // 5. 复制文件
        Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // 6. 返回相对路径
        return getPathFileName(baseDir, fileName);
    }

    public static String upload1(String basePath, File file, String originalFilename, String[] allowedExtensions) throws IOException {
        // 1. 检查扩展名
        String ext = getFileExtension(originalFilename);
        if (!Arrays.asList(allowedExtensions).contains(ext)) {
            throw new IllegalArgumentException("不允许的文件类型");
        }

        // 2. 生成唯一文件名（UUID）
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String filename = uuid + "." + ext;

        // 3. 创建目录（按年月日）
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Path dirPath = Paths.get(basePath, datePath);
        Files.createDirectories(dirPath);

        // 4. 保存文件
        Path targetPath = dirPath.resolve(filename);
        Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // 5. 返回相对路径（不带 baseDir）
        return datePath + "/" + filename;
    }

    /**
     * 编码文件名
     */
    public static final String extractFilename(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        fileName = datePath() + "/" + encodingFilename(fileName) + "." + extension;
        return fileName;
    }

    //  新增：根据原始文件名生成带日期+UUID的文件名
    private static String extractFilename(String originalFilename) {
        String extension = getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        return datePath + "/" + uuid + "." + extension;
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }


    private static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;
    }

    private static final String getPathFileName(String uploadDir, String fileName) throws IOException {
        int dirLastIndex = uploadDir.lastIndexOf("/") + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        String pathFileName = "/file/" + currentDir + "/" + fileName;
        return pathFileName;
    }

    /**
     * 编码文件名
     */
    private static final String encodingFilename(String fileName) {
        fileName = fileName.replace("_", " ");
        fileName = Md5Utils.hash(fileName + System.nanoTime() + counter++);
        return fileName;
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @return
     */
    public static final void assertAllowed(MultipartFile file, String[] allowedExtension)
            throws DescribeException {
        long size = file.getSize();
        if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE) {
            throw new DescribeException("文件太大", -100);
        }

        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
                throw new DescribeException("", -100);
            } else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
                throw new DescribeException("", -100);
            } else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
                throw new DescribeException("", -100);
            } else {
                throw new DescribeException("", -100);
            }
        }

    }

    // 新增：仅校验文件名
    private static void assertAllowed(String fileName, String[] allowedExtension) throws DescribeException {
        if (allowedExtension != null && allowedExtension.length > 0) {
            String extension = getExtension(fileName).toLowerCase();
            boolean isValid = Arrays.stream(allowedExtension)
                    .anyMatch(ext -> ext.toLowerCase().equals(extension));
            if (!isValid) {
                throw new DescribeException("不允许的文件类型: " + extension, -100);
            }
        }
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public static final boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }

    /**
     * 获取文件名的后缀（仅根据文件名）
     *
     * @param fileName 文件名，如 "template.docx"
     * @return 后缀名，如 "docx"；若无则返回空字符串 ""
     */
    public static final String getExtension(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return ""; // 没有点，或点在最后（如 "file."）
        }
        return fileName.substring(lastDotIndex + 1);
    }
}