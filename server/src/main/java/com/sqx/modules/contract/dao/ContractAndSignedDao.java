package com.sqx.modules.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.contract.entity.ContractAndSigned;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Admin
* @description 针对表【contract_and_signed(签署或审批人与合同相关表)】的数据库操作Mapper
* @createDate 2024-08-14 20:52:35
* @Entity generator.domain.ContractAndSigned
*/
@Mapper
public interface ContractAndSignedDao extends BaseMapper<ContractAndSigned> {

}




