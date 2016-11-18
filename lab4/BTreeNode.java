import java.util.ArrayList;

/**
 * [STATUS] About halfway done.
 * TODO: Figure out where to adjust numChildren
 * TODO: Adjust addObject to work with array (need to add shifting)
 * TODO: Implement removeObject
 * 
 * B-Tree node class.
 * Generic.
 *
 * @author ksilva
 * @param <K> Generic type.
 */
public class BTreeNode<K> {
	
	private BTreeNode parent;
	//private ArrayList<BTreeNode> children;
	//private ArrayList<TreeObject> treeObjects;
	private BTreeNode[] children;	// XXX: Should use arrays instead of array list to make size fixed.
	private TreeObject[] treeObjects;
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
			int next = 0;
			
			for (int i = 0; i < treeObjects.length; i++) {
				int result = adding.compare(treeObjects[i]);
				if (result <= 0) {
					if (result != 0) {
						treeObjects[i] = adding;
					} else {
						treeObjects[i].incFrequency();						
					}
					return true;
				} 
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
