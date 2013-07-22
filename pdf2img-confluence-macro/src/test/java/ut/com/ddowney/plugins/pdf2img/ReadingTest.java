/**
 * 
 */
package ut.com.ddowney.plugins.pdf2img;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ddowney.plugins.pdf2img.Reading;

/**
 * @author ddowney
 *
 */
public class ReadingTest {

	private Reading read;
	private String uri;
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
		read = new Reading();
		uri = "test_Converted-files.txt";
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Reading#Reading(java.nio.file.Path, java.io.File)}.
	 */
	@Test
	public void testReading() {
		assertNotNull("Failed to initialise object", read);
	}

	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Reading#getFile()}.
	 */
	@Test
	public void testGetFile() {
		Path path = read.getFile(uri);
		assertTrue("File doesn't exist", path.toFile().exists());
	}

	/**
	 * Test method for {@link com.ddowney.plugins.pdf2img.Reading#readFile()}.
	 */
	@Test
	public void testReadFile() {
		ArrayList<String> attachNames = new ArrayList<String>();
		read.readFile(attachNames, uri);
	}

}
