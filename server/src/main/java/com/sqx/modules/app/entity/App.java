package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;

/**
 * 广告位
 */
@Data
@TableName("app")
@ApiModel("app升级配置")
public class App implements Serializable {
    @TableId
    @ApiModelProperty("app升级配置id")
    private Long id;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("安卓升级地址")
    private String androidWgtUrl;

    @ApiModelProperty("苹果升级地址")
    private String iosWgtUrl;

    @ApiModelProperty("通用下载地址")
    private String wgtUrl;

    @ApiModelProperty("安卓版本")
    private String version;

    @ApiModelProperty("苹果版本")
    private String iosVersion;

    @ApiModelProperty("是否强制升级")
    private String method;

    @ApiModelProperty("描述")
    private String des;

}

