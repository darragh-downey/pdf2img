/**
 * 
 */
package com.ddowney.plugins.pdf2img;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author ddowney
 *
 */
public class Writing{

	private Logger wLog = Logger.getLogger(Writing.class.getName());
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private Path path;
	//private String uri = "test_Converted-files.txt";
	private ClassLoader loader;
	private Map<String, ArrayList<String>> map;
	
	/**
	 * Constructor
	 */
	public Writing(){
		loader = Thread.currentThread().getContextClassLoader();
		map = new HashMap<String, ArrayList<String>>();
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
			wLog.error(String.format("%s", e.toString()), e);
		}catch(IOException e){
			wLog.error(String.format("%s", e.toString()), e);
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
			e.printStackTrace();
			wLog.error(String.format("%s", e.toString()), e);
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
	public void writeFileBuff(String uri) {
		path = getFile(uri);
		map = getMap();
		Charset charset = Charset.defaultCharset();
		BufferedWriter bwriter = null;
		Iterator<String> it = map.keySet().iterator();
		try{
			bwriter = Files.newBufferedWriter(path, charset, StandardOpenOption.APPEND);
			while(it.hasNext()){
				String pageName = it.next();
				ArrayList<String> lines = map.get(pageName);
				bwriter.write(pageName);
				bwriter.newLine();
				bwriter.newLine();
				for(String line : lines){
					bwriter.write(line, 0, line.length());
					bwriter.newLine();
				}
				bwriter.newLine();
				bwriter.flush();
			}
		}catch(IOException | UnsupportedOperationException | SecurityException e){
			wLog.error(String.format("%s", e.toString()), e);
		}catch(Exception e){
			wLog.error(String.format("%s", e.toString()), e);
		}finally{
			if(bwriter != null){
				try {
					bwriter.close();
				} catch (IOException e) {
					wLog.error(String.format("%s", e.toString()), e);
				}	
			}
		}
	}
	
	/**
	 * Write to the file the filenames of the converted attachments.
	 * @param lines ArrayList<String> The filenames to write.
	 * @param uri String The file within the classpath to write to.
	 */
	public void writeFile(ArrayList<String> lines, String uri){
		path = getFile(uri);
		try {
			Files.write(path, lines, ENCODING);
		} catch (IOException e) {
			wLog.error(String.format("%s", e.toString()), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Add the page and file names to the Map for writing to the file.
	 * @param pageName
	 * @param fileNames
	 */
	public void setMap(String pageName, ArrayList<String> fileNames){
		map.put(pageName, fileNames);
	}
	
	/**
	 * Get the Map containing the page/file names. 
	 * @return map
	 */
	private Map<String, ArrayList<String>> getMap(){
		return map; 
	}
}
