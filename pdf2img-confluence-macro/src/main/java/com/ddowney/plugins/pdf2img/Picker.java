package com.ddowney.plugins.pdf2img;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.pages.Attachment;
import com.atlassian.confluence.pages.AttachmentDataExistsException;
import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;

/*
 * This class deals with selecting the attachments for conversion.
 * It takes user input, which involves selecting the space, page(s) and attachments.
 * Can select a space for attachment conversion (convert all pdfs in that space).
 * Can select a page for attachment conversion (convert all pdfs on that page).
 * Can select individual attachments for conversion (convert that pdf).
 */
public class Picker {

	private final static Logger picklog = LoggerFactory.getLogger(Picker.class);
	private final static Logger imagelog = LoggerFactory.getLogger(Picker.class);
	private PageManager pageManager;
	private AttachmentManager attachmentManager;
	private SpaceManager spaceManager;
	
	public Picker(SpaceManager spaceManager, PageManager pageManager, AttachmentManager attachmentManager){
		this.spaceManager = spaceManager;
		this.pageManager = pageManager;
		this.attachmentManager = attachmentManager;
	}
	
	public String[] splitName(String fileName){ 
		String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
		return tokens;
	}
	
	public List<Space> getAllSpaces(){
		List<Space> spaces = spaceManager.getAllSpaces();
		return spaces;
	}
	
	/*
	public List<Page> filterPages(Space space){
		List<Page> filteredPages = new ArrayList<Page>();

		for(Space s : spaces){
			//space keys are unique in Confluence, so there should only be one exact match.
			if(space.getKey() == s.getKey()){
				//get pages from space put in List<Page>. getPages(
				filteredPages = pageManager.getPages(s, true);
			}
		}
		return filteredPages;
	}
	*/
	
	public Map<Space, List<Page>> getAllPages(List<Space> spaces){
    	Map<Space, List<Page>> pages = new HashMap<Space, List<Page>>();
    	
    	for(Space s : spaces){
    		List<Page> p = pageManager.getPages(s, true);
    		pages.put(s, p);
    	}
    	return pages;
    }
	
	/*
	 * Return a filtered list of attachments, ie. should only contain pdfs.
	 */
	public List<Attachment> filterAttachments(List<Attachment> att){
		List<Attachment> filteredList = new ArrayList<Attachment>();
		Set<String> compare = new TreeSet<String>(new StringComparator());
			
		for(Attachment a : att){
			String[] tokens = splitName(a.getFileName());
			if(!compare.add(tokens[0]) && tokens[1].equals("pdf")){
				filteredList.add(a);
			}
		}	
		return filteredList;
	}
	
	/*
	 * Return a Map detailing a filtered list of attachments for each page.
	 * Using the page (key to the map) in the future for attaching the newly created image to the correct page. 
	 * 
	 * @return Map<Page, List<Attachment>> 
	 */
	public Map<Page, List<Attachment>> getAllAttachments(Map<Space, List<Page>> pages){
		Map<Page, List<Attachment>> attachMap = new HashMap<Page, List<Attachment>>();
		Iterator<Space> it = pages.keySet().iterator();
		
		while(it.hasNext()){
			Space space = it.next();
			List<Page> pgs = pages.get(space);
			List<Attachment> hold = new ArrayList<Attachment>();
			List<Attachment> attach = new ArrayList<Attachment>();
			//loop through pages in current space
			for(Page p : pgs){
				hold = attachmentManager.getAttachments(p); //get all attachments for that page
				attach = filterAttachments(hold); //return only the pdf attachments
				attachMap.put(p, attach); //add to Map<Page, List<Attachment>>
			}
		}
    	return attachMap;
    }
	
	public boolean convert(Map<Page, List<Attachment>> attachMap){
		Generator gen = new Generator(attachmentManager);
		Iterator<Page> it = attachMap.keySet().iterator();
		
		while(it.hasNext()){
			Page page = it.next();
			List<Attachment> attachments = attachMap.get(page);
			//loop through the attachments attached to current page
			for(Attachment a : attachments){
				InputStream in = attachmentManager.getAttachmentData(a); //get attachments data
				Attachment attach = null;
				try {
					attach = gen.createImage(in, a.getFileName());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					picklog.error("IO Exception");
					picklog.trace("IO Exception trace", e1);
					e1.printStackTrace();
				}catch (AttachmentDataExistsException e2) {
					//create an image using data, assign to new attachment
					picklog.error("Attachment Data Exists Exception");
					picklog.trace("Attachment Data Exists Exception trace", e2);
					e2.printStackTrace();
				}
				try {
					attachmentManager.saveAttachment(attach, null, in);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					picklog.error("IO Exception");
					picklog.trace("IO Exception trace", e);
					e.printStackTrace();
				} //save attachment
				page.addAttachment(attach); //attach saved attachment to current page
				if(attach.getContent() != page){
					picklog.error("Failed to attach %s to %s", a.getFileName(), page.getTitle());
					return false;
				}
				imagelog.info("Attached %s to %s", a.getFileName(), page.getTitle());
			}
		}
		return true;
	}
	
	public class StringComparator implements Comparator<String>{
		public int compare(String a, String b){		
			return a.compareTo(b);
		}
	}
}