/*
 * @(#) ServiceTraceability.java       1.1 15/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.storagecontroller.presistence.traceabilitystore.service;

/**
 * This interface define operations for the provStore
 * 
 * @version 1.1 15 Aug 2016
 * @author Mufy
 * @Module StorageController
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
