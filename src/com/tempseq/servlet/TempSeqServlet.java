package com.tempseq.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cedarsoftware.util.io.JsonWriter;
import com.tempseq.dao.InsertDataQry;
import com.tempseq.dao.MeasurementsQry;

/**
 * Servlet implementation class TempSeqServlet
 */
@WebServlet(
		urlPatterns = {"/measurements", "/averages", "/data"}
)
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
	 * 
	 * an example:
	 * $ curl --request GET "http://localhost:8080/temp_seq/data?loc_id=3&from_time=2014-11-13&to_time=2014-12-12"
	 * 
	 * {"@type":"java.util.LinkedList",
	 *   "@items":[
	 *     {"@type":"com.tempseq.dao.GetsSets","locationId":"3","time":"Thu Nov 13 00:00:00 EET 2014","temperature":0.0},
	 *     {"@type":"com.tempseq.dao.GetsSets","locationId":"3","time":"Thu Nov 13 01:30:00 EET 2014","temperature":31.0}
	 *   ]
	 * }
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		if (request.getParameter("loc_id") != null)
	    {
			String json = "";
			if ("/measurements".equals(request.getServletPath()))
			{
				MeasurementsQry tsd = new MeasurementsQry(request.getParameter("loc_id"), request.getParameter("from_time"), request.getParameter("to_time"));
				json = JsonWriter.objectToJson(tsd.getResultList());
			}
			else if ("/averages".equals(request.getServletPath())) {
			}
				
			out.println(json);
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * URI: e.g., http://localhost:8080/temp_seq/data?loc_id=5&time_val=2014-01-01 02:30:25&temp_val=20
	 * i.e.,
	 * $ curl --request POST "http://localhost:8080/temp_seq/data?loc_id=5&time_val=2014-01-01%2002%3A30%3A25&temp_val=20"
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("loc_id") != null)
	    {	
			new InsertDataQry(request.getParameter("loc_id"), request.getParameter("time_val"), request.getParameter("temp_val"));
	    }
	}
}