/**
 * 
 */
package com.ddowney.plugins.pdf2img;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author ddowney
 *
 */
public class Writing{

	private Logger wLog = LogManager.getLogger(Writing.class.getName());
	//private final static Charset ENCODING = StandardCharsets.UTF_8;
	private Path path;
	//private String uri = "test_Converted-files.txt";
	private ClassLoader loader = Thread.currentThread().getContextClassLoader();
	
	private ArrayList<String> writeTo;
	
	/**
	 * Constructor
	 */
	public Writing(){
	}
	
	/**
	 * Create the file for tracking, if it already exists throw an exception.
	 * @param uri
	 */
	public void createFile(String uri){
		path = getFile(uri);
		try{
			if(!fileExists(uri)){
				Files.createFile(path);
				wLog.info("Created file tracker.", path);
			}
		}catch(FileAlreadyExistsException e){
			wLog.error("File already exists", e);
			wLog.catching(e);
		}catch(IOException e){
			wLog.error("IO error", e);
			wLog.catching(e);
		}
	}
	
	/**
	 * Get the file from the classpath.
	 * @param uri
	 * @return
	 */
	public Path getFile(String uri){
		URL url = loader.getResource(uri);
		try {
			path = Paths.get(url.toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			wLog.error("URI Syntax Exception");
			wLog.catching(e);
		}
		return path;
	}
	
	/**
	 * Checks to see if the file already exists.
	 * @return boolean True for it exists, False otherwise.
	 */
	public boolean fileExists(String uri){ 
		Path p = getFile(uri); 
		if(p.toFile().exists()){
			wLog.info("File exists", p);
			return true;
		}
		wLog.info("File doesn't exist", p);
		return false;
	}

	/**
	 * Write to the file, the filenames of the converted attachments.
	 * @param lines ArrayList<String> The filenames to write.
	 * @param uri String The file within the classpath to write to.
	 */
	public void writeFile(ArrayList<String> lines, String uri) {
		path = getFile(uri); 
		Charset charset = Charset.defaultCharset();
		BufferedWriter bwriter = null;
		try{
			bwriter = Files.newBufferedWriter(path, charset);
			for(String line : lines){
				bwriter.write(line, 0, line.length());
				bwriter.newLine();
				bwriter.flush();
			}
		}catch(IOException e){
			wLog.error("IOException ", e);
			wLog.catching(e);
		}catch(UnsupportedOperationException e){
			wLog.error("Unsupported Operation Exception", e);
			wLog.catching(e);
		}catch(SecurityException e){
			wLog.error("Security Exception", e);
			wLog.catching(e);
		}finally{
			if(bwriter != null){
				try {
					bwriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					wLog.error("IO Exception", e);
					wLog.catching(e);
				}	
			}
		}
	}
	
	/**
	 * Get the title of the page with the attachments on it convert it to upper case
	 * and surround with '*'. Add the new string to the ArrayList writeTo.
	 * @param pageName The title of the Page with converted Attachments.
	 */
	public void setPages(String pageName){
			String pageUpper = "*** " + pageName.toUpperCase() + " ***";
			writeTo.add(pageUpper);
	}
	
	/**
	 * Get the filenames of the origin file and the thumbnail file.
	 * Stick them together and add the new string to the ArrayList writeTo.
	 * @param origin The filename of the Attachment that was converted.
	 */
	public void setAttachments(String origin){
			writeTo.add(origin);	
	}
	
	/**
	 * Just return the ArrayList with all of the lines to be written to the file in it.
	 * @return writeTo The Page titles and Attachment filenames to be written to the file.
	 */
	public ArrayList<String> getLines(){
		return writeTo;
	}
}
