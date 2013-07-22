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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author ddowney
 *
 */
public class Reading{
	
	private Logger rLog = LogManager.getLogger(Reading.class.getName());
	private Path path; 
	private ArrayList<String> tbcList = new ArrayList<String>();
	private final static Charset ENCODING = StandardCharsets.UTF_8;
		
	public Reading(){
	}

	/**
	 * Get the file to read at the end of the URL/uri.
	 * @param uri
	 * @return
	 */
	public Path getFile(String uri) {
		Writing writing = new Writing();
		path = writing.getFile(uri);
		return path;
	}

	/**
	 * Read the given file and create a list of files that are to be converted by
	 * comparing the filenames in the given list to the filenames in the file.
	 * @param attachNames
	 * @param uri
	 */
	public void readFile(ArrayList<String> attachNames, String uri) {
		try{
			BufferedReader reader = Files.newBufferedReader(getFile(uri), ENCODING);
			String line = null;
			while((line = reader.readLine()) != null){
				for(String names : attachNames){
					if(!line.equals(names)){
						setTBConvertedList(names);
					}
				}
			}
		}catch(IOException e){
			rLog.error("IO Exception", e);
		}catch(SecurityException e){
			rLog.error("Security Exception", e);
		}
	}
	
	/**
	 * Add the files tbc (to be converted) to the list tbcList.
	 * @param name
	 */
	public void setTBConvertedList(String name){
		tbcList.add(name);
	}
	
	/**
	 * Get the list of files tbc (to be converted).
	 * @return tbcList The list of files tbc.
	 */
	public ArrayList<String> getTBConvertedList(){
		return tbcList;
	}
}
