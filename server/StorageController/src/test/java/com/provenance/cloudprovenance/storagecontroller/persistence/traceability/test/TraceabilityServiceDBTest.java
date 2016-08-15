/*
 * @(#) TraceabilityServiceDBTest.java       1.1 16/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.storagecontroller.persistence.traceability.test;

import java.io.File;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xmldb.api.base.XMLDBException;

import com.provenance.cloudprovenance.storagecontroller.presistence.traceabilitystore.service.XmlDbServiceTraceability;

/**
 * This class tests various XML DB operations (insert, delete, search etc)
 * 
 * @version 1.1 16 Aug 2016
 * @author Mufy
 * @Module StorageController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans.xml")
public class TraceabilityServiceDBTest {

	@Autowired
	private XmlDbServiceTraceability service;

	private static Logger logger = Logger.getLogger("DBTest");
	private static String traceabilityFileId = "serviceTraceabilityTest.xml";

	@Before
	public void setUp() {

		String serviceId = "ConfidenShare";
		String traceabilityType = "ServiceTraceability";

		service.createTraceabilityInstance(serviceId, traceabilityType,
				traceabilityFileId);
		addResourceTest();
	}

	// @Ignore
	// @Test
	public void addResourceTest() {

		logger.info("********************************** Create/add resource ********************");

		String serviceId = "ConfidenShare";
		String traceabilityType = "ServiceTraceability";

		logger.info(templateData());
		boolean response = service.createTraceabilityEntry(serviceId,
				traceabilityType, traceabilityFileId, "", templateData());

		Assert.assertTrue(response);
	}

	// @Ignore
	@Test
	public void updateResourceTest() {

		String serviceId = "ConfidenShare";
		String traceabilityType = "ServiceTraceability";
		String traceabilityFileId = "serviceTraceabilityTest.xml";
		String resourceType = "XMLResource";

		logger.info("********************************** Update Resource via xQuery ********************");

		String entryItem = " <prov:agent prov:id=\"ex:ag001224\">"
				+ "<prov:label>phil</prov:label>" + "</prov:agent> ";

		// entryItem+= "\n<prov:agent prov:id=\"ex:ag001224\">"
		// + "<prov:label>phil</prov:label>" + "</prov:agent> ";

		entryItem = this.templateData();

		logger.info(entryItem);

		// service.updateTraceabilityResource(serviceId, traceabilityType,
		// traceabilityFileId, resourceType, entryItem);

		boolean response = service.updateTraceabilityEntry(serviceId,
				traceabilityType, traceabilityFileId, "", entryItem);
		Assert.assertTrue(response);
	}

	// @After
	public void removeResource() {

		String serviceId = "ConfidenShare";
		String traceabilityType = "ServiceTraceability";
		String traceabilityFileId = "serviceTraceabilityTest.xml";

		boolean status = service.removeTraceabiltiltyInstance(serviceId,
				traceabilityType, traceabilityFileId);

		Assert.assertTrue(status);
	}

	@Ignore
	@Test
	public void getAllResources() throws XMLDBException, URISyntaxException {

		String serviceId = "ConfidenShare";
		String traceabilityType = "ServiceTraceability";

		String[] res = service.getAllTraceabilityInstances(serviceId,
				traceabilityType);

		for (int i = 0; i < res.length; i++) {
			Assert.assertNotNull(res[i]);
		}
	}

	@Ignore
	@Test
	public void getAresource() {

		String serviceId = "ConfidenShare";
		String traceabilityType = "ServiceTraceability";
		String traceabilityFileId = "serviceTraceabilityTest.xml";

		logger.info("*********************************************** Get a resource **************************");

		String dbResource = service.getTraceabilityInstance(serviceId,
				traceabilityType, traceabilityFileId);
		Assert.assertNotNull(dbResource);

	}

	@Test
	public void getFileContentSize() {

		String serviceId = "ConfidenShare";
		String traceabilityType = "ServiceTraceability";
		String traceabilityFileId = "serviceTraceabilityTest.xml";

		logger.info("*********************************************** Get a resource size **************************");

		int dbResourceSize = service.currentTraceabilityRecordSize(serviceId,
				traceabilityType, traceabilityFileId);
		logger.info("resource size: " + dbResourceSize);

		Assert.assertNotNull(dbResourceSize);

	}

	// @AfterClass
	public static void removeTempFile() {

		File f = new File(traceabilityFileId);
		if (f.exists()) {
			f.delete();

		}

		// TODO remove the file
		// removeResource();
	}

	private String templateData() {

		String XMLdef = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n";

		XMLdef += "<cprov:traceabilityDocument xmlns:prov=\"http://www.w3.org/ns/prov#\" "
				+ "xmlns:cprov=\"http://labs.orange.com/uk/cprov#\" xmlns:cu=\"http://www.w3.org/1999/xhtml/datatypes/\" "
				+ "xmlns:r=\"http://labs.orange.com/uk/r#\" xmlns:ex=\"http://www.example.org/\" "
				+ "xmlns:cprovd=\"http://labs.orange.com/uk/cprovd#\" "
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "xsi:schemaLocation=\"http://labs.orange.com/uk/cprov# file:/Users/mufy/Dropbox/EngD/year3/schema/cProv-inherited/cProv-v1.3.xsd\">\n"
				+ "<prov:agent prov:id='ex:ag003'/>\n"
				+ "<cprov:cResource prov:id='ex:e001'>"
				+ "<cprov:resType>cprovd:data</cprov:resType>"
				+ "<cprov:userTrustDegree>1</cprov:userTrustDegree>"
				+ "<cprov:userCloudRef>http://orangecloud/user@matt/cluster001/image001</cprov:userCloudRef>"
				+ "<cprov:vResourceRef>/linux/meetingService/res001</cprov:vResourceRef>"
				+ "<cprov:pResourceRef>/Cluster001/126.23.43.45/server001/00-12-00-12-00-1/e001</cprov:pResourceRef>"
				+ "<cprov:TTL>2015-10-10T12:00:00-05:00</cprov:TTL>"
				+ "</cprov:cResource>\n" + "</cprov:traceabilityDocument>";

		return XMLdef;
	}

}
