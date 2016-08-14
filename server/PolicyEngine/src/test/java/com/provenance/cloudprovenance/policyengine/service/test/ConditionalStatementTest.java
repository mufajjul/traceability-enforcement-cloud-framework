/*
 * @(#) ConditionalStatementTest.java       1.1 14/8/2016
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

import com.provenance.cloudprovenance.policyengine.service.PolicyEngine;

/**
 * This class provide tests for cProv statements (entity, activity, etc))
 * 
 * @version 1.1 14 Aug 2016
 * @author Mufy
 * @Module PolicyEngine
 */
//@Ignore
public class ConditionalStatementTest {
	
	static Logger logger = Logger.getLogger(ConditionalStatementTest.class);
	static String type = "ConditionalStatement";

	@BeforeClass
	public static void generateXACMLpolicies() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		PropertyConfigurator.configure(new File(
				"target/classes/log4j.properties").getAbsolutePath());
		
		logger.info("\n *********************************************** TEST: CONDITIONAL STATEMENT - Translating cProvl policies & reuests in XACML policies & requests************************************");

		 TestUtility.generateSources(type);
			TestUtility.storeProvenanceFile();
	}

	@Test
	public void conditionalStatementNodeTest()
			throws URISyntaxException {

		int testPolicyId = 1;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** CONDITIONAL STATEMENT TEST: "
				+ testPolicyId
				+ "   ******************************************");
		logger.info("\n ---DESC: ------\n");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}
	
	@Test
	public void conditionalStatementEdgeTest()
			throws URISyntaxException {

		int testPolicyId = 2;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** CONDITIONAL STATEMENT TEST: "
				+ testPolicyId
				+ "   ******************************************");
		logger.info("\n ---DESC: ------\n");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void conditionalStatementInvalidEdgeTest()
			throws URISyntaxException {

		int testPolicyId = 3;
		String expectedOutcome = "Deny";

		logger.info("\n *********************************************** CONDITIONAL STATEMENT TEST: "
				+ testPolicyId
				+ "   ******************************************");
		logger.info("\n ---DESC: ------\n");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}
		
	/** This test uses the ConfidenShare service traceability data and therefore will fail with the MMC traceability data */ 
	@Test
	public void conditionalStatementWithDrefTargetTest()
			throws URISyntaxException {

		int testPolicyId = 4;
		String expectedOutcome = "Deny";

		logger.info("\n *********************************************** CONDITIONAL STATEMENT TEST: "
				+ testPolicyId
				+ "   ******************************************");
		logger.info("\n ---DESC: ------\n");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}
	
	/** This test uses the ConfidenShare service traceability data and therefore will fail with the MMC traceability data */ 
	@Test
	public void conditionalStatementWithDrefRefCondTest()
			throws URISyntaxException {

		int testPolicyId = 5;
		String expectedOutcome = "Deny";

		logger.info("\n *********************************************** CONDITIONAL STATEMENT TEST: "
				+ testPolicyId
				+ "   ******************************************");
		logger.info("\n ---DESC: ------\n");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}
	
	@Ignore
	@Test
	public void testAll() throws URISyntaxException {
		String xacmlPolicyDirectoryPath = "target/test-classes/policies/xacml/actual/" + type;
		String xacmlRequestDirectoryPath = "target/test-classes/requests/xacml/actual/" + type;
		
		for (int i = 1; i < 3; i++) {
			
			logger.info("\n *********************************************** CONDITIONAL STATEMENT TEST : "+i+ "   ******************************************");

			PolicyEngine policyEngine = new PolicyEngine();
			logger.info("policy path ==> " + xacmlPolicyDirectoryPath
					+ "/policy" + i + "-xacml.xml");
			logger.info("request path ==> " + xacmlRequestDirectoryPath
					+ "/request" + i + "-xacml.xml");

			String outcome = policyEngine.executePolicy(
					xacmlPolicyDirectoryPath + "/policy" + i + "-xacml.xml",
					xacmlRequestDirectoryPath + "/request" + i + "-xacml.xml",
					null);
			Assert.assertNotNull(outcome);

			logger.info(outcome);
		}
	}
}
