package com.sqx.modules.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fang
 * @date 2020/7/13
 */
@Data
@TableName("message_info")
@ApiModel("消息信息")
public class MessageInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 消息id
     */
    @ApiModelProperty("消息id")
    private Long id;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createAt;

    /**
     * 图片
     */
    @ApiModelProperty("图片")
    private String image;

    /**
     * 是否已读
     */
    @ApiModelProperty("是否已读")
    private String isSee;

    /**
     * send_state
     */
    @ApiModelProperty("发送状态")
    private String sendState;

    /**
     * send_time
     */
    @ApiModelProperty("发送时间")
    private String sendTime;

    /**
     * 分类 2问题反馈   5用户消息   8注销
     */
    @ApiModelProperty("分类")
    private String state;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String url;

    /**
     * type
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * platform
     */
    @ApiModelProperty("来源")
    private String platform;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String userName;


    public MessageInfo() {}

}