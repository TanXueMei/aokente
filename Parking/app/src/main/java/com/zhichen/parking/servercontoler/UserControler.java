package com.zhichen.parking.servercontoler;

import android.content.Context;
import android.util.Log;

import com.zhichen.parking.servercontoler.ServerManger.AsyncResponseHandler;
import com.google.gson.Gson;
import com.litesuits.http.LiteHttp;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.FileRequest;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.content.FileBody;
import com.litesuits.http.request.content.JsonBody;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.zhichen.parking.common.Constants;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.User;
import com.zhichen.parking.tools.MyLogger;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by xuemei on 2016-06-21.
 * 用户处理部分  请求
 */
public class UserControler {

    /**
     * 登录
     */
    public static String login(final Context context, final String username, final String password) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(username, password);
        String result = liteHttp.perform(new StringRequest(Constants.LOGIN).setHeaders(authHeader).setMethod(HttpMethods.Post));
        logger.d("login   response===");

        return result;
    }
    public static void login(final Context context, final String username, final String password,AsyncResponseHandler asyncResponseHandler) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(username, password);
        StringRequest requests = new StringRequest(Constants.LOGIN);
        requests.setHeaders(authHeader);
        requests.setMethod(HttpMethods.Post);
        requests.setHttpListener(asyncResponseHandler);
        liteHttp.executeAsync(requests);

    }
    /**
     * 登出
     */
    public static String logout(final Context context, final String username, final String password) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(username, password);
        String result = liteHttp.perform(new StringRequest(Constants.LOGOUT).setHeaders(authHeader).setMethod(HttpMethods.Post));
        logger.d("logout   response===");
        return result;
    }

    /**
     * 读取用户信息
     */
    public static User getUserInfo(Context context, final String userName, final String password) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        String result = liteHttp.perform(new StringRequest(Constants.USER_INFO).setHeaders(authHeader).setMethod(HttpMethods.Get));
        logger.d("getUserInfo -- result=" + result);
        User user = null;
        if (result != null) {
            user = new Gson().fromJson(result, User.class);
        }
        return user;
    }

    public static void getUserInfo(Context context, final String userName, final String password, AsyncResponseHandler asyncResponseHandler) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        StringRequest setMethod = new StringRequest(Constants.USER_INFO).setHeaders(authHeader).setMethod(HttpMethods.Get);
        setMethod.setHttpListener(asyncResponseHandler);
        liteHttp.performAsync(setMethod);
    }

    /**
     * 获取验证码
     */
    public static String getAuthCode(String phone) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        final String url = Constants.GET_CODE + phone;
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        String result = liteHttp.perform(new StringRequest(url).setMethod(HttpMethods.Get));
        logger.d("getAuthCode -- result=" + result);
        return result;
    }

    /**
     * 注册
     */
    public static void register(String phone, String carNum, String authCode, final AsyncResponseHandler responseHandler) {
        RequestParams params = new RequestParams(Constants.REGISTER);
        params.addQueryStringParameter("phone_number", phone);
        params.addQueryStringParameter("plate_number", carNum);
        params.addQueryStringParameter("verification_code", authCode);
        // 只包含字符串参数时默认使用BodyParamsEntity，
        // 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。
        params.addBodyParameter("phone_number", phone);
        params.addBodyParameter("plate_number", carNum);
        params.addBodyParameter("verification_code", authCode);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                responseHandler.onFailure(400, arg0.toString(), arg0);
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String arg0) {
                Log.d("cwf", "register -- onSuccess=" + arg0);
                responseHandler.onSuccess(200, arg0);
            }
        });
    }

    /**
     * 登录密码设置/重置
     */
    public static void setAndResetLoginPsw(String phone, String password, String authCode,final AsyncResponseHandler responseHandler) {
        RequestParams params = new RequestParams(Constants.RESET_PSW);
        params.addQueryStringParameter("phone_number", phone);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("verification_code", authCode);
        // 只包含字符串参数时默认使用BodyParamsEntity，
        // 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。
        params.addBodyParameter("phone_number", phone);
        params.addBodyParameter("password", password);
        params.addBodyParameter("verification_code", authCode);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String response) {
                responseHandler.onSuccess(200, response);
            }

            @Override
            public void onError(Throwable arg0, boolean isOnCallback) {
                responseHandler.onFailure(400, arg0.toString(), arg0);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 设置支付密码  / 找回支付密码
     */
    public static void setPayPassword( String payPassword, String authCode, final AsyncResponseHandler responseHandler) {
        UserManager um = UserManager.instance();
        final String userName = um.getUserName();
        final String password = um.getPassWord();
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);

        RequestParams params = new RequestParams(Constants.PAY_PSW);
        params.addHeader("Authorization", authHeader.get("Authorization"));

        params.addQueryStringParameter("password", payPassword);
        params.addQueryStringParameter("verification_code", authCode);
        // 只包含字符串参数时默认使用BodyParamsEntity，
        // 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。
        params.addBodyParameter("password", payPassword);
        params.addBodyParameter("verification_code", authCode);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String response) {
                responseHandler.onSuccess(200, response);
            }
            @Override
            public void onError(Throwable arg0, boolean isOnCallback) {
                responseHandler.onFailure(400, arg0.toString(), arg0);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 修改密码  登录密码
     */
    public static void modifyLoginPassword(String oldPassword, String newPassword, AsyncResponseHandler responseHandler) {
        modifyPassword(Constants.CHANGE_PSW, oldPassword, newPassword, responseHandler);
    }

    /**
     * 修改支付密码
     */
    public static void modifyPayPassword(String oldPassword, String newPassword, AsyncResponseHandler responseHandler) {
        modifyPassword(Constants.CHANGE_PAY__PSW, oldPassword, newPassword, responseHandler);
    }

    /**
     * 修改登录密码和支付密码公共部分
     *
     * @param url
     * @param oldPassword
     * @param newPassword
     * @param responseHandler
     */
    private static void modifyPassword(String url, String oldPassword, String newPassword, final AsyncResponseHandler responseHandler) {
        UserManager um = UserManager.instance();
        final String userName = um.getUserName();
        final String password = um.getPassWord();
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);

        RequestParams params = new RequestParams(url);
        params.addHeader("Authorization", authHeader.get("Authorization"));
        params.addQueryStringParameter("old_password", oldPassword);
        params.addQueryStringParameter("new_password", newPassword);
        params.addBodyParameter("old_password", oldPassword);
        params.addBodyParameter("new_password", newPassword);
        x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {

            @Override
            public void onCancelled(Callback.CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("cwf", "--strResp---" + arg0);
                responseHandler.onFailure(400, arg0.toString(), arg0);
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String arg0) {
                Log.e("cwf", "--strResp---" + arg0);
                responseHandler.onSuccess(200, arg0);
            }
        });
    }

    /**
     * 用户上传头像
     */
    public static void uploadFile(final Context context, String path, final AsyncResponseHandler asyncResponseHandler) {

        UserManager um = UserManager.instance();
        final String userName = um.getUserName();
        final String password = um.getPassWord();
        HttpListener<String> uploadListener = new HttpListener<String>(true, false, true) {
            @Override
            public void onSuccess(String data, Response<String> response) {
                asyncResponseHandler.onSuccess(200, data);
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                asyncResponseHandler.onFailure(400, e.toString(), e.getCause());
            }
        };
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final StringRequest upload = new StringRequest(Constants.PUT_AVATAR);
        upload.setMethod(HttpMethods.Post);
        upload.setHttpListener(uploadListener);
        upload.setHttpBody(new FileBody(new File(path)));
        upload.setHeaders(authHeader);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        liteHttp.executeAsync(upload);
    }

    /**
     * 下载用户头像
     */
    public static void downLoadFile(final Context context, final String savePath, String url, final AsyncResponseHandler asyncResponseHandler) {
        UserManager um = UserManager.instance();
        final String userName = um.getUserName();
        final String password = um.getPassWord();
        HttpListener<File> uploadListener = new HttpListener<File>(true, false, true) {
            @Override
            public void onSuccess(File data, Response<File> response) {
                asyncResponseHandler.onSuccess(200, savePath);
            }
            @Override
            public void onFailure(HttpException e, Response<File> response) {
                asyncResponseHandler.onFailure(400, e.toString(), e.getCause());
            }
        };
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final FileRequest downreq = new FileRequest(url, savePath);
        downreq.setMethod(HttpMethods.Get);
        downreq.setHttpListener(uploadListener);
        downreq.setHeaders(authHeader);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        liteHttp.executeAsync(downreq);
    }

    /**
     * 建议反馈
     */
    public static String submitSuggest(final Context context, String comment) {
        MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        UserManager um = UserManager.instance();
        final String userName = um.getUserName();
        final String password = um.getPassWord();

        HashMap<String, String> args = new HashMap<String, String>();
        args.put("comment", comment);
        String strJson = new Gson().toJson(args);

        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        StringRequest request = new StringRequest(Constants.FEEK_BACK).setHeaders(authHeader).setMethod(HttpMethods.Post);
        request.setHttpBody(new JsonBody(strJson));
        String result = liteHttp.perform(request);
        logger.d("submitSuggest -- result=" + result);
        return result;
    }

    /**
     * 版本更新
     */
    public static void checkUpgrade(final Context context, final AsyncResponseHandler responseHandler) {
        final LiteHttp liteHttp = ServerManger.intance().getLiteHttp();
        StringRequest postRequest = new StringRequest(Constants.VERSION_INFO);
        postRequest.setMethod(HttpMethods.Get);
        postRequest.setHttpListener(responseHandler);
        liteHttp.performAsync(postRequest);
    }

    /**
     * 添加车牌号  xUtils
     */
    public static void addCarNumber(Context context, String carNumber, final AsyncResponseHandler responseHandler) {
        final MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        UserManager um = UserManager.instance();
        final String userName = um.getUserName();
        final String password = um.getPassWord();
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        final RequestParams params = new RequestParams(Constants.PLATE_NUMBER);
        params.addHeader("Authorization", authHeader.get("Authorization"));
        // 只包含字符串参数时默认使用BodyParamsEntity，
        // 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。
        params.addQueryStringParameter("plate_number", carNumber);
        params.addBodyParameter("plate_number", carNumber);
        logger.d("addCarNumber  params" + params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                logger.e("addCarNumber  onError" + arg0.toString());
                responseHandler.onFailure(400, arg0.toString(), arg0);
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String arg0) {
                logger.d("addCarNumber  onSuccess" + arg0.toString());
                responseHandler.onSuccess(200, arg0);
            }
        });
    }

    /**
     * 删除车牌号
     */
    public static void deleteCarNumber(Context context, String carNumber, final AsyncResponseHandler responseHandler) {
        UserManager um = UserManager.instance();
        String userName = um.getUserName();
        String password = um.getPassWord();
        LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);
        String url;
        try {
            url = Constants.PLATE_NUMBER +"?plate_number="+URLEncoder.encode(carNumber, "UTF-8");
            RequestParams params = new RequestParams(url);
            params.addHeader("Authorization", authHeader.get("Authorization"));
            x.http().request(HttpMethod.DELETE, params, new Callback.CommonCallback<String>() {
                MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
                @Override
                public void onCancelled(CancelledException arg0) {
                }
                @Override
                public void onError(Throwable arg0, boolean arg1) {
                    logger.e("deleteCarNumber  onError" + arg0.toString());
                    responseHandler.onFailure(400, arg0.toString(), arg0);
                }

                @Override
                public void onFinished() {
                    Log.e("cwf", "--strResp---onFinished");
                }

                @Override
                public void onSuccess(String arg0) {
                    logger.d("deleteCarNumber  onSuccess" + arg0.toString());
                    responseHandler.onSuccess(200, arg0);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改车牌号
     */
    public static void modifyCarNumber(String oldNumber, String newNumber, final AsyncResponseHandler responseHandler) {
        final MyLogger logger = MyLogger.getLogger(Constants.USERNAME);
        UserManager um = UserManager.instance();
        final String userName = um.getUserName();
        final String password = um.getPassWord();
        final LinkedHashMap<String, String> authHeader = ServerManger.intance().createAuthHeader(userName, password);

        RequestParams params = new RequestParams(Constants.PLATE_NUMBER);
        params.addHeader("Authorization", authHeader.get("Authorization"));
        params.addQueryStringParameter("old_plate_number", oldNumber);
        params.addQueryStringParameter("new_plate_number", newNumber);
        // 只包含字符串参数时默认使用BodyParamsEntity，
        // 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。
        params.addBodyParameter("old_plate_number", oldNumber);
        params.addBodyParameter("new_plate_number", newNumber);
        x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                logger.e("modifyCarNumber  onError" + arg0.toString());
                responseHandler.onFailure(400, arg0.toString(), arg0);
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String arg0) {
                logger.d("modifyCarNumber  onSuccess" + arg0.toString());
                responseHandler.onSuccess(200, arg0);
            }
        });
    }

    // 将输入流转换成字符串
    public static String inStream2String(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        return new String(baos.toByteArray());
    }

}
