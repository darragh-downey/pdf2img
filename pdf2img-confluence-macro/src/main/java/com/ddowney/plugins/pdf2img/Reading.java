/**
 * 
 */
package com.ddowney.plugins.pdf2img;

import java.io.BufferedReader;
import java.io.File;
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
	private File file;
	private ArrayList<String> tbcList = new ArrayList<String>();
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	
	public Reading(Path path, File file){
		this.path = path;
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void readFile(ArrayList<String> attachNames) {
		try{
			BufferedReader reader = Files.newBufferedReader(path, ENCODING);
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
	 * Get the list of files tbc.
	 * @return tbcList The list of files tbc.
	 */
	public ArrayList<String> getTBConvertedList(){
		return tbcList;
	}
}
