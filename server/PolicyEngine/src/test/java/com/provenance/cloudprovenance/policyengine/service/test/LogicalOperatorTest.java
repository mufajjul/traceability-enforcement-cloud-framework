/**
 * @file 		LogicalOperatorTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		PolicyEngine
 * @date 		18 05 2013
 * @version 	1.0
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
 * description: This class provide tests for logical operators (AND, OR,
 * NOT)
 * 
 * @author Mufy
 * 
 */
@Ignore
public class LogicalOperatorTest {

	private static Logger logger = Logger.getLogger(LogicalOperatorTest.class);

	static String type = "LogicalOperator";

	@BeforeClass
	public static void generateXACMLpolicies() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		PropertyConfigurator.configure(new File(
				"target/classes/log4j.properties").getAbsolutePath());

		logger.info("\n *********************************************** TEST: LOGICAL OPERATOR - Translating cProvl policies & reuests in XACML policies & requests************************************");

		TestUtility.generateSources(type);
		TestUtility.storeProvenanceFile();

	}

	@Test
	public void implicitAndLogicalOperator() throws URISyntaxException {

		int testPolicyId = 1;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** LOGICAL OPERATOR TEST: "
				+ testPolicyId
				+ "   ******************************************");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void implicitAndLogicalOperatorWithInvalidStatement()
			throws URISyntaxException {

		int testPolicyId = 2;
		String expectedOutcome = "Deny";

		logger.info("\n *********************************************** LOGICAL OPERATOR TEST: "
				+ testPolicyId
				+ "   ******************************************");
		logger.info("\n ---DESC: ------\n");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void explicitAndOperator() throws URISyntaxException {

		int testPolicyId = 3;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** LOGICAL OPERATOR TEST: "
				+ testPolicyId
				+ "   ******************************************");
		logger.info("\n ---DESC: ------\n");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void explicitOrOperator() throws URISyntaxException {

		int testPolicyId = 4;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** LOGICAL OPERATOR TEST: "
				+ testPolicyId
				+ "   ******************************************");
		logger.info("\n ---DESC: ------\n");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}

	@Test
	public void explicitNotOperator() throws URISyntaxException {

		int testPolicyId = 5;
		String expectedOutcome = "Deny";

		logger.info("\n *********************************************** LOGICAL OPERATOR TEST: "
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
		String xacmlPolicyDirectoryPath = "target/test-classes/policies/xacml/actual/"
				+ type;
		String xacmlRequestDirectoryPath = "target/test-classes/requests/xacml/actual/"
				+ type;

		String expectedOutcome[] = { "Deny", "Permit" };

		for (int i = 1; i < 4; i++) {

			logger.info("\n *********************************************** TEST: GROUPING  TEST : "
					+ i + "   ******************************************");

			PolicyEngine policyEngine = new PolicyEngine();
			logger.info("policy path ==> " + xacmlPolicyDirectoryPath
					+ "/policy" + i + "-xacml.xml");
			logger.info("request path ==> " + xacmlRequestDirectoryPath
					+ "/request" + i + "-xacml.xml");

			String outcome = policyEngine.executePolicy(
					xacmlPolicyDirectoryPath + "/policy" + i + "-xacml.xml",
					xacmlRequestDirectoryPath + "/request" + i + "-xacml.xml",
					null);

			Assert.assertTrue("Was Expecting outcome to be ==> '"
					+ expectedOutcome[i - 1] + "'",
					outcome.contains(expectedOutcome[i - 1]));

			logger.info(outcome);
		}
	}
}
