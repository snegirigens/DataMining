/**
 * Author:	Sneg (Leonid Snegirev)
 * Created:	23.10.14
 */

package com.sneg;

import com.sun.deploy.util.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class SimSenLSH {
	private static final String FILE = "resources/sentences1.txt";
	private static final double J = 0.8;
//	private static final int MEDIAN = 5;
	private static final int LIMIT = 1000000000;

	public static void main (String[] args) throws Exception {
		SimSenLSH test = new SimSenLSH();

		test.readFile(FILE);
	}

	private Map <String, Integer> _dictionary = new HashMap<>();	// Key: word. Value: id
	private Map <Tuple, Map <Integer, int[]>> _index = new HashMap<>();		// Key: Tuple (wordId, position, suffix). Value: Map <sentencesID, words IDs>

	private void readFile (String file) throws Exception {
		FileReader fr = new FileReader (file);
		BufferedReader reader = new BufferedReader (fr);

		System.out.println ("Reading file " + file);

		int count = 0;
		for (String line; (line = reader.readLine()) != null;) {
//			if (count++ % 10000 == 0) System.out.println (line);
			visitLine(line);

			if (count >= LIMIT) break;
		}

		System.out.println ("Done");
		printDictionary();
		printIndex();

		reader.close();
		fr.close();
	}

	private void visitLine (String line) {
		String[] words = line.split ("\\s");
		int sentenceID = Integer.parseInt (words[0]);


		int[] sentence = new int [words.length - 1];		// Sentence encoded as a list of word Ids

		for (int i = 1; i < words.length; i++) {
			int wordId = getWordId (words[i]);
			sentence[i - 1] = wordId;
		}

		int prefixLength = getPrefixLength (words.length - 1);
		System.out.println (line + " (" + prefixLength + ")");

		for (int i = 0; i < prefixLength; i++) {
			int pos = i + 1;
			int k = sentence.length - pos;
			Tuple tuple = new Tuple (sentence[i], pos, k);

			Map <Integer, int[]> sentences = _index.get (tuple);

			if (sentences == null) {
				sentences = new HashMap<>();
				_index.put (tuple, sentences);
			}

			for (Integer id : sentences.keySet()) {
				int[] candidate = sentences.get (id);
				if (isDistance (sentence, candidate, 1)) {
					System.out.println (id + " ==> " + sentenceID);
				}
			}

			sentences.put (sentenceID, sentence);
		}
	}

	private int getWordId (String word) {
		Integer id = _dictionary.get (word);

		if (id == null) {
			id = _dictionary.size() + 1;
			_dictionary.put (word, id);
		}

		return id;
	}

	private int getPrefixLength (int L) {
		return (int) Math.floor ((1 - J) * L + 1);
	}


	private boolean isDistance (int[] words1, int[] words2, int distance) {
		if (words1.length - words2.length > distance || words2.length - words1.length > distance) {
			return false;
		}

		int[] a = words1.length >= words2.length ? words1 : words2;
		int[] b = a == words1 ? words2 : words1;

		for (int i = 0, j = 0; i < a.length; i++) {
			if (a[i] != (b[j])) {
				if (distance-- <= 0) {
					return false;
				}
			}
			else {
				j++;
			}
		}

		return true;
	}

	private void printDictionary() {
		for (String word : _dictionary.keySet()) {
			System.out.println (word + " = " + _dictionary.get (word));
		}
	}

	private void printIndex() {
		for (Tuple tuple : _index.keySet()) {
			Map <Integer, int[]> sentence = _index.get(tuple);

			for (Integer id : sentence.keySet()) {
				int[] array= sentence.get(id);
				StringBuilder sb = new StringBuilder();
				for (int i : array) {
					if (sb.length() > 0) sb.append (",");
					sb.append (i);

				}
				System.out.println (tuple + " = " + id + " {" + sb + "}");
			}
		}
	}

	private static class Tuple implements Comparable <Tuple> {
		private final int _a;
		private final int _b;
		private final int _c;

		private Tuple (int a, int b, int c) {
			_a = a;
			_b = b;
			_c = c;
		}

		@Override
		public boolean equals (Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Tuple that = (Tuple) o;
			return _a == that._a && _b == that._b && _c == that._c;
		}

		@Override
		public int hashCode() {
			return 31 * _a + 17 * _b + _c;
		}

		@Override
		public int compareTo (Tuple that) {
			return _a < that._a ? -1 : _a > that._a ? 1 : _b < that._b ? -1 : _b > that._b ? 1 : _c < that._c ? -1 : _c > that._c ? 1 : 0;
		}

		@Override
		public String toString() {
			return "(" + _a + ", " + _b + ", " + _c + ")";
		}
	}
}
