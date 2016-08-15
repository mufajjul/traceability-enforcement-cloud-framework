/*
 * @(#) ProvenanceStoreNodeException.java       1.1 14/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.storagecontroller.persistence.exception;

/**
* This class represents a provenance store node exception
* 
* @version 1.1 14 Aug 2016
* @author Mufy
* @Module StorageController
*/
public class ProvenanceStoreNodeException extends Exception {

	private static final long serialVersionUID = 1L;

	public ProvenanceStoreNodeException(String message) {
		super(message);
	}
}
