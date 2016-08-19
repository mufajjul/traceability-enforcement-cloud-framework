/*
 * @(#) ConnectorService.java       1.1 13/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.connector.run;

import java.net.URI;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.provenance.cloudprovenance.connector.policy.PolicyEventConsumer;
import com.provenance.cloudprovenance.connector.traceability.TraceabilityEventConsumer;

/**
 * This class provides a simple JMS broker. It is a singleton class
 *
 * @version 1.1 13 Aug 2016
 * @author Mufy
 * @Module Connector
 */
public class ConnectorService {

	private static ConnectorService conServiceInstance = null;

	private ConnectorService() {
	}

	// Create a single instance of JMS Brokervia this method
	public static ConnectorService ConnectionServiceInstance() {

		if (conServiceInstance == null) {
			conServiceInstance = new ConnectorService();
			return conServiceInstance;
		} else {
			return conServiceInstance;
		}
	}

	public void start() {
		try {
			startJMSbroker();
			startJMSmessageReceiver();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void startJMSmessageReceiver() {

		// TODO - use a property file
		String nameofSpringBeanConfigFile = "beans.xml";
		String trEventConsumerId = "traceabilityEventConsumer";
		String poEventConsumerId = "policyEventConsumer";

		ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { nameofSpringBeanConfigFile });

		// handlers for receiving async response messages
		TraceabilityEventConsumer traceabilityEventConsumer = (TraceabilityEventConsumer) ctx
				.getBean(trEventConsumerId);
		PolicyEventConsumer policyEventConsumer = (PolicyEventConsumer) ctx
				.getBean(poEventConsumerId);

		// TODO - why do we need this?
	}

	/** Method that registers a JMS borkaer on the port 61616 */
	private void startJMSbroker() throws Exception {

		// TODO - use a property file

		String URIAdd = "tcp://localhost:61616";
		int connectorPort = 9999;
		BrokerService broker = new BrokerService();

		TransportConnector connector = new TransportConnector();
		connector.setUri(new URI(URIAdd));
		broker.addConnector(connector);

		broker.setUseJmx(true);
		broker.getManagementContext().setConnectorPort(connectorPort);

		broker.start();
	}

}
