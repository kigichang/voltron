package xv.voltron.webapp.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xv.voltron.core.DataManager;

@WebServlet(name="TestA", urlPatterns="/TestA/*")
public class TestA extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = resp.getWriter();
		try {
			Connection conn = DataManager.getPersistent();
			out.println("<br />In A connection : " + conn.hashCode());
			req.setAttribute("conn", conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		out.println("<br />Test A Thread ID : " + Thread.currentThread().getId());
		
		out.println("<br />Test A req : " + req.hashCode());
		
		long id = Thread.currentThread().getId();
		int code = req.hashCode();
		req.setAttribute("id", id);
		req.setAttribute("code", code);
		req.getRequestDispatcher("/TestB").include(req, resp);
	}

	
}
