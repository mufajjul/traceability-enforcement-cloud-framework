/**
 * @file 		ConfidenshareDaoImpl.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		TConfidenShare
 * @date 		18 05 2013
 * @version 	1.0
 */
package org.provenance.cloudprovenance.confidenshare.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.provenance.cloudprovenance.confidenshare.model.CloudUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * @author Mufy
 * 
 */

@Repository("ConfidenshareDao")
public class ConfidenshareDaoImpl extends HibernateDaoSupport implements
		ConfidenshareDao {

	static Logger logger = Logger.getLogger("ConfidenshareDaoImpl");

	@SuppressWarnings("unused")
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void createUser(CloudUser user) {

		this.getHibernateTemplate().save(user);
	}

	@Override
	public boolean isExist(String userName, String email) {

		
		logger.info(" User name: "+userName+ " email: "+email );
		
		//Query qry=session.createQuery("select e.employeeId,e.employeeName from Employee e where e.deptNumber=:p1");

		String hql = "SELECT cloudUser FROM CloudUser cloudUser WHERE cloudUser.userName ='"
				+ userName + "' OR cloudUser.email = '" + email + "' ";
		
		logger.info("SQL statement: "+ hql);
			
		List result = getHibernateTemplate().find(hql);

		if (result.size()==0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void removeUser(CloudUser user) {

	}
}
