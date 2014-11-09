package com.sneg;

/**
 * Author:	sneg
 * Date:	19.10.14
 * Time:	20:47
 */
public class W2Q1 {
	private static double calcTriangular (double n) {
		return Math.pow (n, 2) * 2 + n * 4;
	}

	private static double calcTriples (double n, double m) {
		return m * 12 + n * 4;
	}

	private static boolean isValid (double n, double m, double s) {
		double h1 = calcTriangular (n);
		double h2 = calcTriples (n, m);
		double h = Math.min (h1, h2);

		boolean valid = h > (s - s/10) && h < (s + s/10);

		System.out.println ("N = " + n + ", M = " + m + ", S = " + s + ", h = " + h + ", valid = " + valid);
		return valid;
	}

	public static void main (String[] args) {
		isValid (4e4, 6e7, 3.2e9);
		isValid (1e5, 1e8, 1.2e9);
		isValid (1e5, 4e7, 8e8);
		isValid (6e4, 2e8, 7.2e9);

	}
}
