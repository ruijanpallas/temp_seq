package com.tempseq.dao;

public class Measurement {

	private String locationId;
	private String time;
	private int temperature;
	
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getTemperature() {
		return temperature;
	}	
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
}
