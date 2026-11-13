package com.sqx.modules.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用配置管理
 */
@Data
@TableName("common_info")
@ApiModel("通用配置管理")
public class CommonInfo implements Serializable {

    @TableId(type = IdType.INPUT)
    @ApiModelProperty("id")
    private long id;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("类型1表示客服二维码 2表示公众号二维码 3表示全局佣金是否开启 4注册客服渠道id配置 5、佣金规则 6、、、")
    private Integer type; //1表示客服二维码 2表示公众号二维码 3表示全局佣金是否开启 4注册客服渠道id配置 5、佣金规则 6、

    @ApiModelProperty("配置文件的值")
    private String value;

    @ApiModelProperty("暂未使用")
    private String max;

    @ApiModelProperty("配置文件名称")
    private String min;

    @ApiModelProperty("分类")
    private String conditionFrom;

}

