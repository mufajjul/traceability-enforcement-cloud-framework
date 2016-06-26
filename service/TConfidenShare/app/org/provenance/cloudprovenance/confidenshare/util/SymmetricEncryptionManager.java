package org.provenance.cloudprovenance.confidenshare.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class SymmetricEncryptionManager {

	private static final String TAG = "SDC";

	static Logger logger = Logger.getLogger("SymmetricEncryptionManager");

	public SymmetricEncryptionManager() {

		BasicConfigurator.configure();
	}

	/**
	 * Generates new symmetric key using 256-bit AES
	 * 
	 * @return Secret key object for use in encrypting files
	 */
	public SecretKey generateNewSymmetricKey() {
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance("AES");
			// kg.init(256);
			// get size in bits of key according to proxy parameters
			int keySize = 128; // ReencryptionManager.getInstance().getParams().mSize()
								// * 8;
			logger.info("SDC " + " Generate " + keySize + " bytes AES key");
			kg.init(keySize);
			SecretKey sk = kg.generateKey();
			return sk;
		} catch (NoSuchAlgorithmException e) {
			logger.error(TAG
					+ ":You're out of luck, AES isn't implemented on your system!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Simple wrapper to getEncoded() function
	 * 
	 * @param sk
	 *            Secret key to be serialized
	 * @return Byte buffer containing serialized SecretKey object
	 */
	public byte[] serializeSymmetricKey(SecretKey sk) {
		return sk.getEncoded();
	}

	/**
	 * Deserializes symmetric key from key buffer
	 * 
	 * @param skBuffer
	 *            Serialized symmetric key in byte buffer
	 * @return SecretKey object for use with cipher
	 */
	public SecretKey deserialize(byte[] skBuffer) {
		SecretKeySpec sk = new SecretKeySpec(skBuffer, "AES");
		return sk;
	}

	/**
	 * Encrypts the provided message using the given SecretKey
	 * 
	 * @param message
	 *            Message to be encrypted as byte buffer
	 * @param sk
	 *            SecretKey to be used in encryption
	 * @return Encrypted message as byte buffer
	 */
	public byte[] encrypt(byte[] message, SecretKey sk) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, sk);
			byte[] cipherText = new byte[cipher.getOutputSize(message.length)];
			cipherText = cipher.doFinal(message);
			return cipherText;
		} catch (IllegalBlockSizeException e) {
			logger.error(TAG + ": Change your block size");
			logger.error(TAG + e.getMessage());
		} catch (BadPaddingException e) {
			logger.error(TAG + "Bad padding");
			logger.error(TAG + e.getMessage());
		} catch (InvalidKeyException e) {
			logger.error(TAG + "The key provided is invalid");
			logger.error(TAG + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			logger.error(TAG
					+ "You're out of luck, AES is not implemented on your system");
			logger.error(TAG + e.getMessage());
		} catch (NoSuchPaddingException e) {
			logger.error(TAG + "Padding invalid");
			logger.error(TAG + e.getMessage());
		}
		return null;
	}

	public CipherOutputStream encrypt(OutputStream out, SecretKey sk,
			String params) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(params);
			cipher.init(Cipher.ENCRYPT_MODE, sk);
			// cipher.doFinal();
		} catch (NoSuchAlgorithmException e) {
			logger.error(TAG
					+ "Out of luck, AES isn't implemented on your system!");
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			logger.error(TAG + "Padding invalid");
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			logger.error(TAG + "Invalid key provided");
			e.printStackTrace();
		}

		CipherOutputStream cipherOut = new CipherOutputStream(out, cipher);
		return cipherOut;
	}

	/**
	 * 
	 * @param out
	 * @param sk
	 * @return
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public CipherOutputStream encrypt(OutputStream out, SecretKey sk) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, sk);
			// cipher.doFinal();
		} catch (NoSuchAlgorithmException e) {
			logger.error(TAG
					+ "Out of luck, AES isn't implemented on your system!");
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			logger.error(TAG + "Padding invalid");
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			logger.error(TAG + "Invalid key provided");
			e.printStackTrace();
		}

		CipherOutputStream cipherOut = new CipherOutputStream(out, cipher);
		return cipherOut;
	}

	/**
	 * 
	 * @param cipher
	 *            Ciphertext to be decrypted
	 * @param sk
	 *            Symmetric key to be used in decryption
	 * @return Byte buffer containing decrypted content
	 */
	public byte[] decrypt(byte[] cipherBytes, SecretKey sk) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, sk);
			byte[] plainText = new byte[cipher
					.getOutputSize(cipherBytes.length)];
			plainText = cipher.doFinal(cipherBytes);
			return plainText;
		} catch (BadPaddingException e) {
			logger.error(TAG + ": Bad padding");
			logger.error(TAG + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			logger.error(TAG
					+ " : Out of luck, AES isn't implemented on your system!");
			logger.error(TAG + e.getMessage());
		} catch (NoSuchPaddingException e) {
			logger.error(TAG + " : No such padding");
			logger.error(TAG + e.getMessage());
		} catch (InvalidKeyException e) {
			logger.error(TAG + " : Invalid key provided");
			logger.error(TAG + e.getMessage());
		} catch (IllegalBlockSizeException e) {
			logger.error(TAG + " : Illegal block size");
			logger.error(TAG + e.getMessage());
		}
		return null;
	}

	public CipherInputStream decrypt(InputStream fileStream, SecretKey sk,
			String params) {
		Cipher cipher = null;
		/* TODO: Add Log code to catch blocks */
		try {
			cipher = Cipher.getInstance(params);
			cipher.init(Cipher.DECRYPT_MODE, sk);

		} catch (NoSuchAlgorithmException e) {
			logger.error(TAG
					+ " : Out of luck, AES isn't implemented on your system!");
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			logger.error(TAG + " :No such padding");
			logger.error(TAG + e.getMessage());
		} catch (InvalidKeyException e) {
			logger.error(TAG + " : Invalid key provided");
			logger.error(TAG + e.getMessage());
		}
		CipherInputStream cipherIn = new CipherInputStream(fileStream, cipher);
		return cipherIn;
	}

	/**
	 * Given an InputStream and a symmetric key, decrypts the file using
	 * CipherStreams
	 * 
	 * @param fileStream
	 *            InputStream corresponding to file to be decrypted
	 * @param sk
	 *            Symmetric key
	 * @return InputStream representing the decrypted file
	 */
	public CipherInputStream decrypt(InputStream fileStream, SecretKey sk) {
		Cipher cipher = null;
		/* TODO: Add Log code to catch blocks */
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, sk);

		} catch (NoSuchAlgorithmException e) {
			logger.error(TAG
					+ " : Out of luck, AES isn't implemented on your system!");
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			logger.error(TAG + " : No such padding");
			logger.error(TAG + e.getMessage());
		} catch (InvalidKeyException e) {
			logger.error(TAG + " : Invalid key provided");
			logger.error(TAG + e.getMessage());
		}
		CipherInputStream cipherIn = new CipherInputStream(fileStream, cipher);
		return cipherIn;
	}

	public static void main(String args[]) throws IOException {

		SymmetricEncryptionManager sem = new SymmetricEncryptionManager();

		SecretKey sk = sem.generateNewSymmetricKey();

		logger.info("Secret key ==> algo: " + sk.getAlgorithm());

		logger.info("Secret key ==> key : " + sk.getEncoded());

		byte[] byteMsg = ("This is a test").getBytes();

		logger.info("byte plain text ==> " + new String(byteMsg));

		byte[] enbyteMsg = sem.encrypt(byteMsg, sk);

		logger.info("byte cipher text ==> " + new String(enbyteMsg));

		byte[] decbyteMsg = sem.decrypt(enbyteMsg, sk);

		logger.info("byte dec text ==> " + new String(decbyteMsg));

		File plainFile = new File("src/main/resource/temp.txt");

		File enFile = new File("src/main/resource/temp-en.dat");

		//encrypt
		
		InputStream iStream = new FileInputStream(plainFile);
		CipherOutputStream cio = sem.encrypt(new FileOutputStream(enFile), sk);

		int numRead = 0;
		byte[] buf = new byte[1024];
		while ((numRead = iStream.read(buf)) >= 0) {
			cio.write(buf, 0, numRead);
		}

		cio.close();
		iStream.close();

		// decrypt
		File dePlainFile = new File("src/main/resource/temp-de.txt");

		CipherInputStream cos = sem.decrypt(new FileInputStream(enFile), sk);
		OutputStream oStream = new FileOutputStream(dePlainFile);

		numRead = 0;
		while ((numRead = cos.read(buf)) >= 0) {
			oStream.write(buf, 0, numRead);
		}
		cos.close();
		oStream.close();

	}

}
