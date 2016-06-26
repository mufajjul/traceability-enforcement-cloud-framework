/**
 * @file 		XPathProvenanceConditionFunctionCluster.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		PolicyEngine
 * @date 		18 05 2013
 * @version 	1.0
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
 * @author Mufy
 * 
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