package com.sqx.modules.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.modules.contract.entity.ContractTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Admin
* @description 针对表【contract_template(合同模板表)】的数据库操作Mapper
* @createDate 2024-08-08 18:13:51
* @Entity generator.domain.ContractTemplate
*/
@Mapper
public interface ContractTemplateDao extends BaseMapper<ContractTemplate> {

    IPage<ContractTemplate> selectByTypeId(@Param("pages")IPage<ContractTemplate> pages,
                                           @Param("id")Long id);

    IPage<ContractTemplate> selectByAllList(@Param("pages") IPage<ContractTemplate> pages,
                                            @Param("contractTemplate") ContractTemplate contractTemplate);

}




