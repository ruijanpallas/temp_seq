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
	
	public TempSeqDao(String locationId, String dateVal) {
		getData(locationId, dateVal);
	}

	protected void getData(String locationId, String dateVal) {
		session = CassandraAccess.getInstance();

		String queryString = "SELECT location_id, time, temperature FROM temp_seq.measurements WHERE location_id = '" + locationId + "' AND date = '" + dateVal + "'";
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

	public TempSeqDao(String locationId, String dateVal, String timeVal, String temperature, Boolean create) {
		if (create)
			insertData(locationId, dateVal, timeVal, temperature);
		// else update
	}

	protected void insertData(String locationId, String dateVal, String timeVal, String temperature) {
		
		session = CassandraAccess.getInstance();

		String datetimeVal = dateVal + " " + timeVal;
		String queryString = "INSERT INTO temp_seq.measurements(location_id, date, time, temperature) VALUES('" + locationId + "', '" + dateVal + "', '" + datetimeVal + "', " + temperature + " )";
		result = session.execute(queryString);
	}
}
