/**
 * @file 		TraceabilityConsumerTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		EventHandler
 * @date 		18 05 2013
 * @version 	1.0
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
 * @author Mufy
 * 
 */
public class TraceabilityConsumerTest {

	Logger logger = Logger.getLogger(TraceabilityConsumerTest.class);

	// TraceabilityEventConsumer tConsumer;
	// PolicyEventConsumer pConsumer;

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

		// MessagingBroker broker = new MessagingBroker();
		// broker.startBroker();

		// CachingConnectionFactory con =
		// (CachingConnectionFactory)ctx.getBean("connectionFactory");
		// tConsumer = (TraceabilityEventConsumer)
		// ctx.getBean("traceabilityEventConsumer");
		// pConsumer = (PolicyEventConsumer) ctx.getBean("policyEventConsumer");

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

	}
}
