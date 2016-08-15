/*
 * @(#) NamespaceResolver.java       1.1 14/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.storagecontroller.persistence.xpath;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

import com.provenance.cloudprovenance.storagecontroller.persistence.exception.ProvenanceStoreNodeException;
import com.provenance.cloudprovenance.storagecontroller.presistence.xmldb.XmlDbConnector;
import com.provenance.cloudprovenance.storagecontroller.presistence.xmldb.XmlDbService;

/**
 * This class provides methods for various xPath quires (searching, matching,
 * etc) for XML-based traceability files
 * 
 * @version 1.1 14 Aug 2016
 * @author Mufy
 * @Module StorageController
 */
public class XPathUtility {

	private XmlDbConnector conn;
	private XmlDbService service;
	private String serviceId;
	private String traceabilityType;

	static Logger logger = Logger.getLogger(XPathUtility.class);

	public XPathUtility(String serviceId, String traceabilityType,
			XmlDbConnector dbCon, XmlDbService dbService)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, XMLDBException {
		conn = dbCon;
		service = dbService;

		this.serviceId = serviceId;
		this.traceabilityType = traceabilityType;
	}

	public XPathUtility() {
	}

	public boolean matchNodeIdByxQuery(String xpathMatch)
			throws URISyntaxException, XMLDBException {

		return service.xQueryTraceabilityStore(xpathMatch, serviceId,
				traceabilityType);

	}

	@Deprecated
	public Node getMatchNode(String xpathMatch) throws XMLDBException,
			URISyntaxException, ParserConfigurationException, SAXException,
			IOException {

		Object result = null;

		Resource resourceList[] = service.getAlltraceabilityResources(
				serviceId, traceabilityType);

		logger.info("Store resourse found ==> " + resourceList.length);

		for (int i = 0; i < resourceList.length; i++) {
			String xmlContent = (String) resourceList[i].getContent();

			logger.debug("Store content ==> " + xmlContent);

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();

			InputSource is = new InputSource(new StringReader(xmlContent));
			Document doc = builder.parse(is);

			// Create XPathFactory for creating XPath Object
			XPathFactory xPathFactory = getXpathFactory();

			// Create XPath object from XPathFactory
			XPath xpath = xPathFactory.newXPath();
			xpath.setNamespaceContext(new NamespaceResolver(doc));

			// Compile the XPath expression
			XPathExpression xPathExpr;

			try {
				logger.info("XPath expression:  " + xpathMatch);

				xPathExpr = xpath.compile(xpathMatch);
				result = xPathExpr.evaluate(doc, XPathConstants.NODE);

				if (result != null) {
					logger.info("Matched node: "
							+ ((Node) result).getTextContent());

					return (Node) result;
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		return (Node) result;
	}

	public Node getMatchNodewithXQuery(String xpathMatch)
			throws XMLDBException, URISyntaxException,
			ParserConfigurationException, SAXException, IOException {

		String aggratedXmlContent = service
				.getAllTraceabilityxQueryRefinedResources(serviceId,
						traceabilityType, xpathMatch);

		logger.debug("Store resourse found ==> " + aggratedXmlContent);

		if (aggratedXmlContent == null || aggratedXmlContent.equals("")) {
			return null;
		} else {
			// wrap it using the provenance root node
			aggratedXmlContent = service
					.wrapNodesUsingCProvRootNode(aggratedXmlContent);

			logger.debug(aggratedXmlContent);
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		InputSource is = new InputSource(new StringReader(aggratedXmlContent));
		Document doc = builder.parse(is);

		logger.debug("Doc element : "
				+ doc.getDocumentElement().getTextContent());

		return doc.getDocumentElement();

	}

	// Updated with xQuery
	public NodeList getMatchedNodeListFromStore(String xpathMatch)
			throws URISyntaxException, XMLDBException,
			ParserConfigurationException, SAXException, IOException {

		Object result = null;
		String xmlContent = service.getAllTraceabilityxQueryRefinedResources(
				serviceId, traceabilityType, xpathMatch);

		logger.debug("Store content ==> " + xmlContent);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		InputSource is = new InputSource(new StringReader(xmlContent));
		Document doc = builder.parse(is);

		XPathFactory xPathFactory = getXpathFactory();
		XPath xpath = xPathFactory.newXPath();
		xpath.setNamespaceContext(new NamespaceResolver(doc));

		// Compile the XPath expression
		XPathExpression xPathExpr;

		try {
			logger.info("XPath expression ==> " + xpathMatch);

			xPathExpr = xpath.compile(xpathMatch);
			result = xPathExpr.evaluate(doc, XPathConstants.NODESET);

			if (result != null)
				logger.info("Matched node ==> "
						+ ((NodeList) result).getLength());

			printXpathResult(result);

			return (NodeList) result;

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return (NodeList) result;
	}

	// Replaced with XQuery
	public NodeList getMatchNodeListWithXquery(String xpathMatch)
			throws XMLDBException, URISyntaxException,
			ParserConfigurationException, SAXException, IOException {

		String matchedResourceXmlContent = service
				.getAllTraceabilityxQueryRefinedResources(serviceId,
						traceabilityType, xpathMatch);

		logger.debug("Aggegrate resource match  ==> "
				+ matchedResourceXmlContent);
		Object result = null;

		logger.debug("Store content ==> " + matchedResourceXmlContent);

		if (matchedResourceXmlContent == null
				|| matchedResourceXmlContent.equals("")) {
			return null;
		}

		if (matchedResourceXmlContent == null
				|| matchedResourceXmlContent.equals("")) {
			return null;
		} else {
			// wrap it using the provenance root node
			matchedResourceXmlContent = service
					.wrapNodesUsingCProvRootNode(matchedResourceXmlContent);

			logger.debug(matchedResourceXmlContent);
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		InputSource is = new InputSource(new StringReader(
				matchedResourceXmlContent));
		Document doc = builder.parse(is);

		XPathFactory xPathFactory = getXpathFactory();
		XPath xpath = xPathFactory.newXPath();
		xpath.setNamespaceContext(new NamespaceResolver(doc));
		XPathExpression xPathExpr;

		try {
			xpathMatch = "//*";

			xPathExpr = xpath.compile(xpathMatch);
			result = xPathExpr.evaluate(doc, XPathConstants.NODESET);

			if (result != null)
				logger.debug("Matched node ==> "
						+ ((NodeList) result).getLength());

			printXpathResult(result);
			return (NodeList) result;

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return (NodeList) result;
	}

	public Node fileNodeMatch(File targetFile, String xpathMatch)
			throws ParserConfigurationException, SAXException, IOException {

		Object result = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document doc = builder.parse(targetFile);

		try {

			XPathFactory xPathFactory = getXpathFactory();
			XPath xpath = xPathFactory.newXPath();
			xpath.setNamespaceContext(new NamespaceResolver(doc));
			XPathExpression xPathExpr;

			logger.debug("Document ==> " + doc.getTextContent());
			logger.info("XPath expression ==> " + xpathMatch);
			logger.debug("Matching file ==> " + targetFile.getAbsolutePath()
					+ "  " + targetFile.exists());

			xPathExpr = xpath.compile(xpathMatch);
			result = xPathExpr.evaluate(doc, XPathConstants.NODE);

			if (result != null)
				logger.debug("Matched node ==> "
						+ ((Node) result).getNodeName());

			return (Node) result;

		} catch (XPathExpressionException e) {

			e.printStackTrace();
			return (Node) result;
		}
	}

	public XPathFactory getXpathFactory() {

		// return XPathFactory.newInstance();
		/*
		 * try { XPathFactory xPathFactory = XPathFactory.newInstance(
		 * XPathFactory.DEFAULT_OBJECT_MODEL_URI,
		 * "net.sf.saxon.xpath.XPathFactoryImpl",
		 * ClassLoader.getSystemClassLoader()); return xPathFactory; } catch
		 * (XPathFactoryConfigurationException e) {
		 * 
		 * e.printStackTrace(); }
		 */
		return XPathFactory.newInstance();

	}

	public NodeList fileDynamicVariableNodeMatch(File targetFile,
			String xpathMatch, Node provNode)
			throws ParserConfigurationException, SAXException, IOException {

		Object result = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(targetFile);

		XPathFactory xPathFactory = getXpathFactory();
		XPath xpath = xPathFactory.newXPath();
		xpath.setNamespaceContext(new NamespaceResolver(doc));
		XPathExpression xPathExpr;

		try {
			logger.debug("XPath expression ==> " + xpathMatch);
			xPathExpr = xpath.compile(xpathMatch);
			result = xPathExpr.evaluate(provNode, XPathConstants.NODESET);

			if (result != null)
				logger.info("Matched node ==> "
						+ ((NodeList) result).getLength());
			NodeList tempList = (NodeList) result;

			for (int i = 0; i < tempList.getLength(); i++) {
				logger.info(tempList.item(i).getTextContent());
				logger.info(tempList.item(i).getNodeName());
			}

			return (NodeList) result;

		} catch (XPathExpressionException e) {

			e.printStackTrace();
			return (NodeList) result;
		}
	}

	public boolean compareNodes(Node expected, Node actual)
			throws ProvenanceStoreNodeException {
		if (expected.getNodeType() != actual.getNodeType()) {
			throw new ProvenanceStoreNodeException("Different types of nodes: "
					+ expected + " " + actual);
		}
		if (expected instanceof Document) {
			Document expectedDoc = (Document) expected;
			Document actualDoc = (Document) actual;
			compareNodes(expectedDoc.getDocumentElement(),
					actualDoc.getDocumentElement());
		} else if (expected instanceof Element) {
			Element expectedElement = (Element) expected;
			Element actualElement = (Element) actual;

			// compare element names
			if (!expectedElement.getLocalName().equals(
					actualElement.getLocalName())) {
				throw new ProvenanceStoreNodeException(
						"Element names do not match: "
								+ expectedElement.getLocalName() + " "
								+ actualElement.getLocalName());
			}
			// compare element ns
			String expectedNS = expectedElement.getNamespaceURI();
			String actualNS = actualElement.getNamespaceURI();
			if ((expectedNS == null && actualNS != null)
					|| (expectedNS != null && !expectedNS.equals(actualNS))) {
				throw new ProvenanceStoreNodeException(
						"Element namespaces names do not match: " + expectedNS
								+ " " + actualNS);
			}

			String elementName = "{" + expectedElement.getNamespaceURI() + "}"
					+ actualElement.getLocalName();

			// compare attributes
			NamedNodeMap expectedAttrs = expectedElement.getAttributes();
			NamedNodeMap actualAttrs = actualElement.getAttributes();
			if (countNonNamespaceAttribures(expectedAttrs) != countNonNamespaceAttribures(actualAttrs)) {
				throw new ProvenanceStoreNodeException(elementName
						+ ": Number of attributes do not match up: "
						+ countNonNamespaceAttribures(expectedAttrs) + " "
						+ countNonNamespaceAttribures(actualAttrs));
			}
			for (int i = 0; i < expectedAttrs.getLength(); i++) {
				Attr expectedAttr = (Attr) expectedAttrs.item(i);
				if (expectedAttr.getName().startsWith("xmlns")) {
					continue;
				}
				Attr actualAttr = null;
				if (expectedAttr.getNamespaceURI() == null) {
					actualAttr = (Attr) actualAttrs.getNamedItem(expectedAttr
							.getName());
				} else {
					actualAttr = (Attr) actualAttrs.getNamedItemNS(
							expectedAttr.getNamespaceURI(),
							expectedAttr.getLocalName());
				}
				if (actualAttr == null) {
					throw new ProvenanceStoreNodeException(elementName
							+ ": No attribute found:" + expectedAttr);
				}
				if (!expectedAttr.getValue().equals(actualAttr.getValue())) {

					// check if it is the root node attribute ID
					if (expectedAttr.getName().equals("prov:id")) {
						logger.info("Node Id, will be different, therefore skipping");
					} else {

						throw new ProvenanceStoreNodeException(elementName
								+ ": Attribute values do not match: "
								+ expectedAttr.getValue() + " "
								+ actualAttr.getValue());
					}
				}
			}

			// compare children
			NodeList expectedChildren = expectedElement.getChildNodes();
			NodeList actualChildren = actualElement.getChildNodes();

			logger.debug("---------------expected children ---------------");
			printXpathResult(expectedChildren);

			logger.debug("---------------actual children ---------------");
			printXpathResult(actualChildren);

			if (expectedChildren.getLength() != actualChildren.getLength()) {
				throw new ProvenanceStoreNodeException(elementName
						+ ": Number of children do not match up: "
						+ expectedChildren.getLength() + " "
						+ actualChildren.getLength());
			}
			for (int i = 0; i < expectedChildren.getLength(); i++) {
				Node expectedChild = expectedChildren.item(i);
				Node actualChild = actualChildren.item(i);
				compareNodes(expectedChild, actualChild);
			}
		} else if (expected instanceof Text) {
			String expectedData = ((Text) expected).getData().trim();
			String actualData = ((Text) actual).getData().trim();

			if (!expectedData.equals(actualData)) {
				throw new ProvenanceStoreNodeException("Text does not match: "
						+ expectedData + " " + actualData);
			}
		}
		return true;
	}

	private static int countNonNamespaceAttribures(NamedNodeMap attrs) {
		int n = 0;
		for (int i = 0; i < attrs.getLength(); i++) {
			Attr attr = (Attr) attrs.item(i);
			if (!attr.getName().startsWith("xmlns")) {
				n++;
			}
		}
		return n;
	}

	public void printXpathResult(Object result) {
		NodeList nodes = (NodeList) result;
		for (int i = 0; i < nodes.getLength(); i++) {

			logger.debug("Node name ==> " + nodes.item(i).getNodeName()
					+ "  Node type ==> " + nodes.item(i).getNodeType()
					+ " Node value ==> " + nodes.item(i).getNodeValue()
					+ " Node content ==> " + nodes.item(i).getTextContent());
			if (nodes.item(i).getNodeName().equals("#text")) {
				continue;
			}

			NamedNodeMap map = nodes.item(i).getAttributes();
			logger.debug("getting the attributes ==>" + map.getLength());
			for (int j = 0; j < map.getLength(); j++) {
				logger.debug("Node Var name ==> " + map.item(j).getNodeName()
						+ "  Node Var type ==> " + map.item(j).getNodeType()
						+ " Node Var value ==> " + map.item(j).getNodeValue()
						+ " Node Var content ==> "
						+ map.item(j).getTextContent());
			}
		}
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getTraceabilityType() {
		return traceabilityType;
	}

	public void setTraceabilityType(String traceabilityType) {
		this.traceabilityType = traceabilityType;
	}

	public XmlDbConnector getConn() {
		return conn;
	}

	public void setConn(XmlDbConnector conn) {
		this.conn = conn;
	}

	public XmlDbService getService() {
		return service;
	}

	public void setService(XmlDbService service) {
		this.service = service;
	}
}
