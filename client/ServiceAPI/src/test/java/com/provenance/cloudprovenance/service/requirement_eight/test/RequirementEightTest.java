/*
 * @(#) CProvRecordEight.java       1.1 19/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.requirement_eight.test;

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
import com.provenance.cloudprovenance.service.requirement_one.test.RequirementOneTest;
import com.provenance.cloudprovenance.service.traceability.api.ServiceXmlDocumentTraceability;

/**
 * Policy request to validate against the following policy: A system cannot
 * replicate a file if the user explicitly marks it as non-replicable, at
 * creation time.
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
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

	@Test
	public void shareLocationTest() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		String expected_outcome = "Permit";

		String userAgent = "Admin";
		String resource = "documentA";
		String process = "replicate";
		String environment1 = "rep.store.confidenshare.labs.orange.com";

		String actual_outcome = srvCompliance.constructRequest(userAgent,
				resource, process, environment1);

		Assert.assertTrue(actual_outcome.contains(expected_outcome));

	}
}