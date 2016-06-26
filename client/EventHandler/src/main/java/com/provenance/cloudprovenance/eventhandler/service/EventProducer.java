/**
 * @file 		EventProducer.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		EventHandler
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.eventhandler.service;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.provenance.cloudprovenance.converter.ResourceConverter;

/**
 * @author Mufy
 * 
 */
public class EventProducer<T> {

	String queueName; // defult queue name
	String responsePolicyQueueName;
	String responseTraceabilityQueueName;
	private boolean traceabilityStoreStorageAck;

	private final JmsTemplate jmsTemplate;
	// private TraceabilityStatementsCollector<T> tsc;
	private final ResourceConverter<T> tc;

	Logger logger = Logger.getLogger(EventProducer.class);

	public EventProducer(final JmsTemplate jmsTemplate, ResourceConverter<T> tc) {
		this.jmsTemplate = jmsTemplate;
		this.tc = tc;
	}

	/*
	 * public EventProducer(final JmsTemplate jmsTemplate) { this.jmsTemplate =
	 * jmsTemplate; }
	 */
	public String sendEvent(T eventItem) {

		// Marshall Item

		String xmlTraceabilityContent = tc.marhsallObject(eventItem);
		String policyEventResponse = sendToQueue(queueName,
				xmlTraceabilityContent);
		// logger.info("Sucessfully added to the following statements:"
		// + xmlTraceabilityContent);

		return policyEventResponse;

	}

	// @Deprecated
	public void send(final Map map) {
		jmsTemplate.convertAndSend(map);
	}

	public String sendToQueue(final String eventTypeDestination,
			final String resourceElements) {

		String policyResponseMsg = null;
		final String IGNORE_RECEIVE_QUEUE = "traceabilityQueue";

		jmsTemplate.send(eventTypeDestination, new MessageCreator() {

			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session
						.createTextMessage(resourceElements);
				// message.setIntProperty("messageCount", i);
				return message;
			}
		});

		logger.info("Message sent: " + resourceElements);

		if (!(eventTypeDestination.equals(IGNORE_RECEIVE_QUEUE))) {

			logger.info("waiting for response message from queue: "
					+ responsePolicyQueueName);
			jmsTemplate.setReceiveTimeout(5000);
			Message message = jmsTemplate.receive(responsePolicyQueueName);

			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				try {
					policyResponseMsg = textMessage.getText();

					logger.info("Message received: " + policyResponseMsg);
					return policyResponseMsg;

				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			// it is a traceability event, i.e asynchronized
		} else if (eventTypeDestination.equals(IGNORE_RECEIVE_QUEUE)) {
			// need to return the policy id

			// if a response is needed from the store record (i.e synchronized)
			if (traceabilityStoreStorageAck) {
				// wait for a response;

				logger.info("waiting for a traceability response message from queue: "
						+ responseTraceabilityQueueName);
				jmsTemplate.setReceiveTimeout(5000);
				Message message = jmsTemplate
						.receive(responseTraceabilityQueueName);

				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					try {
						String traceabilityResponseMsg = textMessage.getText();

						logger.info("Message received, Id : "
								+ traceabilityResponseMsg);
						return traceabilityResponseMsg;

					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
			return "traceability stored successfully";

		}
		// did not recognise the message
		return null; // policyResponseMsg;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getResponsePolicyQueueName() {
		return responsePolicyQueueName;
	}

	public void setResponsePolicyQueueName(String responsePolicyQueueName) {
		this.responsePolicyQueueName = responsePolicyQueueName;
	}

	public String getResponseTraceabilityQueueName() {
		return responseTraceabilityQueueName;
	}

	public void setResponseTraceabilityQueueName(
			String responseTraceabilityQueueName) {
		this.responseTraceabilityQueueName = responseTraceabilityQueueName;
	}

	public boolean isTraceabilityStoreStorageAck() {
		return traceabilityStoreStorageAck;
	}

	public void setTraceabilityStoreStorageAck(
			boolean traceabilityStoreStorageAck) {
		this.traceabilityStoreStorageAck = traceabilityStoreStorageAck;
	}

}
