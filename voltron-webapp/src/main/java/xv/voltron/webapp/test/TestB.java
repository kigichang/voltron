package xv.voltron.webapp.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xv.voltron.core.DataManager;

@WebServlet(name="TestB", urlPatterns="/TestB/*")
public class TestB extends HttpServlet {

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
			out.println("<br />In B connection : " + conn.hashCode());
			
			Connection conn2 = (Connection)req.getAttribute("conn");
			out.println("<br /> equals : " + conn.equals(conn2));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Long id = (Long)req.getAttribute("id");
		Integer code = (Integer)req.getAttribute("code");
		
		out.println("<br />Test A Thread ID : " + id);
		
		out.println("<br />Test A req : " + code);
		
		out.println("<br />Test B Thread ID : " + Thread.currentThread().getId());
		
		out.println("<br />Test B req : " + req.hashCode());
		
		DataManager.closePersistent();
	}

}
