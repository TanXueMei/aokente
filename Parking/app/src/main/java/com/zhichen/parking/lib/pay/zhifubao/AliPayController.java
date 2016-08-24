package com.zhichen.parking.lib.pay.zhifubao;

import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

public class AliPayController {

	/**
	 * 必须异步调用
	 */
	public static String pay(Activity activity, String payInfo) {
		// 构造PayTask 对象
		PayTask alipay = new PayTask(activity);
		// 调用支付接口，获取支付结果
		String result = alipay.pay(payInfo, true);
		return result;
	}

	/**
	 * 返回结果需要通过resultStatus以及result字段的值来综合判断并确定支付结果。在resultStatus=9000，并且success
	 * =“true”以及sign=“xxx”校验通过的情况下，证明支付成功，其它情况归为失败。较低安全级别的场合，
	 * 也可以只通过检查resultStatus以及success=“true”来判定支付结果。
	 */
	public static boolean checkSuccess(String strResult)
	{
		PayResult payResult = new PayResult(strResult);
		/**
		 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
		 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
		 * docType=1) 建议商户依赖异步通知
		 */
		String resultInfo = payResult.getResult();// 同步返回需要验证的信息

		String resultStatus = payResult.getResultStatus();
		// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
		if (TextUtils.equals(resultStatus, "9000")) {
			return true ;
		}
		else {
			// 判断resultStatus 为非"9000"则代表可能支付失败
			// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
			if (TextUtils.equals(resultStatus, "8000")) {
				//Toast.makeText(PayDemoActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

			} else {
				// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
				//Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
			}
			return false ;
		}
	}
	
}
