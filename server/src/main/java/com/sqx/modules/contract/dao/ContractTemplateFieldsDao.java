package com.sqx.modules.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.contract.entity.ContractTemplateFields;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Admin
* @description 针对表【contract_template_fields(合同模板额外字段表)】的数据库操作Mapper
* @createDate 2024-08-12 15:49:36
* @Entity generator.domain.ContractTemplateFields
*/
@Mapper
public interface ContractTemplateFieldsDao extends BaseMapper<ContractTemplateFields> {

    List<ContractTemplateFields> selectByContractId(Long id);

    void updateBatch(@Param(value = "templateFieldList")List<ContractTemplateFields> templateFieldList);

    void insertBatch(@Param(value = "templateFieldList")List<ContractTemplateFields> templateFieldList);

    List<ContractTemplateFields> selectByTemplateId(@Param("templateId")Long templateId);
}




