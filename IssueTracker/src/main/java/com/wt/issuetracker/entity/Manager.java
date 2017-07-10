package com.wt.issuetracker.entity;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.springframework.stereotype.Component;

@Component
@Entity
@PrimaryKeyJoinColumn(name="userId")  
public class Manager extends User{
	
	//@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@OneToMany(fetch = FetchType.EAGER)
	private List<Team> teamList;

	public List<Team> getTeamList() {
		return teamList;
	}

	@Resource(name="Team")
	public void setTeamList(List<Team> teamList) {
		this.teamList = teamList;
	}

	
}
