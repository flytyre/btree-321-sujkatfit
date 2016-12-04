import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author Sujeet Ayyapureddi, Margiawan Fitriani, Kathryn Silva
 *
 */
public class BTreeRead {
	
	public static void main(String[] args) throws IOException {
		
		File btree ;

		btree = new File(args[0]); 
		
		RandomAccessFile raf = new RandomAccessFile(btree, "rw");	// what is this line doing?  raf is not used.

		if (!btree.exists()) { 
			System.out.println("File Not found");
		}
		
		BTree newtree = new BTree (btree) ;
		
		newtree.printBTree();
       
	}
}
