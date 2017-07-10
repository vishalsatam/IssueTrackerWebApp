package com.wt.issuetracker.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

//import com.yusuf.spring.exception.AdException;
//import com.yusuf.spring.pojo.Email;
import com.wt.issuetracker.entity.User;
import com.wt.issuetracker.entity.Analyst;
import com.wt.issuetracker.entity.Customer;
import com.wt.issuetracker.entity.Issue;
import com.wt.issuetracker.entity.Manager;

import javassist.bytecode.Descriptor.Iterator;

public class UserDAO extends DAO {

    public UserDAO() {
    }

    public boolean checkFirstAdmin(){
    	try {
            begin();
            Criteria crit = getSession().createCriteria(User.class);
            crit.add( Restrictions.eq("role","admin"));
            crit.setProjection(Projections.rowCount());
            Long count = (Long)crit.uniqueResult();
            System.out.println("Number of Admins : "+ count);
            commit();
            if(count<=0){
            	return false;
            }
            
        } catch (Exception e) {
            rollback();
            //throw new AdException("Could not get user " + username, e);
            e.printStackTrace();
        }
		return true;
    }
    
    public List getUsersWithoutATeam(){
    	try {
            begin();
            Query q = getSession().createQuery("from User where teamId = null and role = 'analyst'");

            List userList = q.list();
            commit();
            return userList;
        } catch (Exception e) {
            rollback();
            //throw new AdException("Could not get user " + username, e);
            e.printStackTrace();
        }
		return null;
    }
    
    public List<User> getUserList(){
    	//throws AdException
    	
        try {
            begin();
            Query q = getSession().createQuery("from User");

            List userList = q.list();
            commit();
            return userList;
        } catch (Exception e) {
            rollback();
            //throw new AdException("Could not get user " + username, e);
            e.printStackTrace();
        }
		return null;
    }
    
    public Set<Issue> getPendingIssues(Long userId){
    	User u = searchById(userId);
    	return u.getPendingIssuesList();
    }
    
    public void saveUser(User user){
    	System.out.println("Save User : " + user);
    	try{
    		begin();
    		if(user instanceof Analyst){
    			user = (Analyst)user;
    		}
    		if(user instanceof Customer){
    			user = (Customer)user;
    		}
    		if(user instanceof Manager){
    			user = (Manager)user;
    		}
    		getSession().saveOrUpdate(user);
    		commit();
    	}
    	catch(HibernateException e){
    		e.printStackTrace();
    	}
    	
    }
    
    public User searchById(Long userid){
    	//throws AdException
    	System.out.println("Search By ID");
        try {
            begin();
            Query q = getSession().createQuery("from User where userId = :id");
            q.setLong("id", userid);
            User user = (User)q.uniqueResult();
            commit();
            return user;
        } catch (Exception e) {
            rollback();
            //throw new AdException("Could not get user " + username, e);
            e.printStackTrace();
        }
		return null;
    }
    
    /*
    public User create(String username, String password,String emailId)
            throws AdException {
        try {
            begin();
            Email email = new Email(emailId);
            User user = new User(username,password);
            user.setEmail(email);
            email.setUser(user);
            getSession().save(user);
            commit();
            return user;
        } catch (HibernateException e) {
            rollback();
            //throw new AdException("Could not create user " + username, e);
            throw new AdException("Exception while creating user: " + e.getMessage());
        }
    }
	*/
    public void delete(User user){
            //throws AdException {
        try {
            begin();
            getSession().delete(user);
            commit();
        } catch (HibernateException e) {
            rollback();
            System.out.println("Error while deleting user");
            //throw new AdException("Could not delete user " + user.getName(), e);
        }
    }
    
    public void delete(String userId){
        try {
            begin();
            Query q = getSession().createQuery("from User where userId = :id");
            q.setString("id", userId);
            User userToDelete = (User) q.uniqueResult();
            getSession().delete(userToDelete);
            commit();
        } catch (HibernateException e) {
            rollback();
            System.out.println("Error while deleting user");
            //throw new AdException("Could not delete user " + user.getName(), e);
        }
    }
    
    public void deleteBulk(ArrayList<User> userList){
        try {
            begin();
            /*
            	Query q = getSession().createQuery("delete from User where id in :id");
            	q.setParameterList("id", userList);
            	q.executeUpdate();
            */
            for(User user : userList){
            	getSession().delete(user);
            }
            commit();
        } catch (HibernateException e) {
            rollback();
            System.out.println("Error while deleting user");
            //throw new AdException("Could not delete user " + user.getName(), e);
        }
    }
    
	public List<Issue> getMyIssues(Long userId){
		//User u = searchById(userid);
        User u = searchById(userId);
		try {
            begin();
            
            	Query q = getSession().createQuery("from Issue where issueRaisedBy = :user AND status <> :closedByReq AND status <> :closedByTeam");
            	q.setParameter("user", u);
            	q.setParameter("closedByReq", Issue.Status.CLOSED_BY_REQUESTOR.toString());
            	q.setParameter("closedByTeam", Issue.Status.CLOSED_BY_TEAM.toString());            	
            	
            	List<Issue> issueList = q.list();
            
            commit();
            
            return issueList;
        } catch (HibernateException e) {
            rollback();
            System.out.println("Error while deleting user");
            //throw new AdException("Could not delete user " + user.getName(), e);
        }
		return null;
	}
	
	public List<Issue> getClosedIssues(Long userId){
		//User u = searchById(userid);
        User u = searchById(userId);
		try {
            begin();
            
            	Query q = getSession().createQuery("from Issue where (issueRaisedBy = :user or primaryAssigneeId = :primaryAssignee) AND (status = :closedByReq or status = :closedByTeam)");
            	q.setParameter("user", u);
            	q.setParameter("primaryAssignee", u);
            	q.setParameter("closedByReq", Issue.Status.CLOSED_BY_REQUESTOR.toString());
            	q.setParameter("closedByTeam", Issue.Status.CLOSED_BY_TEAM.toString());            	
            	
            	List<Issue> issueList = q.list();
            
            commit();
            
            return issueList;
        } catch (HibernateException e) {
            rollback();
            System.out.println("Error while deleting user");
            //throw new AdException("Could not delete user " + user.getName(), e);
        }
		return null;
	}
	

    
}