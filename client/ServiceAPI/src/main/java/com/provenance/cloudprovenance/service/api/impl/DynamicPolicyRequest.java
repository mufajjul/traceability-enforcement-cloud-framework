/**
 * @file 		DynamicPolicyRequest.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		ServiceAPI
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.service.api.impl;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import com.provenance.cloudprovenance.eventhandler.service.EventProducer;
import com.provenance.cloudprovenance.service.policy.api.ServiceCompliance;
import com.provenance.cloudprovenance.service.traceability.api.ServiceTraceabilityCallback;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.AgentRef;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.EntityRef;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.HadOwnership;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.ObjectFactory;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.PolicyRequest;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.PolicyRequest.Agent;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.REntity;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.REntity.CategoryValue;

/**
 * @author Mufy
 * 
 */
public class DynamicPolicyRequest implements ServiceCompliance<String> {

	public final String UNIQUE_IDENTIFIER_NS_SUFFIX = "http://labs.orange.com/uk/ex#";
	public final String UNIQUE_IDENTIFIER_NS_PREFIX = "ex";

	public final String UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX = "http://labs.orange.com/uk/cprovd#";
	public final String UNIQUE_IDENTIFIER_NS_PROVD_PREFIX = "provd";

	public final String UNIQUE_IDENTIFIER_NS_PROV_SUFFIX = "http://www.w3.org/ns/prov#";
	public final String UNIQUE_IDENTIFIER_NS_PROV_PREFIX = "prov";

	private ObjectFactory pFactory = new ObjectFactory();
	private String UNIQUE_IDENTIFIER_NS_SERVICE_SUFFIX;
	private String UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX;

	private String policyResponse = null;

	private EventProducer<PolicyRequest> eventProducer;// =
														// "com.provenance.cloudprovenance.pap.cprovl_policystore.generated";
	private static Logger logger = Logger.getLogger("DynamicPolicyRequest");
	
	ServiceTraceabilityCallback<String> stCallback;

	public DynamicPolicyRequest(EventProducer<PolicyRequest> eventProducer) {

		this.eventProducer = eventProducer;
	}

	public DynamicPolicyRequest(EventProducer<PolicyRequest> eventProducer, ServiceTraceabilityCallback<String> stCallback) {

		this.eventProducer = eventProducer;
		this.stCallback = stCallback; 
		
	}
	
	public DynamicPolicyRequest(EventProducer<PolicyRequest> eventProducer,
			String prefix, String suffix) {

		// NOTE - need to add the colon, explore why
		this.eventProducer = eventProducer;
		UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX = prefix + ":";
		UNIQUE_IDENTIFIER_NS_SERVICE_SUFFIX = suffix;
	}

	public String constructRequest(String requestUserId, String resourceId,
			String processId, String environmentId) {

		PolicyRequest pRequest = pFactory.createPolicyRequest();

		if (requestUserId != null) {

			pRequest.getAgent().add(
					constructAgent(requestUserId, true, pFactory));
		}

		if (resourceId != null) {

			String fieldType = "Resource";

			String prefix = "ex:";

			if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {
				prefix = UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX;
			}

			// TODO - Look into where the ID will different from the value

			REntity prosEntity = constructField(resourceId, false, fieldType,
					prefix + resourceId, pFactory, null);
			pRequest.getEntity().add(prosEntity);
		}

		if (processId != null) {

			String fieldType = "Action";
			String prefix = "ex:";

			if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {
				prefix = UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX;
			}

			REntity prosEntity = constructField(processId, false, fieldType,
					prefix + processId, pFactory, null);
			pRequest.getEntity().add(prosEntity);

		}

		if (environmentId != null) {

			String fieldType = "Environment";
			String prefix = "ex:";

			if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {
				prefix = UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX;
			}

			REntity prosEntity = constructField(environmentId, false,
					fieldType, prefix + environmentId, pFactory, null);
			pRequest.getEntity().add(prosEntity);

		}
		policyResponse = eventProducer.sendEvent(pRequest);
		return policyResponse;

		// TODO - event consumer for the response .... Synchrnized mode .....
		// return "request sent successfully";
	}

	public String constructRequest(String requestUserId,
			Boolean requestUserIdRef, String resourceId, Boolean resourceIdRef,
			String resourceDesc, String processId, Boolean processIdRef,
			String processDesc) {

		PolicyRequest pRequest = pFactory.createPolicyRequest();

		if (requestUserId != null) {

			pRequest.getAgent().add(
					constructAgent(requestUserId, requestUserIdRef, pFactory));
		}

		if (resourceId != null) {

			String fieldType = "Resource";

			String prefix = "ex:";

			if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {
				prefix = UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX;
			}

			// TODO - Look into where the ID will different from the value

			REntity prosEntity = constructField(resourceId, resourceIdRef,
					fieldType, prefix + resourceId, pFactory, resourceDesc);
			pRequest.getEntity().add(prosEntity);
		}

		if (processId != null) {

			String fieldType = "Action";
			String prefix = "ex:";

			if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {
				prefix = UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX;
			}

			REntity prosEntity = constructField(processId, processIdRef,
					fieldType, prefix + processId, pFactory, processDesc);
			pRequest.getEntity().add(prosEntity);

		}
		return eventProducer.sendEvent(pRequest);

		// TODO - event consumer for the response .... Synchrnized mode .....
		// return "request sent successfully";
	}

	private Agent constructAgent(String requestUserId,
			Boolean requestUserIdRef, ObjectFactory pFactory) {

		// TODO - Agent should really have description
		Agent srcAgent = pFactory.createPolicyRequestAgent();

		if (requestUserIdRef != null) {

			// srcAgent.s
			srcAgent.setIsRef(requestUserIdRef.booleanValue());
		} else {
			srcAgent.setIsRef(true);
		}
		AgentRef reAgRef = pFactory.createAgentRef();

		QName srcAgeRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX,
				requestUserId, UNIQUE_IDENTIFIER_NS_PREFIX);

		if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {

			srcAgeRefId = new QName(UNIQUE_IDENTIFIER_NS_SERVICE_SUFFIX,
					requestUserId, UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX);

		}

		srcAgent.setId(srcAgeRefId);

		reAgRef.setRef(srcAgeRefId);
		// pRequest.setAgent(srcAgent);

		return srcAgent;
	}

	private REntity constructField(String fieldId, Boolean fieldRef,
			String fieldType, String fieldValue, ObjectFactory pFactory,
			String description) {

		REntity resEntity = pFactory.createREntity();

		QName srcAgeRefId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, fieldId,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {
			srcAgeRefId = new QName(UNIQUE_IDENTIFIER_NS_SERVICE_SUFFIX,
					fieldId, UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX);
		}

		resEntity.setId(srcAgeRefId);
		// resEntity.setId(null);

		QName reqFieldType = new QName(UNIQUE_IDENTIFIER_NS_PROVD_SUFFIX,
				fieldType, UNIQUE_IDENTIFIER_NS_PROVD_PREFIX);
		/*
		 * if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {
		 * 
		 * reqFieldType = new QName(UNIQUE_IDENTIFIER_NS_SERVICE_SUFFIX,
		 * fieldType, UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX); }
		 */
		resEntity.setCategoryType(reqFieldType);

		CategoryValue fValue = new REntity.CategoryValue();

		if (fieldRef != null) {
			fValue.setIsRef(fieldRef.booleanValue());
		} else {
			fValue.setIsRef(true);
		}

		resEntity.setCategoryValue(fValue);
		fValue.setValue(fieldValue);
		resEntity.setDescription(null);

		return resEntity;

	}

	// TODO: Do the implementation
	public HadOwnership getOwnership(String id, QName entityId, QName agentId,
			String ownershipType) {

		ObjectFactory pFactory = new ObjectFactory();

		HadOwnership ho = pFactory.createHadOwnership();

		QName hoId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX, id,
				UNIQUE_IDENTIFIER_NS_PREFIX);

		if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {

			hoId = new QName(UNIQUE_IDENTIFIER_NS_SERVICE_SUFFIX, id,
					UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX);
		}

		ho.setId(hoId);

		QName ownershipId = new QName(UNIQUE_IDENTIFIER_NS_SUFFIX,
				ownershipType, UNIQUE_IDENTIFIER_NS_PREFIX);

		if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {

			ownershipId = new QName(UNIQUE_IDENTIFIER_NS_SERVICE_SUFFIX,
					ownershipType, UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX);

		}

		ho.setOwnershipType(ownershipId);

		AgentRef aRef = pFactory.createAgentRef();
		aRef.setRef(agentId);
		ho.setAgent(aRef);

		EntityRef eRef = pFactory.createEntityRef();
		eRef.setRef(entityId);
		ho.setEntity(eRef);

		return ho;
	}

	@Override
	public String getResponse() {
		return policyResponse;
	}

	@Override
	public String constructRequest(String[] requestUserIds,
			String[] resourceIds, String[] processIds, String environmentId) {

		PolicyRequest pRequest = pFactory.createPolicyRequest();

		if (requestUserIds != null) {

			for (int i = 0; i < requestUserIds.length; i++)

				pRequest.getAgent().add(
						constructAgent(requestUserIds[i], false, pFactory));
		}

		if (resourceIds != null) {

			String fieldType = "Resource";

			String prefix = "ex:";

			if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {
				prefix = UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX;
			}

			// TODO - Look into where the ID will different from the value

			for (int i = 0; i < resourceIds.length; i++) {
				REntity prosEntity = constructField(resourceIds[i], true,
						fieldType, prefix + resourceIds[i], pFactory, null);
				pRequest.getEntity().add(prosEntity);
			}

		}

		if (processIds != null) {

			String fieldType = "Action";
			String prefix = "ex:";

			if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {
				prefix = UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX;
			}

			for (int i = 0; i < processIds.length; i++) {
				REntity prosEntity = constructField(processIds[i], true,
						fieldType, prefix + processIds[i], pFactory, null);
				pRequest.getEntity().add(prosEntity);
			}
		}

		if (environmentId != null) {

			String fieldType = "Environment";
			String prefix = "ex:";

			if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {
				prefix = UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX;
			}

			REntity prosEntity = constructField(environmentId, false,
					fieldType, prefix + environmentId, pFactory, null);
			pRequest.getEntity().add(prosEntity);

		}

		if (stCallback != null){
			// wait for the traceability storage to be completed 
			stCallback.traceabilityStored();
			
		}
		return eventProducer.sendEvent(pRequest);

	}
}
