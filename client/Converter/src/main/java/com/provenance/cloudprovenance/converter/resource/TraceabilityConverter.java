/**
 * @file 		TraceabilityConverter.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		Converter
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.converter.resource;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.provenance.cloudprovenance.converter.ResourceConverter;
import com.provenance.cloudprovenance.traceabilityModel.generated.ObjectFactory;
import com.provenance.cloudprovenance.traceabilityModel.generated.TraceabilityDocument;
import com.provenance.cloudprovenance.traceabilitystore.ns.CprovNamespacePrefixMapper;
//import com.provenance.cloudprovenance.traceabilitystore.ns.CprovNamespacePrefixMapper;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * @author Mufy
 * 
 */
public class TraceabilityConverter implements
		ResourceConverter<TraceabilityDocument> {

	CprovNamespacePrefixMapper cProvPrefixMapper;

	private String instanceDir;

	private ObjectFactory tModelFactory = new ObjectFactory();
	private static Logger logger = Logger.getLogger("DynamicPolicyRequest");

	public synchronized String marhsallObject(TraceabilityDocument rootElement) {

		JAXBContext jaxbContext;
		StringWriter sw = new StringWriter();

		try {

			jaxbContext = JAXBContext.newInstance(instanceDir);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

			// NamespacePrefixMapper prefixMapper = new
			// CprovNamespacePrefixMapper();
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
					cProvPrefixMapper);

			JAXBElement<TraceabilityDocument> el = tModelFactory
					.createTraceabilityDocument((TraceabilityDocument) rootElement);

			marshaller.marshal(el, sw);
			logger.info("Sucessfully marshalled Objects: " + el.getName());

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

}
