package com.zhichen.parking.fragment.homepage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;


import com.google.gson.Gson;
import com.zhichen.parking.fragment.BaseFragment;
import com.zhichen.parking.lib.pay.PayResultListener;
import com.zhichen.parking.lib.pay.yilian.YinlianPayTask;
import com.zhichen.parking.lib.pay.zhifubao.AliPayController;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.YinlianPay;
import com.zhichen.parking.model.ZhifubaoPay;
import com.zhichen.parking.servercontoler.PayControler;
import com.zhichen.parking.servercontoler.ServerManger;
import com.zhichen.parking.wxapi.WXPayEntryActivity;

/**
 * Created by xuemei on 2016-07-19.
 */
public class BaseRechargeFragment extends BaseFragment implements PayResultListener {


    public BaseRechargeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     * 支付宝支付
     */
    public void payZhifubao(int money) {
        ZhifubaoTask task = new ZhifubaoTask();
        task.execute(money);
    }
    private class ZhifubaoTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... params) {
            ZhifubaoPay zhifubaoPay = PayControler.getZhifubaoPay(getActivity(), params[0]);
            if (zhifubaoPay == null) {
                return PayResultListener.PAY_CODE_NOORDER;
            }
            String pay = AliPayController.pay(getActivity(), zhifubaoPay.getOrder_string());
            if (pay != null && AliPayController.checkSuccess(pay)) {
                return PayResultListener.PAY_CODE_SUCCESS;
            }
            return PayResultListener.PAY_CODE_FAILED;
        }

        @Override
        protected void onPostExecute(Integer code) {
            onPayResult(code, null);
        }
    }
    /**
     * 微信支付
     */
    public void payWeixin(int money) {
        Intent intent = new Intent(getActivity(), WXPayEntryActivity.class);
        intent.putExtra(WXPayEntryActivity.KEY_AMOUNT, money);
        startActivity(intent);
    }
    /**
     * 银联支付
     */
    private YinlianPayTask mYinlianPay;
    public void payYinlian(int money) {
        if (mYinlianPay == null) {
            mYinlianPay = new YinlianPayTask(BaseRechargeFragment.this);
        }
        if (!mYinlianPay.checkInstalled(getActivity())) {
            mYinlianPay.installPlugin(getActivity());
        } else {
            final ServerManger.AsyncResponseHandler responseHandler = new ServerManger.AsyncResponseHandler() {
                @Override
                public void onSuccess(int statusCode, String response) {
                    Log.d("cwf","YinlianPay  pay response==="+response);
                    if (response != null) {
                        YinlianPay pay = new Gson().fromJson(response, YinlianPay.class);
                        if (mYinlianPay == null) {
                            mYinlianPay = new YinlianPayTask(BaseRechargeFragment.this);
                        }
                        mYinlianPay.pay(getActivity(), pay.getTn());
                    } else {
                        onPayResult(PayResultListener.PAY_CODE_NOORDER, null);
                    }
                }
                @Override
                public void onFailure(int statusCode, String errorResponse, Throwable e) {
                    onPayResult(PayResultListener.PAY_CODE_NOORDER, null);
                }
            };
            PayControler.getYinlianPay(getActivity(), money, responseHandler);
        }
    }
    public void onPayResult(int code, String messsage) {
        Log.d("cwf","YinlianPay  pay code==="+code);
        Log.d("cwf","YinlianPay  pay messsage==="+messsage);
        String hint = null;
        switch (code) {
            case PayResultListener.PAY_CODE_SUCCESS:
                UserManager.instance().updateUser(getActivity());
                hint = "支付成功";
                break;
            case PayResultListener.PAY_CODE_FAILED:
                hint = "支付失败";
                break;
            case PayResultListener.PAY_CODE_CANCEL:
                hint = "取消已支付";
                break;
            case PayResultListener.PAY_CODE_NOORDER:
                hint = "生成订单失败，请重试";
                break;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("支付结果通知");
        builder.setMessage(hint);
        builder.setInverseBackgroundForced(true);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
