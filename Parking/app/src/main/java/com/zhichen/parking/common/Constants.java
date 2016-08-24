package com.zhichen.parking.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by xuemei on 2016-05-26.
 */
public class Constants {
    /**
     * 用户名1
     */
    public static final String USERNAME = "tanxuemei";
    /**
     * app应用名
     */
    public final static String APPNAME = "SmartParking";
    /**
     * 临时目录
     */
    public final static String PATH_TEMP = Environment.getExternalStorageDirectory() + File.separator + "SmartParking";
    /**
     * Logcat日志目录
     */
    public final static String PATH_LOGCAT = PATH_TEMP + File.separator + "logcat";
    /**
     * 正式IP
     */
//    public static String BASE_PARK_URL = "http://120.25.60.20:8080/v0.1";
//    public static String BASE_URL = "http://120.25.60.20:8080/v0.1/";

    /**
     * 测试IP
     */
    public static String BASE_PARK_URL = "http://192.168.1.116:8080/v0.1";
    public static String BASE_URL = "http://192.168.1.116:8080/v0.1/";

    /**
     * 登入
     * POST
     */
    public static String LOGIN = BASE_URL + "account/login/";
    /**
     * 登出
     * POST
     */
    public static String LOGOUT = BASE_URL + "account/logout/";
    /**
     * 获取用户信息
     * GET
     */
    public static String USER_INFO = BASE_URL + "user/";
    /**
     * 获取账单 要拼接 车牌号
     * GET
     */
    public static String GET_BILLING = BASE_URL + "billing/check/?plate_number=";
    /**
     * 付款 要拼接参数transaction_id，即获取到的账单中的out_trade_no
     * GET
     */
    public static String PAY_BILLING = BASE_URL + "billing/pay/?out_trade_no=";
    /**
     * 获取验证码 拼接用户手机号
     * GET
     */
    public static String GET_CODE = BASE_URL + "account/verify/?phone_number=";
    /**
     * 注册
     * POST
     */
    public static String REGISTER = BASE_URL + "account/register/";
    /**
     * 密码设置/重置
     * POST
     */
    public static String RESET_PSW = BASE_URL + "account/reset_password/";
    /**
     * 修改密码
     * PUT
     */
    public static String CHANGE_PSW = BASE_URL + "account/update_password/";
    /**
     * 支付密码设置/重置
     * POST  PUT
     */
    public static String PAY_PSW = BASE_URL + "account/reset_payment_password/";
    /**
     * 支付密码修改
     * PUT
     */
    public static String CHANGE_PAY__PSW = BASE_URL + "account/update_payment_password/";
    /**
     * 用户上传头像
     * POST
     */
    public static String PUT_AVATAR = BASE_URL + "user/avatar/upload/";
    /**
     * 下载用户头像 user/avatar/?filename=avatar.jpg
     * GET
     */
    public static String LOAD_AVATAR = BASE_URL + "user/avatar/?filename";
    /**
     * 停车场信息
     */
    public static String PARKING_INFO = BASE_URL + "parking/parking_lots/";

    /**
     * 车牌号 增删改
     */
    public static String PLATE_NUMBER= BASE_URL + "user/plate_number/";

    /**
     * 车辆进场
     * GET
     */
    public static String VEHICLE_IN= BASE_URL + "user/vehicle_in/?plate_number=";
    /**
     * 车辆车场
     * GET
     */
    public static String VEHICLE_OUT= BASE_URL + "user/vehicle_out/?plate_number=";
    /**
     * 建议反馈
     * POST
     */
    public static String FEEK_BACK= BASE_URL + "user/comment/";
    /**
     * 版本信息
     * GET
     */
    public static String VERSION_INFO= BASE_URL + "version/";
    /**
     * 下载新版本
     * GET
     */
    public static String VERSION_LOAD= BASE_URL + "version/download/";
    /**
     * 微信  生成支付单 拼接amount参数
     * GET
     */
    public static String PAY_WEIXIN_GETORDER= BASE_URL + "billing/prepay/get_order/wxpay/?amount=";
    /**
     * 微信  后台查询实际支付结果 GET
     */
    public static String PAY_WEIXIN_ORDERQUERY= BASE_URL + "billing/prepay/order_query/wxpay/";
    /**
     * 微信  后台接收微信支付异步通知 GET
     */
    public static String PAY_WEIXIN_NOTIFY= BASE_URL + "billing/prepay/notify/wxpay/";
    /**
     * 支付宝  生成支付单 拼接amount参数
     */
    public static String PAY_ALIPAY_GETORDER= BASE_URL + "billing/prepay/get_order/alipay/?amount=";
    /**
     * 支付宝  后台查询实际支付结果 GET
     */
    public static String PAY_ALIPAY_ORDERQUERY= BASE_URL + "billing/alipay/pay/";
    /**
     * 银联  生成支付单 拼接参数value：订单 总额
     */
    public static String PAY_UNIPAY_NOTIFY= BASE_URL + "billing/prepay/get_order/unionpay/?value=";
    /**
     * 银联  后台查询实际支付结果 GET
     */
    public static String PAY_UNIPAY_ORDERQUERY= BASE_URL + "billing/unipay/pay/";

}
