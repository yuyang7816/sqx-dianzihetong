package com.sqx.modules.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserVip;
import com.sqx.modules.app.response.HomeMessageResponse;
import com.sqx.modules.app.response.UserMessageResponse;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.service.UserVipService;
import com.sqx.modules.contract.dao.ContractDao;
import com.sqx.modules.pay.service.PayDetailsService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fang
 * @date 2020/7/30
 */
@RestController
@Api(value = "管理端-用户管理", tags = {"管理端-用户管理"})
@RequestMapping(value = "/user")
@Slf4j
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private PayDetailsService payDetailsService;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private ContractDao contractDao;

    @RequestMapping(value = "/selectUserByInvitationCode", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserByInvitationCode(@ApiParam("邀请码") @RequestParam(required = false)String invitationCode) {
        Map<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.queryByInvitationCode(invitationCode);
        Long userId = userEntity.getUserId();
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
        Double money = 0.0;
        if (userMoney != null) {
            money = userMoney.getMoney().doubleValue();
        }
        //查询用户钱包
        Double m = money;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        //查询本月充值
        Double consume = payDetailsService.instantselectSumPay(date, userId);
        //查询本月提现
        Double income = userMoneyDetailsService.monthIncome(date, userId);
//        //查询邀请人数
//        int count = userService.queryInviterCount(userEntity.getInvitationCode());
        //本月接单
//        int takeOrdersCount = ordersService.selectTakeOrdersCount(userId, date);
//        //本月订单
//        int myOrdersCount = ordersService.selectMyOrdersCount(userId, date);
//        UserVip userVip = userVipService.getOne(new QueryWrapper<UserVip>().eq("user_id",userId));
//        if(userVip!=null){
//            userEntity.setIsVip(userVip.getIsVip());
//            userEntity.setVipEndTime(userVip.getEndTime());
//        }
        map.put("userEntity", userEntity);
        map.put("money", m);
        map.put("consume", consume);
        map.put("income", income);
//        map.put("count", count);
//        map.put("takeOrdersCount", takeOrdersCount);
//        map.put("myOrdersCount", myOrdersCount);
        return Result.success().put("data", map);
    }

    @RequestMapping(value = "/payMessage", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result payMessage(@ApiParam(value = "日期") String date,
                             @ApiParam(value = "type筛选：1年月日 2年月 3年") Integer type) {
        return Result.success().put("data", payDetailsService.payMessage(date, type));
    }


    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@ApiParam("用户id") @PathVariable Long userId) {
        Map<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.queryByUserId(userId);
//        DlMoney dlMoney = dlMoneyService.getOne(new QueryWrapper<DlMoney>().eq("user_id",userId));
        Double money = 0.0;
        Double rozen = 0.0;
//        if (dlMoney != null) {
//            money = dlMoney.getMoney().doubleValue();
//            rozen = dlMoney.getRozen().doubleValue();
//        }
        //查询用户钱包
        Double m = money;
        Double z = rozen;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
//        //查询本月充值
//        Double consume = payDetailsService.instantselectSumPay(date, userId);
//        //查询本月提现
//        Double income = userMoneyDetailsService.monthIncome(date, userId);
//        //查询邀请人数
//        int count = userService.queryInviterCount(userEntity.getInvitationCode());
        //本月接单
//        int takeOrdersCount = dlOrderService.selectTakeOrdersCount(userId, date);
//        //本月订单
//        int myOrdersCount = dlOrderService.selectMyOrdersCount(userId, date);
//        UserVip userVip = userVipService.getOne(new QueryWrapper<UserVip>().eq("user_id",userId));
//        if(userVip!=null){
//            userEntity.setIsVip(userVip.getIsVip());
//            userEntity.setVipEndTime(userVip.getEndTime());
//        }

        map.put("userEntity", userEntity);
        map.put("money", m);
//        map.put("consume", consume);
//        map.put("income", income);
//        map.put("count", count);
        map.put("rozen", z);
//        map.put("takeOrdersCount", takeOrdersCount);
//        map.put("myOrdersCount", myOrdersCount);
        return Result.success().put("data", map);
    }

    @RequestMapping(value = "/addCannotMoney/{userId}/{money}/{type}", method = RequestMethod.POST)
    @ApiOperation("修改金额（未使用）")
    @ResponseBody
    public Result addCannotMoney(@ApiParam("用户id") @PathVariable("userId") Long userId,
                                 @ApiParam("金额") @PathVariable("money") Double money,
                                 @ApiParam("1增 2减") @PathVariable("type") Integer type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        if (type == 1) {
//            dlMoneyService.updateMoney(1,userId,money);
//            MoneyDlLetails moneyDlLetails=new MoneyDlLetails();
//            moneyDlLetails.setMoneyAvailable (BigDecimal.valueOf(money));
//            moneyDlLetails.setUserId(userId);
//            moneyDlLetails.setContent("系统充值："+money);
////            moneyDlLetails.setTitle("系统充值金额");
//            moneyDlLetails.setType(1);
//            moneyDlLetails.setCreateTime(format);
//            dlMoneyLeteilsService.save(moneyDlLetails);
//            PayDetails payDetails = new PayDetails();
//            payDetails.setState(1);
//            payDetails.setCreateTime(format);
//            payDetails.setOrderId("系统充值金额");
//            payDetails.setUserId(userId);
//            payDetails.setMoney(money);
//            payDetails.setClassify(type);
//            payDetailsService.save(payDetails);
        } else {
//            dlMoneyService.updateMoney(2,userId,money);
//            MoneyDlLetails moneyDlLetails=new MoneyDlLetails();
//            moneyDlLetails.setMoneyAvailable(BigDecimal.valueOf(money));
//            moneyDlLetails.setUserId(userId);
//            moneyDlLetails.setContent("系统减少："+money);
////            moneyDlLetails.setTitle("系统减少金额");
//            moneyDlLetails.setType(2);
//            moneyDlLetails.setCreateTime(format);
//            dlMoneyLeteilsService.save(moneyDlLetails);
//            PayDetails payDetails = new PayDetails();
//            payDetails.setState(1);
//            payDetails.setCreateTime(format);
//            payDetails.setOrderId("系统减少金额");
//            payDetails.setUserId(userId);
//            payDetails.setMoney(money);
//            payDetails.setClassify(type);
//            payDetailsService.save(payDetails);
        }
        return Result.success();
    }


    @RequestMapping(value = "/selectUserList", method = RequestMethod.GET)
    @ApiOperation("查询所有用户列表")
    @ResponseBody
    public Result selectUserList(@ApiParam("页码") @RequestParam(required = false)Integer page,
                                 @ApiParam("条数") @RequestParam(required = false)Integer limit,
                                 @ApiParam("用户id 手机号 昵称 模糊搜索") @RequestParam(required = false)String phone,
                                 @ApiParam("性别 1男 2女") @RequestParam(required = false)Integer sex,
                                 @ApiParam("来源来源") @RequestParam(required = false)String platform,
                                 @ApiParam("手机终端") @RequestParam(required = false)String sysPhone,
                                 @ApiParam("开始时间") @RequestParam(required = false)String startTime,
                                 @ApiParam("结束时间") @RequestParam(required = false)String endTime,
                                 @ApiParam("用户昵称") @RequestParam(required = false)String userName,
                                 @ApiParam("邀请码") @RequestParam(required = false)String inviterCode,
                                 @ApiParam("状态 1正常 2封禁") @RequestParam(required = false)Integer status,
                                 @ApiParam("状态 0审核 1通过 2未通过") @RequestParam(required = false)Integer statuss) {
        return Result.success().put("data", userService.selectUserPage(page, limit, phone, sex, platform, sysPhone, status, startTime, endTime, userName, inviterCode, statuss));
    }


    @RequestMapping(value = "/deleteUserByUserId/{userId}", method = RequestMethod.POST)
    @ApiOperation("删除用户")
    @ResponseBody
    public Result deleteUserByUserId(@ApiParam("用户id") @PathVariable("userId") Long userId) {
        userService.removeById(userId);
        return Result.success();
    }

    @RequestMapping(value = "/updateUserByUserId", method = RequestMethod.POST)
    @ApiOperation("修改用户")
    @ResponseBody
    public Result updateUserByUserId(@RequestBody UserEntity userEntity) {
        userService.updateById(userEntity);
        return Result.success();
    }

    @RequestMapping(value = "/updateUserIsAuthentication", method = RequestMethod.GET)
    @ApiOperation("开启或关闭用户实名认证")
    @ResponseBody
    public Result updateUserIsAuthentication(@ApiParam("用户id") @RequestParam(required = false)Long userId) {
        UserEntity byId = userService.getById(userId);
        if (byId.getIsAuthentication() == null || byId.getIsAuthentication() == 0) {
            byId.setIsAuthentication(1);
        } else {
            byId.setIsAuthentication(0);
        }
        userService.updateById(byId);
        return Result.success();
    }

    @PostMapping("/updateUserVip")
    @ApiOperation("修改用户会员")
    public Result updateUserVip(@ApiParam("用户id") @RequestParam(required = false)Long userId) {
        UserVip userVip = userVipService.getOne(new QueryWrapper<UserVip>().eq("user_id", userId));
        Date time = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (userVip != null) {
            if (userVip.getIsVip() == 1) {
                try {
                    time = sdf.parse(userVip.getEndTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            userVip = new UserVip();
            userVip.setCreateTime(sdf.format(new Date()));
            userVip.setUserId(userId);
            userVip.setVipNameType(0);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.add(Calendar.MONTH, 1);
        userVip.setEndTime(sdf.format(cal.getTime()));
        userVip.setIsVip(1);
        if (userVip.getVipId() != null) {
            userVipService.updateById(userVip);
        } else {
            userVipService.save(userVip);
        }
        return Result.success();
    }


    @RequestMapping(value = "/updateUserStatusByUserId", method = RequestMethod.GET)
    @ApiOperation("禁用或启用用户")
    @ResponseBody
    public Result updateUserByUserId(@ApiParam("用户id") @RequestParam(required = false)Long userId) {
        UserEntity byId = userService.getById(userId);
        if (byId.getStatus().equals(1)) {
            byId.setStatus(2);
        } else {
            byId.setStatus(1);
        }
        userService.updateById(byId);
        return Result.success();
    }


    /**
     * 获取openid
     *
     * @param code 微信code
     * @return openid
     */
    @GetMapping("/openId/{code:.+}/{userId}")
    @ApiOperation("根据code获取openid")
    public Result getOpenid(@ApiParam("微信code") @PathVariable("code") String code,
                            @ApiParam("用户id") @PathVariable("userId") Long userId) {
        return userService.getOpenId(code, userId);
    }

    /**
     * 信息分析
     *
     * @return
     */
//    @GetMapping("/homeMessage")
//    @ApiOperation("信息分析")
//    public Result homeMessage() {
//        HomeMessageResponse homeMessageResponse = new HomeMessageResponse();
//        //   0查总   1查天  2查月  3查年
//        //设置总用户人数
//        homeMessageResponse.setTotalUsers(userService.queryUserCount(0, null));
//        //设置今日新增
//        homeMessageResponse.setNewToday(userService.queryUserCount(1, null));
//        //设置本月新增
//        homeMessageResponse.setNewMonth(userService.queryUserCount(2, null));
//        //设置本年新增
//        homeMessageResponse.setNewYear(userService.queryUserCount(3, null));
//
//        //设置总收入
//        homeMessageResponse.setTotalRevenue(userService.queryPayMoney(0));
//        //设置今日收入
//        homeMessageResponse.setTodayRevenue(userService.queryPayMoney(1));
//        //设置本月收入
//        homeMessageResponse.setMonthRevenue(userService.queryPayMoney(2));
//        //设置本年收入
//        homeMessageResponse.setYearRevenue(userService.queryPayMoney(3));
//
//        return Result.success().put("data", homeMessageResponse);
//    }
    @GetMapping("/homeMessage")
    @ApiOperation("信息分析")
    public Result homeMessage() {
        HomeMessageResponse homeMessageResponse = new HomeMessageResponse();
        //   0查总   1查天  2查月  3查年
        //设置总用户人数
        homeMessageResponse.setTotalUsers(userService.queryUserCount(0, null));
        //设置今日新增
        homeMessageResponse.setNewToday(userService.queryUserCount(1, null));
        //设置本月新增
        homeMessageResponse.setNewMonth(userService.queryUserCount(2, null));
        //设置本年新增
        homeMessageResponse.setNewYear(userService.queryUserCount(3, null));

        //设置总收入
        homeMessageResponse.setTotalRevenue(userService.queryPayMoney(0));
        //设置今日收入
        homeMessageResponse.setTodayRevenue(userService.queryPayMoney(1));
        //设置本月收入
        homeMessageResponse.setMonthRevenue(userService.queryPayMoney(2));
        //设置本年收入
        homeMessageResponse.setYearRevenue(userService.queryPayMoney(3));

        return Result.success().put("data", homeMessageResponse);
    }

    /**
     * 接单分析
     *
     * @return
     */
    @GetMapping("/takingOrdersMessage")
    @ApiOperation("接单分析")
    public Result takingOrdersMessage(@ApiParam("页码") @RequestParam(required = false)Long page,
                                      @ApiParam("条数") @RequestParam(required = false)Long limit,
                                      @ApiParam("时间") @RequestParam(required = false)String date,
                                      @ApiParam("日期类型") @RequestParam(required = false)Long type) {
        Page<Map<String, Object>> iPage = new Page<>(page, limit);
        return userService.takingOrdersMessage(iPage, type, date);
    }

    /**
     * 用户分析
     */
    @GetMapping("/userMessage")
    @ApiOperation("用户分析")
    public Result userMessage(@ApiParam("时间") @RequestParam(required = false)String date,
                              @ApiParam("日期类型 1日2月3年") @RequestParam(required = false)int type) {
        UserMessageResponse userMessageResponse = new UserMessageResponse();
        userMessageResponse.setTotalNumber(userService.queryUserCount(type, date));
        userMessageResponse.setVipUserNumber(userService.userMessage(date, type));
        userMessageResponse.setUserNumber(userService.queryUserCount(type, date) - userService.userMessage(date, type));
        return Result.success().put("data", userMessageResponse);
    }

    @GetMapping("/contractAnalysis")
    @ApiOperation("合同分析")
    public Result contractAnalysis(@ApiParam("时间") @RequestParam(required = false)String date,
                                   @ApiParam("日期类型 1日2月3年") @RequestParam(required = false)int type) {
        return userService.contractAnalysis(date, type);
    }

    @GetMapping("/enterpriseAnalysis")
    @ApiOperation("企业分析")
    public Result enterpriseAnalysis(@ApiParam("时间") @RequestParam(required = false)String date,
                                     @ApiParam("日期类型 1日2月3年") @RequestParam(required = false)int type) {
        return userService.enterpriseAnalysis(date, type);
    }

    @GetMapping("/selectUserCountStatisticsByTime")
    @ApiOperation("新增的用户的统计折线图")
    public Result selectUserCountStatisticsByTime(@RequestParam(required = false) @ApiParam("开始时间") String startTime,
                                                  @RequestParam(required = false) @ApiParam("结束时间")String endTime) {
        List<Integer> userCountList = new ArrayList<>();
        List<String> year = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(startTime);
            calendar.setTime(parse);
        } catch (ParseException e) {
            log.error("时间转换错误");
        }
        while (true) {
            String dateTime = simpleDateFormat.format(calendar.getTime());
            int i = userService.queryUserCount(1, dateTime);
            userCountList.add(i);
            year.add(dateTime);
            if (dateTime.equals(endTime)) {
                break;
            }
            calendar.add(Calendar.DATE, 1);
        }
        Map<String, Object> result = new HashMap<>();
        // 折线图数据 新增的用户
        result.put("userCountList", userCountList);
        // 折线图数据 时间
        result.put("year", year);
        return Result.success().put("data", result);
    }

    @GetMapping("/selectContractCountByTime")
    @ApiOperation("用户新增的合同的统计折线图")
    public Result selectContractCountByTime(@RequestParam(required = false) @ApiParam("开始时间") String startTime,
                                            @RequestParam(required = false) @ApiParam("结束时间")String endTime) {
        List<Integer> contractCount = new ArrayList<>();
        List<String> year = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(startTime);
            calendar.setTime(parse);
        } catch (ParseException e) {
            log.error("时间转换错误");
        }
        while (true) {
            String dateTime = simpleDateFormat.format(calendar.getTime());
            int i = contractDao.selectAllCount(dateTime,1);
            contractCount.add(i);
            year.add(dateTime);
            if (dateTime.equals(endTime)) {
                break;
            }
            calendar.add(Calendar.DATE, 1);
        }
        Map<String, Object> result = new HashMap<>();
        // 折线图数据 新增的合同
        result.put("contractCount", contractCount);
        // 折线图数据 时间
        result.put("year", year);
        return Result.success().put("data", result);
    }
}