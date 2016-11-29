

/**
 * 
 * @author user1
 *
 */
public class BTreeTest 
{
	static int MAX_ELEMENTS_IN_TREE = 40 ;
	static int degree = 4 ;
	//BTree.BTreeNode temp = new BTree.BTreeNode(3) ;

   public static void main(String[] args)
   {
	   BTree newtree = new BTree(degree) ;
	 
	   /* for(int i = 0; i < MAX_ELEMENTS_IN_TREE; i++)
	   {
		   newtree.treeInsertKey(i*10);  
	   }*/
	   
	   
	   /*for(int i = 0; i < 2*degree -1 ; i++)
	   {
		   System.out.println(newtree.root.key[i]);
	   }*/
	   
	   
	   TreeObject obj;
	   
	   for (int key = 0; key < MAX_ELEMENTS_IN_TREE; key++) {
		   
		   obj = new TreeObject(key);
		   newtree.insert(obj);
		   
	   }
	  
	   //newtree.printBTree();
	   
   }
	
}
