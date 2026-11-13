package com.sqx.modules.search.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.search.service.AppSearchService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 搜索记录
 *
 * @author liyuan
 * @since 2021-07-17
 */
@RestController
@RequestMapping("app/Search")
@AllArgsConstructor
@Slf4j
@Api(value = "用户端-搜索记录", tags = {"用户端-搜索记录（暂未使用）"})
public class AppSearchController {

    private AppSearchService appSearchService;
    /**
     * 查询搜索记录
     */
    @CrossOrigin
    @Login
    @RequestMapping(value = "/selectAppSearchNum", method = RequestMethod.GET)
    public Result selectAppSearchNum(@RequestAttribute Long userId) {
        return appSearchService.selectAppSearchNum(userId);
    }

    /**
     * 删除用户的搜索记录
     */
    @Login
    @RequestMapping(value = "/deleteAppSearch", method = RequestMethod.GET)
    public Result deleteAppSearch(@RequestAttribute Long userId) {
        return appSearchService.deleteAppSearch(userId);
    }
}
