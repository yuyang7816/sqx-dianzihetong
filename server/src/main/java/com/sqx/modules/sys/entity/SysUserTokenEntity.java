package com.sqx.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 系统用户Token
 *
 */
@Data
@TableName("sys_user_token")
@ApiModel("系统用户Token")
public class SysUserTokenEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//用户ID
	@TableId(type = IdType.INPUT)
	@ApiModelProperty("用户id")
	private Long userId;
	//token
	@ApiModelProperty("token")
	private String token;
	//过期时间
	@ApiModelProperty("过期时间")
	private Date expireTime;
	//更新时间
	@ApiModelProperty("更新时间")
	private Date updateTime;

}
