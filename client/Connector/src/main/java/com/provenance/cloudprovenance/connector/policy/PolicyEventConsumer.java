/*
 * @(#) PolicyEventConsumer.java       1.1 13/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */

package com.provenance.cloudprovenance.connector.policy;

import java.io.IOException;
import java.net.URL;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.xml.sax.SAXException;

//import com.provenance.cloudprovenance.connector.traceability.callback.TraceabilityNotificationEvent;
import com.provenance.cloudprovenance.connector.traceability.response.ResponseExtraction;

/**
 * This class listens for incoming JMS message as a policy request for a
 * service. It then invokes relevant URI for the request to be processed. Once a
 * response is received, the response is sent to another JMS queue for the
 * client to read from.
 *
 * @version 1.1 13 Aug 2016
 * @author Mufy
 * @Module Connector
 */
public class PolicyEventConsumer implements MessageListener {

	private PolicyEnforcement poEnforcement;
	private String serviceID;
	private static int counter = 1;
	private JmsTemplate jmsTemplate;
	private String responsePolicyQueueName;
	private ResponseExtraction resExtraction;

	private static Logger logger = Logger.getLogger("PolicyEventConsumer");

	public PolicyEventConsumer(PolicyEnforcement poEnforcement, String serviceID) {
		this.poEnforcement = poEnforcement;
		this.serviceID = serviceID;

		logger.info("************* Starting Policy Request Event Receiver ************");

	}

	/** Method called when a new message arrives */
	public void onMessage(final Message message) {

		final String outcome;

		logger.info("Received policy request message = " + counter++);

		// Check if the message is a text message
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			String policyRequest = null;
			try {
				policyRequest = textMessage.getText();

				logger.info("Sending  poicy request: " + policyRequest
						+ " .....");
				String response = poEnforcement.policyRequest(serviceID,
						policyRequest);

				logger.info("Response received for policy request: " + response);

				String responsURI = resExtraction.getResponseURI(response);

				logger.debug("Sucessfully extracted response message URI from the policy execution: "
						+ responsURI);

				outcome = poEnforcement.policyResponse(serviceID, new URL(
						responsURI));

				logger.info("Successfully retrieved a policy response: "
						+ outcome);

				logger.info("Sending the retrieved response to a Queue for a client to read: "
						+ outcome + " ......");

				jmsTemplate.send(responsePolicyQueueName, new MessageCreator() {

					public Message createMessage(Session session)
							throws JMSException {
						TextMessage message = session
								.createTextMessage(outcome);
						return message;
					}
				});

				logger.info("Successfully sent the response via the JMS queue to a client");

			} catch (JMSException e) {
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			logger.warn("Message received, but unable to process : " + message
					+ " ... !");
		}
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public ResponseExtraction getResExtraction() {
		return resExtraction;
	}

	public void setResExtraction(ResponseExtraction resExtraction) {
		this.resExtraction = resExtraction;
	}

	public String getResponsePolicyQueueName() {
		return responsePolicyQueueName;
	}

	public void setResponsePolicyQueueName(String responsePolicyQueueName) {
		this.responsePolicyQueueName = responsePolicyQueueName;
	}
}
