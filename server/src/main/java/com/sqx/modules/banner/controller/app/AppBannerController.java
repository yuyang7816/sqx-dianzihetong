package com.sqx.modules.banner.controller.app;


import com.sqx.common.utils.Result;
import com.sqx.modules.banner.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liyuan
 * @date 2021/8/9
 */
@Slf4j
@RestController
@Api(value = "用户端-banner图", tags = {"用户端-banner图"})
@RequestMapping(value = "/app/banner")
public class AppBannerController {


    @Autowired
    private BannerService bannerService;

    @RequestMapping(value = "/selectBannerList", method = RequestMethod.GET)
    @ApiOperation("查询所有banner图")
    @ResponseBody
    public Result selectBannerList(@ApiParam("分类 1 banner图  2 首页分类") Integer classify) {
        return bannerService.selectBannerList(classify);
    }


}