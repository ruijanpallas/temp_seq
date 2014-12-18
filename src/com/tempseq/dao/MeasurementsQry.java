package com.tempseq.dao;

import java.util.Iterator;
import java.util.LinkedList;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.tempseq.dao.CassandraAccess;
import com.tempseq.dao.Measurement;

public class MeasurementsQry {

	private Session session = null;
	private ResultSet result = null;
	private LinkedList <Measurement> resultList;
	
	public MeasurementsQry(String locationId, String fromTime, String toTime) {
		getData(locationId, fromTime, toTime);
	}

	protected void getData(String locationId, String fromTime, String toTime) {
		session = CassandraAccess.getInstance();

		String queryString = "SELECT location_id, time, temperature FROM temp_seq.measurements WHERE location_id = '" + 
		  locationId + "' AND time >= '" + fromTime + "' AND time <= '" + toTime + "'";
		result = session.execute(queryString);
		resultList = new LinkedList<Measurement>();
		
		for (Row row : result) {
			Measurement location = new Measurement();
			location.setLocationId(row.getString("location_id"));
			location.setTime(row.getDate("time").toString());
			location.setTemperature(row.getDouble("temperature"));
			
			resultList.add(location);
		}
	}

	public Iterator <Measurement> getResultIterator() {
		return resultList.iterator();
	}	

	public LinkedList <Measurement> getResultList() {
		return resultList;
	}	
}
