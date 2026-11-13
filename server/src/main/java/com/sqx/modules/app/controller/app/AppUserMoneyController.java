package com.sqx.modules.app.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/app/userMoney")
@Api(value = "用户端-钱包", tags = {"用户端-钱包"})

public class AppUserMoneyController {

    private UserMoneyService userMoneyService;

    @GetMapping("/selectMyMoney")
    @Login
    @ApiOperation("我的钱包余额")
    public Result selectMyMoney(@ApiParam("拦截器获取的用户id") @RequestAttribute Long userId) {
        return Result.success().put("data", userMoneyService.selectUserMoneyByUserId(userId));
    }

    @GetMapping("/selectMyAvailableMoney")
    @Login
    @ApiOperation("可用金额（暂未使用）")
    public Result selectMyAvailableMoney(@ApiParam("拦截器获取的用户id") @RequestAttribute Long userId) {
        return Result.success().put("data", userMoneyService.selectUserAvailableMoneyByUserId(userId));
    }

    @GetMapping("/selectMyFreezeMoney")
    @Login
    @ApiOperation("冻结金额（暂未使用）")
    public Result selectMyFreezeMoney(@ApiParam("拦截器获取的用户id") @RequestAttribute Long userId) {
        return Result.success().put("data", userMoneyService.selectUserFreezeMoneyByUserId(userId));
    }

    @GetMapping("/moneyDetailed")
    @Login
    @ApiOperation("资金明细（暂未使用）")
    public Result moneyDetailed(@ApiParam("拦截器获取的用户id") @RequestAttribute Long userId,
                                @ApiParam("页码") @RequestParam(required = false) Long page,
                                @ApiParam("条数") @RequestParam(required = false) Long limit) {
        Page<UserMoneyDetails> pages = new Page<>(page, limit);
        return Result.success().put("data", userMoneyService.moneyDetailed(userId, pages));
    }

    @GetMapping("/drawCard")
    @Login
    @ApiOperation("提现账号（暂未使用）")
    public Result drawCard(@ApiParam("拦截器获取的用户id") @RequestAttribute Long userId) {
        return Result.success().put("data", userMoneyService.drawCard(userId));
    }


    @GetMapping("/drawRecord")
    @Login
    @ApiOperation("提现记录（暂未使用）")
    public Result drawRecord(@ApiParam("拦截器获取的用户id") @RequestAttribute Long userId,
                             @ApiParam("页码") @RequestParam(required = false) Long page,
                             @ApiParam("条数") @RequestParam(required = false) Long limit) {
        if (page != null || limit != null) {
            return Result.error("分页条件为空");
        } else {
            IPage ipage = new Page(page, limit);
            return Result.success().put("data", userMoneyService.drawRecord(userId, ipage));
        }
    }


    /**
     * 我的提现
     */
    @GetMapping("/selectMyProfit")
    @Login
    @ApiOperation("我的提现")
    public Result selectMyProfit(@ApiParam("拦截器获取的用户id") @RequestAttribute Long userId) {
        return Result.success().put("data", userMoneyService.selectMyProfit(userId));
    }
//
//    /**
//     * 付款接单
//     */
//    @GetMapping("/payTakingOrder")
//    @Login
//    @ApiOperation("付款接单或会员")
//    public Result payTakingOrder(@RequestAttribute Long userId, Long orderId) {
//        return Result.success().put("data", userMoneyService.payTakingOrder(userId, orderId));
//    }

//    /**
//     * 余额明细
//     */
//    @GetMapping("/balanceDetailed")
//    @Login
//    @ApiOperation("余额明细")
//    public Result balanceDetailed(@RequestAttribute Long userId, Long page, Long limit) {
//        Page<UserMoneyDetails> pages = new Page<>(page, limit);
//        return Result.success().put("data", userMoneyService.balanceDetailed(userId, pages));
//    }
//
//
//    /**
//     * 收益明细
//     */
//    @GetMapping("/profitDetailed")
//    @Login
//    @ApiOperation("收益明细")
//    public Result profitDetailed(@RequestAttribute Long userId, Long page, Long limit) {
//        if (page != null || limit != null) {
//            return Result.error("分页条件为空");
//        } else {
//            IPage ipage = new Page(page, limit);
//            return Result.success().put("data", userMoneyService.profitDetailed(userId, ipage));
//        }
//    }

}
