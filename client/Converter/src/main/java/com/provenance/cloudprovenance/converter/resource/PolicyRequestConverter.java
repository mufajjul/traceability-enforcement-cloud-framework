/*
 * @(#) PolicyRequestConverter.java       1.1 16/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.converter.resource;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.provenance.cloudprovenance.converter.ResourceConverter;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.ObjectFactory;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.PolicyRequest;
//import com.provenance.cloudprovenance.traceabilitystore.ns.CprovNamespacePrefixMapper;
import com.provenance.cloudprovenance.traceabilitystore.ns.CprovNamespacePrefixMapper;

/**
 * This class provides implementation for policy resource converter, based on the interface Resource Converter 
 *
 * @version 1.1 16 Aug 2016
 * @author Mufy
 * @Module Converter
 */
public class PolicyRequestConverter implements ResourceConverter<PolicyRequest>{

	String instanceDir;
	CprovNamespacePrefixMapper cProvPrefixMapper;
	ObjectFactory pFactory = new ObjectFactory();

	private static Logger logger = Logger.getLogger("PolicyRequestConverter");

	public String marhsallObject(PolicyRequest pRequest) {

		JAXBContext jaxbContext;
		StringWriter sw = new StringWriter();

		try {
			jaxbContext = JAXBContext.newInstance(instanceDir);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
					cProvPrefixMapper);

			JAXBElement<PolicyRequest> el = pFactory
					.createPolicyRequest(pRequest);

			marshaller.marshal(el, sw);

			logger.info("Sucessfully marshalled the policy objects: " + el.getName());

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return sw.toString();
	}

	public String getInstanceDir() {
		return instanceDir;
	}

	public void setInstanceDir(String instanceDir) {
		this.instanceDir = instanceDir;
	}

	public CprovNamespacePrefixMapper getcProvPrefixMapper() {
		return cProvPrefixMapper;
	}

	public void setcProvPrefixMapper(CprovNamespacePrefixMapper cProvPrefixMapper) {
		this.cProvPrefixMapper = cProvPrefixMapper;
	}

}
