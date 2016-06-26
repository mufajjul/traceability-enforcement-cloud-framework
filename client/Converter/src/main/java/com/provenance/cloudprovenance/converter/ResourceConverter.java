/**
 * @file 		ResourceConverter.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		Converter
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.converter;

/**
 * @author Mufy
 * 
 */
public interface ResourceConverter <T> {

	public String marhsallObject(T element);
}
