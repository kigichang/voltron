package xv.voltron.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public final class CacheResponse extends HttpServletResponseWrapper {

	protected HttpServletResponse orgResp = null;
	protected StringWriter strWriter = null;
	protected PrintWriter wrapperWriter = null;
	public CacheResponse(HttpServletResponse response) {
		super(response);
		// TODO Auto-generated constructor stub
		orgResp = response;
		strWriter = new StringWriter();
		wrapperWriter = new PrintWriter(strWriter);
		
	}
	
	@Override
	public PrintWriter getWriter() throws IOException {
		// TODO Auto-generated method stub
		return wrapperWriter;
	}
	
	
	public void free() throws IOException {
		strWriter.close();
		wrapperWriter.close();
		strWriter = null;
		wrapperWriter = null;
	}
	
	
	

}
