/**
 * @file 		TraceabilityStoreService.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		TraceabilityModel
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.traceabilitystore.ws.controler;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

/**
 * This interface define operations for the provStore
 * 
 * @author Mufy
 * 
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

	// get all traceability records
	// public T[] getAllTraceabilityInstances(String serviceId,
	// String traceabilityType);

	// create a traceability entry in a record
	// public boolean createTraceabilityEntry(String serviceId,
	// String traceabilityType, String instanceId, String entryType,
	// T entryItem);

	// Delete operation not considered (for future use)
	// public boolean removeTraceabiltiltyInstance(String serviceId,
	// String traceabilityType, String instanceId);
}
