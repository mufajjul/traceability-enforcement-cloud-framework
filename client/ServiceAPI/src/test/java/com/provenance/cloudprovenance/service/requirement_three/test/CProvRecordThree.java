/*
 * @(#) CProvRecordThree.java       1.1 19/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.requirement_three.test;

import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.service.api.impl.CprovXmlProvenance;
import com.provenance.cloudprovenance.service.traceability.api.ServiceXmlDocumentTraceability;

/**
 * Provenance graph to validate against the following policy: If a file (fileA)
 * is categorized as ‘restricted’, only the originator (userA) and the receivers
 * (userB ... userN) are allowed to modify the file (fileA and its modifications
 * fileA1 ... fileAn) and share (explicitly re-share) amongst themselves only.
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
 */
public class CProvRecordThree {

	@Autowired
	private ServiceXmlDocumentTraceability<String> cProvApi;

	public CProvRecordThree(ServiceXmlDocumentTraceability<String> cProvApi) {
		this.cProvApi = cProvApi;

	}

	public void genPreStatements() {

		cProvApi.setMaxStatementPerDocument(17);

		// user
		String agent1Id = cProvApi.Agent("Bob", "Bob", "smith");

		// share process
		String cprocess1Id = cProvApi.cProcess("share", "http://127.0.0.1/bob",
				"http://127.0.0.1/bob/Linux/vProcess1",
				"127.0.0.1/Linux/pProcess1Share", null);

		// if sucessful
		String cprocess3Id = cProvApi.cProcess("notification",
				"http://127.0.0.1/bob", "http://127.0.0.1/bob/Linux/vProcess3",
				"127.0.0.1/Linux/pProcess3Notification", null);

		String wicb1Id = cProvApi.wasInitiallyCalledBy(null, cprocess1Id,
				agent1Id, "", "Laptop", "wifi");

		// file
		String cres1Id = cProvApi.cResource("fileA", "file",
				"http:127.0.0.1/bob@yahoo.com",
				"http://127.0.0.1/bob/res/file", "127.0.0.1", true, null, null,
				"restricted", 1.0f, null);

		// TODO - review originator
		cProvApi.hadOwnership(null, cres1Id, agent1Id, "possession");

		// create a copy
		String cprocess2Id = cProvApi.cProcess("copy", "http://127.0.0.1/bob",
				"http://127.0.0.1/bob/Linux/vProcess2",
				"127.0.0.1/Linux/pProcess2copy", null);

		String wic1Id = cProvApi.wasImplicitlyCalledBy(null, cprocess2Id,
				cprocess1Id, "", "", "laptop", "wifi");

		String used3Id = cProvApi.used(null, cprocess2Id, cres1Id, null);

		// outcome
		String cres3Id = cProvApi.cResource("fileA1", "file",
				"http:127.0.0.1/bob@yahoo.com",
				"http://127.0.0.1/bob/res/fileA1", "127.0.0.1", true, null,
				null, "restricted", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres3Id, cprocess2Id, null, "shareComp");

		String used1Id = cProvApi.used(null, cprocess1Id, cres3Id, null);

		String wic2Id = cProvApi.wasRecurrentlyCalledBy(null, cprocess3Id,
				cprocess1Id, "", "wifi", 10000);

		String used4Id = cProvApi.used(null, cprocess3Id, cres3Id, null);

		String cres4Id = cProvApi.cResource("outcome", "data",
				"http:127.0.0.1/bob@yahoo.com",
				"http://127.0.0.1/bob/res/userCreationSuccess", "127.0.0.1",
				true, null, null, "general", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres4Id, cprocess3Id, null, "share");

		cProvApi.wasRepresentationOf(null, cres3Id, cres1Id, cprocess3Id, null,
				null, null);
	}

	public void genPostStatements() {

	}

}
