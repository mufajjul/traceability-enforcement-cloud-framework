/**
 * @file 		PolicyTraceability.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		StorageController
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.storagecontroller.presistence.traceabilitystore.policy;

/** 
* @author Mufy
* 
*/
public interface PolicyTraceability<T> {

	public boolean createRequestInstance(String serviceId,
			String traceabilityType, String instanceId,
			T entryItem);

	public boolean createResponseInstance(String serviceId,
			String traceabilityType, String instanceId,
			T entryItem);
	
}
