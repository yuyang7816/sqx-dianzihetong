package com.sqx.modules.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.pay.dao.PayClassifyDao;
import com.sqx.modules.pay.entity.PayClassify;
import com.sqx.modules.pay.service.PayClassifyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author Admin
* @description 针对表【pay_classify】的数据库操作Service实现
* @createDate 2026-01-29 10:37:57
*/
@Service
public class PayClassifyServiceImpl extends ServiceImpl<PayClassifyDao, PayClassify>
    implements PayClassifyService{

    @Override
    public Result addPayClassify(PayClassify payClassify) {
        if (payClassify.getPayClassifyId() != null){
            PayClassify payClassify1 = this.getById(payClassify.getPayClassifyId());
            if (payClassify1 == null){
                return Result.error("数据不存在");
            }
            return this.updateById(payClassify) ? Result.success() : Result.error();
        }else {
            payClassify.setCreateTime(DateUtils.format(new Date()));
            return this.save(payClassify) ? Result.success() : Result.error();
        }
    }

    @Override
    public Result deletePayClassify(Long payClassifyId) {
        PayClassify payClassify1 = this.getById(payClassifyId);
        if (payClassify1 == null){
            return Result.error("数据不存在");
        }
        return this.removeById(payClassifyId) ? Result.success() : Result.error();
    }

    @Override
    public Result queryPayClassify(Long payClassifyId) {
        PayClassify payClassify1 = this.getById(payClassifyId);
        if (payClassify1 == null){
            return Result.error("数据不存在");
        }
        return Result.success().put("data",payClassify1);
    }

    @Override
    public Result queryPayClassifyList(Integer page, Integer limit, PayClassify payClassify) {
        Page<PayClassify> pages;
        if (page != null && limit != null){
            pages = new Page<>(page,limit);
        }else {
            pages = new Page<>(1,Integer.MAX_VALUE);
        }
        LambdaQueryWrapper<PayClassify> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(payClassify.getExecuted())){
            queryWrapper.like(PayClassify::getExecuted,payClassify.getExecuted());
        }
        IPage<PayClassify> payClassifyPage = baseMapper.selectPage(pages,queryWrapper);
        return Result.success().put("data",payClassifyPage);
    }
}




