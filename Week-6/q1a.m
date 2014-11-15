function q1a
	M = [1, 2, 3, 4; 5, 6, 7, 8; 9, 10, 11, 12; 13, 14, 15, 16];
	v = [1; 2; 3; 4];

	for i = 1 : size (M,1)
		for j = 1 : size (M,2)
			printf ("%d : %d\n", i, M(i,j) * v(j));
		end;
	end;
end;
