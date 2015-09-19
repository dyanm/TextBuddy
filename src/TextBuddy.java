import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
	private static final String MESSAGE_DELETE_FAIL = "unable to delete line.";
	private static final String MESSAGE_DELETE_LINE_NOT_FOUND = "line number %1$d does not exist in %2$s\n";
	private static final String MESSAGE_DISPLAY_FAIL = "unable to display contents from file.";
	private static final String MESSAGE_CLEAR_SUCCESS = "all content deleted from %1$s\n";
	private static final String MESSAGE_CLEAR_FAIL = "unable to clear file";
	private static final String MESSAGE_SORT_SUCCESS = "%1$s has been alphabetically sorted\n";
	private static final String MESSAGE_SORT_FAIL = "unable to sort %1$s";
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
		
		// Appends a new line to the text file based on user input and the file is only accessed when the command is processed.
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
			bw.write(commandParameters + "\n");
			return formatMessage(MESSAGE_ADD, new Object[]{fileName, commandParameters});
		}
		catch (IOException e) {
			return formatMessage(MESSAGE_INVALID_PARAMETER, new Object[]{commandParameters});
		}
	}
	
	protected static String deleteExistingLine(String commandParameters) {
		int deletedLineNumber;
		// Checks if the command parameter is a valid integer before proceeding to prevent mis-entry by the user.
		try {
			deletedLineNumber = Integer.parseInt(commandParameters);
		}
		catch (NumberFormatException e) {
			return formatMessage(MESSAGE_INVALID_PARAMETER, new Object[]{commandParameters});
		}
		
		String deletedLine = "";
		StringBuffer sb = new StringBuffer("");
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			int currentLineNumber = 0;
			String currentLine = "";

			// Stores all lines except for the deleted line in a StringBuffer for write-back later.
			while ((currentLine = br.readLine()) != null) {
				currentLineNumber++;
				if (currentLineNumber != deletedLineNumber) {
					sb.append(currentLine + "\n");
				}
				else {
					deletedLine = currentLine;
				}
			}
			
			// Returns Line Not Found message when the user-specified line number is out of valid range.
			if (deletedLineNumber > currentLineNumber)
				return formatMessage(MESSAGE_DELETE_LINE_NOT_FOUND, new Object[]{deletedLineNumber, fileName});
		}
		catch (IOException e) {
			return MESSAGE_DELETE_FAIL;
		}
		
		// Writes all lines from the StringBuffer back to file.
		try (FileWriter fw = new FileWriter(new File(fileName))) {
			fw.write(sb.toString());
		}
		catch (IOException e) {
			return MESSAGE_DELETE_FAIL;
		}
		return formatMessage(MESSAGE_DELETE_SUCCESS, new Object[]{fileName, deletedLine});
	}
	
	protected static String displayFileContents() {
		ArrayList<String> temp = addFileContentsToList();
		String result = "";
		
		for (int i = 0; i < temp.size(); i++) {
			result = result + (i+1) + ". " + temp.get(i) + "\n";
		}
		
		if (result.trim().isEmpty())
			return formatMessage(MESSAGE_FILE_EMPTY, new Object[]{fileName});
		else
			return result;
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
		
		return writeListContentsToFile(listOfContents);
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
	
	private static String writeListContentsToFile(ArrayList<String> temp) {
		try (FileWriter fw = new FileWriter(new File(fileName))) {
			for (String s : temp)
				fw.write(s + "\n");
			
			return formatMessage(MESSAGE_SORT_SUCCESS, new Object[]{fileName});
		}
		catch (IOException e) {
			return formatMessage(MESSAGE_SORT_FAIL, new Object[]{fileName});
		}
	}		
}