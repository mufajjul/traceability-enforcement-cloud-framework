/**
 * @file 		TraceabilityConverterTest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		Converter
 * @date 		18 05 2013
 * @version 	1.0
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
 * @author Mufy
 * 
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

		// getRootNode().getInstance().add(agent);

		traceabilityDoc.getEntityOrActivityOrWasGeneratedBy().add(agent);

		String conversion = trConverter.marhsallObject(traceabilityDoc);
		logger.info(conversion);

		Assert.notNull(conversion);

	}

}
