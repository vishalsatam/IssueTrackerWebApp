package com.wt.issuetracker.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.wt.issuetracker.dao.UserDAO;
import com.wt.issuetracker.entity.User;
import com.wt.issuetracker.security.SecurityCheck;

@Controller
public class ManageUsersController {
	@RequestMapping(value = "/admin/manageUsers.htm", method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
		if(SecurityCheck.checkAuthentication(request,response)){
			System.out.println("Auth success");
			boolean x = SecurityCheck.checkAuthorization(request,response,"admin");
			System.out.println("Authorizzation result : "+x);
		}
		//End Security Check
		UserDAO userDAO = new UserDAO();
		ModelAndView mv = new ModelAndView();
		if(request.getParameter("action") == null || request.getParameter("action").equals("")){
			List usersList = userDAO.getUserList();
			mv.setViewName("admin/manageUsers");
			mv.addObject("usersList", usersList);
		}
		else if(request.getParameter("action").equals("deleteIndiv")){
			System.out.println("Delete Individual : "+request.getParameter("deleteParam"));
			String userId = request.getParameter("deleteParam");
			userDAO.delete(userId);
			mv.setViewName("admin/manageUsers");
			response.setStatus(response.SC_OK);
		}
		

		return mv;
		
	}
	@RequestMapping(value = "/admin/manageUsers.htm", method = RequestMethod.POST)
	public ModelAndView handleRequestPost(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		//Security Check
		if(SecurityCheck.checkAuthentication(request,response)){
			System.out.println("Auth success");
			boolean x = SecurityCheck.checkAuthorization(request,response,"admin");
			System.out.println("Authorizzation result : "+x);
		}
		//End Security Check
		ModelAndView mv = new ModelAndView();
		UserDAO userDAO = new UserDAO();
			System.out.println("Delete Selected");
			
			String[] toBeDeletedUserList = request.getParameterValues("rowsToBeDeleted");
			//ArrayList<Long> toBeDeletedIds = new ArrayList<Long>();
			for(String userid : toBeDeletedUserList){
				userDAO.delete(userid);
			}
			

			List usersList = userDAO.getUserList();
			mv.setViewName("admin/manageUsers");
			mv.addObject("usersList", usersList);
			return mv;
	}
	
}
