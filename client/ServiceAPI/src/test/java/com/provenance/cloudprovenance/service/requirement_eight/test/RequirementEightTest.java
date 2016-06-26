/**
 * @file 		RequirementEightTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.requirement_eight.test;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import com.provenance.cloudprovenance.service.api.impl.CprovXmlProvenance;
import com.provenance.cloudprovenance.service.api.impl.DynamicPolicyRequest;
import com.provenance.cloudprovenance.service.policy.api.ServiceCompliance;
import com.provenance.cloudprovenance.service.requirement_one.test.RequirementOneTest;
import com.provenance.cloudprovenance.service.traceability.api.ServiceXmlDocumentTraceability;

/**
 * @author Mufy
 * 
 */

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans.xml")
public class RequirementEightTest {

	private static String serviceRequirementTestName = "requirement-8";
	private static Logger logger = Logger.getLogger(RequirementOneTest.class);

	@Autowired
	private ServiceXmlDocumentTraceability<String> cProvApi;

	@Autowired
	ServiceCompliance<String> srvCompliance;

	@Before
	public void setUp() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		CProvRecordEight genProvRecord = new CProvRecordEight(cProvApi);
		 genProvRecord.genPreStatements();

	}

	
	//TODO - updaate the environment ---
	@Test
	public void shareLocationTest() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {
		
		String expected_outcome = "Permit";

		String userAgent = "Admin";
		String resource = "documentA";
		String process = "replicate";
		String environment1 = "rep.store.confidenshare.labs.orange.com";

		srvCompliance.constructRequest(userAgent, resource, process,
				environment1);

	}

}