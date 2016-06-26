package org.provenance.cloudprovenance.confidenshare.util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Arrays;

import com.orangelabs.crypto.proxyreencryption.singlehop.*;
import com.orangelabs.crypto.proxyreencryption.singlehop.params.*;
import com.orangelabs.crypto.proxyreencryption.singlehop.params.ProxyParameters;

import com.orangelabs.crypto.proxyreencryption.singlehop.params.ProxyUserKeyPair;
import com.orangelabs.crypto.proxyreencryption.singlehop.params.ProxyUserPrivateKey;
import com.orangelabs.crypto.proxyreencryption.singlehop.params.ProxyUserPublicKey;
import com.orangelabs.crypto.proxyreencryption.singlehop.params.UserKeyGenerator;

import com.orangelabs.crypto.proxyreencryption.singlehop.ReencryptProxy;
import com.orangelabs.crypto.proxyreencryption.singlehop.params.ProxyParameters;

import play.Play;

public class ReencryptionUtils {
    
    /**
     * Rencrypts a session key.
     * @param sessionKey
     *          The original, public-key encrypted, session key
     * @param rreKey
     *          The re-encryption key geneated by the private key
     * @return
     */
    public static byte[] reencryptSessionKey(byte[] sessionKey, byte[] rreKey){
        ReencryptionManager rreMng = ReencryptionManager.getInstance();
        return rreMng.reencrypt(sessionKey, rreKey);
    }
    
    public static byte[] extractReencKeyFromFile(File in) throws IOException{

        return extractByteFromFile(in, (int) in.length());
    }
    
    public static byte[] extractPublicKeyFromFile(File in) throws IOException{
        return extractByteFromFile(in, (int) in.length());
    }
    
    /**
     * Extracts a encrypted session key from a file that is supposed to contained one.
     * @throws IOException
     */
    public static byte[] extractSessionKeyFromFile(File in) throws IOException{
    	return extractByteFromFile(in, (int) in.length());
}

    
    private static byte[] extractByteFromFile(File in, int size)  throws IOException{
        FileInputStream fis = new FileInputStream(in); //File InputStream should not be here --> TODO refactor into a extracting function
        byte[] rreKeyBuf = new byte[size];
        fis.read(rreKeyBuf, 0, size);
        fis.close();
        return rreKeyBuf;
    }
    
    public static boolean isReencryptionEnable(){
        String fromConf = (String) Play.configuration.get("application.reencMode");
        if(fromConf.equals("true"))
            return true;
        else
            return false;
    }
    
    /**
     * Reads the hop mode from configuration file
     * @return
     *    true if library is multi-hop, false if single-hop
     */
    public static boolean isMultiHopMode(){
    	String s = (String) Play.configuration.get("application.pre_multihop");
    	return (s.equals("true")?true:false);
    }
    
}