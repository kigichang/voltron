package xv.voltron.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import xv.voltron.annotation.Dispatch;
import xv.voltron.constant.ArgumentPolicy;
import xv.voltron.constant.Cache;
import xv.voltron.constant.Const;
import xv.voltron.constant.RequestScope;

public class Action extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected HashMap<String, ActionMethod> getMethods = 
			new HashMap<String, ActionMethod>();
	
	protected HashMap<String, ActionMethod> postMethods = 
			new HashMap<String, ActionMethod>();

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
				getMethods.put(name.substring(4), action_method);
			}
			else if (name.startsWith("do")) {
				postMethods.put(name.substring(2), action_method);
			}
			else {
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
		
		req.setAttribute(Const.REQUEST_TIME, System.currentTimeMillis());
		req.setAttribute(Const.CACHE, Cache.NONE);
		req.setAttribute(Const.AUTO_RENDER, true);
		resp.setCharacterEncoding(Config.encoding());
		resp.setContentType("text/html");
		
		String path_info = req.getPathInfo();
		
		if (path_info == null || "/".equals(path_info)) {
			req.setAttribute(Const.ACTION_METHOD, "Index");
			
			if (RequestScope.GET == scope) {
				showIndex(req, resp);
			}
			else if (RequestScope.POST == scope) {
				doIndex(req, resp);
			}
			return;
		}
		
		String[] params = StringUtils.split(path_info, '/');
		String method_name = params[0];
		req.setAttribute(Const.ACTION_METHOD, method_name);
		
		HashMap<String, ActionMethod> methods = 
				RequestScope.GET == scope ? getMethods : postMethods;
		
		ActionMethod invoke_method = methods.get(method_name);
		
		if (invoke_method != null) {
			int invoke_param_len = invoke_method.parameterLength;
			int req_param_len = params.length - 1;
			
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
			
			Object[] invoke_param = new Object[invoke_param_len + 2];
			invoke_param[0] = req;
			invoke_param[1] = resp;
			
			try {
				int len = req_param_len > invoke_param_len ?
						invoke_param_len : req_param_len;
				int i = 1;
				for (; i <= len; i++) {
					invoke_param[i + 1] = 
							convert(params[i], invoke_method.types[i + 1]);
				}
				StringBuffer new_path_info = new StringBuffer();
				for (; i <= req_param_len; i++) {
					new_path_info.append('/').append(params[i]);
					if (params[i].indexOf(':') > 0) {
						String[] extra_value = 
								StringUtils.split(params[i], ':');
						req.setAttribute(extra_value[0], extra_value[1]);
					}
				}
				if (path_info.charAt(path_info.length() - 1) == '/') {
					new_path_info.append('/');
				}
				
				req.setAttribute(Const.NEW_PATH_INFO, new_path_info.toString());
			}
			catch(NumberFormatException e) {
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
				if (Config.debug()) { 
					throw new ServletException(e);
				}
				return;
			}
			
			try {
				beforeAction(req, resp);
				if (View.isCached(req)) {
					View.cache(req, resp);
				}
				
				invoke_method.invokeMethod.invoke(this, invoke_param);
				
				if (HttpServletResponse.SC_OK == resp.getStatus() 
						&& (Boolean)req.getAttribute(Const.AUTO_RENDER)) {
					beforeRender(req, resp);
					if (View.needCache(req)) {
						CacheResponse cache_resp = new CacheResponse(resp);
						View.draw(req, cache_resp);
						String content = cache_resp.strWriter.toString();
						resp.getWriter().print(content);
						String cache_file = View.cacheFile(req);
						if (cache_file != null) {
							View.writeCache(cache_file, content);
						}
						cache_resp = null;
					}
					else {
						View.draw(req, resp);
					}
				}
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
			} catch(FileNotFoundException e) {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				if (Config.debug()) {
					throw new ServletException(e);
				}
				return;
			}
			
		}
		else {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		if (Config.debug()) {
			resp.getWriter().println(
				String.format("\r\n<!-- Cost %d ms -->", 
								System.currentTimeMillis() 
								- (Long)req.getAttribute(Const.REQUEST_TIME))
					);
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
	
	protected void showIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub	
	}
	
	protected void doIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	protected void beforeAction(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
	}
	
	protected void beforeRender(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	public Action as(HttpServletResponse resp, String contentType) {
		resp.setContentType(contentType);
		return this;
	}
	
	public Action encoding(HttpServletResponse resp, String encoding) {
		resp.setCharacterEncoding(encoding);
		return this;
	}
	
	public PrintWriter writer(HttpServletResponse resp) throws IOException {
		return resp.getWriter();
	}

	public void enableLifetimeCache(HttpServletRequest req, String cacheId, long lifetime) {
		req.setAttribute(Const.CACHE, Cache.LIFETIME);
		req.setAttribute(Const.CACHE_ID, cacheId);
		req.setAttribute(Const.CACHE_LIFETIME, lifetime);
	}
	
	public void enableScheduleCache(HttpServletRequest req, String cacheId, String schedule) {
		req.setAttribute(Const.CACHE, Cache.SCHEDULE);
		req.setAttribute(Const.CACHE_ID, cacheId);
		req.setAttribute(Const.CACHE_SCHEDULE, schedule);
	}
	
	public void resetCache(HttpServletRequest req) {
		req.setAttribute(Const.CACHE, Cache.NONE);
		req.removeAttribute(Const.CACHE_ID);
		req.removeAttribute(Const.CACHE_LIFETIME);
		req.removeAttribute(Const.CACHE_SCHEDULE);
	}
	
	public String template(HttpServletRequest req) {
		return (String) req.getAttribute(Const.TEMPLATE);
	}
	
	public void template(HttpServletRequest req, String template) {
		req.setAttribute(Const.TEMPLATE, template);
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
