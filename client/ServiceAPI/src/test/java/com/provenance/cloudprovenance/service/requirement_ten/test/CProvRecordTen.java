/**
 * @file 		CProvRecordTen.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.requirement_ten.test;

import com.provenance.cloudprovenance.service.api.impl.CprovXmlProvenance;

/**
 * @author Mufy
 *
 */
public class CProvRecordTen {

	private CprovXmlProvenance cProvApi;

	public CProvRecordTen(CprovXmlProvenance cProvApi){
		
		this.cProvApi = cProvApi;
	}
	
	
	public void genPreStatements() {

		// agent
		String agent1Id = cProvApi.Agent("ag-system",
				"system", "system");

		// registration create
		String cprocess1Id = cProvApi.cProcess(
				"a-replicate", "=bob[=]!:uuid:f81d4fae",
				"[=]!:pid:g81d4fde-replicat", "192.168.1.34", null);

		String wicb1Id = cProvApi.wasInitiallyCalledBy(
				null, cprocess1Id, agent1Id, "", "system", "ethernet");

		String cres1Id = cProvApi.cResource("e-document1",
				"", "=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		String used1Id = cProvApi.used(null, cprocess1Id,
				cres1Id, null);

		// Add ownership
		// cProvApi.hadOwnership(null, cres1Id, agent1Id,
		// "Originator");

		// cProvApi.wasVirtualizedBy(null, cres1Id,
		// cprocess1Id, null, null);

	}

	public void genPostStatements() {

	}
}
