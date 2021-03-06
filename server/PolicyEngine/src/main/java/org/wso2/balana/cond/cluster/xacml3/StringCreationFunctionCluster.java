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

package org.wso2.balana.cond.cluster.xacml3;

import java.util.HashSet;
import java.util.Set;

import org.wso2.balana.cond.Function;
import org.wso2.balana.cond.cluster.FunctionCluster;
import org.wso2.balana.cond.xacml3.StringCreationFunction;

/**
 * Clusters all the functions supported by <code>StringCreationFunction</code>
 */
public class StringCreationFunctionCluster implements FunctionCluster {

	public Set<Function> getSupportedFunctions() {

		Set<Function> set = new HashSet<Function>();
		for (String identifier : StringCreationFunction
				.getSupportedIdentifiers()) {
			set.add(new StringCreationFunction(identifier));
		}
		return set;
	}
}