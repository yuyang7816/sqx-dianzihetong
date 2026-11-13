package com.sqx.modules.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.contract.entity.ContractAndUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Admin
* @description 针对表【contract_and_user(发起人和合同关联表)】的数据库操作Mapper
* @createDate 2024-08-09 16:19:16
* @Entity generator.domain.ContractAndUser
*/
@Mapper
public interface ContractAndUserDao extends BaseMapper<ContractAndUser> {

    void insertBatch(@Param("contractAndUsers") List<ContractAndUser> contractAndUsers);

    List<ContractAndUser> selectByContractId(Long id);

    ContractAndUser selectByUserIdOrSignedBy(@Param("contractId")Long contractId,
                                             @Param("userId") Long userId);

    ContractAndUser selectByContractAndUser(@Param("contractId")Long contractId,
                                            @Param("userId") Long userId);

    ContractAndUser selectByReceivingUser(@Param("userId")Long userId);

    List<ContractAndUser> selectByStatus(@Param("id") Long id);

    List<ContractAndUser> selectByContractId1(@Param("contractId") Long contractId);
}




