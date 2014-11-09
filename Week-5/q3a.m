function q3a
	x = [0,0];
	y = [10,10];

	V = [x; y];
	P = [1,6; 3,7; 4,3; 7,7; 8,2; 9,5];
	S = ['a'; 'b'; 'c'; 'd'; 'e'; 'f'];
	O = [];

	while (size (P,1) > 0)
		[V,P,S,O] = fartherstPoint (V,P,S,O);
	end;

	printf ("Order of points:\n");
	disp (O);
end;

function [V, P, S, O] = fartherstPoint (V, P, S, O)
	n = size (P,1);
	dist = zeros (n,1);

	for i = 1 : n			% Over points in P
		m = size (V,1);
		L2 = zeros (m,1);	% Distances of point in P from every point in V

%		printf ("Point %d, %d:\n", P(i,1), P(i,2));

		for j = 1 : m		% Over points in V
			L2(j) = norm (P(i,:), V(j,:));
		end;

%		disp (L2);
		dist(i) = min (L2);
	end;

	[max, p] = max (dist);
	printf ("Fartherst point is %s (%d, %d)\n", S(p), P(p,1), P(p,2));

	V = [V(:,:); P(p,:)];
	O = [O(:); S(p)];
	P(p,:) = [];
	S(p) = [];
end;

function norm = norm (a, b)
	norm = sqrt ((a(1) - b(1))^2 + (a(2) - b(2))^2);
end;

