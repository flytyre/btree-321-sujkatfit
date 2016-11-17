import java.util.ArrayList;

/**
 * B-Tree node class.
 * Generic.
 * @author ksilva
 * @param <K> Generic type.
 */
public class BTreeNode<K> {
	
	private BTreeNode parent;
	private ArrayList<BTreeNode> children;
	private ArrayList<TreeObject> treeObjects;
	private int degree;
	
	/**
	 * Constructor.
	 * @param key The generic type object to store in this node.
	 */
	public BTreeNode(int degree) {
		parent = null;

		children = new ArrayList<BTreeNode>();
		
		this.degree = degree;
		treeObjects = new ArrayList<TreeObject>();
		
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
		if (index >= 0 && index < treeObjects.size()) {
			children.set(index, child);
		}
	}


	/**
	 * Returns the child node at the given index.
	 * @return left The child node at the given index.
	 */
	public BTreeNode getChild(int index) {
		if (index >= 0 && index < treeObjects.size()) {
			return children.get(index);
		}
		return null;
	}
	
	
	/**
	 * Gets the key at the given index.
	 * @param index The given index.
	 * @return keys.get(index) The key at the given index.
	 */
	public TreeObject getObject(int index) {
		if (index >= 0 && index < treeObjects.size()) {
			return treeObjects.get(index);
		}
		return null;	
	}
	
	
	/**
	 * Gets the key at the given index.
	 * @param index The given index.
	 * @return keys.get(index) The key at the given index.
	 */
	public TreeObject removeObject(int index) {
		if (index >= 0 && index < treeObjects.size()) {
			return treeObjects.remove(index);
		}
		return null;	
	}
	
	
	public int getNumObjects() {
		return treeObjects.size();
	}

	
	/**
	 * Adds a key to the tree.
	 * @param adding
	 * @return
	 */
	public boolean addKey(TreeObject adding) {
		if (treeObjects.size() < degree) {
			int next = 0;
			
			for (int i = 0; i < treeObjects.size(); i++) {
				int result = adding.compare(treeObjects.get(i));
				if (result <= 0) {
					if (result != 0) {
						treeObjects.add(i, adding);
					} else {
						treeObjects.get(i).incFrequency();						
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
		if (children.size() == 0) {
			return true;
		}
		return false;
	}
	
}