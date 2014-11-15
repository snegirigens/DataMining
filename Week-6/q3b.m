function q3b
	byers = [28,145; 38,115; 43,83; 50,130; 50,90; 50,60; 50,30; 55,118; 63,88; 65,140];
	notby = [23,40; 25,125; 29,97; 33,22; 35,63; 42,57; 44, 105; 55,63; 55,20; 64,37];

	for i = 1 : size (byers, 1)
		printf ("%d, %d = %d\n", byers(i,1), byers(i,2), descent (byers(i,:)));
	end;

	printf "----------\n");

	for i = 1 : size (notby, 1)
		printf ("%d, %d = %d\n", notby(i,1), notby(i,2), descent (notby(i,:)));
	end;
end;

function y = descent (p)
	a = p(1);
	s = p(2);

	if (a < 45)
		if (s < 110)
			y = 0;
		else
			y = 1;
		end;

	else
		if (s < 75)
			y = 0;
		else
			y = 1;
		end;
	end;
end;