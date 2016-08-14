/*
 * @(#) CprovNamespacePrefixMapper.java       1.1 14/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.traceabilitystore.ns;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * This class handles the prefix namespace issue
 * 
 * @version 1.1 14 Aug 2016
 * @author Mufy
 * @Module TraceabilityModel
 */
public class CprovNamespacePrefixMapper extends NamespacePrefixMapper implements
		NamespaceContext {

	private String nsSuffixCprov;
	private String nsSuffixProv;
	private String nsSuffixCprovl;
	private String nsSuffixCProvd;
	private String nsSuffixR;
	private String nsSuffixEx;
	private String nsSuffixCu;
	private String nsSuffixCOnfidenshare;

	@Override
	public String getPreferredPrefix(String uri, String arg1, boolean arg2) {

		// arg1 and arg2 are not used

		if (uri.equals(nsSuffixCprov)) {
			return "cprov";
		} else if (uri.equals(nsSuffixProv)) {
			return "prov";
		} else if (uri.equals(nsSuffixCprovl)) {
			return "cprovl";
		} else if (uri.equals(nsSuffixCProvd)) {
			return "cprovd";
		} else if (uri.equals(nsSuffixR)) {
			return "r";
		} else if (uri.equals(nsSuffixEx)) {
			return "ex";
		} else if (uri.equals(nsSuffixCu)) {
			return "cu";
		} else if (uri.equals(nsSuffixCOnfidenshare)) {
			return "confidenshare";
		} else {
			return null;
		}
	}

	@Override
	public String getNamespaceURI(String prefix) {

		if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
			return nsSuffixCprov;
		} else if (prefix.equals("cprov")) {
			return nsSuffixCprov;
		} else if (prefix.equals("prov")) {
			return nsSuffixProv;
		} else if (prefix.equals("cprovl")) {
			return nsSuffixCprovl;
		} else if (prefix.equals("cprovd")) {
			return nsSuffixCProvd;
		} else if (prefix.equals("r")) {
			return nsSuffixR;
		} else if (prefix.equals("ex")) {
			return nsSuffixEx;
		} else if (prefix.equals("cu")) {
			return nsSuffixCu;
		} else if (prefix.equals("confidenshare")) {
			return nsSuffixCOnfidenshare;
		} else {
			return null;
		}
	}

	@Override
	public String getPrefix(String suffix) {
		if (suffix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
			return "cprov";
		} else if (suffix.equals(nsSuffixCprov)) {
			return "cprov";
		} else if (suffix.equals(nsSuffixProv)) {
			return "prov";
		} else if (suffix.equals(nsSuffixCprovl)) {
			return "cprovl";
		} else if (suffix.equals(nsSuffixCProvd)) {
			return "cprovd";
		} else if (suffix.equals(nsSuffixR)) {
			return "r";
		} else if (suffix.equals(nsSuffixEx)) {
			return "ex";
		} else if (suffix.equals(nsSuffixCu)) {
			return "cu";
		} else if (suffix.equals(nsSuffixCOnfidenshare)) {
			return "confidenshare";
		} else {
			return null;
		}
	}

	/*
	 * TODO - To be compelted
	 * 
	 * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
	 */
	@Override
	public Iterator getPrefixes(String namespaceURI) {
		return null;
	}

	public String getNsSuffixCprov() {
		return nsSuffixCprov;
	}

	public void setNsSuffixCprov(String nsSuffixCprov) {
		this.nsSuffixCprov = nsSuffixCprov;
	}

	public String getNsSuffixProv() {
		return nsSuffixProv;
	}

	public void setNsSuffixProv(String nsSuffixProv) {
		this.nsSuffixProv = nsSuffixProv;
	}

	public String getNsSuffixCprovl() {
		return nsSuffixCprovl;
	}

	public void setNsSuffixCprovl(String nsSuffixCprovl) {
		this.nsSuffixCprovl = nsSuffixCprovl;
	}

	public String getNsSuffixCProvd() {
		return nsSuffixCProvd;
	}

	public void setNsSuffixCProvd(String nsSuffixCProvd) {
		this.nsSuffixCProvd = nsSuffixCProvd;
	}

	public String getNsSuffixR() {
		return nsSuffixR;
	}

	public void setNsSuffixR(String nsSuffixR) {
		this.nsSuffixR = nsSuffixR;
	}

	public String getNsSuffixEx() {
		return nsSuffixEx;
	}

	public void setNsSuffixEx(String nsSuffixEx) {
		this.nsSuffixEx = nsSuffixEx;
	}

	public String getNsSuffixCu() {
		return nsSuffixCu;
	}

	public void setNsSuffixCu(String nsSuffixCu) {
		this.nsSuffixCu = nsSuffixCu;
	}

	public String getNsSuffixCOnfidenshare() {
		return nsSuffixCOnfidenshare;
	}

	public void setNsSuffixCOnfidenshare(String nsSuffixCOnfidenshare) {
		this.nsSuffixCOnfidenshare = nsSuffixCOnfidenshare;
	}

}
