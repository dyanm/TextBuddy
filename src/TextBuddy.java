import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Dalton
 *
 */
public class TextBuddy {
	
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format: %1$s\n";
	private static final String MESSAGE_INVALID_PARAMETER = "invalid command parameter: %1$s\n";
	
	private static final String MESSAGE_FILE_NOT_SPECIFIED = "No file has been specified.";
	private static final String MESSAGE_FILE_NOT_FOUND = "%1$s does not exist. %1$s has been created.\n";
	private static final String MESSAGE_FILE_FOUND = "%1$s has been loaded successfully.\n";
	private static final String MESSAGE_FILE_EMPTY = "%1$s is empty\n";
	private static final String MESSAGE_FILE_ENTER = "Please enter a file name (including file extension if any): ";
	
	private static final String MESSAGE_ADD = "added to %1$s: \"%2$s\"\n";
	private static final String MESSAGE_DELETE_SUCCESS = "deleted from %1$s: \"%2$s\"\n";
	private static final String MESSAGE_DELETE_FAIL = "unable to delete line.\n";
	private static final String MESSAGE_DELETE_LINE_NOT_FOUND = "line number %1$d does not exist in %2$s\n";
	private static final String MESSAGE_CLEAR_SUCCESS = "all content deleted from %1$s\n";
	private static final String MESSAGE_CLEAR_FAIL = "unable to clear file\n";
	private static final String MESSAGE_SORT_SUCCESS = "%1$s has been alphabetically sorted\n";
	private static final String MESSAGE_SORT_FAIL = "unable to sort %1$s\n";
	private static final String MESSAGE_SEARCH_FAIL = "unable to find any terms with \"%1$s\" in %2$s\n";
	private static final String MESSAGE_EXIT = "Thank you for using TextBuddy.";
	
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is now ready for use.";
	private static final String MESSAGE_COMMAND_ENTER = "Enter your command: ";
	private static final String MESSAGE_COMMAND_LIST = "\nAvailable commands:\n"
														+ "add (text) - add text to file\n"
														+ "delete (line number) - delete line from file\n"
														+ "display - display all text from file\n"
														+ "clear - clear all content from file\n"
														+ "exit - exit TextBuddy\n";
	
	private static Scanner sc = new Scanner(System.in);
	private static String fileName = "";
	
	public static void main(String[] args) {
		loadFile(args);
		displayMessage(formatMessage(MESSAGE_WELCOME, new Object[]{fileName}), true);
		displayMessage(MESSAGE_COMMAND_LIST, true);
		
		while (true) {
			displayMessage(MESSAGE_COMMAND_ENTER, false);
			displayMessage(executeCommand(sc.nextLine()), true);
		}
	}
	
	//=========================================================================================================================
    // File Loading & Creation Functions
    //=========================================================================================================================
	
	// Load or create file based on passed argument or user input to allow flexibility.
	protected static void loadFile(String[] args) {
		if (args.length != 0) {
			fileName = args[0];
			createFile(new File(args[0]));
		}
		else {
			displayMessage(MESSAGE_FILE_NOT_SPECIFIED, true);
			do {
				displayMessage(MESSAGE_FILE_ENTER, false);
				fileName = sc.nextLine();
			} while (fileName.isEmpty());
		}
	}
	
	private static void createFile(File file) {
		try {
			if (file.createNewFile())
				displayMessage(formatMessage(MESSAGE_FILE_NOT_FOUND, new Object[]{fileName}), true);
			else
				displayMessage(formatMessage(MESSAGE_FILE_FOUND, new Object[]{fileName}), true);
		} 
		catch (IOException e) {
			throw new Error(formatMessage(MESSAGE_FILE_NOT_FOUND, new Object[]{fileName}));
		}
	}
	
	//=========================================================================================================================
    // Command Functions
    //=========================================================================================================================
	
	private static String executeCommand(String userInput) {
		String commandType = getFirstWord(userInput);
		String commandParameters = removeFirstWord(userInput);
		
		if (commandType.trim().isEmpty())
			return MESSAGE_COMMAND_LIST;
		else
			return determineCommand(userInput, commandType, commandParameters);
	}

	private static String determineCommand(String userInput, String commandType, String commandParameters) {
		switch (commandType) {
		case "add":
			return addNewLine(commandParameters);
		case "delete":
			return deleteExistingLine(commandParameters);
		case "display":
			return displayFileContents();
		case "clear":
			return clearFileContents();
		case "sort":
			return sortFileContents();
		case "search":
			return searchFileContents(commandParameters);
		case "exit":
			displayMessage(MESSAGE_EXIT, true);
			System.exit(0);
		default:
			return formatMessage(MESSAGE_INVALID_FORMAT, new Object[]{userInput});
		}
	}
	
	protected static String addNewLine(String commandParameters) {
		if (commandParameters.isEmpty())
			return formatMessage(MESSAGE_INVALID_PARAMETER, new Object[]{commandParameters});
		
		ArrayList<String> listOfContents =  addFileContentsToList();
		
		listOfContents.add(commandParameters);
		
		return writeListContentsToFile(listOfContents, MESSAGE_ADD, new Object[]{fileName, commandParameters}, 
									   MESSAGE_INVALID_PARAMETER, new Object[]{commandParameters});
	}
	
	protected static String deleteExistingLine(String commandParameters) {
		int deletedLineNumber;
		String deletedLine = "";
		// Checks if the command parameter is a valid integer before proceeding to prevent mis-entry by the user.
		try {
			deletedLineNumber = Integer.parseInt(commandParameters);
		}
		catch (NumberFormatException e) {
			return formatMessage(MESSAGE_INVALID_PARAMETER, new Object[]{commandParameters});
		}
		
		ArrayList<String> listOfContents = addFileContentsToList();
		
		if (deletedLineNumber > listOfContents.size() || deletedLineNumber < 1) {
			return formatMessage(MESSAGE_DELETE_LINE_NOT_FOUND, new Object[]{deletedLineNumber, fileName});
		}
		else {
			deletedLine = listOfContents.get(deletedLineNumber-1);
			listOfContents.remove(deletedLineNumber-1);
		}
		
		return writeListContentsToFile(listOfContents, MESSAGE_DELETE_SUCCESS, new Object[]{fileName, deletedLine}, MESSAGE_DELETE_FAIL, null);
	}
	
	protected static String displayFileContents() {
		ArrayList<String> listOfContents = addFileContentsToList();
		String fileContents = "";
		
		for (int i = 0; i < listOfContents.size(); i++) {
			fileContents = fileContents + (i+1) + ". " + listOfContents.get(i) + "\n";
		}
		
		if (fileContents.trim().isEmpty())
			return formatMessage(MESSAGE_FILE_EMPTY, new Object[]{fileName});
		else
			return fileContents;
	}
	
	protected static String clearFileContents() {
		try {
			new File(fileName).delete();
			new File(fileName).createNewFile();
			return formatMessage(MESSAGE_CLEAR_SUCCESS, new Object[]{fileName});
		} 
		catch (IOException e) {
			return MESSAGE_CLEAR_FAIL;
		}
	}
	
	protected static String sortFileContents() {
		ArrayList<String> listOfContents = addFileContentsToList();
		
		if (listOfContents.isEmpty())
				return formatMessage(MESSAGE_FILE_EMPTY, new Object[]{fileName});
			
		Collections.sort(listOfContents);
		
		return writeListContentsToFile(listOfContents, MESSAGE_SORT_SUCCESS, new Object[]{fileName}, MESSAGE_SORT_FAIL, new Object[]{fileName});
	}
	
	protected static String searchFileContents(String searchTerms) {
		ArrayList<String> listOfContents = addFileContentsToList();
		HashMap<Integer, String> matchedEntries = new HashMap<Integer, String>();
		
		for (int i = 0; i < listOfContents.size(); i++) {
			if (listOfContents.get(i).toLowerCase().equals(searchTerms.toLowerCase())) {
				matchedEntries.put(i+1, listOfContents.get(i));
			}
		}
		
		String searchResult = "";
		
		for (HashMap.Entry<Integer, String> entry : matchedEntries.entrySet()) {
			searchResult = searchResult + entry.getKey()+ ". " + entry.getValue() + "\n";
		}
		
		if (searchResult.trim().isEmpty())
			return formatMessage(MESSAGE_SEARCH_FAIL, new Object[]{searchTerms, fileName});
		else
			return searchResult;
	}
	
	//=========================================================================================================================
    // Additional Functions
    //=========================================================================================================================
	
	private static String getFirstWord(String userCommand) {
		String commandType = userCommand.trim().split("\\s+")[0];
		return commandType.toLowerCase();
	}

	private static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}
	
	private static String formatMessage(String message, Object[] msgArg) {
		return String.format(message, msgArg);
	}
	
	private static void displayMessage(String text, boolean isPrintLine) {
		if (isPrintLine)
			System.out.println(text);
		else
			System.out.print(text);
	}

	private static ArrayList<String> addFileContentsToList() {
		ArrayList<String> temp = new ArrayList<String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line = "";
			
			while ((line = br.readLine()) != null) {
				temp.add(line);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return temp;
	}	
	
	private static String writeListContentsToFile(ArrayList<String> temp, String success, Object[] successArg, String fail, Object[] failArg) {
		try (FileWriter fw = new FileWriter(new File(fileName))) {
			
			for (String s : temp)
				fw.write(s + "\n");
			
			return formatMessage(success, successArg);
		}
		catch (IOException e) {
			return formatMessage(fail, failArg);
		}
	}		
}