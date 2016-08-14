/*
 * @(#) PolicyTest.java       1.1 14/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.policyengine.service.test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * This class provide tests for the service case
 * 
 * @version 1.1 14 Aug 2016
 * @author Mufy
 * @Module PolicyEngine
 */
@Ignore
public class PolicyTest {

	static Logger logger = Logger.getLogger(PolicyTest.class);

	static String type = "Scenario";

	@BeforeClass
	public static void generateXACMLpolicies() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		PropertyConfigurator.configure(new File(
				"target/classes/log4j.properties").getAbsolutePath());

		logger.info("\n *********************************************** TEST: POLICY SCENARIO - Translating cProvl policies & reuests in XACML policies & requests************************************");

		TestUtility.generateSources(type);
		TestUtility.storeProvenanceFile();
	}

	@Ignore
	@Test
	public void scenarioPolicy1Test() throws URISyntaxException {

		// TODO - provide concrete policy and dataset
		String expectedOutcome = "Permit";
		int testPolicyId = 1;

		logger.info("\n *********************************************** CONFIDENSHARE SERVICE TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Ignore
	@Test
	public void scenarioPolicy2Test() throws URISyntaxException {

		// TODO - provide concrete policy and dataset
		String expectedOutcome = "Deny";
		int testPolicyId = 2;

		logger.info("\n *********************************************** CONFIDENSHARE SERVICE TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Ignore
	@Test
	public void scenarioPolicy3Test() throws URISyntaxException {

		// TODO - provide concrete policy and dataset
		String expectedOutcome = "Permit";
		int testPolicyId = 3;

		logger.info("\n *********************************************** CONFIDENSHARE SERVICE TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Ignore
	@Test
	public void scenarioPolicy4Test() throws URISyntaxException {

		// TODO - provide concrete policy and dataset
		String expectedOutcome = "Permit";
		int testPolicyId = 4;

		logger.info("\n *********************************************** CONFIDENSHARE SERVICE TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}
	
	//@Ignore
	@Test
	public void scenarioPolicy5Test() throws URISyntaxException {

		// TODO - provide concrete policy and dataset
		String expectedOutcome = "Permit";
		int testPolicyId = 5;

		logger.info("\n *********************************************** CONFIDENSHARE SERVICE TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}
	
	@Ignore
	@Test
	public void scenarioPolicy6Test() throws URISyntaxException {

		// TODO - provide concrete policy and dataset
		String expectedOutcome = "Permit";
		int testPolicyId = 6;

		logger.info("\n *********************************************** CONFIDENSHARE SERVICE TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}
	
	
	@Ignore
	@Test
	public void scenarioPolicy7Test() throws URISyntaxException {

		// TODO - provide concrete policy and dataset
		String expectedOutcome = "Permit";
		int testPolicyId = 7;

		logger.info("\n *********************************************** CONFIDENSHARE SERVICE TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}
	

	@Ignore
	@Test
	public void scenarioPolicy8Test() throws URISyntaxException {

		// TODO - provide concrete policy and dataset
		String expectedOutcome = "Deny";
		int testPolicyId = 8;

		logger.info("\n *********************************************** CONFIDENSHARE SERVICE TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}
	
	@Ignore
	@Test
	public void scenarioPolicy9Test() throws URISyntaxException {

		// TODO - provide concrete policy and dataset
		String expectedOutcome = "Deny";
		int testPolicyId = 9;

		logger.info("\n *********************************************** CONFIDENSHARE SERVICE TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

}
