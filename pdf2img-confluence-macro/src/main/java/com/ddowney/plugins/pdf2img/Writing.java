/**
 * 
 */
package com.ddowney.plugins.pdf2img;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author ddowney
 *
 */
public class Writing{

	private Logger wLog = LoggerFactory.getLogger(Writing.class);
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private Path path;
	
	private ArrayList<String> writeTo;
	
	public Writing(){
		
	}
	
	/**
	 * Create the file 'fileTracker.txt'. If it already exists throw an exception.
	 */
	public void createFile(){
		path = Paths.get("/src/main/resources/fileTracker.txt");
		try{
			if(!fileExists()){
				Files.createFile(path);
			}
		}catch(FileAlreadyExistsException e){
			wLog.error("File already exists", e);
		}catch(IOException e){
			wLog.error("IO error", e);
		}
	}
	
	/**
	 * Checks to see if the file already exists.
	 * @return boolean True for it exists, False otherwise.
	 */
	public boolean fileExists(){
		if(path.toFile().exists()){
			return true;
		}
		return false;
	}

	/**
	 * Cycle through the ArrayList writeTo and write each String in that List
	 * to the file denoted by path.
	 */
	public void writeFile() {
		try{
			BufferedWriter writer = Files.newBufferedWriter(path, ENCODING);
			for(String line : writeTo){
				writer.write(line);
				writer.newLine();
			}
		}catch(IOException e){
			wLog.error("IOException ", e);
			wLog.trace("", e);
		}catch(UnsupportedOperationException e){
			wLog.error("Unsupported Operation Exception", e);
			wLog.trace("", e);
		}catch(SecurityException e){
			wLog.error("Security Exception", e);
			wLog.trace("", e);
		}
	}
	
	/**
	 * Get the title of the page with the attachments on it convert it to upper case
	 * and surround with '*'. Add the new string to the ArrayList writeTo.
	 * @param pageName
	 */
	public void setPages(String pageName){
		String pageUpper = "***" + pageName.toUpperCase() + "***";
		writeTo.add(pageUpper);
	}
	
	/**
	 * Get the filenames of the origin file and the thumbnail file.
	 * Stick them together and add the new string to the ArrayList writeTo.
	 * @param origin
	 * @param thumb
	 */
	public void setAttachments(String origin, String thumb){
		String representation = origin + " - " + thumb;
		writeTo.add(representation);
	}
}
