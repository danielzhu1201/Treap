package assignment;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.xml.ws.soap.Addressing;

import org.junit.Test;
import assignment.Treap;
import assignment.TreapMap;

public class TreapTesting {

	@Test
	public void IntegratedTest() {
		// create two treaps for future use
		Treap<Integer, Integer> treap1 = new TreapMap<Integer, Integer>();
		Treap<Integer, Integer> treap2 = new TreapMap<Integer, Integer>();

		// null parameter for split&join
		try {
			treap1.split(null);
		} catch (NullPointerException e) {
			System.out.println("Success - split null");
		}

		try {
			treap1.join(null);
		} catch (NullPointerException e) {
			System.out.println("Success - join null");
		}

		// Behavior on empty treap - should return two empty treaps
		Treap<Integer, Integer>[] treaps = treap1.split(5);
		assertTrue(treaps[0] != null);
		assertTrue(treaps[1] != null);
		Iterator<Integer> testIter = treaps[0].iterator();
		assertTrue(testIter.hasNext() == false);
		testIter = treaps[1].iterator();
		assertTrue(testIter.hasNext() == false);

		// split method testing - normal case
		treap1 = new TreapMap<Integer, Integer>();
		int[] keys = { 1, 2, 3, 4, 5, 20, 30, 80, 120, 950 };
		int[] values = { 2, 3, 4, 5, 20, 30, 80, 120, 950, 1 };
		for (int i = 0; i < keys.length; i++) {
			treap1.insert(keys[i], values[i]);
		}

		int splitFactor = 3;
		Treap<Integer, Integer>[] treaps2 = treap1.split(keys[splitFactor]);

		Treap<Integer, Integer> testTreap1 = treaps2[0];
		Treap<Integer, Integer> testTreap2 = treaps2[1];

		System.out.println("split factor= " + splitFactor);

		for (int i = 0; i < splitFactor; i++) {
			int result = testTreap1.lookup(keys[i]);
			assertEquals(result, values[i]);
		}
		System.out.println(testTreap2.lookup(keys[splitFactor]));
		for (int i = splitFactor; i < keys.length; i++) {
			int result = testTreap2.lookup(keys[i]);
			assertEquals(result, values[i]);
		}

		// split - using max inserted value
		treap1 = new TreapMap<Integer, Integer>();
		for (int i = 0; i < keys.length; i++) {
			treap1.insert(keys[i], values[i]);
		}

		splitFactor = keys.length - 1;
		treaps2 = treap1.split(keys[splitFactor]);

		testTreap1 = treaps2[0];
		testTreap2 = treaps2[1];

		System.out.println("split factor= " + splitFactor);

		for (int i = 0; i < splitFactor; i++) {
			int result = testTreap1.lookup(keys[i]);
			assertEquals(result, values[i]);
		}

		for (int i = splitFactor; i < keys.length; i++) {
			int result = testTreap2.lookup(keys[i]);
			assertEquals(result, values[i]);
		}

		// split - using min inserted value
		treap1 = new TreapMap<Integer, Integer>();
		for (int i = 0; i < keys.length; i++) {
			treap1.insert(keys[i], values[i]);
		}

		splitFactor = 0;
		treaps2 = treap1.split(keys[splitFactor]);

		testTreap1 = treaps2[0];
		testTreap2 = treaps2[1];

		System.out.println("split factor= " + splitFactor);

		for (int i = 0; i < splitFactor; i++) {
			int result = testTreap1.lookup(keys[i]);
			assertEquals(result, values[i]);
		}

		System.out.println(testTreap2.lookup(1));

		for (int i = splitFactor; i < keys.length; i++) {
			int result = testTreap2.lookup(keys[i]);
			assertEquals(result, values[i]);
		}

		// split - using value< min
		treap1 = new TreapMap<Integer, Integer>();
		for (int i = 0; i < keys.length; i++) {
			treap1.insert(keys[i], values[i]);
		}

		splitFactor = 0;
		treaps2 = treap1.split(keys[splitFactor] - 1);

		testTreap1 = treaps2[0];
		testTreap2 = treaps2[1];

		System.out.println("split factor= " + splitFactor);

		for (int i = 0; i < splitFactor; i++) {
			int result = testTreap1.lookup(keys[i]);
			assertEquals(result, values[i]);
		}

		for (int i = splitFactor; i < keys.length; i++) {
			int result = testTreap2.lookup(keys[i]);
			assertEquals(result, values[i]);
		}

		// split - using value> max
		treap1 = new TreapMap<Integer, Integer>();
		for (int i = 0; i < keys.length; i++) {
			treap1.insert(keys[i], values[i]);
		}

		splitFactor = keys.length - 1;
		treaps2 = treap1.split(keys[splitFactor] + 1);

		testTreap1 = treaps2[0];
		testTreap2 = treaps2[1];

		System.out.println("split factor= " + splitFactor);

		for (int i = 0; i < splitFactor; i++) {
			int result = testTreap1.lookup(keys[i]);
			assertEquals(result, values[i]);
		}

		// RESET Treap1,2
		treap1 = new TreapMap<Integer, Integer>();
		treap2 = new TreapMap<Integer, Integer>();

		int[] keys2 = { 1, 2, 3, 4, 5, 20, 30, 80, 120, 950 };
		int[] values2 = { 2, 3, 4, 5, 20, 30, 80, 120, 950, 1000 };

		// adding 1st half to 1 and 2nd half to 2
		for (int i = 0; i < 5; i++) {
			treap1.insert(keys2[i], values2[i]);
		}
		for (int i = 5; i < 10; i++) {
			treap2.insert(keys2[i], values2[i]);
		}

		treap1.join(treap2);

		// test if all elements in treap1
		for (int i = 0; i < keys2.length; i++) {
			assertTrue(values2[i] == treap1.lookup(keys2[i]));
		}

		TreapMap<Integer, Integer> emptyTreap = new TreapMap<>();

		// join with emptyTreap
		treap1.join(emptyTreap);
		for (int i = 0; i < keys2.length; i++) {
			assertTrue(values2[i] == treap1.lookup(keys2[i]));
		}

		emptyTreap.join(treap1);
		for (int i = 0; i < keys2.length; i++) {
			assertTrue(values2[i] == emptyTreap.lookup(keys2[i]));
		}

		// if change tree b, will it affect a? - remove or insert
		Treap<Integer, Integer> treap4 = new TreapMap<>();
		treap4.insert(10000, 10000);
		treap1.join(treap4);
		treap4.remove(10000);
		assertTrue(treap1.lookup(10000) == 10000);
		assertEquals(null, treap4.lookup(100000));

	}

	@Test
	public void LookUpAndInserttest() {

		Treap<Integer, Integer> treap = new TreapMap<Integer, Integer>();

		// Look up in an empty treap
		assertEquals(null, treap.lookup(null));
		assertEquals(null, treap.lookup(10));

		// insert with invalid parameters
		try {
			treap.insert(null, null);
		} catch (NullPointerException e) {
			System.out.println("Success - insert null/null");
		}
		try {
			treap.insert(5, null);
		} catch (NullPointerException e) {
			System.out.println("Success - insert KEY/null");
		}
		try {
			treap.insert(null, 20);
		} catch (NullPointerException e) {
			System.out.println("Success - insert null/VALUE");
		}

		// test regular insert/ multiple value on same key
		treap.insert(1, 5);
		assertEquals(5, (int) treap.lookup(1));
		treap.insert(1, 10);
		treap.insert(1, 20);
		assertEquals(20, (int) treap.lookup(1));
		assertEquals(null, treap.lookup(null));

	}

	@Test
	public void RemoveTest() {

		Treap<Integer, Integer> treap = new TreapMap<Integer, Integer>();
		ArrayList<TestNode> nodeList = new ArrayList<TestNode>();

		// remove in an empty treap
		assertEquals(null, treap.remove(20));

		// randomly insert 5 pairs of K,V
		for (int i = 0; i < 5; i++) {
			int key = i;
			int value = 5 * i;
			treap.insert(key, value);
			nodeList.add(new TestNode<Integer, Integer>(key, value));
		}

		// does it return correct Values?
		for (TestNode<Integer, Integer> node : nodeList) {
			assertEquals(node.value, treap.remove(node.key));
		}

		// shouldn't be able to find the node after removing
		assertEquals(null, treap.lookup((Integer) nodeList.get(0).key));
		assertEquals(null, treap.lookup((Integer) nodeList.get(1).key));
		assertEquals(null, treap.lookup((Integer) nodeList.get(2).key));
		assertEquals(null, treap.lookup((Integer) nodeList.get(3).key));
		assertEquals(null, treap.lookup((Integer) nodeList.get(4).key));

		ArrayList<TestNode> nodeList2 = new ArrayList<TestNode>();

		// randomly insert 5 pairs of K,V
		for (int i = 0; i < 5; i++) {
			int key = i;
			int value = 5 * i;
			treap.insert(key, value);
			nodeList2.add(new TestNode<Integer, Integer>(key, value));
		}

		// null
		// assertEquals(null, treap.remove(null));

		// null throw error
		try {
			treap.remove(null);
		} catch (NullPointerException e) {
			System.out.println("Null pointer exception thrown on null parameter");
		}

		// can we still find the added items?
		assertEquals(nodeList2.get(0).value, treap.lookup((Integer) nodeList2.get(0).key));
		assertEquals(nodeList2.get(1).value, treap.lookup((Integer) nodeList2.get(1).key));
		assertEquals(nodeList2.get(2).value, treap.lookup((Integer) nodeList2.get(2).key));
		assertEquals(nodeList2.get(3).value, treap.lookup((Integer) nodeList2.get(3).key));
		assertEquals(nodeList2.get(4).value, treap.lookup((Integer) nodeList2.get(4).key));

	}

	@Test
	public void toStringAndIterator() {

		Treap<Integer, Integer> treap = new TreapMap<Integer, Integer>();

		// empty treap behaviors
		assertEquals("", treap.toString());
		Iterator<Integer> iterator = treap.iterator();
		assertEquals(iterator.hasNext(), false);

		ArrayList<TestNode> nodeList2 = new ArrayList<TestNode>();
		Hashtable<Integer, Integer> set = new Hashtable<Integer, Integer>();
		// add in 10 random elements
		for (int i = 0; i < 9; i++) {
			int key = i;
			int value = (int) Math.random() * 100;
			treap.insert(key, value);
			set.put(key, value);
			nodeList2.add(new TestNode<Integer, Integer>(key, value));
		}

		// print with correct format?
		// does # of items match?
		System.out.println(treap.toString());

		// iterator test
		iterator = treap.iterator();
		Set<Integer> keyset = set.keySet();
		int counter = 0;
		while (iterator.hasNext()) {
			assertTrue(keyset.contains(iterator.next()));
			counter++;
		}
		assertEquals(counter, nodeList2.size());

		// is the order pertained?
		ArrayList<Integer> keyList = new ArrayList<>();
		while (iterator.hasNext()) {
			keyList.add(iterator.next());
		}

		for (int i = 1; i < keyList.size(); i++) {
			assertTrue(keyList.get(i - 1).compareTo(keyList.get(i)) < 0);
		}

	}

}
