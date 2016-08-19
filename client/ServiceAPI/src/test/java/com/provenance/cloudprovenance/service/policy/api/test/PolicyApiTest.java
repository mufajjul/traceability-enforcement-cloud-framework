/*
 * @(#) PolicyApiTest.java       1.1 19/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.policy.api.test;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.provenance.cloudprovenance.service.api.impl.DynamicPolicyRequest;

/**
 * This is test for composing policy request
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans.xml")
public class PolicyApiTest {

	@Autowired
	DynamicPolicyRequest srvCompliance;
	private Logger logger = Logger.getLogger("PolicyApiTest");

	@Test
	public void policyRequestTest() {

		String userAgent = "Bob";
		String resource = "document1";
		String process = "share";
		String environment1 = "reg.share.confidenshare.labs.orange.com";
		// String environment2 = "reg.share.confidenshare.labs.orange.com";
		// String environment3 = "mod.share.confidenshare.labs.orange.com";

		String response = srvCompliance.constructRequest(userAgent, resource,
				process, environment1);
		logger.info("final response: " + response);

	}
}
