/**
 * @file 		TraceabilityOntology.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		TraceabilityModel
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.traceabilitystore.sw;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.VCARD;

/**
 * @author Mufy
 * 
 */
public class TraceabilityOntology {

	static String personURI = "http://orange.com/Bob";
	static String fullName = "Bob Smith";

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
