package assignment;

import java.awt.RenderingHints.Key;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import javax.net.ssl.ExtendedSSLSession;
import javax.print.attribute.ResolutionSyntax;

public class TreapMap<K extends Comparable<K>, V> implements Treap<K, V> {

	static class Node<K extends Comparable<K>, V> {

		public K key;
		public V value;
		public int priorityValue;
		public Node left;
		public Node right;

		public Node() {
			priorityValue = (int) (MAX_PRIORITY * Math.random());
		}

		//constructor
		public Node(K newkey, V newvalue) {
			key = newkey;
			value = newvalue;
			priorityValue = (int) (MAX_PRIORITY * Math.random());
		}

		private void setPriority(int x) {
			priorityValue = x;
		}

	}

	public Node root;

	public TreapMap() {
		root = null;
	}

	private TreapMap(Node node) {
		root = node;
	}

	@Override
	public V lookup(K key) {
		// null key
		if (key == null)
			return null;

		return BSTLookup(root, key);

	}

	public V BSTLookup(Node node, K Key) {
		// base case for recursion
		if (node == null) {
			return null;
		}
		
		//check left or right side recursively
		else {
			if (node.key.compareTo(Key) > 0) {
				node = node.left;
				return BSTLookup(node, Key);
			}

			if (node.key.compareTo(Key) < 0) {
				node = node.right;
				return BSTLookup(node, Key);
			}
		}

		return (V) node.value;
	}

	@Override
	public void insert(K key, V value) {
		// null key or value
		if (key == null || value == null) {
			throw new NullPointerException("KEY or VALUE is null");
		}
		// base case at node BST
		if (root == null) {
			root = new Node<K, V>(key, value);
			return;
		}

		else
			root = BSTInsert(root, key, value);

	}

	public Node BSTInsert(Node node, K k, V v) {
		// create a new node with given K,V if empty
		if (node == null) {
			node = new Node<K, V>(k, v);
		}

		else {
			//rotate based on priority value
			if (node.key.compareTo(k) > 0) {
				node.left = BSTInsert(node.left, k, v);
				if (node.priorityValue < node.left.priorityValue)
					return rightRot(node);
			} else {
				if (node.key.compareTo(k) < 0) {
					node.right = BSTInsert(node.right, k, v);
					if (node.priorityValue < node.right.priorityValue)
						return leftRot(node);
				} else {
					// same key, then change value
					node.value = v;
					return node;
				}
			}
		}

		return node;

	}

	//perform a right rotation on the node passed
	//then return the new root
	public Node rightRot(Node k2) {

		Node k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		return k1;

	}
	
	//perform a left rotation on the node passed
	//then return the new root
	public Node leftRot(Node k1) {
		Node k2 = k1.right;
		k1.right = k2.left;
		k2.left = k1;
		return k2;

	}

	@Override
	public V remove(K key) {
		// treap not init
		if (key == null)
			throw new NullPointerException("KEY is null");

		//check if the key exists
		V removedValue = lookup(key);
		if (removedValue == null)
			return null;
		
		else {
			// only root present
			if (root.key.equals(key) && root.left == null && root.right == null) {
				root = null;
				return removedValue;
			}

			else {
				root = BSTRemove(root, key);
				return removedValue;
			}

		}

	}

	public Node BSTRemove(Node node, K key) {
		//if not targeted key, recur
		if (node != null) {
			int compare = key.compareTo((K) node.key);
			if (compare < 0) {
				node.left = BSTRemove(node.left, key);
			} else if (compare > 0) {
				node.right = BSTRemove(node.right, key);
			}

			else {
				// one child or leaf
				if (node.left == null)
					return node.right;
				if (node.right == null)
					return node.left;

				// two children
				if (node.left.priorityValue < node.right.priorityValue) {
					node = rightRot(node);
					node.right = BSTRemove(node.right, key);
				} else {
					node = leftRot(node);
					node.left = BSTRemove(node.left, key);
				}
			}
		}

		return node;
	}

	@Override
	public Treap<K, V>[] split(K key) {
		Treap<K, V>[] treaps = new Treap[2];
		// if the treap is empty
		if (root == null) {
			treaps[0] = new TreapMap<K, V>();
			treaps[1] = new TreapMap<K, V>();
			return treaps;
		}
		
		//init a new treap
		TreapMap<K, V> treapMap2 = new TreapMap<>(root);
		V lost = treapMap2.lookup(key);
		int flag = -1;

		// just use whatever value
		treapMap2.root = treapMap2.BSTMaxHeapInsert(root, key, (V) root.value);

		if (lost != null) {
			if (key.compareTo((K) treapMap2.root.key) > 0 || key.compareTo((K) treapMap2.root.key) == 0) {
				flag = 1;
				System.out.println("flag is " + flag);
			}
			if (key.compareTo((K) treapMap2.root.key) < 0)
				flag = 0;
			System.out.println("flag is " + flag);
		}
		
		//two treaps represent left and right
		TreapMap<K, V> treapMapLeft = new TreapMap<>(treapMap2.root.left);
		TreapMap<K, V> treapMapRight = new TreapMap<>(treapMap2.root.right);
		if (flag == 0)
			treapMapLeft.insert(key, lost);
		if (flag == 1)
			treapMapRight.insert(key, lost);

		treaps[0] = treapMapLeft;
		treaps[1] = treapMapRight;

		return treaps;
	}

	public Node BSTMaxHeapInsert(Node node, K k, V v) {
		// create a new node with given K,V if empty
		if (node == null) {
			node = new Node<K, V>(k, v);
			node.setPriority(MAX_PRIORITY);
		}
		
		//same as the recursive insert, but only set priority
		else {
			if (node.key.compareTo(k) > 0) {
				node.left = BSTMaxHeapInsert(node.left, k, v);
				if (node.priorityValue < node.left.priorityValue)
					return rightRot(node);
			} else {
				if (node.key.compareTo(k) < 0) {
					node.right = BSTMaxHeapInsert(node.right, k, v);
					if (node.priorityValue < node.right.priorityValue)
						return leftRot(node);
				} else {
					node.setPriority(MAX_PRIORITY);
					return node;
				}
			}
		}

		return node;

	}

	@Override
	// it's up to the caller to provide legitimate/valid arguments.
	public void join(Treap<K, V> t) {
		//check if an instance of implemented class
		if (t instanceof TreapMap == false) {
			System.err.println("Not an instance of implementing class");
			return;
		}
		
		TreapMap<K, V> merge = (TreapMap<K, V>) t;
		TreapMap<K, V> join = new TreapMap<K, V>();

		// check where one of them is empty
		if (merge.root == null) {
			return;
		}

		if (root == null) {
			root = merge.root;
			merge.root = null;
			return;
		}
		
		//join two treaps together
		join.root = new Node<K, V>((K) root.key, (V) root.value);
		join.root.setPriority(-1);
		join.root.left = root;
		join.root.right = merge.root;
		join = new TreapMap<>(join.root);
		join.remove((K) root.key);

		root = join.root;
		merge.root = null;

	}

	@Override
	public void meld(Treap<K, V> t) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void difference(Treap<K, V> t) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<K> iterator() {
		TreapIterator iterator = new TreapIterator(root);
		return iterator;
	}

	// using inorder traversal
	class TreapIterator implements Iterator<K> {

		ArrayList<Node<K, V>> all = new ArrayList<Node<K, V>>();
		int currentIndex = -1;

		public TreapIterator(Node root) {
			Inorder inorder = new Inorder();
			all = inorder.inorder(root);
		}

		@Override
		public boolean hasNext() {
			if (currentIndex + 1 >= all.size())
				return false;
			else
				return true;
		}

		@Override
		public K next() {
			currentIndex++;
			if (currentIndex < all.size())
				return all.get(currentIndex).key;
			else
				throw new NoSuchElementException();
		}

	}

	public class Inorder {
		ArrayList<Node<K, V>> allNodes = new ArrayList<Node<K, V>>();

		public ArrayList<Node<K, V>> inorder(Node root) {
			if (root != null) {
				recursiveInorder(root);
			}

			return allNodes;
		}

		public void recursiveInorder(Node node) {
			if (node.left != null)
				recursiveInorder(node.left);

			allNodes.add(node);

			if (node.right != null)
				recursiveInorder(node.right);
		}
	}

	@Override
	public double balanceFactor() throws UnsupportedOperationException {
		double maxdepth = maxDepth(root);
		double mindepth = minimumDepth(root);
		if (mindepth == 0)
			return 1.00;
		double ratio = maxDepth(root) / minimumDepth(root);
		return ratio;
	}

	int minimumDepth(Node root) {
		// Corner case. Should never be hit unless the code is
		// called on root = NULL
		if (root == null)
			return 0;
		// Base case : Leaf Node. 
		if (root.left == null && root.right == null)
			return 1;
		// If left subtree is NULL, recur for right subtree
		if (root.left == null)
			return minimumDepth(root.right) + 1;
		// If right subtree is NULL, recur for right subtree
		if (root.right == null)
			return minimumDepth(root.left) + 1;

		return Math.min(minimumDepth(root.left), minimumDepth(root.right)) + 1;
	}

	int maxDepth(Node node) {
		if (node == null)
			return 0;
		else {
			// compute the depth of each subtree 
			int lDepth = maxDepth(node.left);
			int rDepth = maxDepth(node.right);

			// use the larger one 
			if (lDepth > rDepth)
				return (lDepth + 1);
			else
				return (rDepth + 1);
		}
	}
	
	//keep track on both the node and indent level
	class NodeCounter {
		private Node<K, V> node;
		private int indentLev;

		public NodeCounter(Node n, int k) {
			node = n;
			indentLev = k;
		}
	}

	@Override
	public String toString() {
		String data = "";
		int currentPriority = 0;

		if (root == null) {
			return "";
		}

		// Create an empty stack and push root to it
		Stack<NodeCounter> allNodes = new Stack<NodeCounter>();
		allNodes.push(new NodeCounter(root, 0));

		while (allNodes.empty() == false) {
			// Pop the top item from stack and print it
			String indent = "";
			NodeCounter nodeCounter = allNodes.peek();
			Node mynode = nodeCounter.node;
			int lev = nodeCounter.indentLev;
			for (int i = 0; i < lev; i++) {
				indent = indent + "\t";
			}
			data = data + indent + "[" + mynode.priorityValue + "] " + "<" + mynode.key + ", " + mynode.value + ">\n";
			allNodes.pop();

			// Push right and left children of the popped node to stack
			if (mynode.right != null) {
				allNodes.push(new NodeCounter(mynode.right, lev + 1));
			}
			if (mynode.left != null) {
				allNodes.push(new NodeCounter(mynode.left, lev + 1));
			}
		}

		return data;

	}

	void iterativePreorder(Node node) {
		// Base Case
		if (node == null) {
			return;
		}

		// Create an empty stack and push root to it
		Stack<Node> nodeStack = new Stack<Node>();
		nodeStack.push(root);

		while (nodeStack.empty() == false) {
			// Pop the top item from stack and print it
			Node mynode = nodeStack.peek();
			System.out.print(mynode.priorityValue + " ");
			nodeStack.pop();

			// Push right and left children of the popped node to stack
			if (mynode.right != null) {
				nodeStack.push(mynode.right);
			}
			if (mynode.left != null) {
				nodeStack.push(mynode.left);
			}
		}
	}

}
