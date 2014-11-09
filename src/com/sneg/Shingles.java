package com.sneg;

import java.util.HashSet;
import java.util.Set;

/**
 * Author:	sneg
 * Date:	18.10.14
 * Time:	13:03
 */
public class Shingles {
	public static Set <String> set (String document, int k) {
		Set <String> shingles = new HashSet <String>();

		for (int i = 0; i < document.length() - k + 1; i++) {
			shingles.add (document.substring (i, i + k));
			System.out.println (document.substring (i, i + k));
		}

		return shingles;
	}

	private static Set <String> intersection (Set <String> set1, Set <String> set2) {
		Set <String> inter = new HashSet <String>();

		for (String s : set1) {
			if (set2.contains (s)) {
				inter.add (s);
			}
		}
		return inter;
	}

	private static Set <String> union (Set <String> set1, Set <String> set2) {
		Set <String> union = new HashSet <String> (set1);
		union.addAll (set2);

		return union;
	}

	public static void main (String[] args) {
		String s1 = "ABRACADABRA";
		String s2 = "BRICABRAC";

		Set <String> shing1 = set (s1, 2);
		Set <String> shing2 = set (s2, 2);

		System.out.println (s1 + " has " + shing1.size() + " shingles: " + shing1);
		System.out.println (s2 + " has " + shing2.size() + " shingles: " + shing2);

		Set <String> inter = intersection (shing1, shing2);
		Set <String> union = union (shing1, shing2);

		System.out.println ("Common shingles: " + inter + " (" + inter.size() + ")");
		System.out.println ("Unique shingles: " + union + " (" + union.size() + ")");
		System.out.println ("Jaccard similarity: " + ((float) inter.size() / union.size()));
	}
}
