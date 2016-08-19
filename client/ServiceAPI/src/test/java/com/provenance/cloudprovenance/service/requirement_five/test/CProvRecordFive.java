/*
 * @(#) CProvRecordFive.java       1.1 19/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.requirement_five.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.provenance.cloudprovenance.service.traceability.api.ServiceXmlDocumentTraceability;

/**
 * A provenance graph to validate against the following policy: A user (userA)
 * logged in from an authorised region (EU) cannot share a ‘confidential’ or
 * ‘restricted’ file with another user (userB) logged in from an unauthorised
 * region (non-EU)
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans.xml")
public class CProvRecordFive {

	@Autowired
	private ServiceXmlDocumentTraceability<String> cProvApi;

	public CProvRecordFive(ServiceXmlDocumentTraceability<String> cProvApi) {
		this.cProvApi = cProvApi;
	}

	public void genPreStatements() {

		cProvApi.setMaxStatementPerDocument(44);

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
		String cprocess2Id = cProvApi.cProcess("share",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-00share",
				"192.168.1.34", null);

		String wicb2Id = cProvApi.wasInitiallyCalledBy(null, cprocess2Id,
				agent1Id, "", "Laptop", "wifi");

		String cres5Id = cProvApi.cResource("document1", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		cProvApi.hadOwnership(null, cres5Id, agent1Id, "originator");

		cProvApi.used(null, cprocess2Id, cres5Id, null);

		// phil login
		String agent11Id = cProvApi.Agent("Phil", "Phil", "Smith");

		// registration process
		String cprocess11Id = cProvApi.cProcess("login2",
				"=phil[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000login",
				"192.165.1.34", null);

		String wicb11Id = cProvApi.wasInitiallyCalledBy(null, cprocess11Id,
				agent11Id, "", "Laptop", "wifi");

		String cres11Id = cProvApi.cResource("username2", "text",
				"=phil[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-username",
				"192.165.1.34", true, null, null, "restricted", 1.0f, null);

		String cres21Id = cProvApi.cResource("password2", "text",
				"=phil[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-password",
				"192.165.1.34", true, null, null, "restricted", 1.0f, null);

		String used11Id = cProvApi.used(null, cprocess11Id, cres11Id, null);

		String used21Id = cProvApi.used(null, cprocess11Id, cres21Id, null);

		String cres31Id = cProvApi.cResource("loginSuccessful2", "data",
				"=phil[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-password",
				"192.165.1.34", true, null, null, "general", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres31Id, cprocess11Id, null, null);

		// Notification ....
		String cprocess3Id = cProvApi.cProcess("notification",
				"=phil[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000notif",
				"192.165.1.34", null);

		String wic2Id = cProvApi.wasRecurrentlyCalledBy(null, cprocess3Id,
				cprocess2Id, "", "wifi", 10000);

		String wicb21Id = cProvApi.wasInitiallyCalledBy(null, cprocess3Id,
				agent11Id, "", "Laptop", "wifi");

		// Transitions .......

		String cres4Id = cProvApi.transition(null, "source", null, null, null,
				"EU", "event1");

		cProvApi.hadTransitionState_C(null, agent1Id, cres4Id, null);

		// cProvApi.hadTransitionState_B(null, cprocess1Id, cres4Id, null);

		String cres6Id = cProvApi.transition(null, "source", null, null, null,
				"EU", "event1");
		cProvApi.hadTransitionState_B(null, cprocess1Id, cres6Id, null);

		// cres3Id

		String cres7Id = cProvApi.transition(null, "source", null, null, null,
				"EU", "event1");
		cProvApi.hadTransitionState_A(null, cres3Id, cres7Id, null);

		String cres8Id = cProvApi.transition(null, "source", null, null, null,
				"EU", "event1");
		cProvApi.hadTransitionState_B(null, cprocess2Id, cres8Id, null);

		String cres9Id = cProvApi.transition(null, "source", null, null, null,
				"EU", "event1");
		cProvApi.hadTransitionState_B(null, cres5Id, cres9Id, null);

		String cres50Id = cProvApi.transition(null, "source", null, null, null,
				"US", "event1");
		cProvApi.hadTransitionState_C(null, agent11Id, cres50Id, null);

		String cres51Id = cProvApi.transition(null, "source", null, null, null,
				"US", "event1");
		cProvApi.hadTransitionState_B(null, cprocess11Id, cres51Id, null);

		String cres52Id = cProvApi.transition(null, "source", null, null, null,
				"US", "event1");
		cProvApi.hadTransitionState_A(null, cres31Id, cres52Id, null);

		String cres53Id = cProvApi.transition(null, "destination", null, null,
				null, "US", "event1");
		cProvApi.hadTransitionState_B(null, cprocess3Id, cres53Id, null);

	}

	public void genPostStatements() {

	}
}
