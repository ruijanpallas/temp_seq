package com.tempseq.dao;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.datastax.driver.core.Session;

public class InsertDataQry {

	private Session session = null;
	
	public InsertDataQry(String locationId, String timeVal, String temperature) {

		insertData(locationId, timeVal, temperature);
	}

	protected void insertData(String locationId, String timeVal, String tempVal) {
		
		session = CassandraAccess.getInstance();

		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-mm-dd hh:mm:ss");
		DateTime dt = DateTime.parse(timeVal, formatter);
		LocalDate ld = new LocalDate(dt);
		String date = ld.toString();
		int temp = Integer.parseInt(tempVal);
		
		String queryString = "UPDATE temp_seq.daily_averages SET sum = sum + " + temp + ", samples = samples + 1" +
			" WHERE location_id = '" + locationId +"' AND date = '" + date + "'";
		session.execute(queryString);

		queryString = "INSERT INTO temp_seq.measurements(location_id, time, temperature) VALUES('" + locationId + "', '" + timeVal + "', " + tempVal + " )";
		session.execute(queryString);
	}
}
