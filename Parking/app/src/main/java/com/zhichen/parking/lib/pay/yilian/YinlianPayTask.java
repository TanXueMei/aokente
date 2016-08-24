package com.zhichen.parking.lib.pay.yilian;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.unionpay.UPPayAssistEx;
import com.zhichen.parking.lib.pay.PayResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class YinlianPayTask {

	/*****************************************************************
	 * mMode参数解释： "00" - 启动银联正式环境 ;"01" - 连接银联测试环境
	 *****************************************************************/
	private final String mMode = "00";

	PayResultListener mOnPayResultListener;

	public YinlianPayTask(PayResultListener listener) {
		mOnPayResultListener = listener;
	}

	/*************************************************
	 * 步骤1：从网络开始,获取交易流水号即TN 步骤2：通过银联工具类启动支付插件 步骤3：处理银联手机支付控件返回的支付结果
	 ************************************************/
	public void pay(final Activity activity, String tn) 
	{
		Log.d("cwf","YinlianPay  传入订单号==="+tn);
		UPPayAssistEx.startPay(activity, null, null, tn, mMode);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("cwf","YinlianPay  onActivityResult requestCode==="+requestCode);
		Log.d("cwf","YinlianPay  onActivityResult resultCode==="+resultCode);
		if (data == null) {
			return;
		}

		int code = PayResultListener.PAY_CODE_INVALID;
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			// 支付成功后，extra中如果存在result_data，取出校验
			// result_data结构见c）result_data参数说明
			if (data.hasExtra("result_data")) {
				String result = data.getExtras().getString("result_data");
				try {
					JSONObject resultJson = new JSONObject(result);
					String sign = resultJson.getString("sign");
					String dataOrg = resultJson.getString("data");
					// 验签证书同后台验签证书
					// 此处的verify，商户需送去商户后台做验签
					boolean ret = RSAUtil.verify(dataOrg, sign, mMode);
					if (ret) {
						// 验证通过后，显示支付结果
						code = PayResultListener.PAY_CODE_SUCCESS;
					} else {
						// 验证不通过后的处理
						// 建议通过商户后台查询支付结果
						code = PayResultListener.PAY_CODE_FAILED;
					}
				} catch (JSONException e) {
				}
			} else {
				// 未收到签名信息
				// 建议通过商户后台查询支付结果
				code = PayResultListener.PAY_CODE_SUCCESS;
			}
		} 
		else if (str.equalsIgnoreCase("fail")) {
			code = PayResultListener.PAY_CODE_FAILED;
		}
		else if (str.equalsIgnoreCase("cancel")) {
			code = PayResultListener.PAY_CODE_CANCEL;
		}
		if(code != PayResultListener.PAY_CODE_INVALID){
			mOnPayResultListener.onPayResult(code, null);
		}
	}

	public boolean checkInstalled(Context context)
	{
		return UPPayAssistEx.checkInstalled(context) ;
	}
	
	public void installPlugin(Context context)
	{
		UPPayAssistEx.installUPPayPlugin(context);
	} 
}
