package com.wt.issuetracker.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.wt.issuetracker.dao.UserDAO;
import com.wt.issuetracker.entity.Account;
import com.wt.issuetracker.entity.Admin;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	//@RequestMapping(value = "index.htm", method = RequestMethod.GET)
	@RequestMapping(value = {"/","/index.htm"}, method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		
		UserDAO userDAO = new UserDAO();
		if(!userDAO.checkFirstAdmin()){
			Admin admin = new Admin();
			admin.setFirstName("SYSTEM ADMIN");
			admin.setLastName("SYSTEM ADMIN");
			admin.setEmailId("SYSTEMADMIN@issuetraker.com");
			admin.setPhoneNumber("1234567890");
			admin.setRole("admin");
			
			Account account = new Account();
			account.setUsername("admin");
			account.setPassword("admin");
			account.setStatus("active");
			account.setUser(admin);
			admin.setAccount(account);
			
			userDAO.saveUser(admin);
		}
		
		mv.setViewName("home");
		return mv;
	}
	
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
}
