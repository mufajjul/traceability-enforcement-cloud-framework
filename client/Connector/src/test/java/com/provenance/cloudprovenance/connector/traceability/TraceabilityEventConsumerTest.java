/**
 * @file 		TraceabilityEventConsumerTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		Connector
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.connector.traceability;

import java.net.URI;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Mufy
 * 
 */
@Ignore
@ContextConfiguration("classpath:beans.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TraceabilityEventConsumerTest {

	
//	@Autowired
//	TraceabilityEventConsumer tvc;

	@BeforeClass()
	public static void initialSetup() throws Exception {
		//tempJMSbroker();
		// sampleTestJmsMessageSent();
	}

	@Test
	public void receivedJMStraceabilityMessage() {

		// for (int i =0; i <15; i++){
		sampleTestJmsMessageSent();
		// }

	}

	public static void tempJMSbroker() throws Exception {

		BrokerService broker = new BrokerService();
		TransportConnector connector = new TransportConnector();
		connector.setUri(new URI("tcp://localhost:61616"));
		broker.addConnector(connector);
		// broker.setUseJmx(false);
		broker.start();

	}

	public void sampleTestJmsMessageSent() {
		try {
			// Create a ConnectionFactory
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					"vm://localhost");

			// Create a Connection
			Connection connection = connectionFactory.createConnection();
			connection.start();

			// Create a Session
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);

			// Create the destination (Topic or Queue)
			Destination destination = session.createQueue("traceabilityQueue");

			// Create a MessageProducer from the Session to the Topic or Queue
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			// Create a messages
			// String text = "Hello world! From: " +
			// Thread.currentThread().getName() ;

			for (int i = 0; i < 10; i++) {
				TextMessage message = session.createTextMessage(templateData());

				// Tell the producer to send the message
				System.out.println("Sent message: " + message.hashCode()
						+ " : " + Thread.currentThread().getName());
				producer.send(message);
			}
			// Clean up
			session.close();
			connection.close();
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}

	}

	private String templateData() {

		String XMLdef = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n";
		XMLdef += ("<cprov:traceabilityDocument xmlns:prov=\"http://www.w3.org/ns/prov#\" \n"
				+ " xmlns:cprov=\"http://labs.orange.com/uk/cprov#\" xmlns:cu=\"http://www.w3.org/1999/xhtml/datatypes/\" \n"
				+ " xmlns:r=\"http://labs.orange.com/uk/r#\" xmlns:ex=\"http://www.example.org/\" \n"
				+ " xmlns:opmx=\"http://openprovenance.org/model/opmx#\" xmlns:cprovd=\"http://labs.orange.com/uk/cprovd#\" \n"
				+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n"
				+ " xsi:schemaLocation=\"http://labs.orange.com/uk/cprov# file:/Users/mufy/Dropbox/EngD/year3/schema/cProv-inherited/cProv-v1.3.xsd\"> \n");

		XMLdef += "<prov:agent prov:id='ex:ag003'/>";

		XMLdef += "<cprov:cResource prov:id=\"ex:e001\">\n"
				+ "<cprov:type>data</cprov:type>\n"
				+ "<cprov:userTrustDegree>1</cprov:userTrustDegree>"
				+ "<cprov:userCloudRef>http://orangecloud/user@matt/cluster001/image001</cprov:userCloudRef>"
				+ "<cprov:vResourceRef>/linux/meetingService/res001</cprov:vResourceRef>"
				+ "<cprov:pResourceRef>/Cluster001/126.23.43.45/server001/00-12-00-12-00-1/e001</cprov:pResourceRef>"
				+ "<cprov:TTL>2015-10-10T12:00:00-05:00</cprov:TTL>"
				+ "</cprov:cResource>";

		XMLdef += "<prov:agent prov:id='ex:003'/>";

		XMLdef += ("</cprov:traceabilityDocument> \n");

		return XMLdef;
	}

}
