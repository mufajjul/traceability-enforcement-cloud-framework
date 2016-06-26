/**
 * @file 		TraceabilityStoreController.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		TraceabilityStoreWS
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.traceabilitystore.ws.controler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.storagecontroller.presistence.traceabilitystore.service.XmlDbServiceTraceability;
import com.provenance.cloudprovenance.traceabilitystore.ws.support.TraceabilityResponse;

/**
 * REST implementations for service interactions
 * 
 * @author Mufy
 * 
 */
@Path("/")
@Produces("application/xml")
// @Produces({"text/html"} )
public class TraceabilityStoreController implements
		TraceabilityStoreService<Response> {

	static Logger logger = Logger.getLogger("ProvenanceStoreController");

	// TODO - create a arrayList -- for the records ....
	static String currentTraceabilityRecordId = null;
	int maxSizeRecord;
	String defaultFileExtensionOfrecord;
	String defaultServiceNamespace;

	static int fileCounter = 0;

	@Autowired
	private XmlDbServiceTraceability traceabilityStoreService;
	@Autowired
	private TraceabilityResponse trResponse;

	@Override
	@POST
	@Path(value = "/{serviceId}/{traceabilityType}")
	public Response createTraceabilityDocument(
			@PathParam("serviceId") String serviceId,
			@PathParam("traceabilityType") String traceabilityType,
			@Context HttpServletRequest request) {

		boolean response = false;
		String bodyText = null;
		try {
			bodyText = getBody(request);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		;

		if (currentTraceabilityRecordId == null) {
			currentTraceabilityRecordId = "TraceabilityRecord-"
					+ (System.currentTimeMillis());
			String currentTraceabilityRecordIdFileName = constructTraceabilityFileName(currentTraceabilityRecordId);

			// create a blank document first
			response = traceabilityStoreService.createTraceabilityInstance(
					serviceId, traceabilityType,
					currentTraceabilityRecordIdFileName);
		}

		if (bodyText == null || bodyText.equals("")) {
			// add template content
			String tempelateProvText = this.getTemplateProvFile();
			logger.info("Creating traceability record with template content ");
			response = traceabilityStoreService.createTraceabilityEntry(
					serviceId, traceabilityType,
					constructTraceabilityFileName(currentTraceabilityRecordId),
					"", tempelateProvText);
			logger.info("Traceability record created successfully");
		} else {
			// add sent traceability data

			logger.info("Creating traceability record with the content from the request: \n"
					+ bodyText);
			response = traceabilityStoreService.createTraceabilityEntry(
					serviceId, traceabilityType,
					constructTraceabilityFileName(currentTraceabilityRecordId),
					"", bodyText);
			logger.info("traceability record created successfully");
		}

		String responseUri = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ request.getRequestURI() + "/" + currentTraceabilityRecordId;

		String responseContent = trResponse.genTraceabilityRecordIdResponse(
				currentTraceabilityRecordId, serviceId,
				defaultFileExtensionOfrecord, responseUri);

		logger.info("response content: " + responseUri);

		if (response == true) {
			ResponseBuilder rBuilder = Response.status(201);
			rBuilder.entity(responseContent);
			return rBuilder.build();
		} else {
			ResponseBuilder rBuilder = Response.status(400);
			return rBuilder.build();

		}
	}

	@Override
	@PUT
	@Path(value = "/{serviceId}/{traceabilityType}/{documentId}")
	public Response addTraceabilityDocumentEntries(
			@PathParam("serviceId") String serviceId,
			@PathParam("traceabilityType") String traceabilityType,
			@PathParam("documentId") String documentId,
			@Context HttpServletRequest request) {

		try {
			String entryItem = getBody(request);
			logger.info("Received PUT request -> serviceId: " + serviceId
					+ " traceability Type: " + traceabilityType
					+ " documentId: " + documentId + " Request Content: "
					+ entryItem.length());

			String currentFileIdWithoutNamespace = updateFileNameWithoutNamespace(documentId);
			documentId = constructTraceabilityFileName(currentFileIdWithoutNamespace);

			// check for file Id
			if (!(currentTraceabilityRecordId
					.contains(currentFileIdWithoutNamespace))) {
				logger.warn("Unable to find the exisiting traceability record: "
						+ currentTraceabilityRecordId
						+ ", creating a new traceability record! "
						+ currentFileIdWithoutNamespace);

				traceabilityStoreService.createTraceabilityInstance(serviceId,
						traceabilityType, documentId);
				currentTraceabilityRecordId = currentFileIdWithoutNamespace;
			}

			// TODO - Need to check if the file exists in the store
			// Check for MAX size

			logger.info("Check for MAX traceability file size:  serviceId "
					+ serviceId + " traceabilityType: " + traceabilityType
					+ " fileId: " + currentTraceabilityRecordId);

			int currentFileEntrySize = traceabilityStoreService
					.currentTraceabilityRecordSize(serviceId, traceabilityType,
							currentTraceabilityRecordId + ".xml");

			logger.info("current traceability file size: "
					+ currentFileEntrySize);

			if (currentFileEntrySize > this.maxSizeRecord) {

				String filePart = (documentId.split(".xml"))[0];

				String updatedFileName = filePart += "part-" + (++fileCounter);
				// new file
				traceabilityStoreService.createTraceabilityInstance(serviceId,
						traceabilityType,
						constructTraceabilityFileName(updatedFileName));

				currentTraceabilityRecordId = updatedFileName;

				documentId = constructTraceabilityFileName(updatedFileName);

				logger.info("created a new file: " + documentId);
			}

			logger.info("updating content with the traceability record: "
					+ documentId + "\n" + entryItem);

			boolean response = traceabilityStoreService
					.updateTraceabilityEntry(
							serviceId,
							traceabilityType,
							constructTraceabilityFileName(currentTraceabilityRecordId),
							"", entryItem);

			if (response == true) {
				ResponseBuilder rBuilder = Response.status(200);
				return rBuilder.build();
			} else {
				ResponseBuilder rBuilder = Response.status(400);
				return rBuilder.build();
			}

		} catch (Exception e) {

			e.printStackTrace();
			ResponseBuilder rBuilder = Response.status(501);
			return rBuilder.build();
		}

		// return false;
	}

	@Override
	@GET
	@Path(value = "/{serviceId}/{traceabilityType}")
	public Response getTraceabilityDocumentId(
			@PathParam("serviceId") String serviceId,
			@PathParam("traceabilityType") String traceabilityType,
			@Context HttpServletRequest request) {

		if (currentTraceabilityRecordId == null) {

			logger.warn("Current file record Id does not exist, need to create a new record !!!");
			ResponseBuilder rBuilder = Response.status(404);
			rBuilder.entity("<TraceabilityDocument></TraceabilityDocument>");
			return rBuilder.build();

		} else {
			String responseUri = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ request.getRequestURI() + "/"
					+ currentTraceabilityRecordId;

			logger.info("Response URI: " + responseUri);

			logger.info("Returning the current record Id");
			ResponseBuilder rBuilder = Response.status(200);
			String responseContent = trResponse
					.genTraceabilityRecordIdResponse(
							currentTraceabilityRecordId, serviceId,
							defaultFileExtensionOfrecord, responseUri);

			logger.info("Sending GET resource URI response: " + responseContent);

			rBuilder.entity(responseContent);
			return rBuilder.build();
		}
	}

	// TODO - to be implemented
	@Override
	@GET
	@Path(value = "/{serviceId}/{traceabilityType}/{documentId}")
	public Response getTraceabilityDocument(
			@PathParam("serviceId") String serviceId,
			@PathParam("traceabilityType") String traceabilityType,
			@PathParam("documentId") String documentId) {

		logger.info("Get record: serviceId->" + serviceId
				+ "; traceabilityType-> " + traceabilityType + "; documentId: "
				+ documentId);

		String trDocument = traceabilityStoreService.getTraceabilityInstance(
				serviceId, traceabilityType, documentId);

		if (trDocument == null) {
			ResponseBuilder rBuilder = Response.status(501);
			return rBuilder.build();
		} else {
			ResponseBuilder rBuilder = Response.status(200);
			rBuilder.entity(trDocument);
			return rBuilder.build();

		}
		// return "<outcome> not yet implemented </outcome>";
	}

	@Override
	@GET
	@Path(value = "/{serviceId}/{traceabilityType}/{documentId}/{elementId}")
	public Response getTraceabilityDocumentElement(
			@PathParam("serviceId") String serviceId,
			@PathParam("traceabilityType") String traceabilityType,
			@PathParam("documentId") String documentId,
			@PathParam("elementId") String elementId) {
		// return "<outcome> not yet implemented </outcome>";

		ResponseBuilder rBuilder = Response.status(501);
		return rBuilder.build();

	}

	public String constructTraceabilityFileName(String currentId) {

		logger.debug("current record Id: " + currentId);
		String fileRecordId = currentId + "." + defaultFileExtensionOfrecord;

		return fileRecordId;
	}

	public String updateFileNameWithoutNamespace(String clientRequestRecordId) {

		logger.debug("clientRequestRecordId: " + clientRequestRecordId);

		if (clientRequestRecordId.contains("confidenshare")) {

			String updatedId = clientRequestRecordId.split("confidenshare:")[1];
			logger.info("updated record Id without namespace prefix: "
					+ updatedId);
			return updatedId;
		}
		return clientRequestRecordId;
	}

	/**
	 * Get request body content
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getBody(HttpServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(
						inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;
	}

	public String getTemplateProvFile() {

		BufferedReader br = null;

		InputStream is = (getClass().getClassLoader()
				.getResourceAsStream("templateTraceabilityfile.xml"));

		try {
			br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}

			logger.info(sb.toString());
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public int getMaxSizeRecord() {
		return maxSizeRecord;
	}

	public void setMaxSizeRecord(int maxSizeRecord) {
		this.maxSizeRecord = maxSizeRecord;
	}

	public String getDefaultFileExtensionOfrecord() {
		return defaultFileExtensionOfrecord;
	}

	public void setDefaultFileExtensionOfrecord(
			String defaultFileExtensionOfrecord) {
		this.defaultFileExtensionOfrecord = defaultFileExtensionOfrecord;
	}

	public String getDefaultServiceNamespace() {
		return defaultServiceNamespace;
	}

	public void setDefaultServiceNamespace(String defaultServiceNamespace) {
		this.defaultServiceNamespace = defaultServiceNamespace;
	}

}
