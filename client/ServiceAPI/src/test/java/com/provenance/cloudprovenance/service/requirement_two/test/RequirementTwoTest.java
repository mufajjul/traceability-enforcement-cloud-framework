/**
 * @file 		RequirementTwoTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.requirement_two.test;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
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
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans.xml")
public class RequirementTwoTest {

	static Logger logger = Logger.getLogger(RequirementTwoTest.class);

	@Autowired
	private ServiceXmlDocumentTraceability<String> cProvApi;

	@Autowired
	ServiceCompliance<String> srvCompliance;

	@Before
	public void setUp() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		CProvRecordTwo genProvRecord = new CProvRecordTwo(cProvApi);
		genProvRecord.genPreStatements();
	}

	@Test
	public void shareConfidentialFileOriginUserTest()
			throws URISyntaxException, ParserConfigurationException,
			SAXException, IOException, TransformerException {

		int policyId = 1;
		int requestId = 1;
		String expected_outcome = "Permit";

		// convert resources

		String userAgent = "Bob";
		String resource = "fileA";
		String process = "share";
		String environment1 = "confidential.share.confidenshare.labs.orange.com";

		String outcome = srvCompliance.constructRequest(userAgent, resource,
				process, environment1);

		logger.info("Response: " + outcome);

		Assert.assertNotNull(outcome);

	}

	@Ignore
	@Test
	public void shareConfidentialfileReshareByReceivedAuthorTest() {

		String expectedOutcome = "Deny";

		Assert.fail("Not yet implemented ");

	}
}
