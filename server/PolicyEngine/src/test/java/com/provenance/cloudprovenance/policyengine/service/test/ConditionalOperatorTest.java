/*
 * @(#) ConditionalOperatorTest.java       1.1 14/8/2016
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
 * This class provide tests for conditional operator (=, >, < etc)
 * 
 * @version 1.1 14 Aug 2016
 * @author Mufy
 * @Module PolicyEngine
 */
//@Ignore
public class ConditionalOperatorTest {

	static Logger logger = Logger.getLogger(ConditionalOperatorTest.class);

	static String type = "ConditionalOperator";

	@BeforeClass
	public static void generateXACMLpolicies() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		PropertyConfigurator.configure(new File(
				"target/classes/log4j.properties").getAbsolutePath());

		logger.info("\n *********************************************** TEST: CONDITIONAL OPERATOR - Translating cProvl policies & reuests in XACML policies & requests************************************");

		TestUtility.generateSources(type);
		TestUtility.storeProvenanceFile();
		
	}

	@Test
	public void conditionalOperatorEqualTest() throws URISyntaxException {
		int testPolicyId = 1;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** CONDITIONAL OPERATOR TEST: "
				+ testPolicyId
				+ "   ******************************************");
		logger.info("\n ---DESC: ------\n");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void conditionalOperatorEqualWithSingleInvalidFieldTest()
			throws URISyntaxException {

		int testPolicyId = 2;
		String expectedOutcome = "Deny";

		logger.info("\n *********************************************** CONDITIONAL OPERATOR TEST: "
				+ testPolicyId
				+ "   ******************************************");
		logger.info("\n ---DESC: ------\n");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void conditionalOperatorEqualWithMultipleInvalidFieldsTest()
			throws URISyntaxException {

		int testPolicyId = 3;
		String expectedOutcome = "Deny";

		logger.info("\n *********************************************** CONDITIONAL OPERATOR TEST: "
				+ testPolicyId
				+ "   ******************************************");
		logger.info("\n ---DESC: ------\n");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	// Needs to be implemented
	@Ignore
	@Test
	public void greaterOperatorTest() {
		Assert.fail();
	}

	@Ignore
	@Test
	public void lessOperatorTest() {
		Assert.fail();
	}

	@Ignore
	@Test
	public void greaterThanEqualOperatorTest() {
		Assert.fail();
	}

	@Ignore
	@Test
	public void lessThanEqualOperatorTest() {
		Assert.fail();
	}
}
