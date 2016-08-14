/*
 * @(#) XPathProvenanceFunction.java       1.1 13/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.policyengine.cond;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Node;
import org.wso2.balana.attr.AttributeValue;
import org.wso2.balana.attr.BooleanAttribute;
import org.wso2.balana.attr.xacml3.XPathAttribute;
import org.wso2.balana.cond.Evaluatable;
import org.wso2.balana.cond.EvaluationResult;
import org.wso2.balana.cond.FunctionBase;
import org.wso2.balana.ctx.AbstractRequestCtx;
import org.wso2.balana.ctx.Attribute;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.xacml3.Attributes;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;

import com.provenance.cloudprovenance.policyengine.service.DynamicVariableHolder;
import com.provenance.cloudprovenance.policyengine.service.XPathProvenanceConditionUtility;

/**
 * This class handles new target functions to handle provenance data.
 * 
 * @version 1.1 13 Aug 2016
 * @author Mufy
 * @Module PolicyEngine
 */
public class XPathProvenanceFunction extends FunctionBase {

	private static HashMap<String, ArrayList<String>> multipleEntriesInputMap = new HashMap<String, ArrayList<String>>();

	public static final String NAME_XPATH_PROVENANCE_S_ID_MATCH = FUNCTION_NS_3_ORANGE
			+ "ext:xpath-provenance-id-match";

	public static final String NAME_XPATH_PROVENANCE_D_ID_MATCH = FUNCTION_NS_3_ORANGE
			+ "ext:xpath-provenance-d-id-match";

	public static final int ID_XPATH__PROVENANCE_S_ID_MATCH = 0;
	public static final int ID_XPATH__PROVENANCE_D_ID_MATCH = 1;
	public static final int NO_OF_ARGS = 2;

	private String XPATH_PROVENANCE_DYNAMIC_VARIABLE_CATEGORY = "urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:cprov:newvar";
	private String XPATH_PROVENANCE_S_DYNAMIC_VARIABLE_CATEGORY = "urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-id:cprov:s-ref";
	private String XPATH_PROVENANCE_D_DYNAMIC_VARIABLE_CATEGORY = "urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-id:cprov:d-ref";

	static Logger logger = Logger.getLogger(XPathProvenanceFunction.class);

	@Autowired
	private XPathProvenanceConditionUtility xpathUtility;

	public static Set<String> getSupportedIdentifiers() {

		Set<String> set = new HashSet<String>();
		set.add(NAME_XPATH_PROVENANCE_S_ID_MATCH);
		set.add(NAME_XPATH_PROVENANCE_D_ID_MATCH);
		return set;
	}

	private static int getId(String functionName) {

		if (functionName.equals(NAME_XPATH_PROVENANCE_S_ID_MATCH)) {
			return ID_XPATH__PROVENANCE_S_ID_MATCH;
		} else if (functionName.equals(NAME_XPATH_PROVENANCE_D_ID_MATCH)) {
			return ID_XPATH__PROVENANCE_D_ID_MATCH;
		} else {
			throw new IllegalArgumentException("unknown divide function "
					+ functionName);
		}
	}

	public void loadSpringBean() {

		ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "beans.xml" });
		xpathUtility = (XPathProvenanceConditionUtility) ctx
				.getBean("xPathProvenanceConditionUtility");
		ctx.close();
	}

	public XPathProvenanceFunction(String functionName) {

		super(functionName, getId(functionName), XPathAttribute.identifier,
				false, NO_OF_ARGS, BooleanAttribute.identifier, false);
	}

	public EvaluationResult evaluate(List<Evaluatable> inputs,
			EvaluationCtx context) {

		if (xpathUtility == null) {
			loadSpringBean();
		}

		logger.debug(" Loaded bean " + this.getXpathUtility());

		// Evaluate the arguments
		AttributeValue[] argValues = new AttributeValue[inputs.size()];

		logger.debug("Evaluatable list size: " + inputs.size() + "  ");

		// TODO - Explore how this statement is executed ....
		EvaluationResult result = evalArgs(inputs, context, argValues);

		XPathAttribute xpathAttribute = ((XPathAttribute) argValues[0]);
		String xpathValue = xpathAttribute.getValue();
		String category = xpathAttribute.getXPathCategory();
		String attributeId = xpathAttribute.getAttributeId();

		switch (getFunctionId()) {
		case ID_XPATH__PROVENANCE_S_ID_MATCH: {
			boolean evalutionResult = false;
			logger.info("xpath Value: " + xpathValue + ", category: "
					+ category);

			try {
				evalutionResult = xpathUtility.evaluateIdWithStore(xpathUtility
						.constructXpathExpression(xpathValue));
			} catch (XMLDBException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			logger.info("The result is: " + evalutionResult);

			return EvaluationResult.getInstance(evalutionResult);
		}

		// Check for dynamic variable
		case ID_XPATH__PROVENANCE_D_ID_MATCH: {

			logger.debug("Attribute type ==> " + argValues[0]
					+ " XPATH-D-ID-MATCH");

			// Handle new variable type
			if (category.equals(XPATH_PROVENANCE_DYNAMIC_VARIABLE_CATEGORY)) {

				logger.info("Dynamic variable detected, adding it to the store ");

				// Add the variable reference to the Variable holder
				DynamicVariableHolder.getDynamicVariableHolderInstance()
						.addDynamicVar(xpathValue, null);

				return EvaluationResult.getInstance(true);
			}

			// Handle Ref variable
			AbstractRequestCtx ctx = context.getRequestCtx();
			Set<Attributes> attCtxList = ctx.getAttributesSet();

			logger.info("attCtxList size: " + attCtxList.size());

			List<String> attributeMatchedValuesList = new ArrayList<String>();

			for (Attributes att : attCtxList) {
				logger.debug("Context Category: " + att.getCategory()
						+ "; expected category: " + category);

				if (att.getCategory().toString().equals(category)) {
					Node content = att.getContent();

					if (content != null)
						logger.debug("Context content: "
								+ att.getContent().toString());

					if (att.getAttributes().size() > 0) {
						Set<Attribute> attc = att.getAttributes();

						for (Attribute tmpSubAtt : attc) {
							logger.debug("Request Att type: "
									+ tmpSubAtt.getType());
							logger.debug("Request Att ID: " + tmpSubAtt.getId());

							AttributeValue attVal = tmpSubAtt.getValue();
							logger.debug("Request Attribute content: "
									+ attVal.encode());

							// check if the value is single or multiple
							if (attVal.encode().contains(";")) {

								if (multipleEntriesInputMap
										.containsKey(category)) {

									// check if the content has been removed, if
									// so remove the key
									if (multipleEntriesInputMap.get(category)
											.size() == 0) {

										logger.debug("Old key does not contain value, spliting contents again");

										String tokenizeInput[] = attVal
												.encode().split(";");
										ArrayList<String> tokenizedArrayList = new ArrayList<String>(
												new ArrayList<String>(Arrays
														.asList(tokenizeInput)));

										// add the first value to the list
										attributeMatchedValuesList
												.add(tokenizedArrayList.get(0));

										tokenizedArrayList.remove(0);
										// add the remaining to the list

										multipleEntriesInputMap.put(category,
												tokenizedArrayList);
									}

									ArrayList<String> currentList = multipleEntriesInputMap
											.get(category);
									attributeMatchedValuesList.add(currentList
											.get(0));
									currentList.remove(0);

								} else {
									logger.debug("Contains multiple values, spliting contents");
									String tokenizeInput[] = attVal.encode()
											.split(";");
									ArrayList<String> tokenizedArrayList = new ArrayList<String>(
											new ArrayList<String>(Arrays
													.asList(tokenizeInput)));

									// add the first value to the list

									attributeMatchedValuesList
											.add(tokenizedArrayList.get(0));

									tokenizedArrayList.remove(0);
									// add the remaining to the list

									multipleEntriesInputMap.put(category,
											tokenizedArrayList);

								}

							} else {

								attributeMatchedValuesList.add(attVal.encode());
							}
						}
					}
					break;
				}
			}

			if (attributeMatchedValuesList.size() > 0) {
				logger.debug("D-var ref value extracted: "
						+ attributeMatchedValuesList.size());

				// Check it against the store .....
				for (int i = 0; i < attributeMatchedValuesList.size(); i++) {
					boolean evalutionResult = false;
					String variableValue = attributeMatchedValuesList.get(i);

					try {
						// check if it is not a s-ref variable, and only a d-ref
						if (attributeId
								.equals(XPATH_PROVENANCE_D_DYNAMIC_VARIABLE_CATEGORY)) {

							// evaluate the value against the store
							logger.info("D-variable detected matching against the store ");

							evalutionResult = xpathUtility
									.evaluateIdWithStore(xpathUtility
											.constructXpathExpression(variableValue));

							if (evalutionResult == false) {
								logger.warn("Match against the store failed !");

								return EvaluationResult.getInstance(false);
							} else {

								logger.debug("Adding to d-dynamic variable store: "
										+ xpathValue
										+ " ..."
										+ Arrays.toString(attributeMatchedValuesList
												.toArray()));
								DynamicVariableHolder
										.getDynamicVariableHolderInstance()
										.addDynamicVar(xpathValue,
												attributeMatchedValuesList);
								return EvaluationResult.getInstance(true);

							}

						} else if (attributeId
								.equals(XPATH_PROVENANCE_S_DYNAMIC_VARIABLE_CATEGORY)) {

							// evaluate the value against the store
							logger.info("S-variable detecte Adding to d-dynamic variable store: "
									+ xpathValue
									+ " ..."
									+ Arrays.toString(attributeMatchedValuesList
											.toArray()));

							DynamicVariableHolder
									.getDynamicVariableHolderInstance()
									.addDynamicVar(xpathValue,
											attributeMatchedValuesList);
							return EvaluationResult.getInstance(true);
						}

					} catch (XMLDBException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} else {
				logger.warn("No d-var ref variable found !!!");
				return EvaluationResult.getInstance(false);
			}
		}
		default:
			logger.warn("No match found!!!");
			return EvaluationResult.getInstance(false);
		}
	}

	/**
	 * Check for dynamic variables
	 */
	public boolean isdDynamicVariable(String value) {
		String prefix = value.split(":")[0];

		logger.info("Variable prefix and suffix " + value.split(":")[0] + "  "
				+ value.split(":")[1]);

		if (prefix.contains("r")) {
			return true;
		} else {
			return false;
		}
	}

	public XPathProvenanceConditionUtility getXpathUtility() {
		return xpathUtility;
	}

	public void setXpathUtility(XPathProvenanceConditionUtility xpathUtility) {
		this.xpathUtility = xpathUtility;
	}
}
