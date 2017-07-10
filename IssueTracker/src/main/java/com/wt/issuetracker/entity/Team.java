/**
 * 
 */
package com.wt.issuetracker.entity;

import java.io.Serializable;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

/**
 * @author Vishal
 *
 */
@Component
@Entity
@Table(name = "team")
public class Team implements Serializable{
	@Id
	@GeneratedValue
	@Column(name="teamId")
	private long teamId;
	@Column(name="teamName")
	private String teamName;
	
		
	@OneToMany(fetch=FetchType.EAGER)
	private Set<Analyst> analysts;
	
	@ManyToOne
	@JoinColumn(name="managerId")
	private Manager manager;
	
	@OneToMany(fetch=FetchType.EAGER)
	private Set<Issue> workBasket;
	

	@Column
	private int sev1;
	@Column
	private int sev2;
	@Column
	private int sev3;
	
	
	
	
	public int getSev1() {
		return sev1;
	}

	public void setSev1(int sev1) {
		this.sev1 = sev1;
	}

	public int getSev2() {
		return sev2;
	}

	public void setSev2(int sev2) {
		this.sev2 = sev2;
	}

	public int getSev3() {
		return sev3;
	}

	public void setSev3(int sev3) {
		this.sev3 = sev3;
	}

	public Set<Issue> getWorkBasket() {
		return workBasket;
	}

	@Resource(name="WorkBasket")
	public void setWorkBasket(Set<Issue> workBasket) {
		this.workBasket = workBasket;
	}

	public long getTeamId() {
		return teamId;
	}

	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Set<Analyst> getAnalysts() {
		return analysts;
	}
	@Resource(name="Analysts")
	public void setAnalysts(Set<Analyst> analysts) {
		this.analysts = analysts;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}
	

	
}
