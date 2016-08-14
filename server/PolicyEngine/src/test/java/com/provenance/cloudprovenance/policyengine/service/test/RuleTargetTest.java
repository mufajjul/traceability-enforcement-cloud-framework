/*
 * @(#) RuleTargetTest.java       1.1 14/8/2016
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
 * This class tests the target settings of a policy
 * 
 * @version 1.1 14 Aug 2016
 * @author Mufy
 * @Module PolicyEngine
 */
@Ignore
public class RuleTargetTest {

	static String type = "ID";
	static Logger logger = Logger.getLogger(RuleTargetTest.class);

	@BeforeClass
	public static void generateXACMLpolicies() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		/**
		 * Generate policies
		 */

		PropertyConfigurator.configure(new File(
				"target/classes/log4j.properties").getAbsolutePath());

		logger.info("\n *********************************************** TEST: RULE TARGET - Translating cProvl policies & reuests in XACML policies & requests************************************");

		TestUtility.generateSources(type);
		TestUtility.storeProvenanceFile();
	}

	@Test
	public void refIdAgentIDTargetTest() throws URISyntaxException {
		int testPolicyId = 1;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** TEST: RULE TARGET TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void refIDActionTargetTest() throws URISyntaxException {
		int testPolicyId = 2;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** TEST: RULE TARGET TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void newIDTargetTest() throws URISyntaxException {
		int testPolicyId = 3;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** TEST: RULE TARGET TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);
		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void invalidRefIDTest() throws URISyntaxException {
		int testPolicyId = 4;
		String expectedOutcome = "Deny";

		logger.info("\n *********************************************** TEST: RULE TARGET TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void invalidDefaultIDTest() throws URISyntaxException {
		int testPolicyId = 5;
		String expectedOutcome = "Deny";

		logger.info("\n *********************************************** TEST: RULE TARGET TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void newMultipleIDtargetTest() throws URISyntaxException {
		int testPolicyId = 6;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** TEST: RULE TARGET TEST : "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void dRefIDtargetFalseTest() throws URISyntaxException {
		int testPolicyId = 7;
		String expectedOutcome = "Deny";

		logger.info("\n *********************************************** TEST: RULE TARGET TEST : "
				+ testPolicyId + "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void dRefIDtargetValidTest() throws URISyntaxException {
		int testPolicyId = 8;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** TEST: RULE TARGET TEST : "
				+ testPolicyId + "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void IDtargetValidTest() throws URISyntaxException {
		int testPolicyId = 9;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** TEST: RULE TARGET TEST : "
				+ testPolicyId + "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void dRefPolicyDiffValFromInputIDtargetValidTest()
			throws URISyntaxException {

		int testPolicyId = 10;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** TEST: RULE TARGET TEST : "
				+ testPolicyId + "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Ignore
	@Test
	public void testAllTargets() throws URISyntaxException {

		String xacmlPolicyDirectoryPath = "target/test-classes/policies/xacml/actual/"
				+ type;
		String xacmlRequestDirectoryPath = "target/test-classes/requests/xacml/actual/"
				+ type;

		String expectedOutcome[] = { "Permit", "Permit" };

		for (int i = 1; i < 3; i++) {

			logger.info("\n *********************************************** TEST: RULE TARGET TEST : "
					+ i + "   ******************************************");

			PolicyEngine policyEngine = new PolicyEngine();
			String outcome = policyEngine.executePolicy(
					xacmlPolicyDirectoryPath + "/policy" + i + "-xacml.xml",
					xacmlRequestDirectoryPath + "/request" + i + "-xacml.xml",
					null);

			Assert.assertTrue("Was Expecting outcome to be ==> '"
					+ expectedOutcome[i - 1] + "'",
					outcome.contains(expectedOutcome[i - 1]));
		}
	}

	@Ignore
	@Test
	public void thereExistTest() {
		Assert.fail("Not yet implemented");

	}

	/* Test for the case for All */
	@Ignore
	@Test
	public void forAllTest() {
		Assert.fail("Not yet implemented");
	}
}
