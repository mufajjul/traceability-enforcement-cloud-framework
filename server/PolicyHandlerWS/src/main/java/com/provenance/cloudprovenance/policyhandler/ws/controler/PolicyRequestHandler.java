/**
 * @file 		PolicyRequestHandler.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		PolicyHandlerWS
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.policyhandler.ws.controler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.policyhandler.ws.support.PolicyRequestProcessor;
import com.provenance.cloudprovenance.policyhandler.ws.support.PolicyResponseGenerator;
import com.provenance.cloudprovenance.sconverter.PolicyResponseConverter;
import com.provenance.cloudprovenance.storagecontroller.presistence.traceabilitystore.policy.XmlDbPolicyTraceability;

/**
 * REST implementations for service interactions
 * 
 * @author Mufy
 * 
 */
@Path("/")
@Produces("application/xml")
public class PolicyRequestHandler implements PolicyRequest<Response> {

	static Logger logger = Logger.getLogger("PolicyRequestHandler");

	@Autowired
	private XmlDbPolicyTraceability policyTraceability;
	@Autowired
	PolicyResponseGenerator policyResponseGen;
	@Autowired
	PolicyRequestProcessor preqProcessor;
	@Autowired
	PolicyResponseConverter  policyResConv;

	static int counter = 1;
	String requestFileNamePrefix;
	String responseFileNamePrefix;
	String fileNameSuffix;
	String traceabilityType;
	String xpathToEnvironmentId;
	String xpathToXACMLdecisionId;
	

	static HashMap<String,String[]> requestPolicyMappingMap;

	static HashMap<String, String> policyResponseMap = new HashMap<String, String>();

	@Override
	@POST
	@Path(value = "/{serviceId}")
	public Response policyRequest(@PathParam("serviceId") String serviceId,
			@Context HttpServletRequest request) {

		try {

			String constructFileName = requestFileNamePrefix + counter + "."
					+ fileNameSuffix;
			String policyRequestMsg = getBody(request);

			// Store the request to the policy traceability (in traceability
			// store)
			boolean outcome = policyTraceability.createRequestInstance(
					serviceId, traceabilityType, constructFileName,
					policyRequestMsg);
			logger.info("Saved policy request to the policy traceability store");

			// Call the policy controller to validate the policy, remove namespace
			String environmentId = (preqProcessor
					.getIdforPolicyMatch(policyRequestMsg,
							xpathToEnvironmentId)).split(":")[1];

			logger.info("The environment id to match a policy is: "
					+ environmentId);
			// TODO - need to find a way to map a request to an policy !!!!
			
			String[] policyToSelect = requestPolicyMappingMap.get(environmentId);

			logger.info("policy id is: "+policyToSelect[0]);
			
			String response = preqProcessor.executePolicyRequest(serviceId, policyRequestMsg, policyToSelect, request);

			logger.info("policy execution response: "+response);
			
			
			// TODO - replace it with regular expression
		//	String xacmlDecisionValue = preqProcessor
			//		.getIdforPolicyMatch(response,
				//			xpathToXACMLdecisionId);

//			logger.info ("policy response is: "+ xacmlDecisionValue);
			
			// TODO - Convert the XACML response a cProvl response

			String responseURI = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ request.getRequestURI() + "/" + responseFileNamePrefix +counter;
			
			// TODO - process the response store in in the hashmap
			String responseContent = policyResponseGen.genPolicyIdResponse(serviceId,
					 requestFileNamePrefix+counter, responseFileNamePrefix +counter,responseURI);
			
			logger.info("Storing response in a map: Key=> "+(responseFileNamePrefix +counter));
			//Store the response to the HashTable 
			policyResponseMap.put((responseFileNamePrefix +counter), response);

			if (response != null) {
				ResponseBuilder rBuilder = Response.status(201);
				rBuilder.entity(responseContent);
				return rBuilder.build();
			} else {
				ResponseBuilder rBuilder = Response.status(400);
				return rBuilder.build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ResponseBuilder rBuilder = Response.status(500);
			return rBuilder.build();

		}

		// return "<response> to be implemented </reponse>";
	}

	@Override
	@GET
	@Path(value = "/{serviceId}/{responseId}")
	public Response policyResponse(@PathParam("serviceId") String serviceId,
			@PathParam("responseId") String responseId) {

		String constructFileName = responseFileNamePrefix + (counter++) + "."
				+ fileNameSuffix;
		
		logger.info ("Response Id received from the user: "+responseId);

		String response = policyResponseMap.get(responseId);

	
		String responseContent =  policyResConv.XMLpolicyResponse(response);
		
		boolean outcome = policyTraceability.createResponseInstance(serviceId,
				traceabilityType, constructFileName,
				responseContent);

		if (outcome == true) {
			ResponseBuilder rBuilder = Response.status(201);
			rBuilder.entity(responseContent);
			return rBuilder.build();
		} else {
			ResponseBuilder rBuilder = Response.status(400);
			return rBuilder.build();
		}
	}

	public static String getBody(HttpServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(
						inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;
	}

	public String getTemplateProvFile() {

		BufferedReader br = null;

		InputStream is = (getClass().getClassLoader()
				.getResourceAsStream("templateTraceabilityfile.xml"));

		try {
			br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}

			logger.info(sb.toString());
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getRequestFileNamePrefix() {
		return requestFileNamePrefix;
	}

	public void setRequestFileNamePrefix(String requestFileNamePrefix) {
		this.requestFileNamePrefix = requestFileNamePrefix;
	}

	public String getResponseFileNamePrefix() {
		return responseFileNamePrefix;
	}

	public void setResponseFileNamePrefix(String responseFileNamePrefix) {
		this.responseFileNamePrefix = responseFileNamePrefix;
	}

	public String getFileNameSuffix() {
		return fileNameSuffix;
	}

	public void setFileNameSuffix(String fileNameSuffix) {
		this.fileNameSuffix = fileNameSuffix;
	}

	public String getTraceabilityType() {
		return traceabilityType;
	}

	public void setTraceabilityType(String traceabilityType) {
		this.traceabilityType = traceabilityType;
	}

	public String getXpathToXACMLdecisionId() {
		return xpathToXACMLdecisionId;
	}

	public void setXpathToXACMLdecisionId(String xpathToXACMLdecisionId) {
		this.xpathToXACMLdecisionId = xpathToXACMLdecisionId;
	}

	public String getXpathToEnvironmentId() {
		return xpathToEnvironmentId;
	}

	public void setXpathToEnvironmentId(String xpathToEnvironmentId) {
		this.xpathToEnvironmentId = xpathToEnvironmentId;
	}

	public HashMap<String, String[]> getRequestPolicyMappingMap() {
		return requestPolicyMappingMap;
	}

	public void setRequestPolicyMappingMap(
			HashMap<String, String[]> requestPolicyMappingMap) {
		this.requestPolicyMappingMap = requestPolicyMappingMap;
	}

	
}
