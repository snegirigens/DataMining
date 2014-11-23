function q3a
	L = [0, 1, 1, 0; 1, 0, 0, 0; 0, 0, 0, 1; 0, 0, 1, 0];

	h = [1; 1; 1; 1];
	a = [0; 0; 0; 0];

	for i = 1 : 2
		[h, a] = rank (L, h);
	end;

	h
	a
end;

function [h, a] = rank (L, h)
	a = L' * h;
	norm = max (a);
	a = a / norm;
	h = L * a;
	norm = max (h);
	h = h / norm;
end;
