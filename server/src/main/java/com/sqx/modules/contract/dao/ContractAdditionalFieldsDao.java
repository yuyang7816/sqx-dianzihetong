package com.sqx.modules.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.contract.entity.ContractAdditionalFields;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Admin
* @description 针对表【contract_additional_fields】的数据库操作Mapper
* @createDate 2024-08-12 15:49:36
* @Entity generator.domain.ContractAdditionalFields
*/
@Mapper
public interface ContractAdditionalFieldsDao extends BaseMapper<ContractAdditionalFields> {

    void insertBatch(@Param("additionalFields")List<ContractAdditionalFields> additionalFields);

    List<ContractAdditionalFields> selectByContractId(Long id);
}




