import BTree.BTreeNode;

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