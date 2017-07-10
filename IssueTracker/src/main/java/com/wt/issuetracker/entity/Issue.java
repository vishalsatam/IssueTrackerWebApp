	/**
 * 
 */
package com.wt.issuetracker.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Vishal
 *
 */

@Entity
@Table(name="issue")
public class Issue implements Serializable{
	
	public static enum Status {
	    NEW("New"),
	    OPEN_WITH_ASSIGNEE("Open With Assignee"),
	    PENDING_CLARIFICATION("Pending Clarification"),
	    PENDING_REVIEW("Pending Review"), 
	    PENDING_CONSULTATION ("Pending Consultion"),
	    PENDING_CLARIFICATION_APPROVAL ("Pending Clarification Approval"),
	    PENDING_CLOSURE_APPROVAL ("Pending Closure Approval"),
	    CLOSED_BY_TEAM("Closed By Team"),
	    CLOSED_BY_REQUESTOR("Closed By Requestor");
	    
	    private String displayStatus;

		Status(String displayStatus) {
	        this.displayStatus = displayStatus;
		}
		 @Override public String toString() { return displayStatus; }
	}
	
	@Id
	@GeneratedValue
	@Column(name="issueId")
	private long issueId;

	//@NotEmpty(message = "Description cannot be empty")
	@Column(name="description")
	private String description;
	
	//@NotEmpty(message = "Title cannot be empty")
	@Column(name="title")
	private String title;
	
	//@Min(1)
	//@Max(3)
	@Column(name="severity")
	private int severity;
	
	@Column(name="status")
	private String status;

	@ManyToOne
	@JoinColumn(name="teamId")
	private Team teamId;
	
	
	@ManyToOne
	@JoinColumn(name="issueCurrentlyWithId")
	private User issueCurrentlyWithId;
	
	@ManyToOne
	@JoinColumn(name="issueRaisedBy")
	private Customer issueRaisedBy;

	@OneToMany(mappedBy="issue",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Message> messageList;

	@ManyToOne
	@JoinColumn(name="primaryAssigneeId")
	private Analyst primaryAssigneeId;

	//SLA COLUMNS
	
	@Column(name="creationDate")
	private Date creationDate;
	/*
	@Column
	private Date timeElapsed;
	@Column
	private Date statusChangedDate;
	*/
	public Customer getIssueRaisedBy() {
		return issueRaisedBy;
	}

	public void setIssueRaisedBy(Customer issueRaisedBy) {
		this.issueRaisedBy = issueRaisedBy;
	}

	public Analyst getPrimaryAssigneeId() {
		return primaryAssigneeId;
	}

	public void setPrimaryAssigneeId(Analyst primaryAssigneeId) {
		this.primaryAssigneeId = primaryAssigneeId;
	}
	
	
	
	public Team getTeamId() {
		return teamId;
	}

	public void setTeamId(Team teamId) {
		this.teamId = teamId;
	}

	public long getIssueId() {
		return issueId;
	}

	public void setIssueId(long issueId) {
		this.issueId = issueId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}




	public User getIssueCurrentlyWithId() {
		return issueCurrentlyWithId;
	}

	public void setIssueCurrentlyWithId(User issueCurrentlyWithId) {
		this.issueCurrentlyWithId = issueCurrentlyWithId;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	
	
	
	
}
