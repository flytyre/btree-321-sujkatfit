import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * [STATUS] Nowhere near done!
 * TODO: Modify printUsage.
 * TODO: Implement debugging.
 * TODO: Collect k from BTree file name and check for valid sequence length in query file.
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
		String filename;
		File btree;
		int t;
		int k = 0; // XXX: Fix this
		File query;
		int debug = -1;
		Scanner scan;
		String DELIMITER = "[actg]*";
		String sequence = "";

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

		// Grab degree from BTree filename.
		String tmp = "";
		int stop = filename.length() - 1;
		while (filename.charAt(stop) != '.') {
			stop--;
		}
		for(int i = filename.length() - 1; i > stop; i--) {
			tmp += filename.charAt(i);
		}
		t = Integer.parseInt(tmp);	
		
		// XXX: How to get the sequence length???
	
		scan = new Scanner(query);	// throws clause required by Java, but code should never reach this point if query does not exist.
		
		while (scan.hasNextLine()) {
		
			sequence = scan.nextLine();
			
			// Ensure query file is compatible with given BTree.
			if (sequence.length() != k) {
				// CRAAAASH
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
			
			case (1):	System.err.println("Invalid cache option.");
						System.err.println("Must use either 0 for no cache or 1 for cache.");
						
			case (2): 	System.err.println("The given btree file does not exist.");
						
			case (3): 	System.err.println("The given query file does not exist.");
			
			case (5):	System.err.println("Invalid cache size.");
						System.err.println("Must be > 0.");	// XXX: is this correct?
			
			case (6): 	System.err.println("Invalid debug option.");
						System.err.println("Must use 0.");
				
			case (7): 	System.err.println("No cache size given.");
			
		}
	
		System.err.println("Usage: java GeneBankSearch <cache> <btree file> <query file> [<cache size>] [<debug level>]");
		
		System.exit(1);
	}
	
			
}
