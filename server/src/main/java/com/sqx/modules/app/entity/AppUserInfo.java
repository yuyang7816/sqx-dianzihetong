package com.sqx.modules.app.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("微信用户信息")
public class AppUserInfo {


    @ApiModelProperty("微信用户openId")
    private String openid;
    @ApiModelProperty("微信用户昵称")
    private String nickname;
    @ApiModelProperty("微信用户性别")
    private int sex;
    @ApiModelProperty("微信用户所在省")
    private String province;
    @ApiModelProperty("微信用户所在市")
    private String city;
    @ApiModelProperty("微信用户所在地区")
    private String country;
    @ApiModelProperty("微信用户头像")
    private String headimgurl;
    @ApiModelProperty("微信用户UnionId(跨应用识别同一个用户的关键)")
    private String unionid;
    @ApiModelProperty("微信用户权限")
    private List<String> privilege;


}
