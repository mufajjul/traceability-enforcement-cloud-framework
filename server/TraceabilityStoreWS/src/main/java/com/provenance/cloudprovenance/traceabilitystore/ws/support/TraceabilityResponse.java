/**
 * @file 		TraceabilityResponse.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		TraceabilityModel
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.traceabilitystore.ws.support;

import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.converter.resource.TraceabilityConverter;
import com.provenance.cloudprovenance.service.api.impl.CprovObjectTraceability;
import com.provenance.cloudprovenance.traceabilitystore.ns.CprovNamespacePrefixMapper;

/** 
* @author Mufy
* 
*/
public class TraceabilityResponse {

	@Autowired
	CprovNamespacePrefixMapper cMapper;
	@Autowired
	private TraceabilityConverter trConverter;

	public String genTraceabilityRecordIdResponse(String trRecordId,
			String serviceName, String defaultFileExtension, String responseURI) {

		String fileId=trRecordId;
		if (trRecordId.contains(defaultFileExtension)){
			fileId = (trRecordId.split("."+defaultFileExtension))[0];
		}
		
		CprovObjectTraceability obTr = new CprovObjectTraceability(serviceName,
				cMapper.getNsSuffixCOnfidenshare());

		obTr.cResource(fileId, "resource-identification", "=bob[=]!:uuid:f81d4fae",
				"[=]!:pid:g81d4fde-000email", "pid:00:12:00:12:100:11:00:00",
				true, null, null, "general", 1.0f, responseURI);

		return trConverter
				.marhsallObject(obTr.getCurrentTraceabilityDocument());

	}

}
