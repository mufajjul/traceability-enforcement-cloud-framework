/*
 * @(#) TraceabilityStore.java       1.1 13/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
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