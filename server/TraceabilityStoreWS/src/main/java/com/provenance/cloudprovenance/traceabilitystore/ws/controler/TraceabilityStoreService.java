/*
 * @(#) TraceabilityStoreService.java       1.1 13/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.traceabilitystore.ws.controler;

import javax.servlet.http.HttpServletRequest;

/**
 * This interface defines a list of operations for interacting with the
 * traceability store
 *
 * @version 1.1 13 Aug 2016
 * @author Mufy
 * @Module TraceabilityStoreWS
 */
public interface TraceabilityStoreService<T> {

    String POLICY_ENTRY_TYPE = "PolicyTraceabiity";
	String SERVICE_ENTRY_TYPE = "ServiceTraceability";
	String RESOURCE_TYPE = "XMLResource";

	String CPROV_NAMESEPACE = "http://labs.orange.com/uk/cprov#";
	String PROV_NAMESPACE = "http://www.w3.org/ns/prov#";
	String EX_NAMESPACE = "http://labs.orange.com/uk/ex#";
	String CONF_NAMESPACE = "http://labs.orange.com/uk/confidenshare#";

	// Create a traceability record
	public T createTraceabilityDocument(String serviceId,
			String traceabilityType, HttpServletRequest request);

	public T addTraceabilityDocumentEntries(String serviceId,
			String traceabilityType, String documentId,
			HttpServletRequest request);

	public T getTraceabilityDocumentId(String serviceId,
			String traceabilityType, HttpServletRequest request);

	// get a traceability record
	public T getTraceabilityDocument(String serviceId, String traceabilityType,
			String documentId);

	public T getTraceabilityDocumentElement(String serviceId,
			String traceabilityType, String documentId, String elementId);

}
