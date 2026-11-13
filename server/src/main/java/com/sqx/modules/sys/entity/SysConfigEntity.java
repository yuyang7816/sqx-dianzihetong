package com.sqx.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 系统配置信息
 *
 */
@Data
@TableName("sys_config")
@ApiModel("系统配置信息")
public class SysConfigEntity {
	@TableId
	@ApiModelProperty("主键id")
	private Long id;
	@NotBlank(message="参数名不能为空")
	@ApiModelProperty("参数名")
	private String paramKey;
	@ApiModelProperty("参数值")
	@NotBlank(message="参数值不能为空")
	private String paramValue;
	@ApiModelProperty("备注")
	private String remark;

}
