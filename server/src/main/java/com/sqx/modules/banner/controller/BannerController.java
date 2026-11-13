package com.sqx.modules.banner.controller;


import com.sqx.common.utils.Result;
import com.sqx.modules.banner.entity.Banner;
import com.sqx.modules.banner.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "管理端-banner图", tags = {"管理端-banner图"})
@RequestMapping(value = "/banner")
public class BannerController {


    @Autowired
    private BannerService bannerService;


    @RequestMapping(value = "/selectBannerList", method = RequestMethod.GET)
    @ApiOperation("查询所有banner图")
    @ResponseBody
    public Result selectBannerList(@ApiParam("分类 1 banner图  2 首页分类") Integer classify) {
        return Result.success().put("data", bannerService.selectBannerLists(classify));
    }


    @RequestMapping(value = "/selectBannerPage", method = RequestMethod.GET)
    @ApiOperation("查询所有banner图")
    @ResponseBody
    public Result selectBannerPage(@ApiParam("页码") Integer page,
                                   @ApiParam("条数") Integer limit,
                                   @ApiParam("分类 1 banner图  2 首页分类") Integer classify,
                                   @ApiParam("状态 1正常 2隐藏") Integer state) {
        return Result.success().put("data", bannerService.selectPage(page, limit, classify, state));
    }


    @RequestMapping(value = "/selectBannerById", method = RequestMethod.GET)
    @ApiOperation("根据id查看详细信息")
    @ResponseBody
    public Result selectBannerById(@ApiParam("banner图id") Long id) {
        return Result.success().put("data", bannerService.selectBannerById(id));
    }

    @RequestMapping(value = "/updateBannerStateById", method = RequestMethod.POST)
    @ApiOperation("隐藏banner图")
    @ResponseBody
    public Result updateBannerStateById(@ApiParam("banner图id")Long id) {
        return bannerService.updateBannerStateById(id);
    }

    @RequestMapping(value = "/updateBannerById", method = RequestMethod.POST)
    @ApiOperation("修改banner图")
    @ResponseBody
    public Result updateBannerById(@RequestBody Banner banner) {
        bannerService.updateBannerById(banner);
        return Result.success();
    }

    @RequestMapping(value = "/deleteBannerById", method = RequestMethod.GET)
    @ApiOperation("删除banner图")
    @ResponseBody
    public Result deleteBannerById(@ApiParam("banner图id，多个逗号分隔")String ids) {
        bannerService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.success();
    }

    @RequestMapping(value = "/insertBanner", method = RequestMethod.POST)
    @ApiOperation("添加banner图")
    @ResponseBody
    public Result insertBanner(@RequestBody Banner banner) {
        bannerService.insertBanner(banner);
        return Result.success();
    }


}