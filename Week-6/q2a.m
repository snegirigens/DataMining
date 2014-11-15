function q2a
	M = [1, 2; 3, 4; 5, 6];
	N = [1, 2, 3, 4; 5, 6, 7, 8];

	keys = [];
	values = [];

	printf ("------- 1. Matrix M --------\n");

	[keysTemp, valuesTemp] = map (M, N, 0);

	keys = [keys(:,:); keysTemp(:,:)];
	values = [values(:,:); valuesTemp(:,:)];

	printf ("------- 2. Matrix N --------\n");

	[keysTemp, valuesTemp] = map (N', M', 1);

	keys = [keys(:,:); keysTemp(:,:)];
	values = [values(:,:); valuesTemp(:,:)];

	reduce (keys, values, size (M,1), size (N,2));
end;

function P = reduce (keys, values, x, z)	% x - num of rows in M, z - num of cols in N
	P = [];

	disp (keys);
%	disp (values);

	for i = 1 : x
		for k = 1 : z
			p = [];

			for t = 1 : size (keys,1)
				if (keys(t,1) == i && keys(t,2) == k)
					printf ("Found key (%d, %d) = (%d, %d, %d)\n", keys(t,1), keys(t,2), values(t,1), values(t,2), values(t,3));
				else
%					printf ("Skipping (%d, %d)\n", keys(t,1), keys(t,2));
				end;
			end;
		end;
	end;

end;

function [keys, values] = map (M, N, name)
	keys = [];
	values = [];

	for i = 1 : size (M,1)
		[keys, values] = mapKey (M, i, size (N, 2), name);

%		printf ("--- Keys ---\n");
%		disp (keys);

%		printf ("--- Values ---\n");
%		disp (values);
	end;
end;

function [keys, values] = mapKey (matrix, i, kk, name)
	keys = [];
	values = [];

	for k = 1 : kk
		for j = 1 : size (matrix,2)
			if (name == 0)
				keys = [keys(:,:); [i, k]];
			else
				keys = [keys(:,:); [k, i]];
			end;
			values = [values(:,:); [name, j, matrix(i,j)]];
		end;
	end;
end;

