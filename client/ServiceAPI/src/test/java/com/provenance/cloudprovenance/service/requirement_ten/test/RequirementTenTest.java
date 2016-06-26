/**
 * @file 		RequirementTenTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.requirement_ten.test;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import com.provenance.cloudprovenance.service.api.impl.CprovXmlProvenance;
import com.provenance.cloudprovenance.service.api.impl.DynamicPolicyRequest;

/**
 * @author Mufy
 * 
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans.xml")
public class RequirementTenTest {

	@Autowired
	private CprovXmlProvenance cProvApi;

	@Autowired
	DynamicPolicyRequest srvCompliance;

	@Before
	public void setUp() throws URISyntaxException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		CProvRecordTen genProvRecord = new CProvRecordTen(cProvApi);
		genProvRecord.genPostStatements();

	}

	@Test
	public void shareAsOwnershipOfaFileDuplicatesTest()
			throws URISyntaxException, ParserConfigurationException,
			SAXException, IOException, TransformerException {

		String userAgent = "bob";
		String resource = "document1";
		String process = "share";
		String environment1 = "reg.share.confidenshare.labs.orange.com";

		srvCompliance.constructRequest(userAgent, resource, process,
				environment1);
	}

}
