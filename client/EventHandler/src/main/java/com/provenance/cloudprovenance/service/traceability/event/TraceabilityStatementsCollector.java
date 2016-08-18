/*
 * @(#) TraceabilityStatementsCollector.java       1.1 18/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.traceability.event;

/**
 * This interface provides method for handing of traceability statements using
 * collection queue. It has been replaced with JMS queue
 * 
 * @version 1.1 18 Aug 2016
 * @author Mufy
 * @Module EventHandler
 */
@Deprecated
public interface TraceabilityStatementsCollector<E> {

	public void addStatement(E newProvenanceEntry);

	public E getStatement();

	public int getTotalStatements();

}
