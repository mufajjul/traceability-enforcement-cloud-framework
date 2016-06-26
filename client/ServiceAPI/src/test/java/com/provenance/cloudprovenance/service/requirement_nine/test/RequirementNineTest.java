/**
 * @file 		RequirementNineTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.requirement_nine.test;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Assert;
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
import com.provenance.cloudprovenance.service.traceability.api.ServiceXmlDocumentTraceability;

/**
 * @author Mufy
 * 
 */

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans.xml")
public class RequirementNineTest {

	@Autowired
	private ServiceXmlDocumentTraceability<String> cProvApi;

	@Autowired
	ServiceCompliance<String> srvCompliance;

	@Before
	public void setUp() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		CProvRecordNine genProvRecord = new CProvRecordNine(cProvApi);
		genProvRecord.genPreStatements();

	}
	
	@Test
	public void shareAsOwnershipOfaFileTest() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		String expected_outcome = "Deny";

		String userAgent = "Phil";
		String resource = "document1-2";
		String process = "share";
		String environment1 = "mod.share.confidenshare.labs.orange.com";

		String outcome = srvCompliance.constructRequest(userAgent, resource, process,
				environment1);

		Assert.assertNotNull(outcome);
		
	}

}
