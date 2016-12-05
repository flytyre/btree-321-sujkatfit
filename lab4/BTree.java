import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * BTree Class.
 * 
 * @author Sujeet Ayyapureddi, Margiawan Fitriani, Kathryn Silva
 *
 */
public class BTree {

	private int degree;
	private int diskIndex = 0 ;
	private RandomAccessFile raf;
	private BTreeNode root;
	private int rootOffset;

	/**
	 * Constructor.
	 * @param f The file in which to store the BTree.
	 * @param t The degree of the BTree.
	 * @throws IOException If the file is not found.
	 */
	public BTree(File f, int t) throws IOException {
		
		if (t == 0) {
			degree = getDefaultDegree(); 
		} else {
			degree = t;
		}
		root = new BTreeNode();
		root.index = 0;
		root.hasIndex = true;
		raf = new RandomAccessFile(f, "rw");
		root.writeDegree(t);
		root.writeRootOffset(2 * Integer.BYTES);
		rootOffset = 2 * Integer.BYTES;
	}

	/**
	 * Constructor. 
	 * Generates degree.
	 * @param f The file from which to load the BTree.
	 * @throws IOException If the file is not found.
	 */
	public BTree(File f) throws IOException {
		
		raf = new RandomAccessFile(f, "rw");
		degree = getDegree();  
		rootOffset = getRootOffset();
		root = new BTreeNode();
		root.index = 0;
		root.hasIndex = true;
		root = diskReadNode(0);
	}
	
	/**
	 * Reads the BTreeNode from the disk at the given offset.
	 * @param offset
	 * @return BTreeNode The BTreeNode at the given offset.
	 * @throws IOException
	 */
	private BTreeNode diskReadNode(long offset) throws IOException {
		
		raf.seek(rootOffset + offset * getNodeSize());

		BTreeNode node = new BTreeNode();

		// Reading the degree from the file.
		node.nodeDegree = raf.readInt();
		
		// Reading the KeyObject
		for(int i = 0; i < 2 * degree - 1; i++) {
			node.keyObjects[i] = new KeyObject(raf.readLong());     // Key of the tree Object.
			node.keyObjects[i].setFrequency(raf.readInt());         // Frequency of the tree Object.
		}

		// Reading the pointers
		for(int i = 0; i < 2* degree; i++) {
			node.diskOffsets[i] = raf.readLong();      // Pointer to the file on disk
		}

		node.isLeaf = raf.readBoolean();         // Boolean Value of isLeaf
		node.isFull = raf.readBoolean();         // Boolean Value of isFull
		node.numKeys = raf.readInt();            // int Value of numKeys

		node.hasIndex = raf.readBoolean();       // Boolean Value of hasIndex
		node.index = raf.readInt();              // int Value of index

		return node;
	}

	/**
	 * 
	 * @param f
	 * @throws IOException
	 */
	public void inorderTreeTraversal(File f) throws IOException
	{
		FileWriter fstream = new FileWriter(f);
		BufferedWriter out = new BufferedWriter(fstream);
		
		Stack<Pair> treeStack = new Stack<Pair> ();

		treeStack.push(new Pair(0, 0));

		while (!treeStack.isEmpty())
		{
			Pair pair = treeStack.pop();
			BTreeNode currNode = diskReadNode(pair.getIndex());
			int keyPosition = pair.getKeyPosition();


			if (currNode.isLeaf)
			{
				//System.out.println(currNode.keyObjects[keyPosition]);
				for (int i =0; i < currNode.numKeys; i++)
				{
					out.write(currNode.keyObjects[i].getFrequency() + "  " + currNode.keyObjects[i].getKey());
					out.newLine();
					//System.out.println(currNode.keyObjects[i].getFrequency() + "  " + currNode.keyObjects[i].getKey() );
				}


				continue;
			}
			else if (keyPosition > 0)
			{
				out.write(currNode.keyObjects[keyPosition - 1].getFrequency() + "  " + currNode.keyObjects[keyPosition - 1].getKey());
				out.newLine();
				//System.out.println(currNode.keyObjects[keyPosition - 1].getFrequency() + "  " + currNode.keyObjects[keyPosition - 1].getKey() );
			}

			if (keyPosition < currNode.numKeys)
				treeStack.push(new Pair(pair.getIndex(), keyPosition+1));

			BTreeNode childNode = diskReadNode(currNode.diskOffsets[keyPosition]);

			treeStack.push(new Pair(childNode.index, 0));
		}
		//out.flush();
		out.close();
	}
	
	/**
	 * 
	 *
	 */
	private class Pair
	{
		private int index;
		private int keyPosition;

		private Pair(int index, int keyPosition)
		{
			this.index = index;
			this.keyPosition = keyPosition;
		}

		int getIndex()
		{
			return index;
		}

		int getKeyPosition()
		{
			return keyPosition;
		}
	}
	
	/**
	 * 
	 * @param node to be persisted/written to disk
	 * @return index value. If the node is a new node and does not have an index, then a new
	 * index is generated.
	 * @throws IOException
	 */
	private int diskWriteNode(BTreeNode node) throws IOException {

		if(!node.hasIndex) {
			node.index = incDiskIndex();
			node.hasIndex = true;
		}

		raf.seek(rootOffset+ node.index * getNodeSize());

		raf.writeInt(degree);
		
		// Writing the KeyObject
		for (int i = 0; i < 2 * degree - 1 ; i++) {
			raf.writeLong(node.keyObjects[i].getKey());      // Key of the tree Object
			raf.writeInt(node.keyObjects[i].getFrequency()); // Frequency of the tree Object
		}

		// Writing the pointers
		for (int i = 0; i < 2 * degree; i++) {
			raf.writeLong(node.diskOffsets[i]);   // Pointer to the file on disk
		}

		raf.writeBoolean(node.isLeaf);        // Boolean Value of isLeaf
		raf.writeBoolean(node.isFull);        // Boolean Value of isFull
		raf.writeInt(node.numKeys);           // int Value of numKeys
		raf.writeBoolean(node.hasIndex);      // Boolean Value of hasIndex
		raf.writeInt(node.index);             // int Value of index

		return node.index;
	}
	
	/**
	 * Increments disk index.
	 * @return diskIndex
	 */
	public int incDiskIndex() {
		diskIndex ++;
		return diskIndex;
	}

	/**
	 * Method calculates the size of the node in Bytes.
	 * @return size The size of the node in Bytes.
	 */
	public long getNodeSize() {
		
		int degreesize = Integer.BYTES ;
		int keyobjectsize = Long.BYTES + Integer.BYTES ;
		int diskoffsetsize = Long.BYTES ;
		int isLeafsize = 1 ;
		int isFullsize = 1 ;
		int numKeyssize = Integer.BYTES ;
		int hasIndexSize = 1 ;
		int indexSize = Integer.BYTES ;

		int numpointers = 2 * degree ;
		int numkeys = 2 * degree - 1 ;

		int size = degreesize + keyobjectsize * numkeys + diskoffsetsize * numpointers + isLeafsize + isFullsize + numKeyssize + hasIndexSize + indexSize ; 

		return size ; 
	}

	/**
	 * Generates the optimal degree for a BTree based on disk block size of 4096 bytes.
	 * @return degree The calulated optimal degree.
	 */
	public int getDefaultDegree() {
		
		int degreesize = Integer.BYTES ;
		int keyobjectsize = Long.BYTES + Integer.BYTES ;
		int diskoffsetsize = Long.BYTES ;
		int isLeafsize = 1 ;
		int isFullsize = 1 ;
		int numKeyssize = Integer.BYTES ;
		int hasIndexSize = 1 ;
		int indexSize = Integer.BYTES ;

		int degree = (int) Math.floor((4096 - (degreesize + isLeafsize + isFullsize + numKeyssize + hasIndexSize + indexSize + keyobjectsize)) / (2 * (diskoffsetsize + keyobjectsize))) ;
		
		return degree ;
	}
	
	/**
	 * Returns the degree stored on the disk.
	 * @return
	 * @throws IOException
	 */
	public int getDegree() throws IOException {
		raf.seek(0);
		return raf.readInt();
	}
	
	/**
	 * Returns the root offset stored on the disk.
	 * @return
	 * @throws IOException
	 */
	public int getRootOffset() throws IOException {
		return raf.readInt();
	}
	
	/**
	 * Prints all nodes of the Btree level by level (BFS).
	 */
	public String printBTree() {
		
		Queue<BTreeNode> BTQueue = new LinkedList<BTreeNode>();
		BTQueue.add(root);
		
		String s = "";
		
		while(!BTQueue.isEmpty()) {
			
			BTreeNode node = BTQueue.remove();
			s += node.printNode();
			s += "\n";
			
			if(!node.isLeaf) {
				for (int i = 0; i <= node.numKeys; i++) {
					try {
						BTQueue.add(diskReadNode(node.diskOffsets[i]));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		return s;
	}

	/**
	 * Prints all nodes with their frequencies.
	 */
	public void printBTreeFreq() throws IOException {
		
		Queue<BTreeNode> BTQueue = new LinkedList<BTreeNode>();
		BTQueue.add(root);
		
		while(!BTQueue.isEmpty()) {
			
			BTreeNode node = BTQueue.remove();
			node.printNodeFreq();
			
			if(!node.isLeaf) {
				for(int i=0; i <= node.numKeys; i++) {
					BTQueue.add(diskReadNode(node.diskOffsets[i]));
				}
			}
		}
	}

	/** 
	 * Searches for a given key in the BTree.
	 * @param key
	 * @return SearchObject An object containing the node and index the given key is located at.
	 * @return null If the key could not be found.
	 * @throws IOException
	 */
	public int search(long key) throws IOException {

		BTreeNode node = root; 

		while (!node.isLeaf && node.keyObjects[0].getKey() != -1) {	// can't figure out how to simplify this

			// if key is larger than the largest key in this node,
			// skip to this node's rightmost child
			if (key > node.keyObjects[node.numKeys - 1].getKey()) {
				node = diskReadNode(node.diskOffsets[node.numKeys]);
				
			} else {

				// iterate through keys in this node, if key is 
				// smaller than the key at an index in the node, 
				// node becomes the child at the corresponding index
				for(int i = 0; i < node.numKeys; i++) {	// XXX: TRYING TO FIX IOOB
					if (key < node.keyObjects[i].getKey()) {
						node = diskReadNode(node.diskOffsets[i]);
						i = node.numKeys; // break out of loop
					}
					
					if (i == node.numKeys) {
						i--; // please fix it ahhh
					}
					
					if (key == node.keyObjects[i].getKey()) {	// XXX: INDEX OUT OF BOUNDS HERE
						return (node.keyObjects[i].getFrequency()) + 1;
						//System.out.println(key + " (" + freq + ")");
						//return;
					}
				}
			}
		}
		
		// now we have to process final node key might be in
		// look for key in this node if in range
		if (key <= node.keyObjects[node.numKeys - 1].getKey() && key >= node.keyObjects[0].getKey()) {
			
			for(int i = 0; i < node.numKeys; i++) {	
				if (key == node.keyObjects[i].getKey()) {
					return (node.keyObjects[i].getFrequency()) + 1;
					//System.out.println(key + " (" + freq + ")");
					//return;
				}
			}
		}
		
		// if method didn't return before now, search result was a miss
		//System.out.println(key + ": (0)");
		return 0;
	}
	
	/**
	 * Inserts keyObject into the BTree.
	 * @param keyObject given keyObject
	 * @throws IOException 
	 */
	public void treeInsertKey(KeyObject keyObject) throws IOException {
		
		BTreeNode rt = root;

		if(rt.isFull) {  // Does a split. Creates a new root
			
			BTreeNode newRoot = new BTreeNode();
			root = newRoot ;
			newRoot.isLeaf= false;
			newRoot.numKeys = 0 ;
			newRoot.index = 0;
			newRoot.hasIndex = true ;

			rt.hasIndex = false ;

			newRoot.diskOffsets[0] = diskWriteNode(rt) ;
			newRoot.nodeSplit(0);
			nodeInsertKey(newRoot,keyObject);
		}
		
		else {
			nodeInsertKey(root,keyObject);
		}
	}
	
	/**
	 * Inserts the keyObject into the given Node.  
	 * Method also makes sure that nodes are never full in the BTree. 
	 * @param node
	 * @param keyObject
	 * @throws IOException 
	 */
	public void nodeInsertKey(BTreeNode node, KeyObject keyObject) throws IOException {

		while(!node.isLeaf) {	// While node is not leaf.

			int i = node.numKeys - 1;
			
			if(!node.isDuplicateKey(keyObject, i)) {	// If key is a duplicate don't do any of the following.

				while(i >= 0 && (keyObject.compareTo(node.keyObjects[i]) == -1)) {
					i = i - 1;
				}

				i = i + 1;			// Go to the next child

				BTreeNode child = new BTreeNode();
				child  = diskReadNode(node.diskOffsets[i]);

				if(child.isFull) {

					node.nodeSplit(i);

					if(keyObject.compareTo(node.keyObjects[i] )== 1) {		
						i = i + 1 ;		// Change of index causes node to be written back to disk.
					}
				}

				node = diskReadNode(node.diskOffsets[i]);
			} else {
				break;
			}
		}

		if(node.isLeaf) {

			int i = node.numKeys - 1;

			if(!node.isDuplicateKey(keyObject,i)) { // If key is a duplicate don't do any of the following.

				while(i >= 0 && (keyObject.compareTo(node.keyObjects[i]) == -1)) {
					node.keyObjects[i + 1] = node.keyObjects[i];
					i = i - 1;
				}

				node.keyObjects[i + 1] = keyObject;
				node.keyObjects[i + 1].incFrequency();
				node.numKeys = node.numKeys + 1;

				if(node.numKeys == 2 * degree - 1) {
					node.isFull = true ;
				}

			} 
			diskWriteNode(node);
		}
	}

	
	/**
	 * BTree node class.
	 * Inner class.
	 * @author Sujeet Ayyapureddi, Margiawan Fitriani, Kathryn Silva
	 *
	 */
	private class BTreeNode {

		private KeyObject[] keyObjects;
		private long[] diskOffsets;
		private boolean hasIndex;
		private boolean isLeaf;
		private boolean isFull;
		private int index;
		private int numKeys; 
		private int nodeDegree;	// Not used, but necessary for creating BTree from file

		/**
		 * Constructor.
		 */
		private BTreeNode() {
			
			nodeDegree = degree;
			isLeaf = true;
			keyObjects = new KeyObject[2 * degree - 1];
			diskOffsets = new long[2 * degree] ;
			numKeys = 0;
			isFull = false; 

			for(int i = 0; i < 2 * degree; i ++) {
				diskOffsets[i] = 0;
			}

			for(int j = 0; j < 2 * degree - 1; j++) {
				keyObjects[j] = new KeyObject();  
			}

			index = -1;
			hasIndex = false;	
		}
		
		/**
		 * Writes this node's degree to the disk.
		 * @param degree
		 * @throws IOException
		 */
		public void writeDegree(int degree) throws IOException {
			raf.seek(0);
			raf.writeInt(degree);
		}

		/**
		 * 
		 * @param rootOffset
		 * @throws IOException
		 */
		public void writeRootOffset(int rootOffset) throws IOException {
			raf.seek(Integer.BYTES);
			raf.writeInt(rootOffset);
		}
			
		/**
		 * Checks to see if the key to be inserted is a duplicate.
		 * @param node The node in which the key is being added.
		 * @param key The object to be added into the node.
		 * @return true If the key is a duplicate.
		 * @return false If key is not a duplicate.
		 */
		private boolean isDuplicateKey(KeyObject key, int i) {
			
			boolean keyIsDuplicate = false;

			if(i == -1) {
				return keyIsDuplicate;
			}

			// Check for duplicates. If key is duplicate, inc Frequency.
			int j = 0 ;
			while(j < numKeys && (!keyIsDuplicate)) {
				
				if (key.compareTo(keyObjects[j]) == 0) {
					keyObjects[j].incFrequency();
					keyIsDuplicate = true;
				}
				
				j++;
			}
			
			return keyIsDuplicate;
		}

		/**
		 * This method splits the node given by the ith child of the given node
		 * @param node Parent of the node to be split.
		 * @param i is the index of the child of the node which is to be split.
		 * @throws IOException 
		 */
		public void nodeSplit(int i) throws IOException {
			
			int index = i;

			BTreeNode originalNode = diskReadNode(diskOffsets[index]);

			BTreeNode newNode = new BTreeNode();
			newNode.isLeaf = originalNode.isLeaf;

			//Set number of keys to half of full capacity
			newNode.numKeys = degree - 1;

			//Copy keys from full node to newly created node
			for(int count = 0; count < degree-1; count++)
			{
				newNode.keyObjects[count] = originalNode.keyObjects[count + degree];
			}

			//Copy pointers from full node to newly created node
			if(!originalNode.isLeaf)
			{
				for(int count = 0; count < degree; count++)
				{
					newNode.diskOffsets[count] = originalNode.diskOffsets[count + degree];
				}
			}
			
			//Change count to reflect split node
			originalNode.numKeys = degree - 1;

			//Change isFull() flag to false to reflect reduction in number of keys.
			originalNode.isFull = false;

			//Move pointers around so as to create space for new pointer pointing to newly created node.
			for(int count = numKeys; count >= index + 1; count --)
			{
				diskOffsets[count + 1] = diskOffsets[count];
			}

			//Create pointers to newly created node.Copy diskoffset into the parent
			diskOffsets[index+1] = diskWriteNode(newNode);

			// Move keys around so as to make room for key coming from child node.
			for(int count = numKeys - 1; count >= index; count--)
			{
				keyObjects[count + 1] = keyObjects[count];
			}

			//Move median key to parent. Node is parent in this case.
			keyObjects[index] = originalNode.keyObjects[degree - 1];

			//Increment parent count by 1
			numKeys = numKeys + 1;

			//Check for isFull status
			if(numKeys == 2 * degree - 1)
			{
				isFull = true;
			}

			//Writing children nodes to disk and storing the disk offset in the parent.
			diskOffsets[index] = diskWriteNode(originalNode);

			diskWriteNode(this);
		}

		/**
		 * Method prints the content of a BTree Node
		 * TODO: May change to return string instead of printing.
		 */
		public String printNode() {
			
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for(int i = 0; i < this.numKeys ; i++)
			{
				sb.append(this.keyObjects[i].getKey());
				// Skip the last "|"
				if (i != numKeys - 1) { 
				sb.append("|");
				}
			}
			sb.append("]");

			return sb.toString();
		}

		/**
		 * Prints node content and associated Frequency.
		 */
		public String printNodeFreq() {
			
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			
			for(int i = 0; i < this.numKeys ; i++) {
				sb.append(this.keyObjects[i].getKey());
				sb.append("(");
				sb.append(this.keyObjects[i].getFrequency());
				sb.append(")");
				// Skip the last "|"
				if (i != numKeys - 1) {	
					sb.append("|");
				}
			}
			sb.append("]");

			return sb.toString();
		}
	}
}
