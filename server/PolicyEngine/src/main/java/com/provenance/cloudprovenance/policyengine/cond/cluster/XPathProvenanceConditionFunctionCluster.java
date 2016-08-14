/*
 * @(#) XPathProvenanceConditionFunctionCluster.java       1.1 13/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.policyengine.cond.cluster;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.wso2.balana.cond.Function;
import org.wso2.balana.cond.cluster.FunctionCluster;

import com.provenance.cloudprovenance.policyengine.cond.XPathProvenanceConditionFunction;

/**
 * Grouping of XPath condition functions
 * 
 * @version 1.1 13 Aug 2016
 * @author Mufy
 * @Module PolicyEngine
 */
public class XPathProvenanceConditionFunctionCluster implements FunctionCluster {

	@SuppressWarnings("rawtypes")
	public Set<Function> getSupportedFunctions() {
		Set<Function> set = new HashSet<Function>();
		Iterator it = XPathProvenanceConditionFunction
				.getSupportedIdentifiers().iterator();
		while (it.hasNext())
			set.add(new XPathProvenanceConditionFunction((String) (it.next())));

		return set;
	}
}