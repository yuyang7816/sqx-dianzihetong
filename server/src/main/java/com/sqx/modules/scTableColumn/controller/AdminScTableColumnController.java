package com.sqx.modules.scTableColumn.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.scTableColumn.service.ScTableColumnService;
import com.sqx.modules.scTableColumn.vo.JsonRootBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@Api(value = "模板管理", tags = {"管理端-模板管理（暂未使用）"})
@RequestMapping(value = "/admin/scTableColumn/")
public class AdminScTableColumnController {

    @Autowired
    private ScTableColumnService columnService;

    @ApiOperation("添加|修改模板")
    @PostMapping("insertScTable")
    public Result insertScTable(@RequestBody JsonRootBean jsonRootBean) {
        return columnService.insertScTable(jsonRootBean);
    }

    @ApiOperation("用分类合同查询模板")
    @GetMapping("getTableByContractId")
    public Result getTableByContractId(Long contractId){
        return Result.success().put("data", columnService.getTableByContractId(contractId));
    }
    @ApiOperation("删除模板")
    @PostMapping("deleteTableByContractId")
    public Result deleteTableByContractId(Long contractId){
        return columnService.deleteTableByContractId(contractId);
    }
}
