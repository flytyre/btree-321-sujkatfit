import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Test class for reading an existing BTree from a file.
 * 
 * @author Sujeet Ayyapureddi, Margiawan Fitriani, Kathryn Silva
 *
 */
public class BTreeRead {

	public static void main(String[] args) throws IOException {

		File btree ;

		btree = new File(args[0]); 

		//RandomAccessFile raf = new RandomAccessFile(btree, "rw");	// what is this line doing?  raf is not used.

		if (!btree.exists()) { 
			System.out.println("File Not found");
		}

		BTree newtree = new BTree (btree) ;

		System.out.println("Printing out the BTree read from the File");
		System.out.println(newtree.printBTree());
		
		
		newtree.search(40);
		
//		System.out.println(raf.readInt());
//		System.out.println(raf.readInt());
//		System.out.println(raf.readInt());

	}
}
