function q2b
	% y = a*x + b*m/n + c*k/n
	% y = x/(1-B^2) + c*m/n, where c = B/(1+B)

	% Rk = B*y/k + (1-B)/n
	% Rm = B*Rk*k/m + (1-B)/n
	% y = x + B*m*Rm + (1-B)/n --> removing last term as non-significant
	% y = x + B^3*y + B^2*(1-B)*k/n + B*(1-B)*m/n
	% y = x/(1-B^3) + (B^2*(1-B)/(1-B^3)) * k/n + (B*(1-B)/(1-B^3)) * m/n

	B = 0.85;

	a = 1/(1-B^3)
	b = B*(1-B)/(1-B^3)
	c = B^2*(1-B)/(1-B^3)

end;
