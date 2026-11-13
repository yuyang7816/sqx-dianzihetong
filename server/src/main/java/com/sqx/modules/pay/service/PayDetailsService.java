package com.sqx.modules.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.PageUtils;
import com.sqx.modules.pay.entity.PayDetails;

import java.util.HashMap;

public interface PayDetailsService extends IService<PayDetails> {

    PageUtils selectPayDetails(int page, int limit, String startTime, String endTime, Long userId, Integer state);

    Double selectSumPay(String createTime, String endTime, Long userId);

    PageUtils payMemberAnalysis(int page, int limit, String time, Integer flag);

    PageUtils selectUserMemberList(int page, int limit, String phone);

    Double selectSumMember(String time, Integer flag);

    Double selectSumPayByState(String time, Integer flag, Integer state);

    Double selectSumPayByClassify(String time, Integer flag, Integer classify,Integer payClassify);

    Double instantselectSumPay(String date, Long userId);

    HashMap<String,Object> payMessage(String date, Integer type);
}
