import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * @author user1
 *
 */
public class BTree implements Serializable
{

	private static final long serialVersionUID = 1L;
	public int degree ;
	private int diskIndex = 0 ;
	File f ;
	static RandomAccessFile raf ;

	static BTreeNode root ;


	// Constructor
	public BTree(int t, File f)
	{

		this.degree =t ;
		root = new BTreeNode(degree) ;
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
	 */
	public void treeInsertKey(TreeObject keyObject)
	{
		BTreeNode rt = this.root ;

		if(rt.isFull())  // Does a split. Creates a new root
		{

			BTreeNode newRoot = new BTreeNode(degree) ;
			this.root = newRoot ;

			newRoot.numKeys = 0 ;
			newRoot.pointers[0] = rt ;
			BTreeNode.nodeSplit(newRoot,0);
			newRoot.printNode();
			newRoot.pointers[0].printNode();
			newRoot.pointers[1].printNode();

			BTreeNode.insertKeyObject(this.root,keyObject);

		}
		else
		{
			BTreeNode.insertKeyObject(rt,keyObject);
		}
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
			if(!node.isLeaf())
			{
				for(int i=0; i <= node.numKeys; i++)
				{
					BTQueue.add(node.pointers[i]);
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
			if(!node.isLeaf())
			{
				for(int i=0; i <= node.numKeys; i++)
				{
					BTQueue.add(node.pointers[i]);
				}
			}
		}
	}

	public  int genDiskIndex()
	{
		diskIndex ++ ;
		return diskIndex ;
	}


	// Inner Class
	public static class BTreeNode implements Serializable
	{

		private static final long serialVersionUID = 1L;
		//public TreeObject[] treeObjects;
		public TreeObject[] keyObject;
		public BTreeNode[] pointers; // pointers to the children nodes
		public int[] diskOffsets ;
		public int numKeys; // number of elements in the Node
		private static int degree; //degree of the BTree
		boolean hasIndex ;
		int index ;

		// Constructor.
		public  BTreeNode(int t)
		{
			degree = t;
			keyObject = new TreeObject[2*degree-1];
			pointers = new BTreeNode[2*degree];
			diskOffsets = new int[2*degree] ;
			numKeys=0;
			//treeObjects = new TreeObject[2*degree-1];

			for(int i = 0 ; i < 2*degree ; i ++)
			{
				diskOffsets[i] = 0;
			}


			for(int j = 0 ; j < 2*degree -1 ; j++)
			{
				//keyObject[j].getKey();  // Cant figure out null pointer issue here.
			}

			index = -1;
			hasIndex = false ;



		}




		/**
		 * This method splits the node given by the ith child of the given node
		 * @param node Parent of the node to be split.
		 * @param i is the index of the child of the node which is to be split.
		 */
		public static void nodeSplit(BTreeNode node, int i)
		{
			int index = i ;
			int temp_diskOffSet ;

			BTreeNode newNode = new BTreeNode(degree) ;

			//Set number of keys to half of full capacity
			newNode.numKeys = degree -1 ;
			//Copy keys from full node to newly created node
			for(int count = 0 ; count < degree-1; count++)
			{
				newNode.keyObject[count] = node.pointers[index].keyObject[count + degree];
			}

			//Copy pointers from full node to newly created node
			if(!node.pointers[index].isLeaf())
			{
				for(int count = 0; count < degree; count++)
				{
					newNode.pointers[count] = node.pointers[index].pointers[count + degree] ;
				}
			}
			//Change count to reflect split node
			node.pointers[index].numKeys = degree -1;

			//Change isFull() flag to false to reflect reduction in number of keys.


			//Move pointers around so as to create space for new pointer pointing to newly created node.
			for(int count = node.numKeys ; count >= index +1;count -- )
			{
				node.pointers[count+1] = node.pointers[count] ;
			}
			//Create pointers to newly created node.
			node.pointers[index+1] = newNode ;
			// Move keys around so as to make room for key coming from child node.
			for(int count = node.numKeys -1 ; count >= index ; count-- )
			{
				node.keyObject[count + 1] = node.keyObject[count] ;
			}

			//Move median key to parent. Node is parent in this case.
			node.keyObject[index] = node.pointers[index].keyObject[degree -1 ] ;
			//Increment parent count by 1
			node.numKeys = node.numKeys +1;
			// Check for isFull() status

			temp_diskOffSet= diskWrite(node.pointers[index], raf) ;
			node.diskOffsets[index] = temp_diskOffSet ;

			temp_diskOffSet= diskWrite(newNode, raf) ;
			node.diskOffsets[index+1] = temp_diskOffSet ;

			if(node == root)
			{
				diskWrite(node, raf);
			}

		}


		public static int diskWrite(BTreeNode node, RandomAccessFile raf)
		{

			ByteArrayOutputStream bytArrOutStrm = new ByteArrayOutputStream();
			ObjectOutputStream objOutStrm = null;
			try {
				objOutStrm = new ObjectOutputStream(bytArrOutStrm);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

			try 
			{
				objOutStrm.writeObject(node);
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] array = bytArrOutStrm.toByteArray() ;

			try 
			{
				raf.write(array);
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(node.hasIndex)
			{
				return node.index ;
			}
			else
			{
				node.hasIndex = true ;
				return 0 ;//genDiskIndex();
			}



		}

		public static void diskRead(BTreeNode node)
		{

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
		 */
		public static void insertKeyObject(BTreeNode node, TreeObject keyObject)
		{
			//BTreeNode internal_node = node ;
			int i = node.numKeys -1;

			while(!node.isLeaf())
			{
				i = node.numKeys -1;

				if(!isKeyDuplicate(node,keyObject,i)) // If key is a duplicate don't do any of the following.
				{
					//while(i >= 0 && key < node.key[i])
					while(i >= 0 && (keyObject.compareTo(node.keyObject[i] )== -1))
					{
						i = i -1 ;
					}
					i = i+1 ;

					//node.printNode();
					if(node.pointers[i].isFull())
					{
						nodeSplit(node, i);
						//if(key > node.key[i])
						if(keyObject.compareTo(node.keyObject[i] )== 1)
						{
							i = i+1 ;
						}
					}



					//diskWrite(node); 
					//diskRead(node);

					node = node.pointers[i];
					//diskRead(node);
				}
			}

			if(node.isLeaf())
			{
				i = node.numKeys -1;

				if(!isKeyDuplicate(node,keyObject,i)) // If key is a duplicate don't do any of the following.
				{
					System.out.println("Inside InsertKey, isLeaf() before while");

					//while(i >= 0 && key < node.key[i])
					while(i >= 0 && (keyObject.compareTo(node.keyObject[i] )== -1))
					{
						node.keyObject[i+1] = node.keyObject[i] ;
						i = i -1 ;
					}
					System.out.println("Inside InsertKey, isLeaf() after while");
					node.keyObject[i+1] = keyObject ;
					node.keyObject[i+1].incFrequency();
					node.numKeys = node.numKeys +1;

					//diskWrite(node);
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
		private static boolean isKeyDuplicate(BTreeNode node, TreeObject keyObject, int i)
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
		public static void insertObject(BTreeNode node, TreeObject keyObject, int i)
		{
			node.keyObject[i] = keyObject ;
		}

		
		/**
		 * Returns whether or not this node is full.
		 * @return
		 */
		private boolean isFull() {
			return (numKeys < keyObject.length);
		}

		private boolean isLeaf() {
			if (pointers[0] == null) {
				return true;
			}
			return false;
		}
		
		
		/**
		 * Returns whether or not this node is full.
		 * @return
		 */
		private boolean isEmpty() {
			return (numKeys == 0);
		}
		
	}
}
