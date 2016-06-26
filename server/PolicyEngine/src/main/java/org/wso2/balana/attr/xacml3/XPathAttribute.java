/*
 *  Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.balana.attr.xacml3;

import java.net.URI;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.wso2.balana.attr.AttributeValue;

/**
 * Representation of Xpath attribute type
 */
public class XPathAttribute extends AttributeValue {

	/**
	 * Official name of this type
	 */
	
	private static Logger logger = Logger.getLogger(XPathAttribute.class);
	
	public static final String identifier = "urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression";

	/**
	 * URI version of name for this type
	 * <p>
	 * This field is initialized by a static initializer so that we can catch
	 * any exceptions thrown by URI(String) and transform them into a
	 * RuntimeException, since this should never happen but should be reported
	 * properly if it ever does.
	 */
	private static URI identifierURI;
	

	/**
	 * RuntimeException that wraps an Exception thrown during the creation of
	 * identifierURI, null if none.
	 */
	private static RuntimeException earlyException;

	/**
	 * Static initializer that initializes the identifierURI class field so that
	 * we can catch any exceptions thrown by URI(String) and transform them into
	 * a RuntimeException. Such exceptions should never happen but should be
	 * reported properly if they ever do.
	 */
	static {
		try {
			identifierURI = new URI(identifier);
		} catch (Exception e) {
			earlyException = new IllegalArgumentException();
			earlyException.initCause(e);
		}
	};

	/**
	 * The actual xpath expression as String value
	 */
	private String value;

	/**
	 * The namespace context is defined by this value
	 */
	private String xPathCategory;
	
	private String attributeId;


	/**
	 * Creates a new <code>XPathAttribute</code> that represents the String
	 * value supplied.
	 * 
	 * @param value
	 *            the <code>String</code> that represent xpath expression
	 * @param xPathCategory
	 *            the <code>String</code> that represent the namespace context
	 */
	public XPathAttribute(String value, String xPathCategory) {
		super(identifierURI);

		// Shouldn't happen, but just in case...
		if (earlyException != null) {
			throw earlyException;
		}

		if (value == null) {
			this.value = "";
		} else {
			this.value = value;
		}

		if (xPathCategory == null) {
			this.xPathCategory = "";
		} else {
			this.xPathCategory = xPathCategory;
		}
	}

	
	public XPathAttribute(String value, String xPathCategory, String attributeId) {
		super(identifierURI);

		// Shouldn't happen, but just in case...
		if (earlyException != null) {
			throw earlyException;
		}

		if (value == null) {
			this.value = "";
		} else {
			this.value = value;
		}

		if (xPathCategory == null) {
			this.xPathCategory = "";
		} else {
			this.xPathCategory = xPathCategory;
		}
		
		if (attributeId == null) {
			this.attributeId = "";
		} else {
			this.attributeId = attributeId;
		}
		
		
	}

	
	
	/**
	 * Returns a new <code>XPathAttribute</code> that represents a particular
	 * DOM node.
	 * 
	 * @param root
	 *            the <code>Node</code> that contains the desired value
	 * @return a new <code>XPathAttribute</code> representing the appropriate
	 *         value (null if there is a parsing error)
	 */
	public static XPathAttribute getInstance(Node root) {

		String xPathCategory = null;
		String attributeId = null;

		logger.debug("Creating xPath attribute ==> "
				+ root.getTextContent());

		NamedNodeMap nodeMap = root.getAttributes();

		logger.debug("Node map ==> " + nodeMap);

		if (nodeMap != null) {
			Node categoryNode = nodeMap.getNamedItem("XPathCategory");

			// if (categoryNode ==null ){
			// categoryNode = nodeMap.getNamedItem("Category");

			// }

			logger.debug("Node categoryNode ==> " + categoryNode);

			xPathCategory = categoryNode.getNodeValue();
			
			
			Node attributeIdNode = nodeMap.getNamedItem("AttributeId");			
			
			//If it is null then it is a attribute from the input request
			if (attributeIdNode == null){
				
				return getInstance(root.getFirstChild().getNodeValue(), xPathCategory);
				
			}
			
			
			attributeId = attributeIdNode.getNodeValue();

		//	logger.debug("Get attribute Id ==> " + attributeId.getNodeValue());

		
			logger.debug("xpathCategory  ==> " + xPathCategory
					+ " Root node first Child, node val"
					+ root.getFirstChild().getNodeValue());

		}

		return getInstance(root.getFirstChild().getNodeValue(), xPathCategory, attributeId);
	}

	/**
	 * Returns a new <code>XPathAttribute</code> that represents value indicated
	 * by the <code>String</code> provided.
	 * 
	 * @param value
	 *            a string representing the desired xpath expression value
	 * @param xPathCategory
	 *            a String represents the namespace context
	 * @return a new <code>XPathAttribute</code> representing the appropriate
	 *         value
	 */
	public static XPathAttribute getInstance(String value, String xPathCategory) {
		return new XPathAttribute(value, xPathCategory);
	}
	
	public static XPathAttribute getInstance(String value, String xPathCategory, String attributeId) {
		return new XPathAttribute(value, xPathCategory, attributeId);
	}
	

	/**
	 * Returns the <code>String</code> value that represents xpath expression
	 * 
	 * @return the <code>String</code> value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns the <code>String</code> value that represents the namespace
	 * context
	 * 
	 * @return the <code>String</code> xPathCategory
	 */
	public String getXPathCategory() {
		return xPathCategory;
	}

	/**
	 * Returns true if the input is an instance of this class and if its value
	 * equals the value contained in this class.
	 * 
	 * @param o
	 *            the object to compare
	 * 
	 * @return true if this object and the input represent the same value
	 */
	public boolean equals(Object o) {
		if (!(o instanceof XPathAttribute))
			return false;

		XPathAttribute other = (XPathAttribute) o;

		return value.equals(other.value)
				&& xPathCategory.equals(other.xPathCategory);
	}

	/**
	 * Returns the hashcode value used to index and compare this object with
	 * others of the same type. Typically this is the hashcode of the backing
	 * data object.
	 * 
	 * @return the object's hashcode value
	 */
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String encode() {
		return value;
	}

	@Override
	public String encodeWithTags(boolean includeType) {
		if (includeType && getType() != null) {
			return "<AttributeValue DataType=\"" + getType().toString()+"\" "
					+ "XPathCategory=\"" + getXPathCategory() + "\">"
					+ encode() + "</AttributeValue>";
		} else {
			return "<AttributeValue>" + encode() + "</AttributeValue>";
		}
	}


	public String getAttributeId() {
		return attributeId;
	}
}
