/*
 * @(#) XPathUtilityTest.java       1.1 14/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
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
 * This is a test class for XPath Utility 
 * 
 * @version 1.1 14 Aug 2016
 * @author Mufy
 * @Module PolicyEngine
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
		Assert.assertTrue(xpathUtility.evaluateIdWithStore(xpathMatch));

	}	
}
