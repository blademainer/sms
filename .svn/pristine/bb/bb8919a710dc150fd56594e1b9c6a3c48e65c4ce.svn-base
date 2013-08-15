package com.kingray.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharsetFilter implements Filter {
	
	private String encoding;

	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
		throws IOException, ServletException {
		System.out.println(request);
		System.out.println("request.getCharacterEncoding() =========== " + request.getCharacterEncoding());
		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);
		System.out.println("request.getCharacterEncoding() after =========== " + request.getCharacterEncoding());
		chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
		encoding = config.getInitParameter("encoding");
		if(encoding == null || "".equals(encoding.trim())){
			encoding = "UTF-8";
		}
//		encoding = config.getServletContext().getInitParameter("encoding");
	}

}
