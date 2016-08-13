package com.provenance.cloudprovenance.traceabilitystore.ws.support;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.provenance.cloudprovenance.converter.resource.TraceabilityConverter;

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
