package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@ApiModel("用户会员信息")
public class UserVip implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户会员ID
     */
    @TableId
    @ApiModelProperty("用户会员id")
    private Long vipId;
    /**
     * 会员类型
     */
    @ApiModelProperty("会员类型")
    private Integer vipNameType;
    @TableField(exist = false)
    @ApiModelProperty("会员详情信息")
    private VipDetails vipDetails;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 购买时间
     */
    @ApiModelProperty("购买时间")
    private String createTime;
    /**
     * 到期时间
     */
    @ApiModelProperty("到期时间")
    private String endTime;

    /**
     *是否是会员
     */
    @ApiModelProperty("是否是会员")
    private Integer  isVip;

}
