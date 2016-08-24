package com.zhichen.parking.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

	private static final long serialVersionUID = -8372705705689242960L;

	private String kind = "customer#base_info";// 固定值 customer#base_info
	private String avatar;// 客户头像下载路径
	private String phone_number;// 客户手机号码
	private String nick_name;// 昵称
	private String email;// 邮箱
	private float account_balance;// 客户账户余额
	
	private boolean setPayPassword ;

	private List<Vehicle> vehicles;//客户车辆
	private List<BankCard> bank_cards;//银行卡

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getImg() {
		return avatar;
	}

	public void setImg(String img) {
		this.avatar = img;
	}

	public String getPhoneNumber() {
		return phone_number;
	}

	public void setPhoneNumber(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getNickName() {
		return nick_name;
	}

	public void setNickName(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isSetPayPassword() {
		return setPayPassword;
	}

	/**
	 * 返回值单位为元 
	 */
	public float getAccountBalance() {
		return ((int) account_balance)/100f;
	}

	public void setAccountBalance(float account_balance) {
		this.account_balance = account_balance;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}
	
	public void addVehicle(Vehicle vehicle)
	{
		if(vehicles == null){
			vehicles = new ArrayList<Vehicle>();
		}
		vehicles.add(vehicle);
	}

	public List<BankCard> getBankCard() {
		return bank_cards;
	}

	public void setBankCard(List<BankCard> bank_card) {
		this.bank_cards = bank_card;
	}

	@Override
	public String toString() {
		return "User{" +
				"kind='" + kind + '\'' +
				", avatar='" + avatar + '\'' +
				", phone_number='" + phone_number + '\'' +
				", nick_name='" + nick_name + '\'' +
				", email='" + email + '\'' +
				", account_balance=" + account_balance +
				", setPayPassword=" + setPayPassword +
				", vehicles=" + vehicles +
				", bank_cards=" + bank_cards +
				'}';
	}

	/**
	 * Vehicle:车辆
	 * 用户车辆信息
	 */
	public static class Vehicle {
		private String plate_number;// 车牌号
		private boolean locked;// 锁定状态
		private String parking_lot;// 停车场名称
		private String parking_lot_id;// 停车场ID
		private String parking_space;// 停车位ID
		private String parking_time;// 停车时间

		public String getPlate_number() {
			return plate_number;
		}

		public void setPlate_number(String plate_number) {
			this.plate_number = plate_number;
		}

		public boolean isLocking() {
			return locked;
		}

		public void setLocking(boolean locking) {
			this.locked = locking;
		}

		public String getParkingLot() {
			return parking_lot;
		}

		public void setParking_lot(String parking_lot) {
			this.parking_lot = parking_lot;
		}

		public String getParking_space() {
			return parking_space;
		}

		public void setParking_space(String parking_space) {
			this.parking_space = parking_space;
		}

		public String getParking_time() {
			return parking_time;
		}

		public void setParking_time(String parking_time) {
			this.parking_time = parking_time;
		}

		public String getParking_lot_id() {
			return parking_lot_id;
		}

		public void setParking_lot_id(String parking_lot_id) {
			this.parking_lot_id = parking_lot_id;
		}
	}

	/**
	 * 用户银行卡信息
	 */
	public static class BankCard {
		private String number;//银行卡卡号
		private boolean binding;//银行卡绑定状态

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public boolean isBinding() {
			return binding;
		}

		public void setBinding(boolean binding) {
			this.binding = binding;
		}
	}

}
