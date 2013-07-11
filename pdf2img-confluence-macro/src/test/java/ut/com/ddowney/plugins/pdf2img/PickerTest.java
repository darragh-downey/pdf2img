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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.pages.AttachmentDataExistsException;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.pages.AttachmentManager;
import com.atlassian.confluence.pages.Attachment;
import com.ddowney.plugins.pdf2img.Picker;

/**
 * @author ddowney
 *
 */
@RunWith (MockitoJUnitRunner.class)
public class PickerTest {
	
	private Picker picker;
	@Mock 
	private Space jam;
	@Mock 
	private Space man;
	@Mock 
	private Space ikea;
	@Mock 
	private Space meat;
	@Mock 
	private Space veg;
	
	@Mock 
	private Page Daffy_duck = new Page();
	@Mock 
	private Page Bugs_bunny = new Page();
	@Mock 
	private Page Steak = new Page();
	@Mock 
	private Page Rashers = new Page();
	@Mock 
	private Page Carrot = new Page();
	@Mock 
	private Page Broccoli = new Page();
	@Mock 
	private Page Bed = new Page();
	@Mock 
	private Page Table = new Page();
	@Mock 
	private Page JJ = new Page();
	@Mock 
	private Page DD = new Page();
	
	@Mock 
	private Attachment daffy_pdf = new Attachment();
	@Mock 
	private Attachment daffy_doc = new Attachment();
	@Mock 
	private Attachment bugs_doc = new Attachment();
	@Mock 
	private Attachment steak_doc = new Attachment();
	@Mock 
	private Attachment rashers_pdf = new Attachment();
	
	@Mock 
	private SpaceManager spaceManager;
	@Mock 
	private PageManager pageManager;
	@Mock 
	private AttachmentManager attachmentManager;
	
	 
	private List<Space> spaces = new ArrayList<Space>();
	 
	private Map<Space, List<Page>> pages = new HashMap<Space, List<Page>>();
	
	private Map<Page, List<Attachment>> attachments = new HashMap<Page, List<Attachment>>();
	
	@Before
	public void setUp(){		
		picker = new Picker(spaceManager, pageManager, attachmentManager);
		
		jam = new Space();
		man = new Space();
		ikea = new Space();
		meat = new Space();
		veg = new Space();
		
		spaces.add(ikea);
		spaces.add(jam);
		spaces.add(man);
		spaces.add(meat);
		spaces.add(veg);
		
		Daffy_duck.setSpace(jam);
		Bugs_bunny.setSpace(jam);
		Steak.setSpace(meat);
		Rashers.setSpace(meat);
		Carrot.setSpace(veg);
		Broccoli.setSpace(veg);
		Bed.setSpace(ikea);
		Table.setSpace(ikea);
		JJ.setSpace(man);
		DD.setSpace(man);
		
		
		for(Space s : spaces){
			List<Page> pgs = pageManager.getPages(s, true);
			pages.put(s, pgs);
		}
		
		daffy_pdf = new Attachment("daffy.pdf", "attachment", 0, null);
		daffy_doc = new Attachment("daffy.doc", "attachment", 0, null);
		bugs_doc = new Attachment("bug.doc", "attachment", 0, null);
		steak_doc = new Attachment("steak.doc", "attachment", 0, null);
		rashers_pdf = new Attachment("rashers.pdf", "attachment", 0, null);
		
		daffy_pdf.setContent(Daffy_duck);
		daffy_doc.setContent(Daffy_duck);
		bugs_doc.setContent(Daffy_duck);
		steak_doc.setContent(Daffy_duck);
		rashers_pdf.setContent(Daffy_duck);
		
		Iterator<Space> it = spaces.iterator();
		while(it.hasNext()){
			Space sp = it.next();
			List<Page> pg = pages.get(sp);
			List<Attachment> hold = new ArrayList<Attachment>();
			List<Attachment> att = new ArrayList<Attachment>();
			for(Page pge : pg){
				hold = attachmentManager.getAttachments(pge);
				att = picker.filterAttachments(hold);
				attachments.put(pge, att);				
			}
		}
	}
	
	@After
	public void tearDown(){
		spaces.clear();
		pages.clear();
		attachments.clear();
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
    	assertEquals("expected '.' before pdf!", "pdf", tokens[1]);
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#getAllSpaces()}.
	 */
	@Test
	public void testGetAllSpaces() {		
		assertNotNull("Failed to get all spaces.", picker.getAllSpaces());
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#getAllPages(java.util.List)}.
	 */
	@Test
	public void testGetAllPages() {
		assertNotNull("Failed to get all pages", picker.getAllPages(spaces));
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#getAllAttachments(java.util.Map)}.
	 */
	@Test
	public void testGetAllAttachments() {
		assertEquals("Either null or not equal to the filtered attachment list", attachments, picker.getAllAttachments(pages));
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

}
