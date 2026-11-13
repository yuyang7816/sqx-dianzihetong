package com.sqx.modules.contract.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.contract.entity.Contract;
import com.sqx.modules.contract.entity.ContractTemplate;
import com.sqx.modules.contract.service.ContractTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/8/9 18:16
 */
@RestController
@RequestMapping(value = "/admin/contract")
@Api(value = "管理端-合同模板", tags = {"管理端-合同模板"})
public class ContractController {

    @Autowired
    private ContractTemplateService contractTemplateService;

    @PostMapping(value = "/addContractTemplate")
    @ApiOperation(value = "管理后台添加合同模板")
    public Result addContractTemplate(@RequestBody ContractTemplate contractTemplate) {
        return contractTemplateService.addContractTemplate(contractTemplate);
    }

    @PostMapping(value = "/deleteContractTemplate")
    @ApiOperation(value = "管理后台删除合同模板")
    public Result deleteContractTemplate(@ApiParam("合同模板id") Long id) {
        return contractTemplateService.deleteContractTemplate(id);
    }

    @GetMapping(value = "/getContractTemplateDetail")
    @ApiOperation(value = "管理后台获取合同模板详情")
    public Result getContractTemplateDetail(@ApiParam("合同模板id") Long id) {
        return contractTemplateService.getContractTemplateDetail(id);
    }

    @GetMapping(value = "/selectContractTemplateList")
    @ApiOperation(value = "管理后台获取合同模板列表")
    public Result selectContractTemplateList(@ApiParam("页码") Integer page,
                                             @ApiParam("条数") Integer limit,
                                             ContractTemplate contractTemplate) {
        return contractTemplateService.selectContractTemplateList(page, limit, contractTemplate);
    }

    @GetMapping("/getUserContract")
    @ApiOperation("获取用户所有发起的合同信息")
    public Result getUserContract(@ApiParam("页码") Integer page,
                                  @ApiParam("条数") Integer limit,
                                  @ApiParam("用户id") Long userId) {
        return contractTemplateService.selectAllContractList1(page, limit, userId);
    }

    @GetMapping("/userReceivedContract")
    @ApiOperation("获取用户所有收到的合同信息")
    public Result userReceivedContract(@ApiParam("页码") Integer page,
                                       @ApiParam("条数") Integer limit,
                                       @ApiParam("用户id") Long userId) {
        return contractTemplateService.userReceivedContract(page, limit, userId);
    }

    @GetMapping("/copyReceivedContract")
    @ApiOperation("获取用户收到抄送的合同信息")
    public Result copyReceivedContract(@ApiParam("页码") Integer page,
                                       @ApiParam("页码") Integer limit,
                                       @ApiParam("用户id") Long userId) {
        return contractTemplateService.copyReceivedContract(page, limit, userId);
    }

    @GetMapping("/getAllContractList")
    @ApiOperation("获取所有合同列表")
    public Result getAllContractList(@ApiParam("页码") Integer page,
                                     @ApiParam("条数") Integer limit, Contract contract) {
        return contractTemplateService.getAllContractList(page, limit, contract);
    }
}
