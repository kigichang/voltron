package xv.voltron.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xv.voltron.constant.Const;

public class Action extends ActionServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void processAction(Method method,
			HttpServletRequest req, 
			HttpServletResponse resp,
			Object[] params) throws ServletException, IOException {
		
		beforeAction(req, resp);
		try {
			method.invoke(this, params);
		} catch (IllegalAccessException 
				| IllegalArgumentException
				| InvocationTargetException e) {
			
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			if (Config.debug()) {
				throw new ServletException(e);
			}
			return;
		}

		if (HttpServletResponse.SC_OK == resp.getStatus() 
				&& (Boolean)req.getAttribute(Const.AUTO_RENDER)) {

			beforeRender(req, resp);
			req.getRequestDispatcher(templateUri(req)).include(req, resp);
		}
}

	

}
