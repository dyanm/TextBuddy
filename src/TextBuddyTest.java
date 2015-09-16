import static org.junit.Assert.*;
import java.io.File;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TextBuddyTest {

	@Test
	public void a_testLoadFile() {
		String[] fileName = {"testFile.txt"};
		TextBuddy.loadFile(fileName);
		assertTrue(new File("testFile.txt").exists());
	}
	
	@Test
	public void b_testAddNewLine() {	
		assertEquals(TextBuddy.addNewLine("little brown fox"), "added to testFile.txt: \"little brown fox\"\n");
	}
	
	@Test
	public void c_testDeleteExistingLine() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void d_testClearFileContents() {
		fail("Not yet implemented"); // TODO
	}
	
	@Test
	public void e_testDisplayFileContents() {
		fail("Not yet implemented"); // TODO
	}
}
