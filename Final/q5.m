function q5
	a = [2,1,1];
	b = [10,-7,1];

	cos = a*b' / (sqrt (a*a') * sqrt (b*b'));
	printf ("%f\n",cos);
end;
