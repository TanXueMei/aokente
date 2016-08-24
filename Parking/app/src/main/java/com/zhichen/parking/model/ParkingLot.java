package com.zhichen.parking.model;

import com.baidu.mapapi.model.LatLng;

public class ParkingLot 
{
	private long id;
	private String name;
	private String address ;
	private long city_code ;
	private String type;
	private double longitude;
	private double latitude;
	private double distance;
	private String price;
	private int parking_space_total;
	private int parking_space_available;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	public LatLng getPosition()
	{
		return new LatLng(this.latitude, this.longitude);
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getParkingSpaces() {
		return parking_space_total;
	}

	public void setParkingSpaces(int parking_spaces) {
		this.parking_space_total = parking_spaces;
	}

	public int getParkingSpacesAvailable() {
		return parking_space_available;
	}

	public void setParkingSpacesAvailable(int parking_spaces_available) {
		this.parking_space_available = parking_spaces_available;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getCityCode() {
		return city_code;
	}

	public void setCityCode(long city_code) {
		this.city_code = city_code;
	}

}
