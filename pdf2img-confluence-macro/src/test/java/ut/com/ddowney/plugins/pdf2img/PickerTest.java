/**
 * This JUnit class tests the methods in the Picker class.
 */
package ut.com.ddowney.plugins.pdf2img;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
	
	private Map<Page, List<Attachment>> getAttachmentMap(){
		Iterator<Space> it = pages.keySet().iterator();
		while(it.hasNext()){
			Space space = it.next();
			List<Page> page = pages.get(space);
			for(Page p : page){
				List<Attachment> att = p.getAttachments();
				attachments.put(p, att);
			}
		}
		return attachments;
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
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#Picker(com.atlassian.confluence.spaces.SpaceManager, com.atlassian.confluence.pages.PageManager, com.atlassian.confluence.pages.AttachmentManager)}.
	 */
	@Test
	public void testPicker() {
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		assertNotNull("Not initialising the Picker object correctly", picker);
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#splitName(java.lang.String)}.
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
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#getAllSpaces()}.
	 */
	@Test
	public void testGetAllSpaces() {
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		when(spaceManager.getAllSpaces()).thenReturn(spaces);
		assertNotNull("Failed to get all spaces.", picker.getAllSpaces());
		verify(spaceManager).getAllSpaces();
	}
	
	/**
	 * 
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
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#getAllPages(java.util.List)}.
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
	 * 
	 */
	@Test
	public void testGetCurrSpacePages(){
		fail("Not yet implemented");
		picker = new Picker(spaceManager, pageManager, attachmentManager);
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#getAllAttachments(java.util.Map)}.
	 */
	@Test
	public void testGetAllAttachments() {
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		Page mock = mock(Page.class, Mockito.RETURNS_SMART_NULLS);
		when(attachmentManager.getAttachments(mock)).thenReturn(new ArrayList<Attachment>());
		
		assertNotNull("Either null or not equal to the filtered attachment list", picker.getAllAttachments(getPagesMap()));
		verify(attachmentManager,times(9)).getAttachments(mock);
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetAttachmentsInCurrSpace(){
		fail("Not yet implemented");
		picker = new Picker(spaceManager, pageManager, attachmentManager);
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#convert(java.util.Map, double)}.
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
	 * 
	 */
	@Test
	public void testGetWordData(){
		fail("Not yet implemented");
		picker = new Picker(spaceManager, pageManager, attachmentManager);
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetPptData(){
		fail("Not yet implemented");
		picker = new Picker(spaceManager, pageManager, attachmentManager);
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetXlData(){
		fail("Not yet implemented");
		picker = new Picker(spaceManager, pageManager, attachmentManager);
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetWordImg(){
		fail("Not yet implemented");
		picker = new Picker(spaceManager, pageManager, attachmentManager);
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetPptImg(){
		fail("Not yet implemented");
		picker = new Picker(spaceManager, pageManager, attachmentManager);
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetXlImg(){
		fail("Not yet implemented");
		picker = new Picker(spaceManager, pageManager, attachmentManager);
	}

}
