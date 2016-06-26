package org.provenance.cloudprovenance.confidenshare.util;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;
/**
 * Here happens all creations of XML message that are to be delivered to the client.
 * <br>TODO use StringBuilders instead OR use XML templates in views directory.
 * @author Boris Chazalet
 * 
 */

public class XMLMessageCreator {

	static Logger logger = Logger.getLogger("XMLMessageCreator");

	
	/**
	 * It is a header for all XML files.
	 */
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	
	public static final int ERROR_LOGIN_INVALID = 101;
	public static final int ERROR_LOGIN_USER_DONT_EXIST = 102;
	public static final int ERROR_HASH_IS_NULL = 103;
	public static final int ERROR_CANT_READ_FILE_IN = 104;
	public static final int ERROR_DK_KEY_NULL = 105;
	public static final int ERROR_CANT_FIND_FILE = 106;
	public static final int ERROR_WHILE_DOWNLOADING = 107;
	public static final int ERROR_MALFORMED_URL = 108;
	public static final int ERROR_WRONG_FILE_FORMAT = 109; //Meaning we could not extract the file and key from the zip
	public static final int ERROR_WHILE_UPLOADING = 110; 
	public static final int ERROR_FILE_ALREADY_EXIST = 111;
	public static final int ERROR_NOT_LOGGED_IN = 112;
	public static final int ERROR_WRONG_DEVICE_ID = 113;
	public static final int ERROR_MISSING_DEL_KEY = 114;
	public static final int ERROR_NO_ORIGINAL_KEY = 115;
	public static final int ERROR_NOT_ENCRYPTED_FOR_US = 116;
	public static final int ERROR_RES_NOT_FOUND = 117; //equivalent to 404
	public static final int ERROR_ACCESS_DENIED = 118;
	public static final int ERROR_PARAM_NULL = 119;
	public static final int ERROR_COULD_NOT_DECRYPT_FILE = 120;
	public static final int ERROR_IOEXCEPTION = 121;
	public static final int ERROR_SECURITY = 122;
	public static final int ERROR_CONNECTION = 123;
	public static final int CREATEUSER = 124;

	public static final int WARNING_WAIT_CONFIRMATION = 125;

	public static final int ERROR_NOT_MAIN_DEVICE = 126;

	public static final int REGISTERDEVICE = 127;

	public static final int ERROR_NOT_OWNER = 128;

	public static final int ERROR_NOT_SHARED = 129;

	public static final int ERROR_TOKEN = 130;

	public static final int ERROR_USER_EXIST = 131; 
	
	/**
	 * Create a XML generic success message
	 * @return
	 * 		The XML message as a String
	 */
	public static String createSuccessMessage(){
		String ret = XML_HEADER + "<fileshare>" + "<rsp status=\"ok\" />" + "</fileshare>";
		return ret;
	}
	
	/**
	 * Creates a XML response for a GET session key
	 */
	public static String createSuccessUserNameData(String userName){
		String xmlReply = XML_HEADER 
				+ "<fileshare>"
				+ "<rsp status=\"ok\" />"
				+ "<userName>" + userName + "</userName>"
				+ "</fileshare>";
		return xmlReply;
	}
	/**
	 * Returns a XML response to a sync request
	 */
	public static String createFileSyncData(List<CloudFile> userFiles){
		String ret = XML_HEADER;
		ret += "<fileshare>";
		ret += "<rsp status=\"ok\" />";
		ret += "<filelist>";
		if(userFiles != null ){
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
			df.setTimeZone(TimeZone.getTimeZone("GMT"));
    		for (CloudFile oneFile : userFiles){
    			ret += "<file>"
    				 + "<fileId>"+oneFile.uuid+"</fileId>"
    				 + "<filename>"+oneFile.filename +"</filename>"
    				 + "<size>"+oneFile.size +"</size>"
    				 + "<updatedAt>" + df.format((oneFile.updatedAt !=null)?oneFile.updatedAt:oneFile.createdAt) + "</updatedAt>" 
    				 + "</file>";
    			
    		}
    	}
		
		ret += "</filelist>";
		ret += "</fileshare>";
		return ret;
	}
	
	/**
	 * Creates a XML error message
	 * @param error
	 * 		The error id 
	 * @param reason
	 * 		A human readable string explaining the error
	 */
	public static String createErrorMessage(int error, String reason){
		String xml = XML_HEADER 
			+ "<fileshare>"
			+ "<rsp status=\"fail\" >"
			+ "<error code=\""+error+"\" msg=\""+reason+"\" />"
			+ "</rsp>"
			+ "</fileshare>";
		return xml;
	}
	
	/**
	 * Creates a XML error message
	 * @param error
	 * 		The error id 
	 * @param reason
	 * 		A human readable string explaining the error
	 */
	public static String createErrorConnectionMessage(int error, String reason, String name){
		String xml = XML_HEADER 
			+ "<fileshare>"
			+ "<rsp status=\"fail\" >"
			+ "<error code=\""+error+"\" msg=\""+reason+"\" />"
			+ "</rsp>"
			+"<username>"
			+name
			+"</username>"
			+ "</fileshare>";
		return xml;
	}
	
	
	/**
	 * Creates a XML error message
	 * @param error
	 * 		The error id 
	 * @param reason
	 * 		A human readable string explaining the error
	 */
	public static String createErrorMessageWithUserName(int error, String reason,String userName){
		String xml = XML_HEADER 
			+ "<fileshare>"
			+ "<rsp status=\"fail\" >"
			+ "<error code=\""+error+"\" msg=\""+reason+"\" />"
			+ "</rsp>"
			+"<userName>"
			+userName
			+"</userName>"
			+ "</fileshare>";
		return xml;
	}
	
	/**
	 * Creates a XML response containing a one-time-url
	 */
	public static String createOnetimeUrlReplyData(String fileId, String oneTimeUrl){
		String xmlReply = XML_HEADER 
			+ "<fileshare>"
			+ "<rsp status=\"ok\" />"
			+ createOtuData(fileId, oneTimeUrl)
			+ "</fileshare>";
		return xmlReply;
	}
	
	/**
	 * Creates a XML response for a GET session key
	 */
	public static String createSessionKeyReplyData(SessionKey sk){
		String xmlReply = XML_HEADER 
				+ "<fileshare>"
				+ "<rsp status=\"ok\" />"
				+ "<sessionKey>" + sk.getKeyAsBase64() + "</sessionKey>"
				+ "</fileshare>";
		return xmlReply;
	}
	
	private static String createOtuData(String fileId, String oneTimeUrl){
		String xmlReply = "<sharedFile>" 
				+ "<fileId>" + fileId + "</fileId>" 
				+ "<oneTimeURL>" + oneTimeUrl + "</oneTimeURL>" 
				+ "</sharedFile>";
		return xmlReply;
	}
	
	
	/**
	 * Creates a XML response to be replied to a registration request.
	 * For each device passed in parameter, it lists its id and public key.
	 */
	public static String createRegistrationData(List<Device> devices){
		if(devices == null)
			throw new NullPointerException("list of devices can be null");
		String xmlReply = XML_HEADER 
				+ "<fileshare>"
				+ "<rsp status=\"ok\" />";
		if(devices.size() == 0){
			xmlReply += "<devices />";
		} else {
			xmlReply += "<devices>";
			for(Device device: devices){
				
				logger.info(device.deviceId +" "+ ( (device.isActive)? "actif":"non actif") );
				xmlReply += "<device main=\"" + (device.isMain()?"true":"false") + "\">"
						+ "<id>"+ device.deviceId +"</id>"
						+ "<publicKey>" + device.getKeyAsBase64()+ "</publicKey>" 
						+"<deviceName>" + device.deviceName      + "</deviceName>"
						+ "</device>";
			}
			xmlReply += "</devices>";
		}
		xmlReply += "</fileshare>";
		
		return xmlReply;
	}
	
	
	/**
	 * Creates a XML response to be replied to an add contact request
	 */
	
	/**
	 * Creates a XML response to be replied to an add contact request
	 */
	public static String createAddContactReply(String[] emails,User user){
		String reply="";
		String xmlReply = XML_HEADER 
				+ "<fileshare>"
				+ "<rsp status=\"ok\" />";
		
		User contact;
		if(emails!=null)
		{
			for(String e:emails){
				contact = User.find("byEmail", e).first();
				if(contact!=null)
					reply+="<contact><name>"+contact.fullname+"</name><mail>"+e+"</mail><res>";
				else
					reply+="<contact><mail>"+e+"</mail><res>";
					
				switch(user.addContact(contact))
				{
					case User.CONTACT_ADDED:
						reply+=User.CONTACT_ADDED+"</res>";
						reply+=getAllKeyUser(contact);
						break;
					case User.CONTACT_DOESNT_EXIST:
						reply+=User.CONTACT_DOESNT_EXIST+"</res>";
						break;
					case User.CONTACT_IN:
						reply+=User.CONTACT_IN+"</res>";
						break;
				}
			}
			reply+="</contact>";
		}
		xmlReply+=reply	+"</fileshare>";
		logger.info("replys: %s: "+ xmlReply);
		return xmlReply;
	}

	private static String getAllKeyUser(User user) {
		String xmlReply="";
		if(user.devices.size() == 0){
			xmlReply += "<devices />";
		}
		else {
			xmlReply += "<devices>";
			for(Device device: user.devices){
				
				logger.info(device.deviceId +" "+ ( (device.isActive)? "actif":"non actif") );
				xmlReply += "<device main=\"" + (device.isMain()?"true":"false") + "\">"
						+ "<id>"+ device.deviceId +"</id>"
						+ "<publicKey>" + device.getKeyAsBase64()+ "</publicKey>" 
						+"<deviceName>" + device.deviceName      + "</deviceName>"
						+ "</device>";
			}
			xmlReply += "</devices>";
		}
		return xmlReply;
	
	}


	public static String createAllFilesData(User user,String[] files) {
		if(user == null || files==null )
			throw new NullPointerException("list of devices can be null");
		String xmlReply = XML_HEADER 
				+ "<fileshare>"
				+ "<rsp status=\"ok\" />";
		
		for(int i=0 ;i<files.length;i++){
			CloudFile  file = user.findCloudFile(files[i]);
			
			if(file!=null){
				xmlReply+="<path ><filename>"+files[i]+"</filename>"
						+"<fileId>"+file.getUUID()+"</fileId>"
						+"<res>ok</res>";
				for(SharedFile sf : file.sharedfiles){
					//if (!sf.originalFile.owner.email.contentEquals(user.email)){
						xmlReply+="<contact>" +
								"<name>"+sf.userShared.fullname+"</name>"+
								"<email>"+sf.userShared.email+"</email>"
								+"</contact>";
					//}
						
				}
			}
			else{
				xmlReply+="<path ><name>"+files[i]+"</name>"
						+"<res>error</res>";
			}
			xmlReply += "</path>";
		}
		xmlReply += "</fileshare>";
		
		return xmlReply;
	
	}

	public static String createSharedFilesData(SharedFile file,String cloudFileID) {
		String xmlReply = XML_HEADER 
				+ "<fileshare>"
				+ "<rsp status=\"ok\" />"
				+ "<filename>"+file.originalFile.filename+"</filename>"
				+"<fileid>"+cloudFileID+"</fileid>"
				+ "<name>"+file.originalFile.owner.fullname+"</name>"
				+ "<email>"+file.originalFile.owner.email+"</email>"
				+ "<path>"+file.pathDropbox+"</path>";
		xmlReply += "</fileshare>";
		
		return xmlReply;
	
	}
}