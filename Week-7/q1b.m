function q1b
	M = [0, 1/2, 1/2, 0; 1, 0, 0, 0; 0, 0, 0, 1; 0, 0, 1, 0];
	M = M';

	r = [1/4; 1/4; 1/4; 1/4];
	teleport = [2/3; 1/3; 0; 0];

	beta = 0.7;
	e = 1e-6;

	for i = 1 : 100
		temp = beta * M * r + (1 - beta) * teleport;
		err = sum ((temp - r).^2)
		r = temp;

		if (err < e)
			break;
		end;
	end;

	r
end;
