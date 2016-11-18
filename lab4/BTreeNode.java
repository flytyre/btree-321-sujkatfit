import java.util.ArrayList;

/**
 * [STATUS] About halfway done.
 * TODO: Figure out where to adjust numChildren
 * TODO: Adjust addObject to work with array (need to add shifting)
 * TODO: Implement removeObject
 * 
 * B-Tree node class.
 *
 * @author ksilva
 * 
 */
public class BTreeNode {
	
	//private ArrayList<BTreeNode> children;
	//private ArrayList<TreeObject> treeObjects;
	
	// XXX: Should use arrays instead of array list to make size fixed, 
	// will need to manually manage number of children and objects now.
	private BTreeNode[] children;	
	private TreeObject[] treeObjects;
	private BTreeNode parent;
	private int t;
	private int numObjects;
	private int numChildren;
	
	
	/**
	 * Constructor.
	 * @param key The generic type object to store in this node.
	 */
	public BTreeNode(int degree) {
		
		t = degree;
		
		parent = null;
		children = new BTreeNode[t];
		treeObjects = new TreeObject[(2 * t) - 1];
		
		numObjects = 0;
		numChildren = 0;
		//children = new ArrayList<BTreeNode>();
		//treeObjects = new ArrayList<TreeObject>();

	}
	
	
	/**
	 * Sets the given node as this node's parent.
	 * Allows setting to null.
	 * @param parent The new parent of this node.
	 */
	public void setParent(BTreeNode parent) {
		this.parent = parent;
	}

	
	/**
	 * Returns this node's parent.
	 * @return parent The parent of this node.
	 */
	public BTreeNode getParent() {
		return parent;
	}

	
	/**
	 * Sets the given node as this node's left child.
	 * Allows setting to null.
	 * @param left The new left child of this node.
	 */
	public void setChild(int index, BTreeNode child) {
		if (index >= 0 && index < treeObjects.length) {
			children[index] = child;
		}
	}


	/**
	 * Returns the child node at the given index.
	 * @return left The child node at the given index.
	 */
	public BTreeNode getChild(int index) {
		if (index >= 0 && index < treeObjects.length) {
			return children[index];
		}
		return null;
	}
	
	
	/**
	 * Gets the key at the given index.
	 * @param index The given index.
	 * @return keys.get(index) The key at the given index.
	 */
	public TreeObject getObject(int index) {
		if (index >= 0 && index < treeObjects.length) {
			return treeObjects[index];
		}
		return null;	
	}
	
	
	/**
	 * Gets the key at the given index.
	 * @param index The given index.
	 * @return keys.get(index) The key at the given index.
	 */
	public TreeObject removeObject(int index) {
		if (index >= 0 && index < treeObjects.length) {
			TreeObject removed = treeObjects[index];
			// TODO: Remove entry and shift keys (TreeObjects)
			return removed;
		}
		return null;	
	}
	
	
	public int getNumObjects() {
		return numObjects;
	}

	
	/**
	 * Adds a key to the tree.
	 * @param adding
	 * @return
	 */
	public boolean addKey(TreeObject adding) {
		if (treeObjects.length < t) {
			//for (int i = 0; i < treeObjects.length; i++) {
			int i = 0;
			while (i < treeObjects.length) {
				int result = adding.compareTo(treeObjects[i]);
				if (result >= 0) {
					if (result != 0) {	// If adding is greater than TreeObject at i,
						if (treeObjects[i + 1] == null) {	// add if spot after i is empty,
							treeObjects[i + 1] = adding;	
							
						} else {	// else, must shift remaining objects.
							
							// XXX: This part is probably not right.....
							
							TreeObject tmp = treeObjects[i + 1];
							treeObjects[i + 1] = adding;
							
							i++; // advance and check next position
							
							while(treeObjects[i + 1] != null) {	// while the position after i is occupied
								treeObjects[i + 1] = tmp;
								i++;
								tmp = treeObjects[i + 1];
							}
						}
						
					} else {	
						treeObjects[i].incFrequency();						
					}
					return true;
				} 
				i++;
			}
		}
		return false;
	}
	
	
	/**
	 * Returns whether or not this node is a leaf.
	 */
	public boolean isLeaf() {
		if (numChildren == 0) {
			return true;
		}
		return false;
	}
	
}
