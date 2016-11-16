import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Reads DNA sequences from a given gene bank and encodes them 
 * as binary sequences. 
 * XXX: This class is outdated and has been merged with GeneBankCreateBTree -Katy
 * @author 
 */
//public class Interpreter {
//
//	private File file;
//	private Scanner fileScan;
//	private Scanner lineScan;
//	private int k;
//	private String line;
//	private String seq;
//	private final String flag = "ORIGIN";
//	private final String DELIMITER = "[actgn]*";
//	BTree tree = new BTree(0);
//	
//	/**
//	 * Constructor (most of this class happens in here...)
//	 * @param file
//	 * @param k
//	 */
//	public Interpreter(File file, int kin) {
//		
//		k = kin;
//		
//		try {
//			fileScan = new Scanner(file);
//		} catch (FileNotFoundException e) {
//			System.err.println("The file " + file + " could not be found.");
//			e.printStackTrace(); // for debugging, remove later
//			System.exit(1);
//		}
//		
//		// Position the scanners.
//		findFlag();
//		
//		// Apply delimiter to line-by-line scanner.
//		// Do it this way because left-to-right 
//		// scanner is re-instantiated repeatedly.
//		fileScan.useDelimiter(DELIMITER);
//
//		// At this point, scanner is pointing at ORIGIN.
//		// This is one line above the DNA sequences.
//		int maxKeys = 100; // TODO: add eqn for max # of keys using degree (t)
//		
//		while(tree.getUniques() < maxKeys) {
//		if (fileScan.hasNext()) {
//			line = fileScan.next().toLowerCase();
//
//			int start = 0;
//					
//			while (start < line.length() - k) {
//				
//				for (int i = start; i < start + k; i++) {
//					char c = line.charAt(i);
//					
//					switch (c) {
//						case ('a'): seq += "00";
//						case ('c'): seq += "01";
//						case ('g'): seq += "10";
//						case ('t'): seq += "11";	
//					}					
//				}
//				
//				// TODO: Add to B tree here. 
//				TreeObject t = new TreeObject(Long.parseLong(seq));
//				tree.insert(t);
//				
//				start++;
//				
//			}
//		}
//	}
//}
//	
//	
//	/**
//	 * Position the scanners right before the DNA sequence list.
//	 */
//	public void findFlag() {
//		
//		while (fileScan.hasNextLine()) {	
//			lineScan = new Scanner(fileScan.nextLine());
//			String token = lineScan.next();
//			
//			while (lineScan.hasNext()) {
//				if (token.equals(flag)) {
//					return;
//				} 
//			}	
//		}
//		
//	}
//	
//	
//}

