B = 0.7
T = 3;			# In the task we need to normalize to 3 for some reason
p = [ 0; 0; 0];
r = [ T/3; T/3; T/3];
M = [ 0, 0, 0; 0.5, 0, 0; 0.5, 1, 1 ];

while (p - r != 0)
#for i = 1 : 5
	p = r;
	r = B * M * r;
	s = sum (r);
	r = r + (T - s)/3
	sum(r)
end;

printf ("1. a + c = %f\n", r(1) + r(3));
printf ("2. b + c = %f\n", r(2) + r(3));
printf ("3. a + c = %f\n", r(1) + r(3));
printf ("4. a + b = %f\n", r(1) + r(2));
