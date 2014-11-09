package com.sneg;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Author:	sneg
 * Date:	20.10.14
 * Time:	21:36
 */
public class FindSimilarSentences {
	public static final String INPUT_FILE	= "D:/Courses/DataMining/Home/sentences.txt";
	public static final String DICTIONARY	= "D:/Courses/DataMining/Home/dictionary.txt";

	private Set <String> _words = new HashSet <String> (1500000);
	private Map <String, Integer> _dictionary = new HashMap <String, Integer> (1500000);

	private void readFile (String fileName, LineVisitor visitor) {
		FileReader file = null;
		BufferedReader reader = null;

		try {
			file = new FileReader (fileName);
			reader = new BufferedReader (file);

			int count = 0;

			for (String line; (line = reader.readLine()) != null;) {
				visitor.visit (line);

				if (count++ % 5000 == 0) {
					System.out.println (line);
				}
			}

			System.out.println ("Unique words: " + _words.size());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
				file.close();
			}
			catch (IOException e) {}
		}
	}

	private void saveDictionary() {
		FileWriter file = null;
		TreeSet <String> words = new TreeSet <String> (_words);

		try {
			file = new FileWriter (DICTIONARY);

			System.out.println ("Saving dictionary");
			StringBuilder sb = new StringBuilder (10000000);
			for (String word : words) {
				sb.append (word).append ("\n");
			}
			file.write (sb.toString());
			System.out.println ("Done");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				file.close();
			}
			catch (IOException e) {}
		}
	}

	private void process() {
//		prog.readFile();
//		prog.saveDictionary();

		readFile (DICTIONARY, new DictionaryVisitor());
		System.out.println (_dictionary.size());
	}

	public static void main (String[] args) {
		new FindSimilarSentences().process();
	}

	private interface LineVisitor {
		public void visit (String line);
	}

	public class SentenceVisitor implements LineVisitor {
		public void visit (String line) {
			String[] words = line.split ("\\s+");
			for (int i = 1; i < words.length; i++) {
				_words.add (words[i]);
			}
		}
	}

	public class DictionaryVisitor implements LineVisitor {
		private int _count = 1;

		public void visit (String line) {
			_dictionary.put (line, _count++);
		}
	}
}
