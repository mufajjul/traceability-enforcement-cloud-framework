/**
 * @file 		PolicyRequest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		PolicyHandlerWS
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.policyhandler.ws.controler;

import javax.servlet.http.HttpServletRequest;

/**
 * This interface define operations for the provStore
 * 
 * @author Mufy
 * 
 */
public interface PolicyRequest<T> {

	//TODO - how do you map request with a policy ????
	String POLICY_ENTRY_TYPE = "PolicyTraceabiity";
	String SERVICE_ENTRY_TYPE = "ServiceTraceability";
	String RESOURCE_TYPE = "XMLResource";

	String CPROV_NAMESEPACE =  "http://labs.orange.com/uk/cprov#";
	String PROV_NAMESPACE = "http://www.w3.org/ns/prov#";
	String EX_NAMESPACE = "http://labs.orange.com/uk/ex#";
	String CONF_NAMESPACE = "http://labs.orange.com/uk/confidenshare#";
	
	
	public T policyRequest(String serviceId, HttpServletRequest request);
	
	public T policyResponse(String serviceId, String requestId);

}
