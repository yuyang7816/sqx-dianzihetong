package com.sqx.modules.scTableColumn.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.scTableColumn.service.ScTableColumnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@Api(value = "用户端-模板管理", tags = {"用户端-模板管理（暂未使用）"})
@RequestMapping(value = "/app/scTableColumn/")
public class AppScTableColumnController {
    @Autowired
    private ScTableColumnService columnService;


    @ApiOperation("用分类查询合同模板")
    @GetMapping("getTableByContractId")
    public Result getTableByContractId(Long contractId){
        return Result.success().put("data", columnService.getTableByContractId(contractId));
    }
}
