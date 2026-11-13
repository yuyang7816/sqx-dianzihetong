package com.sqx.modules.pay.controller.app;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.dao.UserMoneyDetailsDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.message.dao.MessageInfoDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.pay.config.AliPayConstants;
import com.sqx.modules.pay.dao.PayClassifyDao;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayClassify;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.AliPayService;
import com.sqx.modules.utils.SenInfoCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 支付宝支付处理--暂不做同步处理、回调方式使用异步
 */
@Slf4j
@RestController
@Api(value = "用户端-支付宝支付", tags = {"用户端-支付宝支付"})
@RequestMapping("/app/aliPay")
public class AliPayController {

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;
    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;
    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private PayClassifyDao payClassifyDao;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @RequestMapping(value = "/notifyApp", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public void notifyApp(HttpServletRequest request, HttpServletResponse response) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        try {
            log.info("回调成功！！！");
            boolean flag = AlipaySignature.rsaCheckV1(params, commonInfoService.findOne(64).getValue(), AliPayConstants.CHARSET, "RSA2");
            log.info(flag + "回调验证信息");
            if (flag) {
                String tradeStatus = params.get("trade_status");
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {

                    //支付宝返回的订单编号
                    String outTradeNo = params.get("out_trade_no");
                    log.error(outTradeNo);
                    //支付宝支付单号
                    String tradeNo = params.get("trade_no");
                    PayDetails payDetails = payDetailsDao.selectByOrderId(outTradeNo);
                    if (payDetails.getState() == 0) {
                        payDetailsDao.updateState(payDetails.getId(), 1, sdf.format(new Date()), "");
                        // 处理支付后的逻辑
                        payBack(payDetails);
                    }
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("回调验证失败！！！");
        }
    }

    private void payBack(PayDetails payDetails) {
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
//                        moneyDlLetails.setUserId(orders.getUserId());
        userMoneyDetails.setTitle("购买合同签署套餐");
        userMoneyDetails.setContent("支付宝支付:" + payDetails.getMoney() + ",购买合同签署套餐,订单号：" + payDetails.getOrderId());
        userMoneyDetails.setType(2);
        userMoneyDetails.setClassify(5);
        userMoneyDetails.setUserId(payDetails.getUserId());
        userMoneyDetails.setState(2);
        userMoneyDetails.setMoney(payDetails.getMoney());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userMoneyDetails.setCreateTime(sdf.format(new Date()));
        userMoneyDetailsDao.insert(userMoneyDetails);
        UserEntity userEntity = userDao.selectById(payDetails.getUserId());
        if (userEntity != null) {
            PayClassify payClassify = payClassifyDao.selectById(payDetails.getParentId());
            if (userEntity.getFreeTimes() == null){
                userEntity.setFreeTimes(0);
            }
            userEntity.setFreeTimes(userEntity.getFreeTimes() + payClassify.getNumberSign());
            userDao.updateById(userEntity);
            setCommission(payDetails,userEntity);
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
            messageInfo.setTitle("购买合同签署套餐");
            messageInfo.setContent("支付宝支付:" + payDetails.getMoney() + ",购买合同签署套餐,订单号：" + payDetails.getOrderId());
            messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            messageInfo.setUserName(userEntity.getUserName());
            messageInfo.setIsSee("0");
            messageInfo.setState(String.valueOf(5));
            messageInfoDao.insert(messageInfo);
            if (userEntity.getOpenId() != null) {
                List<String> msgList = new ArrayList<>();
                msgList.add("购买合同签署套餐");
                msgList.add("支付宝支付:" + payDetails.getMoney() + ",购买合同签署套餐,订单号：" + payDetails.getOrderId());
                String value = commonInfoService.findOne(240).getValue();
                SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), value, msgList, 3);
            }
            if (userEntity.getClientid() != null) {
                userService.pushToSingle("购买合同签署套餐", "支付宝支付:" + payDetails.getMoney() + ",购买合同签署套餐,订单号：" + payDetails.getOrderId(), userEntity.getClientid());
            }
        }
    }

    private void setCommission(PayDetails payDetails, UserEntity userEntity) {
        if (userEntity.getInviterCode() != null){
            UserEntity userEntity1 = userDao.selectOne(new LambdaQueryWrapper<UserEntity>()
                    .eq(UserEntity::getInvitationCode, userEntity.getInviterCode()));
            if (userEntity1 != null){
                if (userEntity1.getBalance() == null){
                    userEntity1.setBalance(BigDecimal.ZERO);
                }
                BigDecimal rate = payDetails.getMoney().multiply(userEntity.getZhiRate());
                userEntity1.setBalance(userEntity1.getBalance().add(rate));
                userDao.updateById(userEntity1);
                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                userMoneyDetails.setMoney(rate);
                userMoneyDetails.setUserLevel(1);
                userMoneyDetails.setRelationId(userEntity.getUserId());
                userMoneyDetails.setUserId(userEntity1.getUserId());
                userMoneyDetails.setType(1);
                userMoneyDetails.setState(2);
                userMoneyDetails.setClassify(6);
                userMoneyDetails.setTitle("一级用户获得分佣");
                userMoneyDetails.setContent("支付单号:" + payDetails.getOrderId() + "，支付合计:" + payDetails.getMoney() + "，分成金额:" + rate);
                userMoneyDetails.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                userMoneyDetailsDao.insert(userMoneyDetails);
            }
        }
        if (userEntity.getInviterInviterCode() != null){
            UserEntity userEntity2 = userDao.selectOne(new LambdaQueryWrapper<UserEntity>()
                    .eq(UserEntity::getInvitationCode, userEntity.getInviterInviterCode()));
            if (userEntity2 != null){
                if (userEntity2.getBalance() == null) {
                    userEntity2.setBalance(BigDecimal.ZERO);
                }
                BigDecimal feiRate = payDetails.getMoney().multiply(userEntity.getFeiRate());
                userEntity2.setBalance(userEntity2.getBalance().add(feiRate));
                userDao.updateById(userEntity2);
                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                userMoneyDetails.setMoney(feiRate);
                userMoneyDetails.setUserLevel(2);
                userMoneyDetails.setState(2);
                userMoneyDetails.setRelationId(userEntity.getUserId());
                userMoneyDetails.setUserId(userEntity2.getUserId());
                userMoneyDetails.setType(1);
                userMoneyDetails.setClassify(6);
                userMoneyDetails.setTitle("二级用户获得分佣");
                userMoneyDetails.setContent("支付单号:" + payDetails.getOrderId() + "，支付合计:" + payDetails.getMoney() + "，分成金额:" + feiRate);
                userMoneyDetails.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                userMoneyDetailsDao.insert(userMoneyDetails);
            }
        }
    }

    @Login
    @ApiOperation("购买合同签署套餐")
    @RequestMapping(value = "/payApp", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payDocument(@ApiParam("用户id") @RequestAttribute("userId") Long userId,
                              @ApiParam("支付方式 4支付宝APP支付 5支付宝H5支付") Integer classify,
                              @ApiParam("合同签署套餐id") Long payClassifyId) {
        return aliPayService.payDocument(userId, classify, payClassifyId);
    }

//    @Login
//    @ApiOperation("支付宝支付订单")
//    @RequestMapping(value = "/payOrder", method = RequestMethod.POST)
//    @Transactional(rollbackFor = Exception.class)
//    public Result payOrder(Long id) {
//        //通知页面地址
//        CommonInfo one = commonInfoService.findOne(19);
//        String returnUrl = one.getValue() + "/#/pages/task/recharge";
//        CommonInfo one3 = commonInfoService.findOne(12);
//        String name = one3 == null ? "陪玩接单" : one3.getValue();
//        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
//        log.info("回调地址:" + url);
//        PayOrder orders = ordersDao.selectById(id);
//        try {
//            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
//            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
//            JSONObject order = new JSONObject();
//            order.put("out_trade_no", orders.getOrdersNo()); //订单号
//            order.put("subject", name); //商品标题
//            order.put("product_code", "QUICK_WAP_WAY");
//            order.put("body", name);//商品名称
//            BigDecimal money = orders.getPayMoney();
//            order.put("total_amount", money); //金额
//            alipayRequest.setBizContent(order.toString());
//            //在公共参数中设置回跳和通知地址
//            alipayRequest.setNotifyUrl(url);
//            //通知页面地址
//            alipayRequest.setReturnUrl(returnUrl);
//            String form = alipayClient.pageExecute(alipayRequest).getBody();
//            PayDetails payDetails = new PayDetails();
//            payDetails.setState(0);
//            payDetails.setCreateTime(sdf.format(new Date()));
//            payDetails.setOrderId(orders.getOrdersNo());
//            payDetails.setUserId(orders.getUserId());
//            payDetails.setMoney(money);
//            payDetails.setClassify(4);
//            payDetailsDao.insert(payDetails);
//            return Result.success().put("data", form);
//        } catch (AlipayApiException e) {
//            log.error("CreatPayOrderForH5", e);
//        }
//        return Result.error("获取订单信息错误！");
//    }
//
//
//    @ApiOperation("充值")
//    @RequestMapping(value = "/payApp", method = RequestMethod.POST)
//    @Transactional(rollbackFor = Exception.class)
//    public Result payApp(Long id) {
//        CommonInfo one = commonInfoService.findOne(19);
//        CommonInfo one3 = commonInfoService.findOne(12);
//        String name = one3 == null ? "接单" : one3.getValue();
//        PayOrder orders = ordersDao.selectById(id);
//        if (orders.getState().equals(1)) {
//            return Result.error("订单已支付，请不要重复点击！");
//        }
//        String result = "";
//        try {
//            String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
//            //实例化客户端
//            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
//            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
//            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
//            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//            model.setBody(name);
//            model.setSubject(name);
//            model.setOutTradeNo(orders.getOrdersNo());
//            model.setTimeoutExpress("30m");
//            model.setTotalAmount(orders.getPayMoney().toString());
//            model.setProductCode("QUICK_MSECURITY_PAY");
//            request.setBizModel(model);
//            request.setNotifyUrl(url);
//            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
//            if (response.isSuccess()) {
//                result = response.getBody();
//            } else {
//                return Result.error("获取订单失败！");
//            }
//            PayDetails payDetails = new PayDetails();
//            payDetails.setState(0);
//            payDetails.setCreateTime(sdf.format(new Date()));
//            payDetails.setOrderId(orders.getOrdersNo());
//            payDetails.setUserId(orders.getUserId());
//            payDetails.setMoney(orders.getPayMoney());
//            payDetails.setClassify(4);
//            payDetailsDao.insert(payDetails);
//            return Result.success().put("data", result);
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        return Result.error(-100, "获取订单失败！");
//    }

    /*public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }*/


    /**
     * 说明： 支付宝订单退款
     *
     * @return 公共返回参数 code,msg,   响应参数实例: https://docs.open.alipay.com/api_1/alipay.trade.refund
     */
//    public String alipayRefund(Orders orders) {
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
//        AlipayTradeRefundRequest alipay_request = new AlipayTradeRefundRequest();
//        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
//        model.setOutTradeNo(orders.getOrdersNo());//订单编号
//        //model.setTradeNo(orders.getTradeNo());//支付宝订单交易号
//        model.setRefundAmount(orders.getPayMoney().toString());//退款金额 不得大于订单金额
//        model.setRefundReason(orders.getRefundContent());//退款说明
//        model.setOutRequestNo(orders.getOrdersNo());//标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
//        alipay_request.setBizModel(model);
//        try {
//            AlipayTradeRefundResponse alipay_response = alipayClient.execute(alipay_request);
//            String alipayRefundStr = alipay_response.getBody();
//            log.info(alipayRefundStr);
//            return alipayRefundStr;
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


}
