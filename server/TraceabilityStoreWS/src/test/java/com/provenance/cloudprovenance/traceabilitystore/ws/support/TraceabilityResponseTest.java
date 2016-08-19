/*
 * @(#) TraceabilityResponseTest.java       1.1 13/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.traceabilitystore.ws.support;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import com.provenance.cloudprovenance.converter.resource.TraceabilityConverter;

/**
 * This class tests the functionality of the Traceability response class
 *
 * @version 1.1 13 Aug 2016
 * @author Mufy
 * @Module TraceabilityStoreWS
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans.xml")
public class TraceabilityResponseTest {

	@Autowired
	TraceabilityResponse trResponse;
	@Autowired
	TraceabilityConverter trConverter;

	static Logger logger = Logger.getLogger("TraceabilityResponseTest");

	@Test
	public void resGenForCreateTest() {
		String currentTraceabilityRecord = "record1.xml";
		String serviceId = "ConfidenShare";
		String recordType = "xml";
		String Uri = "http://labs.orange.com/uk/cloudProvenance/cprov-ProvenanceStore/traceabilityDocument/ConfidenShare/ServiceTraceability/document001";
		String responseContent = trResponse.genTraceabilityRecordIdResponse(
				currentTraceabilityRecord, serviceId, recordType, Uri);
		logger.info(responseContent);

		Assert.notNull(responseContent);

	}

}
