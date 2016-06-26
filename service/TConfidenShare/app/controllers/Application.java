/**
 * @file 		Application.java
 * @project 	traceability-enforcement-cloud-framework
 * @Module		TConfidenShare
 * @date 		18 05 2013
 * @version 	1.0
 */
package controllers;

import org.apache.log4j.Logger;
import org.provenance.cloudprovenance.confidenshare.model.CloudUser;
import org.provenance.cloudprovenance.confidenshare.service.ConfidenShareBoService;
import org.provenance.cloudprovenance.confidenshare.util.XMLMessageCreator;

import play.Play;
import play.modules.spring.Spring;
import play.mvc.Controller;

import com.provenance.cloudprovenance.service.api.impl.CprovXmlProvenance;
import com.provenance.cloudprovenance.service.api.impl.DynamicPolicyRequest;

/** 
* @author Mufy
* 
*/
public class Application extends Controller {

	// Load the beans
	static private CprovXmlProvenance cProvApi = Spring
			.getBeanOfType(CprovXmlProvenance.class);
	static DynamicPolicyRequest srvCompliance = Spring
			.getBeanOfType(DynamicPolicyRequest.class);

	private static Logger logger = Logger.getLogger("Application");

	private static final String SESSION_USER = "user_email";
	private static final String SESSION_USER_NAME = "userName";
	private static final String SESSION_SCENARIO = "scenario";
	private static final String SHARE_PROCESS_NAME = "a-share";
	String redirectionUriFunctionPage = "https://" + httpAddress
			+ ":9443/scenario";

	private static String httpAddress = (String) Play.configuration
			.get("http.address");

	public static void index() {
		render();
	}

	public static void createUser(String userName, String email, String password) {

		long startMeasuringTime = 0, endMeasuringTime;

		// *********************Start of the measurement ********************/

		cProvApi.setMaxStatementPerDocument(16);

		startMeasuringTime = System.currentTimeMillis();
		logger.info("Start time:" + startMeasuringTime);

		ConfidenShareBoService dbStore = ConfidenShareBoService
				.confidenShareBoServiceInstance();

		session.put(SESSION_USER_NAME, userName);

		logger.info("userName: " + userName + " Email: " + email
				+ " Password: " + password);

		if (email.contains("@")) {
			email = email.replace("@", ".at.");
		}

		cProvApi.Agent(userName, userName, "");

		String userPid = cProvApi.cProcess("createUser", "http://"
				+ httpAddress + "/" + userName, "http://" + httpAddress + "/"
				+ userName + "/Linux/vProcess1", httpAddress
				+ "/Linux/pProcess1", null);

		// TODO - Review prefix
		cProvApi.wasInitiallyCalledBy("wicb1", userPid, userName, "", "laptop",
				"wifi");

		cProvApi.cResource(email, "data", "http:" + httpAddress + "/" + email,
				"http://" + httpAddress + "/" + userName + "/data1",
				httpAddress, true, null, null, "general", 1.0f, null);

		String passedId = cProvApi.cResource("password", "data", "http://"
				+ httpAddress + "/" + "password", "http://" + httpAddress
				+ userName + "/data2", httpAddress, true, null, null,
				"restricted", 1.0f, null);

		cProvApi.used("u1", userPid, email, null);

		cProvApi.used("u2", userPid, passedId, null);

		// Check for existing entries
		String findUsrId = cProvApi.cProcess("checkUser", "http://"
				+ httpAddress + "/" + userName, "http://" + httpAddress + "/"
				+ userName + "/Linux/vProcess1", httpAddress
				+ "/Linux/pProcess1", null);

		cProvApi.wasImplicitlyCalledBy("wic1", findUsrId, userPid, "",
				"syncronous", "laptop", "wifi");

		logger.info("Creating a new user :" + email);
		// ******** VALIDATION ******
		validation.required(email);
		validation.required(password);
		validation.email(email);

		// Check that the user does not already exist
		// User existingUser = User.find("byEmail", email).first();

		boolean existingUser = dbStore.checkUser(userName, email);

		cProvApi.used("u3", "checkUser", email, null);

		logger.info("Check if the user exists : " + existingUser);

		if (existingUser == false) {

			// User exists prov entry
			String errorId = cProvApi.cResource("userDoesNotExist", "data",
					"http:" + httpAddress + "/" + email, "http://"
							+ httpAddress + "/" + userName + "/data1",
					httpAddress, true, null, null, "general", 1.0f, null);

			cProvApi.wasVirtualizedBy(null, errorId, findUsrId, null, "");

			// renderXml(xml);
		} else {

			// TODO - change the Prov statement time.

			String xml = XMLMessageCreator.createErrorMessage(
					XMLMessageCreator.ERROR_USER_EXIST,
					"This user allready exist");

			// User exists prov entry
			String errorId = cProvApi.cResource("userExist", "data", "http:"
					+ httpAddress + "/" + email, "http://" + httpAddress + "/"
					+ userName + "/data1", httpAddress, true, null, null,
					"general", 1.0f, null);

			cProvApi.wasVirtualizedBy(null, errorId, findUsrId, null, "");

			renderXml(xml);
		}

		// Create User
		CloudUser newUser = new CloudUser();
		newUser.setUserName(userName);
		newUser.setEmail(email);
		newUser.setPassword(password);

		// Store user
		dbStore.registerUser(newUser);

		String newUserPid = cProvApi.cProcess("saveUser", "http://+"
				+ httpAddress + "/" + userName, "http://" + httpAddress + "/"
				+ userName + "/Linux/vProcess1", httpAddress
				+ "/Linux/pProcess1", null);

		cProvApi.wasImplicitlyCalledBy(null, newUserPid, findUsrId, "",
				"synchronous", "laptop", "wifi");

		logger.info("New user has been created: " + email);
		session.put(SESSION_USER, email);

		logger.info("User added to the Database");
		String xml = XMLMessageCreator.createSuccessMessage();

		String newUsrId = cProvApi.cResource("createdSuccessfully", "data",
				"http:" + httpAddress + "/" + email, "http://" + httpAddress
						+ "/" + userName + "/data1", httpAddress, true, null,
				null, "general", 1.0f, null);

		cProvApi.wasVirtualizedBy(null, newUsrId, newUserPid, null, "");

		endMeasuringTime = System.currentTimeMillis();

		logger.info("Start TIme: " + startMeasuringTime + " End time: "
				+ endMeasuringTime + "  Diff: "
				+ (endMeasuringTime - startMeasuringTime));

		// *****************************End of User creation ****************/

		/*
		 * logger.info("Create msg :" + xml);
		 * 
		 * String userAgent = "Bob"; String resource = "document1"; String
		 * process = "createUser"; String environment1 =
		 * "reg.share.confidenshare.labs.orange.com";
		 * 
		 * srvCompliance.constructRequest(userAgent, resource, process,
		 * environment1);
		 */
		// redirect(dURL);

		// REDIRECTION and call to Dropbox API required
	}

	public static void userDetails() {

	}

	public static void home() {

	}

	public static void register() {
		render();
	}

}