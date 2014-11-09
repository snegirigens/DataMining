package com.sneg;

/**
 * Author:	sneg
 * Date:	18.10.14
 * Time:	22:59
 */
public class W2Q6 {
	private static final Point A = new Point (0, 0);
	private static final Point B = new Point (100, 40);

	private static double L1Norm (Point a, Point b) {
		return Math.abs (a.getX() - b.getX()) + Math.abs (a.getY() - b.getY());
	}

	private static double L2Norm (Point a, Point b) {
		return Math.sqrt (Math.pow (Math.abs (a.getX() - b.getX()), 2) + Math.pow (Math.abs (a.getY() - b.getY()), 2));
	}

	public static void main (String[] args) {
		Point p1 = new Point (61, 10);
		Point p2 = new Point (50, 18);
		Point p3 = new Point (51, 15);
		Point p4 = new Point (61, 8);

		for (Point point : new Point[] {p1, p2, p3, p4}) {
			double dist1A = L1Norm (point, A);
			double dist1B = L1Norm (point, B);

			double dist2A = L2Norm (point, A);
			double dist2B = L2Norm (point, B);

			String cluster1 = dist1A <= dist1B ? "A" : "B";
			String cluster2 = dist2A <= dist2B ? "A" : "B";

			boolean same = dist1A <= dist1B && dist2A <= dist2B || dist1A > dist1B && dist2A > dist2B;

			System.out.println (
				point + ". L1 = " + cluster1 + ". L2 = " + cluster2 + ". Same = " + cluster1.equals (cluster2)
			);

//			System.out.println (
//				point + ". L1-A = " + dist1A + ". L1-B = " + dist1B + ". L2-A = " + dist2A + ". L2-B = " + dist2B +
//				". Same = " + same
//			);
		}

	}

	private static class Point {
		private final int _x;
		private final int _y;

		private Point (int x, int y) {
			_x = x;
			_y = y;
		}

		public int getX() {
			return _x;
		}

		public int getY() {
			return _y;
		}

		public String toString() {
			return "(" + _x + ", " + _y + ")";
		}
	}
}
