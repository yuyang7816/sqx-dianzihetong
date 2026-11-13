package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.app.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {


//    IPage<UserEntity> selectUserPage(Page<UserEntity> page,@Param("search") String search,@Param("sex") Integer sex,@Param("platform") String platform, @Param("sysPhone")String sysPhone,@Param("status") Integer status,@Param("member")Integer member,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("userName")String userName,@Param("inviterCode")String inviterCode,@Param("isAuthentication") Integer isAuthentication);
    IPage<UserEntity> selectUserPage(Page<UserEntity> page,
                                     @Param("search") String search,
                                     @Param("sex") Integer sex,
                                     @Param("platform") String platform,
                                     @Param("sysPhone")String sysPhone,
                                     @Param("status") Integer status,
                                     @Param("startTime")String startTime,
                                     @Param("endTime")String endTime,
                                     @Param("userName")String userName,
                                     @Param("inviterCode")String inviterCode,
                                     @Param("statuss")Integer statuss);

    int queryInviterCount(@Param("inviterCode") String inviterCode);

    int queryUserCount(@Param("type") int type, @Param("date") String date);

    Double queryPayMoney(@Param("type") int type, @Param("date") String date);

    IPage<Map<String, Object>> queryCourseOrder(Page iPage, @Param("type") int type, @Param("date") String date);

    int userMessage(String date, int type);

    int insertUser(UserEntity userEntity);

    IPage<Map<String, Object>> takingOrdersMessage(Page<Map<String, Object>> iPage, @Param("type") Long type, @Param("date") String date);

    UserEntity selectUserByUserId(@Param("userId") Long userId,@Param("byUserId") Long byUserId);

    IPage<UserEntity> getAuthExtensionList(@Param("pages") Page<UserEntity> pages,
                                           @Param("invitationCode") String invitationCode);

    IPage<HashMap<String, Object>> getUserIncomeList(@Param("pages") Page<HashMap<String, Object>> pages,
                                                     @Param("type") Integer type,
                                                     @Param("invitationCode") String invitationCode,
                                                     @Param("userId") Long userId);
}
