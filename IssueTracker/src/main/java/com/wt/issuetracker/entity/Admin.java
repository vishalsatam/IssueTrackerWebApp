package com.wt.issuetracker.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import org.springframework.stereotype.Component;

@Component
@Entity
@PrimaryKeyJoinColumn(name="userId")  
public class Admin extends User{
	
	
	
}
