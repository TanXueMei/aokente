package com.zhichen.parking.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 停车记录
 */
public class ParkHistory implements Serializable {
	private static final long serialVersionUID = 6036743295859917761L;

	private Date startTime;
	private Date endTime;
	private String location;
	private float money;
	private int parkType;
	private int payType;
	private boolean isSelected ;
	
	private static final String[] PARK_TYPE_STRING = {"路边", "停车场"};
	private static final String[] PAY_TYPE_STRING = {"微信", "支付宝", "银联"};

	public ParkHistory() {
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public int getParkType() {
		return parkType;
	}
	
	public String getParkTypeString()
	{
		if(parkType >=0 && parkType <= PARK_TYPE_STRING.length){
			return PARK_TYPE_STRING[parkType];
		}
		return "未知停车";
	}

	public void setParkType(int parkType) {
		this.parkType = parkType;
	}

	public int getPayType() {
		return payType;
	}
	
	public String getPayTypeString()
	{
		if(payType >=0 && payType <= PAY_TYPE_STRING.length){
			return PAY_TYPE_STRING[payType];
		}
		return "未知支付";
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
