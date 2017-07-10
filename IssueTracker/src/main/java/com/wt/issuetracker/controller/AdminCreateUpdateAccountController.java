package com.wt.issuetracker.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.wt.issuetracker.configuration.DIConfiguration;
import com.wt.issuetracker.dao.UserDAO;
import com.wt.issuetracker.entity.Account;
import com.wt.issuetracker.entity.Admin;
import com.wt.issuetracker.entity.Analyst;
import com.wt.issuetracker.entity.Customer;
import com.wt.issuetracker.entity.Manager;
import com.wt.issuetracker.entity.User;
import com.wt.issuetracker.security.SecurityCheck;

@Controller
public class AdminCreateUpdateAccountController {

	@RequestMapping(value = "/admin/viewupdateuser.htm", method = RequestMethod.GET)
	ModelAndView handleRequest(HttpServletRequest request,
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
		User u = null;
		
		//Code for hibernate validation

		
		//end code for hibernate validation
		
		
		mv.setViewName("common/profile");
		
		
		if(request.getParameter("useridtodisplay") == null ){
			mv.addObject("userToBeSaved",new User());
			//mv.addObject("userIdOfUser","");
		}
		else{
			u = userDAO.searchById(Long.parseLong(request.getParameter("useridtodisplay")));
			mv.addObject("useridtodisplay",u.getUserId());
			mv.addObject("userToBeSaved",u);
		}
		
		if(u== null){
			System.out.println("Some Error Occurred");	
		}
		
		//mv.addObject("userToBeDisplayed",u);
		return mv;
	}
	
	@RequestMapping(value = "/admin/viewupdateuser.htm", method = RequestMethod.POST)
	ModelAndView handleRequestPost(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				boolean x = SecurityCheck.checkAuthorization(request,response,"admin");
				System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DIConfiguration.class);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/viewUserInAdmin");
		UserDAO userDAO = new UserDAO();
		User u = null;
		System.out.println("Submit User details");
		System.out.println((User)request.getAttribute("userToBeSaved"));
		System.out.println((User)request.getAttribute("userToBeDisplayed"));
		System.out.println(request.getParameter("userIdOfUser"));
		
		if(request.getParameter("userIdOfUser") == null || request.getParameter("userIdOfUser").equals("")){
			//Create New User
			if(request.getParameter("role").equals("analyst")){
				u = context.getBean(Analyst.class);
				//u.setRole("analyst");
			}
			if(request.getParameter("role").equals("customer")){
				u = context.getBean(Customer.class);
				//u.setRole("analyst");
			}
			if(request.getParameter("role").equals("manager")){
				u = context.getBean(Manager.class);
				//u.setRole("analyst");
			}
			if(request.getParameter("role").equals("admin")){
				u = context.getBean(Admin.class);
				//u.setRole("analyst");
			}
			
			//u.setAccount(new Account());
		}
		else{
			u = userDAO.searchById(Long.parseLong(request.getParameter("userIdOfUser")));
			System.out.println(request.getParameter("userIdOfUser"));
		}
		
		if(u!=null){
			System.out.println(u);
			System.out.println(u.getAccount());	
			
	
	
			
			u.setFirstName(request.getParameter("firstName"));
			u.setLastName(request.getParameter("lastName"));
			u.setEmailId(request.getParameter("emailId"));
			u.setRole(request.getParameter("role"));
			u.setPhoneNumber(request.getParameter("phoneNumber"));
			
			u.getAccount().setUsername(request.getParameter("username"));
			u.getAccount().setPassword(request.getParameter("password"));
			u.getAccount().setStatus(request.getParameter("status"));
			
			u.getAccount().setUser(u);
			
			userDAO.saveUser(u);
			
			

			mv.addObject("userToBeDisplayed",u);
			mv.addObject("userIdOfUser",u.getUserId());
			mv.addObject("updateStatus","Updated");
			if(u== null){
				System.out.println("Some Error Occurred");
				mv.addObject("updateStatus","ERROR");
			}
		}
		
		else{
			System.out.println("NULL USER");
			mv.addObject("updateStatus","ERROR");
		}
		
		return mv;
	}
	

}