/*
 * @(#) JMSBroker.java       1.1 18/8/2016
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

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;

/**
 * This is a simple JMS broker test class
 * 
 * @version 1.1 18 Aug 2016
 * @author Mufy
 * @Module EventHandler
 */
public class JMSBroker {

	static BrokerService broker = new BrokerService();
	
	public static void JMSBrokerStart() throws Exception{
		String URIAdd = "tcp://localhost:61616";

		TransportConnector connector = new TransportConnector();
		connector.setUri(new URI(URIAdd));
		broker.addConnector(connector);
		
		broker.setUseJmx(true);
	    broker.getManagementContext().setConnectorPort(9999);
		
		broker.start();
	}
	
	public static void JMSBrokerStop() throws Exception {
		
		broker.stop();
	}
	
	
}
