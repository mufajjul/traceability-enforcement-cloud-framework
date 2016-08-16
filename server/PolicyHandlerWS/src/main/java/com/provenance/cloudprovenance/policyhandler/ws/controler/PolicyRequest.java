/*
 * @(#) PolicyRequest.java       1.1 16/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.policyhandler.ws.controler;

import javax.servlet.http.HttpServletRequest;

/**
 * This interface define operations for the provStore
 * 
 * @version 1.1 16 Aug 2016
 * @author Mufy
 * @Module PolicyHandlerWS
 */
public interface PolicyRequest<T> {

	
	//TODO - use a property file
	String POLICY_ENTRY_TYPE = "PolicyTraceabiity";
	String SERVICE_ENTRY_TYPE = "ServiceTraceability";
	String RESOURCE_TYPE = "XMLResource";

	String CPROV_NAMESEPACE =  "http://labs.orange.com/uk/cprov#";
	String PROV_NAMESPACE = "http://www.w3.org/ns/prov#";
	String EX_NAMESPACE = "http://labs.orange.com/uk/ex#";
	String CONF_NAMESPACE = "http://labs.orange.com/uk/confidenshare#";
	
	
	// Method for handling policy request 
	public T policyRequest(String serviceId, HttpServletRequest request);
	
	//Method for handling policy response
	public T policyResponse(String serviceId, String requestId);

}
