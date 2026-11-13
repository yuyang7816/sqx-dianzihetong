package com.sqx.modules.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.contract.entity.ContractType;

/**
* @author Admin
* @description 针对表【contract_type(合同模版类型表)】的数据库操作Service
* @createDate 2024-08-08 18:13:51
*/
public interface ContractTypeService extends IService<ContractType> {

    /**
     * 管理后台添加或修改合同模版类型
     * @param contractType
     * @return
     */
    Result addContractType(ContractType contractType);

    /**
     * 管理后台删除合同模版类型
     * @param id
     * @return
     */
    Result deleteContractType(Long id);

    /**
     * 管理后台获取合同模版类型列表
     * @param page
     * @param limit
     * @param contractType
     * @return
     */
    Result getContractTypeList(Integer page, Integer limit, ContractType contractType);

    /**
     * 管理后台获取合同模版类型详情
     * @param id
     * @return
     */
    Result getContractTypeDetail(Long id);
}
