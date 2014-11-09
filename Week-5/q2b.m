function q2b
	result = test ([[7, 8]; [12,5]; [13, 10]; [16, 4]]);
	printf ("Result-1: %d\n\n", result);

	result = test ([[3, 3]; [10, 1]; [15, 14]; [20, 10]]);
	printf ("Result-2: %d\n\n", result);

	result = test ([[3, 15]; [13, 7]; [11, 5]; [17, 2]]);
	printf ("Result-3: %d\n\n", result);

	result = test ([[3, 3]; [10, 1]; [13, 10]; [16, 4]]);
	printf ("Result-4: %d\n\n", result);
end;

function result = test (P)
	C = [5, 10; 20, 5];
	Y = getPoints (P(1,:), P(2,:));
	B = getPoints (P(3,:), P(4,:));

	result = 1;
	belongsTo = zeros (4,1);

	for i = 1 : 4
		belongsTo (i) = getCentroid (Y (i,:), C);
		if (belongsTo (i) == 1)
			printf ("OK: yellow point (%d, %d) belongs to target cluster %d\n", Y (i,1), Y (i,2), belongsTo (i));
		else
			printf ("Failed: yellow point (%d, %d) belongs to wrong cluster %d\n", Y (i,1), Y (i,2), belongsTo (i));
			result = 0;
		end;
	end;

	printf ("------------------------\n");

	for i = 1 : 4
		belongsTo (i) = getCentroid (B (i,:), C);
		if (belongsTo (i) == 2)
			printf ("OK: blue point (%d, %d) belongs to target cluster %d\n", B (i,1), B (i,2), belongsTo (i));
		else
			printf ("Failed: blue point (%d, %d) belongs to wrong cluster %d\n", B (i,1), B (i,2), belongsTo (i));
			result = 0;
		end;
	end;
end;

function [P] = getPoints (ul, lr)		% Upper left and lower right
	P = zeros (4,2);
	P(1,:) = ul;
	P(2,:) = [lr(1), ul(2)];
	P(3,:) = [ul(1), lr(2)];
	P(4,:) = lr;
end;

function c = getCentroid (p, C)	% Find closes centroid C to point p
	n = size (C,1);
	dist = zeros (n,1);

	for i = 1 : size (C,1)
		dist (i) = sqrt ((C(i,1) - p(1))^2 + (C(i,2) - p(2))^2);
	end;

%	printf ("Distance to centroids for point (%d, %d)\n", p(1), p(2));
%	disp (dist);
	[none, c] = min (dist);
end;
