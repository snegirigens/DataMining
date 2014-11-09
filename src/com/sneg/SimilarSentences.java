/**
 * Author:	Sneg (Leonid Snegirev)
 * Created:	23.10.14
 */

package com.sneg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SimilarSentences {
	private static final String FILE = "C:/Workspace/Test/resources/sentences.txt";
	private static final int MEDIAN = 5;
	private static final int LIMIT = 1000000000;

	public static void main (String[] args) throws Exception {
		SimilarSentences test = new SimilarSentences();

		test.readFile (FILE, test.new HashCalculator());
		test.packDuplicates();

//		test.printMap ("Low hashes",  test._lowHashes);
//		test.printMap ("High hashes", test._highHashes);
//		test.printMap ("Hashes", test._idsToHashes);
//		test.printMap ("Full hashes", test._fullHashes);

		test.readFile (FILE, test.new SimilarityTester());
//		test.printMap (test._simHashes);

//		test.packMaps();
		test.calcSimilarities (false);
	}

//	private Map <Integer, List <Integer>> _lowHashes  = new HashMap<> (10000000);	// Key: hash of the first half of the sentence. Value: list of sentence ids.
//	private Map <Integer, List <Integer>> _highHashes = new HashMap<> (10000000);	// Key: hash of the last half of the sentence. Value: list of sentence ids.
//	private Map <Integer, List <Integer>> _simHashes  = new HashMap<> (10000000);	// Key: hash of the whole sentence - 1 word + the whole sentence. Value: list of sentence ids.
//	private Map <Integer, int[]> _idsToHashes = new HashMap<> (10000000);			// Key: sentence id. Value: hashes of the first and last halfs of the sentence.

	private Map <Entry, Entry> _fullHashes	= new HashMap<> (15000000);		// Key: hash of the whole sentence. Value: list of sentence ids.
	private Map <Entry, Entry> _lowHashes	= new HashMap<> (15000000);		// Key: hash of the first half of the sentence. Value: list of sentence ids.
	private Map <Entry, Entry> _highHashes	= new HashMap<> (15000000);		// Key: hash of the last half of the sentence. Value: list of sentence ids.
	private Map <Entry, Entry> _simHashes	= new HashMap<> (15000000);		// Key: hash of the whole sentence - 1 word + the whole sentence. Value: list of sentence ids.
	private Map <Entry, Entry> _idsToHashes	= new HashMap<> (15000000);		// Key: sentence id. Value: hashes of the first and last halfs of the sentence.
	private Map <Entry, Entry> _duplicates	= new HashMap<> ();				// Key: sentence id. Value: list of duplicate sentence ids.
	private Map <Entry, Entry> _lengthCount	= new HashMap<> ();				// Key: sentence length. Value: count of sentences of this length.

	private void readFile (String file, Visitor visitor) throws Exception {
		FileReader fr = new FileReader (file);
		BufferedReader reader = new BufferedReader (fr);

		System.out.println ("Reading file " + file);

		int count = 0;
		for (String line; (line = reader.readLine()) != null;) {
			if (count++ % 10000 == 0) System.out.println (line);
			visitor.visit (line);

			if (count >= LIMIT) break;
		}

		System.out.println ("Done");

		reader.close();
		fr.close();
	}

//	private void writeFile (String file, Set <Entry> entries) throws Exception {
//		FileWriter fr = new FileWriter (file);
//		BufferedWriter writer = new BufferedWriter (fr);
//
//		System.out.println ("Writing file " + file);
//
//		for (Entry entry : entries) {
//			writer.write ("");
//		}
//
//		for (String line; (line = reader.readLine()) != null;) {
//			if (count++ % 10000 == 0) System.out.println (line);
//			visitor.visit (line);
//
//			if (count >= LIMIT) break;
//		}
//
//		System.out.println ("Done");
//
//		reader.close();
//		fr.close();
//	}

	private boolean store (Map <Entry, Entry> map, int key, int value, EntryFinder finder) {
		finder.setKey (key);
		Entry entry = map.get (finder);

		if (entry == null) {
			entry = new Entry (key, value);
			map.put (entry, entry);
		}
		else {
			entry.add (value);
		}

		return entry.size() > 1;
	}

	private void increment (Map <Entry, Entry> map, int key, EntryFinder finder) {
		finder.setKey (key);
		Entry entry = map.get (finder);

		if (entry == null) {
			entry = new Entry (key, 1);
			map.put (entry, entry);
		}
		else {
			entry.increment();
		}
	}

	private int countSentences (int length, EntryFinder finder) {
		int count = 0;

		for (int i = 0; i < 3; i++) {
			Entry lengthEntry = _lengthCount.get (finder.setKey (length - 1 + i));
			if (lengthEntry != null) {
				count += lengthEntry.getEntries()[0];
			}
		}


		return count;
	}

	private boolean retrieve (Map <Entry, Entry> map, int key, int value, EntryFinder finder) {
		finder.setKey (key);
		Entry entry = map.get (finder);

		if (entry == null) {
			entry = new Entry (key, value);
			map.put (entry, entry);
		}
		else {
			entry.add (value);
		}

		return entry.size() > 1;
	}

	private void hashSentence (int lineId, String[] words, EntryFinder finder) {
		for (int i = 1; i <= words.length * 2; i++) {	// Iterate over all words + twice for the whole sentence (placing '*' at the beginning and end)
			StringBuilder sb = new StringBuilder();
			int hash = 0;
			int k = (i - 1) / 2;

			for (int j = 1; j <= k; j++) {
				hash = hash * 31 + words[j].hashCode();
				sb.append (words[j]).append (" ");
			}

			if (k < words.length - 1 || i % 2 != 0) {
				hash = hash * 31 + "*".hashCode();
				sb.append ("*").append (" ");
			}

			for (int j = k + 1; j < words.length; j++) {
				if (i / 2 != j || i % 2 != 0) {
					hash = hash * 31 + words[j].hashCode();
					sb.append (words[j]).append(" ");
				}
			}

//			System.out.println (sb);
			store (_simHashes,  hash,  lineId, finder);
		}
	}

	private void packDuplicates() {
		Map <Entry, Entry> packed = new HashMap<>();

		for (Iterator <Entry> iterator = _fullHashes.keySet().iterator(); iterator.hasNext();) {
			Entry entry = iterator.next();
			iterator.remove();

			if (entry.size() > 1) {
				int[] ids = entry.getEntries();
				int key = ids[0];
				Entry duplicate = new Entry (key, entry.getEntries());
				packed.put (duplicate, duplicate);
			}
		}

		_duplicates = packed;
		_fullHashes = null;
	}

	private void packMaps() {
		_lowHashes.clear();
		_highHashes.clear();
		_idsToHashes.clear();

		_lowHashes = null;
		_highHashes = null;
		_idsToHashes = null;

		for (Iterator <Entry> iterator = _simHashes.keySet().iterator(); iterator.hasNext(); ) {
			Entry entry = iterator.next();
			if (entry.size() == 1) {
				iterator.remove();
			}
		}
	}

	private void printMap (String title, Map <Entry, Entry> map) {
		System.out.println("-----  " + title + "  -----");
		for (Entry entry : map.keySet()) {
			System.out.println (entry);
		}
	}

	private int calcSimilarities (boolean print) {
		System.out.println ("Calculating similarities");
		Map <Entry, Entry> pairs = new HashMap<>();
		EntryFinder finder = new EntryFinder();

		for (Entry entry : _duplicates.values()) {
			int[] ids = entry.getEntries();
			for (int i = 0; i < entry.size() - 1; i++) {
				for (int j = i+1; j < entry.size(); j++) {
					int key = Math.min (ids[i], ids[j]);
					int val = Math.max (ids[i], ids[j]);
					store (pairs, key, val, finder);
				}
			}
		}

		for (Iterator <Entry> iterator = _simHashes.keySet().iterator(); iterator.hasNext(); ) {
			Entry entry = iterator.next();
			iterator.remove();

			if (entry.size() > 1) {
				int[] ids = entry.getEntries();
				for (int i = 0; i < entry.size() - 1; i++) {
					for (int j = i+1; j < entry.size(); j++) {
						Entry duplicatesA = _duplicates.get (finder.setKey (ids[i]));
						int[] dupsA = (duplicatesA != null) ? duplicatesA.getEntries() : new int[] {ids[i]};

						Entry duplicatesB = _duplicates.get (finder.setKey (ids[j]));
						int[] dupsB = (duplicatesB != null) ? duplicatesB.getEntries() : new int[] {ids[j]};

						for (int a = 0; a < dupsA.length; a++) {
							for (int b = 0; b < dupsB.length; b++) {
								int key = Math.min (dupsA[a], dupsB[b]);
								int val = Math.max (dupsA[a], dupsB[b]);
								store (pairs, key, val, finder);
							}
						}
					}
				}
			}
		}

//		Set <Tuple> tuples = new TreeSet<>();
		int count = 0;

		for (Entry entry : pairs.keySet()) {
			int key = entry.getKey();
			int[] ids = entry.getEntries();

			for (int i = 0; i < ids.length; i++) {
				int id = ids[i];
//				Tuple tuple = new Tuple (key, id);
//
//				if (tuples.contains (tuple)) {
//					System.out.println ("Error: " + tuple);
//				}
//				else {
//					tuples.add (tuple);
//				}

				++count;
			}
		}

		// pairs.get (new EntryFinder().setKey(1490))

		if (print) {
//			for (Tuple tuple : tuples) {
//				System.out.println (tuple);
//			}
//			System.out.println ("(" + key + ", " + id + ")");
		}

		System.out.println ("Similar pairs = " + count);
//		System.out.println ("Similar pairs = " + count + ". Tuples = " + tuples.size());
		return count;
	}

	private class HashCalculator implements Visitor {
		private final EntryFinder _finder = new EntryFinder();

		@Override
		public void visit (String line) {
			String[] words = line.split ("\\s");
			int lineId = Integer.parseInt (words[0]);

			int lowHash  = 0;
			int highHash = 0;
			int fullHash = 0;

			for (int i = 1; i < words.length; i++) {
				fullHash  = fullHash  * 31 + words[i].hashCode();
			}

			if (store (_fullHashes,  fullHash,  lineId, _finder)) {
//				System.out.println ("Duplicate: " + line);
				return;
			}

			for (int i = 1; i <= MEDIAN; i++) {
				lowHash  = lowHash  * 31 + words[i].hashCode();
				highHash = highHash * 31 + words[words.length-i].hashCode();
			}

			store (_lowHashes,  lowHash,  lineId, _finder);
			store (_highHashes, highHash, lineId, _finder);

			store (_idsToHashes, lineId, lowHash,  _finder);
			store (_idsToHashes, lineId, highHash, _finder);

			increment (_lengthCount, words.length - 1, _finder);
		}
	}

	private class SimilarityTester implements Visitor {
		private final EntryFinder _finder = new EntryFinder();

		@Override
		public void visit (String line) {
			String[] words = line.split ("\\s");
			int lineId = Integer.parseInt(words[0]);

			int count = countSentences (words.length - 1, _finder);

			Entry entry = _idsToHashes.get (_finder.setKey (lineId));

			if (entry == null) {
				return;		// Was a duplicate, so we skip it
			}

			_idsToHashes.remove (entry);

			int lowHash  = entry.getEntries()[0];
			int highHash = entry.size() > 1 ? entry.getEntries()[1] : lowHash;

			Entry lowEntry  = _lowHashes.get  (_finder.setKey (lowHash));
			Entry highEntry = _highHashes.get (_finder.setKey (highHash));

			if (lowEntry.getEntries().length > 1 || highEntry.getEntries().length > 1) {	// We can check the sentence lengths as well
				if (count > 1) {
					hashSentence (lineId, words, _finder);
				}
				else {
					System.out.println ("SKIPPING " + (words.length - 1));
				}
			}

			if (lowEntry.getEntries().length > 2) {		// We can remove this sentence ID if there are more than 1 left
				lowEntry.remove (lineId);
			}

			if (highEntry.getEntries().length > 2) {	// We can remove this sentence ID if there are more than 1 left
				highEntry.remove (lineId);
			}
		}
	}

	private class SimilarityPrinter implements Visitor {
		private final BufferedWriter _writer;
		private final Map <Entry, Entry> _pairs;
		private final EntryFinder _finder = new EntryFinder();

		private SimilarityPrinter (String file, Map <Entry, Entry> pairs) throws Exception {
			FileWriter fr = new FileWriter (file);
			_writer = new BufferedWriter (fr);
			_pairs = pairs;
		}

		@Override
		public void visit (String line) {
			String[] words = line.split ("\\s");
			int lineId = Integer.parseInt (words[0]);
			_finder.setKey (lineId);

			Entry entry = _idsToHashes.get (_finder);

			if (entry == null) {
				return;		// Was a duplicate, so we skip it
			}

			_idsToHashes.remove (entry);

			int lowHash  = entry.getEntries()[0];
			int highHash = entry.getEntries()[1];

			Entry lowEntry  = _lowHashes.get  (_finder.setKey (lowHash));
			Entry highEntry = _highHashes.get (_finder.setKey (highHash));

			if (lowEntry.getEntries().length > 1 || highEntry.getEntries().length > 1) {	// We can check the sentence lengths as well
				hashSentence (lineId, words, _finder);
			}

			if (lowEntry.getEntries().length > 2) {		// We can remove this sentence ID if there are more than 1 left
				lowEntry.remove (lineId);
			}

			if (highEntry.getEntries().length > 2) {	// We can remove this sentence ID if there are more than 1 left
				highEntry.remove (lineId);
			}
		}
	}

	private interface Visitor {
		public void visit (String line);
	}

	private static class Tuple implements Comparable <Tuple> {
		private final int _a;
		private final int _b;

		private Tuple (int a, int b) {
			if (a <= b) {
				_a = a;
				_b = b;
			}
			else {
				_a = b;
				_b = a;
			}
		}

		@Override
		public boolean equals (Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Tuple that = (Tuple) o;
			return _a == that._a && _b == that._b;
		}

		@Override
		public int hashCode() {
			return 31 * _a + _b;
		}

		@Override
		public int compareTo (Tuple that) {
			return _a < that._a ? -1 : _a > that._a ? 1 : _b < that._b ? -1 : _b > that._b ? 1 : 0;
		}

		@Override
		public String toString() {
			return "(" + _a + ", " + _b + ")";
		}
	}

	private static class Entry {
		private static final int INCREMENT_STEP = 1;

		private final int _key;
		private int[] _entries = new int[INCREMENT_STEP];
		private int _count;

		public Entry (int key, int firtValue) {
			_key = key;
			_entries[0] = firtValue;
			_count = 1;
		}

		public Entry (int key, int[] values) {
			_key = key;
			_entries = values;
			_count = _entries.length;
		}

		public void increment() {
			_entries[0] = _entries[0] + 1;
		}

		public void add (int value) {
			if (!contains (value)) {
				if (_count >= _entries.length) {
					_entries = Arrays.copyOf (_entries, _count + INCREMENT_STEP);
				}

				_entries [_count++] = value;
			}
		}

		private boolean contains (int value) {
			for (int i = 0; i < _count; i++) {
				if (_entries[i] == value) return true;
			}

			return false;
		}

		public void remove (int value) {
			for (int i = 0; i < _count; i++) {
				if (_entries[i] == value) {
					for (int j = i + 1; j < _count; j++) {
						_entries[j-1] = _entries[j];
					}

					if (--_count <= _entries.length - INCREMENT_STEP) {
						_entries = Arrays.copyOf (_entries, _count);

					}

					return;
				}
			}
		}

		public int getKey() {
			return _key;
		}

		public int[] getEntries() {
			return _entries;
		}

		public int size() {
			return _count;
		}

		@Override
		public boolean equals (Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Entry entry = (Entry) o;
			return _key == entry._key;
		}

		@Override
		public int hashCode() {
			return _key;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < _count; i++) {
				if (i > 0) sb.append (',');
				sb.append (_entries[i]);
			}
			return _key + " = {" + sb.toString() + "} size = " + _entries.length;
		}
	}

	public static class EntryFinder {
		private int _key;

		public EntryFinder setKey (int key) {
			_key = key;
			return this;
		}

		@Override
		public boolean equals (Object o) {
			if (this == o) return true;
			if (o == null || !(o instanceof Entry)) return false;

			Entry that = (Entry) o;
			return _key == that._key;
		}

		@Override
		public int hashCode() {
			return _key;
		}
	}
}
