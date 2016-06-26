/**
 * @file 		PolicyEnforcement.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		Connector
 * @date 		18 05 2013
 * @version 	1.0
 */

package com.provenance.cloudprovenance.connector.policy;

import java.net.URL;

/**
 * @author Mufy
 * 
 */
public interface PolicyEnforcement {

	// public String validatePolicyrequest (String ServiceId, String
	// policyRequestId, String policyId, HttpServletRequest request) throws
	// IOException, URISyntaxException, ParserConfigurationException,
	// SAXException, TransformerException;

	public String policyRequest(String serviceId, String policyrequestContent);

	public String policyResponse(String serviceId, String policyResponseId);

	public String policyResponse(String serviceId, URL policyResponseURI);

}
