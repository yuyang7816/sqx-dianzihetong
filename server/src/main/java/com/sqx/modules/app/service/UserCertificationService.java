package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.Result;

public interface UserCertificationService {
    Result insert(Long userId, String name, String idNumber, String front, String back,String address,String phone);

    Result isInsert(Long userId);

    Result queryCertification(Long page, Long limit, String status, String name);

    Result queryUserCertification(IPage iPage, String name,String phone);

    Result auditorUserCertification(Integer status, Long userId, String remek,Long id,Integer isSubmit);

    Result queryInsert(Long userId);
}
