/**
 * @file 		PolicyResponseGenerator.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		PolicyHandlerWS
 * @date 		18 05 2013
 * @version 	1.0
 */

package com.provenance.cloudprovenance.policyhandler.ws.support;

import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.converter.resource.TraceabilityConverter;
import com.provenance.cloudprovenance.sconverter.mapper.CprovObjectTraceability;
import com.provenance.cloudprovenance.traceabilitystore.ns.CprovNamespacePrefixMapper;

/**
* 
* @author Mufy
* 
*/
public class PolicyResponseGenerator {

	@Autowired
	CprovNamespacePrefixMapper cMapper;
	@Autowired
	private TraceabilityConverter trConverter;

	public String genPolicyIdResponse(
			String serviceName, String reqId, String resId, String responseURI) {
		
		CprovObjectTraceability obTr = new CprovObjectTraceability(serviceName,
				cMapper.getNsSuffixCOnfidenshare());

		obTr.cResource(reqId, "request-identification", "=bob[=]!:uuid:f81d4fae",
				"[=]!:pid:g81d4fde-000email", "pid:00:12:00:12:100:11:00:00",
				true, null, null, "general", 1.0f, null);

		obTr.cResource(resId, "response-identification", "=bob[=]!:uuid:f81d4fae",
				"[=]!:pid:g81d4fde-000email", "pid:00:12:00:12:100:11:00:00",
				true, null, null, "general", 1.0f, responseURI);
		

		return trConverter
				.marhsallObject(obTr.getCurrentTraceabilityDocument());

	}

}
