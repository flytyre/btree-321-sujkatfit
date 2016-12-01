

/**
 * 
 * @author user1
 *
 */
public class BTreeTest 
{
	static int MAX_ELEMENTS_IN_TREE = 11 ;
	static int degree = 4 ;
	//BTree.BTreeNode temp = new BTree.BTreeNode(3) ;

   public static void main(String[] args)
   {
	   BTreeKaty newtree = new BTreeKaty(degree) ;
	  /* for(int i = 0; i < MAX_ELEMENTS_IN_TREE; i++)
	   {
		   newtree.treeInsertKey(i*10);  
	   }*/
	   
	   
	   
	   /*for(int i = 0; i < 2*degree -1 ; i++)
	   {
		   System.out.println(newtree.root.key[i]);
	   }*/
	   
	   newtree.treeInsertKey(20);
	   newtree.treeInsertKey(30);
	   newtree.treeInsertKey(7);
	   newtree.treeInsertKey(320);
	   newtree.treeInsertKey(120);
	   
	   newtree.treeInsertKey(50);
	   newtree.treeInsertKey(59);
	   newtree.treeInsertKey(29);
	   newtree.treeInsertKey(10);
	   newtree.treeInsertKey(2);
	   
	   newtree.treeInsertKey(18);
	   newtree.treeInsertKey(5);
	   newtree.treeInsertKey(520);
	   //newtree.treeInsertKey(620);
	  // newtree.treeInsertKey(205);
	   
	  /* newtree.treeInsertKey(189);
	   newtree.treeInsertKey(45);
	   newtree.treeInsertKey(63);
	   newtree.treeInsertKey(41);
	   newtree.treeInsertKey(200);*/
	   
	   
	   
	   newtree.printBTree();
	   
   }
	
}
