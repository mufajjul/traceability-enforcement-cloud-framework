/*
 * @(#) TraceabilityStatementsQueue.java       1.1 18/8/2016
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

import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

/**
 * This class implements the methods for traceability collection queue. It has
 * been replaced with JMS queue
 * 
 * @version 1.1 18 Aug 2016
 * @author Mufy
 * @Module EventHandler
 */
@Deprecated
public class TraceabilityStatementsQueue implements
		TraceabilityStatementsCollector<String> {

	private final int MAX_SIZE;

	Logger logger = Logger.getLogger(TraceabilityStatementsQueue.class);

	Queue<String> provenanceEntryQueue = new LinkedList<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.provenance.cloudprovenance.model.client.TraceabilityEventCollector
	 * #addStatement(java.lang.Object)
	 */

	public TraceabilityStatementsQueue(int maxSize) {
		this.MAX_SIZE = maxSize;

	}

	@Override
	public synchronized void addStatement(String newProvenanceEntry) {

		while (this.getProvenanceEntryQueue().size() == MAX_SIZE) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		this.getProvenanceEntryQueue().add(newProvenanceEntry);

		logger.info("ADD: Queue size is ==> "
				+ this.getProvenanceEntryQueue().size());

		notifyAll();

	}

	@Override
	public synchronized String getStatement() {

		while (this.getProvenanceEntryQueue().size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String tempProvenanceEntry = this.getProvenanceEntryQueue().poll();

		logger.info("GET : Queue size is ==> "
				+ this.getProvenanceEntryQueue().size());

		notifyAll();

		return tempProvenanceEntry;
	}

	/**
	 * @return the provenanceEntryQueue
	 */
	public Queue<String> getProvenanceEntryQueue() {
		return provenanceEntryQueue;
	}

	/**
	 * @param provenanceEntryQueue
	 *            the provenanceEntryQueue to set
	 */
	public void setProvenanceEntryQueue(Queue<String> provenanceEntryQueue) {
		this.provenanceEntryQueue = provenanceEntryQueue;
	}

	@Override
	public int getTotalStatements() {
		return this.getProvenanceEntryQueue().size();

	}

}
