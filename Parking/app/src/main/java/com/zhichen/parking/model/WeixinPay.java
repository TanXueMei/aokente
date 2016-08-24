package com.zhichen.parking.model;

import java.io.Serializable;

public class WeixinPay implements Serializable {
	
	private static final long serialVersionUID = 3605715254426180280L;
	
	private String appid;
	private String partnerid;
	private String prepayid;
	private String noncestr;
	private String timestamp;
	private String sign;
	private String packageValue = "Sign=WXPay";

	public String getAppId() {
		return appid;
	}

	public void setAppId(String appId) {
		this.appid = appId;
	}

	public String getPartnerId() {
		return partnerid;
	}

	public void setPartnerId(String partnerId) {
		this.partnerid = partnerId;
	}

	public String getPrepayId() {
		return prepayid;
	}

	public void setPrepayId(String prepayId) {
		this.prepayid = prepayId;
	}

	public String getNonceStr() {
		return noncestr;
	}

	public void setNonceStr(String nonceStr) {
		this.noncestr = nonceStr;
	}

	public String getTimeStamp() {
		return timestamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timestamp = timeStamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPackageValue() {
		return packageValue;
	}

	public void setPackageValue(String packageValue) {
		this.packageValue = packageValue;
	}

}
