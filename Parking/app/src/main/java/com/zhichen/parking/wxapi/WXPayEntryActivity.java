package com.zhichen.parking.wxapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhichen.parking.R;
import com.zhichen.parking.common.AppConstants;
import com.zhichen.parking.manager.UserManager;
import com.zhichen.parking.model.WeixinPay;
import com.zhichen.parking.servercontoler.PayControler;
import com.zhichen.parking.tools.DialogHelper;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = WXPayEntryActivity.class.getSimpleName();
	
	public static final String KEY_AMOUNT = "amount";
	
	public static final int PAY_SUCCESS = 0;
	public static final int PAY_ERROR = -1;
	public static final int PAY_CANCEL = -2;
	
	private IWXAPI wxapi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);

		wxapi = WXAPIFactory.createWXAPI(this, AppConstants.APP_ID);
		wxapi.registerApp(AppConstants.APP_ID); 
		wxapi.handleIntent(getIntent(), this);

		int amount = getIntent().getIntExtra(KEY_AMOUNT, 0);
		if(amount > 0){
			PayTask task = new PayTask(amount);
			task.execute();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		wxapi.handleIntent(intent, this);
	}
	
	@Override
	public void onReq(BaseReq req) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "openid = " + req.openId, Toast.LENGTH_SHORT).show();
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			Toast.makeText(this, "COMMAND_GETMESSAGE_FROM_WX", Toast.LENGTH_SHORT).show();	
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			Toast.makeText(this, "COMMAND_SHOWMESSAGE_FROM_WX", Toast.LENGTH_SHORT).show();
			break;
		case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
			Toast.makeText(this, "COMMAND_LAUNCH_BY_WX", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.e(TAG, "onPayFinish, err= " +resp.toString()+"errCode==="+resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode == PAY_SUCCESS){
				Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show() ;
			}
			else{
				Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show() ;
			}
			finish();
		}
	}
	
	private class PayTask extends AsyncTask<Void, Integer, String> {
		ProgressDialog pd ;
		int amount ;
		WeixinPay weixinPay ;
		public PayTask(int amount){
			this.amount = amount ;
		}
		
		@Override
		protected void onPreExecute() {
			pd = DialogHelper.showProgressDialog(WXPayEntryActivity.this, PayTask.this, "正努力连接中，请稍等");
		}
		
		@Override
		protected String doInBackground(Void... params) {
			
			UserManager um = UserManager.instance() ;
			Log.d("cwf","amount==="+amount);
			weixinPay = PayControler.getWeixinPay(WXPayEntryActivity.this, um.getUserName(), um.getPassWord(), amount);
			if(weixinPay == null){
				return "生成订单失败，请再次尝试！";
			}
			Log.d("cwf","weixinPay==="+weixinPay.toString());
			PayReq request = new PayReq();
			request.appId = weixinPay.getAppId();
			request.partnerId = weixinPay.getPartnerId();
			request.prepayId= weixinPay.getPrepayId();
			request.packageValue = weixinPay.getPackageValue();
			request.nonceStr= weixinPay.getNonceStr();
			request.timeStamp= weixinPay.getTimeStamp();
			request.sign= weixinPay.getSign();
//			返回true表示已经接通，可以启动微信支付界面
			boolean sendReq = wxapi.sendReq(request);
			Log.d("cwf","weixinPay==sendReq==="+sendReq);
			return sendReq ? null : "调用微信支付失败！";
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(pd.isShowing()){
				pd.dismiss();
			}
			Log.d("cwf","weixinPay==result==="+result);
			if(result != null){
				Toast.makeText(WXPayEntryActivity.this, result, Toast.LENGTH_SHORT).show() ;
			}
			WXPayEntryActivity.this.finish();
		}
	}
}
