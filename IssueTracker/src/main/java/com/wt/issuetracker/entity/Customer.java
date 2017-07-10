package com.wt.issuetracker.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import org.springframework.stereotype.Component;

@Component
@Entity
@PrimaryKeyJoinColumn(name="userId")  
public class Customer extends User{

	@OneToMany(fetch = FetchType.EAGER)
	private Set<Issue> issuesRaised;

	public Set<Issue> getIssuesRaised() {
		return issuesRaised;
	}

	public void setIssuesRaised(Set<Issue> issuesRaised) {
		this.issuesRaised = issuesRaised;
	}


	
	
}
