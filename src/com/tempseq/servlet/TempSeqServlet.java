package com.tempseq.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tempseq.dao.TempSeqDao;
import com.tempseq.dao.GetsSets;

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

		if (request.getParameter("loc_id") != null)
	    {	
			TempSeqDao tsd = new TempSeqDao(request.getParameter("loc_id"), request.getParameter("date_val"));
			Iterator<GetsSets> igs = tsd.getResultIterator();
		
			if(igs.hasNext() == true)
	    	{
				out.println("<hr/>");
				out.println("<table cellpadding=\"4\">");
				out.println("<tr><td><b>Location ID</b></td><td><b>Date and Time</b></td><td><b>Temperature</b></td></tr>");
			
				while (igs.hasNext()) {
					GetsSets location = igs.next();

					out.println("<tr>");
					out.println("<td>" + location.getLocationId() + "</td>");
					out.println("<td>" + location.getTime() + "</td>");
					out.println("<td>" + location.getTemperature() + "</td>");
					out.println("</tr>");
				}

				out.println("</table>");
				out.println("<div id=\"map_canvas\" style=\"width:500px; height:500px\"></div>");
	    	}
			else
			{
				out.println("<hr/>");
				out.println("<p>&nbsp;</p>");
				out.println("Sorry, no results for vehicle id " + request.getParameter("loc_id") + " for " + request.getParameter("date_val"));
			}	
	    }
	}
}