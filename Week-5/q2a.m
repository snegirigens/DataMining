function q2a
	more off;

	bid = [0.10; 0.09; 0.08; 0.07; 0.06];
	budget = [1.0; 2.0; 3.0; 4.0; 5.0];
	ctr = [0.015, 0.010, 0.005; 0.016, 0.012, 0.006; 0.017, 0.014, 0.007; 0.018, 0.015, 0.008; 0.019, 0.016, 0.010;];
	cts = zeros (size (budget, 1), 1)

	totalCTs = 101;		% Click-throughs
	earning = 0.0;

	while (totalCTs > 0)
		bidders = allocate (bid, ctr, budget);
		disp (bidders);

		while (continuePhase (totalCTs, budget, bid, bidders))
			event = rand();

			for i = 1 : 3
				b = bidders(i);

				if (ctr(b,i) >= event && totalCTs > 0)
					cts(b) += 1;
					totalCTs -= 1;
					earning += bid(b);
					budget(b) -= bid(b);
					if (budget(b) < 1e-10)
						budget(b) = 0.0;
					end;
					printf ("Clicked through for bidder %d, position %d, budget = %1.2f\n", b, i, budget(b));
				end;
			end;
		end;

		for i = 1 : 3
			b = bidders(i);
			if (budget(b) < bid(b))
				budget(b) = 0.0;
			end;
		end;

		printf ("Earned %1.2f, unspent CTS = %d\n", earning, totalCTs);
%		input ("Enter ...");
	end;

	printf ("Click-throughs:\n");
	disp (cts);

	printf ("Remaining budget:\n");
	disp (budget);

	printf ("Final earning %1.2f\n", earning);
end;

function result = continuePhase (totalCTs, budget, bid, bidders)
	a = budget(bidders(1)) - bid(bidders(1));
	b = budget(bidders(2)) - bid(bidders(2));
	c = budget(bidders(3)) - bid(bidders(3));
	result = a >= 0.0 && b >= 0.0 && c >= 0.0 && totalCTs > 0;
end;

function bidders = allocate (bid, ctr, budget)
	printf ("Budget:\n");
	disp (budget);

	bidders = zeros (3,1);
	yield = zeros (size (budget, 1), 3);

	for i = 1 : 3
		yield(:,i) = ctr(:,i) .* bid .* [budget > 1e-10];
	end;

	printf ("Yield:\n");
	disp (yield);

	[v,p] = max (yield(:,1));
	bidders(1) = p;
	yield(p,2) = 0;

	[v,p] = max (yield(:,2));
	bidders(2) = p;
	yield(p,3) = 0;

	[v,p] = max (yield(:,3));
	bidders(3) = p;
end;
