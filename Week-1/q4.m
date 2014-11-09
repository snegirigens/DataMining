function q4 (n)
	isPrime(n)
end;

function p = isPrime (n)
	if n == 1 || n == 2
		p = 1;
		return;
	end;
	
	if mod (n, 2) == 0
		p = 0;
		return;
	end;
	
	for i = 3:2:(sqrt(n))
		if mod (n, i) == 0
			p = 0;
			return;
		end;
	end;
	
	p = 1;
end;