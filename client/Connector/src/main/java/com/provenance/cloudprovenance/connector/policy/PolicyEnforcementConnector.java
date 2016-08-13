/*
 * @(#) PolicyEnforcementConnector.java       1.1 13/8/2016
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

import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

/**
 * This class implements the PolicyEnforcement interface methods
 *
 * @version 1.1 13 Aug 2016
 * @author Mufy
 * @Module Connector
 */
public class PolicyEnforcementConnector implements PolicyEnforcement {

	// Load the values from the Spring Bean

	private String server_add;
	private int port_no;
	private String protocol;
	private String service;
	private String resource;
	private int DEFAULT_TIMEOUT;

	private static Logger logger = Logger
			.getLogger("PolicyEnforcementConnector");

	/** Method returns the output of the policy request as a string */
	@Override
	public String policyRequest(String serviceId, String policyrequestContent) {

		RestTemplate restTemplate = new RestTemplate();
		String restURIstructure = new String();

		restURIstructure = protocol + "://" + server_add + ":" + port_no + "/"
				+ service + "/" + resource + "/" + serviceId;// + "/" ;

		logger.info("Invoking URI: " + restURIstructure);

		String policyIdResponse = restTemplate.postForObject(restURIstructure,
				policyrequestContent, String.class);

		return policyIdResponse;
	}

	/** Method retrieves the policy response based on a response Id as a string */
	@Override
	public String policyResponse(String serviceId, String policyResponseId) {

		RestTemplate restTemplate = new RestTemplate();
		String restURIstructure = new String();

		restURIstructure = protocol + "://" + server_add + ":" + port_no + "/"
				+ service + "/" + resource + "/" + serviceId + "/"
				+ policyResponseId;

		logger.info("Invoking URI: " + restURIstructure);

		String policyResponse = restTemplate.getForObject(restURIstructure,
				String.class);

		return policyResponse;
	}

	/** Method returns a policy response based on a repose URI as a string */
	@Override
	public String policyResponse(String serviceId, URL policyResponseURI) {

		RestTemplate restTemplate = new RestTemplate();

		logger.info("Invoking URI: " + policyResponseURI);

		String policyResponse = restTemplate.getForObject(
				policyResponseURI.toExternalForm(), String.class);

		return policyResponse;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public int getDEFAULT_TIMEOUT() {
		return DEFAULT_TIMEOUT;
	}

	public void setDEFAULT_TIMEOUT(int dEFAULT_TIMEOUT) {
		DEFAULT_TIMEOUT = dEFAULT_TIMEOUT;
	}

	public String getServer_add() {
		return server_add;
	}

	public void setServer_add(String server_add) {
		this.server_add = server_add;
	}

	public int getPort_no() {
		return port_no;
	}

	public void setPort_no(int port_no) {
		this.port_no = port_no;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
