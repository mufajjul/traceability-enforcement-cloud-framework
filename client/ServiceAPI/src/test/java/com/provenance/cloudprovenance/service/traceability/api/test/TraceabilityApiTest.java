/*
 * @(#) TraceabilityApiTest.java       1.1 19/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.traceability.api.test;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.provenance.cloudprovenance.service.api.impl.CprovXmlProvenance;

/**
 * A test provenance graph
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:beans.xml")
public class TraceabilityApiTest {

	@Autowired
	private CprovXmlProvenance cProvApi;

	@Test
	public void generateStatements() {

		String agent1Id = cProvApi.Agent("bob", "bob", "smith");

		// registration process
		String cprocess1Id = cProvApi.cProcess("a-createUser",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-createus",
				"pid:00:12:00:12:100:11:00:00", null);

		String wicb1Id = cProvApi.wasInitiallyCalledBy(null, cprocess1Id,
				agent1Id, "", "Laptop", "wifi");

		// username and password
		String cres1Id = cProvApi.cResource("email", "",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000email",
				"pid:00:12:00:12:100:11:00:00", true, null, null, "general",
				1.0f, null);

		String agent2Id = cProvApi.Agent("bob", "bob", "smith");

		// registration process
		String cprocess2Id = cProvApi.cProcess("a-createUser",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-createus",
				"pid:00:12:00:12:100:11:00:00", null);

		String wicb2Id = cProvApi.wasInitiallyCalledBy(null, cprocess1Id,
				agent1Id, "", "Laptop", "wifi");

		// username and password
		String cres2Id = cProvApi.cResource("email", "",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-000email",
				"pid:00:12:00:12:100:11:00:00", true, null, null, "general",
				1.0f, null);
	}

}
