package com.wt.issuetracker.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityCheck {
	
	public static boolean checkAuthorization(HttpServletRequest request, HttpServletResponse response, ArrayList<String> role){
		try{
			System.out.println("CheckAuthoriation  session : "+request.getSession(false));
			if((String)request.getSession(false).getAttribute("authorization") == null){
				response.sendRedirect(request.getContextPath()+"/index.htm");
				return false;
			}
			boolean check = false;
			for(String r : role){
				if(!r.equals("")){
					if(!((String)request.getSession(false).getAttribute("authorization")).equals("") && request.getSession(false).getAttribute("authorization").equals(r.toString())){
						System.out.println("AUTH  : "+r.toString());
						check = true;
					}
					
				}
			}
			if(check){
				return true;
			}
			else{
				response.sendRedirect(request.getContextPath()+"/index.htm");
				//rd.forward(request, response);
				return false;
			}
				
			}
			catch(IOException e){
				e.printStackTrace();
			}
			return true;
	}
	
	public static boolean checkAuthentication(HttpServletRequest request, HttpServletResponse response){
		try{
		//RequestDispatcher rd = request.getRequestDispatcher("/index.htm");
		
		System.out.println("checkauthentiction Session : "+request.getSession(false));
		/*
		System.out.println("User ID : "+request.getSession(false).getAttribute("sessionUserId"));
		System.out.println("Authorization : "+request.getSession(false).getAttribute("authorization"));
		*/
		if(request.getSession(false) == null){
			request.getSession(true);
			response.sendRedirect(request.getContextPath()+"/index.htm");
			return false;
			//rd.forward(request, response);
		}
		else if(request.getSession(false).getAttribute("sessionUserId") == null){
				response.sendRedirect(request.getContextPath()+"/index.htm");
				return false;
		}
		else if(!((Long)request.getSession(false).getAttribute("sessionUserId")).toString().equals("")){
			
		}
			//rd.forward(request, response);

		return true;
		}/*
		catch(ServletException s){
			s.printStackTrace();
		}
		*/
		catch(IOException e){
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean checkAuthorization(HttpServletRequest request, HttpServletResponse response, String role){
		try{
		System.out.println("CheckAuthoriation  session : "+request.getSession(false));
		if((String)request.getSession(false).getAttribute("authorization") == null){
			response.sendRedirect(request.getContextPath()+"/index.htm");
			return false;
		}
		if(((String)request.getSession(false).getAttribute("authorization")).equals("") || !request.getSession(false).getAttribute("authorization").equals(role.toString())){
			response.sendRedirect(request.getContextPath()+"/index.htm");
			//rd.forward(request, response);
			return false;
		}
		return true;
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return true;
	}
}
