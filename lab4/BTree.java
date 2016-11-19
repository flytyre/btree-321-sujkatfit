/**
 * [STATUS] Nowhere near done!
 * TODO: Implement insert method
 * 
 * BTree class.
 * 
 * @author
 * 
 */
public class BTree {

	private int t;
	private int uniques;
	
	public BTree(int degree) {
		this.t = degree;
	}
	
	/**
	 * This method is a nightmare.
	 * Adjusts node locations when inserting. 
	 * @param node
	 */
	public void move(BTreeNode node) {
		BTreeNode parent = node.getParent();
	
		if (parent != null) {
			if (parent.getNumObjects() < t) {
				parent.addKey(node.removeObject(t-1)); 
			} 
		} 
	}
	
	public void insert(TreeObject t) {
		
		// this does nothing right now
		
	}
	
	public int getUniques() {
		return uniques;
	}
	
	public int getDegree() {
		return t;
	}
	
	// XXX: do we need a search method here?

	
	
}
