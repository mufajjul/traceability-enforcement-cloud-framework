/*
 * @(#) CProvRecordSeven.java       1.1 19/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.requirement_seven.test;

import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.service.api.impl.CprovXmlProvenance;
import com.provenance.cloudprovenance.service.traceability.api.ServiceXmlDocumentTraceability;

/**
 * Provenance graph to validate against the following policy: The storage of a new
 * sensitive file (fileA) needs to reside in the same region as the registration
 * of the user (userA), otherwise it is denied.
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
 */
public class CProvRecordSeven {

	@Autowired
	private ServiceXmlDocumentTraceability<String> cProvApi;

	public CProvRecordSeven(ServiceXmlDocumentTraceability<String> cProvApi) {
		this.cProvApi = cProvApi;

	}

	public void genPreStatements() {

		cProvApi.setMaxStatementPerDocument(24);

		// agent to register
		String agent1Id = cProvApi.Agent("Bob", "Bob", "Smith");

		// registration process
		String cprocess1Id = cProvApi.cProcess("login",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000login",
				"192.168.1.34", null);

		String wicb1Id = cProvApi.wasInitiallyCalledBy(null, cprocess1Id,
				agent1Id, "", "Laptop", "wifi");

		String cres1Id = cProvApi.cResource("username", "text",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-username",
				"192.168.1.34", true, null, null, "restricted", 1.0f, null);

		String cres2Id = cProvApi.cResource("password", "text",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-password",
				"192.168.1.34", true, null, null, "restricted", 1.0f, null);

		String used1Id = cProvApi.used(null, cprocess1Id, cres1Id, null);

		String used2Id = cProvApi.used(null, cprocess1Id, cres2Id, null);

		String cres3Id = cProvApi.cResource("loginSuccessful", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-password",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres3Id, cprocess1Id, null, null);

			// creation process
		String cprocess2Id = cProvApi.cProcess("create",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-00create",
				"192.168.1.34", null);

		String wicb2Id = cProvApi.wasInitiallyCalledBy(null, cprocess2Id,
				agent1Id, "", "Laptop", "wifi");

		String cres5Id = cProvApi.cResource("document1", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		// Add ownership
		cProvApi.hadOwnership(null, cres5Id, agent1Id, "originator");

		cProvApi.wasVirtualizedBy(null, cres5Id, cprocess2Id, null, null);

		// add transition
		String cres4Id = cProvApi.transition(null, "source", null, null, null,
				"EU", "event1");
		cProvApi.hadTransitionState_C(null, agent1Id, cres4Id, null);

		String cres6Id = cProvApi.transition(null, "source", null, null, null,
				"EU", "event1");
		cProvApi.hadTransitionState_B(null, cprocess1Id, cres6Id, null);

		// cres3Id

		String cres7Id = cProvApi.transition(null, "source", null, null, null,
				"EU", "event1");
		cProvApi.hadTransitionState_A(null, cres3Id, cres7Id, null);

		String cres8Id = cProvApi.transition(null, "source", null, null, null,
				"US", "event1");
		cProvApi.hadTransitionState_B(null, cprocess2Id, cres8Id, null);

		String cres9Id = cProvApi.transition(null, "source", null, null, null,
				"US", "event1");
		cProvApi.hadTransitionState_B(null, cres5Id, cres9Id, null);

		//
		/***************** Share fail *********************************/
	}

	public void genPostStatements() {

	}
}