package com.wt.issuetracker.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.wt.issuetracker.dao.IssueDAO;
import com.wt.issuetracker.dao.UserDAO;
import com.wt.issuetracker.entity.Issue;
import com.wt.issuetracker.entity.Manager;
import com.wt.issuetracker.entity.Team;
import com.wt.issuetracker.security.SecurityCheck;

@Controller
public class MyIssuesController {
	@RequestMapping(value = "*/pendingIssues.htm", method = RequestMethod.GET)
	public ModelAndView viewPendingIssues(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				ArrayList<String> authList = new ArrayList<String>();
				authList.add("customer");
				authList.add("analyst");
				authList.add("manager");
				boolean x = SecurityCheck.checkAuthorization(request,response,authList);
				System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
		ModelAndView mv = new ModelAndView();
		
		Long userId = (Long)request.getSession(false).getAttribute("sessionUserId");
		String authorization = (String)request.getSession(false).getAttribute("authorization");
		if(userId != null){
			System.out.println("User ID : "+userId);
			UserDAO userDAO = new UserDAO();
			Set<Issue> pendingIssuesList = userDAO.getPendingIssues(userId);
			mv.addObject("issueList", pendingIssuesList);
			
			mv.addObject("tabToBeShown", "Pending");
			mv.setViewName("common/workAreaIssueList");
		}
		
		
		return mv;
		
	}
	
	@RequestMapping(value = "*/myIssues.htm", method = RequestMethod.GET)
	public ModelAndView viewMyIssues(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				ArrayList<String> authList = new ArrayList<String>();
				authList.add("customer");
				authList.add("analyst");
				authList.add("manager");
				boolean x = SecurityCheck.checkAuthorization(request,response,authList);
				System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
		ModelAndView mv = new ModelAndView();
		Long userId = (Long)request.getSession(false).getAttribute("sessionUserId");
		if(userId != null){
			UserDAO userDAO = new UserDAO();
			List<Issue>issueList =  userDAO.getMyIssues(userId);
			mv.addObject("issueList", issueList);
			mv.addObject("tabToBeShown","MyIssues");
			mv.setViewName("common/workAreaIssueList");
		}
		
		return mv;
		
		
	}
	
	@RequestMapping(value = "*/closedIssues.htm", method = RequestMethod.GET)
	public ModelAndView viewClosedIssues(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				ArrayList<String> authList = new ArrayList<String>();
				authList.add("customer");
				authList.add("analyst");
				authList.add("manager");
				boolean x = SecurityCheck.checkAuthorization(request,response,authList);
				System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
		ModelAndView mv = new ModelAndView();
		Long userId = (Long)request.getSession(false).getAttribute("sessionUserId");
		if(userId != null){
			UserDAO userDAO = new UserDAO();
			List<Issue>issueList =  userDAO.getClosedIssues(userId);
			mv.addObject("issueList", issueList);
			mv.addObject("tabToBeShown","Closed");
			mv.setViewName("common/workAreaIssueList");
		}
		
		return mv;
		
		
	}
	
	
	@RequestMapping(value = "*/searchForIssues.htm", method = RequestMethod.GET)
	public ModelAndView searchForIssues(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				ArrayList<String> authList = new ArrayList<String>();
				authList.add("customer");
				authList.add("analyst");
				authList.add("manager");
				boolean x = SecurityCheck.checkAuthorization(request,response,authList);
				System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
		ModelAndView mv = new ModelAndView();
		Long userId = (Long)request.getSession(false).getAttribute("sessionUserId");
		if(userId != null){

			mv.addObject("tabToBeShown","SearchForIssue");
			mv.setViewName("common/searchForIssue");

			
		}
		
		return mv;
		
		
	}
	
	@RequestMapping(value = "*/searchForIssues.htm", method = RequestMethod.POST)
	public ModelAndView searchForIssuesPost(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				ArrayList<String> authList = new ArrayList<String>();
				authList.add("customer");
				authList.add("analyst");
				authList.add("manager");
				boolean x = SecurityCheck.checkAuthorization(request,response,authList);
				System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
		ModelAndView mv = new ModelAndView();
		Long userId = (Long)request.getSession(false).getAttribute("sessionUserId");
		if(userId != null){
			String searchBy = request.getParameter("searchBy");
			System.out.println("Search by :"+searchBy);
			if(searchBy.equals("searchById")){
				try{
					Long searchString = Long.parseLong(request.getParameter("searchString"));
					System.out.println("Search String :"+searchString);
					IssueDAO issueDAO = new IssueDAO();
					List<Issue> issueList = new ArrayList<Issue>();
					Issue issue  = issueDAO.searchByID(searchString);
					if(issue!=null){
						issueList.add(issue);
					}
					mv.addObject("searchString",searchString);
					mv.addObject("numResults",issueList.size());
					mv.addObject("searchBy","ID");
					mv.addObject("issueList", issueList);
				}
				catch(NumberFormatException ne){
					ne.printStackTrace();
				}
				
			}
			else if (searchBy.equals("searchByTitle")){
				String searchString = request.getParameter("searchString");
				IssueDAO issueDAO = new IssueDAO();
				List<Issue> issueList = issueDAO.searchByTitle(searchString);
				mv.addObject("searchString",searchString);
				mv.addObject("numResults",issueList.size());
				mv.addObject("searchBy","Title");
				mv.addObject("issueList", issueList);
				
			}
			else if(searchBy.equals("searchByTeamId")){
				try{
					Long searchString = Long.parseLong(request.getParameter("searchString"));
					System.out.println("Search String :"+searchString);
					IssueDAO issueDAO = new IssueDAO();
					List<Issue> issueList  = issueDAO.searchByTeamId(searchString);
					
					mv.addObject("searchString",searchString);
					mv.addObject("numResults",issueList.size());
					mv.addObject("searchBy","TeamId");
					mv.addObject("issueList", issueList);
				}
				catch(NumberFormatException ne){
					ne.printStackTrace();
				}
				
			}
			
			mv.addObject("tabToBeShown","SearchForIssue");
			mv.setViewName("common/searchForIssue");
			
			
			
		}
		
		return mv;
		
		
	}
	
	
}
