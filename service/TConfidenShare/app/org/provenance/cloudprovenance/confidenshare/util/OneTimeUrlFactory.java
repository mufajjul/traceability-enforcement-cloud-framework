package org.provenance.cloudprovenance.confidenshare.util;

import java.security.SecureRandom;
import java.util.Date;

/**
 * Factory to generate random URL for sharing files
 * @author Boris Chazalet
 *
 */
public class OneTimeUrlFactory {

	private static OneTimeUrlFactory singleOne = new OneTimeUrlFactory();
	
	public static OneTimeUrlFactory getInstance(){
		return singleOne;	
	}
	/**
	 * Generate a random URL for file sharing.
	 * @return
	 *        The random URL. 
	 */
	
	
	private void sendEmailConfirmation(String email){
		
		
	}
	
	
	public String generateOneTimeId(){
		String url = "";
//		String url = HTTP_PREFIX + Play.configuration.get("application.domain");
//		url += PREFIX_FOR_SHARING_FILES;
//		try {
//			Date now = new Date();
		    //TODO improve the randomness --> use has functions
			//Random r = new Random();
			//Long rand = (new Date()).getTime()+ Math.abs(r.nextLong());
			//String randomPart = ""+rand;
			
			//GVE
			Long rand = (new Date()).getTime();
			SecureRandom mSecureRandom = new SecureRandom();
			String randomPart = Integer.toString(mSecureRandom.nextInt(100000))+rand;
			
			
			// The following generates characters that are not allowed for a URL.
//			String toHash = ""+r.nextInt();//+FIXED_PART;
//			byte[] bytesOfMessage = toHash.getBytes("UTF-8");
//			MessageDigest md = MessageDigest.getInstance("MD5");
//			byte[] thedigest = md.digest(bytesOfMessage);
//			String randomPart = new String( thedigest , "Cp1252" );
//			url += randomPart;
//			Logger.info("Random part for one time URL" + randomPart);
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			url += "error";
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			url += "error";
//			e.printStackTrace();
//		}
		url += randomPart;
		return url;
	}
	
}
