package com.wt.issuetracker.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.wt.issuetracker.entity.Account;
import com.wt.issuetracker.entity.Analyst;
import com.wt.issuetracker.entity.Customer;
import com.wt.issuetracker.entity.Issue;
import com.wt.issuetracker.entity.Manager;
import com.wt.issuetracker.entity.Team;
import com.wt.issuetracker.entity.User;

public class TeamDAO extends DAO{


	
	public Team searchById(Long teamId){
    	//throws AdException
    	System.out.println("Search By ID");
        try {
            begin();
            Query q = getSession().createQuery("from Team where teamId = :id");
            q.setLong("id", teamId);
            Team team = (Team)q.uniqueResult();
            commit();
            return team;
        } catch (Exception e) {
            rollback();
            //throw new AdException("Could not get user " + username, e);
            e.printStackTrace();
        }
		return null;
    }
	
	public List<Team> getListOfAllAvailableTeams(){
		
		begin();
        Query q = getSession().createQuery("from Team");
        List<Team> teamList = q.list();
        commit();
		return teamList;
	}
	
	
	
	public List<Team> getListOfTeamsByManager(Manager manager){
		
		 try {
	            begin();
	            Query q = getSession().createQuery("from Team where managerId = :managerId");
	            q.setLong("managerId", manager.getUserId());
	            List<Team> teamList = q.list();
	            commit();
	            return teamList;
	        } catch (Exception e) {
	            rollback();
	            //throw new AdException("Could not get user " + username, e);
	            e.printStackTrace();
	        }
			return null;
		
	}
	
    public Team create(Manager manager,String teamName,int sev1, int sev2, int sev3)
           // throws AdException 
    	{
        try {
            begin();
            Team team = new Team();
            team.setTeamName(teamName);
            team.setSev1(sev1);
            team.setSev2(sev2);
            team.setSev3(sev3);
            team.setManager(manager);
            getSession().save(team);
            commit();
            return team;
        } catch (HibernateException e) {
            rollback();
            //throw new AdException("Could not create user " + username, e);
            //throw new AdException("Exception while creating user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public void saveTeam(Team team)
    // throws AdException 
		{
    		System.out.println("Save Team : "+team);
	    	try{
	    		begin();
	    		
	    		getSession().saveOrUpdate(team);
	    		commit();
	    	}
	    	catch(HibernateException e){
	    		e.printStackTrace();
	    	}
		}
    
    public void addAnalystToTeam(Analyst a,Team t){
    	System.out.println("Add User "+ a + "to team : "+t);
    	try{
    		t.getAnalysts().add(a);
    		saveTeam(t);
    	}
    	catch(HibernateException e){
    		e.printStackTrace();
    	}
    }
    
    public void removeAnalystFromTeam(Analyst analyst,Team t){
    	System.out.println("Remove User "+ analyst + "to team : "+t);
    	try{
    		Set<Analyst> setAnalyst= t.getAnalysts();
    		Analyst tempA = null;
    		for(Analyst a : setAnalyst){
    			if(a.getUserId() == analyst.getUserId()){
    				tempA = a;
    			}
    		}
    		if(tempA!=null){
    			t.getAnalysts().remove(tempA);
    		}
    		saveTeam(t);
    	}
    	catch(HibernateException e){
    		e.printStackTrace();
    	}
    }
    
    public void deleteTeam(Team team){
    	System.out.println("Delete team : "+team);
    	try{
    		Manager manager = team.getManager();
			manager.getTeamList().remove(team);
			UserDAO userDAO = new UserDAO();
			userDAO.saveUser(manager);
    		begin();
    		getSession().delete(team);
    		commit();
    	}
    	catch(HibernateException e){
    		e.printStackTrace();
    	}
    }
    	
	
}
