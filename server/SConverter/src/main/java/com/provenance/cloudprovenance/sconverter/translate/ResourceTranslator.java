/*
 * @(#) ServiceXmlDocumentTraceability.java       1.1 18/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.sconverter.translate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This class translates a resource from XACML
 * 
 * @version 1.1 18 Aug 2016
 * @author Mufy
 * @Module SConverter
 */
public class ResourceTranslator {

	private static Document document;

	static Logger logger = Logger.getLogger(ResourceTranslator.class);

	public String convertAcProvRequestToXACML(String cProvlRequest,
			File styleSheet) throws TransformerException {

		System.setProperty("javax.xml.transform.TransformerFactory",
				"net.sf.saxon.TransformerFactoryImpl");

		// Use a Transformer for output
		TransformerFactory tFactory = TransformerFactory.newInstance();
		StreamSource stylesource = new StreamSource(styleSheet);
		Transformer transformer = tFactory.newTransformer(stylesource);

		StringReader reader = new StringReader(cProvlRequest);
		StringWriter fs = new StringWriter();

		transformer.transform(new StreamSource(reader), new StreamResult(fs));

		logger.info("Resource Transformation successful: " + fs.toString());

		return fs.toString();

	}

	public void convertToXACML(File cprovlResourceFile, File styleSheet,
			String xacmlResourceGenDirectory)
			throws ParserConfigurationException, SAXException, IOException,
			TransformerException, URISyntaxException {

		String absolutePathToXACMLresourceDirectory = null;

		try {
			// check if the directory to the path exists, and the directory
			// created
			File xacmlResourceDirectory = new File(xacmlResourceGenDirectory);
			if ((!xacmlResourceDirectory.exists())
					&& xacmlResourceDirectory.getParent() != null) {
				xacmlResourceDirectory.mkdir();

				absolutePathToXACMLresourceDirectory = xacmlResourceDirectory
						.getAbsolutePath();
			} else if (xacmlResourceDirectory.exists()) {
				absolutePathToXACMLresourceDirectory = xacmlResourceDirectory
						.getAbsolutePath();
			} else {

				URI resURI = this.getURIpath(xacmlResourceGenDirectory);

				if (resURI == null) {

					resURI = new URI("file://" + xacmlResourceGenDirectory);
					logger.info("URI path ==.> " + resURI.getPath());

				}
				xacmlResourceGenDirectory = resURI.getPath();
				xacmlResourceDirectory = new File(xacmlResourceGenDirectory);
				boolean directoryCreationStatus = xacmlResourceDirectory
						.mkdir();

				logger.warn("Updated XACML resource directory path ==> "
						+ xacmlResourceGenDirectory
						+ ":  directory creation status: "
						+ directoryCreationStatus);

				absolutePathToXACMLresourceDirectory = xacmlResourceDirectory
						.getAbsolutePath();
			}

		} catch (NullPointerException ex) {
			// directory doesn't exist, create it
			logger.warn("Absolute path not found, recreating");
		}

		logger.debug("XMLresourceGenDirectoryPath : "
				+ absolutePathToXACMLresourceDirectory);
		File conversionFile = new File(absolutePathToXACMLresourceDirectory
				+ "/" + cprovlResourceFile.getName().split("-cprovl.xml")[0]
				+ "-xacml.xml");

		conversionFile.createNewFile();
		logger.debug("ResourceFile Path ==> "
				+ conversionFile.getAbsolutePath());

		System.setProperty("javax.xml.transform.TransformerFactory",
				"net.sf.saxon.TransformerFactoryImpl");

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();
		document = builder.parse(cprovlResourceFile);

		// Use a Transformer for output
		TransformerFactory tFactory = TransformerFactory.newInstance();
		StreamSource stylesource = new StreamSource(styleSheet);
		Transformer transformer = tFactory.newTransformer(stylesource);

		DOMSource source = new DOMSource(document);

		FileOutputStream fs = new FileOutputStream(conversionFile);
		StreamResult result = new StreamResult(fs);

		logger.debug("Applying transfomer");
		transformer.transform(source, result);

		logger.info("Policy and Resource Transformation successful");

		fs.close();
	}

	public boolean directoryFilesConverter(String sourceDirectoryPath,
			String ResourceConverterXSLT, String destinationDirectoryPath)
			throws URISyntaxException, ParserConfigurationException,
			SAXException, IOException, TransformerException {

		String absoluteDestinationDirectoryPath = null;
		File directoryFiles = null;
		File converterXSLTfile = null;

		logger.debug("Source directory path ==> " + sourceDirectoryPath);

		directoryFiles = new File(sourceDirectoryPath);

		// File f = new File ("/TraceableConfidenShare/app/temp.txt");
		// f.createNewFile();
		logger.debug("Directory exists!!!" + directoryFiles.exists()
				+ " new path ==> ");

		if (!(directoryFiles.exists())) {

			directoryFiles = new File(this.getURIpath(sourceDirectoryPath));
		}

		logger.debug("Directory files to be converted ==> "
				+ directoryFiles.getAbsolutePath() + " "
				+ directoryFiles.exists());

		converterXSLTfile = new File(ResourceConverterXSLT);

		if (!(converterXSLTfile.exists())) {
			converterXSLTfile = new File(this.getURIpath(ResourceConverterXSLT));
		}

		logger.debug("Converter files  ==> "
				+ converterXSLTfile.getAbsolutePath() + " "
				+ converterXSLTfile.exists());

		if (directoryFiles.isDirectory()) {

			File allFiles[] = directoryFiles.listFiles();

			logger.debug("Total no of files found ==> " + allFiles.length);

			for (int i = 0; i < allFiles.length; i++) {

				File cprovlResourceFile = allFiles[i];
				logger.debug("Resource file to be converted ==> "
						+ cprovlResourceFile.getAbsolutePath());

				File destinationDirectoryPathFile = new File(
						destinationDirectoryPath);

				if ((!destinationDirectoryPathFile.exists())
						&& destinationDirectoryPathFile.getParent() != null) {
					destinationDirectoryPathFile.mkdir();
					absoluteDestinationDirectoryPath = destinationDirectoryPathFile
							.getAbsolutePath();
				} else {
					absoluteDestinationDirectoryPath = destinationDirectoryPathFile
							.getAbsolutePath();
				}

				logger.debug("absoluteDestinationDirectoryPath: "
						+ absoluteDestinationDirectoryPath);
				convertToXACML(cprovlResourceFile, converterXSLTfile,
						absoluteDestinationDirectoryPath);
			}
		}
		return true;
	}

	public URI getURIpath(String filePath) throws URISyntaxException {

		try {

			logger.info("File path ==> " + filePath);
			URI fileURIPath = ClassLoader.getSystemClassLoader()
					.getResource(filePath).toURI();
			logger.info("file path " + fileURIPath);

			return fileURIPath;

		} catch (NullPointerException ex) {
			logger.warn("Path not found, reutrning null!!!");
			return null;
		}
	}
}
