/**
 * @file 		CProvRecordEight.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.requirement_eight.test;

import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.service.traceability.api.ServiceXmlDocumentTraceability;

/**
 * @author Mufy
 * 
 */
public class CProvRecordEight {

	@Autowired
	private ServiceXmlDocumentTraceability<String> cProvApi;

	public CProvRecordEight(ServiceXmlDocumentTraceability<String> cProvApi) {
		this.cProvApi = cProvApi;
	}

	public void genPreStatements() {

		cProvApi.setMaxStatementPerDocument(12);

		String agent1Id = cProvApi.Agent("Admin", "Admin", "Admin");

		// registration process
		String cprocess1Id = cProvApi.cProcess("create",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000create",
				"192.168.1.34", null);

		String wicb1Id = cProvApi.wasInitiallyCalledBy(null, cprocess1Id,
				agent1Id, "", "Laptop", "wifi");

		// username and password
		String cres1Id = cProvApi.cResource("documentA", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", false, null, null, "general", 1.0f, null);

		// String cres2Id = cProvApi.cResource("restricted", "",
		// "=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-restrict",
		// "192.168.1.34", true, null, null, "general", 1.0f);

		// String used1Id = cProvApi.used(null, cprocess1Id, cres1Id, null);

		// String used2Id = cProvApi.used(null, cprocess1Id, cres2Id, null);

		// Add ownership
		cProvApi.hadOwnership(null, cres1Id, agent1Id, "Originator");

		// make copy
		String cprocess2Id = cProvApi.cProcess("replicate",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-0000rep",
				"192.168.1.34", null);

		String wic1Id = cProvApi.wasImplicitlyCalledBy(null, cprocess2Id,
				cprocess1Id, "", "", "laptop", "wifi");

		String used3Id = cProvApi.used(null, cprocess2Id, cres1Id, null);

		String cres3Id = cProvApi.cResource("output", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres1Id, cprocess1Id, null, null);

		cProvApi.wasVirtualizedBy(null, cres3Id, cprocess2Id, null, null);

		cProvApi.wasRepresentationOf(null, cres3Id, cres1Id, cprocess2Id, null,
				null, null);
	}

	public void genPostStatements() {

	}
}