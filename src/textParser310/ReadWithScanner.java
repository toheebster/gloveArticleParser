package textParser310;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.io.Writer;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;

public class ReadWithScanner {
	private final Path fFilePath;
	static String inputDirectory;
	static String outputDirectory;
	private Writer writer;
	static String ceoName;
	static String firmName;
	// private final static Charset ENCODING = StandardCharsets.UTF_8;
	//constructor
	//param aFileName full path of existing readable file
	
	//
	public ReadWithScanner(String aFileName){
	    fFilePath = Paths.get(aFileName);
	  }
	
	//
	public final void processLineByLine() throws IOException {
		Scanner scanner = new Scanner (new File(inputDirectory));
		String articles = scanner.useDelimiter("\\Z").next();
		String[] publication = articles.split("____________________________________________________________");
		for(int i = 0; i<publication.length; i++){
			if (!publication[i].contains("Full text: Not available") && publication[i].contains("Document")){
				String year = publication[i].substring(publication[i].lastIndexOf(", ") + 2);
				//article number only works for two digit numbers
				//fix by using the index of the first occurrence of "of"
				String articleNumber = publication[i].substring(publication[i].indexOf("Document")+9, publication[i].indexOf("Document")+11);
				String filename = firmName.trim()+"-"+ceoName.trim()+"-"+year.trim()+"-"+articleNumber.trim();
				log(filename);
				File outputFile = new File(outputDirectory, filename);
			    writer = new BufferedWriter(new FileWriter(outputFile));
				writer.write(publication[i]);
				writer.close();
			}
		}
		scanner.close();
	}
	
	private static void log(Object aObject){
	    System.out.println(String.valueOf(aObject));
	  }
	
	private static String[] getDir(){
		boolean done = false;
		String inputDir = "";
		String outputDir = "";
		String ceoCount = "";
		String[] consoleInfo = new String[4];
		
		while(!done) {
			System.out.println("Enter input directory");
			System.out.println("What you enter must be a vaild directory ending in '.txt' indicating that it is a text file");
			System.out.println("If this is not the case type 'exit' and rename your file now before restarting the program.");
			System.out.println("To terminate this program at any time, type 'exit' ");
			Scanner dirScanner = new Scanner(System.in);
		    inputDir = dirScanner.nextLine();
		    
		    //checking for exit or that the directory contains a text file 
		    //last 3 letters of input directory must contain the word "txt"
		    if(inputDir.equals("exit")){//case exit
		    	dirScanner.close();
		    	return null;
		    }
		    
			//check if the file exists in the input directory
		    //only want to get the output directory if input directory is valid
			final Path inputDirectory = Paths.get(inputDir);
			if(!inputDir.isEmpty() && Files.exists(inputDirectory) && inputDir.substring(Math.max(0, inputDir.length() - 4) ).equals(".txt")){
				System.out.println("Enter output directory or type 'exit' to quit ");
			    outputDir = dirScanner.nextLine();				    
			    if(outputDir.equals("exit") ) { //exit on output
			    	dirScanner.close();
			    	return null;
			    }
			    final Path outputDirectory = Paths.get(outputDir);
				//loop to get a valid output directory
			    while(!Files.exists(outputDirectory) || outputDir.isEmpty()){
					  System.out.println("Invalid directory, enter output directory or type 'exit' to quit ");
					  outputDir = dirScanner.nextLine();
					    if(outputDir.equals("exit")){
					    	dirScanner.close();
					    	return null;
					    }
					    if(!outputDir.isEmpty() && Files.exists(outputDirectory)){
					    	System.out.println("Your input directory is " + inputDir);
					    	System.out.println("Your output directory is " + outputDir);
					    	dirScanner.close();
					    	done = true;
					    }
				 }
			}
			done = true;
		    consoleInfo[0] = inputDir;
		    consoleInfo[1] = outputDir;
		}
		
		return consoleInfo;
	}
	
		
	
	public static void main(String... aArgs) throws IOException {
		
		String[] consoleInformation = getDir();
		if(consoleInformation != null){
			inputDirectory = consoleInformation[0];
			outputDirectory = consoleInformation[1];	
		
		///Users/xtxo014/Documents/text.txt
		///Users/xtxo014/Documents/Cutter/AmericanAirlines-Crandall.txt

			//CONSIDER HAVING THIS PUT IN BY THE USER
			ceoName = inputDirectory.substring(inputDirectory.indexOf("-") + 1, inputDirectory.indexOf("."));
			firmName = inputDirectory.substring(inputDirectory.lastIndexOf("/") + 1, inputDirectory.indexOf("-"));
			ReadWithScanner parser = new ReadWithScanner(inputDirectory);
			parser.processLineByLine();
			
			log("Done.");
		} else {
			System.out.println("Exited or No proper input directory specified");
		}
	}
}
