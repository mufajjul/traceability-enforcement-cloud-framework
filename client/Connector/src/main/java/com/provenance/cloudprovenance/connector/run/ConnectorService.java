/**
 * @file 		ConnectorService.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		Connector
 * @date 		18 05 2013
 * @version 	1.0
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
 * @author Mufy
 * 
 */
public class ConnectorService  {

	private static ConnectorService conServiceInstance = null;

	private ConnectorService() {

	}

	public static ConnectorService ConnectionServiceInstance() {

		if (conServiceInstance == null) {
			conServiceInstance = new ConnectorService();
			return conServiceInstance;
		} else {
			return conServiceInstance;
		}
	}

	public void run() {
		//logger.info ("starting event threads");
		//startJMSmessageReceiver();
	}

	public void start() {
		try {
			startJMSbroker();
			startJMSmessageReceiver();
			//this.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void startJMSmessageReceiver() {

		ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "beans.xml" });

		// handlers for receiving async response messages
		TraceabilityEventConsumer traceabilityEventConsumer = (TraceabilityEventConsumer) ctx
				.getBean("traceabilityEventConsumer");
		PolicyEventConsumer policyEventConsumer = (PolicyEventConsumer) ctx
				.getBean("policyEventConsumer");
	}

	private void startJMSbroker() throws Exception {

		String URIAdd = "tcp://localhost:61616";
		BrokerService broker = new BrokerService();

		TransportConnector connector = new TransportConnector();
		connector.setUri(new URI(URIAdd));
		broker.addConnector(connector);
		
		broker.setUseJmx(true);
	    broker.getManagementContext().setConnectorPort(9999);
		
		broker.start();
	}

	public static void main(String args[]) {

		new ConnectorService().start();
	}

}
