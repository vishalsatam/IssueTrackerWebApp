package com.wt.issuetracker.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wt.issuetracker.security.SecurityCheck;

public class AdminAuthFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest hreq =(HttpServletRequest)((ServletRequest)request);
		HttpServletResponse  hres =(HttpServletResponse)((ServletResponse)request);
		//Security Check
				if(SecurityCheck.checkAuthentication(hreq,hres)){
					System.out.println("Auth success");
					boolean x = SecurityCheck.checkAuthorization(hreq,hres,"admin");
					System.out.println("Authorizzation result : "+x);
				}
		//End Security Check
				filterChain.doFilter(hreq, hres);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	
	
}
