package xv.voltron.webapp.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = DataManager.getPersistent();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from admin where id = " + req.getParameter("Admin.Id"));
			if (rs.next()) {
				rs.getString("name");
			}
			out.println("<br />In A connection : " + conn.hashCode() + " and name = " + rs.getString("name"));
			req.setAttribute("conn", conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (rs != null) { try {
				rs.close() ;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} }
			
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
