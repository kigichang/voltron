package xv.voltron.webapp.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xv.voltron.annotation.Dispatch;
import xv.voltron.constant.ArgumentPolicy;
import xv.voltron.constant.Const;
import xv.voltron.core.Action;
import xv.voltron.core.Config;
import xv.voltron.core.DataManager;

@WebServlet(name="Test", urlPatterns="/test1/*")
public class Test extends Action {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Dispatch
	public void List(HttpServletRequest req, HttpServletResponse resp, Integer id) 
			throws ServletException, IOException {
		req.setAttribute(Const.AUTO_RENDER, false);
		//PrintWriter out = as(resp, "text/html").encoding(resp, "UTF-8").writer(resp);
		/*out.println("encode：" + 
				Config.encoding() + 
				" and Debug = " + 
				Config.debug());*/
	}
	
	@Dispatch(policy=ArgumentPolicy.DEFAULT_NULL, strictLength=1)
	public void Edit(HttpServletRequest req, HttpServletResponse resp, Integer id, String val, Long a) 
			throws ServletException ,IOException {
		req.setAttribute("id", id);
		req.setAttribute("val", val);
		req.setAttribute("a", a);
		
		try {
			Connection conn = DataManager.getPersistent();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from admin where id = 1");
			if (rs.next()) {
				req.setAttribute("name", rs.getString("name"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Dispatch(policy=ArgumentPolicy.DEFAULT_NULL, strictLength=1)
	public void EditList(HttpServletRequest req, HttpServletResponse resp, Integer id, String val, Long a) 
			throws ServletException ,IOException {	
	}
	
	@Dispatch
	public void Index(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException ,IOException {	
	}
	
}
