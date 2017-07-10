package com.wt.issuetracker.controller;

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
import com.wt.issuetracker.entity.Analyst;
import com.wt.issuetracker.entity.Issue;
import com.wt.issuetracker.entity.Issue.Status;
import com.wt.issuetracker.security.SecurityCheck;
import com.wt.issuetracker.entity.Manager;
import com.wt.issuetracker.entity.Team;

@Controller
public class ManagerIssueController {

	@RequestMapping(value = "manager/assignIssue.htm", method = RequestMethod.GET)
	public ModelAndView handleRequestAssignIssuesGet(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
		if(SecurityCheck.checkAuthentication(request,response)){
			System.out.println("Auth success");
			boolean x = SecurityCheck.checkAuthorization(request,response,"manager");
			System.out.println("Authorizzation result : "+x);
		}
		//End Security Check
		return populateData(request,response);
	}
	
	
	@RequestMapping(value = "manager/assignIssue.htm", method = RequestMethod.POST)
	public ModelAndView handleRequestAssignIssuesPost(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				boolean x = SecurityCheck.checkAuthorization(request,response,"manager");
				System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
		System.out.println(request.getParameter("teamSelector"));
		if(request.getParameter("teamSelector") != null){
			
			if(!request.getParameter("teamSelector").equals("")){
				try
				{
					UserDAO userDAO = new UserDAO();
					TeamDAO teamDAO = new TeamDAO();
					IssueDAO issueDAO = new IssueDAO();
					Long teamId = Long.parseLong(request.getParameter("teamSelector"));
					Long analystId = Long.parseLong(request.getParameter("analystSelector"));
					Long issueId = Long.parseLong(request.getParameter("issueSelector"));
					
					Team team = teamDAO.searchById(teamId);
					Analyst analyst = (Analyst)userDAO.searchById(analystId);
					Issue issue = issueDAO.searchByID(issueId);
					
					analyst.getWorkList().add(issue);
					analyst.getPendingIssuesList().add(issue);
					userDAO.saveUser(analyst);
					System.out.println("Issue Status after user save: "+issue.getStatus());
					team.getWorkBasket().remove(issue);
					teamDAO.saveTeam(team);
					System.out.println("Issue Status after team: "+issue.getStatus());
					issue.setPrimaryAssigneeId(analyst);
					issue.setIssueCurrentlyWithId(analyst);
					issue.setStatus(Issue.Status.OPEN_WITH_ASSIGNEE.toString());
					issueDAO.saveOrUpdate(issue);
					System.out.println("Issue Status after issue save: "+issue.getStatus());

					request.setAttribute("teamId", teamId);
					
				}
				catch(NumberFormatException e){
					e.printStackTrace();
				}
			}
			
		}
		
		return populateData(request,response);
		
	}
	
	public ModelAndView populateData(HttpServletRequest request, HttpServletResponse response){
		
		ModelAndView mv = new ModelAndView();
		
		TeamDAO teamDAO = new TeamDAO();
		IssueDAO issueDAO = new IssueDAO();
		UserDAO userDAO = new UserDAO();
	
		//Manager manager = (Manager)request.getSession(false).getAttribute("sessionUser");
		Manager manager = (Manager)userDAO.searchById((Long)request.getSession(false).getAttribute("sessionUserId"));
		System.out.println("Manager : "+manager);
		List<Team> teamList = teamDAO.getListOfTeamsByManager(manager);
		mv.addObject("teamList",teamList);
		try{
			System.out.println(request.getParameter("teamId"));
			if(request.getParameter("teamId") != null){
				
				if(!request.getParameter("teamId").equals("")){
					Long teamId = Long.parseLong(request.getParameter("teamId"));
					Team t = teamDAO.searchById(teamId);
					mv.addObject("analystList",t.getAnalysts());
					mv.addObject("issueList",t.getWorkBasket());
					mv.addObject("teamId",teamId);
					System.out.println("Analysts : "+t.getAnalysts());
					System.out.println("WorkBasket : " +t.getWorkBasket());
				}
				
				
			}
			
			
		}
		catch(NumberFormatException e){
			e.printStackTrace();
		}
		
		
		mv.setViewName("manager/assignIssues");
		return mv;
	}
	
}
