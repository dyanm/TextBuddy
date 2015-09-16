import static org.junit.Assert.*;
import java.io.File;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TextBuddyTest {

	private String fileName = "testFile.txt";
	
	@Test
	public void a_testLoadFile() {
		TextBuddy.clearFileContents(fileName);
		String[] args = {"testFile.txt"};
		TextBuddy.loadFile(args);
		assertTrue(new File(fileName).exists());
	}
	
	@Test
	public void b_testAddNewLine() {	
		assertEquals(TextBuddy.addNewLine("little brown fox"), "added to " + fileName + ": \"little brown fox\"\n");
		assertEquals(TextBuddy.addNewLine("jumped over the moon"), "added to " + fileName + ": \"jumped over the moon\"\n");
	}
	
	@Test
	public void c_testDeleteExistingLine() {
		assertEquals(TextBuddy.deleteExistingLine("2"), "deleted from " + fileName + ": \"jumped over the moon\"\n");
	}

	@Test
	public void d_testClearFileContents() {
		TextBuddy.clearFileContents(fileName);
		assertEquals(TextBuddy.displayFileContents(), fileName + " is empty.\n");
	}
	
	@Test
	public void e_testDisplayFileContents() {
		fail("Not yet implemented"); // TODO
	}
}
