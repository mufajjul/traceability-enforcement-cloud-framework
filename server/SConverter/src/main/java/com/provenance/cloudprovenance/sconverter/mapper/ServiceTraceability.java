/*
 * @(#) CprovObjectTraceability.java       1.1 18/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.sconverter.mapper;

import java.util.Date;

/**
 * This interface defines methods for nodes and edges for traceability model
 * 
 * @version 1.1 18 Aug 2016
 * @author Mufy
 * @Module SConverter
 */
public interface ServiceTraceability<T> {
	/**
	 * T = defines type type, it can be XML or JSON or String
	 */

	public final String PREVIOUS_ID = "link_previous";
	public final String AUTO_ID = "auto_gen";

	public final String UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX = "http://labs.orange.com/uk/cprovd#";
	public final String UNIQUE_IDENTIFIER_NS_PROVD_PREFIX = "provd";

	public final String UNIQUE_IDENTIFIER_NS_PROV_SUFFIX = "http://www.w3.org/ns/prov#";
	public final String UNIQUE_IDENTIFIER_NS_PROV_PREFIX = "prov";
	
	// Nodes
	public T pResource(String id, String resType, String ip, String MAC,
			String hostType, Date timeStamp, String restrictionType, float trustValue);

	public T cResource(String id, String resType, String userCloudRef,
			String vResourceRef, String pResourceRef, boolean isReplicable,
			Date TTL, Date timeStamp, String restrictionType, float trustValue, String des);

	public T cProcess(String id, String userCloudRef, String vProcessRef,
			String pProcessRef, Date timeStamp);

	public T transition(String id, String state, String country,
			String latitude, String longitude, String region);

	// Node from Prov
	public T Agent(String id, String name, String description);

	// Edges
	public T wasImplicitCall(String id, String informed, String infromant,
			String type, String callComm, String callMedium, String callNetwork);

	// TODO: Identify the differences between the both
	public T wasExplicitCall(String id, String informed, String informant,
			String type, String callComm, String callMedium, String callNetwork);

	public T wasRecurrentCall(String id, String informed, String informand,
			String type, String callComm, long timeout);

	public T hadOwnership(String id, String resource, String agent,
			String ownershipType);

	// TODO:- What is generation, explore more
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

	public T getCurrentTraceabilityDocument();
	
}