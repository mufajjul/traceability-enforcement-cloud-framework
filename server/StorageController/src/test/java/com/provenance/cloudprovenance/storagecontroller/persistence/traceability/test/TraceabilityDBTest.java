/**
 * @file 		TraceabilityPolicyTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		StorageController
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.storagecontroller.persistence.traceability.test;

/**
 * @file 		DBTest.java
 * @project 	CloudProvenance
 * @Module		ProvenanceStore
 * @date 		18 05 2013
 * @version 	1.0
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

import com.provenance.cloudprovenance.storagecontroller.presistence.xmldb.XmlDbService;

/**
 * Test XML DB operations (insert, delete, search etc)
 * 
 * @author Mufy
 * 
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans.xml")
public class TraceabilityDBTest {

	// @Autowired
	// private XmlDbConnector conn;
	@Autowired
	private XmlDbService service;

	private static Logger logger = Logger.getLogger("DBTest");
	private static String traceabilityFileId = "serviceTraceabilityTest.xml";

	@Before
	public void setUp() {
		addResourceTest();
	}

	// @Ignore
	// @Test
	public void addResourceTest() {

		logger.info("********************************** Create/add resource ********************");

		String serviceId = "ConfidenShare";
		String traceabilityType = "ServiceTraceability";
		String resourceType = "XMLResource";

		File traceabilityFile = new File(traceabilityFileId);

		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(traceabilityFile));

			writer.write(templateData());
			writer.close();

			service.addTraceabilityResource(serviceId, traceabilityType,
					traceabilityFileId, traceabilityFile, resourceType);

		} catch (IOException e) {
			Assert.fail(e.toString());
			e.printStackTrace();
		} catch (XMLDBException e) {
			Assert.fail(e.toString());
			e.printStackTrace();
		} catch (URISyntaxException e) {
			Assert.fail(e.toString());
			e.printStackTrace();
		}
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

		entryItem = this.templateData();
		
		logger.info(entryItem);

		try {

			service.updateTraceabilityResource(serviceId, traceabilityType,
					traceabilityFileId, resourceType, entryItem);

		} catch (XMLDBException e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}
	
	
	@Test 
	public void getFileSize(){
		
		String serviceId = "ConfidenShare";
		String traceabilityType = "ServiceTraceability";
		String traceabilityFileId = "serviceTraceabilityTest.xml";
		String resourceType = "XMLResource";

		logger.info("********************************** Get  Resource File sizes ********************");
		
		
		try {

			int fileSize = service.getResourceSize(serviceId, traceabilityType, traceabilityFileId);
			
			logger.info("file size: "+fileSize);

		} catch (XMLDBException e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.toString());
		}
		
	}

	// @Ignore
	@Test
	public void updateResourceScalabilityTest() {

		String serviceId = "ConfidenShare";
		String traceabilityType = "ServiceTraceability";
		String traceabilityFileId = "serviceTraceabilityTest.xml";
		String resourceType = "XMLResource";

		logger.info("********************************** Update Resource via xQuery ********************");

		// String entryItem = " <prov:agent prov:id=\"ex:ag001224\">"
		// + "<prov:label>phil</prov:label>" + "</prov:agent> ";

		String entryItem = " <cprov:cResource prov:id=\"ex:e001\">"
				+ "<cprov:type>data</cprov:type>"
				+ " <cprov:userTrustDegree>1</cprov:userTrustDegree>"
				+ "<cprov:userCloudRef>http://orangecloud/user@matt/cluster001/image001</cprov:userCloudRef>"
				+ "<cprov:vResourceRef>/linux/meetingService/res001</cprov:vResourceRef>"
				+ "<cprov:pResourceRef>/Cluster001/126.23.43.45/server001/00-12-00-12-00-1/e001</cprov:pResourceRef>"
				+ "<cprov:TTL>2015-10-10T12:00:00-05:00</cprov:TTL>"
				+ " </cprov:cResource>";

		logger.info(entryItem);

		// XmlDbStore dbStore = new XmlDbStore();

		long total = 0;
		try {
			for (int i = 0; i < 10; i++) {
				long startTime = System.currentTimeMillis();

				service.updateTraceabilityResource(serviceId, traceabilityType,
						traceabilityFileId, resourceType, entryItem);

				long endTime = System.currentTimeMillis();

				total += endTime - startTime;

				logger.info("Insertition Time: " + i + " : "
						+ (endTime - startTime));

			}

			logger.info("Total time : " + total);
		} catch (XMLDBException e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	//@After
	public void removeResource() {

		String serviceId = "ConfidenShare";
		String traceabilityType = "ServiceTraceability";
		String traceabilityFileId = "serviceTraceabilityTest.xml";

		try {
			service.deleteTraceabilityResource(serviceId, traceabilityType,
					traceabilityFileId);
		} catch (XMLDBException e) {
			Assert.fail(e.toString());
			e.printStackTrace();
		} catch (URISyntaxException e) {
			Assert.fail(e.toString());
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void getAllResources() throws XMLDBException, URISyntaxException {

		String serviceId = "ConfidenShare";
		String traceabilityType = "ServiceTraceability";

		Resource[] res = service.getAlltraceabilityResources(serviceId,
				traceabilityType);

		for (int i = 0; i < res.length; i++) {
			Assert.assertNotNull(res[i].getContent());
			logger.info("File Content ==> " + res[i].getContent());
		}
	}

	//@Ignore
	@Test
	public void getAresource() {

		String serviceId = "ConfidenShare";
		String traceabilityType = "ServiceTraceability";
		String traceabilityFileId = "serviceTraceabilityTest.xml";

		logger.info("*********************************************** Get a resource **************************");

		try {
			Resource dbResource = service.getTraceabilityResource(serviceId,
					traceabilityType, traceabilityFileId);

			logger.info(dbResource.getContent());
			Assert.assertNotNull(dbResource);

		} catch (XMLDBException e) {

			e.printStackTrace();
		} catch (URISyntaxException e) {

			e.printStackTrace();
		}

	}

	
	
	@AfterClass
	public static void removeTempFile() {

		File f = new File(traceabilityFileId);
		if (f.exists()) {
			f.delete();

		}

		//TODO remove the file 
		
		//removeResource();
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
