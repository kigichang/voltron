package xv.voltron.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import xv.voltron.annotation.Dispatch;
import xv.voltron.constant.ArgumentPolicy;
import xv.voltron.constant.Const;
import xv.voltron.constant.RequestScope;

public class Action extends HttpServlet {

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
	
	protected List<String>split(String str, char seperator) {
		List<String> ret = new ArrayList<String>();
		int len = 0, i = 0, start = 0;
		if (str != null && (len = str.length()) > 0) {
			while(i < len) {
				if (str.charAt(i) == seperator) {
					if (start > 0) {
						ret.add(str.substring(start, i));
					}
					start = ++i;
					continue;
				}
				i++;
			}
			if (start < len) {
				ret.add(str.substring(start, len));
			}
		}
		
		return ret;
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
	
	
	
	
	
	protected Object convert(String val, Type type) 
			throws NumberFormatException {
		
		if (val == null) {
			return null;
		}

		if (type.equals(String.class)) {
			return val;
		}
		else if(type.equals(Integer.class)) {
			return Integer.valueOf(val);
		}
		else if (type.equals(Long.class)) {
			return Long.valueOf(val);
		}
		else if (type.equals(BigDecimal.class)) {
			return new BigDecimal(val);
		}
		else if (type.equals(Double.class)) {
			return Double.valueOf(val);
		}
		else if (type.equals(Float.class)) {
			return Float.valueOf(val);
		}
		else if (type.equals(Character.class)) {
			return Character.valueOf(val.charAt(0));
		}
		else if (type.equals(Byte.class)) {
			return Byte.valueOf(val);
		}
		else if (type.equals(Boolean.class)) {
			if ("true".equalsIgnoreCase(val) ||
					"yes".equalsIgnoreCase(val) ||
					"on".equalsIgnoreCase(val)) {
				return Boolean.valueOf(true);
			}
			else if ("false".equals(val) ||
					"no".equalsIgnoreCase(val) ||
					"off".equalsIgnoreCase(val)) {
				return Boolean.valueOf(false);
			}
			throw new NumberFormatException(
					String.format("For Input String \"%s\"", val));
		}
		throw new NumberFormatException(
				String.format("Not Support Type \"%s\"", type.toString()));
	}
	
	protected void process(RequestScope scope, 
						   HttpServletRequest req, 
						   HttpServletResponse resp) 
								   throws ServletException, IOException {
		
		/* initialize default variables */
		req.setAttribute(Const.REQUEST_TIME, System.currentTimeMillis());
		//req.setAttribute(Const.CACHE, Cache.NONE);
		req.setAttribute(Const.AUTO_RENDER, true);
		//resp.setCharacterEncoding(Config.encoding());
		//resp.setContentType("text/html");
		
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
			params = split(path_info, '/');
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
						convert(params.get(i), invoke_method.types[i + 1]),
				i++);
		}
		catch(NumberFormatException e) {
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
				List<String> extra_value = split(params.get(i), ':');
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
	
	protected void processAction(Method method, 
								 HttpServletRequest req, 
								 HttpServletResponse resp, 
								 Object[] params) 
										 throws ServletException, IOException {
		
		beforeAction(req, resp);
		try {
			method.invoke(this, params);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			if (Config.debug()) {
				throw new ServletException(e);
			}
			return;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			if (Config.debug()) {
				throw new ServletException(e);
			}
			return;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
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
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(RequestScope.GET, req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(RequestScope.POST, req, resp);
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
		// TODO Auto-generated method stub
	}
	
	/*public void enableLifetimeCache(HttpServletRequest req, 
									String cacheId, 
									long lifetime) {
		
		req.setAttribute(Const.CACHE, Cache.LIFETIME);
		req.setAttribute(Const.CACHE_ID, cacheId);
		req.setAttribute(Const.CACHE_LIFETIME, lifetime);
	}
	
	public void enableScheduleCache(HttpServletRequest req, 
									String cacheId, 
									String schedule) {
		
		req.setAttribute(Const.CACHE, Cache.SCHEDULE);
		req.setAttribute(Const.CACHE_ID, cacheId);
		req.setAttribute(Const.CACHE_SCHEDULE, schedule);
	}
	
	public void resetCache(HttpServletRequest req) {
		req.setAttribute(Const.CACHE, Cache.NONE);
		req.removeAttribute(Const.CACHE_ID);
		req.removeAttribute(Const.CACHE_LIFETIME);
		req.removeAttribute(Const.CACHE_SCHEDULE);
	}*/
	
	
	
	/*public void showCache(HttpServletRequest req) {
		req.setAttribute(Const.USE_CACHE, Boolean.TRUE);
	}*/
	
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
