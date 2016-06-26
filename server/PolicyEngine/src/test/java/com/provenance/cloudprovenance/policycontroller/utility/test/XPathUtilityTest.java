/**
 * @file 		XPathUtilityTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		PolicyController
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.policycontroller.utility.test;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;

import com.provenance.cloudprovenance.policyengine.service.XPathProvenanceConditionUtility;

/**
 * @author Mufy
 * 
 */
@Ignore
public class XPathUtilityTest {

	XPathProvenanceConditionUtility xpathUtility;

	static Logger logger = Logger.getLogger(XPathUtilityTest.class);

	@Before
	public void setUp() throws XMLDBException, URISyntaxException {
		ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "beans.xml" });

		xpathUtility = (XPathProvenanceConditionUtility) ctx
				.getBean("xPathProvenanceConditionUtility");
		ctx.close();

		// initilizeStore();
	}

	@Test
	public void matchNodeFromProvenanceStore() throws XPathExpressionException,
			XMLDBException, URISyntaxException, ParserConfigurationException,
			SAXException, IOException {

		logger.info("********************Testing matchNodeFromProvenanceStore **********************************");

		String xpathMatch = "//prov:agent[@prov:id='ex:ag003']";

		// Assert.assertTrue(xpUtility.matchNodeId(xpathMatch));

		Assert.assertTrue(xpathUtility.evaluateIdWithStore(xpathMatch));

	}
	/*
	 * @SuppressWarnings("unused") private void initilizeStore() {
	 * 
	 * final String fileName = "1374225424897.xml";// System.currentTimeMillis()
	 * final String traceabilityType = "ServiceTraceability"; final String
	 * serviceName = "MMC";
	 * 
	 * WebClient client = WebClient
	 * .create("http://localhost:7000/cprov-provenanceStore");
	 * 
	 * client.path(serviceName + "/" + traceabilityType + "/" + fileName + "");
	 * client.type("application/xml").accept("application/xml");
	 * 
	 * Response r = client.put("");
	 * 
	 * client = WebClient
	 * .create("http://localhost:7000/cprov-provenanceStore");
	 * client.path(serviceName + "/" + traceabilityType + "/" + fileName);
	 * client.type("application/xml").accept("application/xml");
	 * 
	 * String XMLdef = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"; XMLdef
	 * +=
	 * ("<cprov:traceabilityDocument xmlns:prov=\"http://www.w3.org/ns/prov#\" \n"
	 * +
	 * " xmlns:cprov=\"http://labs.orange.com/uk/cprov#\" xmlns:cu=\"http://www.w3.org/1999/xhtml/datatypes/\" \n"
	 * +
	 * " xmlns:r=\"http://labs.orange.com/uk/r#\" xmlns:ex=\"http://www.example.org/\" \n"
	 * +
	 * " xmlns:opmx=\"http://openprovenance.org/model/opmx#\" xmlns:cprovd=\"http://labs.orange.com/uk/cprovd#\" \n"
	 * + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
	 * " xsi:schemaLocation=\"http://labs.orange.com/uk/cprov# file:/Users/mufy/Dropbox/EngD/year3/schema/cProv-inherited/cProv-v1.3.xsd\"> \n"
	 * );
	 * 
	 * XMLdef += "<prov:agent prov:id='ex:ag003'/>";
	 * 
	 * XMLdef += "<cprov:cResource prov:id=\"ex:e001\">\n" +
	 * "<cprov:type>data</cprov:type>\n" +
	 * "<cprov:userTrustDegree>1</cprov:userTrustDegree>" +
	 * "<cprov:userCloudRef>http://orangecloud/user@matt/cluster001/image001</cprov:userCloudRef>"
	 * + "<cprov:vResourceRef>/linux/meetingService/res001</cprov:vResourceRef>"
	 * +
	 * "<cprov:pResourceRef>/Cluster001/126.23.43.45/server001/00-12-00-12-00-1/e001</cprov:pResourceRef>"
	 * + "<cprov:TTL>2015-10-10T12:00:00-05:00</cprov:TTL>" +
	 * "</cprov:cResource>";
	 * 
	 * XMLdef += "<prov:agent prov:id='ex:003'/>";
	 * 
	 * XMLdef += ("</cprov:traceabilityDocument> \n");
	 * 
	 * logger.info("Store entry creation URI :" + client.getCurrentURI());
	 * 
	 * Response r2 = client.put(XMLdef);
	 * 
	 * logger.info("Status : " + r2.getStatus()); }
	 */

}
