package com.sqx.modules.app.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Details")
@AllArgsConstructor
@Api(value = "管理端-钱包明细", tags = {"管理端-钱包明细"})
public class UserMoneyDetailsController {
    private UserMoneyDetailsService userMoneyDetailsService;

    @ApiOperation("钱包明细")
    @GetMapping("/queryUserMoneyDetails")
    public Result queryUserMoneyDetails(@ApiParam("页码") Integer page,
                                        @ApiParam("条数")Integer limit,
                                        @ApiParam("用户id")Long userId,
                                        @ApiParam("5购买签署合同套餐")Integer classify,
                                        @ApiParam("类型 1增加 2减少")Integer type) {

        return userMoneyDetailsService.queryUserMoneyDetails(page, limit, userId,classify,type);
    }



}
