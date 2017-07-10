package com.wt.issuetracker.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

//import com.yusuf.spring.exception.AdException;
//import com.yusuf.spring.pojo.Email;
import com.wt.issuetracker.entity.Account;
import com.wt.issuetracker.entity.Issue;
import com.wt.issuetracker.entity.Message;

public class AccountDAO extends DAO {

    public AccountDAO() {
    }

    public Account get(String username, String password){
    	//throws AdException
    	
        try {
            begin();
            Query q = getSession().createQuery("from Account where username = :username and password =:password");
            q.setString("username", username);
            q.setString("password", password);
            Account account = (Account) q.uniqueResult();
            System.out.println("Account in AccountDAO :" + account);
            commit();
            return account;
        } catch (Exception e) {
            rollback();
            //throw new AdException("Could not get user " + username, e);
            e.printStackTrace();
        }
		return null;
    }
	public String checkUserName(String checkUserName){
		 
		begin();
        Session hibSession = getSession();
         
         Query q1  = hibSession.createQuery("from Account where username = :username");
         q1.setString("username",checkUserName);
        if(q1.uniqueResult() != null){
        	commit();
        	return checkUserName;
        }
        else{
        	commit();
        	return "";
        }
        
        /*
		Criteria crit = hibSession.createCriteria(Account.class);
		crit.add(Restrictions.eq("username",checkUserName));
        crit.setProjection(Projections.rowCount());
    	Long countofusers = (Long)crit.uniqueResult();
    	*/
        /*
    	System.out.println("Count of users : "+countofusers);
    	if(countofusers > 0){
    		Query q = hibSession.createQuery("from Account where username = :=username");
    		q.setString("username", checkUserName);
    		Account a = (Account)q.uniqueResult();
    		commit();
    		return a.getUsername();
    	}
    	else{
    		commit();
    		return "";
    	}
    	*/
    	
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

    public void delete(User user)
            throws AdException {
        try {
            begin();
            getSession().delete(user);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw new AdException("Could not delete user " + user.getName(), e);
        }
    }
    */
}