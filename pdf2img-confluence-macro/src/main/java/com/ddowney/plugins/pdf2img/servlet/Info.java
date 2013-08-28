/**
 * 
 */
package com.ddowney.plugins.pdf2img.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
/**
 * @author ddowney
 *
 */
public class Info extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7995054178717108691L;
	public void init() throws ServletException{
		
	}
	
	/**
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String spacename = request.getParameter("SpaceName");
		DAO dao = new DAO();
		dao.getPages(spacename);
		String json = "";
		Gson gson = new Gson();
		
		if(request.getParameter("SpaceName") != null){
			ArrayList<String> spaces = dao.getSpaces();
			Map<String, String> m = new HashMap<String, String>();
			for(int i = 1; i <= spaces.size(); i++){
				m.put(Integer.toString(i), spaces.get(i));
			}
			json = gson.toJson(m);
			
		}else if(request.getParameter("PageName") != null){
			ArrayList<String> pages = dao.getPages(spacename);
			Map<String, String> m = new HashMap<String, String>();
			for(int i = 1; i <= pages.size(); i++){
				m.put(Integer.toString(i), pages.get(i));
			}
			json = gson.toJson(m);
		}else if(request.getParameter("AttachmentName") != null){
			
		}
		response.setContentType("application/json");
		((ServletRequest) response).setCharacterEncoding("UTF-8"); //upgrade the sevlet.jar to 2.4 or higher to get rid of the cast
		response.getWriter().write(json);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		
	}
	
	/**
	 * Take gson.toJson() as input.
	 * @param gson 
	 * @return json
	 */
	public String formatJson(String gson){
		String json = gson;
		return json;
	}
}
