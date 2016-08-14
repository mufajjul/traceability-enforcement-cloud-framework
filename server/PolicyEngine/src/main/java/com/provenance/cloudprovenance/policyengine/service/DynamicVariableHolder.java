/*
 * @(#) DynamicVariableHolder.java       1.1 13/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.policyengine.service;

import java.util.HashMap;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * This class is responsible for storing and handling of the dynamic variables
 * 
 * @version 1.1 13 Aug 2016
 * @author Mufy
 * @Module PolicyEngine
 */
public class DynamicVariableHolder {

	//Store variable in a hashmap 
	private HashMap<String, Object> variableMap;
	Logger logger = Logger.getLogger(DynamicVariableHolder.class);
	static DynamicVariableHolder dynamicHolder;

	//TODO - for scalability, DB should be used
	
	private DynamicVariableHolder() {
		if (variableMap == null) {
			variableMap = new HashMap<String, Object>();
		}
	}

	public static DynamicVariableHolder getDynamicVariableHolderInstance() {
		if (dynamicHolder == null) {
			dynamicHolder = new DynamicVariableHolder();
			return dynamicHolder;
		} else {
			return dynamicHolder;
		}
	}

	public HashMap<String, Object> getVariableMap() {
		return variableMap;
	}

	private void setVariableMap(HashMap<String, Object> variableMap) {
		this.variableMap = variableMap;
	}

	public Object getDynamicVar(String key) {
		return getVariableMap().get(key);

	}

	public boolean varExist(String key) {
		return this.getVariableMap().containsKey(key);
	}

	public void addDynamicVar(String variableName, Object variableValue) {
		this.getVariableMap().put(variableName, variableValue);
	}

	public void removeDynamicVar(String variableName) {
		this.getVariableMap().remove(variableName);
	}

	public Set<String> getKeys() {
		return variableMap.keySet();
	}
}
