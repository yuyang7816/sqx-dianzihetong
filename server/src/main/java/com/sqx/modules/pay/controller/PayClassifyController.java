package com.sqx.modules.pay.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.pay.entity.PayClassify;
import com.sqx.modules.pay.service.PayClassifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description：TODO
 * @author： liuduo
 * @create： 2026/1/29 11:38
 */
@RestController
@RequestMapping("/admin/payClassify")
@Api(value = "管理端-合同签署套餐", tags = {"管理端-合同签署套餐"})
public class PayClassifyController {

    @Autowired
    private PayClassifyService payClassifyService;

    @PostMapping("/addPayClassify")
    @ApiOperation("添加或修改合同签署套餐")
    public Result addPayClassify(@RequestBody PayClassify payClassify) {
        return payClassifyService.addPayClassify(payClassify);
    }

    @PostMapping("/deletePayClassify")
    @ApiOperation("删除合同签署套餐")
    public Result deletePayClassify(@ApiParam("合同签署套餐id") Long payClassifyId) {
        return payClassifyService.deletePayClassify(payClassifyId);
    }

    @GetMapping("/queryPayClassify")
    @ApiOperation("查询合同签署套餐")
    public Result queryPayClassify(@ApiParam("合同签署套餐id") Long payClassifyId) {
        return payClassifyService.queryPayClassify(payClassifyId);
    }

    @GetMapping("/queryPayClassifyList")
    @ApiOperation("查询合同签署套餐列表")
    public Result queryPayClassifyList(@ApiParam("页码") Integer page,
                                       @ApiParam("条数") Integer limit,
                                       PayClassify payClassify) {
        return payClassifyService.queryPayClassifyList(page, limit, payClassify);
    }


}
