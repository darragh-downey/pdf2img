/**
 * 
 */
package ut.com.ddowney.plugins.pdf2img;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;






import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.atlassian.confluence.pages.AttachmentDataExistsException;
import com.atlassian.confluence.pages.AttachmentManager;
import com.ddowney.plugins.pdf2img.Generator;

/**
 * @author ddowney
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GeneratorTest {
	
	@Mock
	private AttachmentManager attachmentManager;
	
	private Generator gen;
	@Before
	public void setUp(){
		gen = new Generator(attachmentManager);
	}
	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Generator#Generator(com.atlassian.confluence.pages.AttachmentManager)}.
	 */
	@Test
	public void testGenerator() {
		assertNotNull("expected Generator object", gen);
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Generator#getAttachmentManager()}.
	 */
	@Test
	public void testGetAttachmentManager() {
		gen.setAttachmentManager(attachmentManager);
		assertEquals("attachment manager not set!", attachmentManager, gen.getAttachmentManager());
	}

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Generator#setAttachmentManager(com.atlassian.confluence.pages.AttachmentManager)}.
	 * unecessary test due to the fact that it's included in the {@link com.ddowney.plugins.tgen.Generator#getAttachmentManager()} test.	 
	@Test
	public void testSetAttachmentManager() {
		//Generator gen = new Generator(attachmentManager);
		fail("Test not written yet...");
	}
	*/

	/**
	 * Test method for {@link com.ddowney.plugins.tgen.Generator#createImage(java.io.InputStream, java.lang.String, double)}.
	 * @throws AttachmentDataExistsException 
	 * @throws IOException 
	 */
	@Test
	public void testCreateImage() throws IOException, AttachmentDataExistsException {
		FileInputStream pdf = null;
		try {
			pdf = new FileInputStream("C:\\Documents and Settings\\ddowney\\My Documents\\Newsletter_Mercury_13.1.pdf");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull("no image!!", gen.createImage(pdf, "Newsletter_Mercury_13.1.pdf"));
	}

}
