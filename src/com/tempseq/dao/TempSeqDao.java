package com.tempseq.dao;

import java.util.Iterator;
import java.util.LinkedList;

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

	protected void insertData(String locationId, String timeVal, String temperature) {
		
		session = CassandraAccess.getInstance();

		String queryString = "INSERT INTO temp_seq.measurements(location_id, time, temperature) VALUES('" + locationId + "', '" + timeVal + "', " + temperature + " )";
		result = session.execute(queryString);
	}
}
