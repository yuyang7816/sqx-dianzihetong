package com.sqx.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/8/19 11:40
 */
@Slf4j
public class DateConverter implements Converter<String, Date> {

    private static final Logger logger = LoggerFactory.getLogger(DateConverter.class);
    private final SimpleDateFormat dateFormat;

    public DateConverter(String pattern) {
        this.dateFormat = new SimpleDateFormat(pattern);
    }

    @Override
    public Date convert(String source) {
        logger.info("Converting date: {}", source);
        try {
            return dateFormat.parse(source);
        } catch (ParseException e) {
            logger.error("Invalid date format: {}", source, e);
            throw new IllegalArgumentException("Invalid date format: " + source, e);
        }
    }
}
