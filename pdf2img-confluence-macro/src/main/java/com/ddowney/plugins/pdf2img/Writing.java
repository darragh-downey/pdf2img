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
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author ddowney
 *
 */
public class Writing{

	private Logger wLog = Logger.getLogger(Writing.class.getName());
	//private final static Charset ENCODING = StandardCharsets.UTF_8;
	private Path path;
	//private String uri = "test_Converted-files.txt";
	private ClassLoader loader;
	
	private ArrayList<String> writeTo;
	
	/**
	 * Constructor
	 */
	public Writing(){
		loader = Thread.currentThread().getContextClassLoader();
		writeTo = new ArrayList<String>();
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
				wLog.info(String.format("Created file tracker %s", path));
			}
		}catch(FileAlreadyExistsException e){
			wLog.error("File already exists", e);
		}catch(IOException e){
			wLog.error("IO error", e);
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
		}
		return path;
	}
	
	/**
	 * Checks to see if the file already exists.
	 * @param uri
	 * @return boolean True for it exists, False otherwise.
	 */
	public boolean fileExists(String uri){ 
		Path p = getFile(uri); 
		if(p.toFile().exists()){
			wLog.info(String.format("File exists %s", p.toString()));
			return true;
		}
		wLog.info(String.format("File doesn't exist %s", p.toString()));
		return false;
	}

	/**
	 * Check if we can write to the file.
	 * @param uri
	 * @return boolean True if we can write, false otherwise.
	 */
	public boolean canWrite(String uri){
		Path p = getFile(uri);
		if(p.toFile().canWrite()){
			return true;
		}
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
			bwriter = Files.newBufferedWriter(path, charset, StandardOpenOption.APPEND);
			for(String line : lines){
				bwriter.write(line, 0, line.length());
				bwriter.newLine();
			}
			bwriter.flush();
		}catch(IOException e | UnsupportedOperationException e | SecurityException e){
			wLog.error("Exception ", e);
		}catch(Exception e){
			wLog.error("Unsupported Operation Exception", e);
		}finally{
			if(bwriter != null){
				try {
					bwriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					wLog.error("IO Exception", e);
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
