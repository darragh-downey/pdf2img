/**
 * 
 */
package com.ddowney.plugins.pdf2img;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.atlassian.confluence.pages.Attachment;
import com.atlassian.confluence.pages.AttachmentDataExistsException;
import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.spaces.SpaceManager;

/**
 * @author ddowney
 *
 */
public class Convert {

	private static Logger cLog = Logger.getLogger(Convert.class.getName()); 
	private SpaceManager spaceManager;
	private PageManager pageManager;
	private AttachmentManager attachmentManager;
	
	/**
	 * Constructor
	 * @param spaceManager
	 * @param pageManager
	 * @param attachmentManager
	 */
	public Convert(SpaceManager spaceManager, PageManager pageManager, AttachmentManager attachmentManager){
		this.spaceManager = spaceManager;
		this.pageManager = pageManager;
		this.attachmentManager = attachmentManager;
	}
	
	/**
	 * Compare the list of Attachments from Confluence to the list of filenames of
	 * Attachments that didn't appear in the file.
	 * @param attachMap The list of files, Map<Page, List<Attachment>>, obtained from Confluence.
	 * @param diff The list of files that didn't appear in the previously converted file.
	 * @return attachToCon A Map<Page, List<Attachment>> of files to be converted.
	 */
	public Map<Page, List<Attachment>> compareLists(Map<Page, List<Attachment>> attachMap, ArrayList<String> diff){
		Map<Page, List<Attachment>> attachToCon = new HashMap<Page, List<Attachment>>();
		Iterator<Page> it = attachMap.keySet().iterator();
		
		while(it.hasNext()){
			Page page = it.next();
			List<Attachment> att = attachMap.get(page);
			List<Attachment> nwatt = new ArrayList<Attachment>();
			for(Attachment a : att){
				for(String s : diff){
					if(s.compareTo(a.getFileName()) == 0){
						nwatt.add(a);
					}
				}
			}
			attachToCon.put(page, nwatt);
		}
		return attachToCon;
	}
	
	/**
	 * 'Convert' all of the Attachments in the given attachMap, ie. if the attachment
	 * is a pdf file copy & convert the front page to a png otherwise if it's a .doc/.docx/.ppt/.pptx/.xls/.xlsx
	 * create an attachment with one of the resource images for those file types.
	 * @param attachMap A Map<Page, List<Attachment>> of files to be converted.
	 * @return
	 */
	public boolean conversion(Map<Page, List<Attachment>> attachMap){
		Generator gen = new Generator(attachmentManager);
		Picker pick = new Picker(spaceManager, pageManager, attachmentManager);
		Writing wrt = new Writing();
		Iterator<Page> it = attachMap.keySet().iterator();
		
		while(it.hasNext()){
			Page page = it.next();
			List<Attachment> attachments = attachMap.get(page);
			wrt.setPages(page.getTitle());
			//loop through the attachments attached to current page
			for(Attachment a : attachments){
				Attachment attach = null;
				if(a.getFileExtension().contains("pdf")){
					InputStream in = attachmentManager.getAttachmentData(a); //get attachments data
					try {
						attach = gen.createImage(in, a.getFileName());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						cLog.error("IO Exception");
						cLog.trace("IO Exception trace", e);
					}catch (AttachmentDataExistsException e) {
						//create an image using data, assign to new attachment
						cLog.error("Attachment Data Exists Exception");
						cLog.trace("Attachment Data Exists Exception trace", e);
					}
					try {
						attachmentManager.saveAttachment(attach, null, in);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						cLog.error("IO Exception");
						cLog.trace("IO Exception trace", e);
					} //save attachment
					page.addAttachment(attach); //attach saved attachment to current page
					wrt.setAttachments(a.getFileName());
					if(attach.getContent() != page){
						cLog.error(String.format("Failed to attach %s to %s", attach, page));
						return false;
					}
				}else if(a.getFileExtension().contains("doc") || a.getFileExtension().contains("docx")){
					try {
						attach = pick.getWordImg(a.getFileName());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						cLog.error("File Not Found Exception", e);
					} catch (AttachmentDataExistsException e) {
						// TODO Auto-generated catch block
						cLog.error("Attachment Data Exists Exception", e);
					}				
					try {
						attachmentManager.saveAttachment(attach, null, pick.getWordData());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						cLog.error("File Not Found Exception", e);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						cLog.error("IO Exception", e);
					}
					page.addAttachment(attach);
					wrt.setAttachments(a.getFileName());
					if(attach.getContent() != page){
						cLog.error(String.format("Failed to attach %s to %s", a.getFileName(), page.getTitle()));
						return false;
					}
				}else if(a.getFileExtension().contains("ppt") || a.getFileExtension().contains("pptx")){
					try {
						attach = pick.getPptImg(a.getFileName());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						cLog.error("File Not Found Exception", e);
					} catch (AttachmentDataExistsException e) {
						// TODO Auto-generated catch block
						cLog.error("Attachment Data Exists Exception", e);
					}
					try {
						attachmentManager.saveAttachment(attach, null, pick.getPptData());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						cLog.error("File Not Found Exception", e);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						cLog.error("IO Exception", e);
					}
					page.addAttachment(attach);
					wrt.setAttachments(a.getFileName());
					if(attach.getContent() != page){
						cLog.error(String.format("Failed to attach %s to %s", a.getFileName(), page.getTitle()));
						return false;
					}
				}else if(a.getFileExtension().contains("xls") || a.getFileExtension().contains("xlsx")){
					try {
						attach = pick.getXlImg(a.getFileName());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						cLog.error("File Not Found Exception", e);
					} catch (AttachmentDataExistsException e) {
						// TODO Auto-generated catch block
						cLog.error("Attachment Data Exists Exception", e);
					}
					try {
						attachmentManager.saveAttachment(attach, null, pick.getXlData());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						cLog.error("File Not Found Exception", e);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						cLog.error("IO Exception", e);
					}
					page.addAttachment(attach);
					wrt.setAttachments(a.getFileName());
					if(attach.getContent() != page){
						cLog.error(String.format("Failed to attach %s to %s", a.getFileName(), page.getTitle()));
						return false;
					}
				}
			}
		}
		return true;
	}
	
}
