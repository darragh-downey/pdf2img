/**
 * 
 */
package ut.com.ddowney.plugins.pdf2img;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.atlassian.confluence.pages.Attachment;
import com.atlassian.confluence.pages.Page;
import com.ddowney.plugins.pdf2img.Writing;

/**
 * @author ddowney
 *
 */
public class WritingTest {

	private Writing writing;
	private String uri = "monitoring/test_Convertedfiles.txt";
	//private String uri_two = "C:\\Documents and Settings\\ddowney\\git\\pdf2img_repo\\pdf2img-confluence-macro\\src\\main\\resources\\test_Converted-files.txt";
	private File file;
	private Path path;
	private ClassLoader loader;
	private URL url;
	//private Path path = Paths.get(uri);
	
	private static Page APG = new Page();
	private static Page BPG = new Page();
	private static Page CPG = new Page();
	
	private static Attachment AAT = new Attachment();
	private static Attachment BAT = new Attachment();
	private static Attachment CAT = new Attachment();
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
				
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		writing = new Writing();
		loader = Thread.currentThread().getContextClassLoader();
		url = loader.getResource(uri);
		path = Paths.get(url.toURI());
		file = path.toFile();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}

	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Writing#Writing()}.
	 */
	@Test
	public void testWriting() {
		assertNotNull("Need to declare a new object in each individual test", writing);
	}

	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Writing#createFile()}.
	 * @param writing 
	 * @throws URISyntaxException 
	 */
	@Test
	public void testCreateFile() throws URISyntaxException {
		writing.createFile(uri);
		Path p = Paths.get(url.toURI());
		assertEquals("File doesn't exist...", path.toFile(), p.toFile());
		assertNotNull("The file was not created...", p.toFile());
	}

	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Writing#fileExists()}.
	 */
	@Test
	public void testFileExists() {
		assertTrue("File doesn't exist...", writing.fileExists(uri));
	}
	
	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Writing#canWrite(String)}.
	 */
	@Test
	public void testCanWrite(){
		assertTrue("Don't have permission to write to the file", writing.canWrite(uri));
	}

	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Writing#writeFile()}.
	 */
	@Test
	public void testWriteFile() {
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("*** PAGENAME ***");
		lines.add("fish.pdf");
		lines.add("tree.pdf");
		writing.writeFile(lines, uri);
		System.out.println(file.lastModified());
		assertNotNull("File doesn't exist", file);
	}

	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Writing#setPages(java.lang.String)}.
	 */
	@Test
	public void testSetPages() {
		APG.setTitle("The Great Escape");
		BPG.setTitle("Home Alone");
		CPG.setTitle("Where Eagles Dare");
		writing.setPages(APG.getTitle());
		writing.setPages(BPG.getTitle());
		writing.setPages(CPG.getTitle());
		assertNotNull("Didn't add any of the page titles to the list", writing.getLines());
	}

	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Writing#setAttachments(java.lang.String, java.lang.String)}.
	 * It also tests {@link com.ddowney.plugins.pdf2img.Writing#getLines()}.
	 */
	@Test
	public void testSetAttachments() {
		AAT.setFileName("Pinochio.pdf");
		BAT.setFileName("Moby_Dick.pdf");
		CAT.setFileName("The_Good,The_Bad_And_The_Ugly.pdf");

		writing.setAttachments(AAT.getFileName());
		writing.setAttachments(BAT.getFileName());
		writing.setAttachments(CAT.getFileName());
		assertNotNull("Didn't add any of the filenames to the list", writing.getLines());
	}
	
}
