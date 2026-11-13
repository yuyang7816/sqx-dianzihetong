package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import org.springframework.web.bind.annotation.RequestAttribute;

public interface UserMoneyService extends IService<UserMoney> {

    UserMoney selectUserMoneyByUserId(Long userId);

    void updateMoney(int i, Long userId, double money);

    Double selectMyProfit(Long userId);

//    Result payTakingOrder(Long userId, Long orderId);

    PageUtils balanceDetailed(@RequestAttribute Long userId, Page<UserMoneyDetails> ipage);

    Result profitDetailed(@RequestAttribute Long userId, IPage ipage);

    Result selectUserFreezeMoneyByUserId(Long userId);

    Result selectUserAvailableMoneyByUserId(Long userId);

    PageUtils moneyDetailed(@RequestAttribute Long userId, Page<UserMoneyDetails> pages);

    Result drawCard(Long userId);

    Result drawRecord(@RequestAttribute Long userId, IPage ipage);




}
