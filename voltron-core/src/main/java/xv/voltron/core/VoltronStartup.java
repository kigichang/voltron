package xv.voltron.core;

import java.io.File;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.configuration.ConfigurationException;

public class VoltronStartup extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		try {
			Config.getInstance(getServletContext());
			Config.setProperty("app.root", 
					getServletContext().getRealPath("/"));
			
			File file = new File(
					new StringBuffer(Config.get("app.root"))
						.append(File.separatorChar)
						.append("WEB-INF")
						.append(File.separatorChar)
						.append("cache").toString()
					);
			file.mkdirs();
			
			if (!file.exists()) {
				throw new ServletException(
						String.format("Cache Folder [%s]Is Not Exist",
									  file.getAbsoluteFile()));
			}
			
			DataManager.getInstance();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			throw new ServletException(e);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new ServletException(e);
		}
	}
	
	

}
