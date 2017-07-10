package com.wt.issuetracker.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.wt.issuetracker.dao.AccountDAO;
import com.wt.issuetracker.entity.Account;
import com.wt.issuetracker.entity.Admin;
import com.wt.issuetracker.entity.Analyst;
import com.wt.issuetracker.entity.Customer;
import com.wt.issuetracker.entity.Manager;

@Controller
public class LoginController {
	
	@RequestMapping(value = "/login.htm", method = RequestMethod.GET)
	public ModelAndView handleRequestGET(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");return mv;
		
	}

	@RequestMapping(value = "/login.htm", method = RequestMethod.POST)
	public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession(true);
		
		System.out.println("LoginController!");;
		ModelAndView mv = new ModelAndView();
		AccountDAO accountDAO = new AccountDAO();
		

		
		Account account = accountDAO.get((String)request.getParameter("username"), (String)request.getParameter("password"));
		System.out.println("Username : " + request.getParameter("username"));
		System.out.println("Password : " + request.getParameter("password"));
		System.out.println("Account "+ account);
		//System.out.println("User "+ account.getUser());
		//System.out.println("Account.getRole() : " + account.getRole());
		if(account == null){
			mv.addObject("success", "FAIL");
			mv.addObject("loginError","Username/Password combination doesn't exist");
			mv.setViewName("home");
			System.out.println("home_template");
		}
		else{
			if(account.getUser().getRole().equals("admin")){
				
				//mv.addObject("user",(Admin)account.getUser());
				mv.setViewName("admin/admin_home");
				session.setAttribute("authorization", "admin");
				System.out.println("admin/admin_home.jsp");
			}
			else if(account.getUser().getRole().equals("analyst") || account.getUser().getRole().equals("expert")){
				//mv.addObject("user",(Analyst)account.getUser());
				mv.setViewName("analyst/analyst_home");
				session.setAttribute("authorization", "analyst");
				System.out.println("analyst/analyst_home.jsp");
			}
			else if(account.getUser().getRole().equals("manager")){
				//mv.addObject("user",(Manager)account.getUser());
				mv.setViewName("manager/manager_home");
				session.setAttribute("authorization", "manager");
				System.out.println("manager/manager_home.jsp");
			}
			else if(account.getUser().getRole().equals("customer")){
				//mv.addObject("user",(Customer)account.getUser());
				mv.setViewName("customer/customer_home");
				session.setAttribute("authorization", "customer");
				System.out.println("customer/customer_home.jsp");
			}
			System.out.println("no role");
			mv.addObject("success",account);
			session.setAttribute("sessionUserId", account.getUser().getUserId());
			session.setAttribute("sessionUserName", account.getUsername() + " - " + account.getUser().getFirstName() + " " + account.getUser().getLastName());
			System.out.println("Session for " + request.getSession(false).getAttribute("sessionUserId") + "is : " +  request.getSession(false));
			//session.setAttribute("sessionUser", account.getUser());
		}
		
		return mv; 
		
	}
	
}
