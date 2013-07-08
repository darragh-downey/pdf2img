/**
 * This JUnit class tests the methods in the Picker class.
 */
package ut.com.ddowney.plugins.pdf2img;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.confluence.spaces.Space;
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
	
	@Mock 
	private Space jam = new Space();
	@Mock 
	private Space man = new Space();
	@Mock 
	private Space ikea = new Space();
	@Mock 
	private Space meat = new Space();
	@Mock 
	private Space veg = new Space();
	
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
	
	@Mock 
	private List<Space> spaces = new ArrayList<Space>();
	@Mock 
	private Map<Space, List<Page>> pages = new HashMap<Space, List<Page>>();
	
	@Before
	public void setup(){		
		spaceManager.createSpace(ikea);
		spaceManager.createSpace(jam);
		spaceManager.createSpace(man);
		spaceManager.createSpace(meat);
		spaceManager.createSpace(veg);
		
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
	}
	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#Picker(com.atlassian.confluence.spaces.SpaceManager, com.atlassian.confluence.pages.PageManager, com.atlassian.confluence.pages.AttachmentManager)}.
	 */
	@Test
	public void testPicker() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#splitName(java.lang.String)}.
	 */
	@Test
	public void testSplitName() {
		Picker p = new Picker(spaceManager, pageManager, attachmentManager);
    	String pdf = "abc.pdf";
    	String[] tokens = p.splitName(pdf);
    	assertEquals("expected '.' before pdf!", "pdf", tokens[1]);
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#getAllSpaces()}.
	 */
	@Test
	public void testGetAllSpaces() {
		Picker p = new Picker(spaceManager, pageManager, attachmentManager);
		
		assertEquals("Ahhhh fuck!", spaces, p.getAllSpaces());
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#getAllPages(java.util.List)}.
	 */
	@Test
	public void testGetAllPages() {
		Picker p = new Picker(spaceManager, pageManager, attachmentManager);
		
		assertEquals("Ahhhhh jayzus!!", pages, p.getAllPages(spaces));
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#getAllAttachments(java.util.Map)}.
	 */
	@Test
	public void testGetAllAttachments() {
		fail("Not yet implemented");
		//Picker p = new Picker(spaceManager, pageManager, attachmentManager);
		
		//assertEquals("Ur takin da piss!!", att, p.getAllAttachments(pages));
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Picker#convert(java.util.Map, double)}.
	 */
	@Test
	public void testConvert() {
		fail("Not yet implemented");
	}

}