package com.sqx.config;

import com.sqx.common.utils.DateConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/8/19 13:13
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 注册 createTime 的日期转换器
        registry.addConverter(new DateConverter("yyyy-MM-dd HH:mm:ss"));

        // 如果需要，也可以注册 startTime 的日期转换器
         registry.addConverter(new DateConverter("HH:mm:ss"));
    }
}
