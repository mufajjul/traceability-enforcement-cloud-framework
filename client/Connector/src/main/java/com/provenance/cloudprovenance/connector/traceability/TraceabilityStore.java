/**
 * @file 		TraceabilityStore.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		Connector
 * @date 		18 05 2013
 * @version 	1.0
 */

package com.provenance.cloudprovenance.connector.traceability;

import java.net.URL;

/**
 * @author Mufy
 * 
 */
public interface TraceabilityStore {

	public String createNewTraceabilityRecord(String serviceId,
			String traceabilityRecord);

	public String getCurrentTraceabilityRecordId(String serviceId);

	public String getCurrentTraceabilityRecordId(URL serviceUri);

	public int updateTraceabilityRecord(String serviceId,
			String traceabilityRecordId, String traceabilityRecord);

	public abstract String getTraceabilityRecord(String serviceId,
			String traceabilityEntryId);

	public abstract String getTraceabilityRecordElement(String serviceId,
			String traceabilityRecordId, String tracebailityElementId);

}