/*
 * @(#) TraceabilityStoreConnector.java       1.1 13/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */

package com.provenance.cloudprovenance.connector.traceability;

import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.util.UriEncoder;

/**
 * @author Mufy
 * 
 */
public class TraceabilityStoreConnector implements TraceabilityStore {
	private String server_add;
	private int port_no;
	private String service;
	private String resource;
	private String TRACEABILITY_TYPE;
	private int DEFAULT_TIMEOUT;
	private String traceabilityRecordId = null;

	private static Logger logger = Logger.getLogger("TraceabilityStoreHandler");

	public synchronized String createNewTraceabilityRecord(String serviceId,
			String traceabilityRecord) {

		String restURI = "http://" + server_add + ":" + port_no + "/" + service
				+ "/" + resource + "/" + serviceId + "/"
				+ this.TRACEABILITY_TYPE;

		logger.info("Invoking URI: " + restURI);
		RestTemplate restTemplate = new RestTemplate();
		// String traceabilityRecordIdResponse =
		// restTemplate.getForObject(restURI, String.class);
		String traceabilityRecordIdResponse = restTemplate.postForObject(
				restURI, traceabilityRecord, String.class);

		return traceabilityRecordIdResponse;

	}

	public String getTraceabilityRecord(String serviceId,
			String traceabilityEntryId) {

		if (this.getTraceabilityRecordId() != null) {
			return this.getTraceabilityRecordId();
		}

		String restURI = "http://" + server_add + ":" + port_no + "/" + service
				+ "/" + resource + "/" + serviceId + "/"
				+ this.TRACEABILITY_TYPE + "/" + traceabilityEntryId;

		RestTemplate restTemplate = new RestTemplate();
		String traceabilityRecordResponse = restTemplate.getForObject(restURI,
				String.class);

		return traceabilityRecordResponse;
	}

	public String getCurrentTraceabilityRecordId(URL serviceId) {
		// URI encoded
			
				RestTemplate restTemplate = new RestTemplate();

				// restTemplate.get

				ResponseEntity<String> traceabilityResponseEntity = null;

				try {
					traceabilityResponseEntity = restTemplate.getForEntity(serviceId.toExternalForm(),
							String.class);

					if (traceabilityResponseEntity.getStatusCode().value() == 200) {
						return traceabilityResponseEntity.getBody();
					} else {
						return null;
					}

				} catch (org.springframework.web.client.HttpClientErrorException ex) {
					logger.warn(ex.toString());
					return null;
				}
	}
	
	public String getCurrentTraceabilityRecordId(String serviceId) {

		// URI encoded
		String restURI = UriEncoder.encode("http://" + server_add + ":"
				+ port_no + "/" + service + "/" + resource + "/" + serviceId
				+ "/" + this.TRACEABILITY_TYPE);

		RestTemplate restTemplate = new RestTemplate();

		// restTemplate.get

		ResponseEntity<String> traceabilityResponseEntity = null;

		try {
			traceabilityResponseEntity = restTemplate.getForEntity(restURI,
					String.class);

			if (traceabilityResponseEntity.getStatusCode().value() == 200) {
				return traceabilityResponseEntity.getBody();
			} else {
				return null;
			}

		} catch (org.springframework.web.client.HttpClientErrorException ex) {
			logger.warn(ex.toString());
			return null;
		}

		// ResponseEntity<String> traceabilityDocId =
		// restTemplate.exchange(restURI, HttpMethod.GET, null, String.class);
		// System.out.println(traceabilityDocId.getBody());
		// System.out.println(traceabilityDocId.getStatusCode());

		// return traceabilityDocId.getBody();

	}

	/*
	public synchronized int updateTraceabilityRecord(String serviceId,
			String traceabilityExistingEntryId, String traceabilityNewEntryData) {

		String restURI = "http://" + server_add + ":" + port_no + "/" + service
				+ "/" + resource + "/" + serviceId + "/"
				+ this.TRACEABILITY_TYPE + "/" + traceabilityExistingEntryId;

		RestTemplate restTemplate = new RestTemplate();
		// int responseNo = restTemplate.getForObject(restURI, Integer.class);
		restTemplate.put(restURI, traceabilityNewEntryData);

		// TODO - how do I get the header value

		return 1;
	}
*/
	
	public synchronized int updateTraceabilityRecord(String serviceId,
			String traceabilityRecordURI, String traceabilityNewEntryData) {

		logger.info("URI to the updated record: "+traceabilityRecordURI);
		//String restURI = "http://" + server_add + ":" + port_no + "/" + service
			//	+ "/" + resource + "/" + serviceId + "/"
				//+ this.TRACEABILITY_TYPE + "/" + traceabilityExistingEntryId;

		RestTemplate restTemplate = new RestTemplate();
		
		HttpEntity<String> entityContent = new HttpEntity<String>(traceabilityNewEntryData, null);

		ResponseEntity<String> response = restTemplate.exchange(traceabilityRecordURI, HttpMethod.PUT, entityContent, String.class);
		 
	    
		// int responseNo = restTemplate.getForObject(restURI, Integer.class);
//		restTemplate.put(traceabilityRecordURI, traceabilityNewEntryData);

		// TODO - how do I get the header value

		return response.getStatusCode().value();
	}
	
	
	// Encore the URI
	public String getTraceabilityRecordElement(String serviceId,
			String traceabilityRecordId, String tracebailityElementId) {

		String restURI = "http://" + server_add + ":" + port_no + "/" + service
				+ "/" + resource + "/" + serviceId + "/"
				+ this.TRACEABILITY_TYPE + "/" + traceabilityRecordId + "/"
				+ tracebailityElementId;

		RestTemplate restTemplate = new RestTemplate();
		String responseElement = restTemplate.getForObject(restURI,
				String.class);

		return responseElement;

	}

	public String getServer_add() {
		return server_add;
	}

	public void setServer_add(String server_add) {
		this.server_add = server_add;
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

	public int getPort_no() {
		return port_no;
	}

	public void setPort_no(int port_no) {
		this.port_no = port_no;
	}

	public String getTRACEABILITY_TYPE() {
		return TRACEABILITY_TYPE;
	}

	public void setTRACEABILITY_TYPE(String tRACEABILITY_TYPE) {
		TRACEABILITY_TYPE = tRACEABILITY_TYPE;
	}

	public int getDEFAULT_TIMEOUT() {
		return DEFAULT_TIMEOUT;
	}

	public void setDEFAULT_TIMEOUT(int dEFAULT_TIMEOUT) {
		DEFAULT_TIMEOUT = dEFAULT_TIMEOUT;
	}

	public String getTraceabilityRecordId() {
		return traceabilityRecordId;
	}

	public void setTraceabilityRecordId(String traceabilityRecordId) {
		this.traceabilityRecordId = traceabilityRecordId;
	}

}
