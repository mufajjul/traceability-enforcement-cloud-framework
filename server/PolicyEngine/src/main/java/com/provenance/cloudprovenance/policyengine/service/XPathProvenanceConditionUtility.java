/*
 * @(#) XPathProvenanceConditionUtility.java       1.1 14/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.policyengine.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wso2.balana.finder.impl.FileBasedPolicyFinderModule;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;

import com.provenance.cloudprovenance.storagecontroller.persistence.exception.ProvenanceStoreNodeException;
import com.provenance.cloudprovenance.storagecontroller.persistence.xpath.XPathUtility;

/**
 * This class is the main class that handles the core functionalities of the Xpath functions
 * 
 * @version 1.1 14 Aug 2016
 * @author Mufy
 * @Module PolicyEngine
 */
public class XPathProvenanceConditionUtility {

	private String serviceId;
	private XPathUtility xpUtility;
	private String TRACEABILITY_ROOT_NODENAME = "cprov:traceabilityDocument";

	static Logger logger = Logger
			.getLogger(XPathProvenanceConditionUtility.class);

	public XPathProvenanceConditionUtility(String serviceId) {
		this.serviceId = serviceId;
	}

	public XPathProvenanceConditionUtility() {
	}

	public boolean evaluateIdWithStore(String xPathExpression)
			throws XMLDBException, URISyntaxException,
			ParserConfigurationException, SAXException, IOException {

		logger.debug("Evaluating store with the XPath (Id): " + xPathExpression);

		return xpUtility.matchNodeIdByxQuery(xPathExpression);
	}

	// possibly with a node
	public boolean evaluateStatementNodeWithStore(String id, Node transitiveNode)
			throws XMLDBException, URISyntaxException,
			ParserConfigurationException, SAXException, IOException {

		String TRANSITIVE_NODE = "cprov:wasRepresentationOf";
		String dynamicVarIdRef = null;

		// TODO - check for the null exception,
		// Get Node from the policy

		Node cprovlPolicyNode = getProvNode(constructCprovlXpathExpression(id),
				xpUtility);

		// change the generated Entity with the previous Used entity
		logger.debug("Checking for transitive node dependencies !!! "
				+ transitiveNode);

		if (transitiveNode != null) {

			NodeList tempChildNodeList = cprovlPolicyNode.getChildNodes();

			for (int k = 0; k < tempChildNodeList.getLength(); k++) {

				String provAttributeName = "prov:ref";

				Node currentNode = tempChildNodeList.item(k);
				if (currentNode.getNodeName().equals("prov:generatedEntity")) {

					// get transitiveNode att Value
					Node transitiveNodeAtt = transitiveNode.getAttributes()
							.getNamedItem(provAttributeName);

					logger.info("Transtive Node Attribute Values :"
							+ transitiveNodeAtt.getNodeValue());

					Node currentNodeAttribute = currentNode.getAttributes()
							.getNamedItem(provAttributeName);

					logger.info("current Node attribute value "
							+ currentNodeAttribute.getNodeValue());

					currentNodeAttribute.setNodeValue(transitiveNodeAtt
							.getNodeValue());
				}
			}
		}

		String currentNodeName = cprovlPolicyNode.getNodeName();

		logger.debug("Policy Node Matched (Id) "
				+ cprovlPolicyNode.getNodeName());

		try {

			logger.debug("Check for variables ....");

			// check dynamic variable for the node identification & child
			// elements
			NodeList matchedDvariables = matchedDVariablesChildNodes(
					cprovlPolicyNode, xpUtility);

			if (matchedDvariables != null && matchedDvariables.getLength() > 0) {
				// go through each dVariable and try to obtain its value from
				// the store

				logger.debug("match dydnamic variable length is: "
						+ matchedDvariables.getLength());

				for (int i = 0; i < matchedDvariables.getLength(); i++) {
					// get the first dVariable
					Node currentDvar = matchedDvariables.item(i);

					// Construct XPath to run it against the provenance store
					String xpath = constructXpathVar(matchedDvariables,
							cprovlPolicyNode, currentDvar);

					logger.debug("Xpath construction ==> " + xpath);

					// Run and return the matched nodes
					NodeList matchedNodeList = xpUtility
							.getMatchNodeListWithXquery(xpath);

					// Check if the Id already exists
					if (matchedNodeList != null
							&& matchedNodeList.getLength() != 0) {

						logger.debug("Retrived matched dynamic Variable Type ==> "
								+ matchedNodeList.item(0).getNodeName()
								+ " "
								+ matchedNodeList.getLength());

						if (matchedNodeList.item(0).getNodeName()
								.equals(TRACEABILITY_ROOT_NODENAME)) {
							Node currentNode = this.clean(matchedNodeList
									.item(0));
							// remove the root node called document, only
							// interested in the child elements
							matchedNodeList = currentNode.getChildNodes();

							logger.debug("removing the root node !!");
						}

						// check if the dynamic value is a child element
						if (currentDvar.getAttributes()
								.getNamedItem("prov:ref") != null) {

							dynamicVarIdRef = currentDvar.getAttributes()
									.getNamedItem("prov:ref").getNodeValue();

							// check if it is a root element
						} else if (currentDvar.getAttributes().getNamedItem(
								"prov:id") != null) {

							dynamicVarIdRef = currentDvar.getAttributes()
									.getNamedItem("prov:id").getNodeValue();

							Node myNode = currentDvar.getAttributes()
									.getNamedItem("prov:id");

							for (int j = 0; j < matchedNodeList.getLength(); j++) {
								logger.debug("Before removal: Element nodes "
										+ matchedNodeList.item(j)
												.getTextContent());

								NodeList childNodes = matchedNodeList.item(j)
										.getChildNodes();
								while (childNodes.getLength() != 0) {
									Node nodeToBeRemoved = childNodes
											.item(childNodes.getLength() - 1);

									logger.info("removing the Node: " + j
											+ " : child element total: "
											+ (childNodes.getLength() - 1)
											+ "  node val: "
											+ nodeToBeRemoved.getNodeValue());

									matchedNodeList.item(j).removeChild(
											nodeToBeRemoved);
								}

								logger.debug("After removal: Element nodes: "
										+ matchedNodeList.item(j)
												.getTextContent());
							}

							logger.debug("Value storing: " + dynamicVarIdRef);

						} else {
							throw new RuntimeException(
									"Could not get the node dynamic node value !!!");
						}

						if (DynamicVariableHolder
								.getDynamicVariableHolderInstance().varExist(
										dynamicVarIdRef)) {

							// if it does then store it
							DynamicVariableHolder
									.getDynamicVariableHolderInstance()
									.addDynamicVar(dynamicVarIdRef,
											(NodeList) matchedNodeList);
							logger.debug("Dynamic variable stored successfully ==> "
									+ dynamicVarIdRef);
						} else {
							logger.error("Could not store, varibale key does not exist !!! ==> "
									+ dynamicVarIdRef);
						}

						if (currentNodeName.equals(TRANSITIVE_NODE)) {

							logger.info("Transitive dependency exist!!! ");
							evaluateStatementNodeWithStore(id,
									matchedNodeList.item(0));
						}

					} else {
						logger.info("No match found against store using the dynamic var ...");

						if (currentNodeName.equals(TRANSITIVE_NODE)
								&& transitiveNode != null) {
							logger.info("Trasitive dependency, reached the root node");

							return true;
						}

						return false;
					}
				}
				return true;
			} else {
				logger.info("Does not contain any dynamic variables !");
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		logger.info("No variables found ....");

		return evaluateNodeWithStoreWithoutDynamicVariable(cprovlPolicyNode, id);

	}

	public List storeDynamicValueForRootVariable(NodeList matchedNodes) {

		List<String> attributeMatchedValuesList = new ArrayList<String>();

		for (int i = 0; i < matchedNodes.getLength(); i++) {
			Node selectedNode = (Node) matchedNodes.item(i);
		}
		return null;

	}

	private boolean evaluateNodeWithStoreWithoutDynamicVariable(
			Node cprovlPolicyNode, String id) throws XMLDBException,
			URISyntaxException, ParserConfigurationException, SAXException,
			IOException {

		Node matchedNode = xpUtility
				.getMatchNodewithXQuery(constructXpathExpression(id));

		if (matchedNode == null) {
			logger.warn("No match found !!!");
			return false;
		} else if (matchedNode == null || cprovlPolicyNode == null) {
			return false;
		} else if (matchedNode.getNodeName().equalsIgnoreCase(
				TRACEABILITY_ROOT_NODENAME)) {

			NodeList allMatchedChildNodes = (clean(matchedNode))
					.getChildNodes();

			for (int i = 0; i < allMatchedChildNodes.getLength(); i++) {
				try {

					if (xpUtility.compareNodes(
							clean(allMatchedChildNodes.item(i)),
							clean(cprovlPolicyNode))) {
						return true;
					}
				} catch (ProvenanceStoreNodeException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				return xpUtility.compareNodes(clean(matchedNode),
						clean(cprovlPolicyNode));
			} catch (ProvenanceStoreNodeException e) {
				e.printStackTrace();
			}
		}

		// return false if all the above condition fails
		return false;
	}

	public String constructXpathVar(NodeList dynamicVariableList,
			Node policyElement, Node currentDVar) {

		// clean the element
		policyElement = clean(policyElement);

		String xpathConstruction = new String();
		String policyElementNodeName = policyElement.getNodeName();

		// get node element name and value
		xpathConstruction = "//" + policyElementNodeName;

		logger.debug("Getting the attributes of the root element");
		// Get attributes for Root element (not needed yet)

		if (policyElement.hasAttributes()) {
			NamedNodeMap variableMap = policyElement.getAttributes();

			String tempXpathConstruction = new String();
			boolean dVariableExist = false;

			tempXpathConstruction += "[";
			for (int i = 0; i < variableMap.getLength(); i++) {
				Node tempVariable = variableMap.item(i);

				if (tempVariable.getNodeName().equals("prov:id")) {

					if (tempVariable.getNodeValue().contains("r:")) {
						// get the content of the variable from the store

						// Get variable node list
						Object varibleList = DynamicVariableHolder.dynamicHolder
								.getDynamicVar(tempVariable.getNodeValue());

						// TODO - Put check for NODELIST
						if (varibleList instanceof ArrayList) {

							@SuppressWarnings("unchecked")
							ArrayList<String> dynamicVaribleList = (ArrayList<String>) varibleList;

							for (String dvalues : dynamicVaribleList) {
								tempXpathConstruction += "@"
										+ tempVariable.getNodeName() + "='"
										+ dvalues + "'";

							}
							dVariableExist = true;

						}

					}
				}

				if (i != variableMap.getLength() - 1) {
					tempXpathConstruction += " and ";
				}
			}
			tempXpathConstruction += "]";

			// We need a flag to check if this should be valid.
			if (dVariableExist == true) {
				xpathConstruction += tempXpathConstruction;
			}
		}

		// Get attribute of the root element, if it is a variable
		NodeList childrenList = policyElement.getChildNodes();

		if (childrenList != null) {
			if (childrenList.getLength() != 0) {
				xpathConstruction = parseAllNodes(childrenList,
						xpathConstruction, dynamicVariableList);
				// get children (including elements and attributes
			}
		}

		// this indicates it a child with dynamic variable ref and not the root
		// element
		if (!(currentDVar.getNodeName().equals(policyElementNodeName))) {
			xpathConstruction += "/" + (currentDVar.getNodeName());
		}
		// TODO - Also need to check if the all the dynamic variable exists,
		// then return spcific child element not needed!!!

		return xpathConstruction;
	}

	// Main function responsible for constructing the XPath
	@SuppressWarnings("unchecked")
	public String parseAllNodes(NodeList children, String xpathConstruction,
			NodeList dynamicVariableList) {

		for (int i = 0; i < children.getLength(); i++) {
			Node childNode = children.item(i);

			String childElementNodeName = childNode.getNodeName();
			logger.debug("Child's node name: " + childElementNodeName);

			Node dynamicVariable = isDynamicValue(dynamicVariableList,
					childElementNodeName);

			logger.debug(" dynamic variable status:  " + dynamicVariable);

			if (dynamicVariable != null) {
				logger.debug("Child node is a dynamic variable:"
						+ childElementNodeName);
				Node variableValue = dynamicVariable.getAttributes()
						.getNamedItem("prov:ref");

				logger.debug("variable name is: "
						+ variableValue.getNodeValue());

				// Get variable node list
				Object varibleList = DynamicVariableHolder.dynamicHolder
						.getDynamicVar(variableValue.getNodeValue());

				if (varibleList == null) {
					logger.warn("Node variable values does not exist !!!");
					// Node already exist
					continue;
				} else {
					// Get the content and added as ORs
					logger.debug("Extracting node variable elements");

					// Check if its a node list or a arrayList;
					if (varibleList instanceof NodeList) {
						NodeList dynamicVaribleList = (NodeList) varibleList;
						xpathConstruction += "[";

						for (int k = 0; k < dynamicVaribleList.getLength(); k++) {
							Node variableElement = dynamicVaribleList.item(k);

							// Not using the node name of the variable, but the
							// original value
							xpathConstruction += childElementNodeName;

							if (variableElement.getTextContent() != null
									&& (!variableElement.getTextContent()
											.equals(""))) {
								xpathConstruction += " = '"
										+ variableElement.getTextContent()
										+ "'";
							}

							if (variableElement.hasAttributes()) {
								NamedNodeMap variableMap = variableElement
										.getAttributes();
								xpathConstruction += "[";

								for (int l = 0; l < variableMap.getLength(); l++) {
									Node tempVariable = variableMap.item(l);

									// TODO find a better workaround, remove
									// namespace
									logger.debug("variable node name: "
											+ tempVariable.getNodeName()
											+ " ; node value: "
											+ tempVariable.getNodeValue());
									if ((tempVariable.getNodeName()
											.contains("xmlns"))) {
										logger.debug("Contains namespace, skipping: "
												+ tempVariable.getNodeName());

										if (l == variableMap.getLength() - 1) {
											// remove the add construction if
											// the namespace is the last element
											xpathConstruction = xpathConstruction
													.replace("and", "");
										}
										continue;
									}

									xpathConstruction += "@";

									// dirty work around, find a better solution
									// to replace id with ref
									if (tempVariable.getNodeName().equals(
											"prov:id")) {
										xpathConstruction += "prov:ref" + "='"
												+ tempVariable.getNodeValue()
												+ "'";

									} else {

										xpathConstruction += tempVariable
												.getNodeName()
												+ "='"
												+ tempVariable.getNodeValue()
												+ "'";
									}
									if (l != variableMap.getLength() - 1) {
										xpathConstruction += " and ";
									}
								}
								xpathConstruction += "]";
							}

							if (k != dynamicVaribleList.getLength() - 1) {
								xpathConstruction += " or ";
							}
						}
						xpathConstruction += "]";
						continue; // skip the existing one (variable ref), this
									// replaced with actual
					} else {
						ArrayList<String> dynamicVaribleList = (ArrayList<String>) varibleList;
						xpathConstruction += "[";

						for (int k = 0; k < dynamicVaribleList.size(); k++) {
							xpathConstruction += childElementNodeName;
							xpathConstruction += "[";
							xpathConstruction += "@" + "prov:ref" + "='"
									+ dynamicVaribleList.get(k) + "'";
							xpathConstruction += "]";

							if (k != dynamicVaribleList.size() - 1) {
								xpathConstruction += " or ";
							}
						}
						xpathConstruction += "]";
						continue; // skip the existing one (variable ref), this
									// replaced with actual
					}
				}
			}
			// get node element name and value
			xpathConstruction += "[" + childElementNodeName;

			if (childNode.getTextContent() != null
					&& (!childNode.getTextContent().equals(""))) {
				xpathConstruction += " = '" + childNode.getTextContent() + "'";
			}

			if (childNode.hasAttributes()) {
				NamedNodeMap variableMap = childNode.getAttributes();
				xpathConstruction += "[";
				for (int j = 0; j < variableMap.getLength(); j++) {
					Node tempVariable = variableMap.item(j);
					xpathConstruction += "@" + tempVariable.getNodeName()
							+ "='" + tempVariable.getNodeValue() + "'";
					if (j != variableMap.getLength() - 1) {
						xpathConstruction += " and ";
					}
				}
				xpathConstruction += "]";
			}
			xpathConstruction += "]";
			if (childNode.getChildNodes() != null) {
				parseAllNodes(childNode.getChildNodes(), xpathConstruction,
						dynamicVariableList);
			}
		}
		return xpathConstruction;
	}

	private Node getProvNode(String xpathMatch, XPathUtility xpUtility)
			throws ParserConfigurationException, SAXException, IOException {

		logger.debug("Get provenance Node ");
		ArrayList<String> policyLocation = FileBasedPolicyFinderModule
				.getRetainedPolicyLocations();

		for (String location : policyLocation) {
			logger.debug("Policy location ==> " + location);

			location = (location.replace("xacml", "cprovl")).replace("/actual",
					"");

			logger.info("current cProvl policy file location:  " + location);

			Node matchedNode = xpUtility.fileNodeMatch(new File(location),
					xpathMatch);

			if (matchedNode != null)
				return matchedNode;
		}

		return null;
	}

	public String constructXpathExpression(String id) {

		String exp_plus = "/cprov:traceabilityDocument//*[@prov:id='" + id
				+ "'] | " + "/*[@prov:id='" + id + "'] ";

		logger.info("Store xpath == > " + exp_plus);
		return exp_plus;
	}

	public String constructCprovlXpathExpression(String id) {
		String exp = "/cprovl:POLICY//*[@prov:id='" + id + "']";

		logger.info("cprovl policy xpath == > " + exp);
		return exp;
	}

	// Get rid of empty text node
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

	private Node isDynamicValue(NodeList dValues, String childNodeValue) {

		for (int i = 0; i < dValues.getLength(); i++) {

			String tempNodeName = dValues.item(i).getNodeName();
			logger.debug("Looking for a match ==> Dynamic Node name "
					+ tempNodeName + " child Node ==> " + childNodeValue);

			if (childNodeValue.contains(tempNodeName)) {
				return dValues.item(i);
			}
		}

		return null;
	}

	public NodeList matchedDVariablesChildNodes(Node provNode,
			XPathUtility xpUtility) throws XPathExpressionException,
			SAXException, IOException, ParserConfigurationException {

		String xpathMatch = "child::node()[@prov:ref[contains(.,'r:')]] | self::node()[@prov:id[contains(.,'r:')]]";

		NodeList matchedNode = null;

		// Used to get the namespace
		logger.info("Get dynamic variable ref ");
		ArrayList<String> policyLocation = FileBasedPolicyFinderModule
				.getRetainedPolicyLocations();

		String location = (policyLocation.get(0).replace("xacml", "cprovl"))
				.replace("/actual", "");

		logger.debug("policy location ==> " + location);

		matchedNode = xpUtility.fileDynamicVariableNodeMatch(
				new File(location), xpathMatch, provNode);

		for (int i = 0; i < matchedNode.getLength(); i++) {
			logger.debug("Variable node name ==> "
					+ matchedNode.item(i).getNodeName());
		}
		return matchedNode;
	}

	public String getDynamicRefVariableId(String variableContent, String varType)
			throws ParserConfigurationException, SAXException, IOException {
		/**
		 * Get the dynamic variable identifier based on the content and variable
		 * type
		 */

		String xPathExp = "/cprovl:POLICY/target/ID[@provl:var_type='"
				+ varType + "']= '" + variableContent
				+ "'/@cprovl:var_identifier";

		// TODO - Check for errors ...
		Node idNode = getProvNode(xPathExp, this.getXpUtility());

		if (idNode != null) {
			return idNode.getTextContent();
		} else
			return null;
	}

	// Method not in use
	public boolean checkForDynamicVar(String content) {

		// Check for dynamic variable val
		logger.info("Matched Node String representation ==> " + content);

		for (String key : DynamicVariableHolder
				.getDynamicVariableHolderInstance().getKeys()) {

			logger.info("Variable key found ==> " + key);
			if (content.contains(key)) {
				return true;
			}
		}
		return false;
	}

	public boolean compareNodesForEqualityForConditionalStatements(
			String sourceNode, String destinationNode) throws XMLDBException,
			URISyntaxException, ParserConfigurationException, SAXException,
			IOException {

		logger.info("Comparing both nodes ");

		boolean isDynamicVar1 = checkForDynamicVar(sourceNode);
		// Check if the destination is a dynamic variable
		boolean isDynamicVar2 = checkForDynamicVar(destinationNode);

		if (isDynamicVar1) {
			NodeList varibleListSourceNodes = (NodeList) DynamicVariableHolder.dynamicHolder
					.getDynamicVar(sourceNode);
			Node firstSourceNode = (Node) varibleListSourceNodes.item(0);

			NamedNodeMap sourceAtt = firstSourceNode.getAttributes();

			Node srcIdetifier = sourceAtt.getNamedItem("prov:id");
			if (srcIdetifier == null) {
				srcIdetifier = sourceAtt.getNamedItem("prov:ref");
			}

			// replace with the actual node identifier
			sourceNode = srcIdetifier.getNodeValue();

		}

		Node srcMatchedNode = xpUtility
				.getMatchNodewithXQuery(constructXpathExpression(sourceNode));

		if (isDynamicVar2) {

			NodeList varibleListDestinationNodes = (NodeList) DynamicVariableHolder.dynamicHolder
					.getDynamicVar(destinationNode);

			Node firstDestinationNode = (Node) varibleListDestinationNodes
					.item(0);

			NamedNodeMap destAtt = firstDestinationNode.getAttributes();
			Node destIdetifier = destAtt.getNamedItem("prov:id");
			if (destIdetifier == null) {
				destIdetifier = destAtt.getNamedItem("prov:ref");
			}

			destinationNode = destIdetifier.getNodeValue();

		}

		Node desMatchedNode = xpUtility
				.getMatchNodewithXQuery(constructXpathExpression(destinationNode));

		if (srcMatchedNode == null) {
			logger.warn("No match found !!!");
			return false;
		} else if (srcMatchedNode == null || desMatchedNode == null) {
			return false;
		} else if (desMatchedNode.getNodeName().equalsIgnoreCase(
				TRACEABILITY_ROOT_NODENAME)) {
			logger.info("Node is TRACEABILTIY root node");

			NodeList allMatchedChildSourceNodes = (clean(srcMatchedNode))
					.getChildNodes();

			NodeList allMatchedChildDestinationNodes = (clean(desMatchedNode))
					.getChildNodes();

			for (int i = 0; i < allMatchedChildSourceNodes.getLength(); i++) {
				try {

					if (xpUtility.compareNodes(
							clean(allMatchedChildSourceNodes.item(i)),
							clean(allMatchedChildDestinationNodes.item(i)))) {
						return true;
					}
				} catch (ProvenanceStoreNodeException e) {
					e.printStackTrace();
				}
			}
		}
		logger.warn("Match not found");
		return false;

	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public XPathUtility getXpUtility() {
		return xpUtility;
	}

	public void setXpUtility(XPathUtility xpUtility) {
		this.xpUtility = xpUtility;
	}
}