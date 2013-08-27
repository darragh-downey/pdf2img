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
		Space space = picker.getSpaceByName(spacename, picker.getAllSpaces());
		List<Page> pgs = picker.getCurrSpacePages(space);
		pages.add("All");
		for(Page p : pgs){
			pages.add(p.getTitle());
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
		Space space = picker.getSpaceByName(spacename, picker.getAllSpaces());
		List<Page> pages = picker.getCurrSpacePages(space);
		Page page = null;
		
		for(Page p : pages){
			if(p.getTitle().equalsIgnoreCase(pagename)){
				page = p;
			}
		}
		List<Attachment> att = picker.getAttachmentsOnCurrPage(page);
		attachments.add("All");
		for(Attachment a : att){
			attachments.add(a.getFileName());
		}
		return attachments;
	}
	
	public void checkSpaceDropdown(String result){
		if(result.equalsIgnoreCase("All")){
			//prepare all attachments
		}else{
			getPages(result);
		}
	}
}
