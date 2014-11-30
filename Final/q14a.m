function q14a
	V = [-0.57, -0.09; -0.11, 0.7; -0.57, -0.09; -0.11, 0.7; -0.57, -0.09]

	d = [5,0,0,0,0];
	q = [0,5,0,0,0];
	p = [0,0,0,0,4];

	a = d * V;
	b = q * V;
	c = p * V;

	printf ("cos(a,b) = %f\n", (a*b' / (sqrt (a*a') * sqrt (b*b'))));
	printf ("cos(a,c) = %f\n", (a*c' / (sqrt (a*a') * sqrt (c*c'))));
	printf ("cos(b,c) = %f\n", (b*c' / (sqrt (b*b') * sqrt (c*c'))));

end;
