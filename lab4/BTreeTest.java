import java.io.File;
import java.io.IOException;

/**
 * Test class for creating a new BTree.
 * 
 * @author Sujeet Ayyapureddi, Margiawan Fitriani, Kathryn Silva
 *
 */
public class BTreeTest {

	public static void main(String[] args) throws IOException {

		int MAX_ELEMENTS_IN_TREE = 20 ;
		int degree = 7 ;
		File f  = new File("temp1.dat");

		BTree newtree = new BTree (f, degree);

		for(int i = 0; i < MAX_ELEMENTS_IN_TREE; i++)
		{
			KeyObject tb2 = new KeyObject(i*10);
			newtree.treeInsertKey(tb2);  
		}

		System.out.println(newtree.printBTree());
	}
}
