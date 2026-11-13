package com.sqx.modules.enterprise.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.enterprise.entity.Enterprise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Admin
* @description 针对表【enterprise】的数据库操作Mapper
* @createDate 2024-08-08 15:48:49
* @Entity generator.domain.Enterprise
*/
@Mapper
public interface EnterpriseDao extends BaseMapper<Enterprise> {

    IPage<Enterprise> selectByAllList(@Param("pages") Page<Enterprise> pages,
                                      @Param("enterprise")Enterprise enterprise);

    Enterprise selectByEnterprise(@Param("id") Long id);

    int selectAllCount(@Param("date") String date,
                       @Param("type") int type);
}


