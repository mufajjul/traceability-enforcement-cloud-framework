/**
 * @file 		ServiceTraceabilityCallback.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.traceability.api;

/**
 * @author Mufy
 *
 */
public interface ServiceTraceabilityCallback<T> {

	public T traceabilityStored();
	
	
}
