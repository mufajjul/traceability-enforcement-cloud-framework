/**
 * @file 		MessagingBroker.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		EventHandler
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.eventhandler;

import java.net.URI;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;

/**
 * @author Mufy TODO - complete the broker class for JMS
 */
public class MessagingBroker {

	public MessagingBroker() {

	}

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
