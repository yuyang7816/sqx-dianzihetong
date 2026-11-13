package com.sqx.modules.contract.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.contract.entity.Contract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Admin
* @description 针对表【contract】的数据库操作Mapper
* @createDate 2024-08-12 17:10:08
* @Entity generator.domain.Contract
*/
@Mapper
public interface ContractDao extends BaseMapper<Contract> {

    Page<Contract> selectMySigned(@Param("pages")IPage<Contract> pages,
                                  @Param("userId") Long userId);

    Page<Contract> selectMyReceive(@Param("pages") IPage<Contract> pages,
                                   @Param("userId") Long userId);

    Page<Contract> selectMyAllContractList(@Param("pages")IPage<Contract> pages,
                                           @Param("userId")Long userId,
                                           @Param("contractName")String contractName,
                                           @Param("isComplete") Integer isComplete);

    Integer selectCountByMyReceive(@Param("userId") Long userId);

    Integer selectCountByMySigned(@Param("userId") Long userId);

    Integer selectCountByMyCopy(@Param("userId")Long userId);

    Integer selectCountByMyOperate(@Param("userId") Long userId);

    Integer selectCountByOtherOperate(@Param("userId") Long userId);

    Integer selectCountByMyFinish(@Param("userId") Long userId);

    Page<Contract> selectMyCopyList(@Param("pages") IPage<Contract> pages,
                                    @Param("userId") Long userId);

    Integer selectCountByWaitForSign(@Param("userId")Long userId);

    Integer selectCountByWaitForApproved(@Param("userId")Long userId);

    Integer selectCountByDraftBox(@Param("userId")Long userId);

    Page<Contract> selectDraftBoxList(@Param("pages")IPage<Contract> pages,
                                      @Param("userId")Long userId);

    Page<Contract> selectSignWaitList(@Param("pages")IPage<Contract> pages,
                                      @Param("userId")Long userId);

    List<Contract> selectApprovalList(@Param("pages")IPage<Contract> pages,
                                      @Param("userId") Long userId);

    IPage<Contract> selectAllContractList(IPage<Contract> pages,
                                          @Param("contract") Contract contract);

    int selectAllCount(@Param("date") String date,
                       @Param("type") int type);

    int selectAllCount1(@Param("date") String date,
                       @Param("type") int type);
}




