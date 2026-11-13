package com.sqx.modules.pay.service;

import com.sqx.common.utils.Result;


/**
 * @author fang
 * @date 2020/2/26
 */
public interface WxService {

//    Result payOrder(Long id, Integer type) throws Exception;

    String payBack(String resXml,Integer type);

//    boolean refund(Orders orders);

    Result payDocument(Long userId, Integer classify, Long payClassifyId);
}