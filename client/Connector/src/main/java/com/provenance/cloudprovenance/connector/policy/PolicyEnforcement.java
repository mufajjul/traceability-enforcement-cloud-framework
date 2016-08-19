/*
 * @(#) PolicyEnforcement.java       1.1 13/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.connector.policy;

import java.net.URL;

/**  
 * This interface defines operations for making policy request and response 
 *
 * @version      1.1 13 Aug 2016  
 * @author       Mufy 
 * @Module		 Connector 
 */
public interface PolicyEnforcement {

	// make policy request
	public String policyRequest(String serviceId, String policyrequestContent);

	// retrieve policy response, either using response id, of via a full URI
	public String policyResponse(String serviceId, String policyResponseId);

	public String policyResponse(String serviceId, URL policyResponseURI);
}
