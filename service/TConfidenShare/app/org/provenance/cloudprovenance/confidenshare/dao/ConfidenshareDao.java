/**
 * @file 		ConfidenshareDao.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		TConfidenShare
 * @date 		18 05 2013
 * @version 	1.0
 */
package org.provenance.cloudprovenance.confidenshare.dao;

import org.provenance.cloudprovenance.confidenshare.model.CloudUser;

/**
 * @author Mufy
 * 
 */
public interface ConfidenshareDao {

	public void createUser(CloudUser user);

	public boolean isExist(String userName, String email);

	public void removeUser(CloudUser user);

}
