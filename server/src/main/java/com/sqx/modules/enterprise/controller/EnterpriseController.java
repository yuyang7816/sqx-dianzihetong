package com.sqx.modules.enterprise.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.enterprise.entity.Enterprise;
import com.sqx.modules.enterprise.service.EnterpriseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/8/8 16:04
 */
@RestController
@RequestMapping("/admin/enterprise")
@Api(value = "管理端-企业管理", tags = {"管理端-企业管理"})
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;


    @PostMapping("/addEnterprise")
    @ApiOperation("管理端添加提交或修改企业信息")
    public Result addEnterprise(@RequestBody Enterprise enterprise) {
        return enterpriseService.addEnterprise(enterprise);
    }
    @PostMapping("/authEnterprise")
    @ApiOperation("管理后台审核企业认证")
    public Result authEnterprise(@RequestBody Enterprise enterprise) {
        return enterpriseService.authEnterprise(enterprise);
    }

    @PostMapping("/deleteEnterprise")
    @ApiOperation("管理后台删除企业")
    public Result deleteEnterprise(@ApiParam("企业id")Long id) {
        return enterpriseService.deleteEnterprise(id);
    }

    @GetMapping("/getEnterpriseDetail")
    @ApiOperation("管理后台获取企业信息详情")
    public Result getEnterpriseDetail(@ApiParam("企业id")Long id) {
        return enterpriseService.getEnterpriseDetail(id);
    }

    @GetMapping("/getEnterpriseList")
    @ApiOperation("管理后台获取企业列表")
    public Result getEnterpriseList(@ApiParam("页码")Integer page,
                                    @ApiParam("条数")Integer limit,Enterprise enterprise){
        return enterpriseService.getEnterpriseList(page, limit, enterprise);
    }
}
