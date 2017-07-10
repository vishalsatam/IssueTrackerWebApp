package com.wt.issuetracker.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.wt.issuetracker.dao.IssueDAO;
import com.wt.issuetracker.dao.TeamDAO;
import com.wt.issuetracker.dao.UserDAO;
import com.wt.issuetracker.entity.Customer;
import com.wt.issuetracker.entity.Issue;
import com.wt.issuetracker.entity.Team;
import com.wt.issuetracker.filter.CustomValidator;
import com.wt.issuetracker.security.SecurityCheck;

@Controller
public class CreateIssueController {

	
	@RequestMapping(value = "/customer/newIssue.htm", method = RequestMethod.GET)
	public ModelAndView handleRequestCreateNewIssue(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
				if(SecurityCheck.checkAuthentication(request,response)){
					System.out.println("Auth success");
					boolean x = SecurityCheck.checkAuthorization(request,response,"customer");
					System.out.println("Authorizzation result : "+x);
				}
		//End Security Check
		ModelAndView mv = new ModelAndView();
		TeamDAO teamDAO = new TeamDAO();
		List<Team> teamList = teamDAO.getListOfAllAvailableTeams();
		mv.addObject("teamList",teamList);
		mv.setViewName("customer/raiseNewIssue");
		
		return mv;
	}
	
	@RequestMapping(value = "/customer/raiseNewIssue.htm", method = RequestMethod.POST)
	public ModelAndView handleRequestPost(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		//Security Check
		if(SecurityCheck.checkAuthentication(request,response)){
			System.out.println("Auth success");
			boolean x = SecurityCheck.checkAuthorization(request,response,"customer");
			System.out.println("Authorizzation result : "+x);
		}
		//End Security Check
		
		ModelAndView mv = new ModelAndView();
		
		UserDAO userDAO = new UserDAO();
		
		//Validation
		String tit = request.getParameter("title");
		String description = request.getParameter("description");
		String sev = request.getParameter("severity");
		String inpteam = request.getParameter("teamSelector");
		
		boolean err = false;
		String titleerr = "";
		String descriptionerr = "";
		if(tit!=null){
			if(tit.equals("")){
				titleerr += "Title Cannot be Empty. ";
				err=true;				
			}
			if(!CustomValidator.validateAlphanumericName(tit)){
				err = true;
				titleerr += "Title Cannot contain special characters. ";
			}
		}
		else{
			titleerr += "Title Cannot be Empty. ";
			err=true;	
		}
		
		if(description!=null){
			if(description.equals("")){
				descriptionerr += "Description Cannot be Empty. ";
				err=true;
				//mv.addObject("descriptionerr","Description Cannot be empty");
			}
			if(!CustomValidator.validateAlphanumericName(description)){
				err = true;
				descriptionerr += "Description Cannot contain special characters. ";
				//mv.addObject("descriptionerr","Description Cannot be contain special characters");
			}
		}
		else{
			descriptionerr += "Severity Cannot be Empty. ";
			err=true;
			//mv.addObject("descriptionerr","Description Cannot be empty");
		}
		
		
		try{
		Long severity = Long.parseLong(sev);
		
		}
		catch(NumberFormatException e){
			mv.addObject("severr","Value for Severity invalid. ");
			err=true;
		}
		try{
			Long teamId = Long.parseLong(inpteam);
			
			}
			catch(NumberFormatException e){
				mv.addObject("teamerr","Value for team invalid. ");
				err=true;
			}
		
		TeamDAO teamDAO = new TeamDAO();
		List<Team> teamList = teamDAO.getListOfAllAvailableTeams();
		
		if(err){
			mv.addObject("titleerr",titleerr);
			mv.addObject("descriptionerr",descriptionerr);
			mv.addObject("title",tit);
			mv.addObject("teamList",teamList);
			mv.addObject("description",description);
			mv.setViewName("customer/raiseNewIssue");
			return mv;
		}
		
		//Validation Ends Here
		
		Issue issue = new Issue();
		issue.setCreationDate(new Date());
		issue.setDescription(request.getParameter("description"));
		issue.setTitle(request.getParameter("title"));
		issue.setSeverity(Integer.parseInt(request.getParameter("severity")));
		//issue.setTeamId(teamDAO.searchById(Long.parseLong(request.getParameter("team"))));
		//Customer customer = (Customer)request.getSession().getAttribute("sessionUser");
		Customer customer = (Customer)userDAO.searchById((Long)request.getSession(false).getAttribute("sessionUserId"));
		issue.setIssueRaisedBy(customer);
		
		

		Team t = teamDAO.searchById(Long.parseLong(request.getParameter("teamSelector")));
		System.out.println("Team Id  : " + request.getParameter("teamSelector"));
		issue.setTeamId(t);
		issue.setStatus(Issue.Status.NEW.toString());
		
		IssueDAO issueDAO = new IssueDAO();
		issueDAO.createAndAssign(t, issue,customer);
	
		//teamDAO.addIssueToWorkBasket(t,issue);
		/*
		UserDAO userDAO = new UserDAO();
		customer.getPendingIssuesList().add(issue);
		userDAO.saveUser(customer);
		*/
		
		mv.addObject("teamList",teamList);
		mv.addObject("success","Updated");
		mv.setViewName("customer/raiseNewIssue");
		return mv;
	}
}
