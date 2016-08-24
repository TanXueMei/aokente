package com.zhichen.parking.lib.pay;

public interface PayResultListener {
	
	/**
	 * 支付结果 
	 */
	public static final int PAY_CODE_INVALID = -1;
	public static final int PAY_CODE_SUCCESS = 0;
	public static final int PAY_CODE_FAILED = 1;
	public static final int PAY_CODE_CANCEL = 2;
	public static final int PAY_CODE_NOORDER = 3;
	
	/**
	 * 回调接口 
	 */
	public abstract void onPayResult(int code, String messsage);
}
