import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
/**
 * [STATUS] Nearly complete.
 *
 * TODO: Implement debugging.
 * 
 * Driver class: Parses DNA sequences from file and creates BTree.
 * 
 * @author 
 * 
 */
public class GeneBankCreateBTree {

	public static void main (String[] args) throws IOException {
		
		File gbk;
		File btree;
		Scanner scan;
		boolean collecting = false;
		int k;
		int t; 
		int cache;
		int cacheSize = 0;
		int debug = 0;
		String nextLine = "";
		String line = "";
		String dseq = "";
		String bseq = "";
		String startflag = "ORIGIN";
		String endflag = "//";
		String DELIMITER = "[actgn/]*";
		
		/*
		 * Collect input from command-line.
		 */
		
		// Check for invalid number of arguments.
		if (args.length < 4 || args.length > 6) {
			printUsage(0);
		}
		
		// Collect cache option and size, check for validity.
		cache = Integer.parseInt(args[0]);	
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
		
		String filename = (gbk.getName() + ".btree.data." + k + "." + t);
		btree = new File(filename);
		BTree tree = new BTree(btree, t); 
		scan = new Scanner(gbk);	// throws clause required by Java, but code should never reach this point if gbk does not exist.
		nextLine = scan.nextLine();
		
		System.err.println("Creating BTree with filename " + filename);
		System.err.println("Populating BTree from given gbk file.  This may take some time.");
		
		/*
		 * Collect and convert sequences, populate BTree.
		 */
		
		// Scanner will always be pointing to the line after the one 
		// we are currently working on. This is necessary for wrapping.
		while(true) {  // Continue until endflag is found -- just after DNA sequences.
			
//			while(!nextLine.contains(startflag) && !collecting) {	// Continue until startflag is fonud -- just before DNA sequences.
//				nextLine = scan.nextLine();
//			}

			// Position the scanner.
			while(!collecting) {
				nextLine = scan.nextLine();
				if (nextLine.contains(startflag)) {
					nextLine = scan.nextLine().toLowerCase().trim();
					collecting = true;
					break;
				}
			}
			
			

			// Apply delimiter to the scanner.
			scan.useDelimiter(DELIMITER);
			
			int start = 0;		// reset cursor
			line = nextLine;	// advance line
			nextLine = scan.nextLine().toLowerCase().trim();	// grab next line
	
			
			// Collect DNA sequence.
			while (start < line.length()) {
			
				int end = start + k;
				
				if (end < line.length()) {	// If the sequence to be collected fits on the current line...
				
					for (int i = start; i < end; i++) {
						dseq += line.charAt(i);	
					}
					
				} else {	// else, we have to wrap to the next line to finish the sequence.
					
					if (nextLine.contains(endflag)) {
						collecting = false;
						break;
					}
					
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
										break;
							case ('c'): bseq += "01";
										break;
							case ('g'): bseq += "10";
										break;
							case ('t'): bseq += "11";	
										break;
							case ('n'): bseq = "";	// Unreadable DNA, discard entire sequence.
										break;
						}		
					}
				
					// Add the binary sequence to the tree, ignore sequences of inappropriate length.
					if (bseq.length() == k * 2) {
						KeyObject obj = new KeyObject(Long.parseLong(bseq));
						try {
							tree.treeInsertKey(obj);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.err.println("fail");
							e.printStackTrace();
						}
					}
				}
			
				start++;	// advance cursor
				bseq = ""; 	
				dseq = "";	// clear sequences	
			}
			
			if (!collecting) {
				break;
			}
			
		}
		
		scan.close();
		System.out.println(tree.printBTree());
		
		if (debug == 1) {
			File dump = new File("dump");
			
		}
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

