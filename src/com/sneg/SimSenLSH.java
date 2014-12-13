/**
 * Author:	Sneg (Leonid Snegirev)
 * Created:	23.10.14
 */

package com.sneg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This implementation finds 429493281 similar pairs of sentences (actual number is 429493953)
 * It runs for 9.5 minutes and consumes about 3GB heap space
 */
public class SimSenLSH {
	private static final String FILE = "resources/sentences.txt";
	private static final int SPLIT = 4;
	private static final int LIMIT = 1000000000;

	public static void main (String[] args) throws Exception {
		SimSenLSH test = new SimSenLSH();

		test.readFile (FILE);
	}

	private Map <String, Integer> _dictionary = new HashMap<>();	// Key: word. Value: id
	private Map <Integer, Map <Integer, int[]>> _lowIndex  = new HashMap<>();	// Key: hash of sentence's first 5 words. Value: Map <sentencesID, words IDs>
	private Map <Integer, Map <Integer, int[]>> _highIndex = new HashMap<>();	// Key: hash of sentence's last 5 words. Value: Map <sentencesID, words IDs>

	private long _count = 0;

	private void readFile (String file) throws Exception {
		FileReader fr = new FileReader (file);
		BufferedReader reader = new BufferedReader (fr);

		System.out.println ("Reading file " + file);

		int count = 0;
		for (String line; (line = reader.readLine()) != null;) {
			if (count++ % 10000 == 0) System.out.println (line);
			visitLine (line);

			if (count >= LIMIT) break;
		}

		System.out.println ("Done " + count + " lines: " + _count + " similar pairs");
//		printDictionary();
//		printIndex (_lowIndex);
//		printIndex (_highIndex);

		reader.close();
		fr.close();
	}

	private void visitLine (String line) {
		String[] words = line.split ("\\s");
		int sentenceID = Integer.parseInt (words[0]);

		int[] sentence  = new int [words.length - 1];		// Sentence encoded as a list of word Ids
		int[] lowWords  = new int [SPLIT];
		int[] highWords = new int [SPLIT];

		for (int i = 1; i < words.length; i++) {
			int wordId = getWordId (words[i]);
			sentence[i - 1] = wordId;

			if (i <= SPLIT) {
				lowWords[i - 1] = wordId;
			}

			if (i >= words.length - SPLIT) {
				highWords[i - (words.length - SPLIT)] = wordId;
			}
		}

		int lowHash  = hashWords (lowWords);
		int highHash = hashWords (highWords);

		Set <Integer> matches = new HashSet<>();	// We should not count identical sentences twice
		matches.addAll (testAndStore (_lowIndex,  lowHash,  sentence, sentenceID));
		matches.addAll (testAndStore (_highIndex, highHash, sentence, sentenceID));

		_count += matches.size();
	}

	private Set <Integer> testAndStore (Map <Integer, Map <Integer, int[]>> indexMap, int hash, int[] sentence, int sentenceID) {
		Map <Integer, int[]> candidates = indexMap.get (hash);
		Set <Integer> matches = new HashSet<>();

		if (candidates == null) {
			candidates = new HashMap<>();
			indexMap.put (hash, candidates);
		}

		for (Integer id : candidates.keySet()) {
			int[] candidate = candidates.get (id);
			if (isDistance (sentence, candidate, 1)) {
				matches.add (id);
			}
		}

		candidates.put (sentenceID, sentence);
		return matches;
	}

	private int hashWords (int[] words) {
//		printArray (words);
		int hash = 0;

		for (int i = 0; i < words.length; i++) {
			hash += 31 * words[i];
		}

		return hash;
	}

	private int getWordId (String word) {
		Integer id = _dictionary.get (word);

		if (id == null) {
			id = _dictionary.size() + 1;
			_dictionary.put (word, id);
		}

		return id;
	}

	private boolean isDistance (int[] words1, int[] words2, int distance) {
		if (words1.length - words2.length > distance || words2.length - words1.length > distance) {
			return false;
		}

		int[] a = words1.length >= words2.length ? words1 : words2;
		int[] b = a == words1 ? words2 : words1;
		int i = 0, j = 0;

OUTER:	for (; i < a.length && j < b.length; i++, j++) {
			if (a[i] == b[j]) {
				continue;
			}

			if (i >= a.length - distance) {
				break;
			}

			for (int k = 1; k <= distance && i+k < a.length; k++) {
				for (int m = 0; m <= distance && j+m < b.length; m++) {
                    if (a[i+k] == b[j+m]) {
                        distance -= k;
						i += k;
						j += m;
                        continue OUTER;
                    }
                }
			}

			return false;
		}

		return a.length - i <= distance && b.length - j <= distance;
	}

	private void printDictionary() {
		for (String word : _dictionary.keySet()) {
			System.out.println (word + " = " + _dictionary.get (word));
		}
	}

	private void printIndex (Map <Integer, Map <Integer, int[]>> indexMap) {
		for (Integer hash : indexMap.keySet()) {
			Map <Integer, int[]> sentence = indexMap.get (hash);

			for (Integer id : sentence.keySet()) {
				int[] array= sentence.get(id);
				StringBuilder sb = new StringBuilder();
				for (int i : array) {
					if (sb.length() > 0) sb.append (",");
					sb.append (i);

				}
				System.out.println (hash + " = " + id + " {" + sb + "}");
			}
		}
	}

	private void printArray (int[] array) {
		StringBuilder sb = new StringBuilder().append('[');
		for (int i = 0; i < array.length; i++) {
			if (i > 0) sb.append (',');
			sb.append (array[i]);
		}
		sb.append (']');
		System.out.println (sb);
	}
}
