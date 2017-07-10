package com.wt.issuetracker.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.wt.issuetracker.configuration.DIConfiguration;
import com.wt.issuetracker.dao.AccountDAO;
import com.wt.issuetracker.dao.UserDAO;
import com.wt.issuetracker.entity.Admin;
import com.wt.issuetracker.entity.Analyst;
import com.wt.issuetracker.entity.Customer;
import com.wt.issuetracker.entity.Manager;
import com.wt.issuetracker.entity.User;
import com.wt.issuetracker.security.SecurityCheck;

@Controller
public class ProfileController {

	/*
	@RequestMapping(value = "/manager/profile.htm", method = RequestMethod.GET)
	public ModelAndView handleRequestManager(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		
		ModelAndView mv = commonCode(request,response);
		mv.setViewName("manager/profile");
		return mv;
	}
	
	@RequestMapping(value = "/customer/profile.htm", method = RequestMethod.GET)
	public ModelAndView handleRequestCustomer(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		
		ModelAndView mv = commonCode(request,response);
		mv.setViewName("customer/profile");
		return mv;
	}
	
	@RequestMapping(value = "/analyst/profile.htm", method = RequestMethod.GET)
	public ModelAndView handleRequestAnalyst(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		
		ModelAndView mv = commonCode(request,response);
		mv.setViewName("analyst/profile");
		return mv;
	}
	*/
	
	
	@RequestMapping(value = "*/profile.htm", method = RequestMethod.GET)
	public ModelAndView viewProfile(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				//boolean x = SecurityCheck.checkAuthorization(request,response,"admin");
				//System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
			

			
		ModelAndView mv = commonCode(request,response);
		String auth = (String)request.getSession(false).getAttribute("authorization");
		if(!auth.equals("")||auth!=null){
				mv.setViewName("common/profile");
		}
		
		
		return mv;
	}
		
	
	@RequestMapping(value = "/*/profile.htm", method = RequestMethod.POST)
	public ModelAndView handleRequestPost(HttpServletRequest request,
            HttpServletResponse response, @Valid @ModelAttribute("userToBeSaved")User userToBeSaved, BindingResult result) throws Exception{
		
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				//boolean x = SecurityCheck.checkAuthorization(request,response,"admin");
				//System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
			
			System.out.println("RESULT ERRORS : "+result);
			
			
			//ModelAndView mv = commonCode(request,response);
		ModelAndView mv = new ModelAndView();
		UserDAO userDAO = new UserDAO();
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DIConfiguration.class);


		
		boolean flag = false;
		Long sessionUserId = (Long)request.getSession(false).getAttribute("sessionUserId");
		Long userId = null;
		if(request.getParameter("useridtodisplay") != null){
			if(!request.getParameter("useridtodisplay").equals("")){
				System.out.println("useridtodisplay");
				userId = Long.parseLong(request.getParameter("useridtodisplay"));
				mv.addObject("useridtodisplay",Long.parseLong(request.getParameter("useridtodisplay")));
				flag = true;
			}
			
		}
		else{
			flag = false;
		}
		
		//Check for errors
		if(result.hasErrors()){
			//System.out.println("result"+result);
			//ModelAndView  mv = new ModelAndView();
			//mv.addObject("userToBeSaved",new Analyst());
			mv.setViewName("common/profile");
			//System.out.println("userToBeSaved " + u);
			//mv.addObject("userToBeSaved",u);

			
			return mv;
		}
		else{
			
			AccountDAO accountDAO = new AccountDAO();
			String chkuser = accountDAO.checkUserName(request.getParameter("account.username"));
			System.out.println("requested input username : "+request.getParameter("account.username"));
			System.out.println("request username : "+chkuser);
			if(chkuser.equals("")){
				
			}
			else{
				User uss = null;
				if(request.getParameter("useridtodisplay") != null){
					if(!request.getParameter("useridtodisplay").equals("")){
						uss = userDAO.searchById(Long.parseLong(request.getParameter("useridtodisplay")));
					}
				}
				
				
				System.out.println("displayed username :"+request.getParameter("useridtodisplay"));
				
					if(uss!=null){
						//ol
						if(!chkuser.equals(uss.getAccount().getUsername())){
							mv.addObject("useridtodisplay",request.getParameter("useridtodisplay"));
							mv.addObject("usernameErr","Username exists");
							mv.setViewName("common/profile");
							return mv;
						}
					}
					else{
						//new user
						//mv.addObject("useridtodisplay",request.getParameter("useridtodisplay"));
						mv.addObject("usernameErr","Username exists");
						mv.setViewName("common/profile");
						return mv;
					}
					
			}

		}
		

		
		//End Check for errors
		
		User u = null;
		
		if(!flag){
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
			
		}
		else{
			if(userId!=null){
				u = userDAO.searchById(userId);
			}
		}
		
			
	
		
	if(u!=null){
		System.out.println(u);
		System.out.println(u.getAccount());	
		


		
		u.setFirstName(request.getParameter("firstName"));
		u.setLastName(request.getParameter("lastName"));
		u.setEmailId(request.getParameter("emailId"));
		u.setRole(request.getParameter("role"));
		u.setPhoneNumber(request.getParameter("phoneNumber"));
		
		u.getAccount().setUsername(request.getParameter("account.username"));
		System.out.println("I/P username : " +request.getParameter("account.username"));
		u.getAccount().setPassword(request.getParameter("account.password"));
		System.out.println("I/P password : " +request.getParameter("account.password"));
		u.getAccount().setStatus(request.getParameter("account.status"));
		System.out.println("I/P status : " +request.getParameter("account.status"));
		u.getAccount().setUser(u);
		
		
		
		userDAO.saveUser(u);
		
		

		//mv.addObject("userToBeSaved",u);
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
		/*
		if(request.getRequestURI().indexOf("customer") >= 0){
			mv.setViewName("customer/profile");
		}
		*/
		mv.setViewName("common/profile");
		return mv;
	}
	
	
	public ModelAndView commonCode(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		ModelAndView mv = new ModelAndView();
		UserDAO userDAO = new UserDAO();
		
		User u = userDAO.searchById((Long)request.getSession(false).getAttribute("sessionUserId"));
		Long userId = (Long)request.getSession(false).getAttribute("sessionUserId");
		if(request.getParameter("useridtodisplay") != null){
			if(!request.getParameter("useridtodisplay").equals("")){
				System.out.println("useridtodisplay");
				userId = Long.parseLong(request.getParameter("useridtodisplay"));
			}
			
		}
		mv.addObject("useridtodisplay",userId);
		mv.addObject("userToBeSaved",u);
		return mv;
		
	}
	
	


	
	
}
