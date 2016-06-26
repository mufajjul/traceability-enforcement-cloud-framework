package org.provenance.cloudprovenance.confidenshare.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Groups all utility methods usefull for zip operation
 * @author Boris Chazalet
 *
 */
public class ZipUtils {

	/**
	 * Read a String from a text-file zip entry (of a ZipInputStream)
	 * @param zipStream
	 * @param entrySize
	 * 			The size of the entry (in bytes)
	 * @return
	 * 			The String extracted from the entry
	 * @throws IOException
	 */
	public static String readStringFromEntry(ZipInputStream zipStream, int entrySize) throws IOException{
		byte[] tmpArray = new byte[entrySize];
    	zipStream.read(tmpArray, 0, entrySize);
    	return new String(tmpArray);		
	}
	
	/**
	 * Read a byte[] from a text-file zip entry (of a ZipInputStream)
	 * @param zipStream
	 * @param entrySize
	 * 			The size of the entry (in bytes)
	 * @return
	 * 			The byte[] extracted from the entry
	 * @throws IOException
	 */
	public static byte[] readByteArrayFromEntry(ZipInputStream zipStream, int entrySize) throws IOException{
		byte[] tmpArray = new byte[entrySize];
    	zipStream.read(tmpArray, 0, entrySize);
    	return tmpArray;
	}
	
	/**
	 * Reads a zip entry and saves it as a file on the file system
	 * @param zipStream
	 * 			The ZipInputStream
	 * @param pathname
	 * 			Where to save the file (inclusing the name of the file)
	 * @throws IOException
	 */
	public static void readandSaveFileFromEntry(ZipInputStream zipStream, String pathname) throws IOException{
		int bufferSize = 1024;
		int count = 0;
		byte[] data = new byte[bufferSize];
		//We should use entry.getName() should we? Rather the actual filename? Zip name?
		File newFile = new File(pathname); //new File(STORED_FILE_FOLDER, entry.getName());
//		System.out.println("Saving the encrypted file at: " +newFile.getAbsolutePath());
		FileOutputStream fout = new FileOutputStream(newFile);
		while ((count = zipStream.read(data, 0, bufferSize)) != -1) {
			fout.write(data, 0, count);
		}
		fout.flush();
		fout.close();		
	}
	/**
	 * Add a file to a zip output stream
	 * @param zipOutput
	 * 			The ZipOutputStream
	 * @param entryName
	 * 			The name of the new zip entry		
	 * @param path
	 * 			The path where to read the file from
	 * @throws IOException
	 */
	public static void writeFileToEntry(ZipOutputStream zipOutput, String entryName, String path) throws IOException{
		ZipEntry entry = new ZipEntry(entryName);
		zipOutput.putNextEntry(entry);
		FileInputStream fileInput = new FileInputStream(path);
		byte[] buf = new byte[1024];
		int len;
		while ((len = fileInput.read(buf)) > 0) {
			zipOutput.write(buf, 0, len);
		}
		fileInput.close();
		zipOutput.closeEntry();
	}
	
	/**
	 * Write a String as a new entry to a zip file
	 * @param zipOutput
	 * 			The ZipOutputStream representing the zip file
	 * @param entryName
	 * 			The name of the new entry that will be creted
	 * @param toAdd
	 * 			The String to add
	 * @throws IOException
	 */
	public static void writeStringToEntry(ZipOutputStream zipOutput, String entryName, String toAdd) throws IOException{
		ZipEntry keyEntry = new ZipEntry(entryName);
		zipOutput.putNextEntry(keyEntry);
		byte[] key_as_bytes = toAdd.getBytes();
		zipOutput.write(key_as_bytes, 0, key_as_bytes.length);
		zipOutput.closeEntry();
	}
	
	/**
	 * Write a String as a new entry to a zip file
	 * @param zipOutput
	 * 			The ZipOutputStream representing the zip file
	 * @param entryName
	 * 			The name of the new entry that will be creted
	 * @param toAdd
	 * 			The byte array to add
	 * @throws IOException
	 */
	public static void writeByteArrayToEntry(ZipOutputStream zipOutput, String entryName, byte[] toAdd) throws IOException{
		CRC32 crc = new CRC32();
		ZipEntry keyEntry = new ZipEntry(entryName);
		keyEntry.setSize(toAdd.length);
		keyEntry.setMethod(ZipEntry.STORED);
		crc.reset();
		crc.update(toAdd);
		keyEntry.setCrc(crc.getValue());
		zipOutput.putNextEntry(keyEntry);
		zipOutput.write(toAdd, 0, toAdd.length);
		zipOutput.closeEntry();
	}
}
