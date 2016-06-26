/**
 * @file 		ConfidenShareBoService.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		TConfidenShare
 * @date 		18 05 2013
 * @version 	1.0
 */
package org.provenance.cloudprovenance.confidenshare.service;
import org.provenance.cloudprovenance.confidenshare.dao.ConfidenshareDao;
import org.provenance.cloudprovenance.confidenshare.model.CloudUser;
import org.springframework.transaction.annotation.Transactional;

import play.modules.spring.Spring;

/**
 * @author Mufy
 * 
 */
@Transactional(readOnly = true)
public class ConfidenShareBoService implements ConfidenshareBo {

	private ConfidenshareDao confidenshareDao;
	private static ConfidenShareBoService confidenshareService;

	public ConfidenShareBoService() {

		confidenshareDao = Spring.getBeanOfType(ConfidenshareDao.class);
	}

	public static ConfidenShareBoService confidenShareBoServiceInstance() {

		if (confidenshareService == null) {

			confidenshareService = new ConfidenShareBoService();

			return confidenshareService;

		} else {
			return confidenshareService;
		}
	}

	@Override
	public void registerUser(CloudUser user) {

		confidenshareDao.createUser(user);

	}

	@Override
	public boolean checkUser(String userName, String email) {

		return confidenshareDao.isExist(userName, email);

	}

	@Override
	public void deleteUser(CloudUser user) {

	}

}
