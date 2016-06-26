/**
 * @file 		ServiceTraceability.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		StorageController
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.storagecontroller.presistence.traceabilitystore.service;

/**
 * This interface define operations for the provStore
 * 
 * @author Mufy
 * 
 */
public interface ServiceTraceability<T> {

	// Create a traceability record
	public boolean createTraceabilityInstance(String serviceId,
			String traceabilityType, String instanceId);

	// get all traceability records
	public T[] getAllTraceabilityInstances(String serviceId,
			String traceabilityType);

	// get a traceability record
	public T getTraceabilityInstance(String serviceId, String traceabilityType,
			String instanceId);

	// create a traceability entry in a record
	public boolean createTraceabilityEntry(String serviceId,
			String traceabilityType, String instanceId, String entryType,
			T entryItem);

	public boolean updateTraceabilityEntry(String serviceId,
			String traceabilityType, String instanceId, String entryType,
			T entryItem);

	// Delete operation not considered (for future use)
	public boolean removeTraceabiltiltyInstance(String serviceId,
			String traceabilityType, String instanceId);

	public int currentTraceabilityRecordSize(String serviceId,
			String traceabilityType, String instanceId);

}
