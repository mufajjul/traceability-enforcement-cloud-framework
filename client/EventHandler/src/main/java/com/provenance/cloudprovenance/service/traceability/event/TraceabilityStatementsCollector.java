/**
 * @file 		TraceabilityStatementsCollector.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		EventHandler
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.traceability.event;

/**
 * @author Mufy
 * 
 */
@Deprecated
public interface TraceabilityStatementsCollector<E> {

	public void addStatement(E newProvenanceEntry);

	public E getStatement();
	
	public int getTotalStatements();

}
