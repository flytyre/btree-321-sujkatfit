import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * [STATUS] Nowhere near done!
 * TODO: Modify printUsage.
 * TODO: Implement debugging.
 * TODO: Implement search (??????).
 * 
 * Driver class: Searches a given BTree using a given query file.
 * 
 * @author 
 *
 */
public class GeneBankSearch {
	public static void main (String args[]) throws FileNotFoundException {

		int cache;
		int cacheSize;
		int debug = -1;
		int k; 
		File btree;
		File query;
		Scanner scan;
		String DELIMITER = "[actg]*";
		String filename;
		String dseq = "";
		String bseq = "";
		
		// Check for invalid number of arguments.
		if (args.length < 3 || args.length > 5) {
			printUsage(0);
		}

		// Collect cache option and size, check for validity.
		//cache = Integer.parseInt(args[0]);	
		cache = 0;	// XXX: Must set to 0, cache is not yet implemented.
		if (cache < 0 || cache > 1) {	// TODO: Simplify this?
			printUsage(1);
		} else if (cache == 1) {
			if (args.length > 3) {
				cacheSize = Integer.parseInt(args[3]);	

				if (cacheSize < 1) {
					printUsage(5);
				}

			} else {
				printUsage(7);
			}
		}

		// Collect btree file and check for validity.
		btree = new File(args[1]); 
		filename = args[1];
		if (!btree.exists()) {
			printUsage(2);
		}

		// Collect query file and check for validity.
		query = new File(args[2]);
		if (!query.exists()) {
			printUsage(3);
		}
		
		// Collect debug option and check for validity.
		if (args.length == 5) {
			debug = Integer.parseInt(args[4]);
			if (debug != 0) {
				printUsage(6);
			}
		}
				
		// XXX: Is there an easier way to collect the sequence length?
		int stop = 0;
		int start = filename.length() - 1;
		int flag = 0;
		while (filename.charAt(start) != '.' && flag < 2) {
			if (filename.charAt(start) == '.') {
				if (flag == 0) { 	// if no '.' have been found yet
					stop = start;	// mark this '.' as the start
				}					
				flag++;				// increment flag (number of '.' found)
			}
			start--;
		}
		
		String tmp = "";
		for (int i = start; i < stop; i++) {
			tmp += filename.charAt(i);
		}
		
		k = Integer.parseInt(tmp);	
	
		scan = new Scanner(query);	// throws clause required by Java, but code should never reach this point if query does not exist.
		
		while (scan.hasNextLine()) {
		
			dseq = scan.nextLine();
			
			// Ensure query file is compatible with given BTree.
			if (dseq.length() != k) {
				printUsage(8);
			} 
			
			for (int i = 0; i < dseq.length(); i++) {
				char c = dseq.charAt(i);
		
				switch (c) {
					case ('a'): bseq += "00";
					case ('c'): bseq += "01";
					case ('g'): bseq += "10";
					case ('t'): bseq += "11";	
					//case ('n'): seq = "";	// XXX: I don't think this is needed when searching... check query files to be sure
				}		
			}
			
			// do something with bseq here...
			
			// do we create a BTree object from the given file?
			// or do we search the BTree file?  It's stored in binary...
			// so we can't use a scanner on it...
			// how is this supposed to work?
			
			bseq = "";	
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
						
			case (2): 	System.err.println("The given btree file does not exist.");
						break;
						
			case (3): 	System.err.println("The given query file does not exist.");
						break;
						
			case (4):	break;
			
			case (5):	System.err.println("Invalid cache size.");
						System.err.println("Must be > 0.");	// XXX: is this correct?
						break;
						
			case (6): 	System.err.println("Invalid debug option.");
						System.err.println("Must use 0.");
						break;
						
			case (7): 	System.err.println("No cache size given.");
						break;
						
			case (8):	System.err.println("The given btree and query files are not compatible.");
						break;
		}
	
		if (code < 8) {
			System.err.println("Usage: java GeneBankSearch <cache> <btree file> <query file> [<cache size>] [<debug level>]");			
		}
		
		System.exit(1);
	}
	
			
}
