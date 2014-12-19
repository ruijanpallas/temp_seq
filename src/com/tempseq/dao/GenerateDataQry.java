package com.tempseq.dao;

import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public class GenerateDataQry {

	private Session session = null;
	
	public GenerateDataQry(String locationId, String count) {

		session = CassandraAccess.getInstance();

		String qry = "INSERT INTO temp_seq.measurements (location_id, time, temperature) VALUES ('" + locationId + "',?,?)";
		PreparedStatement stmt = session.prepare(qry);
		BoundStatement boundStmt = new BoundStatement(stmt);
		
		String updateQry = "UPDATE temp_seq.daily_averages SET sum = sum + ?, samples = samples + 1" +
				" WHERE location_id = '" + locationId +"' AND date = ?";
		PreparedStatement updateStmt = session.prepare(updateQry);
		BoundStatement boundUpdateStmt = new BoundStatement(updateStmt);

		int times = 10;
		if (null != count)
			times = Integer.parseInt(count);
		
		int i = 0;
		Random randomGenerator = new Random();
		MutableDateTime dateTime = new MutableDateTime(1081157732);
		
		DateTimeFormatter dtf = ISODateTimeFormat.dateTime();;
				
		while (i < times)
		{
			int randomTemp = randomGenerator.nextInt(30);
			session.execute(boundStmt.bind(dateTime.toDate(),randomTemp));

			session.execute(boundUpdateStmt.bind((long)randomTemp, dtf.print(dateTime).substring(0,10)));
			
			dateTime.addSeconds(5);
			i++;
		}
	}
}
