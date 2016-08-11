TODO - Find a proper solution to the issue of dynamic generation of the Java Objects from the XML schema with 
some missing namespaces. 

The following is a workaround, which requires inserting it manually.

String prefix = "confidenshare";
		String suffix = "http://labs.orange.com/uk/confidenShare#";


@javax.xml.bind.annotation.XmlSchema(namespace = "http://www.w3.org/ns/prov#",

xmlns = {   
	    @XmlNs(namespaceURI = "http://labs.orange.com/uk/ex#", prefix = "ex"),
	   @XmlNs(namespaceURI = "http://labs.orange.com/uk/cprovd#", prefix = "cprovd"),
	    @XmlNs(namespaceURI = "http://labs.orange.com/uk/cprov#", prefix = "cprov"),
	    @XmlNs(namespaceURI = "http://labs.orange.com/uk/confidenshare#", prefix = "confidenshare"),
	     
	},
 elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)
package com.provenance.cloudprovenance.traceabilityModel.generated;

import javax.xml.bind.annotation.XmlNs;



