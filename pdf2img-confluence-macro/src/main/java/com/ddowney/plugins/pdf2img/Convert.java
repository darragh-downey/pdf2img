/**
 * 
 */
package com.ddowney.plugins.pdf2img;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;





import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	private static Logger cLog = LogManager.getLogger(Convert.class.getName()); 
	private SpaceManager spaceManager;
	private PageManager pageManager;
	private AttachmentManager attachmentManager;
	
	public Convert(SpaceManager spaceManager, PageManager pageManager, AttachmentManager attachmentManager){
		this.spaceManager = spaceManager;
		this.pageManager = pageManager;
		this.attachmentManager = attachmentManager;
	}
	
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
						e.printStackTrace();
					}catch (AttachmentDataExistsException e) {
						//create an image using data, assign to new attachment
						cLog.error("Attachment Data Exists Exception");
						cLog.trace("Attachment Data Exists Exception trace", e);
						e.printStackTrace();
					}
					try {
						attachmentManager.saveAttachment(attach, null, in);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						cLog.error("IO Exception");
						cLog.trace("IO Exception trace", e);
						e.printStackTrace();
					} //save attachment
					page.addAttachment(attach); //attach saved attachment to current page
					wrt.setAttachments(a.getFileName(), attach.getFileName());
					if(attach.getContent() != page){
						cLog.error("Failed to attach %s to %s", a.getFileName(), page.getTitle());
						return false;
					}
				}else if(a.getFileExtension().contains("doc") || a.getFileExtension().contains("docx")){
					try {
						attach = pick.getWordImg(a.getFileName());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (AttachmentDataExistsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
					try {
						attachmentManager.saveAttachment(attach, null, pick.getWordData());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					page.addAttachment(attach);
					wrt.setAttachments(a.getFileName(), attach.getFileName());
					
				}else if(a.getFileExtension().contains("ppt") || a.getFileExtension().contains("pptx")){
					try {
						attach = pick.getPptImg(a.getFileName());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (AttachmentDataExistsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						attachmentManager.saveAttachment(attach, null, pick.getPptData());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					page.addAttachment(attach);
					wrt.setAttachments(a.getFileName(), attach.getFileName());
					
				}else if(a.getFileExtension().contains("xls") || a.getFileExtension().contains("xlsx")){
					try {
						attach = pick.getXlImg(a.getFileName());
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (AttachmentDataExistsException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						attachmentManager.saveAttachment(attach, null, pick.getXlData());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					page.addAttachment(attach);
					wrt.setAttachments(a.getFileName(), attach.getFileName());
				}
			}
		}
		return true;
	}
}
