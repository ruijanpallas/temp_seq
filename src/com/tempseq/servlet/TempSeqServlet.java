package com.tempseq.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * Servlet implementation class TempSeqServlet
 */
@WebServlet("/status")
public class TempSeqServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TempSeqServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		Cluster cluster = Cluster.builder().addContactPoints("localhost").build();
		
		Session session = cluster.connect();

		String queryString = "SELECT location_id, datetime, temperature FROM temp_seq.measurements";
		ResultSet result = session.execute(queryString);
		
		PrintWriter out = response.getWriter();
		out.println("<html><head></head><body>");
		out.println("Temperature sequence");
		out.println("<table>");

		for (Row row : result)
		{
		   out.println("<tr>");
		   out.println("<td>" + row.getString("location_id") + "</td>");
		   out.println("<td>" + row.getDate("datetime") + "</td>");
		   out.println("<td>" + row.getDouble("temperature") + "</td>");   
		   out.println("</tr>");
		}
		out.println("</table>");
		out.println("</body></html>");
	}

}
