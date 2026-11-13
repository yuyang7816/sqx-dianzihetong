package com.sqx.modules.contract.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.sqx.modules.utils.typeHandler.ListStringTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
//@TableName(autoResultMap = true)
@ApiModel("数据字段")
public class DataJsonVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 字段名
     */
    @ApiModelProperty("字段名")
    private String columnName;
    /**
     * 字段描述
     */
    @ApiModelProperty("字段描述")
    private String columnComment;
    /**
     * 字段值
     */
    @ApiModelProperty("字段值")
    private String columnValue;
    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private Integer htmlType;

    /**
     * 是否必填
     */
    @ApiModelProperty("是否必填")
    private Integer isRequired;
    
    /**
     * 选项值
     */
    @ApiModelProperty("选项值")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> optionJson;

}
