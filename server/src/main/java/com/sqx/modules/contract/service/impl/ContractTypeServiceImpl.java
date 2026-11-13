package com.sqx.modules.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.contract.dao.ContractTypeDao;
import com.sqx.modules.contract.entity.ContractType;
import com.sqx.modules.contract.service.ContractTypeService;
import com.sqx.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author Admin
* @description 针对表【contract_type(合同模版类型表)】的数据库操作Service实现
* @createDate 2024-08-08 18:13:51
*/
@Service
public class ContractTypeServiceImpl extends ServiceImpl<ContractTypeDao, ContractType>
    implements ContractTypeService {

    @Override
    public Result addContractType(ContractType contractType) {
        SysUserEntity sysUser = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (sysUser == null){
            return Result.error("用户未登录,请重新登录");
        }
        QueryWrapper<ContractType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_name",contractType.getTypeName());
        if (contractType.getId() != null){
            queryWrapper.ne("id",contractType.getId());
        }
        int count = baseMapper.selectCount(queryWrapper);
        if (count > 0){
            return Result.error("该类型已存在");
        }
        if (contractType.getId() != null){
            contractType.setUpdateUserId(sysUser.getUserId());
            contractType.setUpdateTime(new Date());
            baseMapper.updateById(contractType);
        }else {
            contractType.setCreateUserId(sysUser.getUserId());
            contractType.setCreateTime(new Date());
            baseMapper.insert(contractType);
        }
        return Result.success();
    }

    @Override
    public Result deleteContractType(Long id) {
        SysUserEntity sysUser = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (sysUser == null){
            return Result.error("用户未登录,请重新登录");
        }
        if (id != null){
            ContractType contractType = baseMapper.selectById(id);
            if (contractType != null){
                baseMapper.deleteById(id);
                return Result.success();
            }
        }
        return Result.error("删除失败");
    }

    @Override
    public Result getContractTypeList(Integer page, Integer limit, ContractType contractType) {
        SysUserEntity sysUser = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (sysUser == null){
            return Result.error("用户未登录,请重新登录");
        }
        IPage<ContractType> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        if(contractType == null){
            IPage<ContractType> contractTypeIPage = baseMapper.selectByAllList(pages, null);
            return Result.success().put("data",contractTypeIPage);
        }else {
            IPage<ContractType> contractTypeIPage = baseMapper.selectByAllList(pages, contractType);
            return Result.success().put("data",contractTypeIPage);
        }
    }

    @Override
    public Result getContractTypeDetail(Long id) {
        SysUserEntity sysUser = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (sysUser == null){
            return Result.error("用户未登录,请重新登录");
        }
        if (id != null){
            ContractType contractType = baseMapper.selectById(id);
            if (contractType != null){
                return Result.success().put("data",contractType);
            }
        }
        return Result.error("查看失败");
    }


}




