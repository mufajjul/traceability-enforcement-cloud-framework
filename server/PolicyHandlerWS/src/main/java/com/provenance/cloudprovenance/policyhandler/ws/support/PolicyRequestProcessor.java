/**
 * @file 		PolicyRequestProcessor.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		PolicyHandlerWS
 * @date 		18 05 2013
 * @version 	1.0
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
* 
* @author Mufy
* 
*/

public class PolicyRequestProcessor {

	@Autowired
	private PolicyEngine policyEngine;
	@Autowired
	CprovNamespacePrefixMapper cProvMapper;

	// String xpathToDocumentId;

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
		
		logger.info("conversion to XACML policy request successful: "+xacmlPolicyRequest);

		// convert policy
		resourceConversion(request);

		// getPolicyAbsolutePath
		String policyPathRef = request.getRealPath(xacmlPolicyDirectoryPath)+"/"+policyToSelectId[1];
		
		logger.info("path to policy one Id: "+policyPathRef);

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

		// store request
		// this.storeRequest(policyRequestId, policyRequestContent, request);

		// convert request

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

		// generate the XACML policies

		// long startTime = System.currentTimeMillis();

		rsConverter.directoryFilesConverter(
				request.getRealPath(cProvlPolicyDirectoryPath),
				request.getRealPath(xacmlPolicyConverterFile),
				request.getRealPath(xacmlPolicyDirectoryPath));

		// long endTime = System.currentTimeMillis();
		// logger.info("Policy translation took: start time: " + startTime
		// + " ; endTime: " + endTime + " ; Diff:" + (endTime - startTime));

		// generate the XACML requests

		// startTime = System.currentTimeMillis();

		// converter.directoryFilesConverter(
		// request.getRealPath(cProvlRequestDirectoryPath),
		// request.getRealPath(xacmlRequestConverterFile),
		// request.getRealPath(xacmlRequestDirectoryPath));

		// endTime = System.currentTimeMillis();
		// logger.info("Request translation took: start time: " + startTime
		// + " ; endTime: " + endTime + " ; Diff:" + (endTime - startTime));
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

		logger.info("XpathExpression to match: "+xpathToDocumentId);

		logger.info("Document to match is: "+responseContent);

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
