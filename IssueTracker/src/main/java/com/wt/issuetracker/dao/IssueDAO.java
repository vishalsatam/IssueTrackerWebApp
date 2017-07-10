package com.wt.issuetracker.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import com.wt.issuetracker.entity.Analyst;
import com.wt.issuetracker.entity.Customer;
import com.wt.issuetracker.entity.Issue;
import com.wt.issuetracker.entity.Manager;
import com.wt.issuetracker.entity.Message;
import com.wt.issuetracker.entity.Team;
import com.wt.issuetracker.entity.User;

public class IssueDAO extends DAO{

	public IssueDAO(){
		
	}
	
	public void createAndAssign(Team t, Issue issue, Customer c){
        try {
        	TeamDAO teamDAO = new TeamDAO();
        	t.getWorkBasket().add(issue);
        	teamDAO.saveTeam(t);
        	c.getIssuesRaised().add(issue);
    		UserDAO userDAO = new UserDAO();
    		userDAO.saveUser(c);
            begin();
            getSession().save(issue);
            commit();
        } catch (HibernateException e) {
            rollback();
            //throw new AdException("Could not create user " + username, e);
            //throw new AdException("Exception while creating user: " + e.getMessage());
            e.printStackTrace();
        }
	}
	
	public void saveOrUpdate(Issue issue){
        try {
            begin();
            getSession().saveOrUpdate(issue);
            commit();
            
        } catch (HibernateException e) {
            rollback();
            //throw new AdException("Could not create user " + username, e);
            //throw new AdException("Exception while creating user: " + e.getMessage());
            e.printStackTrace();
        }
	}
	
	public List<Message> getMessagesForIssueId(Long issueId, int first, int max){
		try {
		 	begin();
            Query q = getSession().createQuery("from Issue where issueId = :id");
            q.setLong("id", issueId);
            Issue issue = (Issue)q.uniqueResult();
            commit();
            begin();
            Session hibSession = getSession();
            Criteria crit = hibSession.createCriteria(Message.class);
            Criteria issuCrit = crit.createCriteria("issue");
            issuCrit.add(Restrictions.eq("issueId", issueId));
            crit.addOrder(Order.desc("sentDate"));
            //q = hibSession.createQuery("from Message where issueId = :id");
            //q.setLong("id", issueId);
            crit.setFirstResult(first);
            crit.setMaxResults(max);
            
            
            List<Message> messages = crit.list();
            //List<Message> messages = issue.getMessageList();
            //System.out.println(messages);
            commit();
            return messages;
            
        } catch (HibernateException e) {
            rollback();
            //throw new AdException("Could not create user " + username, e);
            //throw new AdException("Exception while creating user: " + e.getMessage());
            e.printStackTrace();
     }
	 return null;
	}
	
	public long numberOfMessages(Long issueId){
		begin();
        Session hibSession = getSession();
        Criteria crit = hibSession.createCriteria(Message.class);
        Criteria issuCrit = crit.createCriteria("issue");
        issuCrit.add(Restrictions.eq("issueId", issueId));
        crit.setProjection(Projections.rowCount());
        //Query q = hibSession.createQuery("from Message where issueId = :id");
        //q.setLong("id", issueId);
        List<Long> results = crit.list();
        commit();
        return results.get(0);
	}
	
	public Issue searchByID(Long id){
		 try {
			 	begin();
	            Query q = getSession().createQuery("from Issue where issueId = :id");
	            q.setLong("id", id);
	            Issue issue = (Issue)q.uniqueResult();
	            commit();
	            return issue;
	            
	        } catch (HibernateException e) {
	            rollback();
	            //throw new AdException("Could not create user " + username, e);
	            //throw new AdException("Exception while creating user: " + e.getMessage());
	            e.printStackTrace();
	     }
		 return null;
	}
	
	public List<Issue> searchByTitle(String id){
		 try {
			 	begin();
	            Query q = getSession().createQuery("from Issue where title like :id");
	            q.setString("id",  "%"+id+"%");
	            List<Issue> issueList = q.list();
	            commit();
	            return issueList;
	            
	        } catch (HibernateException e) {
	            rollback();
	            //throw new AdException("Could not create user " + username, e);
	            //throw new AdException("Exception while creating user: " + e.getMessage());
	            e.printStackTrace();
	     }
		 return null;
	}
	
	public List<Issue> searchByTeamId(Long teamId){
		 try {
	            begin();
	            Session hibSession = getSession();
	            /*
	            Criteria crit = hibSession.createCriteria(Issue.class);
	            Criteria issuCrit = crit.createCriteria("teamId");
	            issuCrit.add(Restrictions.eq("teamId", teamId));
	            */
	            //crit.addOrder(Order.desc("creationDate"));
	            //q = hibSession.createQuery("from Message where issueId = :id");
	            //q.setLong("id", issueId);
         
	            /*
	            SQLQuery q = hibSession.createSQLQuery("SELECT * FROM issue WHERE teamId = :teamId");
	            q.setLong("teamId", teamId);
	            */
	            Query q = hibSession.createQuery("from Issue where teamId.teamId = :teamId");
	            q.setParameter("teamId", teamId);
	            
	            
	            List<Issue> issueList = q.list();
	            
	            
	            
	            
	            commit();
	            return issueList;
	            
	        } catch (HibernateException e) {
	            rollback();
	            //throw new AdException("Could not create user " + username, e);
	            //throw new AdException("Exception while creating user: " + e.getMessage());
	            e.printStackTrace();
	     }
		 return null;
	}

	
	public void addMessage(Long issueId,Message m){
		try {
		 	begin();
            Query q = getSession().createQuery("from Issue where issueId = :id");
            q.setLong("id", issueId);
            Issue issue = (Issue)q.uniqueResult();
            m.setIssue(issue);
            issue.getMessageList().add(m);
            getSession().saveOrUpdate(issue);
            commit();
            
            
        } catch (HibernateException e) {
            rollback();
            //throw new AdException("Could not create user " + username, e);
            //throw new AdException("Exception while creating user: " + e.getMessage());
            e.printStackTrace();
        }
		
	}
		
		public void sendForClarification(Long issueId,Message m){
			try {
			 	begin();
	            Query q = getSession().createQuery("from Issue where issueId = :id");
	            q.setLong("id", issueId);
	            Issue issue = (Issue)q.uniqueResult();
	            m.setIssue(issue);
	            issue.getMessageList().add(m);
	            User u = issue.getIssueCurrentlyWithId();
	            u.getPendingIssuesList().remove(issue);
	            Team team = issue.getTeamId();
	            
	            Manager mgr = team.getManager();
	            issue.setIssueCurrentlyWithId(mgr);
	            issue.setStatus(Issue.Status.PENDING_CLARIFICATION_APPROVAL.toString());
	            getSession().saveOrUpdate(issue);
	            getSession().update(u);
	            
	            commit();
	            
	            begin();
	            mgr.getPendingIssuesList().add(issue);
	            getSession().update(mgr);
	            commit();
	            
	            
	        } catch (HibernateException e) {
	            rollback();
	            //throw new AdException("Could not create user " + username, e);
	            //throw new AdException("Exception while creating user: " + e.getMessage());
	            e.printStackTrace();
	        }
		
	}
	
		public void closeRequest(Long issueId,Message m){
			try {
			 	begin();
	            Query q = getSession().createQuery("from Issue where issueId = :id");
	            q.setLong("id", issueId);
	            Issue issue = (Issue)q.uniqueResult();
	            m.setIssue(issue);
	            issue.getMessageList().add(m);
	            User u = issue.getIssueCurrentlyWithId();
	            u.getPendingIssuesList().remove(issue);
	            Team team = issue.getTeamId();
	            
	            Manager mgr = team.getManager();
	            issue.setIssueCurrentlyWithId(mgr);
	            issue.setStatus(Issue.Status.PENDING_CLOSURE_APPROVAL.toString());
	            getSession().saveOrUpdate(issue);
	            getSession().update(u);
	            
	            commit();
	            
	            begin();
	            mgr.getPendingIssuesList().add(issue);
	            getSession().update(mgr);
	            commit();
	            
	            
	        } catch (HibernateException e) {
	            rollback();
	            //throw new AdException("Could not create user " + username, e);
	            //throw new AdException("Exception while creating user: " + e.getMessage());
	            e.printStackTrace();
	        }
		
	}
		
		public void approve(Long issueId,Message m){
			try {
				System.out.println("Approval");
			 	begin();
	            Query q = getSession().createQuery("from Issue where issueId = :id");
	            q.setLong("id", issueId);
	            Issue issue = (Issue)q.uniqueResult();
	            m.setIssue(issue);
	            issue.getMessageList().add(m);
	            User u = issue.getIssueCurrentlyWithId();
	            u.getPendingIssuesList().remove(issue);
	            Team team = issue.getTeamId();
	            
	            Customer cust = issue.getIssueRaisedBy();
	            issue.setIssueCurrentlyWithId(cust);
	            if(issue.getStatus().equals(Issue.Status.PENDING_CLOSURE_APPROVAL.toString())){
	            	System.out.println("Closed by Team");
	            	issue.setStatus(Issue.Status.CLOSED_BY_TEAM.toString());
	            }
	            else if(issue.getStatus().equals(Issue.Status.PENDING_CLARIFICATION_APPROVAL.toString())){
	            	System.out.println("Pending Clarificaion");
	            	issue.setStatus(Issue.Status.PENDING_CLARIFICATION.toString());
	            }
	            else{
	            	
	            }
	            getSession().saveOrUpdate(issue);
	            getSession().update(u);
	            
	            commit();
	            
	            begin();
	            cust.getPendingIssuesList().add(issue);
	            getSession().update(cust);
	            commit();
	            
	            
	        } catch (HibernateException e) {
	            rollback();
	            //throw new AdException("Could not create user " + username, e);
	            //throw new AdException("Exception while creating user: " + e.getMessage());
	            e.printStackTrace();
	        }
		}
		
		public void providedClarification(Long issueId,Message m){
			try {
			 	begin();
	            Query q = getSession().createQuery("from Issue where issueId = :id");
	            q.setLong("id", issueId);
	            Issue issue = (Issue)q.uniqueResult();
	            m.setIssue(issue);
	            issue.getMessageList().add(m);
	            User u = issue.getIssueCurrentlyWithId();
	            u.getPendingIssuesList().remove(issue);
	            Team team = issue.getTeamId();
	            
	            Analyst a = issue.getPrimaryAssigneeId();
	            issue.setIssueCurrentlyWithId(a);
	            issue.setStatus(Issue.Status.OPEN_WITH_ASSIGNEE.toString());
	            getSession().saveOrUpdate(issue);
	            getSession().update(u);
	            
	            commit();
	            
	            begin();
	            a.getPendingIssuesList().add(issue);
	            getSession().update(a);
	            commit();
	            
	            
	        } catch (HibernateException e) {
	            rollback();
	            //throw new AdException("Could not create user " + username, e);
	            //throw new AdException("Exception while creating user: " + e.getMessage());
	            e.printStackTrace();
	        }
		}
		
		public void rejectClarification(Long issueId,Message m){
			try {
			 	begin();
	            Query q = getSession().createQuery("from Issue where issueId = :id");
	            q.setLong("id", issueId);
	            Issue issue = (Issue)q.uniqueResult();
	            m.setIssue(issue);
	            issue.getMessageList().add(m);
	            User u = issue.getIssueCurrentlyWithId();
	            u.getPendingIssuesList().remove(issue);
	            Team team = issue.getTeamId();
	            
	            Analyst a = issue.getPrimaryAssigneeId();
	            issue.setIssueCurrentlyWithId(a);
	            issue.setStatus(Issue.Status.OPEN_WITH_ASSIGNEE.toString());
	            getSession().saveOrUpdate(issue);
	            getSession().update(u);
	            
	            commit();
	            
	            begin();
	            a.getPendingIssuesList().add(issue);
	            getSession().update(a);
	            commit();
	            
	            
	        } catch (HibernateException e) {
	            rollback();
	            //throw new AdException("Could not create user " + username, e);
	            //throw new AdException("Exception while creating user: " + e.getMessage());
	            e.printStackTrace();
	        }
		}
		
		public void confirmClosure(Long issueId,Message m){
			try {
			 	begin();
	            Query q = getSession().createQuery("from Issue where issueId = :id");
	            q.setLong("id", issueId);
	            Issue issue = (Issue)q.uniqueResult();
	            m.setIssue(issue);
	            issue.getMessageList().add(m);
	            User u = issue.getIssueCurrentlyWithId();
	            u.getPendingIssuesList().remove(issue);
	            Team team = issue.getTeamId();
	            issue.setIssueCurrentlyWithId(null);
	            
	            issue.setStatus(Issue.Status.CLOSED_BY_REQUESTOR.toString());
	            getSession().saveOrUpdate(issue);
	            getSession().update(u);
	            
	            commit();
	            
	            /*
	            begin();
	            a.getPendingIssuesList().add(issue);
	            getSession().update(a);
	            commit();
	            */
	            
	        } catch (HibernateException e) {
	            rollback();
	            //throw new AdException("Could not create user " + username, e);
	            //throw new AdException("Exception while creating user: " + e.getMessage());
	            e.printStackTrace();
	        }
		}
		
		
		public void reopen(Long issueId,Message m){
			try {
			 	begin();
	            Query q = getSession().createQuery("from Issue where issueId = :id");
	            q.setLong("id", issueId);
	            Issue issue = (Issue)q.uniqueResult();
	            m.setIssue(issue);
	            issue.getMessageList().add(m);
	            if(issue.getStatus().equals(Issue.Status.CLOSED_BY_TEAM.toString())){
	            	User u = issue.getIssueCurrentlyWithId();
		            u.getPendingIssuesList().remove(issue);
		            Team team = issue.getTeamId();
		            Analyst a = issue.getPrimaryAssigneeId();
		            issue.setIssueCurrentlyWithId(a);
		            issue.setStatus(Issue.Status.OPEN_WITH_ASSIGNEE.toString());
		            getSession().update(u);
		            getSession().saveOrUpdate(issue);
		            
		            commit();
		            begin();
		            a.getPendingIssuesList().add(issue);
		            getSession().update(a);
		            commit();
	            }
	            else if(issue.getStatus().equals(Issue.Status.CLOSED_BY_REQUESTOR.toString())){

		            Team team = issue.getTeamId();
		            Analyst a = issue.getPrimaryAssigneeId();
		            issue.setIssueCurrentlyWithId(a);
		            issue.setStatus(Issue.Status.OPEN_WITH_ASSIGNEE.toString());
		            getSession().saveOrUpdate(issue);
		           
		            a.getPendingIssuesList().add(issue);
		            getSession().update(a);
		            commit();
	            }
	            
	        } catch (HibernateException e) {
	            rollback();
	            //throw new AdException("Could not create user " + username, e);
	            //throw new AdException("Exception while creating user: " + e.getMessage());
	            e.printStackTrace();
	        }
		}
		
		public void transfer(Long issueId,Message m,Long userId){
			try {
			 	begin();
			 	
			 	Query q1 = getSession().createQuery("from User where userId = :userID");
			 	q1.setLong("userID", userId);
			 	Analyst newAnalyst = (Analyst)q1.uniqueResult();
			 	
	            Query q = getSession().createQuery("from Issue where issueId = :id");
	            q.setLong("id", issueId);
	            Issue issue = (Issue)q.uniqueResult();
	            m.setIssue(issue);
	            issue.getMessageList().add(m);
	            User u = issue.getIssueCurrentlyWithId();
	            u.getPendingIssuesList().remove(issue);
	            //Team team = issue.getTeamId();
	            
	            //Manager mgr = team.getManager();
	            
	            issue.setIssueCurrentlyWithId(newAnalyst);
	            issue.setPrimaryAssigneeId(newAnalyst);
	            issue.setStatus(Issue.Status.OPEN_WITH_ASSIGNEE.toString());
	            //issue.setStatus(Issue.Status.PENDING_CLARIFICATION_APPROVAL.toString());
	            getSession().saveOrUpdate(issue);
	            getSession().update(u);
	            
	            commit();
	            
	            begin();
	            newAnalyst.getPendingIssuesList().add(issue);
	            getSession().update(newAnalyst);
	            commit();
	            
	            
	        } catch (HibernateException e) {
	            rollback();
	            //throw new AdException("Could not create user " + username, e);
	            //throw new AdException("Exception while creating user: " + e.getMessage());
	            e.printStackTrace();
	        }
		}
		
		public boolean compareTimeIsFirstArgumentGreater(Date firstDate, Date secondDate){
			if(firstDate.after(secondDate)){
				return true;
			}
			else{
				return false;
			}
		}
		
		public boolean isPastDeadline(long issueId){
			Issue issue = searchByID(issueId);
			DateTime creationDate = new DateTime(issue.getCreationDate());
			
			int sev = issue.getSeverity();
			int hourToBeAdded=0;
			if(sev == 1){
				hourToBeAdded = issue.getTeamId().getSev1();
			}
			else if (sev ==2){
				hourToBeAdded = issue.getTeamId().getSev2();
			}
			else if(sev ==3){
				hourToBeAdded = issue.getTeamId().getSev3();
			}
			System.out.println("Hours To Be Added : " + hourToBeAdded);
			
			DateTime finalTime = creationDate.plusHours(hourToBeAdded);
			System.out.println("Final Time : "+finalTime.toDate());
			System.out.println("Current Time : "+new Date());
			Date currDate = new Date();
			
			if(compareTimeIsFirstArgumentGreater(finalTime.toDate(),currDate)){
				return true;
			}
			else{
				return false;
			}
			
		}
		
		public Period getDeadline(long issueId){
			
			Issue issue = searchByID(issueId);
			DateTime creationDate = new DateTime(issue.getCreationDate());
			
			int sev = issue.getSeverity();
			int hourToBeAdded=0;
			if(sev == 1){
				hourToBeAdded = issue.getTeamId().getSev1();
			}
			else if (sev ==2){
				hourToBeAdded = issue.getTeamId().getSev2();
			}
			else if(sev ==3){
				hourToBeAdded = issue.getTeamId().getSev3();
			}
			System.out.println("Hours To Be Added : " + hourToBeAdded);
			
			DateTime finalTime = creationDate.plusHours(hourToBeAdded);
			System.out.println("Final Time : "+finalTime.toDate());
			System.out.println("Current Time : "+new Date());
			Date currDate = new Date();
			
			if(compareTimeIsFirstArgumentGreater(finalTime.toDate(), currDate)){
				return printDifference(currDate,finalTime.toDate());
			}
			else{
				return printDifference(finalTime.toDate(),currDate);
			}
			
			
		}
		
		public Period printDifference(Date startDate, Date endDate){
			
			  Interval interval = 
		               new Interval(startDate.getTime(), endDate.getTime());
			  Period period = interval.toPeriod();
			  
		          System.out.printf(
			       "%d years, %d months, %d days, %d hours, %d minutes, %d seconds%n", 
			       period.getYears(), period.getMonths(), period.getDays(),
			       period.getHours(), period.getMinutes(), period.getSeconds());
				return period;
		}
		
		public List<Message> getAllMessagesForIssueId(Long issueId){
			try {
	            begin();
	            Session hibSession = getSession();
	            Criteria crit = hibSession.createCriteria(Message.class);
	            Criteria issuCrit = crit.createCriteria("issue");
	            issuCrit.add(Restrictions.eq("issueId", issueId));
	            crit.addOrder(Order.desc("sentDate"));
	            //q = hibSession.createQuery("from Message where issueId = :id");
	            //q.setLong("id", issueId);
            
	            List<Message> messages = crit.list();
	            commit();
	            return messages;

	            
	        } catch (HibernateException e) {
	            rollback();
	            //throw new AdException("Could not create user " + username, e);
	            //throw new AdException("Exception while creating user: " + e.getMessage());
	            e.printStackTrace();
	     }
		 return null;
		}
	
}
