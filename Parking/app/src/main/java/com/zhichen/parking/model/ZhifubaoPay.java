package com.zhichen.parking.model;

import java.io.Serializable;

public class ZhifubaoPay implements Serializable {

	private String kind;
	private String order_string;

	public String getOrder_string() {
		return order_string;
	}

	public void setOrder_string(String order_string) {
		this.order_string = order_string;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

}
