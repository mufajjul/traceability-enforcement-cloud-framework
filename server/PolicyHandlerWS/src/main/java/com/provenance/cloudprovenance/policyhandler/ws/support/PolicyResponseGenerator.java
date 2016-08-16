/*
 * @(#) PolicyResponseGenerator.java       1.1 16/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.policyhandler.ws.support;

import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.converter.resource.TraceabilityConverter;
import com.provenance.cloudprovenance.sconverter.mapper.CprovObjectTraceability;
import com.provenance.cloudprovenance.traceabilitystore.ns.CprovNamespacePrefixMapper;

/**
 * This class generated a policy response of cProvl type
 * 
 * @version 1.1 16 Aug 2016
 * @author Mufy
 * @Module PolicyHandlerWS
 */
public class PolicyResponseGenerator {

	@Autowired
	CprovNamespacePrefixMapper cMapper;
	@Autowired
	private TraceabilityConverter trConverter;

	public String genPolicyIdResponse(String serviceName, String reqId,
			String resId, String responseURI) {

		CprovObjectTraceability obTr = new CprovObjectTraceability(serviceName,
				cMapper.getNsSuffixCOnfidenshare());

		obTr.cResource(reqId, "request-identification",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000email",
				"pid:00:12:00:12:100:11:00:00", true, null, null, "general",
				1.0f, null);

		obTr.cResource(resId, "response-identification",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000email",
				"pid:00:12:00:12:100:11:00:00", true, null, null, "general",
				1.0f, responseURI);

		return trConverter
				.marhsallObject(obTr.getCurrentTraceabilityDocument());

	}

}
