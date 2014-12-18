package com.tempseq.dao;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import com.cedarsoftware.util.io.JsonWriter;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class AveragesQry {

	private Session session = null;
	private ResultSet result = null;
	private LinkedList <Average> resultList;
	
	public AveragesQry(String locationId, String fromDate, String toDate) {
		getData(locationId, fromDate, toDate);
	}

	protected void getData(String locationId, String fromDate, String toDate) {
		session = CassandraAccess.getInstance();

		String queryString = "SELECT location_id, date, average, samples FROM temp_seq.daily_averages WHERE location_id = '" + 
		  locationId + "' AND date >= '" + fromDate + "' AND date <= '" + toDate + "'";
		result = session.execute(queryString);
		resultList = new LinkedList<Average>();
		
		for (Row row : result) {
			Average location = new Average();
			location.setLocationId(row.getString("location_id"));
			location.setDate(row.getString("date"));
			location.setAverage(row.getDouble("average"));
			location.setSamples(row.getInt("samples"));
			
			resultList.add(location);
		}
	}

	public Iterator <Average> getResultIterator() {
		return resultList.iterator();
	}	

	public String toJson() {
		
		String json = "";
		if (result != null)
		{
			try {
				json = JsonWriter.objectToJson(resultList);
			} catch (IOException ex) {
				System.out.println (ex.toString());
			}
		}
		return json;
	}	
}
