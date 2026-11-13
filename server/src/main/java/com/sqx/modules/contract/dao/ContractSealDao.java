package com.sqx.modules.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.modules.contract.entity.ContractSeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Admin
* @description 针对表【contract_seal】的数据库操作Mapper
* @createDate 2024-08-15 17:10:09
* @Entity generator.domain.ContractSeal
*/
@Mapper
public interface ContractSealDao extends BaseMapper<ContractSeal> {

    IPage<ContractSeal> selectContractSealList(@Param("pages")IPage<ContractSeal> pages,
                                               @Param("contractSeal") ContractSeal contractSeal);
}




