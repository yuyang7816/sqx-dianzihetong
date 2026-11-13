package com.sqx.modules.contract.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
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
@RequestMapping(value = "/app/contractSeal")
@Api(value = "用户端-合同印章", tags = {"用户端-合同印章"})
public class AppContractSealController {

    @Autowired
    private ContractSealService contractSealService;

    @Login
    @PostMapping("/addContractSeal")
    @ApiOperation(value = "添加或修改合同印章(个人签名、个人印章。公司印章)")
    public Result addContractSeal(@RequestBody ContractSeal contractSeal,
                                  @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        contractSeal.setUserId(userId);
        return contractSealService.addContractSeal(contractSeal);
    }

    @Login
    @PostMapping("/deleteContractSeal")
    @ApiOperation(value = "删除合同印章")
    public Result deleteContractSeal(@ApiParam("合同印章id") Long id,
                                     @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractSealService.deleteContractSeal(id, userId);
    }

    @Login
    @GetMapping("/selectContractSealDetails")
    @ApiOperation(value = "获取合同印章详情")
    public Result selectContractSealDetails(@ApiParam("合同印章id") Long id,
                                            @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractSealService.selectContractSealDetails(id, userId);
    }

    @Login
    @GetMapping("/selectContractSealList")
    @ApiOperation(value = "查询合同印章列表")
    public Result selectContractSealList(@ApiParam("页码") Integer page,
                                         @ApiParam("条数") Integer limit,
                                         ContractSeal contractSeal,
                                         @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        contractSeal.setUserId(userId);
        return contractSealService.selectContractSealList(page, limit, contractSeal);
    }


    @Login
    @PostMapping("deductImage")
    @ApiOperation("扣除背景图")
    public Result deductImage(@RequestBody JSONObject jsonObject, @LoginUser UserEntity userEntity){
        String content = jsonObject.getString("content");
        return contractSealService.deductImage(content,userEntity);
    }
}
