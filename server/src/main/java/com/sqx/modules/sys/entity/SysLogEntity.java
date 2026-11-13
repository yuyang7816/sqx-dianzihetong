package com.sqx.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 系统日志
 *
 */
@Data
@TableName("sys_log")
@ApiModel("系统日志")
public class SysLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId
	@ApiModelProperty("日志id")
	private Long id;
	//用户名
	@ApiModelProperty("用户名")
	private String username;
	//用户操作
	@ApiModelProperty("用户操作")
	private String operation;
	//请求方法
	@ApiModelProperty("请求方法")
	private String method;
	//请求参数
	@ApiModelProperty("请求参数")
	private String params;
	//执行时长(毫秒)
	@ApiModelProperty("执行时长(毫秒)")
	private Long time;
	//IP地址
	@ApiModelProperty("IP地址")
	private String ip;
	//创建时间
	@ApiModelProperty("创建时间")
	private Date createDate;

}
