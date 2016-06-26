package org.provenance.cloudprovenance.confidenshare.util;

import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import play.db.jpa.Model;

/**
 * Represent a -encrypted- symmetric session key that was used to encrypt a whole file.
 * @author Boris Chazalet
 *
 */
@javax.persistence.Entity
public class SessionKey extends Model {

	/**
	 * The symmetric session key used to encrypt the file
	 */
	@Lob //Without it I am getting a limit of 255 bytes --> encrypted key is 281
	public byte[] binaryKey;
	
	/**
	 * The key that can decrypt the session key.
	 */
	@OneToOne
	public Device destDevice;  //Key decryptingKey;
	
	/**
	 * Says whether the session key was encrypted *directly* by the pair of key whose public key is stored (@see decryptingKey).
	 * Otherwise (false), it means that the session key was re-encrypted at least once.
	 * This will be useful to know for single-hop mode.
	 */
	public boolean isFromOrigin;
	
	/**
	 * The file that was encrypted with the session key (i.e. the decrypted session key can be used to decrypt the file) 
	 */
	@OneToOne
	public CloudFile carrier;
	
	/**
	 * 
	 * @param key
	 * 		The actual -encrypted- binary key as an array of bytes
	 * @param destKey
	 * 		The key that can decrypt the se
	 */
	public SessionKey(CloudFile c ,byte[] key, Device destDevice, boolean isFromOrigin){
		this.binaryKey = key;
		this.destDevice = destDevice;
		this.isFromOrigin = isFromOrigin;
		this.carrier = c;
	}

	/**
	 * Returns the key as base64
	 */
	public String getKeyAsBase64() {
		return Base64.encode(binaryKey);
	}
}
