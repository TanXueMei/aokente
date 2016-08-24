package com.zhichen.parking.servercontoler;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.litesuits.http.LiteHttp;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.Billing;
import com.zhichen.parking.model.PayRecord;
import com.zhichen.parking.model.RechargeRecord;
import com.zhichen.parking.model.WeixinPay;
import com.zhichen.parking.model.YinlianPay;
import com.zhichen.parking.model.ZhifubaoPay;
import com.zhichen.parking.tools.MyLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by xuemei on 2016-06-23.
 * 支付部分
 */
public class PayControler {
    /**
     * 获取对应车牌号账单
     *
     * @param context
     * @param userName
     * @param password
     * @param carNumber
     * @return
     */
    public static Billing getBilling(final Context context, final String userName, final String password, String carNumber) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        final String url = Constants.GET_BILLING + carNumber;
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        String result = liteHttp.perform(new StringRequest(url).setHeaders(authHeader).setMethod(HttpMethods.Get));
        AbstractRequest<String> stringAbstractRequest = new StringRequest(url).setHeaders(authHeader).setMethod(HttpMethods.Get);
        logger.d("getBilling -- stringAbstractRequest=" + stringAbstractRequest);
        logger.d("getBilling -- result=" + result);
        Billing billing = null;
        if (result != null) {
            billing = new Gson().fromJson(result, Billing.class);
        }
        return billing;
    }

    public static String getBillingResult(final Context context, final String userName, final String password, String carNumber) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        final String url = Constants.GET_BILLING + carNumber;
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        String result = liteHttp.perform(new StringRequest(url).setHeaders(authHeader).setMethod(HttpMethods.Get));
        AbstractRequest<String> stringAbstractRequest = new StringRequest(url).setHeaders(authHeader).setMethod(HttpMethods.Get);
        logger.d("getBillingResult -- stringAbstractRequest=" + stringAbstractRequest);
        logger.d("getBillingResult -- result=" + result);
        return result;
    }

    /**
     * 账单支付
     */
    public static String pay(Context context, final String userName, final String password, String transactionId) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        logger.d("pay -- transactionId=" + transactionId);
        final String url = Constants.PAY_BILLING + transactionId;
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        String result = liteHttp.perform(new StringRequest(url).setHeaders(authHeader).setMethod(HttpMethods.Get));
        logger.d("pay -- stringAbstractRequest=" + new StringRequest(url).setHeaders(authHeader).setMethod(HttpMethods.Get));
        logger.d("pay -- result=" + result);
        return result;
    }

    /**
     * 微信支付--生成支付单
     */
    public static WeixinPay getWeixinPay(Context context, final String userName, final String password, int amount) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        final String url = Constants.PAY_WEIXIN_GETORDER + amount;
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        String result = liteHttp.perform(new StringRequest(url).setHeaders(authHeader).setMethod(HttpMethods.Get));
        logger.d("getWeixinPayOrder -- result=" + result);
        WeixinPay pay = null;
        if (result != null) {
            pay = new Gson().fromJson(result, WeixinPay.class);
        }
        return pay;
    }

    /**
     * 支付宝支付--生成支付单
     */
    public static ZhifubaoPay getZhifubaoPay(Context context, float amount) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        UserManager um = UserManager.instance();
        final String userName = um.getUserName();
        final String password = um.getPassWord();
        final String url = Constants.PAY_ALIPAY_GETORDER + amount;
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        String result = liteHttp.perform(new StringRequest(url).setHeaders(authHeader).setMethod(HttpMethods.Get));
        logger.d("getZhifubaoPay -- result=" + result);
        ZhifubaoPay pay = null;
        if (result != null) {
            pay = new Gson().fromJson(result, ZhifubaoPay.class);
        }
        return pay;
    }

    /**
     * 银联支付---后台查询支付单
     */
    public static void getYinlianPay(Context context, float amount, final ServerManger.AsyncResponseHandler responseHandler) {
        UserManager um = UserManager.instance();
        final String userName = um.getUserName();
        final String password = um.getPassWord();
        final String url = Constants.PAY_UNIPAY_ORDERQUERY + amount;//还有点问题
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        StringRequest request = new StringRequest(url).setHeaders(authHeader).setMethod(HttpMethods.Get);
        request.setHttpListener(new HttpListener<String>() {
            @Override
            public void onSuccess(String data, Response<String> response) {
                responseHandler.onSuccess(200, data);
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                responseHandler.onFailure(400, e.toString(), e.getCause());
            }
        });
        liteHttp.performAsync(request);
    }

    /**
     * 银联支付---生成支付单 是value？还是amount
     */
    public static YinlianPay getYinlianPay(Context context, final String userName, final String password, float amount) {
        final String url = Constants.PAY_UNIPAY_NOTIFY + amount;
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        String result = liteHttp.perform(new StringRequest(url).setHeaders(authHeader).setMethod(HttpMethods.Get));
        YinlianPay pay = null;
        if (result != null) {
            pay = new Gson().fromJson(result, YinlianPay.class);
        }
        return pay;
    }

    /**
     * 缴费记录
     */
    public static final int MAX_RESULTS = 40;

    public static List<PayRecord> getPayRecord(int startIndex) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        List<PayRecord> payRecordList = null;
        final String url = Constants.BASE_PARK_URL + "/user/bills/?"
                + "start_index=" + startIndex + "&max_results=" + MAX_RESULTS;
        final String userName = UserManager.instance().getUserName();
        final String password = UserManager.instance().getPassWord();
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        String result = liteHttp.perform(new StringRequest(url).setHeaders(authHeader).setMethod(HttpMethods.Get));
        logger.d("getPayRecord -- result=" + result);
        try {
            JSONObject json = new JSONObject(result);
            JSONArray array = json.getJSONArray("bills");
            payRecordList = new Gson().fromJson(array.toString(), new TypeToken<List<PayRecord>>() {
            }.getType());
//            return payRecordList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payRecordList;
    }

    /**
     * 充值记录
     */
    public static List<RechargeRecord> getRechargeRecord(int startIndex) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        List<RechargeRecord> rechargeRecords = null;
        final String url = Constants.BASE_PARK_URL + "/user/prepayments/?"
                + "start_index=" + startIndex + "&max_results=" + MAX_RESULTS;
        final String userName = UserManager.instance().getUserName();
        final String password = UserManager.instance().getPassWord();
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        String result = liteHttp.perform(new StringRequest(url).setHeaders(authHeader).setMethod(HttpMethods.Get));
        logger.d("getRechargeRecord -- result=" + result);
        try {
            JSONObject json = new JSONObject(result);
            JSONArray array = json.getJSONArray("records");
            rechargeRecords = new Gson().fromJson(array.toString(), new TypeToken<List<RechargeRecord>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rechargeRecords;
    }

    public static List<RechargeRecord> getRechargeRecords(int startIndex) {
        final MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        List<RechargeRecord> rechargeRecords = null;
        final String url = Constants.BASE_PARK_URL + "/user/prepayments/?"
                + "start_index=" + startIndex + "&max_results=" + MAX_RESULTS;
        final String userName = UserManager.instance().getUserName();
        final String password = UserManager.instance().getPassWord();
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final RequestParams params = new RequestParams(url);
        params.addHeader("Authorization", authHeader.get("Authorization"));
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                logger.d("getRechargeRecord  onSuccess===" + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                logger.e("getRechargeRecord  onError===" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        return rechargeRecords;
    }
}
