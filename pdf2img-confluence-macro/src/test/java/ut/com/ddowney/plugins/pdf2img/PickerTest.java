/**
 * This JUnit class tests the methods in the Picker class.
 */
package ut.com.ddowney.plugins.pdf2img;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.Attachment;
import com.atlassian.user.*;
import com.ddowney.plugins.pdf2img.Picker;

/**
 * @author ddowney
 *
 */
@RunWith (MockitoJUnitRunner.class)
public class PickerTest {
	
	private static Picker picker;
	private static Space jam = new Space();	
	private static  Space ikea = new Space();
	private static  Space  man = new Space();
	
	private static Page DAFFY_DUCK = new Page();
	private static Page BUGS_BUNNY = new Page();
	private static Page BED = new Page();
	private static Page TABLE = new Page();
	private static Page JJ =  new Page();
	private static Page DD = new Page();
	
	@Mock 
	private static Attachment daffy_pdf = mock(Attachment.class);
	@Mock 
	private static Attachment daffy_doc = mock(Attachment.class);
	@Mock 
	private static Attachment bugs_doc = mock(Attachment.class);
	@Mock 
	private static Attachment steak_doc = mock(Attachment.class);
	@Mock 
	private static Attachment rashers_pdf = mock(Attachment.class);
	
	@Mock 
	private static SpaceManager spaceManager;
	@Mock 
	private static PageManager pageManager;
	@Mock 
	private static AttachmentManager attachmentManager;
	@Mock
	private static UserManager userManager;
	
	private static List<Space> spaces = new ArrayList<Space>();
	private static Map<Space, List<Page>> pages = new HashMap<Space, List<Page>>();
	private static Map<Page, List<Attachment>> attachments = new HashMap<Page, List<Attachment>>();
	
	@BeforeClass
	public static void setUp() throws EntityException{		
		
		jam.setName("jam");
		ikea.setName("ikea");
		man.setName("man");
		
		Page jamh = new Page();
		jamh.setTitle("jam home");
		jam.setHomePage(jamh);
		
		Page ikeah = new Page();
		ikeah.setTitle("ikea home");
		ikea.setHomePage(ikeah);
		
		Page manh = new Page();
		manh.setTitle("man home");
		man.setHomePage(manh);
		
		spaces.add(ikea);
		spaces.add(jam);
		spaces.add(man);
		
		DAFFY_DUCK.setSpace(jam);
		DAFFY_DUCK.setParentPage(jam.getHomePage());
				
		BUGS_BUNNY.setParentPage(jam.getHomePage());
		
		BED.setParentPage(ikea.getHomePage());
		
		TABLE.setParentPage(ikea.getHomePage());
		
		JJ.setParentPage(man.getHomePage());

		DD.setParentPage(man.getHomePage());
		
		daffy_pdf.setFileName("daffy.pdf");
		daffy_pdf.setContent(DAFFY_DUCK);
		daffy_pdf.setContentType("attachment");
			
		daffy_doc = new Attachment();
		daffy_doc.setFileName("daffy.doc");
		daffy_doc.setContentType("attachment");
		daffy_doc.setContent(DAFFY_DUCK);
		
		bugs_doc = new Attachment();
		bugs_doc.setFileName("bug.doc");
		bugs_doc.setContentType("attachment");
		bugs_doc.setContent(DAFFY_DUCK);
		
		steak_doc = new Attachment();
		steak_doc.setFileName("steak.doc");
		steak_doc.setContentType("attachment");
		steak_doc.setContent(DAFFY_DUCK);
		
		rashers_pdf = new Attachment();
		rashers_pdf.setFileName("rashers.pdf");
		rashers_pdf.setContentType("attachment");
		rashers_pdf.setContent(DAFFY_DUCK);

		/*Iterator<Space> it = spaces.iterator();
		while(it.hasNext()){
			Space sp = it.next();
			List<Page> pg = pages.get(sp);
		//	verify(pages).get(sp);
			List<Attachment> hold = new ArrayList<Attachment>();
			List<Attachment> att = new ArrayList<Attachment>();
			for(Page pge : pg){
				hold = attachmentManager.getAttachments(pge);
				verify(attachmentManager).getAttachments(pge);
				att = picker.filterAttachments(hold);
			//	verify(picker).filterAttachments(hold);
				attachments.put(pge, att);				
			//	verify(attachments).put(pge, att);
			}*/
		//}
	}

	private Map<Space, List<Page>> getPagesMap() {
		for(Space s : spaces){
			pages.put(s, getPages() );
		}
		return pages;
	}
	
	private List<Attachment> getAttachmentList(Page page){
		List<Attachment> att = page.getAttachments();
		return att;
	}

	private List<Page> getPages() {
		List<Page> pages= new ArrayList<Page>();
		pages.add(BED);
		pages.add(TABLE);
		pages.add(JJ);
		return pages;
	}
	
	@AfterClass
	public static void tearDown(){
		spaces.clear();
		pages.clear();
		attachments.clear();
	}
	/**
	 *  Test method for {@link com.ddowney.plugins.pdf2img.Picker#Picker(com.atlassian.confluence.spaces.SpaceManager, com.atlassian.confluence.pages.PageManager, com.atlassian.confluence.pages.AttachmentManager)}.
	 */
	@Test
	public void testPicker() {
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		assertNotNull("Not initialising the Picker object correctly", picker);
	}

	/**
	 *  Test method for {@link com.ddowney.plugins.pdf2img.Picker#splitName(java.lang.String)}.
	 */
	@Test
	public void testSplitName() {
    	String pdf = "abc.pdf";
    	picker = new Picker(spaceManager, pageManager, attachmentManager);
    	String[] tokens = picker.splitName(pdf);
    	assertEquals("expected pdf got " + tokens[1], "pdf", tokens[1]);
    	assertEquals("expected abc got " + tokens[0], "abc", tokens[0]);
	}

	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Picker#getAllSpaces()}.
	 */
	@Test
	public void testGetAllSpaces() {
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		when(spaceManager.getAllSpaces()).thenReturn(spaces);
		assertNotNull("Failed to get all spaces.", picker.getAllSpaces());
		verify(spaceManager).getAllSpaces();
	}
	
	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Picker#getSpaceByName(String, List)}.
	 */
	@Test
	public void testGetSpaceByName(){
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		String jame = "jam";
		String name = jam.getName();
		assertEquals("Not the same", name, jame);
		assertNotNull("Expected jam, got " + picker.getSpaceByName(jame, spaces), picker.getSpaceByName(jame, spaces));
	}

	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Picker#getAllPages(java.util.List)}.
	 */
	@Test
	public void testGetAllPages() {
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		
		when(pageManager.getPages(ikea, true)).thenReturn(getPages());
		when(pageManager.getPages(jam, true)).thenReturn(getPages());
		when(pageManager.getPages(man, true)).thenReturn(getPages());
		
		assertNotNull("Failed to get all pages", picker.getAllPages(spaces));
		verify(pageManager).getPages(ikea, true);
		verify(pageManager).getPages(jam, true);
		verify(pageManager).getPages(man, true);
	}
	
	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Picker#getCurrSpacePages(Space)}.
	 */
	@Test
	public void testGetCurrSpacePages(){
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		
		when(spaceManager.getSpace("j")).thenReturn(jam);
		Space j = spaceManager.getSpace("j");
		when(pageManager.getPages(j, true)).thenReturn(getPages());
		List<Page> pages = pageManager.getPages(j, true);
		
		assertEquals("Failed to get pages for chosen space", pages, picker.getPagesInCurrSpace(j));
		assertNotNull("Returned null", picker.getPagesInCurrSpace(j));
		
		verify(spaceManager).getSpace("j");
		verify(pageManager, times(3)).getPages(j, true);
	}

	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Picker#getAllAttachments(java.util.Map)}.
	 */
	@Test
	public void testGetAllAttachments() {
		picker = new Picker(spaceManager, pageManager, attachmentManager);
				
		assertNotNull("Either null or not equal to the filtered attachment list", picker.getAllAttachments(getPagesMap()));
	}
	
	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Picker#getAttachmentsInCurrSpace(List)}.
	 */
	@Test
	public void testGetAttachmentsInCurrSpace(){
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		
		assertNotNull("", picker.getAttachmentsInCurrSpace(getPages()));
	}

	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Picker#convert(java.util.Map, double)}.
	 * This test will work if the createImage method in the Generator class works.
	 * @throws AttachmentDataExistsException 
	 * @throws IOException 
	 
	@Test
	public void testConvert() throws IOException, AttachmentDataExistsException {
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		assertTrue("Failed to attach!", picker.convert(attachments));		
	}
	*/
	
	/**
	 * @throws FileNotFoundException 
	 * Test method for {@link com.ddowney.plugins.pdf2img.Picker#getWordData()}.
	 */
	@Test
	public void testGetWordData() throws FileNotFoundException{
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		InputStream in = null;
		OutputStream out = null;
		File file = new File("C:\\Documents and Settings\\ddowney\\git\\pdf2img_repo\\pdf2img-confluence-macro\\word.png");
		try{
			in = picker.getWordData();
			out = new FileOutputStream(file);
			int read = 0;
			byte[] bytes = new byte[1024];
			
			while((read = in.read(bytes)) != -1 ){
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
		assertNotNull("Returned null", file);
	}
	
	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Picker#getPptData()}.
	 */
	@Test
	public void testGetPptData(){
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		InputStream in = null;
		OutputStream out = null;
		File file = new File("C:\\Documents and Settings\\ddowney\\git\\pdf2img_repo\\pdf2img-confluence-macro\\ppt.png");
		try{
			in = picker.getPptData();
			out = new FileOutputStream(file);
			int read = 0;
			byte[] bytes = new byte[1024];
			
			while((read = in.read(bytes)) != -1 ){
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
		assertNotNull("Returned null", file);
	}
	
	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Picker#getXlData()}.
	 */
	@Test
	public void testGetXlData(){
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		InputStream in = null;
		OutputStream out = null;
		File file = new File("C:\\Documents and Settings\\ddowney\\git\\pdf2img_repo\\pdf2img-confluence-macro\\xl.png");
		try{
			in = picker.getXlData();
			out = new FileOutputStream(file);
			int read = 0;
			byte[] bytes = new byte[1024];
			
			while((read = in.read(bytes)) != -1 ){
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
		assertNotNull("Returned null", file);
	}
	
	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Picker#getWordImg(String)}.
	 */
	@Test
	public void testGetWordImg(){
		fail("Not yet implemented");
		picker = new Picker(spaceManager, pageManager, attachmentManager);
	}
	
	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Picker#getPptImg(String)}.
	 */
	@Test
	public void testGetPptImg(){
		fail("Not yet implemented");
		picker = new Picker(spaceManager, pageManager, attachmentManager);
	}
	
	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Picker#getXlImg(String)}.
	 */
	@Test
	public void testGetXlImg(){
		fail("Not yet implemented");
		picker = new Picker(spaceManager, pageManager, attachmentManager);
	}

}
