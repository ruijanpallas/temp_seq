package com.tempseq.dao;

import java.util.Iterator;
import java.util.LinkedList;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.tempseq.dao.CassandraAccess;
import com.tempseq.dao.GetsSets;

public class TempSeqDao {

	private Session session = null;
	private ResultSet result = null;
	private LinkedList <GetsSets> resultList;
	
	public TempSeqDao(String locationId, String fromTime, String toTime) {
		getData(locationId, fromTime, toTime);
	}

	protected void getData(String locationId, String fromTime, String toTime) {
		session = CassandraAccess.getInstance();

		String queryString = "SELECT location_id, time, temperature FROM temp_seq.measurements WHERE location_id = '" + 
		  locationId + "' AND time >= '" + fromTime + "' AND time <= '" + toTime + "'";
		result = session.execute(queryString);
		resultList = new LinkedList<GetsSets>();
		
		for (Row row : result) {
			GetsSets location = new GetsSets();
			location.setLocationId(row.getString("location_id"));
			location.setTime(row.getDate("time").toString());
			location.setTemperature(row.getDouble("temperature"));
			
			resultList.add(location);
		}
	}

	public Iterator <GetsSets> getResultIterator() {
		return resultList.iterator();
	}	

	public LinkedList <GetsSets> getResultList() {
		return resultList;
	}	

	public TempSeqDao(String locationId, String timeVal, String temperature, Boolean create) {
		if (create)
			insertData(locationId, timeVal, temperature);
		// else update
	}

	protected void insertData(String locationId, String timeVal, String tempVal) {
		
		// TODO: all CQL commands below must be done as a single transaction
		session = CassandraAccess.getInstance();

		String queryString = "INSERT INTO temp_seq.measurements(location_id, time, temperature) VALUES('" + locationId + "', '" + timeVal + "', " + tempVal + " )";
		session.execute(queryString);

		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-mm-dd hh:mm:ss");
		DateTime dt = DateTime.parse(timeVal, formatter);
		LocalDate ld = new LocalDate(dt);
		String date = ld.toString();
		double temp = Double.parseDouble(tempVal);
		
		queryString = "SELECT location_id, date, average, samples FROM temp_seq.daily_averages WHERE location_id = '" + 
				  locationId + "' AND date = '" + date + "'";
		ResultSet result = session.execute(queryString);

		if (result.iterator().hasNext()) {
			GetAverage average = new GetAverage();
			for (Row row : result) {
				average.setLocationId(row.getString("location_id"));
				average.setDate(row.getString("date"));
				average.setAverage(row.getDouble("average"));
				average.setSamples(row.getInt("samples"));
			}
			int newSamples = average.getSamples() + 1;
			Double newAverage = (average.getAverage() * average.getSamples() + temp) / newSamples;
			queryString = "UPDATE temp_seq.daily_averages SET average = " + newAverage + ", samples = " + newSamples +
					" WHERE location_id = '" + locationId +"' AND date = '" + date + "'";
		} else {
			queryString = "INSERT INTO temp_seq.daily_averages(location_id, date, average, samples) VALUES('" + 
					locationId + "', '" + date + "', " + temp + " , " + 1 + " )";
		}
		session.execute(queryString);
	}
}
