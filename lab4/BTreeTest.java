import java.io.File;
import java.io.IOException;

/**
 * TestBench to test the BTree Class
 * @author user1
 *
 */
public class BTreeTest 
{
	public static void main(String[] args) throws IOException
	{
        
	//BTree.BTreeNode temp = new BTree.BTreeNode(3) ;

		File f  = new File("temp1.dat");
        int degree = 4 ;
	   int MAX_ELEMENTS_IN_TREE = 11 ;
	   //BTree newtree = new BTree (degree, f) ;
	   BTreeKaty newtree = new BTreeKaty (degree, f);
	   /* for(int i = 0; i < MAX_ELEMENTS_IN_TREE; i++)
	   {
		   newtree.treeInsertKey(i*10);  
	   }*/
	   
	   TreeObject tb1 = new TreeObject(20);
	   TreeObject tb2 = new TreeObject(30);
	   TreeObject tb3 = new TreeObject(7);
	   TreeObject tb4 = new TreeObject(320);
	   TreeObject tb5 = new TreeObject(120);
	   
	   TreeObject tb6 = new TreeObject(50);
	   TreeObject tb7 = new TreeObject(59);
	   TreeObject tb8 = new TreeObject(79);
	   TreeObject tb9 = new TreeObject(10);
	   TreeObject tb10 = new TreeObject(20);
//	   
//	   TreeObject tb11 = new TreeObject(18);
//	   TreeObject tb12 = new TreeObject(5);
//	   TreeObject tb13 = new TreeObject(41);
//	   TreeObject tb14 = new TreeObject(620);
//	   TreeObject tb15 = new TreeObject(205);
//	   
//	   TreeObject tb16 = new TreeObject(189);
//	   TreeObject tb17 = new TreeObject(45);
//	   TreeObject tb18 = new TreeObject(63);
//	   TreeObject tb19 = new TreeObject(520);
//	   TreeObject tb20 = new TreeObject(200);
//           
//       TreeObject tb21 = new TreeObject(210);
//	   TreeObject tb22 = new TreeObject(410);
//	   TreeObject tb23 = new TreeObject(510);
//	   TreeObject tb24 = new TreeObject(610);
//	   TreeObject tb25 = new TreeObject(110);
//	   
	   
	   
	   /*for(int i = 0; i < 2*degree -1 ; i++)
	   {
		   System.out.println(newtree.root.key[i]);
	   }*/
	   
	   newtree.treeInsertKey(tb1);
	   newtree.treeInsertKey(tb2);
	   newtree.treeInsertKey(tb3);
	   newtree.treeInsertKey(tb4);
	   newtree.treeInsertKey(tb5);
	   
	   newtree.treeInsertKey(tb6);
	   newtree.treeInsertKey(tb7);
	   newtree.treeInsertKey(tb8);
	   newtree.treeInsertKey(tb9);
	   newtree.treeInsertKey(tb10);
//	   
//	   newtree.treeInsertKey(tb11);
//	   newtree.treeInsertKey(tb12);
//	   newtree.treeInsertKey(tb13);
//	   newtree.treeInsertKey(tb14);
//	   newtree.treeInsertKey(tb15);
//	   
//	   newtree.treeInsertKey(tb16);
//	   newtree.treeInsertKey(tb17);
//	   newtree.treeInsertKey(tb18);
//	   newtree.treeInsertKey(tb19);
//	   newtree.treeInsertKey(tb20); 
//	   
//	   newtree.treeInsertKey(tb21);
//	   newtree.treeInsertKey(tb22);
//	   newtree.treeInsertKey(tb23);
//	   newtree.treeInsertKey(tb24);
//	   newtree.treeInsertKey(tb25);
//	   
	   //newtree.printBTree_Freq();
	   newtree.printBTree();
	   
   }
	
}
