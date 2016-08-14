/*
 * @(#) CprovlAttributeFinderModule.java       1.1 13/8/2016
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.wso2.balana.attr.AttributeValue;
import org.wso2.balana.attr.BagAttribute;
import org.wso2.balana.attr.StringAttribute;
import org.wso2.balana.cond.EvaluationResult;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.finder.AttributeFinderModule;

/**
 * This class is used to find custom attributes id
 * 
 * NOTE - Not yet used
 * 
 * @version 1.1 13 Aug 2016
 * @author Mufy
 * @Module PolicyEngine
 */
@Deprecated
public class CprovlAttributeFinderModule extends AttributeFinderModule {

	private URI defaultServiceId;

	public CprovlAttributeFinderModule() {
		try {
			defaultServiceId = new URI(
					"urn:com:provenance:cloudprovenance:cprovl:1.0:service:service-id");
		} catch (URISyntaxException e) {
			// ignore
			e.toString();
		}
	}

	@Override
	public Set<String> getSupportedCategories() {
		Set<String> categories = new HashSet<String>();
		categories
				.add("urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:service");
		return categories;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Set getSupportedIds() {
		Set<String> ids = new HashSet<String>();
		ids.add("http://kmarket.com/id/role");
		return ids;
	}

	@Override
	public EvaluationResult findAttribute(URI attributeType, URI attributeId,
			String issuer, URI category, EvaluationCtx context) {
		String roleName = null;
		List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();

		EvaluationResult result = context.getAttribute(attributeType,
				defaultServiceId, issuer, category);
		if (result != null && result.getAttributeValue() != null
				&& result.getAttributeValue().isBag()) {
			BagAttribute bagAttribute = (BagAttribute) result
					.getAttributeValue();
			if (bagAttribute.size() > 0) {
				String userName = ((AttributeValue) bagAttribute.iterator()
						.next()).encode();
				roleName = findRole(userName);
			}
		}

		if (roleName != null) {
			attributeValues.add(new StringAttribute(roleName));
		}

		return new EvaluationResult(new BagAttribute(attributeType,
				attributeValues));
	}

	@Override
	public boolean isDesignatorSupported() {
		return true;
	}

	private String findRole(String userName) {
		if (userName.equals("bob")) {
			return "blue";
		} else if (userName.equals("alice")) {
			return "silver";
		} else if (userName.equals("peter")) {
			return "gold";
		}

		return null;
	}
}
