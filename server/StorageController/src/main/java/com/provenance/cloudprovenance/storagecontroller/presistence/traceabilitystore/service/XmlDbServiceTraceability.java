/**
 * @file 		XmlDbServiceTraceability.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		StorageController
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.storagecontroller.presistence.traceabilitystore.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

import com.provenance.cloudprovenance.storagecontroller.persistence.xpath.NamespaceResolver;
import com.provenance.cloudprovenance.storagecontroller.presistence.xmldb.XmlDbService;

/**
 * Provenance store interface implementation
 * 
 * @author Mufy
 * 
 */

@SuppressWarnings("unused")
public class XmlDbServiceTraceability implements ServiceTraceability<String> {

	static Logger logger = Logger.getLogger("XmlDbStore");
	static XmlDbService dbService;
	final String RESOURCE_TYPE;

	public XmlDbServiceTraceability(XmlDbService xmlDbService,
			String resourceType) {
		dbService = xmlDbService;
		RESOURCE_TYPE = resourceType;
	}

	// TODO - to be implemented
	public int currentTraceabilityRecordSize(String serviceId,
			String traceabilityType, String instanceId) {

		try {
			return dbService.getResourceSize(serviceId, traceabilityType,
					instanceId);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return -1;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public boolean createTraceabilityEntry(String serviceId,
			String traceabilityType, String instanceId, String entryType,
			String entryItem) {

		logger.debug("Parameter received ==> seviceId:" + serviceId
				+ "; traceabilityType:" + traceabilityType + "; instanceId:"
				+ instanceId + "; entryType:" + entryType + ";\n entryItem: "
				+ entryItem);

		logger.info("getting the traceability file ==> " + instanceId);

		String traceabilityFileContent = this.getTraceabilityInstance(
				serviceId, traceabilityType, instanceId);

		logger.debug("Current traceability file content ==> "
				+ traceabilityFileContent);

		String updatedTraceabilityFileContent;
		try {

			logger.info("Updating the traceability file ");
			updatedTraceabilityFileContent = updateXMLfileContent(
					traceabilityFileContent, entryItem);

			logger.info("Store the new file contents");
			this.createTraceabilityInstance(serviceId, traceabilityType,
					instanceId, updatedTraceabilityFileContent);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}

	}

	public boolean createTraceabilityInstance(String serviceId,
			String traceabilityType, String instanceId) {

		File tempTraceabilityFile = new File(instanceId);
		boolean fileStatus;
		try {
			fileStatus = tempTraceabilityFile.createNewFile();

			BufferedWriter writer;
			writer = new BufferedWriter(new FileWriter(tempTraceabilityFile));

			String tDoc[] = createTraceabilityDocTemplate();

			for (int i = 0; i < tDoc.length; i++) {
				writer.write(tDoc[i]);
			}

			writer.close();

			dbService.addTraceabilityResource(serviceId, traceabilityType,
					instanceId, tempTraceabilityFile, RESOURCE_TYPE);

			// Clean up
			tempTraceabilityFile.delete();

			return true;

		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean createTraceabilityInstance(String serviceId,
			String traceabilityType, String instanceId, String content) {

		File traceabilityFile = new File(instanceId);
		boolean fileStatus;
		try {
			fileStatus = traceabilityFile.createNewFile();

			BufferedWriter writer;
			writer = new BufferedWriter(new FileWriter(traceabilityFile));

			writer.write(content);

			writer.close();

			dbService.addTraceabilityResource(serviceId, traceabilityType,
					instanceId, traceabilityFile, RESOURCE_TYPE);
			return true;

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
	}

	// TODO - review this method implementation
	public String[] getAllTraceabilityInstances(String serviceId,
			String traceabilityType) {

		// TODO - conver resource to string content .... prob better to use
		// collection ...
		try {
			Resource[] res = dbService.getAlltraceabilityResources(serviceId,
					traceabilityType);
		} catch (XMLDBException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getTraceabilityInstance(String serviceId,
			String traceabilityType, String instanceId) {

		try {
			Resource content = dbService.getTraceabilityResource(serviceId,
					traceabilityType, instanceId);

			return (String) content.getContent();

		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/** Update the traceability file in XMLDB store */

	public boolean updateTraceabilityEntry(String serviceId,
			String traceabilityType, String instanceId, String entryType,
			String entryItem) {

		try {

			logger.debug("Parameter received ==> seviceId:" + serviceId
					+ "; traceabilityType:" + traceabilityType
					+ "; instanceId:" + instanceId + "; entryType:" + entryType
					+ ";\n entryItem: " + entryItem.length());

			// String traceabilityElementsOnly =
			// selectAllTraceabilityElements(entryItem);

			/*
			 * String pattern1 = "<cprov:traceabilityDocument.*?>"; Pattern p =
			 * Pattern.compile(pattern1, Pattern.DOTALL); String
			 * removeRootNodeOpenNode = p.matcher(entryItem).replaceFirst("");
			 * //true String removeRootNodeCloseNode =
			 * removeRootNodeOpenNode.replaceFirst
			 * ("</cprov:traceabilityDocument>", "");
			 * 
			 * String pattern2 = "<\\?xml.*?\\?>"; Pattern p2 =
			 * Pattern.compile(pattern2, Pattern.DOTALL); String
			 * removeRootNodeNoXML =
			 * p2.matcher(removeRootNodeCloseNode).replaceFirst(""); //true
			 * 
			 * String removeBlankLine = removeRootNodeNoXML.replaceFirst("\n",
			 * "");
			 * 
			 * logger.info("After the removal :"+removeBlankLine);
			 */

			// Names space is a problem for XQuery, needs to be removed and XML
			// declaration

			String pattern2 = "<\\?xml.*?\\?>";
			Pattern p2 = Pattern.compile(pattern2, Pattern.DOTALL);
			entryItem = p2.matcher(entryItem).replaceFirst(""); // true

			String pattern1 = " xmlns.*?>";
			Pattern p = Pattern.compile(pattern1, Pattern.DOTALL);
			entryItem = p.matcher(entryItem).replaceFirst(">"); // true

			logger.info("removed namespace: " + entryItem);
			dbService.updateTraceabilityResource(serviceId, traceabilityType,
					instanceId, entryType, entryItem);

		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean removeTraceabiltiltyInstance(String serviceId,
			String traceabilityType, String instanceId) {

		try {
			dbService.deleteTraceabilityResource(serviceId, traceabilityType,
					instanceId);
			return true;
		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
	}

	public synchronized String selectAllTraceabilityElements(
			String xmlFileContent) throws SAXException, IOException,
			ParserConfigurationException, TransformerException,
			XPathExpressionException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		InputSource storeFileIs = new InputSource(new StringReader(
				xmlFileContent));
		Document storeFileDoc = builder.parse(storeFileIs);
		XPathFactory xPathFactory = XPathFactory.newInstance();

		XPath xpath = xPathFactory.newXPath();
		xpath.setNamespaceContext(new NamespaceResolver(storeFileDoc));
		XPathExpression xPathExpr;
		xPathExpr = xpath.compile("//cprov:traceabilityDocument");
		Node allElements = (Node) xPathExpr.evaluate(storeFileDoc,
				XPathConstants.NODE);

		return allElements.getNodeName();

	}

	public synchronized String updateXMLfileContent(String xmlFileContent,
			String nodeContent) throws SAXException, IOException,
			ParserConfigurationException, TransformerException,
			XPathExpressionException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		InputSource storeFileIs = new InputSource(new StringReader(
				xmlFileContent));
		Document storeFileDoc = builder.parse(storeFileIs);

		InputSource inputNodeIs = new InputSource(new StringReader(nodeContent));
		Document inputNodeDoc = builder.parse(inputNodeIs);

		logger.info("Child Size ==> "
				+ inputNodeDoc.getChildNodes().getLength());

		XPathFactory xPathFactory = XPathFactory.newInstance();

		// Create XPath object from XPathFactory
		XPath xpath = xPathFactory.newXPath();
		xpath.setNamespaceContext(new NamespaceResolver(storeFileDoc));

		// Compile the XPath expression for getting all brands
		XPathExpression xPathExpr;

		// logger.info("XPath expression ==> " + xpathMatch);

		xPathExpr = xpath.compile("cprov:traceabilityDocument");
		Object result = xPathExpr.evaluate(storeFileDoc, XPathConstants.NODE);

		Node currNode = (Node) result;// storeFileDoc.getElementsByTagName(
		// "cprov:traceabilityDocument").item(0);
		logger.info("Text Content==> content:" + currNode.getTextContent()
				+ "; NodeType:" + currNode.getNodeType() + "; nodeName: "
				+ currNode.getNodeName());

		Node cNode = clean(inputNodeDoc.getElementsByTagName(
				"cprov:traceabilityDocument").item(0));

		for (int j = 0; j < cNode.getChildNodes().getLength(); j++) {
			logger.debug("Exisiting doc root Node name ==> "
					+ cNode.getChildNodes().item(j).getNodeName());

			Node tempNode = clean(cNode.getChildNodes().item(j));
			tempNode = storeFileDoc.importNode(tempNode, true);

			currNode.appendChild(tempNode);
		}

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();

		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(storeFileDoc), new StreamResult(
				writer));
		String output = writer.getBuffer().toString().replaceAll("\n|\r", "");

		logger.info("Serialized Content ==> " + output);

		return output;
	}

	public Node clean(Node node) {
		NodeList childNodes = node.getChildNodes();

		for (int n = childNodes.getLength() - 1; n >= 0; n--) {
			Node child = childNodes.item(n);
			short nodeType = child.getNodeType();

			if (nodeType == Node.ELEMENT_NODE)
				clean(child);
			else if (nodeType == Node.TEXT_NODE) {
				String trimmedNodeVal = child.getNodeValue().trim();
				if (trimmedNodeVal.length() == 0)
					node.removeChild(child);
				else
					child.setNodeValue(trimmedNodeVal);
			} else if (nodeType == Node.COMMENT_NODE)
				node.removeChild(child);
		}
		return node;
	}

	/*
	 * Generate a traceability template document TODO - Refactor - read from a
	 * template file.
	 */
	private String[] createTraceabilityDocTemplate() {

		final String tDoc[] = new String[3];

		tDoc[0] = ("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
		tDoc[1] = ("<cprov:traceabilityDocument xmlns:prov=\"http://www.w3.org/ns/prov#\" \n"
				+ " xmlns:cprov=\"http://labs.orange.com/uk/cprov#\" xmlns:cu=\"http://www.w3.org/1999/xhtml/datatypes/\" \n"
				+ " xmlns:r=\"http://labs.orange.com/uk/r#\" xmlns:ex=\"http://www.example.org/\" \n"
				+ " xmlns:opmx=\"http://openprovenance.org/model/opmx#\" xmlns:cprovd=\"http://labs.orange.com/uk/cprovd#\" \n"

				+ "xmlns:confidenshare=\"http://labs.orange.com/uk/confidenshare#\" \n"

				+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n"
				+ " xsi:schemaLocation=\"http://labs.orange.com/uk/cprov# file:/Users/mufy/Dropbox/EngD/year3/schema/cProv-inherited/cProv-v1.3.xsd\"> \n");

		tDoc[2] = ("</cprov:traceabilityDocument> \n");

		return tDoc;
	}

}
