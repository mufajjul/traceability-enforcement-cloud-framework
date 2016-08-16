/*
 * @(#) DynamicPolicyResponse.java       1.1 16/8/2016
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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.converter.resource.TraceabilityConverter;
import com.provenance.cloudprovenance.sconverter.mapper.CprovObjectTraceability;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.ObjectFactory;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.PolicyResponse;
import com.provenance.cloudprovenance.traceabilitystore.ns.CprovNamespacePrefixMapper;

/**
 * This class implements the operations for the Compliance Response interface
 * 
 * @version 1.1 16 Aug 2016
 * @author Mufy
 * @Module PolicyHandlerWS
 */
public class DynamicPolicyResponse implements ComplianceResponse<PolicyResponse>{

	@Autowired
	private CprovNamespacePrefixMapper cMapper;
	@Autowired
	private TraceabilityConverter trConverter;
	
	private ObjectFactory pFactory = new ObjectFactory();
	private  String UNIQUE_IDENTIFIER_NS_SERVICE_SUFFIX;
	private  String UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX;
	
	private static Logger logger = Logger.getLogger("DynamicPolicyResponse");	
	
	public DynamicPolicyResponse() {
	}
	
	public DynamicPolicyResponse(String prefix, String suffix) {

		UNIQUE_IDENTIFIER_NS_SERVICE_SUFFIX = suffix;
		UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX = prefix+":";
	}
	
	public PolicyResponse constructResponse(String serviceName, String policyId, String requestId, String responseId, String responseValue){

		CprovObjectTraceability obTr = new CprovObjectTraceability(serviceName,
				cMapper.getNsSuffixCOnfidenshare());
		PolicyResponse pResponse = pFactory.createPolicyResponse();
		//TODO - Check the response
		
		return pResponse;
	}

	//TODO - To be implemented
	@Override
	public PolicyResponse constructResponse(String policyId, String requestId,
			String responseId, String responseValue) {
		return null;
	}

}
