/**
 * 
 */
package com.ddowney.plugins.pdf2img;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;


/**
 * @author ddowney
 *
 */
public class Reading{
	
	private Logger rLog = Logger.getLogger(Reading.class.getName());
	private Path path; 
	private ArrayList<String> tbcList = new ArrayList<String>();
	private final static Charset ENCODING = StandardCharsets.UTF_8;
		
	/**
	 * Constructor.
	 */
	public Reading(){
	}

	/**
	 * Get the path to the file to read in the classpath.
	 * @param uri The file to read from the classpath.
	 * @return path The path to the file within the classpath. 
	 */
	public Path getFile(String uri) {
		Writing writing = new Writing();
		path = writing.getFile(uri);
		return path;
	}
	
	/**
	 * Check to see if a file is empty, not including empty Strings/lines.
	 * @param uri The file to check.
	 * @return boolean True if empty, False otherwise.
	 */
	public boolean checkEmpty(String uri){
		try {
			BufferedReader reader = Files.newBufferedReader(getFile(uri), ENCODING);
			String line = null;
			int count = 0;
			while((line = reader.readLine()) != null){
				if(!line.isEmpty()){
					count++;
				}
				if(count >= 1){
					return false;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Read the given file and create a list of files that are to be converted by
	 * comparing the filenames in the given list to the filenames in the file.
	 * @param attachNames
	 * @param uri
	 * @return {@link com.ddowney.plugins.pdf2img.Reading#getTBConvertedList()}
	 */
	public ArrayList<String> readFile(ArrayList<String> attachNames, String uri) {
		ArrayList<String> list = new ArrayList<String>();
		try{
			BufferedReader reader = Files.newBufferedReader(getFile(uri), ENCODING);
			String line = null;
			while((line = reader.readLine()) != null){
				list.add(line);
			}
			for(String a : attachNames){
				setTBConvertedList(list.contains(a) ? null : a);
			}
		}catch(IOException e){
			rLog.error("IO Exception", e);
		}catch(SecurityException e){
			rLog.error("Security Exception", e);
		}
		return getTBConvertedList();
	}
	
	/**
	 * Add the files tbc (to be converted) to the list tbcList.
	 * @param name
	 */
	private void setTBConvertedList(String name){
			tbcList.add(name);
	}
	
	/**
	 * Get the list of files tbc (to be converted).
	 * @return tbcList The list of files tbc.
	 */
	private ArrayList<String> getTBConvertedList(){
		tbcList.removeAll(Collections.singleton(null));
		return tbcList;
	}
}
