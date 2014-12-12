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
		
		Cluster cluster = Cluster.builder().addContactPoint("localhost").build();
		
		Session session = cluster.connect();
		
		String locationId = request.getParameter("loc_id");
		String dateVal = request.getParameter("date_val");
		String queryString = "SELECT location_id, date, time, temperature FROM temp_seq.measurements WHERE location_id = '" + locationId + "' AND date = '" + dateVal + "'";
		ResultSet result = session.execute(queryString);
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"><html>");
		out.println("<head><title>Temperature Sequence</title></head>");
		out.println("<body>");
		out.println("<h1>Temperature Sequence</h1>");
		out.println("Enter the date and id of the location you want to track");	
		out.println("<p>&nbsp;</p>");
		out.println("<form id=\"form1\" name=\"form1\" method=\"get\" action=\"\">");
		out.println("<table>");
		out.println("<tr><td>Date (e.g. 2014-12-12):</td>");
		out.println("<td><input type=\"text\" name=\"date_val\" id=\"date_val\"/></td></tr>");
		out.println("<tr><td>Location id (e.g. 1):</td>");
		out.println("<td><input type=\"text\" name=\"loc_id\" id=\"loc_id\" /></td></tr>");
		out.println("<tr><td></td><td><input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Submit\"/></td></tr>");
		out.println("</table>");
		out.println("</form>");
		out.println("<p>&nbsp;</p>");
		
		if(request.getParameter("loc_id") == null)
		{
			// blank
		}
		else if(result.isExhausted())
		{
			out.println("<hr/>");
			out.println("<p>&nbsp;</p>");
			out.println("Sorry, no results for location id " + request.getParameter("loc_id") + " for " + request.getParameter("date_val"));
		}		
		else
		{
			out.println("<hr/>");
			out.println("<table cellpadding=\"4\">");
			out.println("<tr><td><b>Location id</b></td><td><b>Date</b></td><td><b>Time</b></td><td><b>Temperature</b></td></tr>");
			for (Row row : result)
			{
			   out.println("<tr>");
			   out.println("<td>" + row.getString("location_id") + "</td>");
			   out.println("<td>" + row.getString("date") + "</td>");
			   out.println("<td>" + row.getString("time") + "</td>");
			   out.println("<td>" + row.getDouble("temperature") + "</td>");   
			   out.println("</tr>");
			}
			out.println("</table>");			
		}

		out.println("</body></html>");
	}

}
