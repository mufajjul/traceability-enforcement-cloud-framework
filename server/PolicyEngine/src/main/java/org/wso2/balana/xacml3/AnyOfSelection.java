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

package org.wso2.balana.xacml3;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wso2.balana.DOMHelper;
import org.wso2.balana.Indenter;
import org.wso2.balana.MatchResult;
import org.wso2.balana.ParsingException;
import org.wso2.balana.PolicyMetaData;
import org.wso2.balana.TargetMatch;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.ctx.Status;

/**
 * Represents AnyOfType in the XACML 3.0 policy schema..
 */

@SuppressWarnings({ "rawtypes" })

public class AnyOfSelection {

	/**
     *
     */
	private List<AllOfSelection> allOfSelections;

	private static Logger logger = Logger.getLogger(AnyOfSelection.class);

	/**
	 * Constructor that creates a new <code>AnyOfSelection</code> based on the
	 * given elements.
	 * 
	 * @param allOfSelections
	 *            a <code>List</code> of <code>AllOfSelection</code> elements
	 */
	public AnyOfSelection(List<AllOfSelection> allOfSelections) {
		if (allOfSelections == null)
			this.allOfSelections = new ArrayList<AllOfSelection>();
		else
			this.allOfSelections = new ArrayList<AllOfSelection>(
					allOfSelections);
	}

	/**
	 * creates a <code>AnyOfSelection</code> based on its DOM node.
	 * 
	 * @param root
	 *            the node to parse for the AnyOfSelection
	 * @param metaData
	 *            meta-date associated with the policy
	 * 
	 * @return a new <code>AnyOfSelection</code> constructed by parsing
	 * 
	 * @throws ParsingException
	 *             if the DOM node is invalid
	 */
	public static AnyOfSelection getInstance(Node root, PolicyMetaData metaData)
			throws ParsingException {
		List<AllOfSelection> allOfSelections = new ArrayList<AllOfSelection>();
		NodeList children = root.getChildNodes();

		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if ("AllOf".equals(DOMHelper.getLocalName(child))) {
				allOfSelections
						.add(AllOfSelection.getInstance(child, metaData));
			}
		}

		if (allOfSelections.isEmpty()) {
			throw new ParsingException("AnyOf must contain at least one AllOf");
		}

		return new AnyOfSelection(allOfSelections);
	}

	/**
	 * Determines whether this <code>AnyOfSelection</code> matches the input
	 * request (whether it is applicable).
	 * 
	 * @param context
	 *            the representation of the request
	 * 
	 * @return the result of trying to match the group with the context
	 */
	public MatchResult match(EvaluationCtx context) {

		logger.debug ("Any of the matched Selection");
		
		// if we apply to anything, then we always match
		// if (allOfSelections.isEmpty()) TODO
		// return new MatchResult(MatchResult.MATCH);

		// there are specific matching elements, so prepare to iterate
		// through the list
		Iterator it = allOfSelections.iterator();
		Status firstIndeterminateStatus = null;

		// in order for this section to match, one of the groups must match
		while (it.hasNext()) {
			// get the next group and try matching it
			AllOfSelection group = (AllOfSelection) (it.next());
			MatchResult result = group.match(context);

			// we only need one match, so if this matched, then we're done
			if (result.getResult() == MatchResult.MATCH) {
				return result;
			}
			// if we didn't match then it was either a NO_MATCH or
			// INDETERMINATE...in the second case, we need to remember
			// it happened, 'cause if we don't get a MATCH, then we'll
			// be returning INDETERMINATE
			if (result.getResult() == MatchResult.INDETERMINATE) {
				if (firstIndeterminateStatus == null)
					firstIndeterminateStatus = result.getStatus();
			}
		}

		// if we got here, then none of the sub-matches passed, so
		// we have to see if we got any INDETERMINATE cases
		if (firstIndeterminateStatus == null) {
			return new MatchResult(MatchResult.NO_MATCH);
		} else {
			return new MatchResult(MatchResult.INDETERMINATE,
					firstIndeterminateStatus);
		}
	}

	/**
	 * Encodes this <code>AnyOfSelection</code> into its XML representation and
	 * writes this encoding to the given <code>OutputStream</code> with no
	 * indentation.
	 * 
	 * @param output
	 *            a stream into which the XML-encoded data is written
	 */
	public void encode(OutputStream output) {
		encode(output, new Indenter(0));
	}

	/**
	 * Encodes this <code>AnyOfSelection</code> into its XML representation and
	 * writes this encoding to the given <code>OutputStream</code> with
	 * indentation.
	 * 
	 * @param output
	 *            a stream into which the XML-encoded data is written
	 * @param indenter
	 *            an object that creates indentation strings
	 */
	public void encode(OutputStream output, Indenter indenter) {
		PrintStream out = new PrintStream(output);
		String indent = indenter.makeString();
		Iterator it = allOfSelections.iterator();
		String name = "Match";

		out.println(indent + "<" + name + ">");
		indenter.in();

		while (it.hasNext()) {
			TargetMatch tm = (TargetMatch) (it.next());
			tm.encode(output, indenter);
		}

		out.println(indent + "</" + name + ">");
		indenter.out();
	}

}
