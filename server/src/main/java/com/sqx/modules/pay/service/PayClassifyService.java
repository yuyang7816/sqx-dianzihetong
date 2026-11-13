package com.sqx.modules.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.pay.entity.PayClassify;

/**
* @author Admin
* @description 针对表【pay_classify】的数据库操作Service
* @createDate 2026-01-29 10:37:57
*/
public interface PayClassifyService extends IService<PayClassify> {


    Result addPayClassify(PayClassify payClassify);


    Result deletePayClassify(Long payClassifyId);

    Result queryPayClassify(Long payClassifyId);

    Result queryPayClassifyList(Integer page, Integer limit, PayClassify payClassify);

}
