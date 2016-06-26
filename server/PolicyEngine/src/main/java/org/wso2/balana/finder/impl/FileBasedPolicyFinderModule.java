/*
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

package org.wso2.balana.finder.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wso2.balana.AbstractPolicy;
import org.wso2.balana.DOMHelper;
import org.wso2.balana.MatchResult;
import org.wso2.balana.Policy;
import org.wso2.balana.PolicyMetaData;
import org.wso2.balana.PolicyReference;
import org.wso2.balana.PolicySet;
import org.wso2.balana.VersionConstraints;
import org.wso2.balana.combine.PolicyCombiningAlgorithm;
import org.wso2.balana.combine.xacml2.DenyOverridesPolicyAlg;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.ctx.Status;
import org.wso2.balana.finder.PolicyFinder;
import org.wso2.balana.finder.PolicyFinderModule;
import org.wso2.balana.finder.PolicyFinderResult;

/**
 * This is file based policy repository. Policies can be inside the directory in
 * a file system. Then you can set directory location using
 * "org.wso2.balana.PolicyDirectory" JAVA property
 */


@SuppressWarnings({"unused", "rawtypes"})
public class FileBasedPolicyFinderModule extends PolicyFinderModule {

	private PolicyFinder finder = null;

	private Map<URI, AbstractPolicy> policies;

	private Set<String> policyLocations;

	private PolicyCombiningAlgorithm combiningAlg;

	// Temp storage of the policy file locations.

	private static ArrayList<String> retainedPolicyLocations = new ArrayList<String>();

	/**
	 * the logger we'll use for all messages
	 */
	private static Logger log = Logger
			.getLogger(FileBasedPolicyFinderModule.class);

	public static final String POLICY_DIR_PROPERTY = "org.wso2.balana.PolicyDirectory";

	public FileBasedPolicyFinderModule() {
		policies = new HashMap<URI, AbstractPolicy>();

		log.debug("Policy directory property == > "
				+ System.getProperty(POLICY_DIR_PROPERTY));
		if (System.getProperty(POLICY_DIR_PROPERTY) != null) {
			policyLocations = new HashSet<String>();
			policyLocations.add(System.getProperty(POLICY_DIR_PROPERTY));

			log.info("adding system property for policy location ==> "
					+ POLICY_DIR_PROPERTY);

		}
	}

	public FileBasedPolicyFinderModule(Set<String> policyLocations) {
		policies = new HashMap<URI, AbstractPolicy>();
		this.policyLocations = policyLocations;
	}

	@Override
	public void init(PolicyFinder finder) {

		this.finder = finder;
		loadPolicies();
		combiningAlg = new DenyOverridesPolicyAlg();
	}

	@Override
	public PolicyFinderResult findPolicy(EvaluationCtx context) {

		ArrayList<AbstractPolicy> selectedPolicies = new ArrayList<AbstractPolicy>();
		Set<Map.Entry<URI, AbstractPolicy>> entrySet = policies.entrySet();

		log.debug("policy size ==> " + policies.size());

		// iterate through all the policies we currently have loaded
		for (Map.Entry<URI, AbstractPolicy> entry : entrySet) {

			AbstractPolicy policy = entry.getValue();
			MatchResult match = policy.match(context);
			int result = match.getResult();

			// if target matching was indeterminate, then return the error

			log.debug("Match result value ==> " + result + " Match result ==> "+  MatchResult.MATCH);

			if (result == MatchResult.INDETERMINATE)
				return new PolicyFinderResult(match.getStatus());

			// see if the target matched
			if (result == MatchResult.MATCH) {
				log.info("Match result is successful");
				if ((combiningAlg == null) && (selectedPolicies.size() > 0)) {
					
					// we found a match before, so this is an error
					ArrayList<String> code = new ArrayList<String>();
					code.add(Status.STATUS_PROCESSING_ERROR);
					Status status = new Status(code, "too many applicable "
							+ "top-level policies");
					return new PolicyFinderResult(status);
				}

				// this is the first match we've found, so remember it
				
				selectedPolicies.add(policy);
			}
		}

		// no errors happened during the search, so now take the right
		// action based on how many policies we found

		log.debug("Selected policy Size ==> "+ selectedPolicies.size());
		switch (selectedPolicies.size()) {
		case 0:
			if (log.isDebugEnabled()) {
				log.debug("No matching XACML policy found");
			}
			return new PolicyFinderResult();
		case 1:
			
			//try {
			//	throw new Exception ("Test exception");
			//} catch (Exception e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
			log.debug("returning selected policy == > "+selectedPolicies.get(0));
			return new PolicyFinderResult((selectedPolicies.get(0)));
		default:
			return new PolicyFinderResult(new PolicySet(null, combiningAlg,
					null, selectedPolicies));
		}
	}

	@Override
	public PolicyFinderResult findPolicy(URI idReference, int type,
			VersionConstraints constraints, PolicyMetaData parentMetaData) {

		AbstractPolicy policy = policies.get(idReference);
		if (policy != null) {
			if (type == PolicyReference.POLICY_REFERENCE) {
				if (policy instanceof Policy) {
					return new PolicyFinderResult(policy);
				}
			} else {
				if (policy instanceof PolicySet) {
					return new PolicyFinderResult(policy);
				}
			}
		}

		// if there was an error loading the policy, return the error
		ArrayList<String> code = new ArrayList<String>();
		code.add(Status.STATUS_PROCESSING_ERROR);
		Status status = new Status(code, "couldn't load referenced policy");
		return new PolicyFinderResult(status);
	}

	@Override
	public boolean isIdReferenceSupported() {
		return true;
	}

	@Override
	public boolean isRequestSupported() {
		return true;
	}

	/**
	 * Re-sets the policies known to this module to those contained in the given
	 * files.
	 * 
	 */
	public void loadPolicies() {

		policies.clear();

		for (String policyLocation : policyLocations) {

			File file = new File(policyLocation);
			if (!file.exists()) {
				continue;
			}

			if (file.isDirectory()) {
				String[] files = file.list();
				for (String policyFile : files) {
					File fileLocation = new File(policyLocation
							+ File.separator + policyFile);
					if (!fileLocation.isDirectory()) {
						loadPolicy(
								policyLocation + File.separator + policyFile,
								finder);
						// this.getRetainedPolicyLocations().add(policyLocation
						// + File.separator + policyFile);

						log.info("loading policies ==> " + policyLocation
								+ File.separator + policyFile);
					}
				}
			} else {
				loadPolicy(policyLocation, finder);
			}
		}
	}

	/**
	 * Private helper that tries to load the given file-based policy, and
	 * returns null if any error occurs.
	 * 
	 * @param policyFile
	 *            file path to policy
	 * @param finder
	 *            policy finder
	 * @return <code>AbstractPolicy</code>
	 */
	@SuppressWarnings("unchecked")
	private AbstractPolicy loadPolicy(String policyFile, PolicyFinder finder) {

		AbstractPolicy policy = null;
		InputStream stream = null;

		try {
			// create the factory
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setIgnoringComments(true);
			factory.setNamespaceAware(true);
			factory.setValidating(false);

			// create a builder based on the factory & try to load the policy
			DocumentBuilder db = factory.newDocumentBuilder();

			log.info("load policy method ==> " + policyFile);

			//assigned it zero 
			setRetainedPolicyLocations(new ArrayList());
			
			getRetainedPolicyLocations().add(policyFile);

			stream = new FileInputStream(policyFile);
			Document doc = db.parse(stream);

			
			
			//TODO - Only use for debugging purpose
			printXACMLpolicyFile (doc);
			
			// handle the policy, if it's a known type
			Element root = doc.getDocumentElement();
			String name = DOMHelper.getLocalName(root);
			
			log.debug("The root element "+root + " name"+name);

			if (name.equals("Policy")) {

				policy = Policy.getInstance(root);

				log.debug("getting instance of policy, based on root " + root+ "; policy:"+policy);
			} else if (name.equals("PolicySet")) {
				log.info("getting instance of policy set, based on root, and finder "
						+ root + "  " + finder);

				policy = PolicySet.getInstance(root, finder);
			}
		} catch (Exception e) {
			// just only logs
			log.error("Fail to load policy : " + policyFile, e);
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					log.error("Error while closing input stream");
				}
			}
		}

		if (policy != null) {
			policies.put(policy.getId(), policy);
		}

		log.debug("Returning policy " + policy);
		return policy;
	}

	public static ArrayList<String> getRetainedPolicyLocations() {
		return retainedPolicyLocations;
	}

	private static void setRetainedPolicyLocations(
			ArrayList<String> retainedPolicyLocations) {
		FileBasedPolicyFinderModule.retainedPolicyLocations = retainedPolicyLocations;
	}

	
	private void printXACMLpolicyFile(Document document){
		
		    // Use a Transformer for output
		    TransformerFactory tFactory =
		    TransformerFactory.newInstance();
		    Transformer transformer;
		    
			try {
				transformer = tFactory.newTransformer();
				 DOMSource source = new DOMSource(document);
				    log.info ("\n-------------------------------Policy File -------------------------\n");
				    StreamResult result = new StreamResult(System.out);
				    transformer.transform(source, result);
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		   
		
		
	}
	
	
	
}
