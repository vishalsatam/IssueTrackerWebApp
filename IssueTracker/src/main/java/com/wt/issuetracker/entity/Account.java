/**
 * 
 */
package com.wt.issuetracker.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;


/**
 * @author Vishal
 *
 */
@Component
@Entity
@Table(name = "accounttable")
public class Account implements Serializable{

	@Id
	@Column(name="userId")
	@GeneratedValue(generator="gen")
    @GenericGenerator(name="gen", strategy="foreign",parameters=@Parameter(name="property", value="user"))
	private long id;
	
	public Account(){
		System.out.println("inside account");
	}
	
	@NotEmpty(message="UserName cannot be empty")
	@Pattern(regexp="^[a-zA-Z][a-zA-Z0-9.@]*$",message="Username can only contain alphanumeric,.,and @ characters and must begin with a character")
	@Column(name="username",unique = true)
	private String username;
	
	@NotEmpty(message="Password cannot be empty")
	@Column(name="password")
	private String password;
	
	@NotEmpty(message="Status cannot be empty")
	@Column(name="status")
	private String status;
	
	@OneToOne
	@PrimaryKeyJoinColumn
	private User user;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
