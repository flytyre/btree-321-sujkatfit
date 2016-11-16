import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GeneBankCreateBTree {
	public static void main (String[] args) throws FileNotFoundException {
		
		File file = null; // TODO: collect this data from args
		Scanner fileScan;
		Scanner lineScan;
		int k;
		int t = 0; // TODO: collect this data from args
		String line;
		String seq = "";
		String flag = "ORIGIN";
		String DELIMITER = "[actgn]*";
		
		//at least 4 argument max of 6
		if (args.length < 4 || args.length > 6) {
			printUsage(0);
		}
		
		k = Integer.parseInt(args[3]); // maybe protect this
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
		while (fileScan.hasNextLine()) {	
			lineScan = new Scanner(fileScan.nextLine());
			String token = lineScan.next();
			
			while (lineScan.hasNext()) {
				if (token.equals(flag)) {
					break;
				} 
			}	
		}
	
		// Apply delimiter to line-by-line scanner.
		// Do it this way because left-to-right 
		// scanner is re-instantiated repeatedly.
		fileScan.useDelimiter(DELIMITER);

		// At this point, scanner is pointing at ORIGIN.
		// This is one line above the DNA sequences.
		int maxKeys = 100; // TODO: add eqn for max # of keys using degree (t)
		
		
		while(tree.getUniques() < maxKeys && fileScan.hasNextLine()) {

			line = fileScan.next().toLowerCase();
			int start = 0;
					
			// XXX: This does not allow wrapping 
			// sequence to next line, should it??
			while (start < line.length() - k) {
				
				for (int i = start; i < start + k; i++) {
					char c = line.charAt(i);
					
					switch (c) {
						case ('a'): seq += "00";
						case ('c'): seq += "01";
						case ('g'): seq += "10";
						case ('t'): seq += "11";	
					}					
				}
				
				TreeObject obj = new TreeObject(Long.parseLong(seq));
				tree.insert(obj);
				start++;
			}
		}
	}
		
	
	static void printUsage(int code) {
		switch (code) {
		case (0):	System.err.println("Invalid number of arguments.");
		
		case (4):	System.err.println("Invalid sequence length");
		
		
		}
		
		
		// print usage here
		
		System.exit(1);
	}
	
}

