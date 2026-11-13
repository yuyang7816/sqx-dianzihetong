package com.sqx.modules.oss.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/8/15 11:32
 */
@Data
public class SignatureRequest implements Serializable {

    private String dataUrl;

    // Getters and Setters
    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    private static final long serialVersionUID = 1L;
}
