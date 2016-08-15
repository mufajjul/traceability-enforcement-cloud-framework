/*
 * @(#) XmlDbServiceTraceability.java       1.1 15/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.storagecontroller.presistence.xmldb;

import org.apache.log4j.Logger;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

/**
 * Connection to the XMLDb (Exist)
 * 
 * @version 1.1 15 Aug 2016
 * @author Mufy
 * @Module StorageController
 */
public class XmlDbConnector {

	static Logger logger = Logger.getLogger("XmlDbConnector");

	private String driver;
	private static XmlDbConnector instance = null;

	protected XmlDbConnector() {
		// BasicConfigurator.configure();
	}

	public static XmlDbConnector getInstance() {

		if (instance == null) {
			instance = new XmlDbConnector();
		}
		return instance;
	}

	@SuppressWarnings("rawtypes")
	public void getConnection() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, XMLDBException {

		logger.info("Driver ==> " + driver);

		if (driver == null) {
			// Load default driver
			driver = "org.exist.xmldb.DatabaseImpl";
			logger.info("default driver loaded ==> " + driver);
		}
		Class cl = Class.forName(driver);
		Database database = (Database) cl.newInstance();
		database.setProperty("create-database", "true");
		DatabaseManager.registerDatabase(database);

		logger.info("Databases ==> " + DatabaseManager.getDatabases().toString());
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

}
