/*
 * @(#) TraceabilityPolicyTest.java       1.1 16/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.storagecontroller.persistence.traceability.policy.test;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.provenance.cloudprovenance.storagecontroller.presistence.traceabilitystore.policy.XmlDbPolicyTraceability;

/**
 * This class tests various methods related to policy 
 * 
 * @version 1.1 16 Aug 2016
 * @author Mufy
 * @Module StorageController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans.xml")
public class TraceabilityPolicyTest {

	@Autowired
	private XmlDbPolicyTraceability policyTraceability;

	private static Logger logger = Logger.getLogger("DBTest");

	@Test
	public void createPolcyRequestTest() {

		String policyRequestFileName = "policyRequest1.xml";
		String serviceId = "ConfidenShare";
		String traceabilityType = "PolicyTraceability";
		String tempContent = "<request></request>";

		boolean outcome = policyTraceability.createRequestInstance(serviceId,
				traceabilityType, policyRequestFileName, tempContent);

		Assert.assertTrue(outcome);

	}

	@Test
	public void createPolicyRequestTest() {

		String policyRequestFileName = "policyResponse1.xml";
		String serviceId = "ConfidenShare";
		String traceabilityType = "PolicyTraceability";
		String tempContent = "<response></response>";

		boolean outcome = policyTraceability.createResponseInstance(serviceId,
				traceabilityType, policyRequestFileName, tempContent);

		Assert.assertTrue(outcome);

	}
}
