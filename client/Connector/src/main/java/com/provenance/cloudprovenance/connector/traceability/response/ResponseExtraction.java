/*
 * @(#) ResponseExtraction.java       1.1 13/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.connector.traceability.response;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.provenance.cloudprovenance.traceabilitystore.ns.CprovNamespacePrefixMapper;

public class ResponseExtraction {

	CprovNamespacePrefixMapper cProvMapper;
	String xpathToDocumentId;
	String xpathToResource;

	private static Logger logger = Logger.getLogger("ResponseExtraction");

	public String getResponseId(String responseContent)
			throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {

		return getXpathContent(responseContent, xpathToDocumentId);
	}

	public String getResponseURI(String responseContent)
			throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {
		return getXpathContent(responseContent, xpathToResource);

	}

	private String getXpathContent(String responseContent, String xPathVal)
			throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		InputSource is = new InputSource(new StringReader(responseContent));
		Document doc = builder.parse(is);

		// Create XPathFactory for creating XPath Object
		// XPathFactory xPathFactory = getXpathFactory();
		XPathFactory xPathFactory = XPathFactory.newInstance();

		// Create XPath object from XPathFactory
		XPath xpath = xPathFactory.newXPath();
		xpath.setNamespaceContext(cProvMapper);

		// Compile the XPath expression for getting all brands
		XPathExpression xPathExpr;

		// logger.info("XPath expression ==> " + xpathMatch);

		xPathExpr = xpath.compile(xPathVal);
		return (String) xPathExpr.evaluate(doc, XPathConstants.STRING);

	}

	public String getXpathToDocumentId() {
		return xpathToDocumentId;
	}

	public void setXpathToDocumentId(String xpathToDocumentId) {
		this.xpathToDocumentId = xpathToDocumentId;
	}

	public CprovNamespacePrefixMapper getcProvMapper() {
		return cProvMapper;
	}

	public void setcProvMapper(CprovNamespacePrefixMapper cProvMapper) {
		this.cProvMapper = cProvMapper;
	}

	public String getXpathToResource() {
		return xpathToResource;
	}

	public void setXpathToResource(String xpathToResource) {
		this.xpathToResource = xpathToResource;
	}
}
