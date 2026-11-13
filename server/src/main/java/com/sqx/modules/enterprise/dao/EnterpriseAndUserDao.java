package com.sqx.modules.enterprise.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.enterprise.entity.EnterpriseAndUser;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Admin
* @description 针对表【enterprise_and_user(企业和用户关联表)】的数据库操作Mapper
* @createDate 2024-08-09 17:42:51
* @Entity generator.domain.EnterpriseAndUser
*/
@Mapper
public interface EnterpriseAndUserDao extends BaseMapper<EnterpriseAndUser> {

}




