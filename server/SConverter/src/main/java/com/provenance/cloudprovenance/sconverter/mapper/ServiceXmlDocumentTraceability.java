/**
 * @file 		ServiceXmlDocumentTraceability.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		SConverter
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.sconverter.mapper;

import com.provenance.cloudprovenance.traceabilityModel.generated.TraceabilityDocument;

/**
 * This interface defines specific methods related to XML based data traceability
 * 
 * @author Mufy
 * 
 */
public interface ServiceXmlDocumentTraceability<T> extends ServiceTraceability<T> {

	public TraceabilityDocument getRootNode();

	public void InitilizeTraceabilityDocument();

	public boolean isTraceabilityDocumentEmpty();
	
	public void setCurrentTraceabilityDocument(T t);

	public T getCurrentTraceabilityDocument();
	
	public void setMaxStatementPerDocument(int max);
	
	
	//TODO - may need to remove this.
	//public TraceabilityEventCollector<T> getTraceabilityEventCollection();

}
