/**
 * @file 		CProvRecordNine.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.requirement_nine.test;

import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.service.api.impl.CprovXmlProvenance;
import com.provenance.cloudprovenance.service.traceability.api.ServiceXmlDocumentTraceability;

/**
 * @author Mufy
 * 
 */
public class CProvRecordNine {

	@Autowired 
	private ServiceXmlDocumentTraceability<String> cProvApi;
	
	public CProvRecordNine(ServiceXmlDocumentTraceability<String> cProvApi) {
		this.cProvApi= cProvApi;
	}
	
	
	public void genPreStatements(){
		
		
		cProvApi.setMaxStatementPerDocument(24);
		
		
		String agent1Id = cProvApi.Agent("Bob", "Bob",
				"Smith");
		
		String cprocess1Id = cProvApi.cProcess("share",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000share",
				"192.168.1.34", null);

		String wicb1Id = cProvApi.wasInitiallyCalledBy(
				null, cprocess1Id, agent1Id, "", "Laptop", "wifi");

		// username and password
		String cres1Id = cProvApi
				.cResource("document1", "data", "=bob[=]!:uuid:f81d4fae",
						"[=]!:pid:g81d4fde-document", "192.168.1.34", true,
						null, null, "general", 1.0f, null);
					
	//	String cres2Id = cProvApi.cResource("e-restricted", "",
		//		"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-restrict",
			//	"192.168.1.34", true, null, null, "general", 1.0f);

		String used1Id = cProvApi.used(null, cprocess1Id,
				cres1Id, null);
		
//		String used2Id = cProvApi.used(null, cprocess1Id,
	//			cres2Id, null);
		
		
		// Add ownership
		cProvApi.hadOwnership(null, cres1Id, agent1Id,
				"Originator");


		//make copy
		String cprocess2Id = cProvApi.cProcess("copy",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-0000copy",
				"192.168.1.34", null);

		String wic1Id = cProvApi.wasImplicitlyCalledBy(null,
				cprocess2Id, cprocess1Id, "", "", "laptop", "wifi");


		String used3Id = cProvApi.used(null, cprocess2Id,
				cres1Id, null);
		
		
		String cres3Id = cProvApi.cResource("document1-1", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", true, null, null, "non-modifiable", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres3Id,
				cprocess2Id, null, null);

		
		cProvApi.wasRepresentationOf(null, cres3Id, cres1Id, cprocess2Id, null, null, null);

		String cres4Id = cProvApi.cResource("shareSuccess", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-sharesuc",
				"192.168.1.34", true, null, null, "general", 1.0f, null);
		
		cProvApi.wasVirtualizedBy(null, cres4Id,
				cprocess1Id, null, null);
		
		String agent2Id = cProvApi.Agent("Phil", "Phil",
				"Smith");

		cProvApi.hadOwnership(null, cres3Id, agent2Id,
				"Possession");
		
		cProvApi.hadOwnership(null, cres4Id, agent2Id,
				"Affiliation");
	

		/***** Admin deleting Bob's Account */
		
		//String agent3Id = cProvApi.Agent("ag-admin", "admin",
			//	"admin");
		
		// modify and 
		String cprocess3Id = cProvApi.cProcess("modify",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-00modify",
				"192.168.1.34", null);
		
		
		String wicb2Id = cProvApi.wasInitiallyCalledBy(
				null, cprocess3Id, agent2Id, "", "Laptop", "wifi");


	//	String cres5Id = cProvApi.cResource("e-Bob", "",
		//		"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-00000bob",
			//	"192.168.1.34", true, null, null);

		String cres5Id = cProvApi.cResource("document1-2", "data",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		String used4Id = cProvApi.used(null, cprocess3Id,
				cres3Id, null);
		
		cProvApi.wasVirtualizedBy(null, cres5Id,
				cprocess3Id, null, null);
		
		//TODO - Add a new edge called hadExpired
		cProvApi.hadOwnership(null, cres5Id, agent2Id,
				"Possession");
		
		cProvApi.wasRepresentationOf(null, cres5Id, cres3Id, cprocess3Id, null, null, null);
	}
	
	
	public void genPreStatements_1() {

		// Gen a share with from user A to user B with File A

		// Check for the wasDerivedFrom (wasRepresenatationOf,
		// wasExtendedBy?????)

		cProvApi.setMaxStatementPerDocument(11);
		
		
		// agent 
	//	String agent1Id = cProvApi.Agent("ag-system", "system",
		//		"system");
		
		// registration create
		//String cprocess1Id = cProvApi.cProcess("a-replicate",
			//	"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-replicat",
				//"192.168.1.34", null);

//		String wicb1Id = cProvApi.wasInitiallyCalledBy(
	//			null, cprocess1Id, agent1Id, "", "system", "ethernet");

		//String cres1Id = cProvApi
			//	.cResource("e-document1", "", "=bob[=]!:uuid:f81d4fae",
				//		"[=]!:pid:g81d4fde-document", "192.168.1.34", true,
					//	null, null, "general", 1.0f);
		
//		String used1Id = cProvApi.used(null, cprocess1Id,
	//			cres1Id, null);
		
		
		// agent to register
		String agent1Id = cProvApi.Agent("Bob", "Bob", "Smith");

		// registration process
		String cprocess1Id = cProvApi.cProcess("create",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000create",
				"192.168.1.34", null);

		String wicb1Id = cProvApi.wasInitiallyCalledBy(null, cprocess1Id,
				agent1Id, "", "Laptop", "wifi");

		// username and password
		String cres1Id = cProvApi.cResource("documentA", "",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", false, null, null, "general", 1.0f, null);

		//String cres2Id = cProvApi.cResource("restricted", "",
			//	"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-restrict",
				//"192.168.1.34", true, null, null, "general", 1.0f);

	//	String used1Id = cProvApi.used(null, cprocess1Id, cres1Id, null);

		//String used2Id = cProvApi.used(null, cprocess1Id, cres2Id, null);

		// Add ownership
		cProvApi.hadOwnership(null, cres1Id, agent1Id, "Originator");

		// make copy
		String cprocess2Id = cProvApi.cProcess("replicate",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-0000rep",
				"192.168.1.34", null);

		String wic1Id = cProvApi.wasImplicitlyCalledBy(null, cprocess2Id,
				cprocess1Id, "", "", "laptop", "wifi");

		String used3Id = cProvApi.used(null, cprocess2Id, cres1Id, null);

		String cres3Id = cProvApi.cResource("output", "",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, cres3Id, cprocess2Id, null, null);

		cProvApi.wasRepresentationOf(null, cres3Id, cres1Id, cprocess2Id, null,
				null, null);

//		String cres4Id = cProvApi.cResource("shareSuccess", "",
	//			"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-sharesuc",
		//		"192.168.1.34", true, null, null, "general", 1.0f);

///		cProvApi.wasVirtualizedBy(null, cres4Id, cprocess1Id, null, null);

	//	String agent2Id = cProvApi.Agent("Phil", "Phil", "Phil");

		//cProvApi.hadOwnership(null, cres3Id, agent2Id, "Possession");

//		cProvApi.hadOwnership(null, cres4Id, agent2Id, "Affiliation");

	//	/***** Admin deleting Bob's Account */

		// String agent3Id = cProvApi.Agent("ag-admin", "admin",
		// "admin");

		// modify and
		//String cprocess3Id = cProvApi.cProcess("modify",
			//	"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-00modify",
				//"192.168.1.34", null);

	//	String wicb2Id = cProvApi.wasInitiallyCalledBy(null, cprocess3Id,
		//		agent2Id, "", "Laptop", "wifi");

		// String cres5Id = cProvApi.cResource("e-Bob", "",
		// "=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-00000bob",
		// "192.168.1.34", true, null, null);

		//String cres5Id = cProvApi.cResource("document1-2", "",
			//	"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				//"192.168.1.34", true, null, null, "general", 1.0f);

	//	String used4Id = cProvApi.used(null, cprocess3Id, cres3Id, null);

		//cProvApi.wasVirtualizedBy(null, cres5Id, cprocess3Id, null, null);

		// TODO - Add a new edge called hadExpired
		//cProvApi.hadOwnership(null, cres5Id, agent2Id, "Possession");

		//cProvApi.wasRepresentationOf(null, cres5Id, cres3Id, cprocess3Id, null,
			//	null, null);

		/***************** Share fail *********************************/

	}

	public void genPostStatements() {

	}
}
