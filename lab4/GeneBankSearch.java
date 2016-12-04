import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * [STATUS] Less than halfway done.
 * TODO: Modify printUsage.
 * TODO: Implement debugging.
 * TODO: Implement search (??????).
 * 
 * Driver class: Searches given BTree using given query file.
 * 
 * @author 
 *
 */
public class GeneBankSearch {
	public static <BTreeNode> void main (String args[]) throws NumberFormatException, IOException {

		int cache;
		int cacheSize;
		int debug = -1;
		int k; 
		File source;
		File query;
		Scanner scan;
		String DELIMITER = "[actg]*";
		String filename;
		String dseq = "";
		String bseq = "";
		BTree btree;
		
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
		source = new File(args[1]); 
		filename = args[1];
		if (!source.exists()) {
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
	
		btree = new BTree(source);
		scan = new Scanner(query);	// throws clause required by Java, but code should never reach this point if query does not exist.
		
		while (scan.hasNextLine()) {
		
			dseq = scan.nextLine();
			
//			// Ensure query file is compatible with given BTree. *** We do not have to check for this, it is assumed k matches in query and source
//			if (dseq.length() != k) {
//				printUsage(8);
//			} 
			
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

			btree.search(Long.parseLong(bseq)); 
			bseq = "";	
		}
		
		scan.close();
	}
	
	/**
	 * printUsage
	 * @param code The specific error that resulted in calling printUsage
	 */
	static void printUsage(int code) {

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
						System.err.println("Must be > 0.");	
						break;
						
			case (6): 	System.err.println("Invalid debug option.");
						System.err.println("Must use 0.");
						break;
						
			case (7): 	System.err.println("No cache size given.");
						break;
						
//			case (8):	System.err.println("The given btree and query files are not compatible.");
//						break;
		}
	
		if (code < 8) {
			System.err.println("Usage: java GeneBankSearch <cache> <btree file> <query file> [<cache size>] [<debug level>]");			
		}
		
		System.exit(1);
	}
	
			
}
