/*
 * @(#) XmlDbService.java       1.1 16/8/2016
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

import java.io.File;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.exist.xmldb.RemoteXPathQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

import com.provenance.cloudprovenance.traceabilitystore.ns.CprovNamespacePrefixMapper;


/**
 * Generic XML DB operations implementation
 * 
 * @version 1.1 16 Aug 2016
 * @author Mufy
 * @Module StorageController
 */
public class XmlDbService {

	@Autowired
	private CprovNamespacePrefixMapper cProvNameSpace;

	static Logger logger = Logger.getLogger("XmlDbService");

	private XmlDbConnector conn;
	private String dbCommURI;
	private String userName;
	private String password;
	private String store;

	public XmlDbService() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, XMLDBException {

		// BasicConfigurator.configure();
		conn = XmlDbConnector.getInstance();
		conn.getConnection();
	}

	public synchronized Resource getTraceabilityResource(String serviceId,
			String traceabilityType, String traceabilityFileId)
			throws XMLDBException, URISyntaxException {

		String traceabilityUri = this.constructTraceabilityURI(serviceId,
				traceabilityType);

		Collection col = DatabaseManager.getCollection(this.getDbCommURI()
				+ traceabilityUri);
		Resource res = col.getResource(traceabilityFileId);

		return res;
	}

	public synchronized Collection getTraceabilityResourceCollection(
			String serviceId, String traceabilityType, String traceabilityFileId)
			throws XMLDBException, URISyntaxException {

		String traceabilityUri = this.constructTraceabilityURI(serviceId,
				traceabilityType);

		Collection col = DatabaseManager.getCollection(this.getDbCommURI()
				+ traceabilityUri, this.getUserName(), this.getPassword());

		return col;
	}

	public boolean xQueryTraceabilityStore(String xPathExpression,
			String serviceId, String traceabilityType)
			throws URISyntaxException, XMLDBException {

		String fileURI = constructTraceabilityURI(serviceId, traceabilityType);

		logger.debug("file URI : " + fileURI + ":   Resource URI (Db comm : "
				+ this.getDbCommURI() + "  xPath " + xPathExpression);

		Collection col = DatabaseManager.getCollection(this.getDbCommURI()
				+ fileURI, this.getUserName(), this.getPassword());

		String childCollectionNames[] = col.listResources();

		logger.debug("Child collection name size : "
				+ childCollectionNames.length);

		RemoteXPathQueryService service = (RemoteXPathQueryService) col
				.getService("XPathQueryService", "3.0");
		service.setProperty("indent", "yes");
		service.setNamespace("prov", cProvNameSpace.getNsSuffixProv());
		service.setNamespace("cprov", cProvNameSpace.getNsSuffixCprov());
		service.setNamespace("ex", cProvNameSpace.getNsSuffixEx());
		service.setNamespace("cprovd", cProvNameSpace.getNsSuffixCProvd());
		service.setNamespace("confidenshare",
				cProvNameSpace.getNsSuffixCOnfidenshare());

		for (int i = 0; i < childCollectionNames.length; i++) {
			logger.debug("collection content: " + childCollectionNames[i]);

			String individualFilePath = fileURI + "/" + childCollectionNames[i];

			String xQuery = "  doc(\"" + individualFilePath + "\")"
					+ xPathExpression;

			logger.info("xQuery path: " + xQuery);

			ResourceSet resultSet = service.query(xQuery);

			logger.info("Result is: " + resultSet + "  result Size: "
					+ resultSet.getSize());

			if (resultSet.getSize() != 0) {
				return true;
			}
		}

		return false;
	}

	public int getResourceSize(String serviceId, String traceabilityType,
			String traceabilityFileId) throws XMLDBException,
			URISyntaxException {

		int sumResult = 0;
		String fileURI = constructTraceabilityURI(serviceId, traceabilityType);
		String xPathExpression = "//cprov:traceabilityDocument/count(*)";

		logger.debug("file URI : " + this.getDbCommURI()
				+ ":   Resource URI : " + fileURI);

		Collection col = DatabaseManager.getCollection(this.getDbCommURI()
				+ fileURI, this.getUserName(), this.getPassword());

		fileURI += "/" + traceabilityFileId;

		RemoteXPathQueryService service = (RemoteXPathQueryService) col
				.getService("XPathQueryService", "3.0");
		service.setProperty("indent", "yes");

		service.setNamespace("prov", cProvNameSpace.getNsSuffixProv());
		service.setNamespace("cprov", cProvNameSpace.getNsSuffixCprov());
		service.setNamespace("ex", cProvNameSpace.getNsSuffixEx());
		service.setNamespace("cprovd", cProvNameSpace.getNsSuffixCProvd());
		service.setNamespace("confidenshare",
				cProvNameSpace.getNsSuffixCOnfidenshare());

		String xQuery = "  doc(\"" + fileURI + "\")" + xPathExpression;

		logger.info("xQuery path: " + xQuery);
		ResourceSet resultSet = service.query(xQuery);
		logger.info("Result is: " + resultSet.getSize());

		if (resultSet.getSize() != 0) {
			ResourceIterator ri = resultSet.getIterator();

			while (ri.hasMoreResources()) {
				Resource r = ri.nextResource();
				logger.info("result is: " + (String) r.getContent());
				sumResult += Integer.parseInt((String) r.getContent());
			}
			return sumResult;
		}

		logger.warn("No match found !");
		return -1;
	}

	public synchronized Resource[] getAlltraceabilityResources(
			String serviceId, String traceabilityType) throws XMLDBException,
			URISyntaxException {

		String traceabilityURI = this.constructTraceabilityURI(serviceId,
				traceabilityType);

		logger.info("ResourceURI: " + traceabilityURI);

		Collection traceabilityCol = DatabaseManager.getCollection(this
				.getDbCommURI() + traceabilityURI);

		String childCollectionNames[] = traceabilityCol.listResources();

		Resource childResources[] = new Resource[childCollectionNames.length];

		for (int i = 0; i < childResources.length; i++) {

			logger.info("File exists ==> " + childCollectionNames[i]);
			childResources[i] = traceabilityCol
					.getResource(childCollectionNames[i]);
		}

		return childResources;

	}

	public synchronized String getAllTraceabilityxQueryRefinedResources(
			String serviceId, String traceabilityType, String xPathExpression)
			throws URISyntaxException, XMLDBException {

		String fileURI = constructTraceabilityURI(serviceId, traceabilityType);

		String totalMatchNodes = new String();

		logger.debug("file URI : " + this.getDbCommURI()
				+ ":   Resource URI : " + fileURI);

		Collection col = DatabaseManager.getCollection(this.getDbCommURI()
				+ fileURI, this.getUserName(), this.getPassword());

		String childCollectionNames[] = col.listResources();

		for (int i = 0; i < childCollectionNames.length; i++) {

			String connectionFileUri = fileURI + "/" + childCollectionNames[i];

			RemoteXPathQueryService service = (RemoteXPathQueryService) col
					.getService("XPathQueryService", "3.0");
			service.setProperty("indent", "yes");

			service.setNamespace("prov", cProvNameSpace.getNsSuffixProv());
			service.setNamespace("cprov", cProvNameSpace.getNsSuffixCprov());
			service.setNamespace("ex", cProvNameSpace.getNsSuffixEx());
			service.setNamespace("cprovd", cProvNameSpace.getNsSuffixCProvd());
			service.setNamespace("confidenshare",
					cProvNameSpace.getNsSuffixCOnfidenshare());

			String xQuery = "  doc(\"" + connectionFileUri + "\")"
					+ xPathExpression;

			logger.info("xQuery path: " + xQuery);

			ResourceSet resultSet = service.query(xQuery);

			logger.info("Result is: " + resultSet.getSize());

			if (resultSet.getSize() != 0) {

				ResourceIterator ri = resultSet.getIterator();

				while (ri.hasMoreResources()) {
					Resource r = ri.nextResource();

					logger.debug((String) r.getContent());

					totalMatchNodes += (String) r.getContent() + "\n";
				}
			}
		}
		return totalMatchNodes;
	}

	public synchronized void addTraceabilityResource(String serviceId,
			String traceabilityType, String traceabilityFileId,
			File traceabilityFile, String resourceType) throws XMLDBException,
			URISyntaxException {

		String traceabilityURI = this.constructTraceabilityURI(serviceId,
				traceabilityType);

		Collection traceabilityCol = DatabaseManager.getCollection(
				this.getDbCommURI() + traceabilityURI, this.getUserName(),
				this.getPassword());

		Resource newResource = traceabilityCol.createResource(
				traceabilityFileId, resourceType);
		newResource.setContent(traceabilityFile);

		traceabilityCol.storeResource(newResource);

		logger.info("Successfully added provenance ==> " + traceabilityFileId);
	}

	public synchronized void updateTraceabilityResource(String serviceId,
			String traceabilityType, String traceabilityFileId,
			String resourceType, String traceabilityFileContent)
			throws XMLDBException, URISyntaxException {

		try {

			String fileURI = constructTraceabilityURI(serviceId,
					traceabilityType);

			logger.debug("file URI : " + this.getDbCommURI()
					+ ":   Resource URI : " + fileURI);

			Collection col = DatabaseManager.getCollection(this.getDbCommURI()
					+ fileURI, this.getUserName(), this.getPassword());

			fileURI += "/" + traceabilityFileId;

			logger.debug("Resource Count: " + col.getResourceCount());

			RemoteXPathQueryService service = (RemoteXPathQueryService) col
					.getService("XPathQueryService", "3.0");
			service.setProperty("indent", "yes");
			service.clearNamespaces();

			service.setNamespace("cprov", cProvNameSpace.getNsSuffixCprov());
			service.setNamespace("prov", cProvNameSpace.getNsSuffixProv());
			service.setNamespace("ex", cProvNameSpace.getNsSuffixEx());
			service.setNamespace("confidenshare",
					cProvNameSpace.getNsSuffixCOnfidenshare());

			String xQuery = " update insert  " + traceabilityFileContent
					+ "  into   doc(\"" + fileURI
					+ "\")/cprov:traceabilityDocument";

			logger.info("xQuery path:  update insert  "
					+ traceabilityFileContent + "  into  doc(\"" + fileURI
					+ "\")/cprov:traceabilityDocument "
					+ cProvNameSpace.getNsSuffixCprov());

			logger.info("Content:" + traceabilityFileContent);

			ResourceSet result = service.query(xQuery);

		} catch (XMLDBException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		logger.info("Successfully added provenance ==> " + traceabilityFileId);
	}

	public synchronized void deleteTraceabilityResource(String serviceId,
			String traceabilityType, String traceabilityFileId)
			throws XMLDBException, URISyntaxException {

		String traceabilityURI = this.constructTraceabilityURI(serviceId,
				traceabilityType);

		Resource resource = this.getTraceabilityResource(serviceId,
				traceabilityType, traceabilityFileId);

		Collection traceabilityCol = DatabaseManager.getCollection(
				this.getDbCommURI() + traceabilityURI, this.getUserName(),
				this.getPassword());

		traceabilityCol.removeResource(resource);

		logger.info("Successfully removed provenance ==> " + traceabilityFileId);
	}

	public String constructTraceabilityURI(String serviceId,
			String traceabilityType) throws URISyntaxException {

		if (serviceId == null || traceabilityType == null)
			throw new URISyntaxException(
					"serviceId, provenanceId and provenanceType",
					"Missing information");

		String URI = this.getStore() + "/" + serviceId + "/" + traceabilityType;

		logger.debug("Provenance relative URI ==> " + URI);

		return URI;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getDbCommURI() {
		return dbCommURI;
	}

	public void setDbCommURI(String dbCommURI) {
		this.dbCommURI = dbCommURI;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// TODO - find a proper fix, temp workaround
	@Deprecated
	private String removeExtraNameSpaceDeclaration(String elementWithNameSpace) {

		// String removeThisText =
		// " xmlns:ex=\"http://labs.orange.com/uk/ex#\" xmlns:prov=\"http://www.w3.org/ns/prov#\" xmlns:cprov=\"http://labs.orange.com/uk/cprov#\"";

		String cprovFullNamepace = "xmlns:cprov=\""
				+ cProvNameSpace.getNsSuffixCprov() + "\" ";
		String provFullNamepace = "xmlns:prov=\""
				+ cProvNameSpace.getNsSuffixProv() + "\" ";

		// logger.info("namespaced element : "+elementWithNameSpace +
		// "cprov Namespace: "+cprovFullNamepace +
		// " prov Namespace: "+provFullNamepace);

		if (elementWithNameSpace.contains(cprovFullNamepace)) {
			elementWithNameSpace = elementWithNameSpace.replaceFirst(
					cprovFullNamepace, "");

		}

		if (elementWithNameSpace.contains(provFullNamepace)) {
			elementWithNameSpace = elementWithNameSpace.replaceFirst(
					provFullNamepace, "");
		}

		return elementWithNameSpace;
	}

	// TODO - Use a config file
	public String wrapNodesUsingCProvRootNode(String provenanceNodes) {

		String tDoc = new String();

		tDoc = ("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
		tDoc += ("<cprov:traceabilityDocument xmlns:prov=\"http://www.w3.org/ns/prov#\" \n"
				+ " xmlns:cprov=\"http://labs.orange.com/uk/cprov#\" xmlns:cu=\"http://www.w3.org/1999/xhtml/datatypes/\" \n"
				+ " xmlns:r=\"http://labs.orange.com/uk/r#\" xmlns:ex=\"http://www.example.org/\" \n"
				+ " xmlns:opmx=\"http://openprovenance.org/model/opmx#\" xmlns:cprovd=\"http://labs.orange.com/uk/cprovd#\" \n"
				+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n"
				+ " xsi:schemaLocation=\"http://labs.orange.com/uk/cprov# file:/Users/mufy/Dropbox/EngD/year3/schema/cProv-inherited/cProv-v1.2.xsd\"> \n");

		tDoc += provenanceNodes + " \n";
		tDoc += ("</cprov:traceabilityDocument> \n");

		return tDoc;

	}

	public CprovNamespacePrefixMapper getcProvNameSpace() {
		return cProvNameSpace;
	}

	public void setcProvNameSpace(CprovNamespacePrefixMapper cProvNameSpace) {
		this.cProvNameSpace = cProvNameSpace;
	}

}
