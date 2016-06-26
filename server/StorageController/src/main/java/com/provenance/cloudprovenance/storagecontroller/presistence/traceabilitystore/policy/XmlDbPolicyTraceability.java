/**
 * @file 		XmlDbPolicyTraceability.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		StorageController
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.storagecontroller.presistence.traceabilitystore.policy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.xmldb.api.base.XMLDBException;

import com.provenance.cloudprovenance.storagecontroller.presistence.xmldb.XmlDbService;

/** 
* @author Mufy
* 
*/
public class XmlDbPolicyTraceability implements PolicyTraceability<String> {

	static Logger logger = Logger.getLogger("XmlDbStore");
	static XmlDbService dbService;
	final String RESOURCE_TYPE;

	public XmlDbPolicyTraceability(XmlDbService xmlDbService,
			String resourceType) {
		dbService = xmlDbService;
		RESOURCE_TYPE = resourceType;

	}

	// @Override
	public boolean createRequestInstance(String serviceId,
			String traceabilityType, String instanceId, String entryItem) {

		boolean response = createDbfileInstance(serviceId, traceabilityType,
				instanceId, entryItem);

		return response;
	}

	@Override
	public boolean createResponseInstance(String serviceId,
			String traceabilityType, String instanceId, String entryItem) {

		boolean response = createDbfileInstance(serviceId, traceabilityType,
				instanceId, entryItem);

		return response;

	}

	private boolean createDbfileInstance(String serviceId,
			String traceabilityType, String instanceId, String entryItems) {

		File tempTraceabilityFile = new File(instanceId);
		boolean fileStatus = false;
		try {
			fileStatus = tempTraceabilityFile.createNewFile();

			BufferedWriter writer;
			writer = new BufferedWriter(new FileWriter(tempTraceabilityFile));
			writer.write(entryItems);
			writer.close();

			dbService.addTraceabilityResource(serviceId, traceabilityType,
					instanceId, tempTraceabilityFile, RESOURCE_TYPE);

			// Clean up
			tempTraceabilityFile.delete();
			return true;

		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
	}

}
