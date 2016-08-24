package com.zhichen.parking.model;

import java.io.Serializable;

/**
 * 车辆停车计费
 */

public class Billing implements Serializable 
{
	/**
	 * {
	 "kind": "billing#base_info",
	 "transaction_id": 2,//交易流水号
	 "plate_number": "100002",//缴费车辆
	 "parking_card_number": "TEST",//停车卡号
	 "parking_lot": "海岸城",
	 "parking_time": "2015-11-13T12:14:26.742Z",
	 "billing_time": "2015-11-15T15:21:22.342",
	 "charged_duration": 3066,
	 "amount": 1,
	 "price": "Randomly charged."
	 }
	 */
	private static final long serialVersionUID = 4383843730196696375L;

	private String kind = "billing#base_info";
	private String out_trade_no;//交易流水号
	private String type;//交易类型
	private String customer_name;//客户姓名
	private String plate_number;//缴费车辆
	private String parking_card_number;//停车卡号
	private String parking_lot ;
	private int parking_lot_id;//停车场ID
	private int floor;//停车场楼层
	private int parking_space_id;//停车位ID
	private String parking_time ;
	private String billing_time ;
	private int charged_duration;
	private int amount ;
	private String price ;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getPlate_number() {
		return plate_number;
	}

	public void setPlate_number(String plate_number) {
		this.plate_number = plate_number;
	}

	public String getParking_card_number() {
		return parking_card_number;
	}

	public void setParking_card_number(String parking_card_number) {
		this.parking_card_number = parking_card_number;
	}

	public int getParking_lot_id() {
		return parking_lot_id;
	}

	public void setParking_lot_id(int parking_lot_id) {
		this.parking_lot_id = parking_lot_id;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getParking_space_id() {
		return parking_space_id;
	}

	public void setParking_space_id(int parking_space_id) {
		this.parking_space_id = parking_space_id;
	}

	public String getParking_lot() {
		return parking_lot;
	}

	public void setParking_lot(String parking_lot) {
		this.parking_lot = parking_lot;
	}

	public String getParking_time() {
		return parking_time;
	}

	public void setParking_time(String parking_time) {
		this.parking_time = parking_time;
	}

	public String getBilling_time() {
		return billing_time;
	}

	public void setBilling_time(String billing_time) {
		this.billing_time = billing_time;
	}

	public int getCharged_duration() {
		return charged_duration;
	}

	public void setCharged_duration(int charged_duration) {
		this.charged_duration = charged_duration;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
