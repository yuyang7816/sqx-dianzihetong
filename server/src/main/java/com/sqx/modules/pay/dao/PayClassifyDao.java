package com.sqx.modules.pay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.pay.entity.PayClassify;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Admin
* @description 针对表【pay_classify】的数据库操作Mapper
* @createDate 2026-01-29 10:37:57
* @Entity com.sqx.modules.pay.entity.PayClassify
*/
@Mapper
public interface PayClassifyDao extends BaseMapper<PayClassify> {

}




