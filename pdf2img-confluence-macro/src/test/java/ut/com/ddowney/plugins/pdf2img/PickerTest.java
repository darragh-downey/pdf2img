/**
 * This JUnit class tests the methods in the Picker class.
 */
package ut.com.ddowney.plugins.pdf2img;

import static org.junit.Assert.*;

import java.io.IOException;
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
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.pages.AttachmentDataExistsException;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.Attachment;
import com.atlassian.user.*;
import com.atlassian.user.impl.DefaultUser;
import com.ddowney.plugins.pdf2img.Picker;

/**
 * @author ddowney
 *
 */
@RunWith (MockitoJUnitRunner.class)
public class PickerTest {
	
	private static Picker picker;
	
	//@Mock 
	//private static Page Daffy_duck = mock(Page.class);
	private static Page Daffy_duck = new Page();
	//@Mock 
	//private static Page Bugs_bunny = mock(Page.class);
	private static Page Bugs_bunny = new Page();
	//@Mock 
	//private static Page Bed = mock(Page.class);
	private static Page Bed = new Page();
	//@Mock 
	//private static Page Table = mock(Page.class);
	private static Page Table = new Page();
	//@Mock 
	//private static Page JJ = mock(Page.class);
	private static Page JJ =  new Page();
	//@Mock 
	//private static Page DD = mock(Page.class);
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
	
	//@SuppressWarnings("unchecked")
	@Mock 
	//private static List<Space> spaces = mock(List.class);
	private static List<Space> spaces = new ArrayList<Space>();
	//@SuppressWarnings("unchecked")
	@Mock
	//private static Map<Space, List<Page>> pages = mock(Map.class);
	private static Map<Space, List<Page>> pages = new HashMap<Space, List<Page>>();
	//@SuppressWarnings("unchecked")
	@Mock
	//private static Map<Page, List<Attachment>> attachments = mock(Map.class);
	private static Map<Page, List<Attachment>> attachments = new HashMap<Page, List<Attachment>>();
	
	@BeforeClass
	public static void setUp() throws EntityException{		
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		
		DefaultUser duser = mock(DefaultUser.class);
		duser.setEmail("ddowney@fexco.com");
		duser.setFullName("Darragh Downey");
		duser.setPassword("ddowney");
		//User user = duser;
			
		Space jam = new Space();
		Space ikea = new Space();
		Space man = new Space();
		
		jam.setName("jam");
		ikea.setName("ikea");
		man.setName("man");
		
		//verify(jam).setName("jam");
		//verify(ikea).setName("ikea");
		//verify(man).setName("man");
		
		Page jamh = new Page();
		jamh.setTitle("jam home");
		jam.setHomePage(jamh);
		
		Page ikeah = new Page();
		ikeah.setTitle("ikea home");
		ikea.setHomePage(ikeah);
		
		Page manh = new Page();
		manh.setTitle("man home");
		man.setHomePage(manh);
		//Space jam = spaceManager.createSpace("j", "jam", "Space Jam", user);
		//Space ikea = spaceManager.createSpace("ik", "ikea", "Ikea, for all your furniture needs", user);
		//Space man = spaceManager.createSpace("m", "man", "AWW MAN", user);
		//Space meat = spaceManager.createSpace("mt", "meat", "#MMMM MEAT", user);
		//Space veg = spaceManager.createSpace("v", "veg", "Veggies", user);
		
	
		//verify(spaceManager).createSpace("j", "jam", "Space Jam", user);
		//verify(spaceManager).createSpace("ik", "ikea", "Ikea, for all your furniture needs", user);
		//verify(spaceManager).createSpace("m", "man", "AWW MAN", user);
		//verify(spaceManager).createSpace("mt", "meat", "#MMMM MEAT", user);
		//verify(spaceManager).createSpace("v", "veg", "Veggies", user);
		
		spaces.add(ikea);
		spaces.add(jam);
		//spaces.add(meat);
		spaces.add(man);
		//spaces.add(veg);
		
		//verify(spaces).add(ikea);		
		//verify(spaces).add(jam);		
		//verify(spaces).add(man);		
		//verify(spaces, times(5)).add(meat);		
		//verify(spaces, times(5)).add(veg);
		
		Daffy_duck.setSpace(jam);
		Daffy_duck.setParentPage(jam.getHomePage());
		
		//verify(Daffy_duck).setParentPage(jam.getHomePage());
		
		Bugs_bunny.setParentPage(jam.getHomePage());
		
		//verify(Bugs_bunny).setParentPage(jam.getHomePage());
		
		Bed.setParentPage(ikea.getHomePage());
		
		//verify(Bed).setParentPage(ikea.getHomePage());
		
		Table.setParentPage(ikea.getHomePage());
		
		//verify(Table).setParentPage(ikea.getHomePage());
		
		JJ.setParentPage(man.getHomePage());
		
		//verify(JJ).setParentPage(man.getHomePage());
		
		DD.setParentPage(man.getHomePage());
		
		//verify(DD).setParentPage(man.getHomePage());
				
		for(Space s : spaces){
		//	if(pageManager.getPages(s, true) != null){
				List<Page> pgs = pageManager.getPages(s, true);
				verify(pageManager).getPages(s, true);
				pages.put(s, pgs);
			//	verify(pages).put(s, pgs);
		//	}			
		}
		
		daffy_pdf = new Attachment();
		daffy_pdf.setFileName("daffy.pdf");
		daffy_pdf.setContent(Daffy_duck);
		daffy_pdf.setContentType("attachment");
			
		daffy_doc = new Attachment();
		daffy_doc.setFileName("daffy.doc");
		daffy_doc.setContentType("attachment");
		daffy_doc.setContent(Daffy_duck);
		
		bugs_doc = new Attachment();
		bugs_doc.setFileName("bug.doc");
		bugs_doc.setContentType("attachment");
		bugs_doc.setContent(Daffy_duck);
		
		steak_doc = new Attachment();
		steak_doc.setFileName("steak.doc");
		steak_doc.setContentType("attachment");
		steak_doc.setContent(Daffy_duck);
		
		rashers_pdf = new Attachment();
		rashers_pdf.setFileName("rashers.pdf");
		rashers_pdf.setContentType("attachment");
		rashers_pdf.setContent(Daffy_duck);
		
		//verify(daffy_pdf).setContent(Daffy_duck);
		//verify(daffy_doc).setContent(Daffy_duck);
		//verify(bugs_doc).setContent(Daffy_duck);
		//verify(steak_doc).setContent(Daffy_duck);
		//verify(rashers_pdf).setContent(Daffy_duck);
		
		Iterator<Space> it = spaces.iterator();
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
			}
		}
	}
	
	@AfterClass
	public static void tearDown(){
		spaces.clear();
	//	verify(spaces).clear();
		pages.clear();
	//	verify(pages).clear();
		attachments.clear();
	//	verify(attachments).clear();
	}
	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#Picker(com.atlassian.confluence.spaces.SpaceManager, com.atlassian.confluence.pages.PageManager, com.atlassian.confluence.pages.AttachmentManager)}.
	 */
	@Test
	public void testPicker() {
		assertNotNull("Not initialising the Picker object correctly", picker);
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#splitName(java.lang.String)}.
	 */
	@Test
	public void testSplitName() {
    	String pdf = "abc.pdf";
    	String[] tokens = picker.splitName(pdf);
    	verify(picker).splitName(pdf);
    	assertEquals("expected pdf got " + tokens[1], "pdf", tokens[1]);
    	assertEquals("expected abc got " + tokens[0], "abc", tokens[0]);
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#getAllSpaces()}.
	 */
	@Test
	public void testGetAllSpaces() {	
		verify(picker.getAllSpaces());
		assertNotNull("Failed to get all spaces.", picker.getAllSpaces());
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetSpaceByName(){
		String jam = "jam";
		String name = spaceManager.getSpace("j").getName();
		verify(spaceManager).getSpace("j").getName();
		assertEquals("Not the same", name, jam);
		assertNotNull("Expected jam, got " + picker.getSpaceByName(jam, spaces), picker.getSpaceByName(jam, spaces));
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#getAllPages(java.util.List)}.
	 */
	@Test
	public void testGetAllPages() {
		//System.out.println(picker.getAllPages(spaces));
		verify(picker).getAllPages(spaces);
		assertNotNull("Failed to get all pages", picker.getAllPages(spaces));
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetCurrSpacePages(){
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#getAllAttachments(java.util.Map)}.
	 */
	@Test
	public void testGetAllAttachments() {
		verify(picker).getAllAttachments(pages);
		assertEquals("Either null or not equal to the filtered attachment list", attachments, picker.getAllAttachments(pages));
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetAttachmentsInCurrSpace(){
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#convert(java.util.Map, double)}.
	 * This test will work if the createImage method in the Generator class works.
	 * @throws AttachmentDataExistsException 
	 * @throws IOException 
	 */
	@Test
	public void testConvert() throws IOException, AttachmentDataExistsException {
		assertTrue("Failed to attach!", picker.convert(attachments));		
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetWordData(){
		fail("Not yet implemented");
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetPptData(){
		fail("Not yet implemented");
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetXlData(){
		fail("Not yet implemented");
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetWordImg(){
		fail("Not yet implemented");
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetPptImg(){
		fail("Not yet implemented");
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetXlImg(){
		fail("Not yet implemented");
	}

}
