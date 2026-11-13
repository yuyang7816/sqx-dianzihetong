package com.sqx.modules.enterprise.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.enterprise.entity.Enterprise;

import java.text.ParseException;

/**
* @author Admin
* @description 针对表【enterprise】的数据库操作Service
* @createDate 2024-08-08 15:48:50
*/
public interface EnterpriseService extends IService<Enterprise> {

    /**
     * app端添加提交或修改企业信息
     * @param enterprise
     * @return
     */
    Result addEnterprise(Enterprise enterprise);

    /**
     * 管理后台审核企业认证
     * @param enterprise
     * @return
     */
    Result authEnterprise(Enterprise enterprise);

    /**
     * 管理后台获取企业列表
     * @param page
     * @param limit
     * @param enterprise
     * @return
     */
    Result getEnterpriseList(Integer page,Integer limit,Enterprise enterprise);

    /**
     * 管理后台删除企业
     * @param id
     * @return
     */
    Result deleteEnterprise(Long id);

    /**
     * 管理后台获取企业信息详情
     * @param id
     * @return
     */
    Result getEnterpriseDetail(Long id);

    /**
     * 用户端获取企业信息详情
     * @param id
     * @return
     */
    Result appGetEnterpriseDetail(Long id,Long userId);

    Result selectEnterpriseList(Integer page, Integer limit, Enterprise enterprise, Long userId);


    Result getEnterpriseInfo(String enterpriseName);

}
