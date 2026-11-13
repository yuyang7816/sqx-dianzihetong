package com.sqx.modules.scTableColumn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.contract.entity.ContractTemplate;
import com.sqx.modules.contract.service.ContractTemplateService;
import com.sqx.modules.scTableColumn.dao.ScTableColumnDao;
import com.sqx.modules.scTableColumn.entity.ScTableColumn;
import com.sqx.modules.scTableColumn.service.ScTableColumnService;
import com.sqx.modules.scTableColumn.vo.ColumnData;
import com.sqx.modules.scTableColumn.vo.JsonRootBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScTableColumnServiceImpl extends ServiceImpl<ScTableColumnDao, ScTableColumn> implements ScTableColumnService {
    @Autowired
    private ScTableColumnDao scTableColumnDao;
    @Autowired
    ContractTemplateService contractTemplateService;

    @Override
    public Result insertScTable(JsonRootBean jsonRootBean) {
        ContractTemplate contractTemplate = contractTemplateService.getById(jsonRootBean.getContractId());
        if (contractTemplate == null) {
            return Result.error("当前合同模板不存在!");
        }
        List<ColumnData> columnData = jsonRootBean.getColumnData();
        //校验提交的字段是否重复,如果有则返回重复字段
        List<String> collect = columnData.stream().collect(Collectors.groupingBy(ColumnData::getColumnName, Collectors.counting()))
                .entrySet().stream().filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            StringBuilder str = new StringBuilder();
            str.append("同一合同模板下不能有重复提交的字段：");
            for (int i = 0; i < collect.size(); i++) {
                str.append(collect.get(i));
                if (i < collect.size() - 1) {
                    str.append(",");
                }
            }
            return Result.error(str.toString());
        }
        scTableColumnDao.delete(new QueryWrapper<ScTableColumn>().eq("contract_id", contractTemplate.getId()));
        for (ColumnData columnDatum : columnData) {
            ScTableColumn tableColumn = new ScTableColumn();
            BeanUtils.copyProperties(columnDatum, tableColumn);
            tableColumn.setOptionJson(columnDatum.getOptionJson());
            tableColumn.setContractId(jsonRootBean.getContractId());
            tableColumn.setCreateTime(new Date());
            tableColumn.setUpdateTime(new Date());
            scTableColumnDao.insert(tableColumn);
        }
        return Result.success();
    }

    @Override
    public List<ScTableColumn> getTableByContractId(Long contractId) {
        return scTableColumnDao.selectList(new QueryWrapper<ScTableColumn>().eq("contract_id", contractId).orderByAsc("sort"));

    }

    @Override
    public Result deleteTableByContractId(Long contractId) {
        List<ScTableColumn> columnList = scTableColumnDao.selectList(new QueryWrapper<ScTableColumn>().eq("contract_id", contractId));
        if (!columnList.isEmpty()) {
            return Result.error("删除失败,该合同模板下已有用户发布数据,无法删除!");
        }
        return Result.upStatus(scTableColumnDao.delete(new QueryWrapper<ScTableColumn>().eq("contract_id", contractId)));
    }
}
