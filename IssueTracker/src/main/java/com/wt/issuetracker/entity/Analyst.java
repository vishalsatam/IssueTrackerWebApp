package com.wt.issuetracker.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Entity
//@DiscriminatorValue("analyst")
@PrimaryKeyJoinColumn(name="userId")  
public class Analyst extends User{

	@Column
	private int level;
	
	@OneToMany(fetch = FetchType.EAGER)
	private Set<Issue> workList;
	
	@ManyToOne
	@JoinColumn(name="teamId")
	private Team teamId;

	/*
	@Override
	@Autowired
	public void setAccount(Account account) {
		this.account = account;
	}
	*/
	
	public Team getTeamId() {
		return teamId;
	}

	public void setTeamId(Team teamId) {
		this.teamId = teamId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Set<Issue> getWorkList() {
		return workList;
	}

	public void setWorkList(Set<Issue> workList) {
		this.workList = workList;
	}

	
	
}
