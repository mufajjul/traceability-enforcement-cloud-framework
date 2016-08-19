/*
 * @(#) ServiceXmlDocumentTraceability.java       1.1 19/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.traceability.api;

import com.provenance.cloudprovenance.traceabilityModel.generated.TraceabilityDocument;

/**
 * This interface defines specific methods related to XML based data
 * traceability
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
 */
public interface ServiceXmlDocumentTraceability<T> extends
		ServiceTraceability<T> {

	public TraceabilityDocument getRootNode();

	public void InitilizeTraceabilityDocument();

	public boolean isTraceabilityDocumentEmpty();

	public void setCurrentTraceabilityDocument(T t);

	public T getCurrentTraceabilityDocument();

	public void setMaxStatementPerDocument(int max);

}