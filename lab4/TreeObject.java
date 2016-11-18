/**
 * [STATUS] Complete?
 * 
 * TreeObject class.
 * 
 * @author 
 * 
 */
public class TreeObject {

	private int frequency;
	private long key;
	
	/**
	 * Constructor.
	 * @param key The DNA sequence to store in this TreeObject, represented in binary.
	 */
	public TreeObject(long key) {
		this.key = key;
		frequency = 0;
	}
	
	/**
	 * Returns the key stored in this TreeObject.
	 * @return key The key stored in this TreeObject.
	 */
	public long getKey() {
		return key;
	}
	
	
	/**
	 * Increments the frequency of this TreeObject.
	 */
	public void incFrequency() {
		frequency++;
	}
	
	/**
	 * Compares this TreeObject to a given TreeObject.
	 * @param that The TreeObject to compare with this TreeObject.
	 * @return 1 If this TreeObject is larger.
	 * @return -1 If this TreeObject is smaller.
	 * @return 0 If both TreeObjects are equal.
	 */
	public int compareTo(TreeObject that) {
		if (this.key > that.getKey()) {
			return 1;
		} else if (this.key < that.getKey()) {
			return -1;
		} else {
			return 0;
		}
	}
}
