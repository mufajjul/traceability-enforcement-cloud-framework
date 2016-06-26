package org.provenance.cloudprovenance.confidenshare.util;

import groovy.lang.Tuple;

import java.util.List;
/**
 * Groups utility methods to find session key and delegation keys that can be used between two different entities.
 * @author Boris Chazalet
 *
 */
public class FindKeyUtils {

	/**
	 * Checks if a session key exists within the list that was encrypted for a particular
	 * destination public key 
	 * @param listOfSessionKeys
	 * 		The list of keys (which should be from a file)
	 * @param destKey
	 * 		The destination public key
	 * @param fromOrigin
	 * 		if the key *must* be from origin. If this is true, it means that a re-encrypted key is not good enough for you
	 * @return
	 * 		True if such key exists, false if it does not
	 */
/*	public static boolean hasSomeForThatKey(List<SessionKey> listOfSessionKeys, Key destKey, boolean fromOrigin){
		if(getSomeForThatKey(listOfSessionKeys, destKey, fromOrigin) != null)
			return true;
		else 
			return false;
	}	
	*/
	/**
	 * Find a SessionKey in the list of session keys that was encrypted for the particular destKey.
	 * Note that fromOrigin = !multiHopMode. NOT TRUE!!! REFACTOR!!!!!
	 * @param list
	 * 		The list of keys (which should be from a file)
	 * @param destKey
	 * 		The destination public key
	 * @param fromOrigin
	 * 		if the key *must* be from origin. If this is true, it means that a re-encrypted key is not good enough for you
	 * @return
	 * 		null if not found or the SessionKey if found
	 */
	public static SessionKey getSomeForThatKey(SessionKey mSessionKey, Device destDevice, boolean fromOrigin){
		
		if (mSessionKey.destDevice.sha1Hash.contentEquals(destDevice.sha1Hash)){
			return mSessionKey;
		}
		else{
			//TODO create a session key 
		}
		//for(SessionKey key :listOfSessionKeys){
		//	if((!fromOrigin || key.isFromOrigin) && key.decryptingKey.equals(destKey)){
		//		return key;
		//	}
		//}
		return null;
	}
	
	/**
	 * Find a session key that can be re-encrypted using a device-to-device re-encryption key stored on the server
	 * @return
	 * 		A tuple containing first the session key and then the re-encryption key to be applied
	 */
	public static Tuple findSessionAndReencryptionKeyPair(Device destDevice, SessionKey candidateKey, boolean multiHopMode){ //, List<ReEncryptionKey> reencKeys){
		
		List<ReEncryptionKey> reencKeys = ReEncryptionKey.find("toWhom", destDevice).fetch();
		//for(SessionKey candidateKey : sessionKeys){
			for(ReEncryptionKey reencKey : reencKeys){
				if(reencKey.isDeviceToDevice && (candidateKey.isFromOrigin || multiHopMode) 
						){
					//TODO verifier la condition car change, ne doit pas marcher!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
					Object[] t = {candidateKey, reencKey}; 
					return new Tuple(t);
//					key = candidateKey;
//					rightReencKey = reencKey;
				}
			}
	//	}
		return null;
	}
}