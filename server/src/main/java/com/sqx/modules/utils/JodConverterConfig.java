package com.sqx.modules.utils;

import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2026/3/18 20:39
 */
@Configuration
public class JodConverterConfig {

    private static final Logger log = LoggerFactory.getLogger(JodConverterConfig.class);

//    @PostConstruct
//    public void checkLibreOffice() {
//        File officeHome = new File("/usr/lib64/libreoffice");
//        if (!officeHome.exists()) {
//            throw new RuntimeException("LibreOffice查找不到++++++++++++++++++ " + officeHome.getAbsolutePath());
//        }
//    }
    @Bean(destroyMethod = "stop")
    public OfficeManager officeManager() {
        // 1. 创建 OfficeManager
        LocalOfficeManager officeManager = LocalOfficeManager.builder()
                .install()
                .maxTasksPerProcess(50)          // 每50次重启，防内存泄漏
                .processTimeout(20_000L)         // 启动超时 20s
                .taskExecutionTimeout(30_000L)   // 单任务超时 30s)
                .build();

        // 2. 手动启动！
        try {
            officeManager.start();
            log.info("LibreOffice++++++++++++++++++++： 进程已成功启动");
        } catch (Exception e) {
            log.error("启动 LibreOffice 失败", e);
            throw new RuntimeException("无法启动 OfficeManager-----------------", e);
        }

        return officeManager;
    }

    // 正确创建 DocumentConverter
    @Bean
    public DocumentConverter documentConverter(OfficeManager officeManager) {
        return LocalConverter.make(officeManager); // 工厂方法
    }

}
