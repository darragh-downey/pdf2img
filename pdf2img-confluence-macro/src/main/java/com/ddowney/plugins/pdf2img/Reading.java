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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ddowney
 *
 */
public class Reading{
	
	private Logger rLog = LoggerFactory.getLogger(Reading.class);
	private Path path; 
	private File file;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	
	public Reading(Path path, File file){
		this.path = path;
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void readFile() {
		try{
			BufferedReader reader = Files.newBufferedReader(path, ENCODING);
			String line = null;
			while((line = reader.readLine()) != null){
				/**
				 * process the lines some way
				 * what I imagine doing is writing the to the file in this way
				 * pageName - originalFileName - thumbnailName
				 * this way I know where the file originated from.
				 * I should get back to work...
				 */
				
			}
		}catch(IOException e){
			rLog.error("IO Exception", e);
		}catch(SecurityException e){
			rLog.error("Security Exception", e);
		}
	}
	
}
