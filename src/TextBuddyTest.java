import static org.junit.Assert.*;
import java.io.File;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TextBuddyTest {

	private String fileName = "testFile.txt";
	
	//@Test
	public void testAllCE1() {
		testLoadFile();
		testAddNewLine();
		testDeleteExistingLine();
		testClearFileContents();
		testDisplayFileContents();
	}
	
	//@Test
	public void testLoadFile() {
		TextBuddy.clearFileContents();
		String[] args = {"testFile.txt"};
		TextBuddy.loadFile(args);
		assertTrue(new File(fileName).exists());
	}
	
	//@Test
	public void testAddNewLine() {	
		assertEquals(TextBuddy.addNewLine("little brown fox"), "added to " + fileName + ": \"little brown fox\"\n");
		assertEquals(TextBuddy.addNewLine("jumped over the moon"), "added to " + fileName + ": \"jumped over the moon\"\n");
	}
	
	//@Test
	public void testDeleteExistingLine() {
		assertEquals(TextBuddy.deleteExistingLine("2"), "deleted from " + fileName + ": \"jumped over the moon\"\n");
	}

	//@Test
	public void testClearFileContents() {
		TextBuddy.clearFileContents();
		assertEquals(TextBuddy.displayFileContents(), fileName + " is empty.\n");
	}
	
	//@Test
	public void testDisplayFileContents() {
		TextBuddy.addNewLine("little brown fox");
		assertEquals(TextBuddy.displayFileContents(), "1. little brown fox\n");
	}
	
	// CE2 Test cases
	@Test
	public void testSortFileContentsIsEmpty() {
		TextBuddy.loadFile(new String[]{fileName});
		TextBuddy.clearFileContents();
		assertEquals(TextBuddy.sortFileContents(), fileName + " is empty\n");
	}
	
	@Test
	public void testSortFileContentsNotEmpty() {
		TextBuddy.loadFile(new String[]{fileName});
		TextBuddy.clearFileContents();
		TextBuddy.addNewLine("little brown fox");
		assertEquals(TextBuddy.sortFileContents(), fileName + " has been alphabetically sorted\n");
	}
	
	@Test
	public void testSortFileContentsMultipleLinesOutput() {
		TextBuddy.loadFile(new String[]{fileName});
		TextBuddy.clearFileContents();
		TextBuddy.addNewLine("there is no spoon");
		TextBuddy.addNewLine("little brown fox");
		TextBuddy.addNewLine("jumped over the moon");		
		TextBuddy.sortFileContents();
		assertEquals(TextBuddy.displayFileContents(), "1. jumped over the moon\n"
												 	+ "2. little brown fox\n"
													+ "3. there is no spoon\n");
	}
}
