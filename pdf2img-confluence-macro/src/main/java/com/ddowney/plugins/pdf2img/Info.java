/**
 * 
 */
package com.ddowney.plugins.pdf2img;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
/**
 * @author ddowney
 *
 */
public class Info extends HttpServlet{

	private SpaceManager spaceManager;
	private PageManager pageManager;
	private AttachmentManager attachmentManager;
	private ArrayList<String> spaces;
	private ArrayList<String> pages;
	private Map<String, Long> spaceMap;
	
	public void init() throws ServletException{
		
	}
	
	public void doGet(HttpServletRequest request, HttpServletRequest response) throws ServletException, IOException{
		
	}
	/**
	 * set all the spaces for the dropdown.
	 * @param spacename
	 */
	public void setSpaceRequest(){
		Picker p = new Picker(spaceManager, pageManager, attachmentManager);
		List<Space> sps = p.getAllSpaces();
		for(Space s : sps){
			spaces.add(s.getName());
		}
	}
	
	/**
	 * Get o
	 * @return
	 */
	public ArrayList<String> getSpaceRequest(){
		return spaces;
	}
	
	/**
	 * 
	 * @param pagename
	 */
	public void setPageRequest(String spacename){
		Picker p = new Picker(spaceManager, pageManager, attachmentManager);
		Iterator<String> it = spaceMap.keySet().iterator();
		String key = it.next();
		Space space = spaceManager.getSpace(key);
		List<Page> pgs = p.getCurrSpacePages(space);
		for(Page pg : pgs){
			pages.add(pg.getTitle());
		}
		
	}
	
	public ArrayList<String> getPageRequest(){
		return pages;
	}
}
