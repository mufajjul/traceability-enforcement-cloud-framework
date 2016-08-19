/*
 * @(#) CProvRecordTen.java       1.1 19/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.requirement_ten.test;

import com.provenance.cloudprovenance.service.api.impl.CprovXmlProvenance;

/**
 * Provenance graph to validate against the following policy: TODO
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
 */
public class CProvRecordTen {

	private CprovXmlProvenance cProvApi;

	public CProvRecordTen(CprovXmlProvenance cProvApi) {

		this.cProvApi = cProvApi;
	}

	public void genPreStatements() {

		// agent
		String agent1Id = cProvApi.Agent("ag-system", "system", "system");

		// registration create
		String cprocess1Id = cProvApi.cProcess("a-replicate",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-replicat",
				"192.168.1.34", null);

		String wicb1Id = cProvApi.wasInitiallyCalledBy(null, cprocess1Id,
				agent1Id, "", "system", "ethernet");

		String cres1Id = cProvApi.cResource("e-document1", "",
				"=bob[=]!:uuid:f81d4fae", "[=]!:pid:g81d4fde-document",
				"192.168.1.34", true, null, null, "general", 1.0f, null);

		String used1Id = cProvApi.used(null, cprocess1Id, cres1Id, null);

	}

	public void genPostStatements() {

	}
}
