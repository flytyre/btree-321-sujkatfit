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
public class BTree 
{

	public   int degree ;
	int diskIndex = 0 ;
	File f ;
	RandomAccessFile raf ;

	BTreeNode root ;

	long nodesize ;


	// Constructor
	public BTree(int t, File f)
	{
		degree = t ;
		root = new BTreeNode(degree) ;
		root.index = 0;
		root.hasIndex = true ;

		//f = new File("Test.dat") ;
		try {
			raf = new RandomAccessFile(f,"rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/** 
	 * Searches for a given key in the BTree.
	 * @param key
	 * @return SearchObject An object containing the node and index the given key is located at.
	 * @return null If the key could not be found.
	 * @throws IOException
	 */
	public SearchObject search(long key) throws IOException {

		/////////////////////////////////
		// I'll try to simplify this, //
		// but it works for now.	 //
		//////////////////////////////
		
		BTreeNode node = root; 

		while (!node.isLeaf && node.keyObject[0].getKey() != -1) {	// can't figure out how to simplify this

			// if key is larger than the largest key in this node,
			// skip to this node's rightmost child
			if (key > node.keyObject[node.numKeys-1].getKey()) {
				node = diskRead(node.diskOffsets[node.numKeys-1]);
				
			} else {

				// iterate through keys in this node, if key is 
				// smaller than the key at an index in the node, 
				// node becomes the child at the corresponding index
				for(int i = 0; i < node.numKeys; i++) {	
					if (key < node.keyObject[i].getKey()) {
						node = diskRead(node.diskOffsets[i]);
					}

					if (key == node.keyObject[i].getKey()) {
						return new SearchObject(node, i);
					}
				}
			}
		}
		
		// now we have to process final node key might be in
		// first check range, return null if out of range
		// look for key in this node if in range
		if (key <= node.keyObject[node.numKeys].getKey() && key >= node.keyObject[0].getKey()) {
			
			for(int i = 0; i < node.numKeys; i++) {	
				if (key == node.keyObject[i].getKey()) {
					return new SearchObject(node, i);
				}
			}
		}

		return null;
	}

	/**
	 * Inserts keyObject into the BTree.
	 * @param keyObject given keyObject
	 * @throws IOException 
	 */
	public void treeInsertKey(TreeObject keyObject) throws IOException
	{
		BTreeNode rt = root ;

		if(rt.isFull)  // Does a split. Creates a new root
		{
			//System.out.println("root index before =:" + BTree.root.index);
			BTreeNode newRoot = new BTreeNode(degree) ;
			root = newRoot ;
			newRoot.isLeaf= false;
			newRoot.numKeys = 0 ;
			newRoot.index = 0;
			newRoot.hasIndex = true ;

			rt.hasIndex = false ;
			//System.out.println("root index after =:" + BTree.root.index);


			newRoot.diskOffsets[0] = diskWrite(rt) ;

			//System.out.println("diskoffset =:" + BTree.root.diskOffsets[0]);


			newRoot.nodeSplit(newRoot,0);
			//System.out.println("Inside rt is full, about to print node");
			//newRoot.printNode();

			newRoot.insertKeyObject(newRoot,keyObject);

		}
		else
		{
			root.insertKeyObject(root,keyObject);
		}
	}

	/**
	 * 
	 * @param node to be persisted/written to disk
	 * @return index value. If the node is a new node and does not have an index, then a new
	 * index is generated.
	 * @throws IOException
	 */
	int diskWrite(BTreeNode node) throws IOException 
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
			raf.writeLong(node.keyObject[i].getKey());      // Key of the tree Object
			raf.writeInt(node.keyObject[i].getFrequency()); // Frequency of the tree Object
		}

		// Writing the pointers
		for(int i = 0 ; i < 2*(degree) ; i++)
		{
			raf.writeLong(node.diskOffsets[i]);      // Pointer to the file on disk

		}

		raf.writeBoolean(node.isLeaf);         // Boolean Value of isLeaf
		raf.writeBoolean(node.isFull);         // Boolean Value of isFull
		raf.writeInt(node.numKeys) ;           // int Value of numKeys

		raf.writeBoolean(node.hasIndex);       // Boolean Value of hasIndex
		raf.writeInt(node.index) ;             // int Value of index

		return node.index ;

	}


	private   BTreeNode diskRead(long offset) throws IOException
	{
		//System.out.println("offset:" + offset);

		raf.seek(offset * getNodeSize()) ;

		BTreeNode node = new BTreeNode(degree) ;

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

		node.isLeaf = raf.readBoolean();         // Boolean Value of isLeaf
		node.isFull = raf.readBoolean();         // Boolean Value of isFull
		node.numKeys = raf.readInt() ;           // int Value of numKeys

		node.hasIndex = raf.readBoolean();       // Boolean Value of hasIndex
		node.index = raf.readInt() ;             // int Value of index

		return node;
	}


	/**
	 * Prints all nodes of the Btree level by level(BSF).
	 */
	public void printBTree()
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
					try {
						BTQueue.add(diskRead(node.diskOffsets[i]));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//diskRead(node.diskOffsets[i]);
				}
			}
		}
	}



	public void printBTree_Freq() throws IOException
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
					BTQueue.add(diskRead(node.diskOffsets[i]));

				}
			}
		}
	}

	public    int genDiskIndex()
	{
		diskIndex ++ ;
		return diskIndex ;
	}


	/**
	 * Method calculates the size of the node in Bytes
	 * @return nodesize in Bytes
	 */
	public   long getNodeSize()
	{
		int keyobjectsize = Long.BYTES + Integer.BYTES ;
		//int pointersize = Long.BYTES ;
		int diskoffsetsize = Long.BYTES ;
		int isLeafsize = 1 ;
		int isFullsize = 1 ;
		int numKeyssize = Integer.BYTES ;
		int hasIndexSize = 1 ;
		int indexSize = Integer.BYTES ;

		int numpointers = 2*degree ;
		int numkeys = 2*degree -1 ;

		int size = keyobjectsize*numkeys + /*pointersize*numpointers +*/ diskoffsetsize *numpointers +isLeafsize +isFullsize + numKeyssize + hasIndexSize +indexSize ; 

		return size ; 

	}

	public long getDefaultDegree()
	{
		int keyobjectsize = Long.BYTES + Integer.BYTES ;
		//int pointersize = Long.BYTES ;
		int diskoffsetsize = Long.BYTES ;
		int isLeafsize = 1 ;
		int isFullsize = 1 ;
		int numKeyssize = Integer.BYTES ;
		int hasIndexSize = 1 ;
		int indexSize = Integer.BYTES ;

		int numpointers = 2*degree ;
		int numkeys = 2*degree -1 ;

		return (long) Math.floor((4096 - (isLeafsize +isFullsize + numKeyssize + hasIndexSize +indexSize + keyobjectsize))/(2*(diskoffsetsize + keyobjectsize))) ;
	}


	// Inner Class
	public   class BTreeNode 
	{

		//public TreeObject[] treeObjects;
		public TreeObject[] keyObject;
		//public BTreeNode[] pointers; // pointers to the children nodes
		public long[] diskOffsets ;
		boolean isLeaf;
		boolean isFull ;
		public int numKeys; // number of elements in the Node
		private   int degree; //degree of the BTree
		boolean hasIndex ;
		int index ;

		// Constructor.
		public  BTreeNode(int t)
		{
			degree = t;
			isLeaf = true;
			keyObject = new TreeObject[2*degree-1];
			diskOffsets = new long[2*degree] ;
			numKeys=0;
			isFull = false; 

			for(int i = 0 ; i < 2*degree ; i ++)
			{
				diskOffsets[i] = 0;
			}

			for(int j = 0 ; j < 2*degree -1 ; j++)
			{
				keyObject[j] = new TreeObject();  // Cant figure out null pointer issue here.
			}

			index = -1;
			hasIndex = false ;



		}




		/**
		 * This method splits the node given by the ith child of the given node
		 * @param node Parent of the node to be split.
		 * @param i is the index of the child of the node which is to be split.
		 * @throws IOException 
		 */

		public   void nodeSplit(BTreeNode node, int i) throws IOException
		{
			int index = i ;

			BTreeNode originalNode = diskRead(node.diskOffsets[index]);

			BTreeNode newNode = new BTreeNode(degree) ;
			newNode.isLeaf = originalNode.isLeaf ;

			//Set number of keys to half of full capacity
			newNode.numKeys = degree -1 ;


			//Copy keys from full node to newly created node
			for(int count = 0 ; count < degree-1; count++)
			{
				newNode.keyObject[count] = originalNode.keyObject[count + degree];
			}

			//Copy pointers from full node to newly created node
			if(!originalNode.isLeaf)
			{
				for(int count = 0; count < degree; count++)
				{
					newNode.diskOffsets[count] = originalNode.diskOffsets[count + degree] ;
				}
			}
			//Change count to reflect split node
			originalNode.numKeys = degree -1;

			//Change isFull() flag to false to reflect reduction in number of keys.
			originalNode.isFull = false ;

			//Move pointers around so as to create space for new pointer pointing to newly created node.
			for(int count = node.numKeys ; count >= index +1;count -- )
			{
				node.diskOffsets[count+1] = node.diskOffsets[count] ;
			}


			//Create pointers to newly created node.Copy diskoffset into the parent
			node.diskOffsets[index+1] = diskWrite(newNode) ;


			// Move keys around so as to make room for key coming from child node.
			for(int count = node.numKeys -1 ; count >= index ; count-- )
			{
				node.keyObject[count + 1] = node.keyObject[count] ;
			}

			//Move median key to parent. Node is parent in this case.
			node.keyObject[index] = originalNode.keyObject[degree -1 ] ;

			//Increment parent count by 1
			node.numKeys = node.numKeys +1;

			//Check for isFull status
			if(node.numKeys == 2*degree -1)
			{
				node.isFull = true ;
			}


			//Writing children nodes to disk and storing the disk offset in the parent.

			node.diskOffsets[index] = diskWrite(originalNode) ;


			if(!(node == root))
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
		public   void insertKeyObject(BTreeNode node, TreeObject keyObject) throws IOException
		{
			//BTreeNode internal_node = new BTreeNode(degree) ; // This is a temp node which holds the reconstructed child node read from disk.
			//int i = node.numKeys -1;

			while(!node.isLeaf) // while node is not leaf.
			{
				int i = node.numKeys -1;

				if(!isKeyDuplicate(node,keyObject,i)) // If key is a duplicate don't do any of the following.
				{
					//while(i >= 0 && key < node.key[i])
					while(i >= 0 && (keyObject.compareTo(node.keyObject[i] )== -1))
					{
						i = i -1 ;
					}
					i = i+1 ;  //Go to the next child
					//node.pointers[i] = diskRead(node.diskOffsets[i]);  //reading the child from disk

					BTreeNode child = new BTreeNode(degree);
					child  = diskRead(node.diskOffsets[i]);


					//node.printNode(); 
					if(child.isFull)
					{
						nodeSplit(node, i);
						//if(key > node.key[i])
						if(keyObject.compareTo(node.keyObject[i] )== 1)
						{
							//System.out.println("Inside node is not leaf,inside while 1 away from writedisk");
							//diskWrite(node.pointers[i]); //Write the old node to disk as the index i is changing in the next step

							i = i+1 ; // Change of index causes node to be written back to disk 
						}
					}

					//diskWrite(node); 
					//diskRead(node);
					//System.out.println("Inside node is leaf, 1 away from writedisk");



					//node = node.pointers[i];
					node = diskRead(node.diskOffsets[i]);

				}
			}

			if(node.isLeaf)
			{
				int i = node.numKeys -1;

				if(!isKeyDuplicate(node,keyObject,i)) // If key is a duplicate don't do any of the following.
				{


					//while(i >= 0 && key < node.key[i])
					while(i >= 0 && (keyObject.compareTo(node.keyObject[i] )== -1))
					{
						node.keyObject[i+1] = node.keyObject[i] ;
						i = i -1 ;
					}

					node.keyObject[i+1] = keyObject ;
					node.keyObject[i+1].incFrequency();
					node.numKeys = node.numKeys +1;
					if(node.numKeys == 2*degree -1)
					{
						node.isFull = true ;
					}

					diskWrite(node);
				}
			}
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
		private   boolean isKeyDuplicate(BTreeNode node, TreeObject keyObject, int i)
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
		public   void insertObject(BTreeNode node, TreeObject keyObject, int i)
		{
			node.keyObject[i] = keyObject ;
		}
	}

	public class SearchObject{

		BTreeNode node;
		int location;

		public SearchObject(BTreeNode foundNode, int index) {
			node = foundNode;
			location = index;
		}

		public int getLocation() {
			return location;
		}

		public BTreeNode getNode() {
			return node;
		}

	}
	
	
}
