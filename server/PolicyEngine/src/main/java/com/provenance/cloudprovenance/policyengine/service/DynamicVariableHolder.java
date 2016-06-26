/**
 * @file 		DynamicVariableHolder.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		PolicyEngine
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.policyengine.service;

import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * This class is responsible for storing and handling of the dynamic variables
 * 
 * @author Mufy
 * @Note For scalability, DB should be considered  
 */
public class DynamicVariableHolder {

	//Store variable in a hashmap 
	private HashMap<String, Object> variableMap;
	Logger logger = Logger.getLogger(DynamicVariableHolder.class);
	static DynamicVariableHolder dynamicHolder;

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
