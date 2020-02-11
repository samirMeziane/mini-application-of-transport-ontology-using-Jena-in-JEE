package com.TransportOws;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TransportServlet
 */
@WebServlet(urlPatterns = "/Transport")
public class TransportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private OwlBase ob;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TransportServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    

	@Override
	public void init() throws ServletException {
		super.init();
		ob=OwlBase.getInstance();
	
	}



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub		
				
		//doGet(request, response);
		
		String TransportType=request.getParameter("trans");
		String depart=request.getParameter("Depart");
		String destination=request.getParameter("Destination");
		String filter=request.getParameter("trie");
		if(filter==null)
			filter="";
		/*System.out.println(filter);
		System.out.println(depart);
		System.out.println(destination);*/
		List<String>Transports=new ArrayList<String>();
		
		Map<String ,Travel> travels=null;
		if(TransportType!=null)
		{
			
			Transports=ob.get_type_transport(TransportType);
			request.setAttribute("Transports_Type", Transports);
			ob.get_ratio_uber();

			
		}else if(depart!=null)
		{
		
			travels=ob.get_possible_route(parse_String(depart), parse_String(destination), filter);
			request.setAttribute("routes", travels);
			
		}
			
		

		request.getRequestDispatcher("/onlyPage.jsp").forward(request, response);

	
	}

	private String parse_String(String s)
	{
		// s.replace(" ", '_');
		
		return s.toLowerCase().replaceAll("\\s+", "_");
		
	
		
		
	}
}
