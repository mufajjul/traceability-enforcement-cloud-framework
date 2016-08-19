/*
 * @(#) ServiceCompliance.java       1.1 19/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.policy.api;

/**
 * This is the service compliance API
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
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
