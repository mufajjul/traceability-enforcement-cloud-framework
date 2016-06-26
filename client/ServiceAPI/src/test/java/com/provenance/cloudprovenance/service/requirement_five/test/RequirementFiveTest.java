/**
 * @file 		RequirementFiveTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.requirement_five.test;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

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
public class RequirementFiveTest {

	@Autowired
	private ServiceXmlDocumentTraceability<String> cProvApi;

	@Autowired
	ServiceCompliance<String> srvCompliance;

	@Before
	public void setUp() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		CProvRecordFive genProvRecord = new CProvRecordFive(cProvApi);
		genProvRecord.genPreStatements();

	}

	@Test
	public void shareAsOwnershipOfaFileTest() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		String expected_outcome = "Permit";

		String userAgent = "Bob";
		String userAgent2 = "Phil";
		String resource = "document1";
		String process = "share";
		String environment1 = "region.share.confidenshare.labs.orange.com";

		// srvCompliance.constructRequest(userAgent, resource, process,
		// environment1); region.share.confidenshare.labs.orange.com

		srvCompliance.constructRequest(new String[] { userAgent, userAgent2 },
				new String[] { resource }, new String[] { process },
				environment1);

	}

}
