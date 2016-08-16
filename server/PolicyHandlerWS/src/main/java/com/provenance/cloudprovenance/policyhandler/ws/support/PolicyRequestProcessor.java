/*
 * @(#) PolicyRequestProcessor.java       1.1 16/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.policyhandler.ws.support;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.provenance.cloudprovenance.policyengine.service.PolicyEngine;
import com.provenance.cloudprovenance.sconverter.translate.ResourceTranslator;
import com.provenance.cloudprovenance.traceabilitystore.ns.CprovNamespacePrefixMapper;

/**
 * This class processes a policy request received from the client
 * 
 * @version 1.1 16 Aug 2016
 * @author Mufy
 * @Module PolicyHandlerWS
 */
public class PolicyRequestProcessor {

	@Autowired
	private PolicyEngine policyEngine;
	@Autowired
	CprovNamespacePrefixMapper cProvMapper;

	// Values obtained from the spring bean
	private String cProvlPolicyDirectoryPath;
	private String xacmlPolicyConverterFile;
	private String xacmlPolicyDirectoryPath;

	private String cProvlRequestDirectoryPath;
	private String xacmlRequestConverterFile;
	private String xacmlRequestDirectoryPath;

	@Autowired
	ResourceTranslator rsConverter;

	static Logger logger = Logger.getLogger("PolicyRequestProcessor");

	public String executePolicyRequest(String serviceId,
			String cprovlPolicyRequest, String[] policyToSelectId,
			HttpServletRequest request) throws TransformerException,
			URISyntaxException, ParserConfigurationException, SAXException,
			IOException {

		File styleSheetPath = new File(
				request.getRealPath(xacmlRequestConverterFile));

		String xacmlPolicyRequest = rsConverter.convertAcProvRequestToXACML(
				cprovlPolicyRequest, styleSheetPath);

		logger.info("conversion to XACML policy request successful: "
				+ xacmlPolicyRequest);

		// convert policy
		resourceConversion(request);

		// getPolicyAbsolutePath
		String policyPathRef = request.getRealPath(xacmlPolicyDirectoryPath)
				+ "/" + policyToSelectId[1];

		logger.info("path to policy one Id: " + policyPathRef);

		// String executionOutcome = policyEngine.executePolicy(
		String policyExecutionOutcome = policyEngine.executeWebPolicy(
				policyPathRef, xacmlPolicyRequest, serviceId);
		return policyExecutionOutcome;
	}

	public String validatePolicyRequest(String serviceId,
			String policyRequestId, String policyId,
			String policyRequestContent, HttpServletRequest request)
			throws IOException, URISyntaxException,
			ParserConfigurationException, SAXException, TransformerException {

		// convert policies
		this.resourceConversion(request);

		// execute policy
		String executionOutcome = policyEngine.executePolicy(
				request.getRealPath(xacmlPolicyDirectoryPath + "/policy"
						+ policyId + "-xacml.xml"),
				request.getRealPath(xacmlRequestDirectoryPath + "/request"
						+ policyId + "-xacml.xml"), null);

		return executionOutcome;
	}

	private void storeRequest(String requestId, String requestContent,
			HttpServletRequest request) throws IOException {

		File requestFile = new File(
				request.getRealPath(cProvlRequestDirectoryPath) + "/request"
						+ requestId + "-cprovl.xml");

		logger.info("File URI: " + requestFile.getAbsolutePath());

		requestFile.createNewFile();

		FileWriter fWritter = new FileWriter(requestFile);
		fWritter.write(requestContent);
		fWritter.close();
	}

	private void resourceConversion(HttpServletRequest request)
			throws URISyntaxException, ParserConfigurationException,
			SAXException, IOException, TransformerException {

		rsConverter.directoryFilesConverter(
				request.getRealPath(cProvlPolicyDirectoryPath),
				request.getRealPath(xacmlPolicyConverterFile),
				request.getRealPath(xacmlPolicyDirectoryPath));

	}

	public String getIdforPolicyMatch(String responseContent,
			String xpathToDocumentId) throws ParserConfigurationException,
			SAXException, IOException, XPathExpressionException,
			XPathFactoryConfigurationException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		InputSource is = new InputSource(new StringReader(responseContent));
		Document doc = builder.parse(is);

		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		xpath.setNamespaceContext(cProvMapper);

		XPathExpression xPathExpr;
		xPathExpr = xpath.compile(xpathToDocumentId);

		logger.debug("XpathExpression to match: " + xpathToDocumentId);
		logger.debug("Document to match is: " + responseContent);

		return (String) xPathExpr.evaluate(doc, XPathConstants.STRING);
	}

	/**
	 * @return the cProvlPolicyDirectoryPath
	 */
	public String getcProvlPolicyDirectoryPath() {
		return cProvlPolicyDirectoryPath;
	}

	/**
	 * @param cProvlPolicyDirectoryPath
	 *            the cProvlPolicyDirectoryPath to set
	 */
	public void setcProvlPolicyDirectoryPath(String cProvlPolicyDirectoryPath) {
		this.cProvlPolicyDirectoryPath = cProvlPolicyDirectoryPath;
	}

	/**
	 * @return the xacmlPolicyConverterFile
	 */
	public String getXacmlPolicyConverterFile() {
		return xacmlPolicyConverterFile;
	}

	/**
	 * @param xacmlPolicyConverterFile
	 *            the xacmlPolicyConverterFile to set
	 */
	public void setXacmlPolicyConverterFile(String xacmlPolicyConverterFile) {
		this.xacmlPolicyConverterFile = xacmlPolicyConverterFile;
	}

	/**
	 * @return the xacmlPolicyDirectoryPath
	 */
	public String getXacmlPolicyDirectoryPath() {
		return xacmlPolicyDirectoryPath;
	}

	/**
	 * @param xacmlPolicyDirectoryPath
	 *            the xacmlPolicyDirectoryPath to set
	 */
	public void setXacmlPolicyDirectoryPath(String xacmlPolicyDirectoryPath) {
		this.xacmlPolicyDirectoryPath = xacmlPolicyDirectoryPath;
	}

	/**
	 * @return the cProvlRequestDirectoryPath
	 */
	public String getcProvlRequestDirectoryPath() {
		return cProvlRequestDirectoryPath;
	}

	/**
	 * @param cProvlRequestDirectoryPath
	 *            the cProvlRequestDirectoryPath to set
	 */
	public void setcProvlRequestDirectoryPath(String cProvlRequestDirectoryPath) {
		this.cProvlRequestDirectoryPath = cProvlRequestDirectoryPath;
	}

	/**
	 * @return the xacmlRequestConverterFile
	 */
	public String getXacmlRequestConverterFile() {
		return xacmlRequestConverterFile;
	}

	/**
	 * @param xacmlRequestConverterFile
	 *            the xacmlRequestConverterFile to set
	 */
	public void setXacmlRequestConverterFile(String xacmlRequestConverterFile) {
		this.xacmlRequestConverterFile = xacmlRequestConverterFile;
	}

	/**
	 * @return the xacmlRequestDirectoryPath
	 */
	public String getXacmlRequestDirectoryPath() {
		return xacmlRequestDirectoryPath;
	}

	/**
	 * @param xacmlRequestDirectoryPath
	 *            the xacmlRequestDirectoryPath to set
	 */
	public void setXacmlRequestDirectoryPath(String xacmlRequestDirectoryPath) {
		this.xacmlRequestDirectoryPath = xacmlRequestDirectoryPath;
	}

}
