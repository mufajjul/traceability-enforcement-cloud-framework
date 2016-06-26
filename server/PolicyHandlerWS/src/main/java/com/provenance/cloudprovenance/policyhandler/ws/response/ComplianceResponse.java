/**
 * @file 		ComplianceResponse.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		PolicyHandlerWS
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.policyhandler.ws.response;


/**
 * This interface define operations for the Compliance Response
 * 
 * @author Mufy
 * 
 */
public interface ComplianceResponse<T> {

	
		public final String UNIQUE_IDENTIFIER_NS_SUFFIX = "http://labs.orange.com/uk/ex#";
		public final String UNIQUE_IDENTIFIER_NS_PREFIX = "ex";

		public final String UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX = "http://labs.orange.com/uk/cprovd#";
		public final String UNIQUE_IDENTIFIER_NS_PROVD_PREFIX = "provd";

		public final String UNIQUE_IDENTIFIER_NS_PROV_SUFFIX = "http://www.w3.org/ns/prov#";
		public final String UNIQUE_IDENTIFIER_NS_PROV_PREFIX = "prov";

		public T constructResponse(String policyId, String requestId, String responseId, String responseValue);

	
}
