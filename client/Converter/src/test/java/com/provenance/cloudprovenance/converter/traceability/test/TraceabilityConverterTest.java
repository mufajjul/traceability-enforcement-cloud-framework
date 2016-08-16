/*
 * @(#) TraceabilityConverterTest.java       1.1 16/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.converter.traceability.test;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.provenance.cloudprovenance.converter.resource.TraceabilityConverter;
import com.provenance.cloudprovenance.traceabilityModel.generated.Agent;
import com.provenance.cloudprovenance.traceabilityModel.generated.InternationalizedString;
import com.provenance.cloudprovenance.traceabilityModel.generated.ObjectFactory;
import com.provenance.cloudprovenance.traceabilityModel.generated.TraceabilityDocument;

/**
 * This is a test class for traceability converter
 *
 * @version 1.1 16 Aug 2016
 * @author Mufy
 * @Module Converter
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans.xml")
public class TraceabilityConverterTest {

	Logger logger = Logger.getLogger(TraceabilityConverterTest.class);

	@Autowired
	private TraceabilityConverter trConverter;

	@Test
	public void convertTraceabilityElementToXMLTest() {

		// gen temp data
		ObjectFactory tModelFactory = new ObjectFactory();
		TraceabilityDocument traceabilityDoc = tModelFactory
				.createTraceabilityDocument();

		Agent agent = tModelFactory.createAgent();

		QName agentId = new QName("www.temp.org", "ag001", "ex");
		agent.setId(agentId);

		InternationalizedString iStringName = new InternationalizedString();
		iStringName.setValue("Bob");

		agent.getLocationOrLabelOrType().add(
				tModelFactory.createLabel(iStringName));

		InternationalizedString iStringDescription = new InternationalizedString();
		iStringDescription.setValue("A test user");

		traceabilityDoc.getEntityOrActivityOrWasGeneratedBy().add(agent);

		String conversion = trConverter.marhsallObject(traceabilityDoc);
		logger.info(conversion);

		Assert.notNull(conversion);
	}

}
