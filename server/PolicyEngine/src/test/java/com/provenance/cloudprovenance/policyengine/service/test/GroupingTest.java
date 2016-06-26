/**
 * @file 		GroupingTest.java
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

/**
 * description: This class provide tests for grouping of statements 
 * 
 * @author Mufy
 * 
 */
@Ignore
public class GroupingTest {

	static Logger logger = Logger.getLogger(GroupingTest.class);

	static String type = "Grouping";
	
	@BeforeClass
	public static void generateXACMLpolicies() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		PropertyConfigurator.configure(new File(
				"target/classes/log4j.properties").getAbsolutePath());
		
		logger.info("\n *********************************************** TEST: GROUPING OPERATOR/STATEMENT - Translating cProvl policies & reuests in XACML policies & requests************************************");

		 TestUtility.generateSources(type);
			TestUtility.storeProvenanceFile();

	}
	
	@Test
	public void singleGroupingTest() throws URISyntaxException {
		int testPolicyId = 1;
		String expectedOutcome = "Deny";

		logger.info("\n *********************************************** GROUPING OPERATOR/STATEMENT TEST: "
				+ testPolicyId
				+ "   ******************************************");
		logger.info("\n ---DESC: ------\n");

		String outcome = TestUtility.executeTest(testPolicyId, type);
		
		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}
	
	@Test
	public void GroupingOrTest() throws URISyntaxException {
		int testPolicyId = 2;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** GROUPING OPERATOR/STATEMENT TEST: "
				+ testPolicyId
				+ "   ******************************************");
		logger.info("\n ---DESC: ------\n");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}
	
	@Test
	public void multipleGroupingTest() throws URISyntaxException {
		int testPolicyId = 3;
		String expectedOutcome = "Permit";

		logger.info("\n *********************************************** GROUPING OPERATOR/STATEMENT TEST: "
				+ testPolicyId
				+ "   ******************************************");
		logger.info("\n ---DESC: ------\n");

		String outcome = TestUtility.executeTest(testPolicyId, type);

		Assert.assertTrue("Expected outcome ==> '" + expectedOutcome + "'",
				outcome.contains(expectedOutcome));
	}
}
