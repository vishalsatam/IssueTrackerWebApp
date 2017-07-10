package com.wt.issuetracker.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.wt.issuetracker.dao.TeamDAO;
import com.wt.issuetracker.dao.UserDAO;
import com.wt.issuetracker.entity.Account;
import com.wt.issuetracker.entity.Analyst;
import com.wt.issuetracker.entity.Manager;
import com.wt.issuetracker.entity.TEST;
import com.wt.issuetracker.entity.Team;
import com.wt.issuetracker.security.SecurityCheck;

@Controller
public class ManageTeamsController {

	@RequestMapping(value = "manager/teamManagement.htm", method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				boolean x = SecurityCheck.checkAuthorization(request,response,"manager");
				System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
		ModelAndView mv = new ModelAndView();
		
		//Manager manager = (Manager)request.getSession(false).getAttribute("sessionUser");
		//Add Code to populate a list of teams
		UserDAO userDAO = new UserDAO();
		Manager manager = (Manager)userDAO.searchById((Long)request.getSession(false).getAttribute("sessionUserId"));
		
		System.out.println("Manager ID: "+ (Long)request.getSession(false).getAttribute("sessionUserId"));
		System.out.println("Manager POJO : "+ manager);
		TeamDAO teamDAO = new TeamDAO();
		List<Team> teamList = teamDAO.getListOfTeamsByManager(manager);
		
		mv.addObject("teamList", teamList);
		
		
		List usersWithoutList = userDAO.getUsersWithoutATeam();
		
		mv.addObject("userWithoutTeam", usersWithoutList);
		
		mv.setViewName("manager/manageTeams");
		
		return mv;
		
	}
	/*
	@RequestMapping(value = "/manager/createTeam.htm", method = RequestMethod.GET)
	public ModelAndView handleRequestCreateTeamGet(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		ModelAndView mv = new ModelAndView();
		
		if(request.getParameter("teamId") == null){
			
		}
		
		mv.setViewName("viewTeam");
		return mv;
	}
	*/
	@RequestMapping(value = "manager/createTeam.htm", method = RequestMethod.GET)
	public void handleRequestCreateTeamAjax(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
		if(SecurityCheck.checkAuthentication(request,response)){
			System.out.println("Auth success");
			boolean x = SecurityCheck.checkAuthorization(request,response,"manager");
			System.out.println("Authorizzation result : "+x);
		}
		//End Security Check
		System.out.println("create");
		if(request.getParameter("teamId") == null){
			System.out.println("team id is null");
			
			/*
			Team team = new Team();
			team.setManager(manager);
			team.setTeamName(request.getParameter("teamName"));
			*/
			UserDAO userDAO = new UserDAO();
			//Manager manager = (Manager)request.getSession().getAttribute("sessionUser");
			Manager manager = (Manager)userDAO.searchById((Long)request.getSession(false).getAttribute("sessionUserId"));
			TeamDAO teamDAO = new TeamDAO();
			Team team = teamDAO.create(manager,request.getParameter("teamName"),Integer.parseInt(request.getParameter("sev1")),Integer.parseInt(request.getParameter("sev2")),Integer.parseInt(request.getParameter("sev3")));
			System.out.println(team.getTeamName());
			
			manager.getTeamList().add(team);
			userDAO.saveUser(manager);
			/*
			//response.setAttribute("newTeam", team);
			//Manager manager = (Manager)request.getSession().getAttribute("sessionUser");
			//Team team = manager.addTeam();
			JSONObject obj = new JSONObject();
			List<Team> teamList = new ArrayList<Team>();
			teamList.add(team);
            obj.put("teamList",teamList);
			*/
            
			String jsonString = "{\"teamName\":\""+team.getTeamName()+"\",\"teamId\":\""+team.getTeamId()+"\"}";
			
			//System.out.println(jsonString);
			PrintWriter out = response.getWriter();
            
			out.println(jsonString);
			response.setStatus(200);
            
			
			//response.getWriter().println("Team Added");
		}
		
	}
		
	@RequestMapping(value = "manager/populateUsers.htm", method = RequestMethod.GET)
	public void handleRequestPopulteUsersAjax(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
		if(SecurityCheck.checkAuthentication(request,response)){
			System.out.println("Auth success");
			boolean x = SecurityCheck.checkAuthorization(request,response,"manager");
			System.out.println("Authorizzation result : "+x);
		}
		//End Security Check
		System.out.println("Populate Users");
		if(request.getParameter("teamId") != null){
			System.out.println("team id is "+request.getParameter("teamId") );
			Long teamId = Long.parseLong(request.getParameter("teamId"));
			
			TeamDAO teamDAO = new TeamDAO();
			Team team = teamDAO.searchById(teamId);
			Set<Analyst> analystList = team.getAnalysts();
			
            
                try {
                	
                ObjectMapper mapper = new ObjectMapper();
                //String jsonInString = mapper.writeValueAsString(analystList);
                TEST t = new TEST();
                t.setFirstName("LAS");
                t.setLastName("VEGAS");
                String jsonInString = mapper.writeValueAsString(t);
                System.out.println("Object Mappr :\n"+jsonInString);
//                Gson gson = new Gson();
//            	String json = gson.toJson(obj);
            	Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
					
					@Override
					public boolean shouldSkipField(FieldAttributes fieldAttributes) {
						// TODO Auto-generated method stub
						//return "Team".equals((fieldAttributes.getDeclaredClass().getName())) && "analysts".equals(fieldAttributes.getName()); 
						return  ("pendingIssuesList".equals(fieldAttributes.getName()) || "workList".equals(fieldAttributes.getName()) || "messageList".equals(fieldAttributes.getName()) || "issuesRaised".equals(fieldAttributes.getName()) || "analysts".equals(fieldAttributes.getName()) || "teamList".equals(fieldAttributes.getName()) || "workBasket".equals(fieldAttributes.getName()) || "manager".equals(fieldAttributes.getName()));
					}
			
					@Override
					public boolean shouldSkipClass(Class<?> clazz) {
						// TODO Auto-generated method stub
						
						return (clazz == Account.class);
					}
				}).serializeNulls().create();
            	System.out.println("GSON : ");
            	String gsonString = gson.toJson(analystList);
            	JsonElement jsonElem = gson.toJsonTree(analystList);
                System.out.println("GSON : \n"+gsonString);
                System.out.println("JSONELEM : \n"+jsonElem);
                
                PrintWriter out = response.getWriter();
                
                response.setStatus(response.SC_OK);
                
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("users", analystList);
                System.out.println("JSON : " + analystList);
                out.println(jsonElem);  
                
                
                } catch (JsonGenerationException e) {
        			e.printStackTrace();
        		} catch (JsonMappingException e) {
        			e.printStackTrace();
        		} 
                catch(IOException e){
                    e.printStackTrace();
                }catch (Exception e){
        			e.printStackTrace();
        		}
                
                
            
			
			//String jsonString = "{\"teamName\":\""+team.getTeamName()+"\",\"teamId\":\""+team.getTeamId()+"\"}";
			
			//System.out.println(jsonString);
			
            
			
			//response.getWriter().println("Team Added");
		}
		
	}	
		
	
	@RequestMapping(value = "manager/addToTeam.htm", method = RequestMethod.GET)
	public void handleRequestAddToTeamAjax(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				boolean x = SecurityCheck.checkAuthorization(request,response,"manager");
				System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
		System.out.println("Populate Users");
		if(request.getParameter("teamId") != null){
			System.out.println("team id is "+request.getParameter("teamId") );
			Long teamId = Long.parseLong(request.getParameter("teamId"));
			Long analystId = Long.parseLong(request.getParameter("analystId"));
			
			TeamDAO teamDAO = new TeamDAO();
			Team team = teamDAO.searchById(teamId);
			UserDAO userDAO = new UserDAO();
			Analyst a = (Analyst)userDAO.searchById(analystId);
			teamDAO.addAnalystToTeam(a, team);
			a.setTeamId(team);
			userDAO.saveUser(a);
			
			try{
                
                String jsonString = "{\"userId\":\""+a.getUserId()+"\",\"firstName\":\""+a.getFirstName()+"\"}";
                
                PrintWriter out = response.getWriter();
                response.setStatus(response.SC_OK);
                out.println(jsonString);    
                }
            catch(IOException e){
                    e.printStackTrace();
            } 
			
			//String jsonString = "{\"teamName\":\""+team.getTeamName()+"\",\"teamId\":\""+team.getTeamId()+"\"}";
			
			//System.out.println(jsonString);
			
            
			
			//response.getWriter().println("Team Added");
		}
		
	}
	
	@RequestMapping(value = "manager/removeFromTeam.htm", method = RequestMethod.GET)
	public void handleRequestRemoveFromTeamAjax(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				boolean x = SecurityCheck.checkAuthorization(request,response,"manager");
				System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
		System.out.println("Delete Users");
		if(request.getParameter("teamId") != null){
			System.out.println("team id is "+request.getParameter("teamId") );
			Long teamId = Long.parseLong(request.getParameter("teamId"));
			Long analystId = Long.parseLong(request.getParameter("analystId"));
			
			TeamDAO teamDAO = new TeamDAO();
			Team team = teamDAO.searchById(teamId);
			UserDAO userDAO = new UserDAO();
			Analyst a = (Analyst)userDAO.searchById(analystId);

			System.out.println("Analyst Worklist Size : "+a.getWorkList().size());
			try{
				if(a.getWorkList().size() > 0){
					PrintWriter out = response.getWriter();
					response.setStatus(response.SC_HTTP_VERSION_NOT_SUPPORTED);
					//System.out.println("Analyst contains assigned issue");
					//out.println("Analyst contains assigned issues");
					out.println("{\"msg\":\"Analyst Contains Assigned Issues\"}");
					
				}
				else{
					
					teamDAO.removeAnalystFromTeam(a, team);
					a.setTeamId(null);
					userDAO.saveUser(a);
					
	                PrintWriter out = response.getWriter();
	                response.setStatus(response.SC_OK);
	                System.out.println("Analyst Successfully Removed");
	                
	                String jsonString = "{\"msg\":\"Analyst Successfully Removed\",\"userId\":\""+a.getUserId()+"\",\"firstName\":\""+a.getFirstName()+"\",\"lastName\":\""+a.getLastName()+"\"}";
	                System.out.println(jsonString);
	                out.println(jsonString);
				}
				
                /*
                String jsonString = "{\"userId\":\""+a.getUserId()+"\",\"firstName\":\""+a.getFirstName()+"\"}";
                
                PrintWriter out = response.getWriter();
                response.setStatus(response.SC_OK);
                out.println(jsonString);
                */    
            }
            catch(IOException e){
                    e.printStackTrace();
            } 
			
			//String jsonString = "{\"teamName\":\""+team.getTeamName()+"\",\"teamId\":\""+team.getTeamId()+"\"}";
			
			//System.out.println(jsonString);
			
            
			
			//response.getWriter().println("Team Added");
		}
		
	}	
	
	
	@RequestMapping(value = "manager/deleteTeam.htm", method = RequestMethod.GET)
	public void handleRequestDeleteTeamAjax(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				boolean x = SecurityCheck.checkAuthorization(request,response,"manager");
				System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
		System.out.println("Delete Team");
		if(request.getParameter("teamId") != null){
			System.out.println("team id is "+request.getParameter("teamId") );
			Long teamId = Long.parseLong(request.getParameter("teamId"));
			
			
			TeamDAO teamDAO = new TeamDAO();
			Team team = teamDAO.searchById(teamId);
			if(team.getAnalysts().size() == 0){
				teamDAO.deleteTeam(team);
			}
			
			try{

                PrintWriter out = response.getWriter();
                response.setStatus(response.SC_OK);
                out.println("Success");    
                }
            catch(IOException e){
                    e.printStackTrace();
            } 
			
			//String jsonString = "{\"teamName\":\""+team.getTeamName()+"\",\"teamId\":\""+team.getTeamId()+"\"}";
			
			//System.out.println(jsonString);
			
            
			
			//response.getWriter().println("Team Added");
		}
		
	}	
	
}
