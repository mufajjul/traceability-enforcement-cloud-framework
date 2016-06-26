/**
 * @file 		PolicyApiTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
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
* @author Mufy
* 
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
		String environment2 = "reg.share.confidenshare.labs.orange.com";
		String environment3 = "mod.share.confidenshare.labs.orange.com";		
		
	//	srvCompliance.constructRequest(userAgent, false, resource, false, null,
		//		process, true, null);
		
		String response = srvCompliance.constructRequest(userAgent,resource, 
				process, environment1 );
		logger.info("final response: "+response);
		
		
	}

}
