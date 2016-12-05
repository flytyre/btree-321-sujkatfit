/**
 * [STATUS] Complete.
 * 
 * KeyObject class.
 * 
 * @author Sujeet Ayyapureddi, Margiawan Fitriani, Kathryn Silva
 * 
 */
public class KeyObject {

	private int frequency;
	private long key;
	
	/**
	 * Constructor.
	 * @param key The DNA sequence to store in this KeyObject, represented in binary.
	 */
	public KeyObject(long key) {
		this.key = key;
		setFrequency(0);
	}
	
	/**
	 * Constructor. 
	 * Populates this KeyObject with a dummy value if no key was given.
	 */
	public KeyObject() {
		this.key = -1;  // Non-positive dummy key.
		setFrequency(0);
	}
	
	/**
	 * Increments the frequency of the key in this KeyObject.
	 */
	public void incFrequency() {
		setFrequency(getFrequency() + 1);
	}
	
	/**
	 * Returns the key stored in this KeyObject.
	 * @return key The key stored in this KeyObject.
	 */
	public long getKey() {
		return key;
	}

	/**
	 * 
	 * @param key
	 */
	public void setKey(long key) {
		this.key = key;
	}

	/**
	 * Compares this KeyObject to a given KeyObject.
	 * @param that The KeyObject to compare with this KeyObject.
	 * @return 1 If this KeyObject is larger.
	 * @return -1 If this KeyObject is smaller.
	 * @return 0 If both KeyObjects are equal.
	 */
	public int compareTo(KeyObject that) {
		if (this.key > that.getKey()) {
			return 1;
		} else if (this.key < that.getKey()) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 *
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
}