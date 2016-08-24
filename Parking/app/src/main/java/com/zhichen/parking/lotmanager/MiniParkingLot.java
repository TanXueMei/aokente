package com.zhichen.parking.lotmanager;

import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.model.LatLng;

public class MiniParkingLot implements ClusterItem{

	private int type;
	private LatLng loclatLng;
	private long parkingLotId;
	
	public MiniParkingLot(int type, LatLng latLng, long parkingLotId)
	{
		this.type = type ;
		this.loclatLng = latLng;
		this.parkingLotId = parkingLotId ;
	}
	
	public MiniParkingLot(int type, double latitude, double longitude, long parkingLotId)
	{
		this.type = type ;
		this.loclatLng = new LatLng(latitude, longitude);
		this.parkingLotId = parkingLotId ;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setPosition(LatLng loclatLng) {
		this.loclatLng = loclatLng;
	}

	public long getParkingLotId() {
		return parkingLotId;
	}

	public void setParkingLotId(long parkingLotId) {
		this.parkingLotId = parkingLotId;
	}

	@Override
	public LatLng getPosition() {
		return loclatLng;
	}

}
