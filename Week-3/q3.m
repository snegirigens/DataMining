function q3
	k = 5 * (8^2 + 7^2);

	X = findX ([25, 34, 47])
	err = abs (k - X);
	printf ("1. Error = %d\n", err);

	X = findX ([14, 35, 42])
	err = abs (k - X);
	printf ("2. Error = %d\n", err);

	X = findX ([22, 42, 62])
	err = abs (k - X);
	printf ("3. Error = %d\n", err);

	X = findX ([20, 49, 53])
	err = abs (k - X);
	printf ("4. Error = %d\n", err);
end;

function [X] = findX (a)
	x = [];

	for i = [1:3]
		t = a(i);
		x(i) = surprise (t);
	end;

	disp (x);

	X = median (x);
end;


function [X] = surprise (t)
	m = idivide ((75 - t), 10, "fix");
	if t % 10 <= 5
		m += 1;
	end;

	X = 75 * (2 * m - 1);
%	printf ("m = %d --> X = %d\n", m, X);
end;