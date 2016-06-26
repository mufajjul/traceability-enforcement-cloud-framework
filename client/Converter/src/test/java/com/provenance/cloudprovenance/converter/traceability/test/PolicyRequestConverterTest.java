/**
 * @file 		PolicyRequestConverterTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		Converter
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.converter.traceability.test;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.provenance.cloudprovenance.converter.resource.PolicyRequestConverter;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.ObjectFactory;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.PolicyRequest;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.PolicyRequest.Agent;

/**
 * @author Mufy
 * 
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
		
//		req.setAgent(agent);
		
		String outcome = pConverter.marhsallObject(req);
		logger.info("outcome: "+outcome);
		
	}
	
	
}
