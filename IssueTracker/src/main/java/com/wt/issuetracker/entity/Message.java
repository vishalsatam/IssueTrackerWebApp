/**
 * 
 */
package com.wt.issuetracker.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * @author Vishal
 *
 */
@Entity
@Table(name="messages")
public class Message {
	
	public static enum ActionToBePerformed {
	    SENT_FOR_REVIEW("Sent for Review"),
	    SENT_FOR_CLARIFICATION("Sent for Clarification"),
	    CLOSED_BY_ANALYST("Closed By Analyst"),
	    PENDING_REVIEW("Pending Review"), 
	    SENT_FOR_CONSULTATION ("Sent for Consultion"),
	    CLOSED_BY_TEAM("Closed By Team"),
	    CLOSED_BY_REQUESTOR("Closed By Requestor"),
	    APPROVED_CLARIFICATION("Approved for Clarification"),
	    APPROVED_CLOSURE("Approved for Closure"),
	    REJECTED_CLARIFICATION("Rejected Clarification Request"),
	    REJECTED_CLOSURE("Rejected Closure Request"),
	    PROVIDED_CLARIFICATION("Provided Clarification"),
	    REOPEN("Re-Opened"),
	    TRANSFERRED("Transferring Issue"),
		COMMENT("Comment Posted");
	    
	    private String actionToBePerformed;

	    ActionToBePerformed(String actionToBePerformed) {
	        this.actionToBePerformed = actionToBePerformed;
		}
		 @Override public String toString() { return actionToBePerformed; }
	}
	
	@Column(name="description")
	private String description;
	
	@Id
	@GeneratedValue
	@Column(name="messageId")
	private long messageId;
	@Column(name="senderId")
	private long senderId;
	@Column(name="senderName")
	private String senderName;
	@Column(name="attachment")
	private String attachment;
	@Column(name="sentDate")
	private Date sentDate;
	@Column(name="actionPerformed")
	private String actionPerformed;
	
	@ManyToOne
	@JoinColumn(name="issueId")
	private Issue issue;

	
	
	public String getActionPerformed() {
		return actionPerformed;
	}

	public void setActionPerformed(String actionPerformed) {
		this.actionPerformed = actionPerformed;
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	
	
}
