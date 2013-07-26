/**
 * 
 */
package com.ddowney.plugins.pdf2img;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.google.gson.Gson;
/**
 * @author ddowney
 *
 */
public class Info extends HttpServlet{

	private SpaceManager spaceManager;
	private PageManager pageManager;
	private AttachmentManager attachmentManager;
	private Map<String, Map<String, Long>> spaces;
	private Map<String, String> pages;
	private Map<String, Long> spaceMap;
	
	public void init() throws ServletException{
		
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String spacename = request.getParameter("SpaceName");
		setSpaceRequest();
		String json = null;
		Gson gson = new Gson();
		
		if(spacename.equals("all")){
			setAllPages();
			json = gson.toJson(getAllPages());
		}else{
			Iterator<String> space = getSpaceRequest().keySet().iterator();
			while(space.hasNext()){
				if(spacename.equals(space)){
					setPageRequest(spacename);
					json = gson.toJson(getPageRequest());
				}
			}
		}
		response.setContentType("application/json");
		response.
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}
	/**
	 * set all the spaces for the dropdown.
	 * @param spacename
	 */
	public void setSpaceRequest(){
		Picker p = new Picker(spaceManager, pageManager, attachmentManager);
		List<Space> sps = p.getAllSpaces();
		for(Space s : sps){
			spaceMap.put(s.getKey(), s.getId());
			spaces.put(s.getName(), spaceMap);
		}
	}
	
	/**
	 * Get every Spaces name.
	 * @return spaces ArrayList<String>
	 */
	public Map<String, Map<String, Long>> getSpaceRequest(){
		return spaces;
	}
	
	/**
	 * Return the identifiers for all of the Spaces, ie. their Space key and Long ID.
	 * @return spaceMap Map<String, Long> Every Spaces Space key and Long ID.
	 */
	public Map<String, Long> getSpaceMap(){
		return spaceMap;
	}
	
	/**
	 * Get all of the Pages of the chosen Space.
	 * @param pagename
	 */
	public void setPageRequest(String spacename){
		Picker p = new Picker(spaceManager, pageManager, attachmentManager);
		pages = new LinkedHashMap<String, String>();
		Map<String, Long> spaceInfo = spaces.get(spacename);
		Iterator<String> spaceKey = spaceInfo.keySet().iterator();	
		String key = spaceKey.next();
		Space space = spaceManager.getSpace(key);
		List<Page> pgs = p.getCurrSpacePages(space);
		int x = 1;
		for(Page pg : pgs){
			String num = Integer.toString(x);
			pages.put(num, pg.getTitle());
			x++;
		}
	}
	
	/**
	 * Return all of the pages for a space to be added to the dropdown list.
	 * @return pages
	 */
	public Map<String, String> getPageRequest(){
		return pages;
	}
	
	public void setAllPages(){
		Picker p = new Picker(spaceManager, pageManager, attachmentManager);
		pages = new LinkedHashMap<String, String>();
		List<Space> spacez = p.getAllSpaces();
		Map<Space, List<Page>> pgs = p.getAllPages(spacez);
		Iterator<Space> its = pgs.keySet().iterator();
		
		while(its.hasNext()){
			Space sp = its.next();
			
		}
	}
	
	public Map<String, String> getAllPages(){
		return pages;
	}
}
