package org.provenance.cloudprovenance.confidenshare.util;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import play.Play;
import play.db.jpa.Model;
import play.mvc.Router;

/**
 * Represents a file that has been shared and is publicly available for download.
 * In fact, this consists of the encrypted file and an encrypted session key. At delivery time,
 * both are combined into a zip file.
 * <br>The session key was re-encrypted from a {@link CloudFile}'s session key (which could have been original or not) 
 * and can only be decrypted by intented recepient.
 * @author Boris Chazalet
 *
 */
@javax.persistence.Entity
public class SharedFile extends Model {
	
	
	
	//private static final String STORED_SHARED_FILE_FOLDER = "storedFiles/shared/";
	private static String HTTP_PREFIX = "http://";
//	private static String PREFIX_FOR_SHARING_FILES = "/share/file?fileId="; //http://localhost:9000/share/file/84fewe86r56e5r84
	
	
	
	/**
	 * When the file was first created (i.e. shared) 
	 */
	public Date createdAt;
	
	
	
	/**
	 * The unique & temporary id of the shared file (the one given in the one-tim-URL).
	 */
	public String sharingId;
	
	/**
	 * The hash of the receiver's key. If the user is in our DB, you can use this hash
	 * to look for the key and therefore find the particular device and the intented receiver user. 
	 */
	public String hashOfReceiverKey;
	
	/**
	 * The storing the number of downloads for this particular file
	 */
	public int nbDownloads;
	
	/**
	 * the path to download the file
	 */
	public String pathDropbox;
    
	/**
	 * Reference to the file that was shared to produce this one.
	 */
    @ManyToOne
    public CloudFile originalFile;
    
	/**
	 * Reference to the file that was shared to produce this one.
	 */
    @ManyToOne
    public User userShared;
    
    
    @PreUpdate
    @PrePersist
    void updateModDates() {
    if (createdAt == null) // first time
    	createdAt = new Date();
    }
    
    /**
     * Constructs a SharedFile from a CloudFile (copying the name, size) and giving it a sharind uuid
     * @param originalFile
     * 			The original CloudFile to construct the ShareFile from
     */
    public SharedFile(CloudFile originalFile, String hashOfReceiverKey){
    	createdAt = new Date();
    	//this.owner = originalFile.owner;
    	this.originalFile = originalFile;
    	//    	this.filename = originalFile.filename;
    	//copyFileOnDisk(originalFile);
    	this.hashOfReceiverKey = hashOfReceiverKey;
    	OneTimeUrlFactory urlFactory = OneTimeUrlFactory.getInstance();
    	sharingId = urlFactory.generateOneTimeId();
    	
    	userShared = originalFile.owner;
    }
    
    /**
     * Constructs a SharedFile from a CloudFile (copying the name, size) and giving it a sharind uuid
     * @param originalFile
     * 			The original CloudFile to construct the ShareFile from
     */
    public SharedFile(CloudFile originalFile, User user){
    	createdAt = new Date();
    	//this.owner = originalFile.owner;
    	this.originalFile = originalFile;
    	//    	this.filename = originalFile.filename;
    	//copyFileOnDisk(originalFile);
    	this.hashOfReceiverKey = null;
    	OneTimeUrlFactory urlFactory = OneTimeUrlFactory.getInstance();
    	sharingId = urlFactory.generateOneTimeId();
    	//userShared =new ArrayList<User>();
    	userShared = user;
    }
    
    public SharedFile(CloudFile originalFile, User user, String path ){
    	createdAt = new Date();
    	//this.owner = originalFile.owner;
    	this.originalFile = originalFile;
    	this.pathDropbox= path;
    	//    	this.filename = originalFile.filename;
    	//copyFileOnDisk(originalFile);
    	this.hashOfReceiverKey = null;
    	OneTimeUrlFactory urlFactory = OneTimeUrlFactory.getInstance();
    	sharingId = urlFactory.generateOneTimeId();
    	//userShared =new ArrayList<User>();
    	userShared = user;
    }
    
 /*   public void addUserShared(User user ){
    	userShared.add(user);
    }
    
    public void removeReEncryptionKey(User user){
    	if(user.email.contentEquals(this.originalFile.owner.email))
    		return;
    	for(int i=0;i<userShared.size();i++){
    		if(userShared.get(i).email.contentEquals(user.email))
    		{
    			userShared.remove(i);
    		}
    	}
    }
*/
    
    /**
     * Get the one time URL pointing to the file
     * @return
     * 			The fully formed one-time URL for downloading (something like share/file?fileId=8d5fs6f5sdfioj36iopj9gd6f)
     */
    public String getOneTimeURL(){
    	HashMap map = new HashMap();
    	map.put("id", sharingId);
    	String url = HTTP_PREFIX + Play.configuration.get("application.domain") + Router.reverse("Sharing.getSharedFile", map).url;
//    	String url = HTTP_PREFIX + Play.configuration.get("application.domain");
//		url += PREFIX_FOR_SHARING_FILES;
//		url += sharingId;
		return url;
    }
    
    /**
     * Returns a zip file of both the re-encrypted session key and the encrypted file
     * @return
     * 			The zip file as input stream ready to be sent to the client
     */
    /*
    public InputStream getStream(){
		ZipOutputStream zipOutput = null;
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		//ByteArrayOutputStream keyInput = new ByteArrayOutputStream();
		//System.out.println("Preparing the zip file to send");
		try {
			zipOutput = new ZipOutputStream(new BufferedOutputStream(bytes));
			// Adding the re-encrypted key to the zip
			ZipUtils.writeByteArrayToEntry(zipOutput, CloudFile.KEY_ENTRY, this.reencryptedKey);
			// Adding the encrypted file to the zip
			//ZipUtils.writeFileToEntry(zipOutput, this.filename, STORED_SHARED_FILE_FOLDER + this.filename);
			ZipUtils.writeFileToEntry(zipOutput, this.filename, CloudFile.buildStoringPath(originalFile.owner.email) + this.filename);
			// Close Zip stream
			zipOutput.close();
			// at this point, bytes contains the complete zip file
			return new ByteArrayInputStream(bytes.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return null;
    }*/
    
    
//  private boolean copyFileOnDisk(CloudFile originalFile){
//	File from = new File(originalFile.STORED_FILE_FOLDER, originalFile.filename);
//	FileInputStream in;
//	try {
//		in = new FileInputStream(from);
//    	File to = new File(STORED_SHARED_FILE_FOLDER, this.filename);
//    	FileOutputStream out = new FileOutputStream(to); //will overwrite the file
//    	byte[] buf = new byte[1024];
//		int len;
//		while ((len = in.read(buf)) > 0){
//			out.write(buf, 0, len);
//		}
//		in.close();
//		out.close();
//	} catch (FileNotFoundException e) {
//		e.printStackTrace();
//		return false;
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	Logger.info("File copied into shared space.");
//	return true;
//}
}
