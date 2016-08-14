/*
 * @(#) TraceabilityOntology.java       1.1 14/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.traceabilitystore.sw;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.VCARD;

/**
 * This class is designed to present a traceability Ontology
 * TODO - needs to be completed 
 * 
 * @version 1.1 14 Aug 2016
 * @author Mufy
 * @Module TraceabilityModel
 */
public class TraceabilityOntology {

	static String personURI = "http://orange.com/Bob";
	static String fullName = "Bob Smith";

	//TODO - update the create model
	public static void createModel() {

		Model model = ModelFactory.createDefaultModel();

		Resource johnSmith = model.createResource(personURI);
		johnSmith.addProperty(VCARD.FN, fullName);

		model.write(System.out);

	}

	public static void main(String args[]) {

		TraceabilityOntology.createModel();

	}

}
