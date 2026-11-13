package com.sqx.modules.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description member
 * @author fang
 * @date 2021-06-19
 */
@Data
@ApiModel("会员特权")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 会员特权id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("会员特权id")
    private Integer memberId;

    /**
     * 特权图标
     */
    @ApiModelProperty("特权图标")
    private String memberImg;

    /**
     * 特权名称
     */
    @ApiModelProperty("特权名称")
    private String memberName;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private String sort;

    public Member() {}
}
