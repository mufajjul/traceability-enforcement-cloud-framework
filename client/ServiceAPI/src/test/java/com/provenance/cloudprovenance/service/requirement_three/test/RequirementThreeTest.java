/*
 * @(#) RequirementThreeTest.java       1.1 19/8/2016
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
 * Policy request to validate against the following policy: If a file (fileA)
 * is categorized as ‘restricted’, only the originator (userA) and the receivers
 * (userB ... userN) are allowed to modify the file (fileA and its modifications
 * fileA1 ... fileAn) and share (explicitly re-share) amongst themselves only.
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans.xml")
public class RequirementThreeTest {

	@Autowired
	private ServiceXmlDocumentTraceability<String> cProvApi;

	@Autowired
	ServiceCompliance<String> srvCompliance;

	@Before
	public void setUp() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		CProvRecordThree genProvRecord = new CProvRecordThree(cProvApi);
		genProvRecord.genPreStatements();
	}

	@Test
	public void shareConfidentialFileOriginUserTest()
			throws URISyntaxException, ParserConfigurationException,
			SAXException, IOException, TransformerException {

		String expected_outcome = "Permit";

		// convert resources
		String userAgent = "Bob";
		String resource = "fileA";
		String process = "share";
		String environment1 = "restricted.share.confidenshare.labs.orange.com";

		String actual_outcome = srvCompliance.constructRequest(userAgent, resource, process,
				environment1);

		Assert.assertTrue(actual_outcome.contains(expected_outcome));
	}

	@Ignore
	@Test
	public void shareConfidentialfileReshareByReceivedAuthorTest() {

		String expectedOutcome = "Deny";
		Assert.fail("Not yet implemented ");

	}

}
