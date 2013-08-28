/**
 * 
 */
package com.ddowney.plugins.pdf2img.servlet;

import java.util.ArrayList;
import java.util.List;
import com.atlassian.confluence.pages.Attachment;
import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.ddowney.plugins.pdf2img.Picker;

/**
 * @author ddowney
 *
 */
public class DAO {

	private Picker picker;
	private SpaceManager spaceManager;
	private PageManager pageManager;
	private AttachmentManager attachmentManager;
	
	public DAO(){
		
	}
	
	/**
	 * Get the list of spaces in Confluence for the dropdown list.
	 * It populates the first dropdown list.
	 * @return spaces List of spaces in Confluence.
	 */
	public ArrayList<String> getSpaces(){
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		ArrayList<String> spaces = new ArrayList<String>();
		List<Space> sp = picker.getAllSpaces();
		spaces.add("All");
		for(Space s : sp){
			spaces.add(s.getName());
		}
		return spaces;
	}
	
	/**
	 * Get the list of pages of the chosen Space for the dropdown list.
	 * @param spacename The name of the chosen space.
	 * @return pages The list of page titles in the chosen space.
	 */
	public ArrayList<String> getPages(String spacename){
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		ArrayList<String> pages = new ArrayList<String>();
		if(spacename.equalsIgnoreCase("All")){
			//get all pages across all spaces...
			//call a get all attachments method here
			pages.add("All");
		}else{
			//get an individual spaces list of pages
			Space space = picker.getSpaceByName(spacename, picker.getAllSpaces());
			List<Page> pgs = picker.getPagesInCurrSpace(space);
			pages.add("All");
			for(Page p : pgs){
				pages.add(p.getTitle());
			}
		}
		return pages;
	}
	
	/**
	 * Get the list of attachments of the chosen page, within the chosen space for the dropdown list.
	 * @param pagename The title of the chosen page.
	 * @param spacename The name of the chosen space.
	 * @return attachments The list of attachments on the chosen page.
	 */
	public ArrayList<String> getAttachments(String pagename, String spacename){
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		ArrayList<String> attachments = new ArrayList<String>();
		if(spacename.equalsIgnoreCase("All") && pagename.equalsIgnoreCase("All")){
			//convert all attachments
			attachments.add("All");
		}else if(!(spacename.equalsIgnoreCase("All")) && pagename.equalsIgnoreCase("All")){
			//convert all attachments attached to that page
			Space space = picker.getSpaceByName(spacename, picker.getAllSpaces());
			attachments.add("All");
			for(Page p : picker.getPagesInCurrSpace(space)){
				List<Attachment> att = picker.getAttachmentsOnCurrPage(p);
				for(Attachment a : att){
					attachments.add(a.getFileName());
				}
			}
		}else if(!(spacename.equalsIgnoreCase("All") && pagename.equalsIgnoreCase("All"))){
			//convert a select few (one -> max - 1) attachments
			Space space = picker.getSpaceByName(spacename, picker.getAllSpaces());
			Page page = picker.getPageByTitle(space.getKey(), pagename);
			List<Attachment> att = picker.getAttachmentsOnCurrPage(page);
			attachments.add("All");
			for(Attachment a : att){
				attachments.add(a.getFileName());
			}
		}
		return attachments;
	}
	
}
