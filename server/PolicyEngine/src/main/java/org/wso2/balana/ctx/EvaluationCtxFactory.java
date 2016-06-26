/*
 *  Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.balana.ctx;

import org.apache.log4j.Logger;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.ParsingException;
import org.wso2.balana.XACMLConstants;
import org.wso2.balana.ctx.xacml2.XACML2EvaluationCtx;
import org.wso2.balana.ctx.xacml3.RequestCtx;
import org.wso2.balana.ctx.xacml3.XACML3EvaluationCtx;

/**
 * Factory that creates the EvaluationCtx
 */
public class EvaluationCtxFactory {

	/**
	 * factory instance
	 */
	private static volatile EvaluationCtxFactory factoryInstance;
	
	private static Logger logger = Logger.getLogger(EvaluationCtxFactory.class);


	public EvaluationCtx getEvaluationCtx(AbstractRequestCtx requestCtx,
			PDPConfig pdpConfig) throws ParsingException {

		logger.debug(EvaluationCtxFactory.class.getName()
				+ "  Evaluating the expression ");

		if (XACMLConstants.XACML_VERSION_3_0 == requestCtx.getXacmlVersion()) {
			logger.debug(EvaluationCtxFactory.class.getName()
					+ "  Evaluating Version 3.0 ");

			return new XACML3EvaluationCtx((RequestCtx) requestCtx, pdpConfig);
		} else {
			logger.info(EvaluationCtxFactory.class.getName()
					+ "  Evaluating the expression 2.0");

			return new XACML2EvaluationCtx(
					(org.wso2.balana.ctx.xacml2.RequestCtx) requestCtx,
					pdpConfig);
		}
	}

	/**
	 * Overridden to allow access to the provenance store
	 * @param requestCtx
	 * @param pdpConfig
	 * @return
	 * @throws ParsingException
	 */
	public EvaluationCtx getEvaluationCtx(AbstractRequestCtx requestCtx,
			PDPConfig pdpConfig, String serviceId) throws ParsingException {

		logger.info(EvaluationCtxFactory.class.getName()
				+ "  Evaluating the expression ");

		if (XACMLConstants.XACML_VERSION_3_0 == requestCtx.getXacmlVersion()) {
			logger.info(EvaluationCtxFactory.class.getName()
					+ "  Evaluating Version 3.0 ");

			return new XACML3EvaluationCtx((RequestCtx) requestCtx, pdpConfig, serviceId);
		} else {
			logger.info(EvaluationCtxFactory.class.getName()
					+ "  Evaluating the expression 2.0");

			return new XACML2EvaluationCtx(
					(org.wso2.balana.ctx.xacml2.RequestCtx) requestCtx,
					pdpConfig, serviceId);
		}
	}

	
	

	/**
	 * Returns an instance of this factory. This method enforces a singleton
	 * model, meaning that this always returns the same instance, creating the
	 * factory if it hasn't been requested before.
	 * 
	 * @return the factory instance
	 */
	public static EvaluationCtxFactory getFactory() {
		if (factoryInstance == null) {
			synchronized (EvaluationCtxFactory.class) {
				if (factoryInstance == null) {
					factoryInstance = new EvaluationCtxFactory();
				}
			}
		}

		return factoryInstance;
	}

}
