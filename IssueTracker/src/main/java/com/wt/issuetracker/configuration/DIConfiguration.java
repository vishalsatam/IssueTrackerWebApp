package com.wt.issuetracker.configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.wt.issuetracker.entity.Account;
import com.wt.issuetracker.entity.Analyst;
import com.wt.issuetracker.entity.Issue;
import com.wt.issuetracker.entity.Team;

@Configuration
@ComponentScan(value={"com.wt.issuetracker.entity"})
public class DIConfiguration {
 
    @Bean
    public Account getAccount(){
    	System.out.println("new account");
        return new Account();
    }
    @Bean
    public Set<Issue> PendingIssuesList(){
    	System.out.println("new pending issues list");
        return new HashSet<Issue>();
    }
    
    @Bean
    public ArrayList<Team> Team(){
    	System.out.println("new teamlist");
        return new ArrayList<Team>();
    }
    
    @Bean
	public HashSet<Analyst> Analysts() {
		return new HashSet<Analyst>();
	}
    
    @Bean
    public ArrayList<Issue> WorkBasket(){
    	System.out.println("new workbasket");
    	return new ArrayList<Issue>();
    }
    
}
