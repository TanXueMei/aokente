package com.zhichen.parking.model;

import java.util.Date;
import java.util.List;

public class ParkingLotList {
	private String kind;
	private List<ParkingLot> parking_lots;
	private Date update_time;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public List<ParkingLot> getParking_lots() {
		return parking_lots;
	}

	public void setParking_lots(List<ParkingLot> parking_lots) {
		this.parking_lots = parking_lots;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
}
