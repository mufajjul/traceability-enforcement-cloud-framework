package org.provenance.cloudprovenance.confidenshare.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import play.db.jpa.Model;

import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * Represents a device. A device belongs to one User and has an associated Key.
 * @author Boris Chazalet
 *
 */
@javax.persistence.Entity
public class Device extends Model {
	
	
	static Logger logger = Logger.getLogger("Device");


	@Override
	public String toString() {
		String s = "Device name: "+this.deviceName+", ismain: "+this.isMainDevice+", isActive: "+ this.isActive;
		return s;
	}

	@SuppressWarnings("unused")
	@Override
	public <T extends JPABase> T delete() {
		
		List<SessionKey> sessionKeys = SessionKey.find("byDestDevice",this).fetch();
		ReEncryptionKey mReEncryptKey = ReEncryptionKey.findFromSameUser(this);
		if(owner.devices.size()>1 && mReEncryptKey!=null){
			logger.info("owner.devices.size()>1 && mReEncryptKey!=null");
			byte[] sessionkeyRencrypt=null;
			for(SessionKey session :sessionKeys ){
				//sessionkeyRencrypt = ReencryptionUtils.reencryptSessionKey(session.binaryKey,mReEncryptKey.key);
				session.isFromOrigin=false;
				//session.binaryKey = sessionkeyRencrypt;
				session.destDevice = mReEncryptKey.toWhom;
				session.save();
				//TODO envoyer pushnotification.
			}
		}
		else {
			logger.info("else owner.devices.size()>1 && mReEncryptKey!=null");
			for(SessionKey session :sessionKeys ){
				logger.info("delete cloudfile & session");
				session.carrier.delete();
			}
		}
		
	//	this.reEncyptKeysOwn.clear();
	//	this.reEncyptKeysWhom.clear();
	//	this.save();
		
		return super.delete();
	}


	/**
	 * When the device was first registered
	 */
	public Date createdAt=null;
	
	/**
	 * Last time the device was updated
	 */
	public Date updatedAt = null;
	
	/**
	 * The client-generated unique device Id.
	 */
	public String deviceId;
	
	
	/**
	 * The client-generated unique device Id.
	 */
	public String deviceName;
	
	/**
	 * The sha1 hash of the public key
	 */
	public String sha1Hash;

	/**
     * All the user's files stored in the cloud
     */
    @OneToMany(mappedBy="owner", cascade=CascadeType.ALL)
    public List<ReEncryptionKey> reEncyptKeysOwn;
    
    /**
     * All the user's files stored in the cloud
     */
    @OneToMany(mappedBy="toWhom", cascade=CascadeType.ALL)
    public List<ReEncryptionKey> reEncyptKeysWhom;
    
	/**
	 * The actual public key
	 */
	@Lob
	public byte[] key; //
	/**
	 * Size of the key
	 */
	public int size; //in bits
	
    
	
	/**
	 * The owner of the device. A device must be registered for a given user.
	 */
	@ManyToOne
	public User owner;
	
	/**
	 * The C2DM registration id if any
	 */
	public String c2dmRegitrationId=null;
	
	/**
	 * A device is active until it is reported as lost. 
	 * It will then becomes inactive (or disabled) until the restoration process is done and device is removed from DB.
	 */
	public boolean isActive;
	
	
	/**
	 * If it is the main device, it receives confirmation for other device
	 */
	public boolean isMainDevice;
	
	@PreUpdate
	@PrePersist
	void updateModDates() {
	    if (createdAt == null) // first time
	    	createdAt = new Date();
	    else
	    	updatedAt = new Date();
	}
	
	public Device(User user,byte[] key, String deviceId,String nameDevice, boolean isMain){
		this.deviceId = deviceId;
		this.owner = user;
		this.isActive = isMain;
		this.isMainDevice = isMain;
		this.deviceName = nameDevice;
		this.createdAt = new Date();
		this.key = key;
		this.size = key.length;
    	this.sha1Hash = HashUtils.calculateHashAsString(key);
    	reEncyptKeysWhom =new ArrayList<ReEncryptionKey>();
    	reEncyptKeysOwn = new ArrayList<ReEncryptionKey>();
	}
	
	
	
	public void  setName(String name){
		this.deviceName = name;
		this.save();
	}
	/**
	 * Returns whether the device is the user's main device or not
	 * @return
	 * 		true if this is the user's main device, false otherwise
	 */
	public boolean isMain(){
		return  isMainDevice;  /////publicKey.equals(owner.publicKey);
	}
	
	/**
	 * Find all files that only have a session key encrypted for this device.
	 * @return
	 * 		The list of all files that should be restored in device is lost.
	 * 		Note: every file in the list has one and only one session key.
	 */
	public List<CloudFile> findFilesToRestore(){
		Query query = JPA.em().createQuery(
				"select distinct f from CloudFile f, SessionKey sk, Device d " +
				"where sk.carrier = f and sk.decryptingKey = d.publicKey and d = ?1 " +
				"and f in " +
				"(select distinct ff from CloudFile ff, SessionKey sk2 " +
				"where sk2.carrier = ff " +
				"group by ff " +
				"having count(sk2) = 1" +
				")");
		query.setParameter(1, this);
	    return query.getResultList();
	}
	
	/**
	 * Disable the device (isActive becomes false) and *save* it.
	 */
	public void disable(){
		this.isActive = false;
		this.save();
	}

	public void setActive() {
		this.isActive = true;
		this.save();
		
	}
	
	
    /**
     * Returns the key as base64
     */
    public String getKeyAsBase64(){
    	return Base64.encode(key);
    	//return "keyAsBase64-to-be-implemented";
    }

	public void addReEncryptionKey(Device destDevice, byte[] rekey) {
		ReEncryptionKey k = new ReEncryptionKey(this,destDevice , rekey);
		k.save();
		reEncyptKeysOwn.add(k);
		this.save();
		destDevice.reEncyptKeysWhom.add(k);
		destDevice.save();
	}
	
	public void deleteReEncryptionKey(String emailDest) {
		int i=0;
		ReEncryptionKey key = null;
		while(i< this.reEncyptKeysOwn.size()){
			key = this.reEncyptKeysOwn.get(i);
			if(key.toWhom.owner.email.contentEquals(emailDest))
			{

				this.reEncyptKeysOwn.remove(key);
				key.delete();
			}
			else i++;
		}
		this.save();
	}
	/*public void deleteReEncryptionKey(){
		
	}*/
}
