package com.sqx.modules.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.contract.entity.ContractSeal;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/8/15 17:21
 */
public interface ContractSealService extends IService<ContractSeal> {

    /**
     * 添加或修改合同印章(个人签名、个人印章。公司印章)
     * @param contractSeal
     * @return
     */
    Result addContractSeal(ContractSeal contractSeal);

    /**
     * 删除合同印章
     * @param id
     * @return
     */
    Result deleteContractSeal(Long id,Long userId);

    /**
     * 查询合同印章详情
     * @param id
     * @return
     */
    Result selectContractSealDetails(Long id,Long userId);

    /**
     * 查询合同印章列表
     * @param page
     * @param limit
     * @param contractSeal
     * @return
     */
    Result selectContractSealList(Integer page,Integer limit,ContractSeal contractSeal);

    /**
     * 获取用户所有的合同印章
     * @param page
     * @param limit
     * @return
     */
    Result selectContractSealList1(Integer page, Integer limit, ContractSeal contractSeal);

    Result addContractSeal1(ContractSeal contractSeal);

    Result deductImage(String content,UserEntity userEntity);
}
