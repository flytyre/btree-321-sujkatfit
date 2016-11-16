
public class TreeObject {

	private int frequency;
	private long key;
	
	public TreeObject(long key) {
		this.key = key;
		frequency = 0;
	}
	
	public long getKey() {
		return key;
	}
	
	public void incFrequency() {
		frequency++;
	}
	
	public int compare(TreeObject that) {
		if (this.key > that.getKey()) {
			return 1;
		} else if (this.key < that.getKey()) {
			return -1;
		} else {
			return 0;
		}
	}
}
