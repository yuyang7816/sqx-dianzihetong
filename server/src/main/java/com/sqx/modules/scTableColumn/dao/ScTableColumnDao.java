package com.sqx.modules.scTableColumn.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.contract.vo.DataJsonVo;
import com.sqx.modules.scTableColumn.entity.ScTableColumn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScTableColumnDao extends BaseMapper<ScTableColumn> {
    int updateBatch(@Param("dataJsonVos") List<DataJsonVo> dataJsonVos);
}
