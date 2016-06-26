package org.provenance.cloudprovenance.confidenshare.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Groups utility methods that have to do with hashing.
 * @author Boris Chazalet
 *
 */
public class HashUtils {

	/**
	 * Calculates the SHA1 hash of a given byte array and convert it to a hexadecimal String.
	 * @param toHash
	 * 		The content to hash
	 * @return
	 * 		The hash value as a String
	 */
	public static String calculateHashAsString(byte[] toHash){
		// read the file and update the hash calculation
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			ByteArrayInputStream streamToHash = new ByteArrayInputStream(toHash);
			DigestInputStream dis = new DigestInputStream(streamToHash, sha1);
			while (dis.read() != -1);
			// get the hash value as byte array
			byte[] hash = sha1.digest();
			return byteArray2Hex(hash);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Calculates the SHA1 hash of a given byte array.
	 * @param toHash
	 * 		The content to hash
	 * @return
	 * 		The hash value as a byte array
	 */
	public static byte[] calculateHash(byte[] toHash) {
		MessageDigest sha1;
		try {
			sha1 = MessageDigest.getInstance("SHA1");
			ByteArrayInputStream streamToHash = new ByteArrayInputStream(toHash);
			DigestInputStream dis = new DigestInputStream(streamToHash, sha1);

			// read the file and update the hash calculation
			while (dis.read() != -1);

			// get the hash value as byte array
			return sha1.digest();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}

	/**
	 * Converts a byte array into a hexadecimal string
	 * @param hash
	 * @return
	 */
	public static String byteArray2Hex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}
}
