package com.ddowney.plugins.pdf2img;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
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

	private final static Logger pickLog = Logger.getLogger(Picker.class.getName());
	private PageManager pageManager;
	private AttachmentManager attachmentManager;
	private SpaceManager spaceManager;
	private ClassLoader loader = Thread.currentThread().getContextClassLoader();
	
	/**
	 * Constructor
	 * @param spaceManager
	 * @param pageManager
	 * @param attachmentManager
	 */
	public Picker(SpaceManager spaceManager, PageManager pageManager, AttachmentManager attachmentManager){
		this.spaceManager = spaceManager;
		this.pageManager = pageManager;
		this.attachmentManager = attachmentManager;
	}
	
	/**
	 * Splits the string by the '.' and omits it.
	 * @param fileName The name of the file to split.
	 * @return tokens The resulting strings of the split.
	 */
	public String[] splitName(String fileName){ 
		String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
		return tokens;
	}
	
	/**
	 * Get all the spaces in the site.
	 * @return spaces Return a List<Space>, a list of spaces.
	 */
	public List<Space> getAllSpaces(){
		List<Space> spaces = spaceManager.getAllSpaces();
		for(Space s : spaces){
			pickLog.info(s.getName());
		}
		return spaces;
	}
	
	/**
	 * Select a space by it's name.
	 * @param name The name of the space.
	 * @param spaces The List of spaces.
	 * @return space The space with the same name as 'name'.
	 */
	public Space getSpaceByName(String name, List<Space> spaces){
		Space space = null;
		for(Space s : spaces){
			if(name.equalsIgnoreCase(s.getName()) && s.getName() != null){
				space = s;
				pickLog.info(space.getName());
			}
		}
		return space;
	}
	
	/**
	 * Get all the pages that are in all of the spaces in the List<Space> spaces.
	 * @param spaces A List<Space> containing all of the spaces in Confluence.
	 * @return pages A Map<Space, List<Page>> that contains spaces as keys and all the pages
	 * associated with that space as the value.
	 */
	public Map<Space, List<Page>> getAllPages(List<Space> spaces){
    	Map<Space, List<Page>> pages = new HashMap<Space, List<Page>>();
    	
    	for(Space s : spaces){
    		List<Page> p = pageManager.getPages(s, true);
    		pages.put(s, p);
    		pickLog.info(String.format("Added %s and list of pages %s to the Map pages.", s.getName(), p.toString()));
    	}
    	return pages;
    }
	
	/**
	 * Get all of the pages of a selected space.
	 * @param space The selected Space. 
	 * @return pages The List<Page> for that Space.
	 */
	public List<Page> getPagesInCurrSpace(Space space){
		List<Page> pages = pageManager.getPages(space, true);
		return pages;
	}
	
	/**
	 * Get a page by it's spacekey and title.
	 * @param spacekey
	 * @param title
	 * @return page
	 */
	public Page getPageByTitle(String spacekey, String title){
		Page page = pageManager.getPage(spacekey, title);
		return page;
	}
	/**
	 * Filter attachments by file extension. Get list of pdf files only.
	 * @param att
	 * @return filteredList List<Attachment> with only a single occurrence of each pdf per page.  
	 */
	public List<Attachment> filterAttachments(List<Attachment> att){
		List<Attachment> filteredList = new ArrayList<Attachment>();
		Set<String> compare = new TreeSet<String>(new StringComparator());
			
		for(Attachment a : att){
			String[] tokens = splitName(a.getFileName());
			if(!compare.add(tokens[0]) && tokens[1].equals("pdf")){
				filteredList.add(a);
			}else if(!compare.add(tokens[0]) && (tokens[1].equals("doc") || tokens[1].equals("docx"))){
				filteredList.add(a);
			}else if(!compare.add(tokens[0]) && (tokens[1].equals("ppt") || tokens[1].equals("pptx"))){
				filteredList.add(a);
			}else if(!compare.add(tokens[0]) && (tokens[1].equals("xls") || tokens[1].equals("xlsx"))){
				filteredList.add(a);
			}
		}	
		return filteredList;
	}
	
	/**
	 * Return a Map detailing a filtered list of attachments for each page.
	 * Using the page (key to the map) in the future for attaching the newly created image to the correct page.
	 * @param pages A Map<Space, List<Page>> containing all of the spaces and pages associated with them.
	 * @return attachMap A Map<Page, List<Attachment>> containing all of the pages and attachments associated with them.
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
	
	/**
	 * Get all of the Attachments in the selected Space.
	 * @param pages A List<Page> of all the Pages in the selected Space.
	 * @return attachments A Map<Page, List<Attachment>> containing all the pages, used as keys in the Map,
	 * for the selected Space and a List of Attachments associated with each Page.
	 */
	public Map<Page, List<Attachment>> getAttachmentsInCurrSpace(List<Page> pages){
		Map<Page, List<Attachment>> attachments = new HashMap<Page, List<Attachment>>();
		
		for(Page p : pages){
			List<Attachment> attach = new ArrayList<Attachment>();
			attach = attachmentManager.getAttachments(p);
			attachments.put(p, filterAttachments(attach));
		}
		return attachments;
	}	
	
	/**
	 * Get all of the attachments for the selected page.
	 * @param page 
	 * @return attachments List<Attachment>
	 */
	public List<Attachment> getAttachmentsOnCurrPage(Page page){
		List<Attachment> attachments = new ArrayList<Attachment>();
		attachments = attachmentManager.getAttachments(page);
		return attachments;
	}
	
	/**
	 * Get the image data of Word2007Logo.png and place in an InputStream.
	 * @return doc
	 * @throws FileNotFoundException
	 */
	public InputStream getWordData() throws FileNotFoundException{
		String path = "images/Word2007Logo.png";
		InputStream doc = loader.getResourceAsStream(path);
		return doc;
	}
	
	/**
	 * Get the image data of pptLogo.png and place in an InputStream.
	 * @return ppt
	 * @throws FileNotFoundException
	 */
	public InputStream getPptData() throws FileNotFoundException{
		String path = "images/pptLogo.png";
		InputStream ppt = loader.getResourceAsStream(path);
		return ppt;
	}
	
	/**
	 * Get the image data of excelLogo.png and place in an InputStream.
	 * @return xl
	 * @throws FileNotFoundException
	 */
	public InputStream getXlData() throws FileNotFoundException{
		String path = "images/excelLogo.png";
		InputStream xl = loader.getResourceAsStream(path);
		return xl;
	}
	
	/**
	 * Create an attachment using the filename of the doc file and the data 
	 * from the word logo.
	 * @param filename
	 * @return word
	 * @throws FileNotFoundException
	 * @throws AttachmentDataExistsException
	 */
	public Attachment getWordImg(String filename) throws FileNotFoundException, AttachmentDataExistsException{
		Attachment word = new Attachment();
		word.setFileName(filename + ".png");
		attachmentManager.setAttachmentData(word, getWordData());
		return word;	
	}
	
	/**
	 * Create an attachment using the filename of the ppt file and the data 
	 * from the ppt logo.
	 * @param filename
	 * @return ppt
	 * @throws FileNotFoundException
	 * @throws AttachmentDataExistsException
	 */
	public Attachment getPptImg(String filename) throws FileNotFoundException, AttachmentDataExistsException{
		Attachment ppt = new Attachment();
		ppt.setFileName(filename + ".png");
		attachmentManager.setAttachmentData(ppt, getPptData());
		return ppt;	
	}
	
	/**
	 * Create an attachment using the filename of the xls file and the data 
	 * from the xls logo.
	 * @param filename
	 * @return xl
	 * @throws FileNotFoundException
	 * @throws AttachmentDataExistsException
	 */
	public Attachment getXlImg(String filename) throws FileNotFoundException, AttachmentDataExistsException{
		Attachment xl = new Attachment();
		xl.setFileName(filename + ".png");
		attachmentManager.setAttachmentData(xl, getXlData());
		return xl;	
	}
	
	public class StringComparator implements Comparator<String>{
		/**
		 * Compare two strings.
		 * If the value returned isn't 0 then the strings aren't the same.
		 */
		public int compare(String a, String b){		
			return a.compareTo(b);
		}
	}
}