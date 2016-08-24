package com.zhichen.parking.lib.pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import com.zhichen.parking.lib.pay.yilian.YinlianPayTask;
import com.zhichen.parking.lib.pay.zhifubao.AliPayController;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.YinlianPay;
import com.zhichen.parking.model.ZhifubaoPay;
import com.zhichen.parking.servercontoler.PayControler;
import com.zhichen.parking.servercontoler.ServerManger.AsyncResponseHandler;
import com.zhichen.parking.wxapi.WXPayEntryActivity;


public class BasePayAct extends Activity implements PayResultListener {

    private YinlianPayTask mYinlianPay;

    public void payWeixin(int money) {
        Intent intent = new Intent(this, WXPayEntryActivity.class);
        intent.putExtra(WXPayEntryActivity.KEY_AMOUNT, money);
        startActivity(intent);
    }

    public void payZhifubao(int money) {
        ZhifubaoTask task = new ZhifubaoTask();
        task.execute(money);
    }

    public void payYinlian(int money) {
        Log.d("cwf","YinlianPay  pay ");
        if (mYinlianPay == null) {
            mYinlianPay = new YinlianPayTask(BasePayAct.this);
        }
        if (!mYinlianPay.checkInstalled(BasePayAct.this)) {
            mYinlianPay.installPlugin(BasePayAct.this);
        } else {
            final AsyncResponseHandler responseHandler = new AsyncResponseHandler() {
                @Override
                public void onSuccess(int statusCode, String response) {
                    Log.d("cwf","YinlianPay  pay response==="+response);
                    if (response != null) {
                        YinlianPay pay = new Gson().fromJson(response, YinlianPay.class);
                        if (mYinlianPay == null) {
                            mYinlianPay = new YinlianPayTask(BasePayAct.this);
                        }
                        mYinlianPay.pay(BasePayAct.this, pay.getTn());
                    } else {
                        onPayResult(PayResultListener.PAY_CODE_NOORDER, null);
                    }
                }
                @Override
                public void onFailure(int statusCode, String errorResponse, Throwable e) {
                    onPayResult(PayResultListener.PAY_CODE_NOORDER, null);
                }
            };
            PayControler.getYinlianPay(this, money, responseHandler);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mYinlianPay != null) {
            mYinlianPay.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class ZhifubaoTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... params) {
            ZhifubaoPay zhifubaoPay = PayControler.getZhifubaoPay(BasePayAct.this, params[0]);
            if (zhifubaoPay == null) {
                return PayResultListener.PAY_CODE_NOORDER;
            }
            String pay = AliPayController.pay(BasePayAct.this, zhifubaoPay.getOrder_string());
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



    @Override
    public void onPayResult(int code, String messsage) {
        Log.d("cwf","YinlianPay  pay code==="+code);
        Log.d("cwf","YinlianPay  pay messsage==="+messsage);
        String hint = null;
        switch (code) {
            case PayResultListener.PAY_CODE_SUCCESS:
                UserManager.instance().updateUser(getApplicationContext());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(hint);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
