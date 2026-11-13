package com.sqx.modules.enterprise.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.enterprise.entity.Enterprise;
import com.sqx.modules.enterprise.service.EnterpriseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/8/8 16:05
 */
@RestController
@RequestMapping("/app/enterprise")
@Api(value = "用户端-企业管理", tags = {"用户端-企业管理"})
public class AppEnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Login
    @PostMapping("/addEnterprise")
    @ApiOperation("app端添加提交或修改企业信息")
    public Result addEnterprise(@RequestBody Enterprise enterprise,
                                @ApiParam("拦截器获取的用户id") @RequestAttribute("userId")Long userId) {
        enterprise.setUserId(userId);
        return enterpriseService.addEnterprise(enterprise);
    }

    @Login
    @GetMapping("/getEnterpriseInfo")
    @ApiOperation("app端获取企业信息")
    public Result getEnterpriseInfo(@ApiParam("拦截器获取的用户id")String enterpriseName,
                                    @RequestAttribute Long userId) {
        return enterpriseService.getEnterpriseInfo(enterpriseName);
    }

    @Login
    @GetMapping("/appGetEnterpriseDetail")
    @ApiOperation("用户端获取企业信息详情")
    public Result appGetEnterpriseDetail(@ApiParam("企业id")Long id,
                                         @ApiParam("拦截器获取的用户id")@RequestAttribute("userId")Long userId) {
        return enterpriseService.appGetEnterpriseDetail(id,userId);
    }

    @Login
    @GetMapping("/selectEnterpriseList")
    @ApiOperation("用户端查询我的企业列表")
    public Result selectEnterpriseList(@ApiParam("页码")Integer page,
                                       @ApiParam("条数")Integer limit,Enterprise enterprise,
                                       @ApiParam("拦截器获取的用户id")@RequestAttribute("userId")Long userId) {
        return enterpriseService.selectEnterpriseList(page, limit, enterprise,userId);
    }

}
