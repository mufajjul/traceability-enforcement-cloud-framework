/*
 * @(#) TraceabilityConsumerTest.java       1.1 18/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.eventhandler.service.test;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.provenance.cloudprovenance.eventhandler.service.EventProducer;
import com.provenance.cloudprovenance.traceabilityModel.generated.TraceabilityDocument;

/**
 * This class provides a number of tests for traceability statements
 * 
 * @version 1.1 18 Aug 2016
 * @author Mufy
 * @Module EventHandler
 */
public class TraceabilityConsumerTest {

	Logger logger = Logger.getLogger(TraceabilityConsumerTest.class);

	EventProducer<TraceabilityDocument> tProducer;

	@BeforeClass
	public static void startJMSBroker() throws Exception {

		JMSBroker.JMSBrokerStart();
	}

	@AfterClass
	public static void stopJMSBroker() throws Exception {

		JMSBroker.JMSBrokerStop();

	}

	@Before
	public void init() throws Exception {
		ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "beans.xml" });

		tProducer = (EventProducer<TraceabilityDocument>) ctx
				.getBean("eventProducer");
	}

	@Test
	public void sendTraceabilitycMessageTest() {

		String traceabilityQueue = "traceabilityQueue";

		tProducer.sendToQueue(traceabilityQueue, "Queue item 1");
		tProducer.sendToQueue(traceabilityQueue, "Queue item 2");
		tProducer.sendToQueue(traceabilityQueue, "Queue item 3");
		tProducer.sendToQueue(traceabilityQueue, "Queue item 4");
	}

	// @Ignore
	@Test
	public void sendPolicycMessageTest() {

		String policyQueue = "enforcementQueue";

		String policyQueue1 = "enforcementResponseQueue";

		logger.info(tProducer
				.sendToQueue(policyQueue1, "Queue response item 1"));
		logger.info(tProducer.sendToQueue(policyQueue, "Queue response item 2"));
		logger.info(tProducer.sendToQueue(policyQueue, "Queue response item 3"));
		logger.info(tProducer
				.sendToQueue(policyQueue1, "Queue response item 4"));
	}

	@Ignore
	@Test
	public void receiveTraceabilityMsg() {
		//TODO - to be implemented
	}
}
