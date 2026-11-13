package com.sqx.modules.contract.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.contract.entity.ContractType;
import com.sqx.modules.contract.service.ContractTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/8/9 18:22
 */
@RestController
@RequestMapping(value = "/admin/contractType")
@Api(value = "管理端-合同类型管理", tags = {"管理端-合同类型管理"})
public class ContractTypeController {

    @Autowired
    private ContractTypeService contractTypeService;

    @PostMapping("/addContractType")
    @ApiOperation("添加合同类型")
    public Result addContractType(@RequestBody ContractType contractType) {
        return contractTypeService.addContractType(contractType);
    }

    @PostMapping("/deleteContractType")
    @ApiOperation("删除合同类型")
    public Result deleteContractType(@ApiParam("合同类型id") Long id) {
        return contractTypeService.deleteContractType(id);
    }

    @GetMapping("/getContractTypeDetail")
    @ApiOperation("获取合同类型详情")
    public Result getContractTypeDetail(@ApiParam("合同类型id") Long id) {
        return contractTypeService.getContractTypeDetail(id);
    }

    @GetMapping("/getContractTypeList")
    @ApiOperation("获取合同类型列表")
    public Result getContractTypeList(@ApiParam("页码") Integer page,
                                      @ApiParam("条数") Integer limit, ContractType contractType) {
        return contractTypeService.getContractTypeList(page, limit, contractType);
    }
}
