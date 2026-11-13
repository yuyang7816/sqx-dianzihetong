package com.sqx.modules.contract.entity;

import lombok.Data;

import java.util.Map;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2026/3/10 9:59
 */
@Data
public class GeneratePdfRequest {

    private String templatePath;
    private Long contractId;
    private Map<String, String> fieldValues;
}
