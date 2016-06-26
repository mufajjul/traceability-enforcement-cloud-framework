package org.provenance.cloudprovenance.confidenshare.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.log4j.Logger;

import play.db.jpa.Model;

/**
 * Represents a user file stored in the cloud
 * @author bchazalet
 *
 */
/**
 * @author gve
 * 
 */
@javax.persistence.Entity
public class CloudFile extends Model {

	/**
	 * Parent folder containing all user's folder where files are stored.
	 */
	public static final String STORED_FILE_FOLDER = "storedFiles/";

	static Logger logger = Logger.getLogger("CloudFile");

	/**
	 * The zip entry name for the key
	 */
	public static final String KEY_ENTRY = "key";
	// private static final String FILE_ENTRY = "file";

	/**
	 * From where to start counting (linear generation of uuid)
	 */
	private static int uuid_count = 2001;

	/**
	 * Name of the file (including the extension). In clear.
	 */
	public String filename;

	/**
	 * Last updated date
	 */
	public Date updatedAt;

	/**
	 * Creation date (first upload from user)
	 */
	public Date createdAt;

	/**
	 * Size of the file
	 */
	public double size; // in Bytes

	/**
	 * Id of the file within the server (fileId)
	 */
	public String uuid;

	/**
	 * Whether the file is encrypted or not in the filesystem
	 */
	public boolean isEncrypted;

	/**
	 * Stores the different session keys, i.e. the key for each device
	 */
	// @OneToMany(mappedBy="carrier", cascade=CascadeType.ALL)//cascade =
	// {CascadeType.PERSIST, CascadeType.REMOVE})
	// public List<SessionKey> sessionKeys;
	// @JoinColumn(nullable=false) //Can't have a file without a session keys
	@OneToOne(mappedBy = "carrier", cascade = CascadeType.ALL)
	SessionKey sessionKey;
	/**
	 * The owner of the file
	 */
	@JoinColumn(nullable = false)
	// Can't have a file without owner
	@ManyToOne
	public User owner;

	/**
	 * All the user's files stored in the cloud
	 */
	@OneToMany(mappedBy = "originalFile", cascade = CascadeType.ALL)
	public List<SharedFile> sharedfiles;

	@PreUpdate
	@PrePersist
	void updateModDates() {
		if (createdAt == null) // first time
			createdAt = new Date();
		else
			updatedAt = new Date();
	}

	public String getUUID() {
		return uuid;
	}

	/**
	 * Constructor for a cloudfiel
	 * 
	 * @param name
	 *            namne of the file
	 * @param size
	 *            length of the fyle
	 * @param key
	 *            the session key
	 * @param origin
	 *            if it is a session which will be save
	 * @param d
	 */
	public CloudFile(User user, String name, int size) {
		this.filename = name;
		this.size = size;
		this.uuid = generateNewUUID();
		this.sessionKey = null;
		this.owner = user;
		this.sharedfiles = new ArrayList<SharedFile>();
		// this.sessionKeys = new ArrayList<SessionKey>();
	}

	public void addSessionKey(byte[] key, Boolean origin, Device d) {
		this.sessionKey = new SessionKey(this, key, d, origin);
		this.sessionKey.save();
	}

	public SharedFile addSharedFile(User user) {
		logger.info("add shared file, before  %s: "+ sharedfiles.size());
		SharedFile sharedfile = new SharedFile(this, user, null);
		sharedfiles.add(sharedfile);
		sharedfile.save();
		// user.AddSharedfolderToUser(sharedfile);
		logger.info("add shared file, after  %s: "+ sharedfiles.size());
		return sharedfile;
	}

	public void deleteSharedFile(User user) {
		logger.info("in delete shared file contact ;ail %s : "+ user.email);
		SharedFile s = null;
		int i = 0;
		while (i < this.sharedfiles.size()) {

			s = this.sharedfiles.get(i);
			logger.info(s.userShared.email);

			if (s.userShared.email.contentEquals(user.email)) {
				logger.info("delete shared file " + this.filename + " from "
						+ this.owner.fullname + " to " + user.fullname);
				s.delete();
				this.sharedfiles.remove(i);
				logger.info("size sharefile : " + this.sharedfiles.size());
				this.save();
			} else
				i++;
		}
	}

	@Override
	public CloudFile save() {
		// this.sessionKey.save();
		return super.save();
	}

	@Override
	public String toString() {
		String s = "cloudfile name " + this.filename + ", nb de sharedfile"
				+ this.sharedfiles.size();
		return s;
	}

	/**
	 * Generate a new UUID for a new CloudFile. For now, just an incremental
	 * number.
	 * 
	 * @return The uuid as a String.
	 */
	public static String generateNewUUID() {
		// long nb = CloudFile.count();
		// long newId = uuid_count+nb+1;
		int newId = uuid_count;
		if (CloudFile.count() > 0) {
			CloudFile cf = CloudFile.find("order by uuid desc").first();
			newId = Integer.parseInt(cf.uuid) + 1;
		}
		return "" + newId;
	}

	/**
	 * Constructs a new SharedFile version of this file
	 * 
	 * @return A new SharedFiled pointing to this CloudFile
	 */
	public SharedFile share(String hashOfReceiverKey) {
		// TODO update
		return null; // new SharedFile(this, hashOfReceiverKey);
	}

	public void updateSessionKey(byte[] key, Device destDevice) {
		this.sessionKey.binaryKey = key;
		this.sessionKey.destDevice = destDevice;
	}

	public SessionKey getSessionKey() {
		// TODO Auto-generated method stub
		return this.sessionKey;
	}

	/**
	 * Adds a session key for the file
	 * 
	 * @param ssKey
	 *            The actual session key to add
	 * @deprecated
	 */
	/*
	 * public void addSessionKey(SessionKey ssKey){ //
	 * this.sessionKeys.add(ssKey); ssKey.carrier = this; // this.save(); }
	 */

	/**
	 * Delete all session keys for this file
	 */
	/*
	 * public void removeAllSessionKeys(){ // At least two ways: hql or java
	 * loop for(SessionKey ssKey : this.sessionKeys){ ssKey.carrier = null;
	 * ssKey.save(); } this.sessionKeys.clear(); }
	 */

	/*
	 * @Override public <T extends JPABase> T delete() {
	 * logger.info("delete sessionkey"); //this.sessionKey.delete(); return
	 * super.delete(); }
	 */

	/**
	 * Builds and add a session key for the file
	 * 
	 * @param key
	 *            The raw session key
	 * @param destKey
	 *            The key that can decrypt the session key
	 * @param fromOrigin
	 *            Whether the key is original (true if it is)
	 * @return
	 */
	/*
	 * public SessionKey addSessionKey(byte[] key, Key destKey, boolean
	 * fromOrigin ){ SessionKey ss = new SessionKey(key, destKey, fromOrigin);
	 * ss.carrier = this; this.sessionKeys.add(ss); return ss; //return this; //
	 * The play! tutorial had this but I don't understand why. }
	 */
	/**
	 * Remove existing session keys from the file and the DB.
	 * 
	 * @param destKey
	 *            The decryptingKey of the session keys that should be removed
	 * @return the number of keys that were removed
	 */
	/*
	 * public int removeSessionKey(Key destKey){ List<SessionKey> keys =
	 * SessionKey.find("byDecryptingKey", destKey).fetch(); for(SessionKey key:
	 * keys){ key.carrier = null; this.sessionKeys.remove(key); key.delete(); }
	 * return keys.size(); }
	 */
	/**
	 * Checks whether a file already exist for the given user
	 * 
	 * @param user
	 * 
	 * @param filename
	 *            The name of the file we are looking for
	 * @return
	 */
	public static boolean doesAlreadyExist(User user, String filename) {
		CloudFile existingFile = CloudFile.getByFilename(user, filename);
		if (existingFile == null)
			return false;
		else
			return true;
	}

	/**
	 * Find a file by name and user
	 * 
	 * @param user
	 *            The owner of the file you are looking for
	 * @param filename
	 *            The name of the file you are looking for
	 * @return
	 */
	public static CloudFile getByFilename(User user, String filename) {
		return find("byOwnerAndFilename", user, filename).first();
	}

	public SharedFile getsharedfile(User user) {
		for (SharedFile file : this.sharedfiles)
			if (file.userShared.equals(user))
				return file;
		return null;
	}

	public boolean isSharedWith(User user) {
		for (SharedFile file : this.sharedfiles)
			if (file.userShared.equals(user))
				return true;
		return false;
	}

}
