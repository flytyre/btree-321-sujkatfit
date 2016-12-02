import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * @author user1
 *
 */
public class BTreeKaty
{

	public final int degree ;
	int diskIndex = 0 ;
	File f ;
	RandomAccessFile raf ;

	BTreeNode root ;

	long nodesize ;


	// Constructor
	public BTreeKaty(int t, File f)
	{
		degree =t ;
		root = new BTreeNode() ;
		root.index = 0;
		root.hasIndex = true ;
		root.isLeaf = true;


		f = new File("Test.dat") ;
		try {
			raf = new RandomAccessFile(f,"rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Inserts keyObject into the BTree.
	 * @param keyObject given keyObject
	 * @throws IOException 
	 */
	public void treeInsertKey(TreeObject keyObject) throws IOException
	{
//		BTreeNode rt = root ;

		root.insertKeyObject(root, keyObject);
		
	}

	/**
	 * 
	 * @param node to be persisted/written to disk
	 * @return index value. If the node is a new node and does not have an index, then a new
	 * index is generated.
	 * @throws IOException
	 */
	public int diskWrite(BTreeNode node) throws IOException 
	{
		
		
		if(!(node.hasIndex))
		{
			node.index = genDiskIndex() ;
			node.hasIndex = true ;
		}

		raf.seek(node.index * getNodeSize()) ;

		// Writing the KeyObject
		for(int i = 0 ; i < 2*(degree)-1 ; i++)
		{
			if (node.keyObject[i] == null) {
				System.err.println("Null object");
			}
			raf.writeLong(node.keyObject[i].getKey());      // Key of the tree Object
			raf.writeInt(node.keyObject[i].getFrequency()); // Frequency of the tree Object
		}

		// Writing the pointers
		for(int i = 0 ; i < 2*(degree) ; i++)
		{
			raf.writeLong(node.diskOffsets[i]);      // Pointer to the file on disk
		}

		raf.writeBoolean(node.isLeaf);         // Boolean Value of isLeaf()
		raf.writeBoolean(node.isFull);         // Boolean Value of isFull()
		raf.writeInt(node.numKeys) ;           // int Value of numKeys

		raf.writeBoolean(node.hasIndex);       // Boolean Value of hasIndex
		raf.writeInt(node.index) ;             // int Value of index

		return node.index ;

	}


	private BTreeNode diskRead(long offset) throws IOException
	{
		System.out.println("offset:" + offset);

		raf.seek(offset * getNodeSize()) ;

		BTreeNode node = new BTreeNode() ;

		// Reading the KeyObject
		for(int i = 0 ; i < 2*(degree) -1 ; i++)
		{
			node.keyObject[i] = new TreeObject(raf.readLong())  ;     // Key of the tree Object
			node.keyObject[i].setFrequency(raf.readInt());            // Frequency of the tree Object
		}

		// Reading the pointers
		for(int i = 0 ; i < 2*(degree) ; i++)
		{
			node.diskOffsets[i] = raf.readLong();      // Pointer to the file on disk

		}

		node.isLeaf = raf.readBoolean();         // Boolean Value of isLeaf()
		node.isFull = raf.readBoolean();         // Boolean Value of isFull()
		node.numKeys = raf.readInt() ;           // int Value of numKeys

		node.hasIndex = raf.readBoolean();       // Boolean Value of hasIndex
		node.index = raf.readInt() ;             // int Value of index

		return node;
	}


	/**
	 * Prints all nodes of the Btree level by level(BSF).
	 * @throws IOException 
	 */
	public void printBTree() throws IOException
	{
		Queue<BTreeNode> BTQueue = new LinkedList<BTreeNode>();
		BTQueue.add(root);
		while(!BTQueue.isEmpty())
		{
			BTreeNode node = BTQueue.remove();
			node.printNode();
			if(!node.isLeaf)
			{
				for(int i=0; i <= node.numKeys; i++)
				{
					BTQueue.add(diskRead(node.diskOffsets[i]));
				}
			}
		}
	}



	public void printBTree_Freq()
	{
		Queue<BTreeNode> BTQueue = new LinkedList<BTreeNode>();
		BTQueue.add(root);
		while(!BTQueue.isEmpty())
		{
			BTreeNode node = BTQueue.remove();
			node.printNode_freq();
			if(!node.isLeaf)
			{
				for(int i=0; i <= node.numKeys; i++)
				{
					BTQueue.add(node.pointers[i]);
				}
			}
		}
	}

	public int genDiskIndex()
	{
		diskIndex ++ ;
		return diskIndex ;
	}


	/**
	 * Method calculates the size of the node in Bytes
	 * @return nodesize in Bytes
	 */
	public long getNodeSize()
	{
		int keyobjectsize = Long.BYTES + Integer.BYTES ;
		int pointersize = Long.BYTES ;
		int diskoffsetsize = Long.BYTES ;
		int isLeafsize = 1 ;
		int isFullsize = 1 ;
		int numKeyssize = Integer.BYTES ;
		int hasIndexSize = 1 ;
		int indexSize = Integer.BYTES ;

		int numpointers = 2*degree ;
		int numkeys = 2*degree -1 ;

		int size = keyobjectsize*numkeys + pointersize*numpointers + diskoffsetsize *numpointers + isLeafsize + isFullsize + numKeyssize + hasIndexSize +indexSize ; 

		return size ; 

	}
	
	/**
	 * CheckOffsets
	 * @throws IOException 
	 */
	public void printOffsets(BTreeNode node) throws IOException {
		for(int i = 0 ; i < 2*(degree) ; i++)
		{
			node.diskOffsets[i] = raf.readLong();      // Pointer to the file on disk
		}
	}


	// Inner Class
	public class BTreeNode 
	{
		public TreeObject[] keyObject;
		public BTreeNode[] pointers; // pointers to the children nodes
		public long[] diskOffsets ;
		boolean isLeaf;
		boolean isFull;
		public int numKeys; // number of elements in the Node
		public int numPointers;
		//private int degree; //degree of the BTree
		boolean hasIndex ;
		int index ;

		/**
		 * 
		 * @param t
		 */
		public  BTreeNode()
		{
			isLeaf = false;
			keyObject = new TreeObject[2*degree-1];
			pointers = new BTreeNode[2*degree];
			diskOffsets = new long[2*degree] ;
			numKeys=0;
			numPointers = 0;
			isFull = isFull(); 

			for(int i = 0 ; i < 2*degree ; i ++)
			{
				diskOffsets[i] = 0;
			}

			for(int j = 0 ; j < 2*degree -1 ; j++)
			{
				keyObject[j] = new TreeObject(Integer.MAX_VALUE);  // Cant figure out null pointer issue here.
			}

			index = -1;
			hasIndex = false;
		}
		

		/**
		 * This method splits the node given by the ith child of the given node
		 * NodeSplit should never be called on a leaf that is not also the root.
		 * @param node Parent of the node to be split.
		 * @param i is the index of the child of the node which is to be split.
		 * @throws IOException 
		 */
		public void nodeSplit(BTreeNode node, int i)
		{
			int index = i ;
			int temp_diskOffSet ;

			///////////////////////////////
			// Case 1: very first split //
			/////////////////////////////
			// # of new nodes = 2
			// the original root becomes a child of new node
			// other new node inherits leaf status from original root
			// root pointer moves to original root's parent
		
			if (node == root && node.isLeaf) {
	
				BTreeNode tmp = root; // store current root
				
				BTreeNode newRoot = new BTreeNode() ;	// create new node
				root = newRoot ;						// point root to this new node
				
//				newRoot.isLeaf= false;	// taken care of by default
				newRoot.numKeys = 0 ;
				newRoot.pointers[0] = tmp;
				newRoot.pointers[1] = new BTreeNode();
			}
			
			
			////////////////////////////
			// Case 2: tree grows up //
			//////////////////////////
			// # of new nodes = # of levels + 1
			// 
			
			if (node == root) {
				// tree will grow upwards
			}
		
			
			//////////////////////////////////
			// Case 3: Tree grows sideways //
			////////////////////////////////
			// # of new nodes = 1
			//
				
			BTreeNode newSibling = new BTreeNode() ;
			newSibling.isLeaf = node.pointers[index].isLeaf ;

			//Set number of keys to half of full capacity
			int numKey = (node.numKeys)/2;
			//Copy keys from full node to newly created node
			for(int count = 0 ; count < numKey; count++) 
			{
				
				newSibling.keyObject[count] = node.pointers[index].keyObject[count + degree];
		
			}

			//Copy pointers from full node to newly created node
			if(!node.pointers[index].isLeaf)
			{
				for(int count = 0; count < numKey; count++)
				{
					newSibling.pointers[count] = node.pointers[index].pointers[count + degree] ;
				}
			}
			//Change count to reflect split node
			node.pointers[index].numKeys = degree -1;

			//Change isFull flag to false to reflect reduction in number of keys.
			node.pointers[index].isFull = false ;

			//Move pointers around so as to create space for new pointer pointing to newly created node.
			for(int count = node.numKeys ; count >= index +1;count -- )
			{
				node.pointers[count+1] = node.pointers[count] ;
			}
			//Create pointers to newly created node.
			node.pointers[index+1] = newSibling ;
			// Move keys around so as to make room for key coming from child node.
			for(int count = node.numKeys -1 ; count >= index ; count-- )
			{
				node.keyObject[count + 1] = node.keyObject[count] ;
			}

			//Move median key to parent. Node is parent in this case.
			node.keyObject[index] = node.pointers[index].keyObject[degree -1 ] ;
			//Increment parent count by 1
			node.numKeys = node.numKeys +1;
			// Check for isFull status
			if(node.numKeys == 2*degree -1)
			{
				node.isFull = true ;
			}

			temp_diskOffSet= diskWrite(node.pointers[index]) ;
			node.diskOffsets[index] = temp_diskOffSet ;

			temp_diskOffSet= diskWrite(newSibling) ;
			node.diskOffsets[index+1] = temp_diskOffSet ;

			if(node == root)
			{
				diskWrite(node);
			}

		}


		/**
		 * Method prints the content of a BTree Node
		 */
		public void printNode()
		{
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for(int i = 0; i < this.numKeys ; i++)
			{
				sb.append(this.keyObject[i].getKey());
				sb.append("|");

			}
			sb.append("]");

			System.out.println(sb.toString());
		}


		/**
		 * Inserts the keyObject into the given Node.  
		 * Method also makes sure that nodes are never full in the BTree. 
		 * @param node
		 * @param keyObject
		 * @throws IOException 
		 */
		public void insertKeyObject(BTreeNode node, TreeObject keyObject) throws IOException
		{			
			BTreeNode parent = node;
			int index = 0;
			
			while (!node.isLeaf) {	
				for(int i = 0; i <= node.numKeys; i++) {	// XXX: may be off by one
					if(!isKeyDuplicate(node,keyObject,i)) {	// XXX: Does this handle disk writing ?
						if (keyObject.compareTo(node.keyObject[i]) <=0) {
							parent = node;
							node = diskRead(node.diskOffsets[i]);
							index = i;
						}
					}
				}
			}
			
			// Now node points to the appropriate leaf for insertion
			
			if (node.isFull()) {
				//node = null;
				nodeSplit(parent, index);
			} 
			
			if (parent == null) {
				System.err.println("Parent is null");
			}
			
			for(int i = 0; i <= parent.numKeys; i++) {	// XXX: may be off by one
				if (keyObject.compareTo(parent.keyObject[i]) < 0) {
					node = diskRead(parent.diskOffsets[i]);
					insertObject(node, keyObject);
					break;
				}
			}
		
			diskWrite(node);
		
		}
		

		/**
		 * Prints node content and associated Frequency
		 */
		public void printNode_freq()
		{
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for(int i = 0; i < this.numKeys ; i++)
			{
				sb.append(this.keyObject[i].getKey());
				sb.append("(");
				sb.append(this.keyObject[i].getFrequency());
				sb.append(")");
				sb.append("|");

			}
			sb.append("]");

			System.out.println(sb.toString());
		}


		/**
		 * Checks to see if the key to be inserted is a duplicate
		 * @param node is the destination in which the keyObject is to be added
		 * @param keyObject is the object to be added into the node.
		 * @return a boolean indicating if the keyObject is a duplicate. TRUE = Duplicate 
		 * FALSE =  not duplicate 
		 */
		private boolean isKeyDuplicate(BTreeNode node, TreeObject keyObject, int i)
		{
			boolean keyIsDuplicate = false ;

			if( i == -1)
			{
				return keyIsDuplicate ;
			}

			//Check for duplicates. If key is duplicate, inc Frequency.
			int j =0 ;
			while(j < node.numKeys && (!keyIsDuplicate))
			{
				if (keyObject.compareTo(node.keyObject[j] )== 0)
				{
					node.keyObject[j].incFrequency();
					keyIsDuplicate = true ;
				}
				j++ ;
			}
			return keyIsDuplicate ;
		}


		/**
		 *  This is a test method used for debug purposes.
		 * @param node
		 * @param keyObject
		 * @param i
		 */
		public void insertObject(BTreeNode node, TreeObject keyObject)
		{
			numKeys++;
			int i = node.numKeys;
			
			while (i > 0) {		
				int result = keyObject.compareTo(node.keyObject[i]);
				
				if (result >= 0) { 		// if addThis is greater than or equal to object at i
					if (result != 0) {	// if addThis is not equal to object at i
	
						int stop = i;	// store this position in stop
						i = numKeys;	// i is now pointing at first empty index
						
						while(i > stop) {	// while the object behind i is not the object we just added
							node.keyObject[i] = node.keyObject[i - 1]; // shift objects forward
							i--; // walk backwards
						}
					
						node.keyObject[i] = keyObject;
						return;

					} else {	
						node.keyObject[i].incFrequency();
						return;
					}
				} 
				i--;
			}	
		}

		
		/**
		 * Removes the object at the given index.
		 * @param index The given index.
		 * @return removed The removed object.
		 * @return null If the given index was out of range.
		 */
		private TreeObject removeObject(int index) {
			if (index < 0 || index >= numKeys) {
				return null;
			}

			TreeObject removed = keyObject[index]; // store object to remove 

			int i = index;

			while (keyObject[i + 1] != null) {
				keyObject[i] = keyObject[i + 1];
				i++;
			}

			numKeys--;
			return removed;
		}

		
		
		/**
		 * Returns whether or not this node is full.
		 * @return
		 */
		private boolean isFull() {
			return (numKeys >= keyObject.length);
		}
		
		
//		private boolean isLeaf() {
//			if (numPointers == 0) {
//				return true;
//			}
//			
//			return false;
//		}
		
		private boolean willBeFull() {
			return (numKeys +1 >= keyObject.length);
		}

	}
}
