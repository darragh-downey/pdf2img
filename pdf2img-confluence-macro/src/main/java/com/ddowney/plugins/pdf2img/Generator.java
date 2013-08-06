package com.ddowney.plugins.pdf2img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
//import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import com.atlassian.confluence.pages.Attachment;
import com.atlassian.confluence.pages.AttachmentDataExistsException;
import com.atlassian.confluence.pages.AttachmentManager;

import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import com.mortennobel.imagescaling.*;
import org.apache.pdfbox.pdmodel.PDDocument; //load the doc
import org.apache.pdfbox.pdmodel.PDPage; //convert to image
//import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class Generator {
	
	private final static Logger genLog = LogManager.getLogger(Generator.class.getName());
	//private double dime = 1;
	//private double[] sizes = {0.25, 0.5, 0.75, 1, 1.25, 1.5, 1.75, 2}; //ranging from 25% to 200% original size.
	private AttachmentManager attachmentManager;
	private Attachment attachment;
	
	/**
	 * Constructor
	 * @param attachmentManager
	 */
	public Generator(AttachmentManager attachmentManager){
		this.setAttachmentManager(attachmentManager);
	}
	
	/**
	 * Get the AttachmentManager.
	 * @return attachmentManager
	 */
	public AttachmentManager getAttachmentManager() {
		return attachmentManager;
	}

	/**
	 * Set the AttachmentManager. 
	 * @param attachmentManager
	 */
	public void setAttachmentManager(AttachmentManager attachmentManager) {
		this.attachmentManager = attachmentManager;
	}

	/*
	public File[] scanDir(String dirName){
		File dir = new File(dirName);

		return dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename){
				return filename.endsWith(".pdf");
			}
		});
	}
	*/
	
	/**
	 * Split the given filename into two parts, the name and the file extension.
	 * @param fileName The name of the file to split
	 * @return tokens A String[] that contains the filename and the file extension, for example
	 * 				  
	 * tokens[0] = "filename";
	 * tokens[1] = "pdf"; //the '.' is removed from the filename. 
	 */
	private static String[] splitName(String fileName){ 
		String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
		return tokens;
	}
	
	/**
	 * Set Attachment data manually, ie. create a new Attachment.  
	 * @param attachment 
	 * @param attachmentData
	 * @throws AttachmentDataExistsException
	 */
	private void setAttachment(Attachment attachment, InputStream attachmentData) throws AttachmentDataExistsException{
		attachmentManager.setAttachmentData(attachment, attachmentData);
	}
	
	/**
	 * Get the Attachment.
	 * @return attachment
	 */
	public Attachment getAttachment(){
		return attachment;
	}
	/*
	private BufferedImage resampleOperation(BufferedImage buffIn, int width, int height){
		ResampleOp resampleOp = new ResampleOp(width, height);
		resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal);
		resampleOp.addProgressListener(new ProgressListener() {
			public void notifyProgress(float fraction){
				System.out.printf("Still working - %.2f percent %n", fraction*100);
			}
		});
		BufferedImage buffOut = resampleOp.filter(buffIn, null);
		return buffOut;
	}
	*/
	
	/**
	 * This method converts a PDDocument page to a png/jpg. 
	 * @param in The pdf document.
	 * @param name The name of the pdf document.
	 * @return attachment Return the newly created image as an Attachment.
	 * @throws IOException
	 * @throws AttachmentDataExistsException
	 */
	public Attachment createImage(InputStream in, String name) throws IOException, AttachmentDataExistsException 
    {
            PDDocument document = null; 
            Attachment attachment = null;
            File img = null;
            InputStream input = null;
            
            try{
                document = PDDocument.load(in);
            }catch (IOException ex){
                   System.out.println("" + ex);
            }

            @SuppressWarnings("unchecked")
    		List<PDPage> pages =  document.getDocumentCatalog().getAllPages();
            Iterator<PDPage> iter =  pages.iterator(); 
            String[] tokens = splitName(name);
            int i = 1;
            while (iter.hasNext() && i < 2){
               	try{
               		PDPage page = (PDPage) iter.next();
               		//PDRectangle mediabox = page.getMediaBox();
               		BufferedImage image = page.convertToImage();
                	/*	
               		if(dimensions != 0 && dimensions <= 2 && dimensions > 0){
               			double width = (int)mediabox.getWidth()* dime;
        	            double height = (int)mediabox.getHeight() * dime;
                    		
        	            BufferedImage rescaledImage = resampleOperation(image, (int)width, (int)height);
        	            img = new File(tokens[0]+".png");
        	            ImageIO.write(rescaledImage, "png", img);
        	                
               		}else if(dimensions == 0){
               			double width = (int)mediabox.getWidth()* 1;
        	            double height = (int)mediabox.getHeight() * 1;
                    		  
        	            BufferedImage rescaledImage = resampleOperation(image, (int)width, (int)height);
        	            img = new File(tokens[0]+".png");
        	            ImageIO.write(rescaledImage, "png", img);
               		} 
               		*/               
               		img = new File(tokens[0]+".png");
    	            ImageIO.write(image, "png", img);
               		input = new FileInputStream(img);
               		setAttachment(attachment, input);
    	            i++;
               	}catch(IOException e){
               		genLog.error("Something happened!", e);
               		e.printStackTrace();
               	}finally{
               		document.close();
               		input.close();
               		genLog.info("Closed streams");
               	}    
            }
            return attachment;
        }
	
	public Attachment createImageMagick(InputStream in, String name){
		ImageInfo info = null;
		MagickImage image = null;
		
		try{
			info = new ImageInfo(InputStreamToFile(in, name).getAbsolutePath());
			image = new MagickImage(info);
			
			image.writeImage(info);
		} catch (MagickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return attachment; 
	}
	
	public File InputStreamToFile(InputStream in, String name){
		OutputStream out = null;
		File file = new File(name);
		try{
			out = new FileOutputStream(file);
			int read = 0;
			byte[] bytes = new byte[1024];
			while((read = in.read(bytes)) != -1){
				out.write(bytes, 0, read);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			if(out != null){
				try{
					out.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return file;
	}
	
}