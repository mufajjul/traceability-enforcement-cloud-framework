/*
 * @(#) ResourceConverter.java       1.1 16/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.converter;

/**
 * This interface defines a method for converting Java Objects to XML 
 *
 * @version 1.1 16 Aug 2016
 * @author Mufy
 * @Module Converter
 */
public interface ResourceConverter <T> {

	public String marhsallObject(T element);
}
