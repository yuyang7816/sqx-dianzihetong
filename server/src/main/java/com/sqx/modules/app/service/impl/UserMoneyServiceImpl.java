package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.*;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class UserMoneyServiceImpl extends ServiceImpl<UserMoneyDao, UserMoney> implements UserMoneyService {
    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;
    @Autowired
    private UserVipDao userVipDao;
    @Autowired
    private VipDetailsDao vipDetailsDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;


    @Override
    public void updateMoney(int i, Long userId, double money) {
        baseMapper.updateMayMoney(i, userId, money);
    }

    @Override
    public UserMoney selectUserMoneyByUserId(Long userId) {
        return baseMapper.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userId));
    }

    @Override
    public Double selectMyProfit(Long userId) {

        return userMoneyDetailsDao.selectMyProfit(userId);
    }

//    @Override
//    public synchronized Result payTakingOrder(Long userId, Long orderId) {
//        //时间类型
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        //查看订单
////        Orders orders = ordersDao.selectOne(new QueryWrapper<Orders>().eq("orders_id", orderId));
//        if (orders == null) {
//            return Result.error("订单不存在！");
//        } else {
//            //如果是接单订单
//            if (orders.getOrdersType() == 1) {
//                if (orders.getState().equals("1")) {
//                    return Result.error("订单进行中！");
//                } else if (orders.getState().equals("2")) {
//                    return Result.error("订单已完成！");
//                } else if (orders.getState().equals("3")) {
//                    return Result.error("订单已退款！");
//                } else {
//                    //订单状态为待支付
//                    //查看用户账户余额
//                    UserMoney userMoney = selectUserMoneyByUserId(userId);
//                    if (userMoney == null) {
//                        return Result.error("用户钱包信息不存在！");
//                    } else {
//                        //用户余额
//                        BigDecimal money = userMoney.getMoney();
//                        int i = money.compareTo(orders.getPayMoney());
//                        if (i >= 0) {
//                            //修改我的的余额
//                            baseMapper.updateMayMoney(2, userId, orders.getPayMoney().doubleValue());
//                            //设置订单状态
//                            orders.setState("1");
//                            //设置更新时间
//                            orders.setUpdateTime(simpleDateFormat.format(new Date()));
//                            //更新到订单表中
//                            ordersDao.updateById(orders);
//                            return Result.success("支付成功!");
//
//                        } else {
//                            return Result.error("余额不足!");
//                        }
//                    }
//                }
//            } else {
//                //会员订单
//                if (orders.getState().equals("1")) {
//                    return Result.error("订单进行中！");
//                } else if (orders.getState().equals("2")) {
//                    return Result.error("订单已完成！");
//                } else if (orders.getState().equals("3")) {
//                    return Result.error("订单已退款！");
//                } else {
//                    //订单的状态为待支付
//                    //查看用户账户余额
//                    UserMoney userMoney = selectUserMoneyByUserId(userId);
//                    //用户余额
//                    BigDecimal money = userMoney.getMoney();
//                    int i = money.compareTo(orders.getPayMoney());
//                    if (i >= 0) {
//                        Long vipTypeId = orders.getVipDetailsId();
//                        //查看要开通会员类型
//                        VipDetails vipDetails = vipDetailsDao.selectOne(new QueryWrapper<VipDetails>().eq("id", vipTypeId));
//                        UserEntity userEntity = userService.selectUserById(userId);
//                        UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
//                        MessageInfo messageInfo=new MessageInfo();
//                        userMoneyDetails.setMoney(orders.getPayMoney());
//                        userMoneyDetails.setUserId(orders.getUserId());
//                        if ((vipDetails.getVipNameType().equals(0))) {
//                            //月
//                            userMoneyDetails.setContent("开通月卡会员");
//                            messageInfo.setContent("订单下单成功");
//                        } else if ((vipDetails.getVipNameType()).equals(1)) {
//                            //季
//                            userMoneyDetails.setContent("开通季卡会员");
//                            messageInfo.setContent("订单下单成功");
//                        } else {
//                            //年
//                            userMoneyDetails.setContent("开通年费会员");
//                            messageInfo.setContent("开通年费会员");
//                        }
//                        userMoneyDetails.setTitle("开通会员");
//                        userMoneyDetails.setType(2);
//                        userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
//                        userMoneyDetailsService.save(userMoneyDetails);
//                        messageInfo.setTitle("开通会员");
//                        messageInfo.setState(String.valueOf(4));
//                        messageInfo.setUserName(userEntity.getUserName());
//                        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
//                        messageInfo.setCreateAt(simpleDateFormat.format(new Date()));
//                        messageInfo.setIsSee("0");
//                        messageService.saveBody(messageInfo);
//                        if(StringUtil.isNotBlank(userEntity.getClientid())){
//                            userService.pushToSingle(messageInfo.getTitle(),messageInfo.getContent(),userEntity.getClientid());
//                        }
//                        //修改我的的余额
//                        baseMapper.updateMayMoney(2, userId, orders.getPayMoney().doubleValue());
//                        //设置订单状态
//                        orders.setState("2");
//                        //设置更新时间
//                        orders.setUpdateTime(simpleDateFormat.format(new Date()));
//                        //更新到订单表中
//                        ordersDao.updateById(orders);
//                        //查看会员类型
//
//                        //查看用户是否是会员
//                        UserVip userVip = userVipDao.selectOne(new QueryWrapper<UserVip>().eq("user_id", userId));
//                        //日历
//                        Calendar cal = Calendar.getInstance();
//                        if (userVip != null) {
//                            //是会员
//                            //查看会员到期时间
//                            Date endDate = null;
//                            try {
//                                endDate = simpleDateFormat.parse(userVip.getEndTime());
//                            } catch (Exception e) {
//                                e.getMessage();
//                            }
//                            //查看会员是否到期
//                            if (endDate != null && System.currentTimeMillis() < (endDate.getTime())) {
//                                //没有到期
//                                if (vipDetails != null) {
//                                    //设置会员到期时间到日历
//                                    cal.setTime(endDate);
//                                    //判断会员的续费时间
//                                    if ((vipDetails.getVipNameType().equals(0))) {
//                                        //月
//                                        cal.add(Calendar.MONTH, 1);
//                                    } else if ((vipDetails.getVipNameType()).equals(1)) {
//                                        //季
//                                        cal.add(Calendar.MONTH, 3);
//                                    } else {
//                                        //年
//                                        cal.add(Calendar.YEAR, 1);
//                                    }
//                                    //设置会员的到期时间
//                                    userVip.setEndTime(simpleDateFormat.format(cal.getTime()));
//                                    //更新会员信息
//                                    userVipDao.updateById(userVip);
//                                    //填写邀请码则奖励金币 没有则不奖励
//                                    addMoney(userId, vipDetails);
//                                    return Result.success("开通成功！");
//                                } else {
//                                    return Result.error("会员类型详情为空！");
//                                }
//                                //没有开通过会员 或会员已经到期
//                            } else {
//                                //到期了
//                                //将现在的时间设置到日历中去
//                                cal.setTime(new Date());
//                                //判断会员续费的时间
//                                if ((vipDetails.getVipNameType()).equals(0)) {
//                                    //月
//                                    cal.add(Calendar.MONTH, 1);
//                                } else if ((vipDetails.getVipNameType()).equals(1)) {
//                                    //季
//                                    cal.add(Calendar.MONTH, 3);
//                                } else {
//                                    //年
//                                    cal.add(Calendar.YEAR, 1);
//                                }
//                                //设置会员的到期时间
//                                userVip.setEndTime(simpleDateFormat.format(cal.getTime()));
//                                //更新会员信息
//                                userVipDao.updateById(userVip);
//                                //填写邀请码则奖励金币 没有则不奖励
//                                addMoney(userId, vipDetails);
//                                return Result.success("开通成功！");
//                            }
//
//                        } else {
//                            //不是会员
//                            //创建会员对象
//                            UserVip userVip1 = new UserVip();
//                            //设置会员类型
//                            userVip1.setVipNameType(vipDetails.getVipNameType());
//                            //设置开通会员的用户id
//                            userVip1.setUserId(userId);
//                            //设置会员的购买时间
//                            userVip1.setCreateTime(simpleDateFormat.format(new Date()));
//                            //将现在时间设置到日历中
//                            cal.setTime(new Date());
//                            //判断会员的续费时间
//                            if ((vipDetails.getVipNameType()).equals(0)) {
//                                //月
//                                cal.add(Calendar.MONTH, 1);
//                            } else if ((vipDetails.getVipNameType()).equals(1)) {
//                                //季
//                                cal.add(Calendar.MONTH, 3);
//                            } else {
//                                //年
//                                cal.add(Calendar.YEAR, 1);
//                            }
//                            //设置会员的到期时间
//                            userVip1.setEndTime(simpleDateFormat.format(cal.getTime()));
//                            //设置会员
//                            userVipDao.insert(userVip1);
//                            //填写邀请码则奖励金币 没有则不奖励
//                            addMoney(userId, vipDetails);
//                            return Result.success("开通成功！");
//                        }
//
//                    } else {
//                        return Result.error("余额不足!");
//                    }
//
//
//                }
//            }
//
//        }
//    }

    @Override
    public PageUtils balanceDetailed(Long userId, Page<UserMoneyDetails> pages) {
        return new PageUtils(userMoneyDetailsDao.selectPage(pages,new QueryWrapper<UserMoneyDetails>().eq("user_id",userId).orderByDesc("create_time ")));
    }

    @Override
    public Result profitDetailed(Long userId, IPage ipage) {
        //收益明细
        return null;
    }

    @Override
    public Result selectUserFreezeMoneyByUserId(Long userId) {
        return null;
    }

    @Override
    public Result selectUserAvailableMoneyByUserId(Long userId) {
        return null;
    }

    @Override
    public PageUtils moneyDetailed(Long userId, Page<UserMoneyDetails> pages) {
        return null;
    }

    @Override
    public Result drawCard(Long userId) {
        return null;
    }

    @Override
    public Result drawRecord(Long userId, IPage ipage) {
        return null;
    }


    /**
     * 开通成功会员后  奖励金币
     *
     * @param userId
     * @param
     */
    public void addMoney(Long userId, VipDetails vipDetails) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //是否填写过邀请码
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity.getInviterCode() != null) {
            //查询填写邀请码的所属人
            UserEntity user = userDao.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", userEntity.getInviterCode()));
            if(user!=null){
                //创建钱包详情模板
                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                //设置收益人
                userMoneyDetails.setUserId(user.getUserId());
                //设置购买人
                userMoneyDetails.setByUserId(userId);
                //设置类型
                userMoneyDetails.setClassify(2);
                userMoneyDetails.setType(1);
                //设置title
                userMoneyDetails.setTitle("邀请用户购买会员");
                //设置创建时间
                userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                //填写过邀请码
                userMoneyDetails.setMoney(vipDetails.getAward());
                //设置内容
                userMoneyDetails.setContent("邀请用户购买会员奖励："+vipDetails.getAward()+"金币");
                userMoneyDetailsDao.insert(userMoneyDetails);
                //将奖励的金币添加到受益人的钱包里
                baseMapper.updateMayMoney(1,user.getUserId(),vipDetails.getAward().doubleValue());

                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("邀请用户购买会员奖励："+vipDetails.getAward()+"金币！");
                messageInfo.setTitle("邀请用户购买会员");
                messageInfo.setState(String.valueOf(5));
                messageInfo.setUserName(user.getUserName());
                messageInfo.setUserId(String.valueOf(user.getUserId()));
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                messageInfo.setCreateAt(sdf.format(new Date()));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
                if(StringUtils.isNotEmpty(user.getClientid())){
                    userService.pushToSingle("邀请用户购买会员","邀请用户购买会员奖励："+vipDetails.getAward()+"金币",user.getClientid());
                }
                log.info("奖励成功！");
            }

        } else {
            log.error("没有填写过邀请码 无法奖励金币");
        }
    }
}
