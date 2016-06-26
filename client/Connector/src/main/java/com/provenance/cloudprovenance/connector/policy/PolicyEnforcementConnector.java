/**
 * @file 		PolicyEnforcementConnector.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		Conncetor
 * @date 		18 05 2013
 * @version 	1.0
 */

package com.provenance.cloudprovenance.connector.policy;

import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

/**
 * @author Mufy
 * 
 */
public class PolicyEnforcementConnector implements PolicyEnforcement {

	private static String server_add;
	private static int port_no;
	private String service;
	private String resource;
	private int DEFAULT_TIMEOUT;

	private String policyResponseId = null;

	private static Logger logger = Logger
			.getLogger("PolicyEnforcementConnector");

	@Override
	public String policyRequest(String serviceId, String policyrequestContent) {

		String restURI = "http://" + server_add + ":" + port_no + "/" + service
				+ "/" + resource + "/" + serviceId;// + "/" ;

		logger.info("Invoking URI: " + restURI);
		RestTemplate restTemplate = new RestTemplate();
		String policyIdResponse = restTemplate.postForObject(restURI,
				policyrequestContent, String.class);

		return policyIdResponse;
	}

	@Override
	public String policyResponse(String serviceId, String policyResponseId) {

		String restURI = "http://" + server_add + ":" + port_no + "/" + service
				+ "/" + resource + "/" + serviceId + "/" + policyResponseId;

		logger.info("Invoking URI: " + restURI);
		RestTemplate restTemplate = new RestTemplate();
		String policyResponse = restTemplate
				.getForObject(restURI, String.class);

		return policyResponse;
	}

	@Override
	public String policyResponse(String serviceId, URL policyResponseURI) {

		// String restURI = "http://" + server_add + ":" + port_no + "/" +
		// service
		// + "/" + resource + "/" + serviceId + "/" + policyResponseId;

		logger.info("Invoking URI: " + policyResponseURI);
		RestTemplate restTemplate = new RestTemplate();
		String policyResponse = restTemplate.getForObject(
				policyResponseURI.toExternalForm(), String.class);

		return policyResponse;
	}

	public static String getServer_add() {
		return server_add;
	}

	public static void setServer_add(String server_add) {
		PolicyEnforcementConnector.server_add = server_add;
	}

	public static int getPort_no() {
		return port_no;
	}

	public static void setPort_no(int port_no) {
		PolicyEnforcementConnector.port_no = port_no;
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

}
