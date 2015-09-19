import static org.junit.Assert.*;
import java.io.File;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TextBuddyTest {

	private String fileName = "testFile.txt";
	
	// CE1 Test cases
	@Test
	public void testAllCE1() {
		testLoadFile();
		testAddNewLine();
		testDeleteExistingLine();
		testClearFileContents();
		testDisplayFileContents();
	}

	public void testLoadFile() {
		String[] args = {"testFile.txt"};
		TextBuddy.loadFile(args);
		assertTrue(new File(fileName).exists());
	}
	
	public void testAddNewLine() {
		TextBuddy.clearFileContents();
		assertEquals("added to " + fileName + ": \"little brown fox\"\n", TextBuddy.addNewLine("little brown fox"));
		assertEquals("added to " + fileName + ": \"jumped over the moon\"\n", TextBuddy.addNewLine("jumped over the moon"));
	}
	
	public void testDeleteExistingLine() {
		assertEquals("deleted from " + fileName + ": \"jumped over the moon\"\n", TextBuddy.deleteExistingLine("2"));
	}

	public void testClearFileContents() {
		TextBuddy.clearFileContents();
		assertEquals(fileName + " is empty\n", TextBuddy.displayFileContents());
	}

	public void testDisplayFileContents() {
		TextBuddy.addNewLine("little brown fox");
		assertEquals("1. little brown fox\n", TextBuddy.displayFileContents());
	}
	
	// CE2 Test cases
	@Test
	public void testSortFileContentsIsEmpty() {
		TextBuddy.loadFile(new String[]{fileName});
		TextBuddy.clearFileContents();
		assertEquals(fileName + " is empty\n", TextBuddy.sortFileContents());
	}
	
	@Test
	public void testSortFileContentsNotEmpty() {
		TextBuddy.loadFile(new String[]{fileName});
		TextBuddy.clearFileContents();
		TextBuddy.addNewLine("little brown fox");
		assertEquals(fileName + " has been alphabetically sorted\n", TextBuddy.sortFileContents());
	}
	
	@Test
	public void testSortFileContentsMultipleLinesOutput() {
		TextBuddy.loadFile(new String[]{fileName});
		TextBuddy.clearFileContents();
		TextBuddy.addNewLine("there is no spoon");
		TextBuddy.addNewLine("little brown fox");
		TextBuddy.addNewLine("jumped over the moon");		
		TextBuddy.sortFileContents();
		assertEquals("1. jumped over the moon\n"
				   + "2. little brown fox\n"
				   + "3. there is no spoon\n", 
				   TextBuddy.displayFileContents());
	}

	@Test
	public void testSearchFileContentsExact() {
		TextBuddy.loadFile(new String[]{fileName});
		TextBuddy.clearFileContents();
		TextBuddy.addNewLine("there is no spoon");
		TextBuddy.addNewLine("little brown fox");
		TextBuddy.addNewLine("jumped over the moon");
		assertEquals("2. little brown fox\n", TextBuddy.searchFileContents("little brown fox"));
	}
	
	@Test
	public void testSearchFileContentsIgnoreCase() {
		TextBuddy.loadFile(new String[]{fileName});
		TextBuddy.clearFileContents();
		TextBuddy.addNewLine("there is no spoon");
		TextBuddy.addNewLine("little brown fox");
		TextBuddy.addNewLine("jumped over the moon");
		assertEquals("2. little brown fox\n", TextBuddy.searchFileContents("LITTLE BROWN FOX"));
	}
}
