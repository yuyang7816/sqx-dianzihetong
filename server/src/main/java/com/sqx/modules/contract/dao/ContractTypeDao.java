package com.sqx.modules.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.modules.contract.entity.ContractType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Admin
* @description 针对表【contract_type(合同模版类型表)】的数据库操作Mapper
* @createDate 2024-08-08 18:13:51
* @Entity generator.domain.ContractType
*/
@Mapper
public interface ContractTypeDao extends BaseMapper<ContractType> {

    IPage<ContractType> selectByAllList(@Param("pages") IPage<ContractType> pages,
                                        @Param("contractType") ContractType contractType);

    List<ContractType> selectByAll();

}




