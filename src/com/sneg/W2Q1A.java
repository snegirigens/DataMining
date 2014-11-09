package com.sneg;

/**
 * Author:	sneg
 * Date:	19.10.14
 * Time:	20:47
 */
public class W2Q1A {
	private static final double ITEMS	= 1e6;
	private static final double FITEMS	= 2.5e5;
	private static final double FPAIRS	= 1e6;

	private static double calcMemory (double buckets, double load) {
		return FPAIRS * load * 12 + FITEMS * 4 + buckets / 32;
	}

	private static double calcBuckets (double memory) {
		return (memory - ITEMS * 4) / 4;
	}

	private static boolean isValid (double s, double p) {
		double buckets = calcBuckets (s);
		double load = (p + FPAIRS) / buckets;
		double required = calcMemory (buckets, load);

		boolean valid = required > (s - s/10) && required < (s + s/10);

		System.out.println (
			"S = " + s + ", P = " + p + ", Buckets = " + buckets + ", Load = " + load + ", Mem = " + required + ", Valid = " + valid
		);
		return valid;
	}

	public static void main (String[] args) {
		isValid (1e8, 1.2e8);
		isValid (1e8, 2e8);
		isValid (3e8, 7.5e8);
		isValid (1e9, 1e10);

//		isValid (1e8, 1.2e8);
//		isValid (1e8, 5.4e8);
//		isValid (1e9, 3.5e10);
//		isValid (1e9, 2e10);
	}
}
