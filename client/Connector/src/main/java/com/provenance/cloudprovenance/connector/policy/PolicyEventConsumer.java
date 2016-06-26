/**
 * @file 		PolicyEventConsumer.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		Connector
 * @date 		18 05 2013
 * @version 	1.0
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

import com.provenance.cloudprovenance.connector.traceability.callback.TraceabilityNotificationEvent;
import com.provenance.cloudprovenance.connector.traceability.response.ResponseExtraction;

/**
 * @author Mufy
 *
 */
public class PolicyEventConsumer implements MessageListener {

	private PolicyEnforcement poEnforcement;
	private String serviceID;
	private static Logger logger = Logger.getLogger("PolicyEventConsumer");
	private static int counter = 1;
	private JmsTemplate jmsTemplate;
	private String responsePolicyQueueName;
	private ResponseExtraction resExtraction;

	// public TraceabilityNotificationEvent<String> trPolicyResEvent;

	public PolicyEventConsumer(PolicyEnforcement poEnforcement, String serviceID) {
		this.poEnforcement = poEnforcement;
		this.serviceID = serviceID;

		logger.info("************* Starting Policy Request Event Receiver ************");

	}

	public void onMessage(final Message message) {

		logger.info("Received Policy Request Message = " + counter++);
		final String outcome;

		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			String policyRequest = null;
			try {
				policyRequest = textMessage.getText();

				logger.info("Sending  poicy request: " + policyRequest);
				String response = poEnforcement.policyRequest(serviceID,
						policyRequest);

				logger.info("Response received for policy request: " + response);

				String responsURI = resExtraction.getResponseURI(response);

				logger.info("Sucessfully extracted responseMSG URI from policy response: "
						+ responsURI);

				// TODO - how do you enforce the response ... need a callback
				// method??????
				outcome = poEnforcement.policyResponse(serviceID, new URL(
						responsURI));
				logger.info("Successfully received a policy response: "
						+ outcome);

				jmsTemplate.send(responsePolicyQueueName, new MessageCreator() {

					public Message createMessage(Session session)
							throws JMSException {
						TextMessage message = session
								.createTextMessage(outcome);
						// message.setIntProperty("messageCount", i);
						return message;
					}
				});

				logger.info("Successfully sent the response to the requester: "
						+ outcome);

				// if (trPolicyResEvent != null) {
				// trPolicyResEvent.storeNotification(outcome);
				// }

			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("PO*****Received: " + text);
			catch (XPathExpressionException e) {
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
			System.out.println("PO*****Received obj: " + message);
		}
	}

	// TODO - extract the response from the request
	public String getReponseId(String policyRequestResponse) {
		return null;
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
