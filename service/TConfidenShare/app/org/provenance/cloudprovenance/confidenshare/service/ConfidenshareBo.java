/**
 * @file 		ConfidenshareBo.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		TConfidenShare
 * @date 		18 05 2013
 * @version 	1.0
 */
package org.provenance.cloudprovenance.confidenshare.service;

import org.provenance.cloudprovenance.confidenshare.model.CloudUser;

/**
 * @author Mufy
 *
 */
public interface ConfidenshareBo {

	public void registerUser (CloudUser user);
	
	public boolean checkUser (String userName, String email);
	
	public void deleteUser (CloudUser user);
}
