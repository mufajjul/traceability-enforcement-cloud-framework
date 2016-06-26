/**
 * @file 		TraceabilityStoreConnectorTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		Connector
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.connector.traceability;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.xml.sax.SAXException;

import com.provenance.cloudprovenance.connector.TempJMSBroker;
import com.provenance.cloudprovenance.connector.traceability.response.ResponseExtraction;

/**
 * @author Mufy
 * 
 */
//@Ignore
@ContextConfiguration("classpath:beans.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TraceabilityStoreConnectorTest {

	@Autowired
	TraceabilityStoreConnector trConnector;
	@Autowired
	ResponseExtraction re;
	private static Logger logger = Logger
			.getLogger("TraceabilityStoreConnectorTest");

	String serviceId = "ConfidenShare";
	String feedbackResponseURI = null;

	
	
	@BeforeClass 
	public static void initilizeServer() throws Exception{
		
		TempJMSBroker.JMSBrokerStart();
	}
	
	@AfterClass 
	public static void stopServer() throws Exception{
		
		TempJMSBroker.JMSBrokerStop();
		
	}
	
	
	@Ignore
	@Test
	public void getTraceabilityRecordWithoutCreatedId() {
		String feedBack = trConnector.getCurrentTraceabilityRecordId(serviceId);
		logger.info("getTraceabilityRecordId; response: " + feedBack);
		Assert.isNull(feedBack);
	}

	//@Ignore
	@Test
	public void createTraceabilityRecordTest() throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {

		String feedBack = trConnector.createNewTraceabilityRecord(serviceId,
				templateData());
		logger.info("createTraceabilityRecord:  response: \n" + feedBack);

		Assert.notNull(feedBack);

		feedbackResponseURI = re.getResponseURI(feedBack);//.split(":")[1];
		logger.info("Id is: " + feedbackResponseURI);

		Assert.notNull(feedbackResponseURI);
		updateTraceabilityRecordTest();
	}

	@Ignore
	@Test
	public void updateTraceabilityRecordTest() {

		// TODO - how do I get the ID?????
		// String traceabilityExistingEntryId = null;

		// String trRecord = "<TraceabilityDocument> </TraceabilityDocument>";
		int feedBack = trConnector.updateTraceabilityRecord(serviceId,
				feedbackResponseURI, templateData());
		logger.info("createTraceabilityRecord:  response: " + feedBack);

		Assert.notNull(feedBack);

		// getTraceabilityRecordId();
	}

	@Ignore
	@Test
	public void getTraceabilityRecordWithoutValidId() {

		String traceabilityEntryId = "document001";
		String feedBack = trConnector.getTraceabilityRecord(serviceId,
				traceabilityEntryId);
		logger.info("getTraceabilityRecordId; response: " + feedBack);

		Assert.isNull(feedBack);
	}
		

	@Ignore
	@Test
	public void getTraceabilityRecordWitValidId() {

		String traceabilityEntryId = "document001";
		String feedBack = trConnector.getTraceabilityRecord(serviceId,
				traceabilityEntryId);
		logger.info("getTraceabilityRecordId; response: " + feedBack);

		Assert.notNull(feedBack);
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
