/*
 * @(#)DenyOverridesRuleAlg.java
 *
 * Copyright 2003-2005 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistribution of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *   2. Redistribution in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for use in
 * the design, construction, operation or maintenance of any nuclear facility.
 */

package org.wso2.balana.combine.xacml2;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.wso2.balana.ObligationResult;
import org.wso2.balana.Rule;
import org.wso2.balana.combine.RuleCombinerElement;
import org.wso2.balana.combine.RuleCombiningAlgorithm;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.ctx.ResultFactory;
import org.wso2.balana.xacml3.Advice;

/**
 * This is the standard Deny Overrides rule combining algorithm. It allows a
 * single evaluation of Deny to take precedence over any number of permit, not
 * applicable or indeterminate results. Note that since this implementation does
 * an ordered evaluation, this class also supports the Ordered Deny Overrides
 * algorithm.
 * 
 * @since 1.0
 * @author Seth Proctor
 */

@SuppressWarnings({ "rawtypes"})
public class DenyOverridesRuleAlg extends RuleCombiningAlgorithm {

	/**
	 * The standard URN used to identify this algorithm
	 */
	public static final String algId = "urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:"
			+ "deny-overrides";

	// a URI form of the identifier
	private static URI identifierURI;
	// exception if the URI was invalid, which should never be a problem
	private static RuntimeException earlyException;

	static {
		try {
			identifierURI = new URI(algId);
		} catch (URISyntaxException se) {
			earlyException = new IllegalArgumentException();
			earlyException.initCause(se);
		}
	}

	/**
	 * Standard constructor.
	 */
	public DenyOverridesRuleAlg() {
		super(identifierURI);

		if (earlyException != null)
			throw earlyException;
	}

	/**
	 * Protected constructor used by the ordered version of this algorithm.
	 * 
	 * @param identifier
	 *            the algorithm's identifier
	 */
	protected DenyOverridesRuleAlg(URI identifier) {
		super(identifier);
	}

	/**
	 * Applies the combining rule to the set of rules based on the evaluation
	 * context.
	 * 
	 * @param context
	 *            the context from the request
	 * @param parameters
	 *            a (possibly empty) non-null <code>List</code> of
	 *            <code>CombinerParameter<code>s
	 * @param ruleElements
	 *            the rules to combine
	 * 
	 * @return the result of running the combining algorithm
	 */
	public AbstractResult combine(EvaluationCtx context, List parameters,
			List ruleElements) {

		boolean atLeastOneError = false;
		boolean potentialDeny = false;
		boolean atLeastOnePermit = false;
		AbstractResult firstIndeterminateResult = null;
		List<ObligationResult> permitObligations = new ArrayList<ObligationResult>();
		List<Advice> permitAdvices = new ArrayList<Advice>();
		Iterator it = ruleElements.iterator();

		while (it.hasNext()) {
			Rule rule = ((RuleCombinerElement) (it.next())).getRule();
			AbstractResult result = rule.evaluate(context);
			int value = result.getDecision();

			// if there was a value of DENY, then regardless of what else
			// we've seen, we always return DENY
			if (value == AbstractResult.DECISION_DENY) { // TODO -- i changed
				return result;
			}
			// if it was INDETERMINATE, then we couldn't figure something
			// out, so we keep track of these cases...
			if (value == AbstractResult.DECISION_INDETERMINATE
					|| value == AbstractResult.DECISION_INDETERMINATE_DENY
					|| value == AbstractResult.DECISION_INDETERMINATE_PERMIT
					|| value == AbstractResult.DECISION_INDETERMINATE_DENY_OR_PERMIT) {

				atLeastOneError = true;

				// there are no rules about what to do if multiple cases
				// cause errors, so we'll just return the first one
				if (firstIndeterminateResult == null) {
					firstIndeterminateResult = result;
				}
				// if the Rule's effect is DENY, then we can't let this
				// alg return PERMIT, since this Rule might have denied
				// if it could do its stuff
				if (rule.getEffect() == AbstractResult.DECISION_DENY) {
					potentialDeny = true;
				}
			} else {
				// keep track of whether we had at least one rule that
				// actually pertained to the request
				if (value == AbstractResult.DECISION_PERMIT) {
					atLeastOnePermit = true;
					permitAdvices.addAll(result.getAdvices());
					permitObligations.addAll(result.getObligations());
				}

			}
		}

		// we didn't explicitly DENY, but we might have had some Rule
		// been evaluated, so we have to return INDETERMINATE
		if (potentialDeny) {
			return firstIndeterminateResult;
		}
		// some Rule said PERMIT, so since nothing could have denied,
		// we return PERMIT
		if (atLeastOnePermit) {
			return ResultFactory.getFactory().getResult(
					AbstractResult.DECISION_PERMIT, permitObligations,
					permitAdvices, context);
		}

		// we didn't find anything that said PERMIT, but if we had a
		// problem with one of the Rules, then we're INDETERMINATE
		if (atLeastOneError) {
			return firstIndeterminateResult;
		}
		// if we hit this point, then none of the rules actually applied
		// to us, so we return NOT_APPLICABLE
		// return new Result(Result.DECISION_NOT_APPLICABLE,
		// context.getResourceId().encode());
		return ResultFactory.getFactory().getResult(
				AbstractResult.DECISION_NOT_APPLICABLE, context);
	}

}
