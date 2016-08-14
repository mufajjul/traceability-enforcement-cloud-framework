/*
 * @(#) TestUtility.java       1.1 14/8/2016
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.provenance.cloudprovenance.policyengine.service.PolicyEngine;
import com.provenance.cloudprovenance.sconverter.translate.ResourceTranslator;

/**
 * This class provides support for the tests, (policy conversion,
 * request conversion, etc)
 * 
 * @version 1.1 14 Aug 2016
 * @author Mufy
 * @Module PolicyEngine
 */
public class TestUtility {

	static Logger logger = Logger.getLogger(TestUtility.class);

	public static void generateSources(String type) throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		// Generate policies
		String cProvlPolicyDirectoryPath = "target/test-classes/policies/cprovl/"
				+ type;
		String xacmlPolicyConverterFile = "target/test-classes/transformer/cprovl-to-xacml-policy-converter-v.1.0.xsl";
		String xacmlPolicyDirectoryPath = "target/test-classes/policies/xacml/actual/"
				+ type;

		// generate requests
		String cProvlRequestDirectoryPath = "target/test-classes/requests/cprovl/"
				+ type;
		String xacmlRequestConverterFile = "target/test-classes/transformer/cprovl-to-xacml-request-converter-v.1.0.xsl";
		String xacmlRequestDirectoryPath = "target/test-classes/requests/xacml/actual/"
				+ type;

		ResourceTranslator converter = new ResourceTranslator();

		// generate the XACML policies
		converter.directoryFilesConverter(cProvlPolicyDirectoryPath,
				xacmlPolicyConverterFile, xacmlPolicyDirectoryPath);

		// generate the XACML requests

		converter.directoryFilesConverter(cProvlRequestDirectoryPath,
				xacmlRequestConverterFile, xacmlRequestDirectoryPath);
	}

	public static void storeProvenanceFile() throws IOException {

	}

	public static String executeTest(int policyId, String type)
			throws URISyntaxException {

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

		return outcome;
	}

	private static String readFile(String path, Charset encoding)
			throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}

}
