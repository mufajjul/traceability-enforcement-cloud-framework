/**
 * @file 		ServiceCompliance.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.policy.api;

/**
 * @author Mufy
 *
 */
public interface ServiceCompliance<T> {

	/* construct a policy request with single user, resource and process */
	public T constructRequest(String requestUserId, String resourceId,
			String processId, String environmentId);

	/* construct a policy request with multiple users, resources and processes */
	public T constructRequest(String requestUserIds[], String resourceIds[],
			String processIds[], String environmentId);

	/* obtain a response */
	public T getResponse();

}
