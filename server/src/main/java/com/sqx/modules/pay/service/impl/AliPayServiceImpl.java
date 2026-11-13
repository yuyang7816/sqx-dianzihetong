package com.sqx.modules.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.pay.config.AliPayConstants;
import com.sqx.modules.pay.dao.PayClassifyDao;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayClassify;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.AliPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.sqx.modules.utils.wx.WxPayUtils.getGeneralOrder;

@Service
@Slf4j
public class AliPayServiceImpl implements AliPayService {
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private PayClassifyDao payClassifyDao;
    @Autowired
    private UserDao userDao;


    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Result payMoney(Long userId, BigDecimal money, Long parentId, Integer classify) {
        CommonInfo one = commonInfoService.findOne(19);
        CommonInfo one3 = commonInfoService.findOne(12);
        String name = one3 == null ? "余额充值" : one3.getValue();
        String generalOrder = getGeneralOrder();
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        String returnUrl = one.getValue() + "/sqx_fast/subpackage/my_activity/my_exchange";
        PayDetails payDetails = new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(generalOrder);
        payDetails.setUserId(userId);
        payDetails.setMoney(money);
        payDetails.setClassify(classify);
        payDetails.setType(1);
        payDetails.setParentId(parentId);
        payDetailsDao.insert(payDetails);
        if (classify == 4) {
            return payApp(generalOrder, name, money, url);
        }
        return payH5(generalOrder, name, money, url, returnUrl);
    }


    @Override
    public Result payDocument(Long userId, Integer classify, Long payClassifyId) {
        UserEntity userEntity = userDao.selectById(userId);
        if (userEntity == null) {
            return Result.error("用户不存在，请重新登录");
        }
        PayClassify payClassify = payClassifyDao.selectById(payClassifyId);
        if (payClassify == null) {
            return Result.error("该合同签署套餐不存在");
        }
        CommonInfo one = commonInfoService.findOne(19);
        CommonInfo one3 = commonInfoService.findOne(12);
        String name = one3 == null ? "合同签署套餐" : one3.getValue();
        String generalOrder = getGeneralOrder();
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        // 设置跳转地址
        String returnUrl = one.getValue() + "/sqx_fast/subpackage/my_activity/my_exchange";
        reentrantReadWriteLock.writeLock().lock();
        try {
            PayDetails payDetails = new PayDetails();
            payDetails.setState(0);
            payDetails.setCreateTime(sdf.format(new Date()));
            payDetails.setOrderId(generalOrder);
            payDetails.setUserId(userId);
            payDetails.setMoney(payClassify.getPrice());
            payDetails.setClassify(classify);
            payDetails.setType(3);
            payDetails.setParentId(payClassifyId);
            payDetailsDao.insert(payDetails);
            if (classify == 4) {
                return payApp(generalOrder, name, payClassify.getPrice(), url);
            }
            return payH5(generalOrder, name, payClassify.getPrice(), url, returnUrl);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("支付宝支付订单类型8报错：" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试");
    }


    public Result payApp(String generalOrder, String name, BigDecimal money, String url) {
        try {
            String pay = commonInfoService.findOne(97).getValue();
            if ("1".equals(pay)) {
                //实例化客户端
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
                //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
                AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
                //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
                AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
                model.setBody(name);
                model.setSubject(name);
                model.setOutTradeNo(generalOrder);
                model.setTimeoutExpress("30m");
                model.setTotalAmount(String.valueOf(money));
                model.setProductCode("QUICK_MSECURITY_PAY");
                request.setBizModel(model);
                request.setNotifyUrl(url);
                AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
                if (response.isSuccess()) {
                    return Result.success().put("data", response.getBody());
                } else {
                    return Result.error("获取订单失败！");
                }
            } else {
                //构造client
                CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
                //设置网关地址
                certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
                //设置应用Id
                certAlipayRequest.setAppId(commonInfoService.findOne(63).getValue());
                //设置应用私钥
                certAlipayRequest.setPrivateKey(commonInfoService.findOne(65).getValue());
                //设置请求格式，固定值json
                certAlipayRequest.setFormat("json");
                //设置字符集
                certAlipayRequest.setCharset(AliPayConstants.CHARSET);
                //设置签名类型
                certAlipayRequest.setSignType(AliPayConstants.SIGNTYPE);
                CommonInfo urls = commonInfoService.findOne(200);
                certAlipayRequest.setCertPath(urls.getValue() + "/appCertPublicKey.crt"); //应用公钥证书路径（app_cert_path 文件绝对路径）
                certAlipayRequest.setAlipayPublicCertPath(urls.getValue() + "/alipayCertPublicKey_RSA2.crt"); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
                certAlipayRequest.setRootCertPath(urls.getValue() + "/alipayRootCert.crt");  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
                //构造client
                AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);

                //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
                AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
                //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
                AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
                model.setBody(name);
                model.setSubject(name);
                model.setOutTradeNo(generalOrder);
                model.setTimeoutExpress("30m");
                model.setTotalAmount(money + "");
                model.setProductCode("QUICK_MSECURITY_PAY");
                request.setBizModel(model);
                request.setNotifyUrl(url);
                //这里和普通的接口调用不同，使用的是sdkExecute
                AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
                if (response.isSuccess()) {
                    return Result.success().put("data", response.getBody());
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return Result.error(-100, "获取订单失败！");
    }

    public Result payH5(String generalOrder, String name, BigDecimal money, String url, String returnUrl) {
        String pay = commonInfoService.findOne(97).getValue();
        try {
            if ("1".equals(pay)) {
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                        commonInfoService.findOne(63).getValue(),
                        commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET,
                        commonInfoService.findOne(64).getValue(), "RSA2");
                AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
                JSONObject order = new JSONObject();
                order.put("out_trade_no", generalOrder); //订单号
                order.put("subject", name); //商品标题
                order.put("product_code", "QUICK_WAP_WAY");
                order.put("body", name);//商品名称
                order.put("total_amount", money); //金额
                alipayRequest.setBizContent(order.toString());
                //在公共参数中设置回跳和通知地址
                alipayRequest.setNotifyUrl(url);
                //通知页面地址
                alipayRequest.setReturnUrl(returnUrl);
                String form = alipayClient.pageExecute(alipayRequest).getBody();
                return Result.success().put("data", form);
            } else {
                //构造client
                CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
                //设置网关地址
                certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
                //设置应用Id
                certAlipayRequest.setAppId(commonInfoService.findOne(63).getValue());
                //设置应用私钥
                certAlipayRequest.setPrivateKey(commonInfoService.findOne(65).getValue());
                //设置请求格式，固定值json
                certAlipayRequest.setFormat("json");
                //设置字符集
                certAlipayRequest.setCharset(AliPayConstants.CHARSET);
                //设置签名类型
                certAlipayRequest.setSignType(AliPayConstants.SIGNTYPE);
                CommonInfo urls = commonInfoService.findOne(200);
                certAlipayRequest.setCertPath(urls.getValue() + "/appCertPublicKey.crt"); //应用公钥证书路径（app_cert_path 文件绝对路径）
                certAlipayRequest.setAlipayPublicCertPath(urls.getValue() + "/alipayCertPublicKey_RSA2.crt"); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
                certAlipayRequest.setRootCertPath(urls.getValue() + "/alipayRootCert.crt");  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
                //构造client
                AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
                AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
                JSONObject order = new JSONObject();
                order.put("out_trade_no", generalOrder); //订单号
                order.put("subject", name); //商品标题
                order.put("product_code", "QUICK_WAP_WAY");
                order.put("body", name);//商品名称
                order.put("total_amount", money + ""); //金额
                alipayRequest.setBizContent(order.toString());
                alipayRequest.setNotifyUrl(url); //在公共参数中设置回跳和通知地址
                alipayRequest.setReturnUrl(returnUrl); //线上通知页面地址
                String form = alipayClient.pageExecute(alipayRequest).getBody();
                return Result.success().put("data", form);
            }

        } catch (AlipayApiException e) {
            log.error("CreatPayOrderForH5", e);
        }
        return Result.error("获取订单信息错误！");
    }

    @Override
    public boolean aliRefund(String orderId, BigDecimal refundMoney) {
        PayDetails payDetails = new PayDetails();
        try {
            payDetails = payDetailsDao.selectByOrderId(orderId);
            if (refundMoney == null) {
                refundMoney = payDetails.getMoney();
            }
            String pay = commonInfoService.findOne(97).getValue();
            if ("1".equals(pay)) {
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                        commonInfoService.findOne(63).getValue(),
                        commonInfoService.findOne(65).getValue(),
                        "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
                AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                AlipayTradeRefundModel model = new AlipayTradeRefundModel();
                model.setOutTradeNo(payDetails.getOrderId()); //订单号
                model.setRefundAmount(refundMoney + ""); //退款金额
                model.setOutRequestNo(payDetails.getOrderId()); //退款单号
                request.setBizModel(model);
                AlipayTradeRefundResponse response = alipayClient.execute(request);
                if (response.isSuccess()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                //构造client
                CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
                //设置网关地址
                certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
                //设置应用Id
                certAlipayRequest.setAppId(commonInfoService.findOne(63).getValue());
                //设置应用私钥
                certAlipayRequest.setPrivateKey(commonInfoService.findOne(65).getValue());
                //设置请求格式，固定值json
                certAlipayRequest.setFormat("json");
                //设置字符集
                certAlipayRequest.setCharset(AliPayConstants.CHARSET);
                //设置签名类型
                certAlipayRequest.setSignType(AliPayConstants.SIGNTYPE);
                CommonInfo urls = commonInfoService.findOne(200);
                certAlipayRequest.setCertPath(urls.getValue() + "/appCertPublicKey.crt"); //应用公钥证书路径（app_cert_path 文件绝对路径）
                certAlipayRequest.setAlipayPublicCertPath(urls.getValue() + "/alipayCertPublicKey_RSA2.crt"); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
                certAlipayRequest.setRootCertPath(urls.getValue() + "/alipayRootCert.crt");  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
                //构造client
                AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
                AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                AlipayTradeRefundModel model = new AlipayTradeRefundModel();
                model.setOutTradeNo(payDetails.getOrderId()); //订单号
                model.setRefundAmount(refundMoney + ""); //退款金额
                model.setOutRequestNo(payDetails.getOrderId()); //退款单号
                request.setBizModel(model);
                AlipayTradeRefundResponse response = alipayClient.certificateExecute(request);
                if (response.isSuccess()) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.error("支付宝退款失败！，订单号" + payDetails.getOrderId() + "，失败原因：" + e.getMessage(), e);
        }
        return false;
    }


}
