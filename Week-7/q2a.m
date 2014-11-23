function q2a
	J = 0.2;
	S = {"abcef"; "acdeg"; "bcdefg"; "adfg"; "bcdfgh"; "bceg"; "cdfg"; "abcd"};

	map = struct();

	for i = 1 : size (S,1)
		probe = deblank (S{i});
		pref = prefix (probe, J);

		for j = 1 : pref
			key = S{i}(j);

			if (isfield (map, key))
				map = setfield (map, key, [map.(key); probe]);
			else
				map = setfield (map, key, [probe]);
			end;
%			printf ("%d. %s (%d) --> prefix = %s\n", i, S{i}, pref, S{i}(j));

    	end;
	end;

	result = struct();

	for i = [1, 3, 6]
		probe = deblank (S{i});
		pref = prefix (probe, J);

		printf ("Probe: %s\n", probe);

		for j = 1 : pref
			key = probe(j);
			candidates = getfield (map, key);

			for k = 1 : size (candidates,1)
				test = deblank (candidates(k,:));

				if (strcmp (test, probe) == 0)
					printf ("\t%s --> %s (%s)\n", key, test, probe);
				end;
			end;
		end;

	end;
end;

function pref = prefix (s, J)
%	printf ("%s (%d)\n", s, length (s));
	pref = floor (J * length (s)) + 1;
end;
