import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GeneBankCreateBTree {
	public static void main (String[] args) throws FileNotFoundException {
		
		File file = null; // TODO: collect this data from args
		Scanner fileScan;
		int k;
		int t; // TODO: collect this data from args
		int cache;
		String prevLine = "";
		String nextLine = "";
		String line = "";
		String dseq = "";
		String bseq = "";
		String startflag = "ORIGIN";
		String endflag = "//";
		String DELIMITER = "[actgn/]*";
		
		// Check for invalid number of arguments.
		if (args.length < 4 || args.length > 6) {
			printUsage(0);
		}
		
		cache = Integer.parseInt(args[1]);
		
		
		// Collect t and check for validity.
		t = Integer.parseInt(args[1]); 
		if (t < 0 || t == 1) {
			printUsage(2);
		} else if (t == 0) {
			// TODO: Generate optimum t based on disk block 
			// of 4096 and size of BTreeNode on disk. <-- what is this?
		}
		
		// Collect k and check for validity.
		k = Integer.parseInt(args[3]); // maybe protect this?
		if (k < 1 || k > 31) {
			printUsage(4);
		}
	
		BTree tree = new BTree(t);

//		try {
//			fileScan = new Scanner(file);
//		} catch (FileNotFoundException e) {
//			System.err.println("The file " + file + " could not be found.");
//			e.printStackTrace(); // for debugging, remove later
//			System.exit(1);
//		}
		
		fileScan = new Scanner(file);
		
		// Position the scanners.
		// Possibly merge this with while loops below?
		while (fileScan.hasNextLine()) {	
			line = fileScan.nextLine();
			
			if (!line.contains(startflag)) {
				break;	
			}				
		}
	
		// Apply delimiter to the scanner.
		fileScan.useDelimiter(DELIMITER);

		// At this point, scanner is pointing at ORIGIN.
		// This is one line above the DNA sequences.
		if (fileScan.hasNextLine()) {
			nextLine = fileScan.nextLine().toLowerCase();
		}
		
		// Scanner will always be pointing to the line 
		// after the one we are currently working on.  
		// This is necessary for wrapping.
		while(!nextLine.contains(endflag)) { // continue until all data is read, this may need to be changed.

			int start = 0;		// reset cursor
			line = nextLine;	// advance line
			nextLine = fileScan.nextLine().toLowerCase();	// grab next line
			
			// outer while breaks when // is collected as next line
			// meaning line is the last line of dna sequences
			// holy crap my head hurts
			
			while (start < line.length()) {
			
				int end = start + k;
				
				// If the sequence collected fits on the current line.
				if (end < line.length()) {
				
					for (int i = start; i < end; i++) {
						dseq += line.charAt(i);	
					}
					
				// If we have to wrap to the next line to finish the sequence.
				} else {
					
					end = k - (nextLine.length() - start);
					
					for (int i = start; i < line.length(); i++) {
						dseq += line.charAt(i);	
					}
					
					for (int i = 0; i < end; i++) {
						dseq += nextLine.charAt(i);
					}
				}
				
				// Convert the collected DNA sequence to binary.
				if (dseq.length() == k) {	// discard any sequences < k
					for (int i = 0; i < dseq.length(); i++) {
						char c = dseq.charAt(i);
				
						switch (c) {
							case ('a'): bseq += "00";
							case ('c'): bseq += "01";
							case ('g'): bseq += "10";
							case ('t'): bseq += "11";	
						}		
					}
				
					// Add the binary sequence to the tree.
					TreeObject obj = new TreeObject(Long.parseLong(bseq));
					tree.insert(obj);
				}
				
				start++;	// advance cursor
				dseq = "";	// clear sequences
				bseq = ""; 
				
			}			
		}
	}
		
	
	static void printUsage(int code) {
		switch (code) {
		case (0):	System.err.println("Invalid number of arguments.");
		// case 1: the cache option is invalid
		// case 2: the degree given is invalid
		// case 3: the gbk file does not exist
		case (4):	System.err.println("Invalid sequence length");
		
		
		}
		
		
		// print usage here
		
		System.exit(1);
	}
	
}

