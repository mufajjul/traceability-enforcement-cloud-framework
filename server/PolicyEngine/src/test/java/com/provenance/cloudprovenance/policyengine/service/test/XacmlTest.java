/**
 * @file 		XacmlTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		PolicyEngine
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.policyengine.service.test;

import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.Assert;

import com.provenance.cloudprovenance.policyengine.service.PolicyEngine;

/**
 * @author Mufy
 * 
 */
@Ignore
public class XacmlTest {

	static Logger logger = Logger.getLogger(XacmlTest.class);

	@Ignore
	@Test
	public void testXACMLpolicyTest() throws URISyntaxException {

		String policyId = "1";
		String type = "xacml";

		String xacmlPolicyDirectoryPath = "target/test-classes/policies/xacml/actual/"
				+ type;
		String xacmlRequestDirectoryPath = "target/test-classes/requests/xacml/actual/"
				+ type;

		PolicyEngine policyEngine = new PolicyEngine();
		logger.info("policy path ==> " + xacmlPolicyDirectoryPath + "/policy"
				+ policyId + "-xacml.xml");
		logger.info("request path ==> " + xacmlRequestDirectoryPath
				+ "/request" + policyId + "-xacml.xml");

		String outcome = policyEngine.executePolicy(xacmlPolicyDirectoryPath
				+ "/policy" + policyId + "-xacml.xml",
				xacmlRequestDirectoryPath + "/request" + policyId
						+ "-xacml.xml", null);

		logger.info(outcome);

		Assert.notNull(outcome);
	}

	@Ignore
	@Test
	public void testXACMLpolicy2Test() throws URISyntaxException {

		String policyId = "2";
		String type = "xacml";

		String xacmlPolicyDirectoryPath = "target/test-classes/policies/xacml/actual/"
				+ type;
		String xacmlRequestDirectoryPath = "target/test-classes/requests/xacml/actual/"
				+ type;

		PolicyEngine policyEngine = new PolicyEngine();
		logger.info("policy path ==> " + xacmlPolicyDirectoryPath + "/policy"
				+ policyId + "-xacml.xml");
		logger.info("request path ==> " + xacmlRequestDirectoryPath
				+ "/request" + policyId + "-xacml.xml");

		String outcome = policyEngine.executePolicy(xacmlPolicyDirectoryPath
				+ "/policy" + policyId + "-xacml.xml",
				xacmlRequestDirectoryPath + "/request" + policyId
						+ "-xacml.xml", null);

		logger.info(outcome);

		Assert.notNull(outcome);
	}

	@Ignore
	@Test
	public void testXACMLxmlPolicyTest() throws URISyntaxException {

		String policyId = "3";
		String type = "xacml";

		String xacmlPolicyDirectoryPath = "target/test-classes/policies/xacml/actual/"
				+ type;
		String xacmlRequestDirectoryPath = "target/test-classes/requests/xacml/actual/"
				+ type;

		PolicyEngine policyEngine = new PolicyEngine();
		logger.info("policy path ==> " + xacmlPolicyDirectoryPath + "/policy"
				+ policyId + "-xacml.xml");
		logger.info("request path ==> " + xacmlRequestDirectoryPath
				+ "/request" + policyId + "-xacml.xml");

		String outcome = policyEngine.executePolicy(xacmlPolicyDirectoryPath
				+ "/policy" + policyId + "-xacml.xml",
				xacmlRequestDirectoryPath + "/request" + policyId
						+ "-xacml.xml", null);

		logger.info(outcome);

		Assert.notNull(outcome);
	}

}
