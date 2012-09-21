package xv.voltron.webapp.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xv.voltron.core.data.Operator;

@WebServlet(name="TestC", urlPatterns="/TestC/*")
public class TestC extends javax.servlet.http.HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*Admin admin = new Admin();
		admin.setAccount("test");
		admin.setPassword("test");
		admin.setName("中文測試123");
		admin.setId(BigDecimal.valueOf(7));
		*/
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("Admin.Id", "7");
		data.put("Admin.Account", "Test111");
		data.put("Admin.Password", "AABBCC");
		data.put("Admin.Name", "哇哈哈");
		
		try {
			Operator<Admin> op = new Operator<Admin>(Admin.class);
			op.update(data);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resp.getWriter().println("id = " + data.get("Admin.Id") + " and name = " + data.get("Admin.Name"));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

	

	
}
