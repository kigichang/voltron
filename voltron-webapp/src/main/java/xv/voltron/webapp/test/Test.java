package xv.voltron.webapp.test;

import java.io.IOException;
import java.io.PrintWriter;

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
		PrintWriter out = resp.getWriter();
		try {
			InitialContext ctx = new InitialContext();
			NamingEnumeration<NameClassPair> e = ctx.list("Data");
			while(e.hasMoreElements()) {
				out.println(e.nextElement().getName());
			}
		} catch (NamingException e) {
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
