package assignment;

public class TestNode<K extends Comparable<K>, V> {

	public K key;
	public V value;
	public TestNode left;
	public TestNode right;

	public TestNode() {
	}

	public TestNode(K newkey, V newvalue) {
		key = newkey;
		value = newvalue;
	}

}
