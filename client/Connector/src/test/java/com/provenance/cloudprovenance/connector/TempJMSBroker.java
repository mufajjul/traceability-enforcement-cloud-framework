/**
 * @file 		TempJMSBroker.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		Connector
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.connector;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;

/**
 * @author Mufy
 * 
 */
public class TempJMSBroker {

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
