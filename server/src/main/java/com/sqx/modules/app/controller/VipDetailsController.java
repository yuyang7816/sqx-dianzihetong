package com.sqx.modules.app.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.app.service.VipDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "管理端-会员管理", tags = {"管理端-会员管理"})
@RequestMapping(value = "/vipDetails")
public class VipDetailsController {

    @Autowired
    private VipDetailsService vipDetailsService;

    @ApiParam("添加会员的详情信息")
    @PostMapping("/insertVipDetails")
    public Result insertVipDetails(@RequestBody VipDetails vipDetails) {
        return vipDetailsService.insertVipDetails(vipDetails);
    }


    @ApiParam("修改会员的详情信息")
    @PostMapping("/updateVipDetails")
    public Result updateVipDetails(@RequestBody VipDetails vipDetails) {
        vipDetailsService.updateById(vipDetails);
        return Result.success();
    }

    @ApiParam("删除的详情信息")
    @PostMapping("/deleteVipDetails")
    public Result deleteVipDetails(@ApiParam("会员详情id") Long id) {
        vipDetailsService.removeById(id);
        return Result.success();
    }

    @ApiParam("查询会员列表")
    @GetMapping("/selectVipDetailsList")
    public Result selectVipDetailsList(@ApiParam("页码")Integer page,
                                       @ApiParam("条数")Integer limit) {
        return Result.success().put("data",new PageUtils(vipDetailsService.page(new Page<>(page,limit))));
    }

}
