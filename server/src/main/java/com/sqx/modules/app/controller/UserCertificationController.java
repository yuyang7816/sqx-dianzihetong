package com.sqx.modules.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserCertificationDao;
import com.sqx.modules.app.entity.UserCertification;
import com.sqx.modules.app.service.UserCertificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/userCertification")
@Api(value = "管理端-实名认证", tags = {"管理端-实名认证"})
public class UserCertificationController {
    private UserCertificationService userCertificationService;

    @Autowired
    private UserCertificationDao userCertificationDao;


    /**
     * 实名列表
     */
    @GetMapping("/queryCertification")
    @ApiOperation("实名列表")
    public Result queryCertification(@ApiParam("页码") @RequestParam(required = false) Long page,
                                     @ApiParam("条数") @RequestParam(required = false) Long limit,
                                     @ApiParam("状态") @RequestParam(required = false) String status,
                                     @ApiParam("姓名") @RequestParam(required = false) String name) {
        return userCertificationService.queryCertification(page, limit, status, name);
    }

    /**
     * 查询已经进行实名的用户
     */
    @GetMapping("/queryUserCertification")
    @ApiOperation("查询已经进行实名的用户")
    public Result queryUserCertification(@ApiParam("页码") @RequestParam(required = false) Long page,
                                         @ApiParam("条数") @RequestParam(required = false) Long limit,
                                         @ApiParam("真实姓名") @RequestParam(required = false) String name,
                                         @ApiParam("页码") @RequestParam(required = false) String phone) {
        if (page == null || limit == null) {
            return Result.error("分页条件为空");
        } else {
            IPage<UserCertification> iPage = new Page<>(page, limit);

            return userCertificationService.queryUserCertification(iPage, name, phone);
        }
    }

    /**
     * 审核实名认证
     */
    @PostMapping("/auditorUserCertification")
    @ApiOperation("审核实名认证")
    public Result auditorUserCertification(@ApiParam("传1审核通过 其他为否") @RequestParam(required = false) Integer status,
                                           @ApiParam("用户id") @RequestParam(required = false) Long userId,
                                           @ApiParam("实名认证id") @RequestParam(required = false) Long id,
                                           @ApiParam("审核备注") @RequestParam(required = false) String remark,
                                           @ApiParam("是否可以再次提交审核 0是 1否") @RequestParam(required = false) Integer isSubmit) {
        if (status == null || id == null) {
            return Result.error("审核入参为空");
        } else {
            return userCertificationService.auditorUserCertification(status, userId, remark, id, isSubmit);
        }
    }


    /**
     * 审核实名认证
     */
    @PostMapping("/deleteCertification")
    @ApiOperation("删除实名认证")
    public Result deleteCertification(@ApiParam("实名认证id") @RequestParam(required = false) Long id) {
        UserCertification userCertification = userCertificationDao.selectById(id);
        if (userCertification == null) {
            return Result.error("实名认证不存在");
        }
        int result = userCertificationDao.deleteById(id);
        if (result > 0) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }


}
