/**
 * 
 */
package com.wt.issuetracker.entity;

import java.io.Serializable;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author Vishal
 *
 */

@Component
@Entity  
@Table(name = "usertable")  
@Inheritance(strategy=InheritanceType.JOINED)   
//@DiscriminatorColumn(name="type",discriminatorType=DiscriminatorType.STRING)  
//@DiscriminatorValue(value="user")  
public class User implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="userId")
	private long userId;
	
	@NotEmpty(message = "First Name cannot be empty")
	@Pattern(regexp="^[a-zA-Z][a-zA-Z0-9. -]*$",message="First Name can contain only alphanumeric and . and - characters and must start with a letter")
	@Column(name="firstName")
	private String firstName;
	
	@NotEmpty(message = "Last Name cannot be empty")
	@Pattern(regexp="^[a-zA-Z][a-zA-Z0-9. -]*$",message="Last Name can contain only alphanumeric and . and - characters and must start with a letter")
	@Column(name="lastName")
	private String lastName;
	
	@NotEmpty(message = "Email cannnot be empty")
	@Email(message = "Email invalid")
	@Column(name="emailId")
	private String emailId;
	
	@Pattern(regexp="^[0-9]{10}$",message="Phone number can contain numbers and must be 10 characters")
	@NotEmpty(message = "Phone Number cannot be empty")
	//@Pattern(regexp = "[0-9]\\d*")
	//@Digits(integer=10,fraction=0,message = "Phone Number must be 10 digits")
	//@NotNull @Min(10) @Max(10)
	@Column(name="phoneNumber")
	private String phoneNumber;
	
	@NotEmpty(message = "Role cannot be empty")
	@Column(name="type")
	private String role;
	
	@Valid
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
	@JoinColumn(name="userId")
	protected Account account;
	

	@OneToMany(fetch = FetchType.EAGER)
	private Set<Issue> pendingIssuesList;
	
	
	//Constructor
	public User(){
		
	}
	
	
	public Set<Issue> getPendingIssuesList() {
		return pendingIssuesList;
	}
	
	@Resource(name="PendingIssuesList")
	public void setPendingIssuesList(Set<Issue> pendingIssuesList) {
		this.pendingIssuesList = pendingIssuesList;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Account getAccount() {
		return account;
	}

	@Autowired
	public void setAccount(Account account) {
		this.account = account;
	}
	
	
	
}
