/**
 * Reencryption Manager manages the re-encryption library and the native calls to the library.
 * All methods are private or protected. Use ReencryptionUtils to access them from outside the re-encryption functionalities from outside the package.
 * @author bchazalet
 * 
 */
package org.provenance.cloudprovenance.confidenshare.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;

import org.apache.log4j.Logger;

import com.francetelecom.rd.crypto.params.SECNamedCurves;
import com.francetelecom.rd.crypto.params.X9ECParameters;
import com.francetelecom.rd.math.ECPointExt;
import com.orangelabs.crypto.proxyreencryption.singlehop.ReencryptProxy;
import com.orangelabs.crypto.proxyreencryption.singlehop.params.ProxyParameters;
import com.orangelabs.crypto.proxyreencryption.singlehop.params.ProxyReencryptionKey;
import com.orangelabs.crypto.proxyreencryption.singlehop.params.SecondLevelCiphertext;


/**
 * Reencryption Manager manages the re-encryption library and the native calls to the library.
 * All methods are private or protected. Use ReencryptionUtils to access them from outside the re-encryption functionalities from outside the package.
 * @author bchazalet
 * 
 */
public class ReencryptionManager {
    
    private static ReencryptionManager singleOne = new ReencryptionManager();
    
    
    static ReencryptProxy proxy;
    static ProxyParameters params; 

    static Logger logger = Logger.getLogger("ReencryptionManager");

    

    public ProxyParameters getParams() {
        return params;
    }

    private ReencryptionManager() {
        File paramFile = new File("params_config");
        if(paramFile.exists() == false) {
           logger.info("Parameter file not found. Generate new one."); 
           generateAndSaveParams(paramFile);
        }
        logger.info("Params"); 
        params = readParamsFromFile(new File("params_config"));
        logger.info("Proxys"); 
        proxy = new ReencryptProxy(params);
    }
    
    protected static ReencryptionManager getInstance(){
        return singleOne;
    }
    
//    protected String reencrypt(String encryptedContent, String reencryptionKey){
//        byte[] reencryptedContent = proxy.reEncrypt(
//                (ProxyReencryptionKey) ProxyReencryptionKey.fromBinary(params, reencryptionKey.getBytes()),
//                SecondLevelCiphertext.fromBinary(params, encryptedContent.getBytes())).toBinary();
//        if(reencryptedContent.length > 0){
//          return new String(reencryptedContent);
//      }
//      // Unsuccessfull
//        return null;
//    }
    
    protected byte[] reencrypt(byte[] encryptedContent, byte[] reencryptionKey){
        logger.info("Reencrypting");
//      logger.info("Encrypted Content: " + HashUtils.byteArray2Hex(encryptedContent));
//      logger.info("Reencryption key: " + HashUtils.byteArray2Hex(reencryptionKey));
        
        byte[] reencryptedContent = proxy.reEncrypt(
                (ProxyReencryptionKey) ProxyReencryptionKey.fromBinary(params, reencryptionKey),
                SecondLevelCiphertext.fromBinary(params, encryptedContent)).toBinary();
        
        if(reencryptedContent != null && reencryptedContent.length > 0){
//          logger.info("Reencrypted content: " + HashUtils.byteArray2Hex(reencryptedContent));
            return reencryptedContent;
        }
        logger.info("Failing... the reencrypting is returning null or < 0");
        // Unsuccessfull
        return null;
    }
  
    private static ProxyParameters readParamsFromFile(File paramFile) {
        logger.info("Read params from file");
        try {
            InputStream fis = new FileInputStream(paramFile);
            DataInputStream dis = new DataInputStream(new BufferedInputStream(fis));
            logger.info("ProxyParameters");
            ProxyParameters params = ProxyParameters.fromDataStream(dis);
            dis.close();
            return params;
        } catch (Exception e) {
            logger.error("Pb during file read :"+e.getMessage());
            e.printStackTrace();
        }
        return null;
    } 
    
    
    public static void generateAndSaveParams(File paramFile) {
        logger.info("Generate new parameters for proxyReencryption");
        String curveName = "secp192r1";
        String hashAlgo = "SHA-512";
//        SecureRandom rand = new SecureRandom();
 
        X9ECParameters ecSpec = SECNamedCurves.getByName(curveName);
        ECPointExt bigG = new ECPointExt(ecSpec.getG());
//        ECCurve.Fp curve = (ECCurve.Fp) bigG.getCurve();
        BigInteger q = ecSpec.getN();

        int mSize = 16; // bytes

        ProxyParameters params = new ProxyParameters(bigG, q, mSize, 140, hashAlgo);
//        params.doPrecomputation(9, 5);
//        System.out.println("Params: \n" + params.toString());

        logger.info("Write params in file");
        try {
            FileOutputStream fos = new FileOutputStream(paramFile);
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(fos));
            params.toDataStream(dos);
            dos.close(); 
        } catch (Exception e) {
            logger.error("Pb during file save");
            e.printStackTrace();
        }
    }
}