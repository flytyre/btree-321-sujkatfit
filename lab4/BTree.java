
import java.util.Stack;

/**
 * [STATUS] Getting somewhere...
 * TODO: Implement checkBalance
 * TODO: Implement shift
 * TODO: Implement remove
 * 
 * 
 * BTree class.
 * Contains BTreeNode inner class.
 * 
 * @author
 * 
 */
public class BTree {

	private int t;
	private int uniques;
	private BTreeNode root;
	private Stack s;

	
	/**
	 * Constructor.
	 */
	public BTree(int degree) {
		this.t = degree;
		root = new BTreeNode();
		s = new Stack();
	}
	
	
	/**
	 * Not finished, check pseudocode for accuracy
	 * Search method.
	 * Recursive.
	 * No idea what it should return
	 */
	public BTreeNode search(BTreeNode node, long query) {
		int result = node.isInRange(query);
		
		if (result < 0) {
			search(node.children[0], query);
		}
		
		else if (result > 0) {
			search(node.children[node.numKeys], query);
		}
		
		else {
			for(int i = 0; i < node.numKeys; i++) {
				if (query < node.keys[i].getKey()) {
					search(node.children[i], query);
				}
			}
		}
		
		// TODO: Store and return what exactly??
		
		return null;
	}
	
	
	/**
	 * Inserts a value in the appropriate place in the tree.
	 * Recursive.
	 * @param node The node to attempt insertion at.
	 * @param key The key to insert in the tree.
	 */
	public void insert(BTreeNode node, TreeObject key) {
		
		if (node.isLeaf()) {
			if (node.isFull()) {
				split(node);
				insert(node, key);
			} else {
				node.insertKey(key);
			}
		}

		int result = node.isInRange(key.getKey());
		
		if (result < 0) {
			insert(node.children[0], key);
		}
		
		else if (result > 0) {
			insert(node.children[node.numKeys], key);
		}
		
		else {
			for(int i = 0; i < node.numKeys; i++) {
				if (key.compareTo(node.keys[i]) < 0) {
					insert(node.children[i], key);
				}
			}
		}
		
		// TODO: need to actually call insertion on node once location is found
		
		checkBalance();
	}
	
	
	/**
	 * Remove
	 * @param key
	 */
	public void remove(long key) {
		
		checkBalance();
	}
	
	
	/**
	 * Checks for and corrects imbalance.
	 */
	public void checkBalance() {
		
		// compare heights to ensure tree is even
		// correct balance if needed
		// shift up if room in parent
		// down if no room
		
	}
	
	
	/**
	 * 
	 * @param node
	 */
	public void shift(BTreeNode node) {
		if (node.parent.isFull()) {
			// shift down
		} else {
			// shift up
		}
	}


	/**
	 * Splits a node into a tree with three nodes and two leaves.
	 * The middle element becomes the new root.
	 * The leftmost elements are the left child.
	 * The rightmost elements are the right child.
	 * Not recursive.
	 * @param node The node to be split.
	 */
	public void split(BTreeNode node) {
		if (!node.isFull()) {
			System.err.println("Attempted to split a non-full node -- this shouldn't happen!  Check insert in BTree.");
			return;
		}
		
		if (!node.isLeaf()) {
			System.err.println("Attempted to split a non-leaf node -- this shouldn't happen!  Check insert in BTree.");
			return;
		}
		
		int middle = (int) Math.floor((double) node.numKeys / 2);
		int numKeys = node.numKeys;
		node.children[0] = new BTreeNode();
		node.children[0].parent = node;
		
		for (int i = 0; i < middle; i++) {
			node.children[0].insertKey(node.removeObject(i));
		}

		node.children[1] = new BTreeNode();
		node.children[1].parent = node;
		
		for (int i = middle + 1; i < numKeys; i++) {
			node.children[numKeys].insertKey(node.removeObject(i));
		}	
	}

	
	/**
	 * Height
	 * uhhh this is just wrong, check pseudocode
	 * how to do this with > 2 children?
	 * @param node
	 * @return
	 */
	public int height(BTreeNode node) {
		if (node == null) {
			return 0;
		}
		
		if (node.isLeaf()) {
			return 1;
		}
		
		int max = 1;
		s.push(node);
		
		while (!s.isEmpty()) {
			BTreeNode current = (BTreeNode) s.pop();
			for (int i = 0; i < node.numChildren; i++) {
				s.push(current.children[i]);
			}
			
		}
		
		return max;
	}

		
	/**
	 * Returns the number of unique keys in this BTree.
	 * @return uniques The number of unique keys in this BTree.
	 */
	public int getUniques() {
		return uniques;
	}

	
	/**
	 * Returns the degree of this BTree.
	 * @return t The degree of this BTree.
	 */
	public int getDegree() {
		return t;
	}


	

	/**
	 * [STATUS] More than halfway done.
	 * TODO: Figure out child assignment
	 * 
	 * B-Tree node class.
	 * Inner class.
	 * 
	 * @author 
	 * 
	 */
	private class BTreeNode {

		private BTreeNode[] children;	
		private TreeObject[] keys;
		private BTreeNode parent;
		private int numKeys;
		private int numChildren;

		
		/**
		 * Constructor.
		 * @param key The generic type object to store in this node.
		 */
		private BTreeNode() {

			parent = null;
			children = new BTreeNode[2 * t];
			keys = new TreeObject[(2 * t) - 1];

			numKeys = 0;
			numChildren = 0;

		}


		/**
		 * Returns whether or not this node is a leaf.
		 */
		private boolean isLeaf() {
			if (numChildren == 0) {
				return true;
			}
			return false;
		}


		/**
		 * Removes the given object.
		 * XXX: DO NOT DECREMENT NUMOBJECTS HERE -- it is handled in removeObject(int)
		 * @param removed The object to remove.
		 * @return removed The removed object.
		 * @return null If the given object is not in this node.
		 */
		private TreeObject removeObject(TreeObject removed) {
			for (int i = 0; i < numKeys; i++) {
				if (keys[i] == removed) {
					return removeObject(i);
				}
			}
			return null; 
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

			TreeObject removed = keys[index]; // store object to remove 

			int i = index;

			while (keys[i + 1] != null) {
				keys[i] = keys[i + 1];
				i++;
			}

			numKeys--;
			return removed;
		}


		/**
		 * Adds an object to this node.
		 * @param addThis The object being added.
		 * @return 
		 * XXX: May change to use int codes instead of a boolean...
		 */
		private void insertKey(TreeObject addThis) {
			// Case 1: Node is full [Failure]
			if (this.isFull()) {
				System.err.println("Attempted to add key to full node -- this shouldn't happen!  Check insert in BTree.");
				return; // this node is full
			}

			//TreeObject addThis = new TreeObject(key);	
			
			// Case 2: Node is empty [Success]
			if (this.isEmpty()) {
				keys[numKeys] = addThis;
				numKeys++;
				return;
			} 
			
			int i = numKeys - 1;	// initialize cursor
			
			// Case 3: Node has room [Success]
			while (i > 0) {		
				int result = addThis.compareTo(keys[i]);
				
				if (result >= 0) { 		// if addThis is greater than or equal to object at i
					if (result != 0) {	// if addThis is not equal to object at i
	
						int stop = i;	// store this position in stop
						i = numKeys;	// i is now pointing at first empty index
						
						while(i > stop) {	// while the object behind i is not the object we just added
							keys[i] = keys[i - 1]; // shift objects forward
							i--; // walk backwards
						}
					
						keys[i] = addThis;
						return;

					} else {	
						keys[i].incFrequency();	// object already in this node - do not insert or inc numobjects (do we even need to inc frequency?)
						return;
					}
				} 
				i--;
			}	
		}


		/**
		 * Returns the number of objects stored in this node.
		 * @return numObjects The number of objects stored in this node.
		 */
		private int getNumObjects() {
			return numKeys;
		}
		
		
		/**
		 * Returns whether or not this node is full.
		 * @return
		 */
		private boolean isFull() {
			return (numKeys < keys.length);
		}

		
		/**
		 * Returns whether or not this node is full.
		 * @return
		 */
		private boolean isEmpty() {
			return (numKeys == 0);
		}
		
		
		/**
		 * 
		 * @return
		 */
		private int isInRange(long key) {
			if (this.keys[0].getKey() > key) {
				return -1; // go left
			}
			
			if (this.keys[numKeys-1].getKey() < key) {
				return 1; // go right
			}
			
			return 0;
		}
		
		
		/**
		 * Returns whether or not the given key is already in the node
		 * @return
		 */
		private int contains(long key) {
			int result = isInRange(key);
			if (result == 0) {
				for (int i = 0; i < numKeys; i++) {
					if (keys[i].getKey() == key) {
						return i;
					}
				}
			}
			
			return -1;
		}
		
		

		/**
		 * Returns the object at the given index.
		 * @param index The given index.
		 * @return treeObjects[index] The object at the given index.
		 * @return null If the given index was out of range.
		 */
		private TreeObject getObject(int index) {
			if (index >= 0 && index < numKeys) {
				return keys[index];
			}
			return null;	
		}
		
	}

}
