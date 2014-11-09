B = 1
T = 3;
p = [ 0; 0; 0];
r = [ T/3; T/3; T/3];
M = [ 0, 0, 1; 0.5, 0, 0; 0.5, 1, 0 ];
i = 0;
b = 0;
c = 0;

#while (sum (abs(p - r) > 0.00001) > 0)
for i = 1 : 5
	i
	p = r;
	r = B * M * r;
	s = sum (r);
	r = r + (T - s)/3
	
	if i == 4
		b = r(2);
		c = r(3);
	end;
	
	sum(r)
#	fflush;
end;
#r
#sum(r)

printf ("1. b = 9/16 (%f = 9/16) (%d)\n", r(2), (r(2) == 9/16));
printf ("2. c = 9/8 (%f = 9/8) (%d)\n", r(3), (r(3) == 9/8));
printf ("3. b = 11/16 (%f = 11/16) (%d)\n", b, (b == 11/16));
printf ("4. c = 11/8 (%f = 11/8) (%d)\n", c, (c == 11/8));
