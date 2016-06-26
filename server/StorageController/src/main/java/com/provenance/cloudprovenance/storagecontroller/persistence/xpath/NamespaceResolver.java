/**
 * @file 		NamespaceResolver.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		StorageController
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.storagecontroller.persistence.xpath;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Document;

/**
 * This class returns suffix of a namespace
 * 
 * @author Mufy
 * 
 */
public class NamespaceResolver implements NamespaceContext {

	private Document sourceDocument;

	public NamespaceResolver(Document document) {
		sourceDocument = document;
	}

	public NamespaceResolver() {

	}

	/**
	 * The lookup for the namespace uris is delegated to the stored document.
	 * 
	 * @param prefix
	 *            to search for
	 * @return uri
	 */
	public String getNamespaceURI(String prefix) {
		if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
			return "http://labs.orange.com/uk/cprov#";
		} else if (prefix.equals("cprov")) {
			return "http://labs.orange.com/uk/cprov#";

		} else if (prefix.equals("prov")) {
			return "http://www.w3.org/ns/prov#";
		}
		
		else if (prefix.equals("confidenshare")) {
			return "http://labs.orange.com/uk/confidenshare#";
		}

		else if (prefix.equals("cprovl")) {
			return "http://labs.orange.com/uk/cprovl#";
		} else if (prefix.equals("cprovd")) {

			return "http://labs.orange.com/uk/cprovd#";
		} else if (prefix.equals("r")) {
			return "http://labs.orange.com/uk/r#";

		} else if (prefix.equals("ex")) {
			return "http://labs.orange.com/uk/ex#";
		} else if (prefix.equals("cu")) {
			return "http://www.w3.org/1999/xhtml/datatypes/";
		} else {
			return sourceDocument.lookupNamespaceURI(prefix);
		}
	}

	public String getPrefix(String namespaceURI) {

		String prefix = sourceDocument.lookupPrefix(namespaceURI);

		return prefix;
	}

	@SuppressWarnings("rawtypes")
	public Iterator getPrefixes(String namespaceURI) {

		// TODO - To be implemented

		return null;
	}
}
