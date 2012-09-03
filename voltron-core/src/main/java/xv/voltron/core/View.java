package xv.voltron.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import xv.voltron.constant.Const;

public final class View {

	private static String uri(String context, 
							  String servlet, 
							  String method, 
							  String tmpl,
							  boolean isCache,
							  String cacheId) {
		
		StringBuffer uri = isCache ? new StringBuffer("/WEB-INF/cache") :
										new StringBuffer("/WEB-INF");
		cacheId = cacheId == null ? "" : (cacheId.trim() + "_");
		
		if (method != null) {
			return uri.append(context)
					.append(servlet)
					.append('/')
					.append(cacheId)
					.append(method)
					.append(".jsp").toString();
		}
		
		if (tmpl != null) {
			if (tmpl.charAt(0) == '/') {
				return uri.append(tmpl).toString();
			}
			
			if(tmpl.indexOf('/') > 0) {
				return uri.append(context)
						.append('/')
						.append(cacheId)
						.append(tmpl).toString();
			}
			return uri.append(context)
					.append(servlet)
					.append('/')
					.append(cacheId)
					.append(tmpl).toString();
		}
		
		return null;
	}
	
	private static String file(String context, 
						String servlet, 
						String method, 
						String tmpl,
						boolean isCache,
						String cacheId) {
		String tmp = uri(context, servlet, method, tmpl, isCache, cacheId);
		if (tmp == null) {
			return null;
		}
		
		tmp = StringUtils.replaceChars(tmp, '/', File.separatorChar);
		return (Config.get("app.root") + tmp);
	}
	
	public static String templateUri(String context, String servlet, String method, String tmpl)
		throws IOException {
		String tmp = uri(context, servlet, method, tmpl, false, null);
		if (tmp == null) {
			throw new IOException(String.format("Uri %s Not Found", tmp));
		}
		return tmp;
	}
	
	public static String TemplateFile(HttpServletRequest req) {
		String tmpl = (String)req.getAttribute(Const.TEMPLATE);
		return View.file(req.getContextPath(), 
						 req.getServletPath(), 
						 tmpl == null ? 
								 (String)req.getAttribute(Const.ACTION_METHOD) :
								 null, 
						tmpl, false, null);
	}
	
	public static String cacheUri(String context, String servlet, String method, String tmpl, String cacheId) 
		throws IOException {
		String tmp = uri(context, servlet, method, tmpl, true, cacheId);
		if (tmp == null) {
			throw new IOException(String.format("Uri %s Not Found", tmp));
		}
		return tmp;
	}
	
	public static String cacheFile(HttpServletRequest req) {
		String tmpl = (String)req.getAttribute(Const.TEMPLATE);
		return View.file(req.getContextPath(), 
						 req.getServletPath(),
						 tmpl == null ?
								 (String)req.getAttribute(Const.ACTION_METHOD) :
								 null, 
						 tmpl,
						 true,
						 (String)req.getAttribute(Const.CACHE_ID));
	}
	
	public static String noCacheStart() {
		return "<!-- VOLTRON_NO_CACHE_START -->";
	}
	
	public static String noCacheEnd() {
		return "<!-- VOLTRON_NO_CACHE_END -->";
	}
	
	public static boolean isCached(String cacheFile, long expired, long currentTime) {
		File file = new File(cacheFile);
		if (!file.exists()) {
			return false;
		}
		
		return (file.lastModified() + expired) < currentTime; 
	}
	
	public static boolean isModified(String tmpl_file, String cache_file) 
			throws FileNotFoundException {
		File tmpl = new File(tmpl_file);
		File cache = new File(cache_file);
		
		if (!tmpl.exists()) {
			throw new FileNotFoundException(String.format("File %s Not Found", 
														  tmpl_file));
		}
		
		if (!cache.exists()) {
			return true;
		}
		
		return tmpl.lastModified() >= cache.lastModified();
	}
	
	public static boolean isCached(String cacheFile, String schedule, long currentTime) {
		File file = new File(cacheFile);
		if (!file.exists()) {
			return false;
		}
		
		long compute = 0;
		return (file.lastModified() + compute) < currentTime;
	}
	public static void draw(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		String tmpl = (String)req.getAttribute(Const.TEMPLATE);
		req.getRequestDispatcher(
				View.templateUri(
					req.getContextPath(),
					req.getServletPath(),
					tmpl == null ? 
							(String)req.getAttribute(Const.ACTION_METHOD) :
							null,
					tmpl)
				).include(req, resp);
	}
	
	public static void cache(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		String tmpl = (String)req.getAttribute(Const.TEMPLATE);
		req.getRequestDispatcher(
				View.cacheUri(
					req.getContextPath(),
					req.getServletPath(),
					tmpl == null ?
							(String)req.getAttribute(Const.ACTION_METHOD) :
							null,
					tmpl,
					(String)req.getAttribute(Const.CACHE_ID))
				).include(req, resp);
	}
	
	public static void writeCache(String file, String content) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Config.encoding()));
			bw.append(content);
		}
		catch(Exception e) {
			
		}
		finally {
			if (bw != null) {
				try { bw.close(); } catch(Exception e2){}
			}
		}
	}
}
