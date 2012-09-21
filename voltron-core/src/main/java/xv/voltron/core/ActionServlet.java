package xv.voltron.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import xv.voltron.annotation.Dispatch;
import xv.voltron.constant.ArgumentPolicy;
import xv.voltron.constant.Cache;
import xv.voltron.constant.Const;
import xv.voltron.constant.RequestScope;
import xv.voltron.util.CoreUtils;

public abstract class ActionServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * GET methods
	 */
	protected HashMap<String, ActionMethod> getMethods = 
			new HashMap<String, ActionMethod>();
	
	/**
	 * POST methods
	 */
	protected HashMap<String, ActionMethod> postMethods = 
			new HashMap<String, ActionMethod>();

	protected String _(String name) {
		return Convention.toUnderlineName(name);
	}
	
	protected String templateUri(HttpServletRequest req) {
		String tmpl = (String)req.getAttribute(Const.TEMPLATE);
		if (tmpl != null) {
			if (tmpl.charAt(0) == '/') {
				return tmpl;
			}
			else if(tmpl.indexOf('/') > 0) {
				return new StringBuffer("/WEB-INF")
						.append(req.getContextPath())
						.append('/')
						.append(tmpl).toString();
			}
			else {
				return new StringBuffer("/WEB-INF")
					.append(req.getContextPath())
					.append(req.getServletPath())
					.append('/')
					.append(tmpl).toString();
			}
		}
		else {
			return new StringBuffer("/WEB-INF")
					.append(req.getContextPath())
					.append(req.getServletPath())
					.append('/')
					.append(req.getAttribute(Const.ACTION_METHOD))
					.append(".jsp").toString();
		}
	}
	
	
	protected void process(RequestScope scope, 
						   HttpServletRequest req, 
						   HttpServletResponse resp) 
								   throws ServletException, IOException {
		
		/* initialize default variables */
		req.setAttribute(Const.REQUEST_TIME, System.currentTimeMillis());
		req.setAttribute(Const.CACHE, Cache.NONE);
		req.setAttribute(Const.AUTO_RENDER, true);
		resp.setCharacterEncoding(Config.encoding());
		resp.setContentType("text/html");
		
		String path_info = req.getPathInfo();
		HashMap<String, ActionMethod> methods = 
				RequestScope.GET == scope ? getMethods : postMethods;
		
		ActionMethod invoke_method = null;
		String method_name = null;
		Object[] invoke_param = null;
		List<String> params = null;
		if (path_info == null || "/".equals(path_info)) {
			/* Servlet default action. 
			 * if no unspecified function, invoke unspecified()
			 * */ 
			invoke_method = methods.get(method_name = "index");
			if (invoke_method == null) {
				beforeAction(req, resp);
				unspecified(req, resp);
				return;
			}
			//params = new String[]{method_name};
			params = new ArrayList<String>();
			params.add(method_name);
		}
		else {
			params = CoreUtils.split(path_info, '/');
			method_name = params.get(0);
			//method_name = method_name.toLowerCase();
			
			invoke_method = methods.get(method_name);
			if (invoke_method == null) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
		
		req.setAttribute(Const.ACTION_METHOD, method_name);
		
		int invoke_param_len = invoke_method.parameterLength;
		int req_param_len = params.size() -1;
		
		if (ArgumentPolicy.STRICT == invoke_method.strict) {
			if (req_param_len != invoke_param_len) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		}
		
		int strict_len = invoke_method.strictLength;
		if (strict_len != 0 && strict_len > req_param_len) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		invoke_param = new Object[invoke_param_len + 2];
		invoke_param[0] = req;
		invoke_param[1] = resp;
		
		int i = 1;
		try {
			for (int len = req_param_len > invoke_param_len ?
							invoke_param_len : req_param_len; 
				i <= len;
				invoke_param[i + 1] = 
						invoke_method.types[i - 1].parseValue(params.get(i)),
				i++);
		}
		catch(ParseException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			if (Config.debug()) { 
				throw new ServletException(e);
			}
			return;
		}
		
		/* Generate new path info */
		StringBuffer new_path_info = new StringBuffer();
		for (; i <= req_param_len; i++) {
			new_path_info.append('/').append(params.get(i));
			if (params.get(i).indexOf(':') > 0) {
				List<String> extra_value = CoreUtils.split(params.get(i), ':');
				req.setAttribute(extra_value.get(0), extra_value.get(1));
			}
		}
		
		if (path_info.charAt(path_info.length() - 1) == '/') {
			new_path_info.append('/');
		}
		
		req.setAttribute(Const.NEW_PATH_INFO, new_path_info.toString());
		
		processAction(invoke_method.invokeMethod, req, resp, invoke_param);

		if (Config.debug()) {
			resp.getWriter().println(
				String.format("\r\n<!-- Cost %d ms -->", 
								System.currentTimeMillis() 
								- (Long)req.getAttribute(Const.REQUEST_TIME))
					);
		}
		
	}
	
	protected abstract void processAction(Method method, 
										  HttpServletRequest req, 
										  HttpServletResponse resp, 
										  Object[] params) 
										throws ServletException, IOException;
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			process(RequestScope.GET, req, resp);
		}
		finally {
			DataManager.freeAllPersisten();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			process(RequestScope.POST, req, resp);
		}
		finally {
			DataManager.freeAllPersisten();
		}
	}
	
	protected String template(HttpServletRequest req) {
		return (String) req.getAttribute(Const.TEMPLATE);
	}
	
	protected void template(HttpServletRequest req, String template) {
		req.setAttribute(Const.TEMPLATE, template);
	}
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		Method[] methods = getClass().getMethods();
		
		for (int i = 0, len = methods.length; i < len; i++) {
			Dispatch ann = methods[i].getAnnotation(Dispatch.class);
			
			if (ann == null) {
				continue;
			}
			
			String name = methods[i].getName();
			ActionMethod action_method = 
				new ActionMethod(methods[i], ann.policy(), ann.strictLength());
			
			if (name.startsWith("show")) {
				getMethods.put(_(name.substring(4)), action_method);
			}
			else if (name.startsWith("do")) {
				postMethods.put(_(name.substring(2)), action_method);
			}
			else {
				name = _(name);
				if (RequestScope.BOTH == ann.scope()) {
					getMethods.put(name, action_method);
					postMethods.put(name, action_method);
				}
				else if (RequestScope.GET == ann.scope()) {
					getMethods.put(name, action_method);
				}
				else if (RequestScope.POST == ann.scope()) {
					postMethods.put(name, action_method);
				}
			}
		}// for
	}
	
	public void unspecified(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
	}
	
	public void beforeAction(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
	}
	
	public void beforeRender(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	
	}
	
	public String retrieve(String uri, 
						   HttpServletRequest req, 
						   HttpServletResponse resp) 
			throws ServletException, IOException {
		
		CacheResponse cache_resp = new CacheResponse(resp);
		req.getRequestDispatcher(uri).include(req, cache_resp);
		
		String content = cache_resp.strWriter.toString();
		cache_resp.free();
		cache_resp = null;
		return content;
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		getMethods.clear();
		getMethods = null;
		
		postMethods.clear();
		postMethods = null;
		super.destroy();
	}
}
