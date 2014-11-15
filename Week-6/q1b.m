function q1b
	x1 = [5;4];
	x2 = [8;3];

	x3 = [7;2];
	x4 = [3;3];

	[w, b] = solve (x1, x2);
	dist1 = distance (x3, w, b);
	dist2 = distance (x4, w, b);

	printf ("Line through x1 & x2: w = [%.2f, %.2f], b = %.2f\n", w(1), w(2), b);
	printf ("Distance of x3 (%d, %d) = %.2f\n", x3(1), x3(2), dist1);
	printf ("Distance of x4 (%d, %d) = %.2f\n", x4(1), x4(2), dist2);
	printf ("Margin = %.2f\n", min (dist1, dist2));

	y1 = -b / w(2);
	b2 = -1 * (w * x3);
	y2 = -b2 / w(2);

	y3 = y2 + (y1 - y2) / 2;
	b3 = -1 * (w * [0; y3]);

	x0 = 8;
	y0 = -1 * (w(1) * x0 + b3) / w(2);

	printf ("w = [%.2f, %.2f]. b = %.2f\n", w(1), w(2), b3);

	printf ("-------------------\n");

	figure; hold on;
	axis ([0,10,0,10]);

	plot ([0, x2(1)], [y1, x2(2)], "b-");
	plot ([0, x3(1)], [y2, x3(2)], "r-");
	plot ([0, x0], [y3, y0], "k-", 'MarkerSize', 4);

	plot (x1(1), x1(2), "bo", 'MarkerSize', 3);
	plot (x2(1), x2(2), "bo", 'MarkerSize', 3);

	plot (x3(1), x3(2), "ro", 'MarkerSize', 3);
	plot (x4(1), x4(2), "ro", 'MarkerSize', 3);

	[w, b] = solve (x3, x4);
	dist1 = distance (x1, w, b);
   	dist2 = distance (x2, w, b);

	printf ("Line through x3 & x4: w = [%.2f, %.2f], b = %.2f\n", w(1), w(2), b);
	printf ("Distance of x1 (%d, %d) = %.2f\n", x1(1), x1(2), dist1);
	printf ("Distance of x2 (%d, %d) = %.2f\n", x2(1), x2(2), dist2);
	printf ("Margin = %.2f\n", min (dist1, dist2));
end;

function [w, b] = solve (x1, x2)
	w = zeros (1,2);
	w(2) = 1 /((x2(2) - x1(2)) / (x1(1) - x2(1)) + 1);
	w(1) = 1 - w(2);
	b = -1 * w*x1;
end;

function dist = distance (p, w, b)
	dist = abs ((w * p + b) / sqrt (w(1)^2 + w(2)^2));
%	dist = abs ((w * p + b) / sqrt (w * w'));
end;
