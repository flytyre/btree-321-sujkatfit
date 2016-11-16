
public class BTree {

	private int degree;
	private int uniques;
	
	public BTree(int degree) {
		this.degree = degree;
	}
	
	public void move(BTreeNode node) {
		BTreeNode parent = node.getParent();
	
		if (parent != null) {
			if (parent.getNumObjects() < degree) {
				parent.addKey(node.removeObject(degree-1)); 
			} 
		} 
	}
	
	public void insert(TreeObject t) {
		// this does nothing right now
	}
	
	public int getUniques() {
		return uniques;
	}
}
