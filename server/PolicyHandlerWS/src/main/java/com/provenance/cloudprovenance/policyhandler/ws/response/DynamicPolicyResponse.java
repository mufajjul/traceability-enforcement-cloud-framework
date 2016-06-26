/**
 * @file 		DynamicPolicyResponse.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		PolicyHandlerWS
 * @date 		18 05 2013
 * @version 	1.0
 */

package com.provenance.cloudprovenance.policyhandler.ws.response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.provenance.cloudprovenance.converter.resource.TraceabilityConverter;
import com.provenance.cloudprovenance.sconverter.mapper.CprovObjectTraceability;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.ObjectFactory;
import com.provenance.cloudprovenance.traceabilityLanguage.generated.PolicyResponse;
import com.provenance.cloudprovenance.traceabilitystore.ns.CprovNamespacePrefixMapper;

/**
* REST implementations for compliance response
* 
* @author Mufy
* 
*/
public class DynamicPolicyResponse implements ComplianceResponse<PolicyResponse>{

	@Autowired
	CprovNamespacePrefixMapper cMapper;
	@Autowired
	private TraceabilityConverter trConverter;

	
	private ObjectFactory pFactory = new ObjectFactory();
	private  String UNIQUE_IDENTIFIER_NS_SERVICE_SUFFIX;
	private  String UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX;
	
	//private EventProducer<PolicyRequest> eventProducer;//  = "com.provenance.cloudprovenance.pap.cprovl_policystore.generated";
	private static Logger logger = Logger.getLogger("DynamicPolicyResponse");

	
	public DynamicPolicyResponse() {
	}
	
	public DynamicPolicyResponse(String prefix, String suffix) {

		UNIQUE_IDENTIFIER_NS_SERVICE_SUFFIX = suffix;
		UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX = prefix+":";
	}
	
	public PolicyResponse constructResponse(String serviceName, String policyId, String requestId, String responseId, String responseValue){

		CprovObjectTraceability obTr = new CprovObjectTraceability(serviceName,
				cMapper.getNsSuffixCOnfidenshare());
		PolicyResponse pResponse = pFactory.createPolicyResponse();

		return null;
		
		/*
		// create a policy 
		if (policyId != null) {
			pResponse.setAgent(constructAgent(policyId, true,
					pFactory));
		}

		// create cResource --> responseId
		//CloudResource crRequest = obTr.cResource(requestId, "request-identification", "=bob[=]!:uuid:f81d4fae",
			//	"[=]!:pid:g81d4fde-000email", "pid:00:12:00:12:100:11:00:00",
				//true, null, null, "general", 1.0f);

		//pResponse.set;
		
		// pRespons
		
		//pResponse.
		
		
		
		if (requestId != null) {

			String fieldType = "Resource";

			String prefix = "ex:";

			if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {
				prefix = UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX;
			}

			REntity prosEntity = constructField(resourceId, true,
					fieldType, prefix + resourceId, pFactory, null);
			pRequest.getEntity().add(prosEntity);
		}

		if (processId != null) {

			String fieldType = "Action";
			String prefix = "ex:";

			if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {
				prefix = UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX;
			}

			REntity prosEntity = constructField(processId, true,
					fieldType, prefix + processId, pFactory, null);
			pRequest.getEntity().add(prosEntity);

		}
		
		if (environmentId != null) {

			String fieldType = "Environment";
			String prefix = "ex:";

			if (UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX != null) {
				prefix = UNIQUE_IDENTIFIER_NS_SERVICE_PREFIX;
			}

			REntity prosEntity = constructField(environmentId, true,
					fieldType, prefix + environmentId, pFactory, null);
			pRequest.getEntity().add(prosEntity);

		}
		
		 eventProducer.sendEvent(pRequest);
		 
		 //TODO - event consumer for the response .... Synchrnized mode .....
		 return "request sent successfully";
	}
	
	public String constructRequest(String requestUserId,
			Boolean requestUserIdRef, String resourceId, Boolean resourceIdRef,
			String resourceDesc, String processId, Boolean processIdRef,
			String processDesc) {

		PolicyRequest pRequest = pFactory.createPolicyRequest();

		if (requestUserId != null) {

			pRequest.setAgent(constructAgent(requestUserId, requestUserIdRef,
					pFactory));
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
		 eventProducer.sendEvent(pRequest);
		 
		 //TODO - event consumer for the response .... Synchrnized mode .....
		 return "request sent successfully";
	}

	private Agent constructAgent(String requestUserId,
			Boolean requestUserIdRef, ObjectFactory pFactory) {

		// TODO - Agent should really have description
		Agent srcAgent = pFactory.createPolicyResponseAgent();

		if (requestUserIdRef != null) {
			
			//srcAgent.s
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
		
		RsEntity respEntity =  pFactory.createRsEntity();

		//pFactory.createc
		
		
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
		resEntity.setReqField(reqFieldType);

		FieldValue fValue = new REntity.FieldValue();

		if (fieldRef != null) {
			fValue.setIsRef(fieldRef.booleanValue());
		} else {
			fValue.setIsRef(true);
		}

		resEntity.setFieldValue(fValue);
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
		
		*/
	}

	@Override
	public PolicyResponse constructResponse(String policyId, String requestId,
			String responseId, String responseValue) {
		// TODO Auto-generated method stub
		return null;
	}

}
