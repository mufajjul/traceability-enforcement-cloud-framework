/**
 * @file 		XPathProvenanceConditionFunction.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		PolicyEngine
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.policyengine.cond;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.wso2.balana.attr.AttributeValue;
import org.wso2.balana.attr.BooleanAttribute;
import org.wso2.balana.attr.xacml3.XPathAttribute;
import org.wso2.balana.cond.Evaluatable;
import org.wso2.balana.cond.EvaluationResult;
import org.wso2.balana.cond.FunctionBase;
import org.wso2.balana.ctx.EvaluationCtx;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;

import com.provenance.cloudprovenance.policyengine.service.XPathProvenanceConditionUtility;

/**
 * This class provides handles the XPath target function
 * 
 * @author Mufy
 * 
 */
public class XPathProvenanceConditionFunction extends FunctionBase {

	// main function
	public static final String NAME_XPATH_PROVENANCE_NODE_MATCH = FUNCTION_NS_3_ORANGE
			+ "ext:xpath-provenance-node-match";

	public static final String NAME_XPATH_PROVENANCE_NODES_MATCH = FUNCTION_NS_3_ORANGE
			+ "ext:xpath-provenance-nodes-match";

	// To be implemented in the future
	public static final String NAME_XPATH_PROVENANCE_NODE_GREATER_THAN_MATCH = FUNCTION_NS_3_ORANGE
			+ "ext:xpath-provenance-node-greater-than-match";

	public static final String NAME_XPATH_PROVENANCE_NODE_GREATER_THAN_OR_EQUAL_MATCH = FUNCTION_NS_3_ORANGE
			+ "ext:xpath-provenance-node-greater-than-or-equal-match";

	public static final String NAME_XPATH_PROVENANCE_NODE_LESS_THAN_MATCH = FUNCTION_NS_3_ORANGE
			+ "ext:xpath-provenance-node-less-than-match";

	public static final String NAME_XPATH_PROVENANCE_NODE_LESS_THAN_OR_EQUAL_MATCH = FUNCTION_NS_3_ORANGE
			+ "ext:xpath-provenance-node-less-than-or-equal-match";

	public static final String NAME_XPATH_PROVENANCE_NODE_MATCH_NEGATE = FUNCTION_NS_3_ORANGE
			+ "ext:xpath-provenance-node-match-negate";

	static Logger logger = Logger
			.getLogger(XPathProvenanceConditionFunction.class);

	public static final int ID_XPATH_PROV_NODE_MATCH = 0;
	public static final int ID_XPATH_PROV_NODES_MATCH = 1;

	String currentFunctionName;

	@Autowired
	private XPathProvenanceConditionUtility xpathUtility;

	public static Set<String> getSupportedIdentifiers() {

		Set<String> set = new HashSet<String>();
		set.add(NAME_XPATH_PROVENANCE_NODE_MATCH);
		set.add(NAME_XPATH_PROVENANCE_NODES_MATCH);

		return set;
	}

	/**
	 * TODO - Incorporate the service ID to the function
	 * 
	 * @param functionName
	 * @param serviceId
	 */

	public XPathProvenanceConditionFunction(String functionName) {

		super(getNodeMatchName(functionName), getNodeMatchNameId(functionName),
				XPathAttribute.identifier, false,
				getNodeMatchNameIdArguments(functionName),
				BooleanAttribute.identifier, false);

		currentFunctionName = functionName;

	}

	public static String getNodeMatchName(String functionName) {

		logger.info("get function Name : " + functionName);

		if (functionName.equals(FUNCTION_NS_3_ORANGE
				+ "ext:xpath-provenance-node-match")) {
			logger.debug("Matched function Name: " + FUNCTION_NS_3_ORANGE
					+ "ext:xpath-provenance-node-match");
			return FUNCTION_NS_3_ORANGE + "ext:xpath-provenance-node-match";
		} else if (functionName.equals(FUNCTION_NS_3_ORANGE
				+ "ext:xpath-provenance-nodes-match")) {
			return FUNCTION_NS_3_ORANGE + "ext:xpath-provenance-nodes-match";
		} else
			return null;
	}

	public static int getNodeMatchNameId(String functionName) {

		logger.debug("get function Name Identifier for Id: " + functionName);

		if (functionName.equals(FUNCTION_NS_3_ORANGE
				+ "ext:xpath-provenance-node-match")) {
			return 0;
		} else if (functionName.equals(FUNCTION_NS_3_ORANGE
				+ "ext:xpath-provenance-nodes-match")) {
			return 1;
		} else
			return -1;
	}

	public static int getNodeMatchNameIdArguments(String functionName) {

		if (functionName.equals(FUNCTION_NS_3_ORANGE
				+ "ext:xpath-provenance-node-match")) {

			logger.debug("function Name Identifier for arguments : "
					+ functionName + " param :" + 1);

			return 1;
		} else if (functionName.equals(FUNCTION_NS_3_ORANGE
				+ "ext:xpath-provenance-nodes-match")) {

			logger.debug("function Name Identifier for arguments : "
					+ functionName + " param :" + 2);

			return 2;
		} else
			return -1;
	}

	public void loadSpringBean() {

		ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "beans.xml" });

		xpathUtility = (XPathProvenanceConditionUtility) ctx
				.getBean("xPathProvenanceConditionUtility");
	}

	public EvaluationResult evaluate(List<Evaluatable> inputs,
			EvaluationCtx context) {

		logger.debug("Evaluatable list size ==> " + inputs.size()
				+ "; functionId  " + getFunctionId());

		Evaluatable eve = inputs.get(0);

		logger.debug("Evaluatable type:  " + eve.getType());
		logger.debug("context att size in target: "
				+ context.getRequestCtx().getAttributesSet().size());

		if (xpathUtility == null) {
			loadSpringBean();
		}

		if (getFunctionId() == ID_XPATH_PROV_NODE_MATCH) {

			boolean evalutionResult = false;

			AttributeValue[] argValues = new AttributeValue[inputs.size()];
			logger.debug("Evaluatable list size ==> " + inputs.size() + "  ");
			EvaluationResult result = evalArgs(inputs, context, argValues);

			if (result != null) {
				logger.warn("Result expression is null");
				return result;
			}

			logger.debug("Attribute type: " + argValues[0]);

			XPathAttribute xpathAttribute = ((XPathAttribute) argValues[0]);
			String xpathValue = xpathAttribute.getValue();
			String category = xpathAttribute.getXPathCategory();

			logger.debug("xpath Value: " + xpathValue + ", category: "
					+ category);

			try {
				// null sent as treated as direct statement
				evalutionResult = xpathUtility.evaluateStatementNodeWithStore(
						xpathValue, null);
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

			logger.debug("Function outcome: " + evalutionResult);

			return EvaluationResult.getInstance(evalutionResult);
		} else if (getFunctionId() == ID_XPATH_PROV_NODES_MATCH) {

			logger.warn("Fuction:  " + currentFunctionName
					+ " : To be implemented");
			int parameterSize = inputs.size();

			if (parameterSize == 2) {

				AttributeValue[] argValues1 = new AttributeValue[inputs.size()];
				EvaluationResult result = evalArgs(inputs, context, argValues1);

				if (result != null) {
					logger.warn("Result expression is null");
					return result;
				}

				XPathAttribute xpathAttribute1 = ((XPathAttribute) argValues1[0]);
				String xpathValue1 = xpathAttribute1.getValue();
				String category1 = xpathAttribute1.getXPathCategory();

				XPathAttribute xpathAttribute2 = ((XPathAttribute) argValues1[1]);
				String xpathValue2 = xpathAttribute2.getValue();
				String category2 = xpathAttribute2.getXPathCategory();

				logger.info("attribute1 values: xpathValue: " + xpathValue1
						+ "; xpathCategory: " + category1);
				logger.info("attribute1 values: xpathValue: " + xpathValue2
						+ "; xpathCategory: " + category2);

				try {
					boolean evalutionResult = xpathUtility
							.compareNodesForEqualityForConditionalStatements(
									xpathValue1, xpathValue2);
					return EvaluationResult.getInstance(evalutionResult);

				} catch (XMLDBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return EvaluationResult.getInstance(true);
		} else {

			logger.error("Function not found !");
			return EvaluationResult.getInstance(false);
		}
	}

	public XPathProvenanceConditionUtility getXpathUtility() {
		return xpathUtility;
	}

	public void setXpathUtility(XPathProvenanceConditionUtility xpathUtility) {
		this.xpathUtility = xpathUtility;
	}
}
