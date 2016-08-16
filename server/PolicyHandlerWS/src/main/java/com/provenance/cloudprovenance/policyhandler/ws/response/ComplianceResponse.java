/*
 * @(#) ComplianceResponse.java       1.1 16/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.policyhandler.ws.response;

/**
 * This interface defines operations for the Compliance Response
 * 
 * @version 1.1 16 Aug 2016
 * @author Mufy
 * @Module PolicyHandlerWS
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
