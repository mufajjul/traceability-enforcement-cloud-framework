/**
 * @file 		PolicyEnforcementConnectorTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		Connector
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.connector.policy;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Mufy
 * 
 */
@Ignore
@ContextConfiguration("classpath:beans.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class PolicyEnforcementConnectorTest {

	@Autowired
	PolicyEnforcementConnector poCon;
	
	String serviceId ="confidenShare";
	String responseId = "tobedefined";
	
	@Test 
	public void policyRequestTest(){
		String response = poCon.policyRequest(serviceId, this.dummyPolicyRequestContent());
	
		// get the response Id  		
	}
	
	@Test
	public void policyResponseTest (){
		String outcome = poCon.policyResponse(serviceId, responseId);
	}
	
	public String dummyPolicyRequestContent(){
	
		return "<traceabilityRequest> </traceabilityRequest>";
	}	
}
