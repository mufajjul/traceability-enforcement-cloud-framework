/**
 * @file 		PolicyEngine.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		PolicyEngine
 * @date 		18 05 2013
 * @version 	1.0
 */
package com.provenance.cloudprovenance.policyengine.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.wso2.balana.Balana;
import org.wso2.balana.PDP;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.finder.AttributeFinder;
import org.wso2.balana.finder.AttributeFinderModule;
import org.wso2.balana.finder.PolicyFinder;
import org.wso2.balana.finder.PolicyFinderModule;
import org.wso2.balana.finder.impl.FileBasedPolicyFinderModule;

/**
 * This class represent the XACML policy Engine
 * 
 * @author Mufy
 * 
 */
public class PolicyEngine {

	Set<String> policyLocations = new HashSet<String>();
	PolicyFinder finder = new PolicyFinder();

	static Logger logger = Logger.getLogger("PolicyEngine");

	public Set<String> getPolicyLocations() {
		return policyLocations;
	}

	public void setPolicyLocations(Set<String> policyLocations) {
		this.policyLocations = policyLocations;
	}

	public String executePolicy(String policyPath, String requestPath,
			String responsePath, String serviceId) throws URISyntaxException {

		String policyAbsolutePath = this.getAbsolutePath(this
				.getURIpath(policyPath));
		policyLocations.add(policyAbsolutePath);

		logger.debug("Policy Path ==> " + policyAbsolutePath);

		String requestContent = this.readFileContentAsString(new File(this
				.getAbsolutePath(this.getURIpath(requestPath))));
		logger.info("\n------------------------------------------------------XACML Request ------------------------------------------------ \n"
				+ requestContent);

		PDP pdp = this.initilizePolicyEngine();

		String outcome = pdp.evaluate(requestContent, serviceId);

		logger.info("\n------------------------------------------------------XACML response ----------------------------------------------- \n"
				+ outcome);

		try {
			String resPath = (policyAbsolutePath.replace("policies",
					"responses")).replace("policy", "policy-res");

			logger.debug("res path ==> " + resPath);

			File resFile = new File(resPath);

			FileWriter fWriter = new FileWriter(resFile);
			fWriter.write(outcome);
			fWriter.close();
			logger.info("Response written to ==> " + resFile.getName());

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return outcome;
	}
	
	public String executeWebPolicy(String policyPath, String policyRequest,
			 String serviceId) throws URISyntaxException {

		//String policyAbsolutePath = this.getAbsolutePath(this
			//	.getURIpath(policyPath));
		
		policyLocations.add(policyPath);

		//logger.debug("Policy Path ==> " + policyAbsolutePath);

		logger.info("\n------------------------------------------------------XACML Request ------------------------------------------------ \n"
				+ policyRequest);

		PDP pdp = this.initilizePolicyEngine();

		String outcome = pdp.evaluate(policyRequest, serviceId);

		logger.info("\n------------------------------------------------------XACML response ----------------------------------------------- \n"
				+ outcome);
		return outcome;
	}
	
	
	public String executePolicy(String xacmlRequest, File policyPath){
		
		policyLocations.add(this.getAbsolutePath(policyPath.toURI()));
		PDP pdp = this.initilizePolicyEngine();
		
		logger.info("\n------------------------------------------------------XACML Request ------------------------------------------------ \n"
				+ xacmlRequest);

		String outcome = pdp.evaluate(xacmlRequest);

		logger.info("\n------------------------------------------------------XACML response ----------------------------------------------- \n"
				+ outcome);

		return outcome;
	}
	
	
	public String executePolicy(String policyPath, String requestPath,
			String responsePath) throws URISyntaxException {

		String absolutePathToPolicyFiles = null;

		File policyPathDirectory = new File(policyPath);

		if (policyPathDirectory.exists()) {
			absolutePathToPolicyFiles = policyPathDirectory.getAbsolutePath();
		} else {

			URI policyAbPathUri = this.getURIpath(policyPath);

			if (policyAbPathUri == null) {
				policyAbPathUri = new URI("file://" + policyPath);
				logger.info("policy path ==> " + policyAbPathUri);

			}

			absolutePathToPolicyFiles = this.getAbsolutePath(policyAbPathUri);
		}
		policyLocations.add(absolutePathToPolicyFiles);

		logger.info("Policy Path ==> " + absolutePathToPolicyFiles);

		String requestContent = this.readFileContentAsString(new File(requestPath).getAbsoluteFile());
		
		logger.info("\n------------------------------------------------------XACML Request ------------------------------------------------ \n"
				+ requestContent);

		PDP pdp = this.initilizePolicyEngine();

		String outcome = pdp.evaluate(requestContent);

		logger.info("\n------------------------------------------------------XACML response ----------------------------------------------- \n"
				+ outcome);

		try {
			String resPath = (absolutePathToPolicyFiles.replace("policies",
					"responses")).replace("policy", "policy-res");

			logger.info("res path ==> " + resPath);

			File resFile = new File(resPath);
			resFile.createNewFile();

			FileWriter fWriter = new FileWriter(resFile);
			
			fWriter.write(outcome);
			fWriter.close();
			logger.info("Response written to ==> " + resFile.getName());

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return outcome;
	}

	public PDP initilizePolicyEngine() {
		FileBasedPolicyFinderModule cprovlPolicyFinderModule = new FileBasedPolicyFinderModule(
				policyLocations);
		Set<PolicyFinderModule> policyModules = new HashSet<PolicyFinderModule>();
		policyModules.add(cprovlPolicyFinderModule);
		finder.setModules(policyModules);

		Balana balana = Balana.getInstance();
		PDPConfig pdpConfig = balana.getPdpConfig();

		AttributeFinder attributeFinder = pdpConfig.getAttributeFinder();

		List<AttributeFinderModule> finderModules = attributeFinder
				.getModules();
		finderModules.add(new CprovlAttributeFinderModule());

		pdpConfig = new PDPConfig(attributeFinder, finder,
				pdpConfig.getResourceFinder(), true);

		return new PDP(pdpConfig);
	}

	public String readFileContentAsString(File f) {
		try {
			byte[] bytes = Files.readAllBytes(f.toPath());
			return new String(bytes, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getAbsolutePath(URI filePath) {
		logger.info("URI ==> " + filePath);
		return new File(filePath).getAbsolutePath();
	}

	public URI getURIpath(String filePath) throws URISyntaxException {
		try {
			URI fileURIPath = ClassLoader.getSystemClassLoader()
					.getResource(filePath).toURI();
			logger.info("file path " + fileURIPath);

			return fileURIPath;

		} catch (NullPointerException ex) {
			logger.warn("Path not found, reutrning null!!!");
			// Constructing new URI

			return new URI("file://" + filePath);
		}
	}
}
