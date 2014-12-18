package com.tempseq.dao;

public class Average {

	private String locationId;
	private String date;
	private Double average;
	private int samples;
	
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Double getAverage() {
		return average;
	}	
	public void setAverage(Double average) {
		this.average = average;
	}

	public int getSamples() {
		return samples;
	}	
	public void setSamples(int samples) {
		this.samples = samples;
	}
}
