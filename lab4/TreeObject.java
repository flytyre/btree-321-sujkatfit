import java.io.Serializable;

/**
 * [STATUS] Complete?
 * 
 * TreeObject class.
 * 
 * @author 
 * 
 */
public class TreeObject 
{

	private int frequency;
	private long key;
	
	public TreeObject() 
	{
		this.key = -1;  //Non positive Key
		setFrequency(0);
	}
	
	
	/**
	 * Constructor.
	 * @param key The DNA sequence to store in this TreeObject, represented in binary.
	 */
	public TreeObject(long key) {
		this.key = key;
		setFrequency(0);
	}
	
	/**
	 * Returns the key stored in this TreeObject.
	 * @return key The key stored in this TreeObject.
	 */
	
	
	/**
	 * Increments the frequency of this TreeObject.
	 */
	public void incFrequency() {
		setFrequency(getFrequency() + 1);
	}
	
	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
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

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
}