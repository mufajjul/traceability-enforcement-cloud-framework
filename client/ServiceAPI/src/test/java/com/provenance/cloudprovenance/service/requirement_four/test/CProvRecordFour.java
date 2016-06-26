/**
 * @file 		CProvRecordFour.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.requirement_four.test;

import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.service.api.impl.CprovXmlProvenance;
import com.provenance.cloudprovenance.service.traceability.api.ServiceXmlDocumentTraceability;

/**
 * @author Mufy
 * 
 */
public class CProvRecordFour {

	@Autowired 
	private ServiceXmlDocumentTraceability<String> cProvApi;

	public CProvRecordFour(ServiceXmlDocumentTraceability<String> cProvApi) {
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
		String cprocess3Id = cProvApi.cProcess("notification", "http://127.0.0.1/bob",
				"http://127.0.0.1/bob/Linux/vProcess3",
				"127.0.0.1/Linux/pProcess3Notification", null);

		
		String wicb1Id = cProvApi.wasInitiallyCalledBy(null, cprocess1Id,
				agent1Id, "", "Laptop", "wifi");

		// file
		String cres1Id = cProvApi.cResource("fileA", "file",
				"http:127.0.0.1/bob@yahoo.com",
				"http://127.0.0.1/bob/res/file", "127.0.0.1", true, null,
				null, "general", 1.0f, null);

		
		//TODO - review originator
				cProvApi.hadOwnership(null, cres1Id, agent1Id, "possession");
	
				
	//	String cres2Id = cProvApi.cResource(null, "",
		//		"http:127.0.0.1/bob@yahoo.com", "http://127.0.0.1/bob/res/pw",
			//	"127.0.0.1", true, null, null, "general", 1.0f);

//		cProvApi.wasVirtualizedBy(null, cres1Id, cprocess2Id, null, "resource");

		//cProvApi.wasVirtualizedBy(null, cres2Id, cprocess1Id, null, "share");


//		String used2Id = cProvApi.used(null, cprocess1Id, cres2Id, null);

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
				"http://127.0.0.1/bob/res/fileA1", "127.0.0.1", true,
				null, null, "general", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres3Id, cprocess2Id, null, "shareComp");

		String used1Id = cProvApi.used(null, cprocess1Id, cres3Id, null);

	
		String wic2Id = cProvApi.wasRecurrentlyCalledBy(null, cprocess3Id,
				cprocess1Id, "","wifi", 10000);

		
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
