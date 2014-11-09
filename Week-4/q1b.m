function q1b
	m = [2/7, 3/7, 6/7];

	a1 = [-.937, .312, .156];
	a2 = [1.125, .500, -.625];
	a3 = [.485, -.485, .728] ;
	a4 = [-.288, -.490, .772] ;

	printf ('a1 = %d\n', orthonormal (m, a1));
	printf ('a2 = %d\n', orthonormal (m, a2));
	printf ('a3 = %d\n', orthonormal (m, a3));
	printf ('a4 = %d\n', orthonormal (m, a4));
end;

function [res] = orthonormal (m, n)
	res = abs (1.0 - sum (n.*n)) < 0.001;

	if (res == 0)
		return;
	end;

	res = abs (0.0 - sum (m.*n)) < 0.001;
end;
