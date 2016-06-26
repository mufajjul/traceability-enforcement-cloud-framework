/**
 * @file 		CloudUser.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		TConfidenShare
 * @date 		18 05 2013
 * @version 	1.0
 */
package org.provenance.cloudprovenance.confidenshare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Mufy
 * 
 */
@Entity
@Table(name = "CloudUser")
public class CloudUser {

	@Id
	@GeneratedValue
	@Column(name = "userId")
	private Long Id;

	private String userName;
	private String email;
	private String password;
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return Id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		Id = id;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
