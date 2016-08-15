/*
 * @(#) PolicyTraceability.java       1.1 15/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.storagecontroller.presistence.traceabilitystore.policy;

/**
 * This interface defines the policy traceability API methods
 * 
 * @version 1.1 15 Aug 2016
 * @author Mufy
 * @Module StorageController
 */
public interface PolicyTraceability<T> {

	//Method for creating a request instance
	public boolean createRequestInstance(String serviceId,
			String traceabilityType, String instanceId,
			T entryItem);

	//Method for creating a response instance
	public boolean createResponseInstance(String serviceId,
			String traceabilityType, String instanceId,
			T entryItem);
	
}
