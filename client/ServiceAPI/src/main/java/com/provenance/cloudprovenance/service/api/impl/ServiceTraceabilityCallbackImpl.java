/**
 * @file 		ServiceTraceabilityCallbackImpl.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.api.impl;

import org.apache.log4j.Logger;

import com.provenance.cloudprovenance.service.traceability.api.ServiceTraceabilityCallback;

public class ServiceTraceabilityCallbackImpl implements
		ServiceTraceabilityCallback<String> {

	private static Logger logger = Logger.getLogger("DynamicPolicyRequest");

	@Override
	public String traceabilityStored() {

		logger.info("Traceability callback sucessful");
		return "Success";
	}

}
