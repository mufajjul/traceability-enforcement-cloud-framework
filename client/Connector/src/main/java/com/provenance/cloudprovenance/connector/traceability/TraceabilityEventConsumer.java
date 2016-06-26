/**
 * @file 		ConnectorService.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		Connector
 * @date 		18 05 2013
 * @version 	1.0
 */

package com.provenance.cloudprovenance.connector.traceability;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.xml.sax.SAXException;

import com.provenance.cloudprovenance.connector.traceability.response.ResponseExtraction;

/**
 * @author Mufy
 *
 */
public class TraceabilityEventConsumer implements MessageListener {

	private JmsTemplate jmsTemplate;
	private String responseTraceabilityQueueName;
	private boolean traceabilityStoreStorageAck;

	private ResponseExtraction resExtraction;
	private TraceabilityStore trConnection;
	private String serviceID;
	static int counter = 1;
	String traceabilityRecordUri = null;

	private static Logger logger = Logger
			.getLogger("TraceabilityEventConsumer");

	public TraceabilityEventConsumer(TraceabilityStore trStoreCon,
			String serviceID) {
		this.trConnection = trStoreCon;
		this.serviceID = serviceID;

		logger.info("************* Starting Traceability Event Receiver ************");

	}

	public void onMessage(final Message newMessage) {

		logger.info("Received Traceability Statements Message = " + counter++);

		if (newMessage instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) newMessage;

			String traceabilityRecord = null;
			try {
				traceabilityRecord = textMessage.getText();

				logger.info(" Traceability content: \n" + traceabilityRecord);

				/**
				 * Get the record Id, if it is just use POST request, otherwise
				 * use PUT reequest
				 */

				if (traceabilityRecordUri == null
						|| traceabilityRecordUri.equals("")) {

					logger.info("Calling get Resource URI ...");
					String traceabilityRecordResponse = trConnection
							.getCurrentTraceabilityRecordId(serviceID);

					if (traceabilityRecordResponse != null
							|| traceabilityRecordResponse == "") {

						logger.info("Resource URI does not exist, creation a new resource with content");

						// get existing record ID

						/*
						 * traceabilityRecordId = (resExtraction
						 * .getResponseId(traceabilityRecordResponse));//
						 * .split(":")[1]; logger.info(
						 * "Sucessfully retrieved traceability record Id : " +
						 * traceabilityRecordId);
						 * 
						 * logger.info("Updating record: " +
						 * traceabilityRecordId);
						 */

						traceabilityRecordUri = (resExtraction
								.getResponseURI(traceabilityRecordResponse));

						logger.info("New resource created URI is: "
								+ traceabilityRecordUri);

						trConnection.updateTraceabilityRecord(serviceID,
								traceabilityRecordUri, traceabilityRecord);

						logger.info("Sucessfully updated the traceability record: "
								+ traceabilityRecordUri);

					} else {
						// get a new record Id
						String response = trConnection
								.createNewTraceabilityRecord(serviceID,
										traceabilityRecord);

						traceabilityRecordUri = (resExtraction
								.getResponseURI(response));// .split(":")[1];
						logger.info("Sucessfully created a new traceability record: "
								+ response + "\n" + traceabilityRecordUri);
					}
				} else {
					logger.info("Updating record :" + traceabilityRecordUri);
					trConnection.updateTraceabilityRecord(serviceID,
							traceabilityRecordUri, traceabilityRecord);

					logger.info("Sucessfully updated the traceability record: "
							+ traceabilityRecordUri);

				}

				// If an ack is required, use the reponse queue
				if (traceabilityStoreStorageAck) {
					logger.info("Sending an ACK to response tranceability queue"
							+ traceabilityRecordUri);

					jmsTemplate.send(responseTraceabilityQueueName,
							new MessageCreator() {

								public Message createMessage(Session session)
										throws JMSException {
									TextMessage message = session
											.createTextMessage("Sucessfully Stored, ID: "
													+ counter);
									// message.setIntProperty("messageCount",
									// i);
									return message;
								}
							});
				}

			} catch (JMSException e) {
				logger.error(e.toString());
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			logger.warn("Unknown message type, cannot process");
		}
	}

	public ResponseExtraction getResExtraction() {
		return resExtraction;
	}

	public void setResExtraction(ResponseExtraction resExtraction) {
		this.resExtraction = resExtraction;
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public boolean isTraceabilityStoreStorageAck() {
		return traceabilityStoreStorageAck;
	}

	public void setTraceabilityStoreStorageAck(
			boolean traceabilityStoreStorageAck) {
		this.traceabilityStoreStorageAck = traceabilityStoreStorageAck;
	}

	public String getResponseTraceabilityQueueName() {
		return responseTraceabilityQueueName;
	}

	public void setResponseTraceabilityQueueName(
			String responseTraceabilityQueueName) {
		this.responseTraceabilityQueueName = responseTraceabilityQueueName;
	}

}
