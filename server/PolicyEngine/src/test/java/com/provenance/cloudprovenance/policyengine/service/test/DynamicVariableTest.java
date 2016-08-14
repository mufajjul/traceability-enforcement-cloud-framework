/*
 * @(#) DynamicVariableTest.java       1.1 14/8/2016
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
 * description: This class provide tests for variables (default, new and ref)
 * 
 * @version 1.1 14 Aug 2016
 * @author Mufy
 * @Module PolicyEngine
 */
@Ignore
public class DynamicVariableTest {

	static Logger log = Logger.getLogger(DynamicVariableTest.class);

	static String type = "DynamicVariable";

	@BeforeClass
	public static void generateXACMLpolicies() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		PropertyConfigurator.configure(new File(
				"target/classes/log4j.properties").getAbsolutePath());

		log.info("\n *********************************************** TEST: CONDITIONAL DYNAMIC - Translating cProvl policies & reuests in XACML policies & requests************************************");

		TestUtility.generateSources(type);
		TestUtility.storeProvenanceFile();
	}

	/**
	 * This test uses a dynamic variable (r:a001) in a conditional statement,
	 * and generates it content through policy statement validation against the
	 * policy store. The value retrieved is store for consequent statements
	 */
	@Ignore
	@Test
	public void newVairableWithin_A_StatementTest() throws URISyntaxException {
		String expectedOutcome = "Permit";
		int testPolicyId = 1;

		log.info("\n *********************************************** CONDITIONAL DYNAMIC VARIABLE TEST :  1    ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	/**
	 * This test uses a dynamic variable (r:a001) in a conditional statement,
	 * and generates it content through policy statement validation against the
	 * policy store. The value retrieved is store is used for the next
	 * statement.
	 */
	@Ignore
	@Test
	public void newVairableWithin_multiple_StatementsTest()
			throws URISyntaxException {

		String expectedOutcome = "Permit";
		int testPolicyId = 2;

		log.info("\n *********************************************** CONDITIONAL DYNAMIC VARIABLE TEST :  2    ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	/**
	 * This test uses a dynamic variable (r:a001) in a conditional statement,
	 * and generates it content through policy statement validation against the
	 * policy store. The value retrieved is store is used for the next
	 * statement, however ex:ag013 in this statement does not exist, and should
	 * fail.
	 */
	@Ignore
	@Test
	public void newVairableWithin_multiple_Statements_withWrongDetailsTest()
			throws URISyntaxException {

		int testPolicyId = 3;
		String expectedOutcome = "Deny";

		log.info("\n *********************************************** CONDITIONAL DYNAMIC VARIABLE TEST :  3    ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	/**
	 * This test uses a ref variable (ex:ag001) in a conditional statement. It
	 * is first declared within in the target statement, the content of the
	 * variable will be matched against the input request, if successful, will
	 * be used within the conditional statement. It should return valid.
	 */
	@Ignore
	@Test
	public void refVairableWithin_a_Statement() throws URISyntaxException {
		int testPolicyId = 4;
		String expectedOutcome = "Permit";

		log.info("\n *********************************************** CONDITIONAL DYNAMIC VARIABLE TEST :  4    ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	/**
	 * This test uses a ref variable (ex:ag010) in a conditional statement. It
	 * is first declared within in the target statement, the content of the
	 * variable will be matched against the input request, if successful, will
	 * be used within the conditional statement. ex:ag010 does not exist, should
	 * return invalid.
	 */
	@Ignore
	@Test
	public void refInvalidVairableWithin_a_Statement()
			throws URISyntaxException {

		int testPolicyId = 5;
		String expectedOutcome = "Deny";

		log.info("\n *********************************************** CONDITIONAL DYNAMIC VARIABLE TEST :  5    ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	/**
	 * Ref and New vairables within statements, should return true
	 */
	@Ignore
	@Test
	public void refAndNewVairableWithin_Statements() throws URISyntaxException {
		int testPolicyId = 6;
		String expectedOutcome = "Permit";

		log.info("\n *********************************************** CONDITIONAL DYNAMIC VARIABLE TEST :  6    ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	/**
	 * Ref and New vairables within statements, should return true
	 */
	@Ignore
	@Test
	public void srefAndNewVairableWithin_Statements() throws URISyntaxException {
		int testPolicyId = 7;
		String expectedOutcome = "Permit";

		log.info("\n *********************************************** CONDITIONAL DYNAMIC VARIABLE TEST :  7    ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void srefWithTwoAgentsVairableWithin_Statements()
			throws URISyntaxException {
		int testPolicyId = 8;
		String expectedOutcome = "Permit";

		log.info("\n *********************************************** CONDITIONAL DYNAMIC VARIABLE TEST :  8    ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Ignore
	@Test
	public void testAll() throws URISyntaxException {
		String xacmlPolicyDirectoryPath = "target/test-classes/policies/xacml/actual/"
				+ type;
		String xacmlRequestDirectoryPath = "target/test-classes/requests/xacml/actual/"
				+ type;

		for (int i = 1; i < 3; i++) {

			log.info("\n *********************************************** TEST: CONDITIONAL DYNAMIC VARIABLE TEST : "
					+ i + "   ******************************************");

			PolicyEngine policyEngine = new PolicyEngine();
			log.info("policy path ==> " + xacmlPolicyDirectoryPath + "/policy"
					+ i + "-xacml.xml");
			log.info("request path ==> " + xacmlRequestDirectoryPath
					+ "/request" + i + "-xacml.xml");

			String outcome = policyEngine.executePolicy(
					xacmlPolicyDirectoryPath + "/policy" + i + "-xacml.xml",
					xacmlRequestDirectoryPath + "/request" + i + "-xacml.xml",
					null);
			Assert.assertNotNull(outcome);
		}
	}
}
