package com.tempseq.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cedarsoftware.util.io.JsonWriter;
import com.tempseq.dao.TempSeqDao;
import com.tempseq.dao.GetsSets;

/**
 * Servlet implementation class TempSeqServlet
 */
@WebServlet("/data")
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
	 * {"@type":"java.util.LinkedList",
	 *   "@items":[
	 *     {"@type":"com.tempseq.dao.GetsSets","locationId":"2","time":"Thu Jan 01 02:00:00 EET 1970","temperature":0.0},
	 *     {"@type":"com.tempseq.dao.GetsSets","locationId":"2","time":"Wed Nov 12 00:00:00 EET 2014","temperature":20.0}
	 *   ]
	 * }
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		if (request.getParameter("loc_id") != null)
	    {	
			TempSeqDao tsd = new TempSeqDao(request.getParameter("loc_id"), request.getParameter("date_val"));
			String json = JsonWriter.objectToJson(tsd.getResultList());
			out.println(json);
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * URI: i.g., http://localhost:8080/temp_seq/status?loc_id=33&date_val=2014-11-18&time_val=01:01:30&temp_val=27
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("loc_id") != null)
	    {	
			new TempSeqDao(request.getParameter("loc_id"), request.getParameter("date_val"), request.getParameter("time_val"), request.getParameter("temp_val"), true);

			PrintWriter out = response.getWriter();
			out.println(request.getParameter("date_val"));

	    }
	}
}