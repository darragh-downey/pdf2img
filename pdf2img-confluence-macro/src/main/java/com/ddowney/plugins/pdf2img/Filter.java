/**
 * 
 */
package com.ddowney.plugins.pdf2img;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.atlassian.confluence.pages.Attachment;
import com.atlassian.confluence.pages.Page;

/**
 * @author ddowney
 *
 */
public class Filter {

	private static Logger fLog = LogManager.getLogger(Filter.class.getName()); 
	private Map<Page, List<Attachment>> pages;
	private ArrayList<String> filenames;
	
	public Filter(Map<Page, List<Attachment>> pages, ArrayList<String> filenames){
		this.filenames = filenames;
		this.pages = pages;
	}
	
	/**
	 * Get all of the filenames of all the attachments in the Map.
	 * @param pages Map<Page, List<Attachment>>
	 * @return pickerList ArrayList<String>
	 */
	public ArrayList<String> extract(Map<Page, List<Attachment>> pages){
		ArrayList<String> pickerList = new ArrayList<String>();
		Iterator<Page> it = pages.keySet().iterator();
		while(it.hasNext()){
			Page page = it.next();
			List<Attachment> att = pages.get(page);
			for(Attachment a : att){
				String name = a.getFileName();
				pickerList.add(name);
			}
		}
		return pickerList;
	}
	
	/**
	 * Compare the list from the file and the list of attachments just compiled and return
	 * a list of "unique" filenames.
	 * @param pickerList ArrayList<String> 
	 * @param readingList ArrayList<String>
	 * @return tocon ArrayList<String>
	 */
	public ArrayList<String> compare(ArrayList<String> pickerList, ArrayList<String> readingList){
		ArrayList<String> tocon = new ArrayList<String>();
		for(String s : readingList){
			for(String ss : pickerList){
				if(!s.equals(ss)){
					tocon.add(ss);
				}
			}
		}
		return tocon;
	}
	
}
