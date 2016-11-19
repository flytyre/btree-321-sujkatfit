import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * [STATUS] Nearly complete.
 * TODO: Implement degree generation based on BTreeNode disk size.
 * TODO: Implement debugging.
 * 
 * Driver class: Parses DNA sequences from file and creates BTree.
 * 
 * @author 
 * 
 */
public class GeneBankCreateBTree {
	public static void main (String[] args) throws FileNotFoundException {
		
		File gbk;
		Scanner scan;
		int k;
		int t; 
		int cache;
		int cacheSize = 0;
		int debug = -1;
		String nextLine = "";
		String line = "";
		String dseq = "";
		String bseq = "";
		String startflag = "ORIGIN";
		String endflag = "//";
		String DELIMITER = "[actgn/]*";
		
		
		/*
		 * Collect user input.
		 */
		
		// Check for invalid number of arguments.
		if (args.length < 4 || args.length > 6) {
			printUsage(0);
		}
		
		// Collect cache option and size, check for validity.
		//cache = Integer.parseInt(args[0]);	
		cache = 0;	// XXX: Must set to 0, cache is not yet implemented.
		if (cache < 0 || cache > 1) {	// TODO: Simplify this?
			printUsage(1);
		} else if (cache == 1) {
			if (args.length > 4) {
				cacheSize = Integer.parseInt(args[4]);	
				
				if (cacheSize < 1) {
					printUsage(5);
				}
		
			} else {
				printUsage(7);
			}
		}
		
		// Collect t and check for validity.
		t = Integer.parseInt(args[1]); 
		if (t < 0 || t == 1) {
			printUsage(2);
		} else if (t == 0) {
			// TODO: Generate optimum t based on disk block 
			// of 4096 and size of BTreeNode on disk. <-- what is this?
			
		}
		
		// Collect gbk file and check for validity.
		gbk = new File(args[2]); 
		if (!gbk.exists()) {
			printUsage(3);
		}
		
		// Collect k and check for validity.
		k = Integer.parseInt(args[3]); // maybe protect this?
		if (k < 1 || k > 31) {
			printUsage(4);
		}
		
		// Collect debug option and check for validity.
		if (args.length == 6) {
			debug = Integer.parseInt(args[5]);
			if (debug < 0 || debug > 1) {
				printUsage(6);
			}
		}
	
		
		/*
		 * Instantiate BTree and Scanner.
		 */

		BTree tree = new BTree(t);
		scan = new Scanner(gbk);	// throws clause required by Java, but code should never reach this point if gbk does not exist.
		
	
		/*
		 * Collect and convert sequences, populate BTree.
		 */
		
		// Scanner will always be pointing to the line after the one 
		// we are currently working on. This is necessary for wrapping.
		while(!nextLine.contains(endflag)) {  // Continue until endflag is found -- just after DNA sequences.
			
			// Position the scanner.
			while(!nextLine.contains(startflag)) {	// Continue until startflag is fonud -- just before DNA sequences.
				nextLine = scan.nextLine();
			}
			
			// Apply delimiter to the scanner.
			scan.useDelimiter(DELIMITER);

			int start = 0;		// reset cursor
			line = nextLine;	// advance line
			nextLine = scan.nextLine().toLowerCase();	// grab next line
			
			// Collect DNA sequence.
			while (start < line.length()) {
			
				int end = start + k;
				
				if (end < line.length()) {	// If the sequence to be collected fits on the current line...
				
					for (int i = start; i < end; i++) {
						dseq += line.charAt(i);	
					}
					
				} else {	// else, we have to wrap to the next line to finish the sequence.
					
					end = k - (nextLine.length() - start);
					
					for (int i = start; i < line.length(); i++) {
						dseq += line.charAt(i);	
					}
					
					for (int i = 0; i < end; i++) {
						dseq += nextLine.charAt(i);
					}
				}
				
				// Convert the collected DNA sequence to binary.
				if (dseq.length() == k) {	// Discard any sequences != k in length.
					for (int i = 0; i < dseq.length(); i++) {
						char c = dseq.charAt(i);
				
						switch (c) {
							case ('a'): bseq += "00";
							case ('c'): bseq += "01";
							case ('g'): bseq += "10";
							case ('t'): bseq += "11";	
							case ('n'): bseq = "";	// Unreadable DNA, discard entire sequence.
						}		
					}
				
					// Add the binary sequence to the tree, ignore sequences of inappropriate length.
					if (bseq.length() == k * 2) {
						TreeObject obj = new TreeObject(Long.parseLong(bseq));
						tree.insert(obj);
					}
				}
				
				start++;	// advance cursor
				dseq = "";	// clear sequences
				bseq = ""; 	
			}			
		}
		
		scan.close();
	}
		
	
	/**
	 * printUsage
	 * @param code The specific error that resulted in calling printUsage
	 */
	static void printUsage(int code) {
		// TODO: Improve language.
		switch (code) {
			case (0):	System.err.println("Invalid number of arguments.");
						break;
						
			case (1):	System.err.println("Invalid cache option.");
						System.err.println("Must use either 0 for no cache or 1 for cache.");
						break;
						
			case (2): 	System.err.println("Invalid degree.");
						System.err.println("Must be > 1, or use 0 for optimized degree.");
						break;
				
			case (3): 	System.err.println("The given gbk file does not exist.");
						break;
			
			case (4):	System.err.println("Invalid sequence length.");
						System.err.println("Must be in the range [1, 31].");
						break;
						
			case (5):	System.err.println("Invalid cache size.");
						System.err.println("Must be > 0.");	// XXX: is this correct?
						break;
						
			case (6): 	System.err.println("Invalid debug option.");
						System.err.println("Must use either 0 to print to standard error or 1 to print to file.");
						break;
						
			case (7): 	System.err.println("No cache size given.");
						break;
		}
	
		System.err.println("Usage: java GeneBankCreateBTree <cache> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
		
		System.exit(1);
	}
	
}

