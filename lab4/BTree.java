/**
 * [STATUS] Nowhere near done!
 * TODO: Implement insert method
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

	/**
	 * Constructor.
	 */
	public BTree(int degree) {
		this.t = degree;
		root = new BTreeNode(t);
	}

	
	/**
	 * This method is a nightmare.
	 * Adjusts node locations when inserting. 
	 * ew
	 * @param node
	 */
	public void move(BTreeNode node) {
		BTreeNode parent = node.getParent();

		if (parent != null) {
			if (parent.getNumObjects() < t) {
				parent.addObject(node.removeObject(t-1)); 
			} 
		} 
	}

	
	/**
	 * Non-recursive, may change to recursive later
	 * may use int codes instead of boolean
	 * if returns false, objects in tree must be shifted
	 * @param t
	 */
	public boolean insert(TreeObject obj) {
		if (!root.isFull()) {	// if there is space in the root
			root.addObject(obj);
		}
		
		int result;
		
		// if there was no space, must find where to add this object
		for (int i = 0; i < root.getNumObjects(); i++) {
			result = obj.compareTo(root.getObject(i));
			
			if 
			
			
		}
		
		return false;
		
		// XXX: this does nothing right now
		
	}

	/**
	 * 
	 * @return
	 */
	public int getUniques() {
		return uniques;
	}

	/**
	 * 
	 * @return
	 */
	public int getDegree() {
		return t;
	}

	
	/** 
	 * 
	 */
	public void search() {
		
		
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

		// XXX: Should use arrays instead of array list to make size fixed, 
		// will need to manually manage number of children and objects now.
		private BTreeNode[] children;	
		private TreeObject[] objects;
		private BTreeNode parent;
		private int t;
		private int numObjects;
		private int numChildren;


		/**
		 * Constructor.
		 * @param key The generic type object to store in this node.
		 */
		private BTreeNode(int degree) {

			t = degree;

			parent = null;
			children = new BTreeNode[2 * t];
			objects = new TreeObject[(2 * t) - 1];

			numObjects = 0;
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
		 * Sets the given node as this node's parent.
		 * Allows setting to null.
		 * @param parent The new parent of this node.
		 */
		private void setParent(BTreeNode parent) {
			this.parent = parent;
		}


		/**
		 * Returns this node's parent.
		 * @return parent The parent of this node.
		 */
		private BTreeNode getParent() {
			return parent;
		}


		/**
		 * Sets the given node as this node's child at the give index.
		 * Allows setting to null.
		 * XXX: this might not be needed... replace with add/removeChild?
		 * @param 
		 */
		private void setChild(int index, BTreeNode child) {
			if (index >= 0 && index < children.length) {
				children[index] = child;
			}
		}


		/**
		 * Returns the child node at the given index.
		 * @return children[index] The child node at the given index.
		 * @return null If the given index was out of range.
		 */
		private BTreeNode getChild(int index) {
			if (index >= 0 && index < numChildren) {
				return children[index];
			}
			return null;
		}


		/**
		 * Returns the number of children this node has.
		 * @return numChildren The number of children this node has.                                                                                             
		 */
		private int getNumChildren() {
			return numChildren;
		}


		/**
		 * Removes the given object.
		 * XXX: DO NOT DECREMENT NUMOBJECTS HERE -- it is handled in removeObject(int)
		 * @param removed The object to remove.
		 * @return removed The removed object.
		 * @return null If the given object is not in this node.
		 */
		private TreeObject removeObject(TreeObject removed) {
			for (int i = 0; i < numObjects; i++) {
				if (objects[i] == removed) {
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
			if (index < 0 || index >= numObjects) {
				return null;
			}

			TreeObject removed = objects[index]; // store object to remove 

			int i = index;

			while (objects[i + 1] != null) {
				objects[i] = objects[i + 1];
				i++;
			}

			numObjects--;
			return removed;

		}


		/**
		 * Adds an object to this node.
		 * @param adding The object being added. XXX: should probably rename this variable
		 * @return 
		 * XXX: May change to use int codes instead of a boolean...
		 * XXX: this method is kind of messed up right now...
		 */
		private boolean addObject(TreeObject adding) {
			if (this.isFull()) {
				return false; // this node is full
			}

			int i = 0; // initialize cursor

			while (i < objects.length) { // XXX: is this right?
				int result = adding.compareTo(objects[i]);
				if (result <= 0) { // if adding is less than or equal to object at i 
					if (result != 0) {	// and if adding is not equal to object at i
						if (objects[i + 1] == null) {	// add if spot after i is empty,
							objects[i + 1] = adding;	
							numObjects++;
							return true;

						} else {	// else, must shift remaining objects forward.

							i++; // advance cursor, now pointing to index for insertion

							TreeObject tmp = objects[i]; // store object at i in tmp, we will place this back after shifting
							objects[i] = adding;	// insert adding at i

							i = numObjects; // i is now pointing at first empty index

							while(objects[i - 1] != adding) {	// while the object behind i is not the object we just added
								objects[i] = objects[i - 1]; // shift objects forward
								i--; // walk backwards
							}

							// at this point, i is pointing at the index just after adding
							// put tmp back in this spot, done!
							objects[i] = tmp;
							return true;
						}

					} else {	
						objects[i].incFrequency();	// object already in this node - do not insert or inc numobjects (do we even need to inc frequency?)
						return true;
					}
				} 
				i++; // advance cursor if adding is less than treeObject at i
			}

			return false; // failed to insert (will this ever happen if the node wasn't full...?)
		}


		/**
		 * Returns the number of objects stored in this node.
		 * @return numObjects The number of objects stored in this node.
		 */
		private int getNumObjects() {
			return numObjects;
		}
		
		
		/**
		 * Returns whether or not this node is full.
		 * @return
		 */
		private boolean isFull() {
			return (numObjects < objects.length);
		}


		/**
		 * Returns the object at the given index.
		 * @param index The given index.
		 * @return treeObjects[index] The object at the given index.
		 * @return null If the given index was out of range.
		 */
		private TreeObject getObject(int index) {
			if (index >= 0 && index < numObjects) {
				return objects[index];
			}
			return null;	
		}
		
	}

}
