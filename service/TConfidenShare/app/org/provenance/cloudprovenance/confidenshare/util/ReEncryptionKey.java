package org.provenance.cloudprovenance.confidenshare.util;

import java.util.Date;
import java.util.List;

import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import org.apache.commons.lang.NullArgumentException;
import org.apache.log4j.Logger;

import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * Re-encryption key (also called Delegation key) is the key that allows already
 * encrypted data to be re-encrypted towards another key
 * @author Boris Chazalet
 * 
 */
@javax.persistence.Entity
public class ReEncryptionKey extends Model {

	
	static Logger logger = Logger.getLogger("ReEncryptionKey");

	
	@Override
	public boolean equals(Object other) {
		// TODO Auto-generated method stub
		return hashOfdestPublicKey.contentEquals(((ReEncryptionKey)other).hashOfdestPublicKey);
	}

	/**
	 * The binary re-encryption key
	 */
	@Lob
	public byte[] key; //136 bytes
	
	/**
	 * The key that has generated the re-encryption key
	 */
	@ManyToOne
	public Device owner;
	
	/**
	 * The target key for which the key has been generated if he is registered to the server. Can be null.
	 */
	@ManyToOne
	public Device toWhom;
	
	/**
	 * Whether the key is used to re-encrypt data between the user's devices or not. 
	 */
	public boolean isDeviceToDevice = false;
	
	/**
	 * The hash of the receiver's public key (whether he is registered or not).
	 */
	@Required
	public String hashOfdestPublicKey;

	/**
	 * When the re-encryption was uploaded to the system.
	 */
	public Date createdAt;
	
    @PrePersist
    void updateModDates() {
    if (createdAt == null) // first time
    	createdAt = new Date();
    }	
    
    /**
     * Constructor to create re-encryption key between two users
     * @param fromWhom
     * @param hashOfDestPubKey
     * @param thekey
     */
   /* public ReEncryptionKey(User fromWhom, String hashOfDestPubKey, byte[] thekey){
    	this.owner = fromWhom.publicKey;
    	this.key = thekey;
    	this.hashOfdestPublicKey = hashOfDestPubKey;
    	User user = User.find("publicKey.sha1Hash", hashOfDestPubKey).first();
    	if(user != null){
    		Logger.info("The destination key belongs to a registered user. Saving that.");
    		this.toWhom = user.publicKey;
    	}
    }
    */
    
    /**
     * Constructor to create re-encryption between two devices of the same user
     * @param fromWhom
     * @param toWhom
     * @param theKey
     */
    public ReEncryptionKey(Device fromWhom, Device toWhom, byte[] theKey){
    	this.owner = fromWhom;
    	this.isDeviceToDevice = true;
    	this.key = theKey;
    	this.toWhom = toWhom;
    	this.hashOfdestPublicKey = toWhom.sha1Hash; // this is null because it is internal re-encryption key (device-to-device)
    }
    
    /**
     * Find a re-encryption key generated by Device origin for Device dest
     * @param origin
     * 		The device that has generated the key
     * @param dest
     * 		The device which the key was generated for
     * @return
     * 		The re-encryption key if found, null otherwise
     */
    public static ReEncryptionKey find(Device origin, Device dest){
    	if(origin == null || dest == null)
    		throw new NullArgumentException("Device " + ((origin == null)?"origin":"dest"));
    	return ReEncryptionKey.find("byOwnerAndToWhom", origin, dest).first();
    }
    
   /** Find a re-encryption key corresponding from device belongings to the same user
    * 
 * @param user
 * @param origin
 * @return  key reencrypting from origin to a device owned by the user
 */
public static ReEncryptionKey findFromSameUser(Device origin){
	   ReEncryptionKey key = null;
		 List<ReEncryptionKey> reEncKeys = ReEncryptionKey.find("byOwner", origin ).fetch();
		 for(ReEncryptionKey k:reEncKeys){
			 if(k.toWhom.owner.email.contentEquals(origin.owner.email));
			 {
				 key = k;
				 break;
			 }
		 }
		 return key;
   }
    
    /**
     * Find a re-encryption key
     * @return
     * 		The re-encryption key
     * @deprecated You can't find a re-encryption by owner anymore, since 1.1. You need to go through devices.
     */
    public static ReEncryptionKey find(User fromWhom, String destPublicKey){
		return ReEncryptionKey.find("byOwnerAndHashOfdestPublicKey", fromWhom, destPublicKey ).first();
    	//ReEncryptionKey res = ReEncryptionKey.find(query, params);
    }
}