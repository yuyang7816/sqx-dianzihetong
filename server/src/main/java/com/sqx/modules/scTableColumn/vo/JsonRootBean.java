package com.sqx.modules.scTableColumn.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("暂未使用")
public class JsonRootBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long contractId;
    private List<ColumnData> columnData;
    private Long columnId;
    private Date createTime;
    private Date updateTime;
}
