package com.sqx.modules.scTableColumn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.scTableColumn.entity.ScTableColumn;
import com.sqx.modules.scTableColumn.vo.JsonRootBean;

import java.util.List;

public interface ScTableColumnService extends IService<ScTableColumn> {
    Result insertScTable(JsonRootBean jsonRootBean);

    List<ScTableColumn> getTableByContractId(Long contractId);

    Result deleteTableByContractId(Long contractId);
}
