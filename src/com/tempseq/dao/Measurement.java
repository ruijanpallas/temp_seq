package com.tempseq.dao;

public class Measurement {

	private String locationId;
	private String time;
	private Double temperature;
	
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
	public Double getTemperature() {
		return temperature;
	}	
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
}
