/*
 * @(#) ServiceTraceability.java       1.1 19/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.traceability.api;

import java.util.Date;

/**
 * This interface defines methods for nodes and edges for traceability model
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
 */
public interface ServiceTraceability<T> {

	/**
	 * T = defines type, it can be of XML, JSON or String
	 */
	// Nodes
	public T pResource(String id, String resType, String ip, String MAC,
			String hostType, Date timeStamp, String restrictionType,
			float trustValue);

	public T cResource(String id, String resType, String userCloudRef,
			String vResourceRef, String pResourceRef, boolean isReplicable,
			Date TTL, Date timeStamp, String restrictionType, float trustValue,
			String des);

	public T cProcess(String id, String userCloudRef, String vProcessRef,
			String pProcessRef, Date timeStamp);

	public T transition(String id, String state, String country,
			String latitude, String longitude, String region, String eventId);

	// Node from Prov
	public T Agent(String id, String name, String description);

	// Edges
	public T wasImplicitlyCalledBy(String id, String informed,
			String infromant, String type, String callComm, String callMedium,
			String callNetwork);

	public T wasExplicitlyCalledBy(String id, String informed,
			String informant, String type, String callComm, String callMedium,
			String callNetwork);

	public T wasRecurrentlyCalledBy(String id, String informed,
			String informand, String type, String callComm, long timeout);

	public T hadOwnership(String id, String resource, String agent,
			String ownershipType);

	public T wasRepresentationOf(String id, String generatedResource,
			String usedResource, String processInvolved, String generation,
			String usage, String method);

	public T wasReferenceOf(String id, String generatedResource,
			String usedResource, String processInvolved, String generation,
			String usage, String method, String type);

	public T wasVirtualizedBy(String id, String generatedResource,
			String processInvolved, Date time, String purpose);

	public T hadTransitionState_A(String id, String generatedResource,
			String transResource, String method);

	public T hadTransitionState_B(String id, String generatedActivity,
			String transResource, String method);

	public T hadTransitionState_C(String id, String generatedAgent,
			String stateResource, String method);

	// edge from Prov
	public T used(String id, String processInvolved, String generatedResource,
			Date date);

	public T wasInitiallyCalledBy(String id, String processInvolved,
			String involvedAgent, String plan, String accessMedium,
			String accessNetwork);

}