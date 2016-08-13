/*
 * @(#) TraceabilityResponse.java       1.1 13/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.traceabilitystore.ws.support;

import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.converter.resource.TraceabilityConverter;
import com.provenance.cloudprovenance.service.api.impl.CprovObjectTraceability;
import com.provenance.cloudprovenance.traceabilitystore.ns.CprovNamespacePrefixMapper;

/**
 * This class converts the output of a the policy engine into a cProv response
 *
 * @version 1.1 13 Aug 2016
 * @author Mufy
 * @Module TraceabilityStoreWS
 */

public class TraceabilityResponse {

	@Autowired
	CprovNamespacePrefixMapper cMapper;
	@Autowired
	private TraceabilityConverter trConverter;

	public String genTraceabilityRecordIdResponse(String trRecordId,
			String serviceName, String defaultFileExtension, String responseURI) {

		String fileId = trRecordId;
		if (trRecordId.contains(defaultFileExtension)) {
			fileId = (trRecordId.split("." + defaultFileExtension))[0];
		}

		CprovObjectTraceability obTr = new CprovObjectTraceability(serviceName,
				cMapper.getNsSuffixCOnfidenshare());

		obTr.cResource(fileId, "resource-identification",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000email",
				"pid:00:12:00:12:100:11:00:00", true, null, null, "general",
				1.0f, responseURI);

		return trConverter
				.marhsallObject(obTr.getCurrentTraceabilityDocument());

	}

}
