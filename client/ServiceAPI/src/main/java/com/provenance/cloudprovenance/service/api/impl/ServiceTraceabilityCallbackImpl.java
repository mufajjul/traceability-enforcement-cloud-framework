/*
 * @(#) ServiceTraceabilityCallbackImpl.java       1.1 19/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.api.impl;

import org.apache.log4j.Logger;

import com.provenance.cloudprovenance.service.traceability.api.ServiceTraceabilityCallback;

/**
 * This is the service called to acknowledge of storage outcome
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
 */
public class ServiceTraceabilityCallbackImpl implements
		ServiceTraceabilityCallback<String> {

	private static Logger logger = Logger.getLogger("DynamicPolicyRequest");

	@Override
	public String traceabilityStored() {

		logger.info("Traceability callback sucessful");
		return "Success";
	}

}
