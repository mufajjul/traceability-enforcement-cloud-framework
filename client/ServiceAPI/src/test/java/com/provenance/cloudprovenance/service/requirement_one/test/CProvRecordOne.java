/*
 * @(#) CProvRecordOne.java       1.1 19/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.requirement_one.test;

import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.service.traceability.api.ServiceXmlDocumentTraceability;

/**
 * Provenance graph to validate against the following policy: A file (fileA) can
 * only be shared by a registered user (userA) to another user (userB).
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
 */
public class CProvRecordOne {

	@Autowired
	private ServiceXmlDocumentTraceability<String> cProvApi;

	public CProvRecordOne(ServiceXmlDocumentTraceability<String> cProvApi) {
		this.cProvApi = cProvApi;
	}

	public void genPreStatements_old() {

		cProvApi.setMaxStatementPerDocument(19);

		String agent1Id = cProvApi.Agent("Bob", "Bob", "Smith");

		// registration process
		String cprocess1Id = cProvApi.cProcess("createUser",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-createus",
				"pid:00:12:00:12:100:11:00:00", null);

		String wicb1Id = cProvApi.wasInitiallyCalledBy(null, cprocess1Id,
				agent1Id, "", "Laptop", "wifi");

		// username and password
		String cres1Id = cProvApi.cResource("email", "text",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000email",
				"pid:00:12:00:12:100:11:00:00", true, null, null, "general",
				1.0f, null);

		String cres2Id = cProvApi.cResource("password", "text",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-passowrd",
				"pid:00:12:00:12:100:11:00:00", true, null, null, "general",
				1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres1Id, cprocess1Id, null, null);

		cProvApi.wasVirtualizedBy(null, cres2Id, cprocess1Id, null, null);

		// check if user email exists
		String cprocess2Id = cProvApi.cProcess("checkUser",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-checkuse",
				"pid:00:12:00:12:100:11:00:00", null);

		String wic1Id = cProvApi.wasImplicitlyCalledBy(null, cprocess2Id,
				cprocess1Id, "", "", "laptop", "wifi");

		String used3Id = cProvApi.used(null, cprocess2Id, cres1Id, null);

		// outcome
		String cres3Id = cProvApi.cResource("userDoesNotExist", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-userdoes",
				"pid:00:12:00:12:100:11:00:00", true, null, null, "general",
				1.0f, null);

		// derivation
		cProvApi.wasRepresentationOf(null, cres3Id, cres1Id, cprocess2Id, null,
				null, null);

		cProvApi.wasVirtualizedBy(null, cres3Id, cprocess2Id, null, null);

		// if sucessful
		String cprocess3Id = cProvApi.cProcess("saveUser",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-saveuser",
				"pid:00:12:00:12:100:11:00:00", null);

		// create user
		String wic2Id = cProvApi.wasImplicitlyCalledBy(null, cprocess3Id,
				cprocess2Id, "", "", "laptop", "wifi");

		String used4Id = cProvApi.used(null, cprocess3Id, cres3Id, null);

		String cres4Id = cProvApi.cResource("userSaved", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-usersave",
				"pid:00:12:00:12:100:11:00:00", true, null, null, "general",
				1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres4Id, cprocess3Id, null, null);

		cProvApi.wasRepresentationOf(null, cres4Id, cres3Id, cprocess3Id, null,
				null, null);

	}

	public void genPreStatements() {

		cProvApi.setMaxStatementPerDocument(20);

		// agent to register
		String agent1Id = cProvApi.Agent("Bob", "Bob", "Smith");

		// registration process
		String cprocess1Id = cProvApi.cProcess("createUser",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-createus",
				"pid:00:12:00:12:100:11:00:00", null);

		String wicb1Id = cProvApi.wasInitiallyCalledBy(null, cprocess1Id,
				agent1Id, "", "laptop", "wifi");

		// username and password
		String cres1Id = cProvApi.cResource("email", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000email",
				"pid:00:12:00:12:100:11:00:00", true, null, null, "general",
				1.0f, null);

		String cres2Id = cProvApi.cResource("password", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-passowrd",
				"pid:00:12:00:12:100:11:00:00", true, null, null, "general",
				1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres1Id, cprocess1Id, null,
				"registration");

		cProvApi.wasVirtualizedBy(null, cres2Id, cprocess1Id, null,
				"registration");

		// check if user email exists
		String cprocess2Id = cProvApi.cProcess("checkUser",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-checkuse",
				"pid:00:12:00:12:100:11:00:00", null);

		String wic1Id = cProvApi.wasImplicitlyCalledBy(null, cprocess2Id,
				cprocess1Id, "", "", "laptop", "wifi");

		String used3Id = cProvApi.used(null, cprocess2Id, cres1Id, null);

		// outcome
		String cres3Id = cProvApi.cResource("userDoesNotExist", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-userdoes",
				"pid:00:12:00:12:100:11:00:00", true, null, null, "general",
				1.0f, null);

		// derivation
		cProvApi.wasRepresentationOf(null, cres3Id, cres1Id, cprocess2Id, null,
				null, "direct");

		cProvApi.wasVirtualizedBy(null, cres3Id, cprocess2Id, null,
				"registration");

		// if sucessful
		String cprocess3Id = cProvApi.cProcess("saveUser",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-createus",
				"pid:00:12:00:12:100:11:00:00", null);

		// create user
		String wic2Id = cProvApi.wasImplicitlyCalledBy(null, cprocess3Id,
				cprocess2Id, "", "", "laptop", "wifi");

		String used4Id = cProvApi.used(null, cprocess3Id, cres3Id, null);

		String cres4Id = cProvApi.cResource("createdSuccessfully", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-createds",
				"pid:00:12:00:12:100:11:00:00", true, null, null, "general",
				1.0f, null);

		// TODO - review originator
		cProvApi.hadOwnership(null, cres4Id, agent1Id, "originator");

		cProvApi.wasVirtualizedBy(null, cres4Id, cprocess3Id, null,
				"registration");

		cProvApi.wasRepresentationOf(null, cres4Id, cres3Id, cprocess3Id, null,
				null, "direct");

	}

}
