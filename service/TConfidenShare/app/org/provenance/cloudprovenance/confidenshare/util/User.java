package org.provenance.cloudprovenance.confidenshare.util;
 
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;

import play.db.jpa.JPABase;
import play.db.jpa.Model;

/**
 * Represent a user of our service.
 * @author Boris Chazalet
 *
 */
@javax.persistence.Entity
public class User extends Model {
	
	static Logger logger = Logger.getLogger("User");
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}


	public static final int CONTACT_IN = 0;
	public static final int CONTACT_ADDED = 1;
	public static final int CONTACT_DOESNT_EXIST = 2;
	
	private static final int numberQuestion = 2;
	public static final String[] questions = new String[]{
	"whate the fucj?",
	"whate the fucj?",
	"whate the fucj?",
	"whate the fucj?",
	"whate the fucj?"};
	
	
	
	/**
	 * The email is the username, i.e. it uniquely identifies the user in our DB.
	 */
    public String email;
    /**
     * The user's chosen password
     */
    public String password;
    
    /**
     * The name of the user
     */
    public String fullname;

    /**
     * Used for EngD work
     */
    
    public String address;
    
    public String DOB;
    
    public String gender;
    
    public String tel;
    
    public String mobile;
        
    
    @OneToMany
    public List<User> contacts;
    
    
    @Override
	public String toString() {
		String s = "fullname "+fullname+", email : "+email+", nb de cloudfile"+files.size()+", nb devices :"+devices.size();
		int i =0;
		for(CloudFile c:files)
			i+=c.sharedfiles.size();
		s+=", nb sharedfile total : "+i;
		return  s;
	}




	/**
     * Whether the user has admin rights or not. For now, no one gets granted admin rights.
     */
    public boolean isAdmin;
    
    @Override
	public <T extends JPABase> T delete() {

    	//this.files.clear();
    	//this.devices.clear();
    	//this.save();
    	
    	logger.info("ncoucoucos");
    	List<SharedFile> sharedFiles=null;
   		sharedFiles = SharedFile.find("byUserShared", this).fetch();
    	
		if(sharedFiles.isEmpty())
			logger.info("no shared files");
		if(sharedFiles!=null && !sharedFiles.isEmpty())
			logger.info("efface");
			for(SharedFile s:sharedFiles){
				logger.info("USER DELETE --> delete shared %s from %s to %s"+  s.originalFile.filename + " " +s.originalFile.owner.fullname+ "  "+s.userShared.fullname);
				s.originalFile.deleteSharedFile(this);
			}
			
		return super.delete();
	}

	/**
     * salt used for password, and answers to recovery questions 0: salt password
     */
    public String[] salts;
    /**
     * answers to the recovery questions
     */
    public String[] answers;
    
    /**
     * indice of questions
     */
    public int[] numquestions;;
    
    
    @OneToMany(mappedBy="owner", cascade=CascadeType.ALL)
    public List<Device> devices;
    
    /**
     * All the user's files stored in the cloud
     */
    @OneToMany(mappedBy="owner", cascade=CascadeType.ALL)
    public List<CloudFile> files;
    
    
  //  @OneToMany //(mappedBy="userShared", cascade=CascadeType.ALL)
   // public List<SharedFile> sharedfiles;
    
    /**
     * The key of the user's main device. This <strong>must</strong> one of the user's device's key, i.e.
     * this is just a reference to another key, not a standalone key.
     */
  //  @OneToOne
  //  public Key publicKey;
    
    public User(String email, String password, String fullname) {
        this.email = email;
    
        this.password = password;
        this.fullname = fullname;
        this.isAdmin = false;
        this.contacts = new ArrayList<User>();
        this.files = new ArrayList<CloudFile>();
        this.devices = new ArrayList<Device>();
    }
    
    
    public User(String email, String password, String fullname, String address, String DOB, String gender, String tel, String mobile){
	
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.address = address;
        this.DOB = DOB;
        this.tel = tel;
        this.mobile= mobile;

        this.isAdmin = false;
        this.contacts = new ArrayList<User>();
        this.files = new ArrayList<CloudFile>();
        this.devices = new ArrayList<Device>();
    }
    
    
    
    public Device addDevice(String nameDevice,String deviceId,byte[] key){
    	logger.info("in addDevice devices size: %s: "+ devices.size());
    	boolean isMain = ( (devices!=null && devices.size()>=1) ? false : true  );
    	Device d = new Device(this, key,deviceId,nameDevice, isMain);
    	d.save();
    	devices.add(d);
    	
    	logger.info("Updated device size "+devices.size());
    	return d;
    }
    
    public int addContact(User contact){
    	logger.info("in addContact try to add: %s: "+email);
    	
    	
    	if(contacts.contains(contact))
    		return CONTACT_IN;
    	else if(contact!=null){
    		contacts.add(contact);
    		return CONTACT_ADDED;
    	}
    	else
    		return CONTACT_DOESNT_EXIST;
    }
    
    public void deleteDevice(String deviceId){
    	Device d=null;
    	for(int i=0;i<this.files.size();i++){
    		d=this.devices.get(i);
    		if(d.deviceId.contentEquals(deviceId)){
    			this.devices.remove(i);
    			d.delete();
    		//	d.save();
    			break;
    		}
    	}
    }
    public CloudFile addCloudFile(String nameFile,byte[] keyBite, Device d){
    	CloudFile c = new CloudFile(this,nameFile,0);
    	c.save();
    	c.addSessionKey(keyBite, true, d);
    	files.add(c);
    	return c;
    	
    }
    
    public void deleteCloudFile(String nameFile){
    	for(int i=0;i<this.files.size();i++){
    		CloudFile c =this.files.get(i);
    		if(c.filename.contentEquals(nameFile)){
    			logger.info("delete file: "+ c.filename);
    			this.files.remove(i);
    		//	this.save();
    			c.delete();
    			break;
    		}
    	}
    }
    
    public void deleteCloudFile(String[] nameFile){
    	for(int j=0;j<nameFile.length;j++)
    		deleteCloudFile(nameFile[j]);
    	//TODO OPTIMISER
     /*	
    	for(int i=0;i<this.files.size();i++){
    		CloudFile c =this.files.get(i);
    		for(int j=0;j<nameFile.length;j++){
	    		if(c.filename.contentEquals(nameFile[j])){
	    			Logger.info("delete file"+c.filename);
	    			this.files.remove(i);
	    			c.delete();
	    			break;
	    		}
    		}
    	}*/
    }
	/**
	 * store the answers for the recovery procedure, the answers are stored
	 * @param answ answers send by the user through the app during the set up
	 * @param quest indice of the questions he replied
	 * @throws Exception in case if he dedi not send enough answers
	 */
	public void setAnswers(String[] answ,int[] quest) throws Exception{
		if(answers==null)
			answers =new String[numberQuestion];
		if(numquestions==null)
			numquestions= new int[numberQuestion];
		if(questions.length==numberQuestion){
			
			for(int i=0;i<questions.length;i++){
				salts[i+1] = PasswordHash.getSalt();
				answers[i] = PasswordHash.createHash(answ[i].toCharArray(), salts[i+1]);
			}
			
		} 
		else
			throw new Exception("wrong number questions answered");
	}
	
	
	
    /**
     * Tries to find a user given the email identifier and the password
     * @param email
     * 		The email identifying the user
     * @param password
     * 		The password input from the user
     * @return
     * 		The user if found, null otherwise
     * @throws InvalidKeySpecException 
     * @throws NoSuchAlgorithmException 
     */
    public static User connect(String email, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
    //	User user = find("byEmail", email).first();
    	//String passwordHashed = PasswordHash.createHash(password.toCharArray(),user.salts[0]);
    	
    	logger.info("Connect user using email and password");
    	
        return find("byEmailAndPassword", email, password).first();
        
    }
    
    
    /**   * Tries to find a user given the email identifier 
     * @param email
     *  * 		The email identifying the user
     * @return
     */
    public static User connect(String email) {
        return find("byEmail", email).first();
    }
   
    /**
     * Set the user's main device
     * @param device
     * 		The device that should be marked as main
     */
   /* public void setMainDevice(Device device){
    	if(device == null || device.publicKey == null)
    		throw new NullPointerException("Device and device.publicKey can't be null");
    	this.publicKey = device.publicKey;
    }
    */
    
    /**
     * Get the user's main device
     */
    public Device getMainDevice(){
    	//TODO requete to get main device
    	return null; // Device.find("byOwnerAndPublicKey", this, this.publicKey).first();
    }
    
    
    /**
     * @param email identify the user
     * @return the list of questions he answered
     */
    public static String[] getQuestions(String email){
    	User user = find("byEmail", email).first();
    	String[] questions = new String[numberQuestion];
        if (user!=null && user.numquestions!=null ){
        	for(int i =0;i<numberQuestion;i++){
        		questions[i]=questions[user.numquestions[i]];
        	}
        }
        return questions;
    }

	public Device getDevice(Long id) {
		for(Device d:this.devices)
			if(d.id==id)
				return d;
		return null;
	}

	public CloudFile findCloudFile(String name) {
		for( CloudFile f:files)
			if(f.filename.contentEquals(name))
				return f;
		return null;
	}
}