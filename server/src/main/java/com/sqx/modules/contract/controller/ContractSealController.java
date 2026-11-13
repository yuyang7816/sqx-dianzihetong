package com.sqx.modules.contract.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.contract.entity.ContractSeal;
import com.sqx.modules.contract.service.ContractSealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/8/15 17:42
 */
@RestController
@RequestMapping(value = "/admin/contractSeal")
@Api(value = "管理端-合同印章",tags = {"管理端-合同印章"})
public class ContractSealController {

    @Autowired
    private ContractSealService contractSealService;

    @PostMapping("/addContractSeal")
    @ApiOperation(value = "添加或修改合同印章(个人签名、个人印章。公司印章)")
    public Result addContractSeal(@RequestBody ContractSeal contractSeal) {
        return contractSealService.addContractSeal1(contractSeal);
    }

    @PostMapping("/deleteContractSeal")
    @ApiOperation(value = "删除合同印章")
    public Result deleteContractSeal(@ApiParam("合同印章id") Long id,
                                     @ApiParam("用户id") Long userId) {
        if (contractSealService.removeById(id)){
            return Result.success();
        }
        return Result.error();
    }

    @GetMapping("/selectContractSealDetails")
    @ApiOperation(value = "获取合同印章详情")
    public Result selectContractSealDetails(@ApiParam("合同印章id")Long id,
                                            @ApiParam("用户id")Long userId) {
       ContractSeal contractSeal = contractSealService.getById(id);
        if (contractSeal == null){
            return Result.error("该合同印章不存在");
        }
        return Result.success().put("data",contractSeal);
    }

    @GetMapping("/selectContractSealList")
    @ApiOperation(value = "查询合同印章列表")
    public Result selectContractSealList(@ApiParam("页码")Integer page,
                                         @ApiParam("条数")Integer limit,ContractSeal contractSeal) {
        return contractSealService.selectContractSealList1(page,limit,contractSeal);
    }
}
