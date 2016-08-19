/*
 * @(#) PolicyResponseConverter.java       1.1 18/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.sconverter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

import com.provenance.cloudprovenance.traceabilityLanguage.generated.ObjectFactory;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.PolicyResponse;
import com.provenance.cloudprovenance.traceabilitystore.ns.CprovNamespacePrefixMapper;

/**
 * This class converts an XACML response into a cProl response.
 * 
 * @version 1.1 18 Aug 2016
 * @author Mufy
 * @Module SConverter
 */
public class PolicyResponseConverter {

	CprovNamespacePrefixMapper cProvPrefixMapper;
	Resource pathToXACMLtoCprovResponse;

	String instanceDir;
	ObjectFactory pFactory = new ObjectFactory();

	private static Logger logger = Logger.getLogger("PolicyResponseConverter");

	public String marhsallObject(PolicyResponse pResponse) {

		JAXBContext jaxbContext;
		StringWriter sw = new StringWriter();

		try {
			jaxbContext = JAXBContext.newInstance(instanceDir);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
					cProvPrefixMapper);

			JAXBElement<PolicyResponse> el = pFactory
					.createPolicyResponse(pResponse);

			marshaller.marshal(el, sw);

			logger.info("Sucessfully marshalled the policy objects: "
					+ el.getName());

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

	public void setcProvPrefixMapper(
			CprovNamespacePrefixMapper cProvPrefixMapper) {
		this.cProvPrefixMapper = cProvPrefixMapper;
	}

	public String XMLpolicyResponse(String XACMLResponse) {

		StringReader reader;
		StringWriter writer;
		InputStream fsStylesheetIs;
		TransformerFactory tFactory;
		StreamSource stylesource;
		Transformer transformer;
		String result;

		logger.info("XACML response to be converted: " + XACMLResponse);

		reader = new StringReader(XACMLResponse);
		writer = new StringWriter();

		try {

			fsStylesheetIs = pathToXACMLtoCprovResponse.getInputStream();

			System.setProperty("javax.xml.transform.TransformerFactory",
					"net.sf.saxon.TransformerFactoryImpl");

			// Use a Transformer for output
			tFactory = TransformerFactory.newInstance();
			stylesource = new StreamSource(fsStylesheetIs);
			transformer = tFactory.newTransformer(stylesource);

			transformer.transform(new javax.xml.transform.stream.StreamSource(
					reader),
					new javax.xml.transform.stream.StreamResult(writer));
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		result = writer.toString();

		return result;
	}

	public Resource getPathToXACMLtoCprovResponse() {
		return pathToXACMLtoCprovResponse;
	}

	public void setPathToXACMLtoCprovResponse(
			Resource pathToXACMLtoCprovResponse) {
		this.pathToXACMLtoCprovResponse = pathToXACMLtoCprovResponse;
	}
}
