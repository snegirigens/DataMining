function q9
	M = [0,0,0,1/4; 1,0,0,1/4; 0,1,0,1/4; 0,0,1,1/4]
%	M = M';

	r = [1/4; 1/4; 1/4; 1/4];
	teleport = [1/4; 1/4; 1/4; 1/4];

	beta = 1;
	e = 1e-6;

	for i = 1 : 20
		temp = beta * M * r + (1 - beta) * teleport;
		err = sum ((temp - r).^2);
		r = temp;

		if (err < e)
			break;
		end;
	end;

	r
end;
