/**
 * @file 		RequirementOneTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.requirement_one.test;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import com.provenance.cloudprovenance.service.policy.api.ServiceCompliance;
import com.provenance.cloudprovenance.service.traceability.api.ServiceXmlDocumentTraceability;


/**
 * @author Mufy
 *
 */
//@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans.xml")
public class RequirementOneTest {
	
	@Autowired
	private ServiceXmlDocumentTraceability<String> cProvApi;

	@Autowired
	ServiceCompliance<String> srvCompliance;

	private Logger logger = Logger.getLogger("XmlTraceabilityModel");

	
	@Before
	public void setUp() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		logger.info("Initilizing the traceability data for policy 1");

		CProvRecordOne genProvRecord = new CProvRecordOne(cProvApi);
		genProvRecord.genPreStatements();

	}
	
	//@Ignore
	@Test 
	public void case1_valid_registeredUserTest () throws URISyntaxException, ParserConfigurationException, SAXException, IOException, TransformerException{
		
		String expected_outcome = "Permit";

		String userAgent = "Bob";
		String resource = "document1";
		String process = "createUser";
		String environment1 = "reg.share.confidenshare.labs.orange.com";

		String actual_outcome =   srvCompliance.constructRequest(userAgent, resource, process,
				environment1);
		
		Assert.assertTrue(actual_outcome.contains(expected_outcome));
	}
	
	// Not implemented 
	@Ignore
	@Test 
	public void case1_invalid_registeredUserTest () throws URISyntaxException, ParserConfigurationException, SAXException, IOException, TransformerException{

		int policyId = 1;
		int requestId = 2;
		String expected_outcome= "Deny";
		
		//convert resources		
	}
		
	@AfterClass
	public static void tearDown(){

		//TODO: remove policy statements from the traceability store 
		
		//genProvRecord.removeStatements();		
	}
	
}
