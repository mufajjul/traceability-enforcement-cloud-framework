/*
 * @(#) CProvRecordSix.java       1.1 19/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.requirement_six.test;

import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.service.api.impl.CprovXmlProvenance;
import com.provenance.cloudprovenance.service.traceability.api.ServiceXmlDocumentTraceability;

/**
 * Provenance graph to validate against the following policy: If a user (userA)
 * is classed as ‘removed’, none of the associated shared files (file X) can be
 * shared further.
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
 */
public class CProvRecordSix {

	@Autowired
	private ServiceXmlDocumentTraceability<String> cProvApi;

	public CProvRecordSix(ServiceXmlDocumentTraceability<String> cProvApi) {

		this.cProvApi = cProvApi;
	}

	public void genPreStatements() {

		cProvApi.setMaxStatementPerDocument(22);

		// agent to register
		String agent1Id = cProvApi.Agent("Bob", "Bob", "Smith");

		// registration process
		String cprocess1Id = cProvApi.cProcess("share",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000share",
				"192.168.1.34", null);

		String wicb1Id = cProvApi.wasInitiallyCalledBy(null, cprocess1Id,
				agent1Id, "", "Laptop", "wifi");

		// username and password
		String cres1Id = cProvApi.cResource("document1", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", true, null, null, "restricted", 1.0f, null);

		String used1Id = cProvApi.used(null, cprocess1Id, cres1Id, null);

		// Add ownership
		cProvApi.hadOwnership(null, cres1Id, agent1Id, "Originator");

		// make copy
		String cprocess2Id = cProvApi.cProcess("copy",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-0000copy",
				"192.168.1.34", null);

		String wic1Id = cProvApi.wasImplicitlyCalledBy(null, cprocess2Id,
				cprocess1Id, "", "", "laptop", "wifi");

		String used3Id = cProvApi.used(null, cprocess2Id, cres1Id, null);

		String cres3Id = cProvApi.cResource("document1-1", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", true, null, null, "restricted", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres3Id, cprocess2Id, null, null);

		cProvApi.wasRepresentationOf(null, cres3Id, cres1Id, cprocess2Id, null,
				null, null);

		String cres4Id = cProvApi.cResource("shareSuccess", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-sharesuc",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres4Id, cprocess1Id, null, null);

		String agent2Id = cProvApi.Agent("Phil", "Phil", "Phil");

		cProvApi.hadOwnership(null, cres3Id, agent2Id, "Possession");

		cProvApi.hadOwnership(null, cres4Id, agent2Id, "Affiliation");

		/***** Admin deleting Bob's Account */

		// delete process
		String cprocess3Id = cProvApi.cProcess("delete",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-00delete",
				"192.168.1.34", null);

		String cres6Id = cProvApi.cResource("deleteSuccess", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-deletesu",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres6Id, cprocess3Id, null, null);

		// TODO - Add a new edge called hadExpired
		cProvApi.hadOwnership(null, cres6Id, agent1Id, "Originator");

		String wicb2Id = cProvApi.wasInitiallyCalledBy(null, cprocess3Id,
				agent1Id, "", "Laptop", "wifi");

	}

	public void gen_old_PreStatements() {

		// Gen a share with from user A to user B with File A
		cProvApi.setMaxStatementPerDocument(25);

		// agent to register
		String agent1Id = cProvApi.Agent("Bob", "Bob", "Smith");

		// registration process
		String cprocess1Id = cProvApi.cProcess("Share",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000share",
				"192.168.1.34", null);

		String wicb1Id = cProvApi.wasInitiallyCalledBy(null, cprocess1Id,
				agent1Id, "", "Laptop", "wifi");

		// username and password
		String cres1Id = cProvApi.cResource("document1", "file",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		String used1Id = cProvApi.used(null, cprocess1Id, cres1Id, null);

		// Add ownership
		cProvApi.hadOwnership(null, cres1Id, agent1Id, "Originator");

		// make copy
		String cprocess2Id = cProvApi.cProcess("Copy",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-0000copy",
				"192.168.1.34", null);

		String wic1Id = cProvApi.wasImplicitlyCalledBy(null, cprocess2Id,
				cprocess1Id, "", "", "laptop", "wifi");

		String used3Id = cProvApi.used(null, cprocess2Id, cres1Id, null);

		String cres3Id = cProvApi.cResource("document1-1", "file",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres3Id, cprocess2Id, null, null);

		cProvApi.wasRepresentationOf(null, cres3Id, cres1Id, cprocess2Id, null,
				null, null);

		String cres4Id = cProvApi.cResource("shareSuccess", "text",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-sharesuc",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres4Id, cprocess1Id, null, null);

		String agent2Id = cProvApi.Agent("Phil", "Phil", "Phil");

		cProvApi.hadOwnership(null, cres3Id, agent2Id, "Possession");

		cProvApi.hadOwnership(null, cres4Id, agent2Id, "Affiliation");

		String cres5Id = cProvApi.cResource("document1-2", "file",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres5Id, cprocess2Id, null, null);

		cProvApi.wasRepresentationOf(null, cres5Id, cres3Id, cprocess2Id, null,
				null, null);

		String agent3Id = cProvApi.Agent("Jones", "Jones", "Jones");

		String cres6Id = cProvApi.cResource("shareSuccess2", "text",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-sharesuc",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres6Id, cprocess1Id, null, null);

		cProvApi.hadOwnership(null, cres5Id, agent3Id, "Possession");

		cProvApi.hadOwnership(null, cres6Id, agent3Id, "Affiliation");

		/***************** Share fail *********************************/
	}

	public void genPostStatements() {

	}
}
