/*
 * @(#) CprovXmlProvenance.java       1.1 19/8/2016
 *
 * Copyright (c)  Provenance Intelligence Consultancy Limited.
 * 
 * This software is the confidential and proprietary information of 
 * Provenance Intelligence Consultancy Limited.  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Provenance Intelligence Consultancy Limited.
 */
package com.provenance.cloudprovenance.service.api.impl;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import com.provenance.cloudprovenance.eventhandler.service.EventProducer;
import com.provenance.cloudprovenance.service.traceability.api.ServiceTraceabilityCallback;
import com.provenance.cloudprovenance.service.traceability.api.ServiceXmlDocumentTraceability;
import com.provenance.cloudprovenance.traceabilityModel.generated.ActivityRef;
import com.provenance.cloudprovenance.traceabilityModel.generated.Agent;
import com.provenance.cloudprovenance.traceabilityModel.generated.AgentRef;
import com.provenance.cloudprovenance.traceabilityModel.generated.CProcess;
import com.provenance.cloudprovenance.traceabilityModel.generated.CResource;
import com.provenance.cloudprovenance.traceabilityModel.generated.EntityRef;
import com.provenance.cloudprovenance.traceabilityModel.generated.HadOwnership;
import com.provenance.cloudprovenance.traceabilityModel.generated.HadTransitionStateA;
import com.provenance.cloudprovenance.traceabilityModel.generated.HadTransitionStateB;
import com.provenance.cloudprovenance.traceabilityModel.generated.HadTransitionStateC;
import com.provenance.cloudprovenance.traceabilityModel.generated.InternationalizedString;
import com.provenance.cloudprovenance.traceabilityModel.generated.ObjectFactory;
import com.provenance.cloudprovenance.traceabilityModel.generated.PResource;
import com.provenance.cloudprovenance.traceabilityModel.generated.TraceabilityDocument;
import com.provenance.cloudprovenance.traceabilityModel.generated.Transition;
import com.provenance.cloudprovenance.traceabilityModel.generated.Usage;
import com.provenance.cloudprovenance.traceabilityModel.generated.WasExplicitlyCalledBy;
import com.provenance.cloudprovenance.traceabilityModel.generated.WasImplicitlyCalledBy;
import com.provenance.cloudprovenance.traceabilityModel.generated.WasInitiallyCalledBy;
import com.provenance.cloudprovenance.traceabilityModel.generated.WasRecurrentlyCalledBy;
import com.provenance.cloudprovenance.traceabilityModel.generated.WasReferenceOf;
import com.provenance.cloudprovenance.traceabilityModel.generated.WasRepresentationOf;
import com.provenance.cloudprovenance.traceabilityModel.generated.WasVirtualizedBy;

/**
 * This class implements all the method of the ServiceXMLDocumentTraceability,
 * interface, and returns resource's Id
 * 
 * @version 1.1 19 Aug 2016
 * @author Mufy
 * @Module ServiceAPI
 */
public class CprovXmlProvenance implements
		ServiceXmlDocumentTraceability<String>,
		ServiceTraceabilityCallback<String> {

	public final String PREVIOUS_ID = "link_previous";
	public final String AUTO_ID = "auto_gen";

	public final String UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX = "http://labs.orange.com/uk/cprovd#";
	public final String UNIQUE_IDENTIFIER_NS_PROVD_PREFIX = "provd";

	public final String UNIQUE_IDENTIFIER_NS_PROV_SUFFIX = "http://www.w3.org/ns/prov#";
	public final String UNIQUE_IDENTIFIER_NS_PROV_PREFIX = "prov";

	private ObjectFactory tModelFactory = new ObjectFactory();
	private TraceabilityDocument traceabilityDoc;
	private EventProducer<TraceabilityDocument> traceabilityEventProducer;

	private Logger logger = Logger.getLogger("XmlTraceabilityModel");
	private String currentTraceabilityDocument;
	public static String UNIQUE_IDENTIFIER_NS_SUFFIX = "http://labs.orange.com/uk/ex#";
	public static String UNIQUE_IDENTIFIER_NS_PREFIX = "ex";

	private static int MAX_SIZE_GRAPH = 10;
	private static int currentGraphSize = 1;

	// Identifiers static index
	public static int e_cRId = 0, ag_Id = 0, a_Id = 0, wic_Id = 0, wec_Id = 0,
			wrc_Id = 0, ho_Id = 0, wro_Id = 0, wreo_Id = 0, wvb_Id = 0,
			wsb_Id = 0, hpa_Id = 0, u_Id = 0, wicb_Id = 0, tr_id = 0,
			htsa_Id = 0, htsb_Id = 0, htsc_Id = 0;

	public CprovXmlProvenance(
			EventProducer<TraceabilityDocument> traceabilityEventProducer,
			int collectionSize) {
		MAX_SIZE_GRAPH = collectionSize;
		this.traceabilityEventProducer = traceabilityEventProducer;
	}

	// Allow for service level - prefix and suffix
	public CprovXmlProvenance(
			EventProducer<TraceabilityDocument> traceabilityEventProducer,
			String serviceNSPrefix, String ServiceNSSuffix, int collectionSize) {

		this.traceabilityEventProducer = traceabilityEventProducer;
		UNIQUE_IDENTIFIER_NS_PREFIX = serviceNSPrefix;
		UNIQUE_IDENTIFIER_NS_SUFFIX = ServiceNSSuffix;
		MAX_SIZE_GRAPH = collectionSize;
	}

	/**
	 * A node that represents a Cloud resource TODO - Add validation, check if
	 * it is null (
	 */

	@Override
	public synchronized String cResource(String id, String resType,
			String userCloudRef, String vResourceRef, String pResourceRef,
			boolean isReplicable, Date TTL, Date timeStamp,
			String restrictionType, float trustDegree, String des) {

		CResource cResource = tModelFactory.createCResource();

		if (id == null || id.equalsIgnoreCase("")) {
			QName cResourceId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "e"
					+ (++e_cRId), UNIQUE_IDENTIFIER_NS_PREFIX);
			cResource.setId(cResourceId);
			id = cResourceId.getLocalPart();
		} else {
			QName cResourceId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);

			cResource.setId(cResourceId);
		}

		cResource.setUserCloudRef(userCloudRef);
		cResource.setPResourceRef(pResourceRef);

		if (TTL != null) {
			try {
				cResource.setTTL(getXMLgregorialDate(TTL));
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
		}

		cResource.setVResourceRef(vResourceRef);
		cResource.setIsReplicable(isReplicable);
		cResource.setUserTrustDegree(trustDegree);

		QName trResType = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX, resType,
				UNIQUE_IDENTIFIER_NS_PROVD_PREFIX);
		// tr.setRegion(trRegionId);
		cResource.setResType(trResType);

		QName trRestrictionType = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX,
				restrictionType, UNIQUE_IDENTIFIER_NS_PROVD_PREFIX);

		// TODO: Change the restrictionType input to an Array
		cResource.getRestrictionType().add(trRestrictionType);

		if (des != null) {
			cResource.setDes(des);
		}

		getRootNode().getInstance().add(cResource);
		validateAndStoreTraceabilityDocument();

		return id;
	}

	/**
	 * A node that defines a physical resource (resource outside of the cloud)
	 */
	@SuppressWarnings("unused")
	@Override
	public synchronized String pResource(String id, String type, String ip,
			String MAC, String location, Date timeStamp,
			String restrictionType, float trustDegree) {

		PResource phResource = tModelFactory.createPResource();

		if (id == null || id.equalsIgnoreCase("")) {
			QName pResourceId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "e"
					+ (++e_cRId), UNIQUE_IDENTIFIER_NS_PREFIX);
			phResource.setId(pResourceId);
			id = pResourceId.getLocalPart();
		} else {
			QName pResourceId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);

			phResource.setId(pResourceId);
		}

		phResource.setMAC(MAC);
		phResource.setOriginIP(ip);

		// TODO - workout how this can be achieved ......
		List<JAXBElement<?>> locationList = phResource
				.getLocationOrValueOrLabel();

		this.getRootNode().getInstance().add(phResource);

		QName trRestrictionType = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX,
				restrictionType, UNIQUE_IDENTIFIER_NS_PROVD_PREFIX);
		// tr.setRegion(trRegionId);
		phResource.getRestrictionType().add(trRestrictionType);
		phResource.setUserTrustDegree(trustDegree);

		getRootNode().getInstance().add(phResource);
		validateAndStoreTraceabilityDocument();

		return id;
	}

	/**
	 * Represents an agent, which can be an user, or a machine agent
	 */
	@Override
	public synchronized String Agent(String id, String name, String description) {

		Agent agent = tModelFactory.createAgent();

		if (id == null || id.equalsIgnoreCase("")) {
			QName agentId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "ag"
					+ (++ag_Id), UNIQUE_IDENTIFIER_NS_PREFIX);
			agent.setId(agentId);
			id = agentId.getLocalPart();
		} else {
			QName agentId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);

			agent.setId(agentId);
		}

		InternationalizedString iStringName = new InternationalizedString();
		iStringName.setValue(name);

		agent.getLocationOrLabelOrType().add(
				tModelFactory.createLabel(iStringName));

		InternationalizedString iStringDescription = new InternationalizedString();
		iStringDescription.setValue(description);

		this.getRootNode().getEntityOrActivityOrWasGeneratedBy().add(agent);

		validateAndStoreTraceabilityDocument();

		return id;
	}

	/**
	 * A node that represents a process
	 */
	@Override
	public synchronized String cProcess(String id, String userCloudRef,
			String vProcessRef, String pProcessRef, Date timeStamp) {

		CProcess cProcess = tModelFactory.createCProcess();

		if (id == null || id.equalsIgnoreCase("")) {
			QName cProcessId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "a"
					+ (++a_Id), UNIQUE_IDENTIFIER_NS_PREFIX);
			cProcess.setId(cProcessId);
			id = cProcessId.getLocalPart();
		} else {

			QName cProcessId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);

			cProcess.setId(cProcessId);
		}

		cProcess.setPProcessRef(pProcessRef);
		cProcess.setUserCloudRef(userCloudRef);
		cProcess.setVProcessRef(vProcessRef);

		// TODO XMLGogerian Calender
		cProcess.setStartTime(null);
		cProcess.setEndTime(null);

		this.getRootNode().getInstance().add(cProcess);

		validateAndStoreTraceabilityDocument();

		return id;
	}

	// Not sure this should be implemented directly !!!!!!!!!!!!
	@Override
	public synchronized String transition(String id, String state,
			String country, String latitude, String longitude, String region,
			String event) {

		Transition tr = tModelFactory.createTransition();

		if (id == null || id.equalsIgnoreCase("")) {
			QName trId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "tr"
					+ (++tr_id), UNIQUE_IDENTIFIER_NS_PREFIX);
			tr.setId(trId);
			id = trId.getLocalPart();
		} else {

			QName trId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);

			tr.setId(trId);
		}

		tr.setLatitude(latitude);
		tr.setLongitude(longitude);

		QName trStateId = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX, state,
				UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX);
		tr.setState(trStateId);

		if (region != null && !(id.equalsIgnoreCase(""))) {

			QName trRegionId = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX,
					region, UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX);
			tr.setRegion(trRegionId);
		}

		if (country != null && !(id.equalsIgnoreCase(""))) {

			QName trCountryId = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX,
					country, UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX);
			tr.setCountry(trCountryId);
		}

		if (event != null && (!(id.equalsIgnoreCase("")))) {

			QName trEventId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, event,
					UNIQUE_IDENTIFIER_NS_PREFIX);
			tr.setEvent(trEventId);
		}

		getRootNode().getInstance().add(tr);
		validateAndStoreTraceabilityDocument();

		return id;
	}

	@Override
	public synchronized String wasImplicitlyCalledBy(String id,
			String informed, String informant, String type, String callComm,
			String callMedium, String callNetwork) {

		WasImplicitlyCalledBy wic = tModelFactory.createWasImplicitlyCalledBy();

		if (id == null || id.equalsIgnoreCase("")) {
			QName wicId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "wic"
					+ (++wic_Id), UNIQUE_IDENTIFIER_NS_PREFIX);
			wic.setId(wicId);
			id = wicId.getLocalPart();
		} else {

			QName wicId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);

			wic.setId(wicId);
		}

		ActivityRef aRefInformed = tModelFactory.createActivityRef();
		QName informedRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, informed,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		aRefInformed.setRef(informedRefId);
		wic.setInformed(aRefInformed);

		ActivityRef aRefInfromant = tModelFactory.createActivityRef();
		QName informantRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX,
				informant, UNIQUE_IDENTIFIER_NS_PREFIX);

		aRefInfromant.setRef(informantRefId);
		wic.setInformant(aRefInfromant);

		QName callMediumId = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX,
				callMedium, UNIQUE_IDENTIFIER_NS_PROVD_PREFIX);

		wic.setCallMedium(callMediumId);

		QName callNetworkId = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX,
				callNetwork, UNIQUE_IDENTIFIER_NS_PROVD_PREFIX);

		wic.setCallNetwork(callNetworkId);

		// TODO add values
		wic.setCallComm(null);
		wic.setImplicitType(null);

		getRootNode().getRelation().add(wic);
		validateAndStoreTraceabilityDocument();

		return id;
	}

	@Override
	public synchronized String wasExplicitlyCalledBy(String id,
			String informed, String informant, String type, String callComm,
			String callMedium, String callNetwork) {

		WasExplicitlyCalledBy wec = tModelFactory.createWasExplicitlyCalledBy();

		if (id == null || id.equalsIgnoreCase("")) {
			QName wecId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "wec"
					+ (++wec_Id), UNIQUE_IDENTIFIER_NS_PREFIX);
			wec.setId(wecId);

			id = wecId.getLocalPart();
		} else {

			QName wecId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);
			wec.setId(wecId);
		}

		QName callMediumId = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX,
				callMedium, UNIQUE_IDENTIFIER_NS_PROVD_PREFIX);
		wec.setCallMedium(callMediumId);

		QName callNetworkId = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX,
				callNetwork, UNIQUE_IDENTIFIER_NS_PROVD_PREFIX);
		wec.setCallNetwork(callNetworkId);

		ActivityRef aRefInformed = tModelFactory.createActivityRef();

		QName informedRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, informed,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		aRefInformed.setRef(informedRefId);
		wec.setInformed(aRefInformed);

		ActivityRef aRefInfromant = tModelFactory.createActivityRef();
		QName informantRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX,
				informant, UNIQUE_IDENTIFIER_NS_PREFIX);

		aRefInfromant.setRef(informantRefId);
		wec.setInformant(aRefInfromant);

		// TODO
		wec.setCallComm(null);
		wec.setExplicitType(null);

		getRootNode().getRelation().add(wec);
		validateAndStoreTraceabilityDocument();

		return id;
	}

	@Override
	public synchronized String wasRecurrentlyCalledBy(String id,
			String informed, String informant, String type, String callComm,
			long timeout) {

		WasRecurrentlyCalledBy wrc = tModelFactory
				.createWasRecurrentlyCalledBy();

		if (id == null || id.equalsIgnoreCase("")) {
			QName wrcId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "wrc"
					+ (++wec_Id), UNIQUE_IDENTIFIER_NS_PREFIX);
			wrc.setId(wrcId);
		} else {

			QName wrcId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);
			wrc.setId(wrcId);
			id = wrcId.getLocalPart();
		}

		ActivityRef aRefInformed = tModelFactory.createActivityRef();
		QName informedRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, informed,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		aRefInformed.setRef(informedRefId);
		wrc.setInformed(aRefInformed);

		if (informed.equals(informant)) {
			wrc.setInformant(aRefInformed);
		} else {
			logger.warn("Informer and informant should be the same !! ");

			ActivityRef aRefInfromant = tModelFactory.createActivityRef();
			QName informantRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX,
					informant, UNIQUE_IDENTIFIER_NS_PREFIX);

			aRefInfromant.setRef(informantRefId);
			wrc.setInformant(aRefInfromant);
		}

		getRootNode().getRelation().add(wrc);
		validateAndStoreTraceabilityDocument();

		return id;
	}

	@Override
	public synchronized String hadOwnership(String id, String resource,
			String agent, String ownershipType) {

		HadOwnership ho = tModelFactory.createHadOwnership();

		if (id == null || id.equalsIgnoreCase("")) {
			QName hoId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "ho"
					+ (++ho_Id), UNIQUE_IDENTIFIER_NS_PREFIX);
			ho.setId(hoId);
			id = hoId.getLocalPart();
		} else {

			QName hoId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);

			ho.setId(hoId);
		}

		QName ownershipId = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX,
				ownershipType, UNIQUE_IDENTIFIER_NS_PROVD_PREFIX);
		ho.setOwnershipType(ownershipId);

		QName agentId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, agent,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		AgentRef aRef = tModelFactory.createAgentRef();
		aRef.setRef(agentId);
		ho.setAgent(aRef);

		QName resourceId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, resource,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		EntityRef eRef = tModelFactory.createEntityRef();
		eRef.setRef(resourceId);
		ho.setEntity(eRef);

		getRootNode().getRelation().add(ho);
		validateAndStoreTraceabilityDocument();

		return id;
	}

	@Override
	public synchronized String wasRepresentationOf(String id,
			String generatedResource, String usedResource,
			String processInvolved, String generation, String usage,
			String method) {

		WasRepresentationOf wro = tModelFactory.createWasRepresentationOf();

		if (id == null || id.equalsIgnoreCase("")) {
			QName wroId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "wro"
					+ (++wro_Id), UNIQUE_IDENTIFIER_NS_PREFIX);
			wro.setId(wroId);
			id = wroId.getLocalPart();

		} else {

			QName wroId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);

			wro.setId(wroId);
		}

		if (method != null && (!method.equals(""))) {
			QName methodId = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX,
					method, UNIQUE_IDENTIFIER_NS_PROVD_PREFIX);
			wro.setMethod(methodId);
		}

		QName acRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, processInvolved,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		ActivityRef acRef = tModelFactory.createActivityRef();
		acRef.setRef(acRefId);
		wro.setActivity(acRef);

		QName eRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX,
				generatedResource, UNIQUE_IDENTIFIER_NS_PREFIX);
		EntityRef eRef = tModelFactory.createEntityRef();

		eRef.setRef(eRefId);
		wro.setGeneratedEntity(eRef);

		EntityRef eUsedRef = tModelFactory.createEntityRef();
		QName eRefUsedId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, usedResource,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		eUsedRef.setRef(eRefUsedId);
		wro.setUsedEntity(eUsedRef);

		// TODO - implement this method
		wro.setGeneration(null);

		getRootNode().getRelation().add(wro);
		validateAndStoreTraceabilityDocument();

		return id;
	}

	@Override
	public synchronized String wasReferenceOf(String id,
			String generatedResource, String usedResource,
			String processInvolved, String generation, String usage,
			String method, String type) {

		WasReferenceOf wReo = tModelFactory.createWasReferenceOf();

		if (id == null || id.equalsIgnoreCase("")) {
			QName wReoId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "wreo"
					+ (++wreo_Id), UNIQUE_IDENTIFIER_NS_PREFIX);
			wReo.setId(wReoId);

			id = wReoId.getLocalPart();

		} else {

			QName wReoId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);

			wReo.setId(wReoId);
		}

		QName eRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX,
				generatedResource, UNIQUE_IDENTIFIER_NS_PREFIX);
		EntityRef eRef = tModelFactory.createEntityRef();
		eRef.setRef(eRefId);
		wReo.setGeneratedEntity(eRef);

		QName eRefUsedId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, usedResource,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		EntityRef eUsedRef = tModelFactory.createEntityRef();
		eUsedRef.setRef(eRefUsedId);
		wReo.setUsedEntity(eUsedRef);

		QName acRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, processInvolved,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		ActivityRef acRef = tModelFactory.createActivityRef();
		acRef.setRef(acRefId);
		wReo.setActivity(acRef);

		// TODO - implement this method
		wReo.setGeneration(null);

		// TODO - check if type should be an Id or not
		// ************************************************
		wReo.setRefType(null);

		// return this.marshallObject(wReo);

		getRootNode().getRelation().add(wReo);
		validateAndStoreTraceabilityDocument();

		return id;
	}

	@Override
	public synchronized String wasVirtualizedBy(String id,
			String generatedResource, String processInvolved, Date time,
			String purpose) {

		// TODO - Correct the schema - WasVritu(a)lizedBy -- Missing a
		WasVirtualizedBy wvb = tModelFactory.createWasVirtualizedBy();

		if (id == null || id.equalsIgnoreCase("")) {
			QName wvbId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "wvb"
					+ (++wvb_Id), UNIQUE_IDENTIFIER_NS_PREFIX);
			wvb.setId(wvbId);
			id = wvbId.getLocalPart();
		} else {

			QName wvbId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);
			wvb.setId(wvbId);
		}

		if (purpose != null && (!purpose.equals(""))) {
			QName purposeId = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX,
					purpose, UNIQUE_IDENTIFIER_NS_PROVD_PREFIX);
			wvb.setPurpose(purposeId);
		}

		QName acRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, processInvolved,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		ActivityRef acRef = tModelFactory.createActivityRef();
		acRef.setRef(acRefId);
		wvb.setActivity(acRef);

		QName enRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX,
				generatedResource, UNIQUE_IDENTIFIER_NS_PREFIX);

		EntityRef enRef = tModelFactory.createEntityRef();
		enRef.setRef(enRefId);
		wvb.setEntity(enRef);

		// TODO - Set the XMLGegorian Date
		wvb.setTime(null);

		getRootNode().getRelation().add(wvb);
		validateAndStoreTraceabilityDocument();

		return id;
	}

	/*** Transition relation between an Entity and Transition ******/
	@Override
	public synchronized String hadTransitionState_A(String id,
			String entityInvolved, String stateResource, String method) {

		HadTransitionStateA htsa = tModelFactory.createHadTransitionStateA();

		if (id == null || id.equalsIgnoreCase("")) {
			QName wsbId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "htsa"
					+ (++htsa_Id), UNIQUE_IDENTIFIER_NS_PREFIX);
			htsa.setId(wsbId);
			id = wsbId.getLocalPart();
		} else {

			QName wsbId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);

			htsa.setId(wsbId);
		}

		QName enRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, entityInvolved,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		EntityRef enRef = tModelFactory.createEntityRef();

		enRef.setRef(enRefId);
		htsa.setUsedEntity(enRef);

		QName enGenRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX,
				stateResource, UNIQUE_IDENTIFIER_NS_PREFIX);

		EntityRef enGenRef = tModelFactory.createEntityRef();
		enGenRef.setRef(enGenRefId);
		htsa.setGeneratedEntity(enGenRef);

		// TODO set the XMLGegorian calender ....
		// htsa.setTime(null);

		getRootNode().getRelation().add(htsa);
		validateAndStoreTraceabilityDocument();

		return id;

	}

	/**** Transition relation between an Activity and Transition ****/
	@Override
	public synchronized String hadTransitionState_B(String id,
			String processInvolved, String stateResource, String method) {

		HadTransitionStateB htsb = tModelFactory.createHadTransitionStateB();

		if (id == null || id.equalsIgnoreCase("")) {
			QName wsbId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "htsb"
					+ (++htsb_Id), UNIQUE_IDENTIFIER_NS_PREFIX);
			htsb.setId(wsbId);
			id = wsbId.getLocalPart();
		} else {

			QName wsbId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);

			htsb.setId(wsbId);
		}

		QName acRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, processInvolved,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		ActivityRef acRef = tModelFactory.createActivityRef();
		acRef.setRef(acRefId);
		htsb.setActivity(acRef);

		QName enRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, stateResource,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		EntityRef enRef = tModelFactory.createEntityRef();
		enRef.setRef(enRefId);
		// htsa.setEntity(enRef);
		htsb.setEntity(enRef);

		// TODO set the XMLGegorian calender ....
		// htsa.setTime(null);

		getRootNode().getRelation().add(htsb);
		validateAndStoreTraceabilityDocument();

		return id;

	}

	/*** Transition between an agent and Transition */
	@Override
	public synchronized String hadTransitionState_C(String id,
			String agentInvolved, String stateResource, String method) {

		HadTransitionStateC htsb = tModelFactory.createHadTransitionStateC();

		if (id == null || id.equalsIgnoreCase("")) {
			QName wsbId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "htsc"
					+ (++htsc_Id), UNIQUE_IDENTIFIER_NS_PREFIX);
			htsb.setId(wsbId);
			id = wsbId.getLocalPart();
		} else {

			QName wsbId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);

			htsb.setId(wsbId);
		}

		QName agRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, agentInvolved,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		AgentRef agRef = tModelFactory.createAgentRef();
		agRef.setRef(agRefId);
		htsb.setAgent(agRef);

		QName enRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, stateResource,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		EntityRef enRef = tModelFactory.createEntityRef();
		enRef.setRef(enRefId);
		// htsa.setEntity(enRef);
		htsb.setEntity(enRef);

		// TODO set the XMLGegorian calender ....
		// htsa.setTime(null);

		getRootNode().getRelation().add(htsb);
		validateAndStoreTraceabilityDocument();

		return id;
	}

	// Node and Edge from Prov
	@Override
	public synchronized String used(String id, String processInvolved,
			String generatedResource, Date date) {

		Usage used = tModelFactory.createUsage();

		if (id == null || id.equalsIgnoreCase("")) {
			QName usedId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "u"
					+ (++u_Id), UNIQUE_IDENTIFIER_NS_PREFIX);
			used.setId(usedId);

			id = usedId.getLocalPart();

		} else {

			QName usedId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);

			used.setId(usedId);
		}

		EntityRef genRef = tModelFactory.createEntityRef();

		QName uRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX,
				generatedResource, UNIQUE_IDENTIFIER_NS_PREFIX);
		genRef.setRef(uRefId);

		used.setEntity(genRef);

		ActivityRef acRef = tModelFactory.createActivityRef();

		QName acRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, processInvolved,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		acRef.setRef(acRefId);

		used.setActivity(acRef);

		// getRootNode().getRelation().add(used);

		this.getRootNode().getEntityOrActivityOrWasGeneratedBy().add(used);
		validateAndStoreTraceabilityDocument();

		return id;
	}

	@Override
	public synchronized String wasInitiallyCalledBy(String id,
			String processInvolved, String involvedAgent, String plan,
			String accessMedium, String accessNetwork) {

		WasInitiallyCalledBy wicb = tModelFactory.createWasInitiallyCalledBy();

		if (id == null || id.equalsIgnoreCase("")) {
			QName wicbId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, "wicb"
					+ (++wicb_Id), UNIQUE_IDENTIFIER_NS_PREFIX);
			wicb.setId(wicbId);

			id = wicbId.getLocalPart();

		} else {

			QName wicbId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_PREFIX);

			wicb.setId(wicbId);
		}

		AgentRef agentRef = tModelFactory.createAgentRef();

		QName aRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, involvedAgent,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		agentRef.setRef(aRefId);
		wicb.setAgent(agentRef);

		ActivityRef acRef = tModelFactory.createActivityRef();
		QName acRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, processInvolved,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		acRef.setRef(acRefId);
		wicb.setActivity(acRef);

		QName accessMediumId = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX,
				accessMedium, UNIQUE_IDENTIFIER_NS_PROVD_PREFIX);
		wicb.setAccessMedium(accessMediumId);

		QName accessNetworkId = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX,
				accessNetwork, UNIQUE_IDENTIFIER_NS_PROVD_PREFIX);

		wicb.setAccessNetwork(accessNetworkId);
		getRootNode().getRelation().add(wicb);
		validateAndStoreTraceabilityDocument();

		return id;
	}

	public XMLGregorianCalendar getXMLgregorialDate(Date currentDate)
			throws DatatypeConfigurationException {

		GregorianCalendar myCal = new GregorianCalendar();
		myCal.setTime(currentDate);

		XMLGregorianCalendar xgcal;
		xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(myCal);

		return xgcal;

	}

	public synchronized TraceabilityDocument getRootNode() {

		logger.debug("getting the root node ....");

		if (traceabilityDoc == null) {
			traceabilityDoc = tModelFactory.createTraceabilityDocument();
			return traceabilityDoc;
		} else {
			return traceabilityDoc;
		}
	}

	public synchronized void resetRootNode() {

		logger.debug("getting the root node ....");

		traceabilityDoc = tModelFactory.createTraceabilityDocument();
	}

	/**
	 * Create a traceability root node
	 */
	public void InitilizeTraceabilityDocument() {

		logger.debug("Emptying the Traceability Document ");
		this.setCurrentTraceabilityDocument("");
		notify();
	}

	/**
	 * Check if the root node is empty
	 * 
	 * @return
	 */
	public boolean isTraceabilityDocumentEmpty() {

		if (this.getCurrentTraceabilityDocument() == null
				|| this.getCurrentTraceabilityDocument().equals("")) {

			logger.debug("  Traceability Document is empty: " + traceabilityDoc);
			return true;
		} else {
			logger.debug("  Traceability Document is NOT empty");
			return false;
		}

	}

	public String getCurrentTraceabilityDocument() {

		return currentTraceabilityDocument;
	}

	public synchronized void setCurrentTraceabilityDocument(
			String currentTraceabilityDocument) {

		this.currentTraceabilityDocument = currentTraceabilityDocument;

	}

	private void validateAndStoreTraceabilityDocument() {

		if (currentGraphSize == MAX_SIZE_GRAPH) {
			String traceabilityOutput = traceabilityEventProducer
					.sendEvent(getRootNode());

			logger.info("Traceability storage outcome: " + traceabilityOutput);

			// callback method
			traceabilityStored();

			resetRootNode();
			currentGraphSize = 1;
			//
		} else {

			currentGraphSize++;
		}
	}

	@Override
	public void setMaxStatementPerDocument(int max) {

		MAX_SIZE_GRAPH = max;

	}

	@Override
	public String traceabilityStored() {
		return "traceabilityStoredSuccessfully";
	}
}
