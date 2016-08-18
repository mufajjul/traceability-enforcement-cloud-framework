/*
 * @(#) MessagingBroker.java       1.1 18/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.eventhandler;

import java.net.URI;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;

/**
 * This is a simple JMS mseeage borker class
 *
 * @version 1.1 18 Aug 2016
 * @author Mufy
 * @Module EventHandler
 */
public class MessagingBroker {

	public MessagingBroker() {

	}

	//Start the JMS borker on port 61616
	public void startBroker() throws Exception {
		
		BrokerService broker = new BrokerService();
		TransportConnector connector = new TransportConnector();
		connector.setUri(new URI("tcp://localhost:61616"));
		broker.addConnector(connector);
		// broker.setUseJmx(false);
		broker.start();
	}

	public static void main(String args[]) throws Exception {
		new MessagingBroker().startBroker();

	}

}
