package com.sneg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:	sneg
 * Date:	19.10.14
 * Time:	15:34
 */
public class FrequentItemSets {
	private static List <List <Integer>> buildBaskets (int capacity) {
		List <List <Integer>> baskets = new ArrayList <List <Integer>>();

		for (int i = 1; i <= capacity; i++) {
			List <Integer> basket = new ArrayList <Integer>();
			baskets.add (basket);

			for (int j = 1; j <= i; j++) {
				if (i % j == 0) {
					basket.add (j);
				}
			}
		}

		return baskets;
	}

	private static Map <Integer, Integer> firstPass (List <List <Integer>> baskets) {
		Map <Integer, Integer> frequentItems = new HashMap <Integer, Integer>();

		for (List <Integer> basket : baskets) {
			for (Integer item : basket) {
				Integer count = frequentItems.get (item);
				if (count == null) count = 0;
				frequentItems.put (item,  ++count);
			}
		}

		return frequentItems;
	}

	private static boolean isFrequent (Integer item, Map <Integer, Integer> frequentItems) {
		return frequentItems.get (item) != null && frequentItems.get (item) > 0;
	}

	private static boolean isFrequent (Tuple tuple, Map <Tuple, Integer> frequentTuples) {
		return frequentTuples.get (tuple) != null && frequentTuples.get (tuple) > 0;
	}

	private static Map <Tuple, Integer> secondPass (List <List <Integer>> baskets, Map <Integer, Integer> frequentItems) {
		Map <Tuple, Integer> frequentTuples = new LinkedHashMap <Tuple, Integer>();

		for (List <Integer> basket : baskets) {
			for (int i = 0; i < basket.size() - 1; i++) {
				Integer a = basket.get (i);
				if (!isFrequent (a, frequentItems)) continue;

				for (int j = i+1; j < basket.size(); j++) {
					Integer b = basket.get (j);
					if (!isFrequent (b, frequentItems)) continue;
					Tuple tuple = new Tuple (a, b);
					Integer count = frequentTuples.get(tuple);
					if (count == null) count = 0;
					frequentTuples.put (tuple, ++count);
				}
			}
		}

		return frequentTuples;
	}

	private static Map <Triple, Integer> thirdPass (List <List <Integer>> baskets, Map <Tuple, Integer> frequentTuples) {
		Map <Triple, Integer> frequentTriples = new LinkedHashMap <Triple, Integer>();

		for (List <Integer> basket : baskets) {
			for (int i = 0; i < basket.size() - 2; i++) {
				Integer a = basket.get (i);

				for (int j = i+1; j < basket.size() - 1; j++) {
					Integer b = basket.get (j);
					if (!isFrequent (new Tuple (a, b), frequentTuples)) continue;

					for (int k = j+1; k < basket.size(); k++) {
						Integer c = basket.get (k);
						Triple triple = new Triple (a, b, c);
						Integer count = frequentTriples.get (triple);
						if (count == null) count = 0;
						frequentTriples.put (triple, ++count);
					}
				}
			}
		}

		return frequentTriples;
	}

	private static void printConfidentTuples (Map <Tuple, Integer> frequentTuples, Map <Integer, Integer> frequentItems) {
		for (Tuple tuple : frequentTuples.keySet()) {
			Integer count = frequentTuples.get (tuple);
			int a = tuple.getA();
			int b = tuple.getB();

			int countA = frequentItems.get (a);
			int countB = frequentItems.get (b);

			if (countA == count) {
				System.out.println ("{" + a + "} -> " + b + " = 100%");
			}
			if (countB == count) {
				System.out.println ("{" + b + "} -> " + a + " = 100%");
			}
		}
	}

	private static void printConfidentTriples (Map <Triple, Integer> frequentTriples, Map <Tuple, Integer> frequentTuples) {
		for (Triple triple : frequentTriples.keySet()) {
			Integer count = frequentTriples.get (triple);
			int a = triple.getA();
			int b = triple.getB();
			int c = triple.getC();

			int countAB = frequentTuples.get (new Tuple (a, b));
			int countAC = frequentTuples.get (new Tuple (a, c));
			int countBC = frequentTuples.get (new Tuple (b, c));

			if (countAB == count) {
				System.out.println ("{" + a + "," + b + "} -> " + c + " = 100% (" + count + ")");
			}
			if (countAC == count) {
				System.out.println ("{" + a + "," + c + "} -> " + b + " = 100% (" + count + ")");
			}
			if (countBC == count) {
				System.out.println ("{" + b + "," + c + "} -> " + a + " = 100% (" + count + ")");
			}
		}
	}

	private static float getConfidence (Tuple tuple, Integer item, Map <Triple, Integer> frequentTriples, Map <Tuple, Integer> frequentTuples) {
		Integer countTuple = frequentTuples.get (tuple);
		if (countTuple == null) return 0.0f;

		Triple triple = new Triple (tuple.getA(), tuple.getB(), item);
		Integer countTriple = frequentTriples.get (triple);

		float confidence = (float) countTriple / (float) countTuple;
		System.out.println ("{" + tuple.getA() + "," + tuple.getB() + "} -> " + item + " = " + confidence);

		return confidence;
	}

	public static void main (String[] args) {
		List <List <Integer>> baskets = buildBaskets (100);

		for (List <Integer> basket : baskets) {
			System.out.println (basket);
		}

		Map <Integer, Integer> frequentItems = firstPass (baskets);
		System.out.println (frequentItems);

		Map <Tuple, Integer> frequentTuples = secondPass (baskets, frequentItems);
		System.out.println (frequentTuples);
//		printConfidentTuples (frequentTuples, frequentItems);

		Map <Triple, Integer> frequentTriples = thirdPass (baskets, frequentTuples);
		System.out.println (frequentTriples);
		printConfidentTriples (frequentTriples, frequentTuples);

		getConfidence (new Tuple (8, 10), 20, frequentTriples, frequentTuples);
		getConfidence (new Tuple (1, 2), 4, frequentTriples, frequentTuples);
		getConfidence (new Tuple (8, 12), 96, frequentTriples, frequentTuples);
	}

	private static class Tuple {
		private final int _i;
		private final int _j;

		private Tuple (int i, int j) {
			_i = i;
			_j = j;
		}

		public int getA() {
			return _i;
		}

		public int getB() {
			return _j;
		}

		public boolean equals (Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Tuple tuple = (Tuple) o;

			if (_i != tuple._i) return false;
			if (_j != tuple._j) return false;

			return true;
		}

		public int hashCode() {
			return 31 * _i + _j;
		}

		public String toString () {
			return "(" + _i + "," + _j + ")";
		}
	}

	private static class Triple {
		private final int _a;
		private final int _b;
		private final int _c;

		private Triple (int a, int b, int c) {
			_a = a;
			_b = b;
			_c = c;
		}

		public int getA() {
			return _a;
		}

		public int getB() {
			return _b;
		}

		public int getC() {
			return _c;
		}

		public boolean equals (Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Triple triple = (Triple) o;

			if (_a != triple._a) return false;
			if (_b != triple._b) return false;
			if (_c != triple._c) return false;

			return true;
		}

		public int hashCode() {
			int result = _a;
			result = 31 * result + _b;
			result = 31 * result + _c;
			return result;
		}

		public String toString() {
			return "(" + _a + "," + _b + "," + _c + ")";
		}
	}
}
