/*
 * @(#) PolicyRequestConverterTest.java       1.1 16/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.converter.traceability.test;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.provenance.cloudprovenance.converter.resource.PolicyRequestConverter;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.ObjectFactory;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.PolicyRequest;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.PolicyRequest.Agent;

/**
* This is a test class for policy request converter 
*
* @version 1.1 16 Aug 2016
* @author Mufy
* @Module Converter
*/
@ContextConfiguration("classpath:beans.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class PolicyRequestConverterTest {

	@Autowired
	private PolicyRequestConverter pConverter;
	Logger logger = Logger.getLogger(PolicyRequestConverterTest.class);

	@Test 
	public void convertPolicyToXMLTest(){
		
		ObjectFactory tModelFactory = new ObjectFactory();
		
		PolicyRequest req = tModelFactory.createPolicyRequest();
		
		Agent agent = tModelFactory.createPolicyRequestAgent();
		QName agentId = new QName("www.temp.org", "ag001"
				, "ex");

		agent.setId(agentId);
		req.getAgent().add(agent);

		String outcome = pConverter.marhsallObject(req);
		Assert.notNull(outcome);
		
		logger.info("outcome: "+outcome);
		
	}
	
}
