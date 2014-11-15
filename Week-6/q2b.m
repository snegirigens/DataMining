function q2b
%	figure;
%	hold on;
%	axis ([0, 10, 0, 10]);

	pos = [1, 6; 1, 8; 3, 4; 3, 6; 3, 8; 5, 10; 7, 8; 7, 10];
	neg = [1, 2; 1, 4; 3, 2; 5, 4; 5, 6; 5, 8; 7, 4; 7, 6];

	b = -2;
	w = [-1; 1];

%	showPlot (w, b, pos, neg);

	for i = 1 : size (pos, 1)
		slack = calcSlack (pos(i,:), w, b, 1);

		if (slack != 0)
			printf ("Point (%d, %d) = %.2f\n", pos(i,1), pos(i,2), slack);
		end;
	end;

	for i = 1 : size (neg, 1)
		slack = calcSlack (neg(i,:), w, b, -1);

		if (slack != 0)
			printf ("Point (%d, %d) = %.2f\n", neg(i,1), neg(i,2), slack);
		end;
	end;
end;

function slack = calcSlack (p, w, b, side)
	dist = distance (p, w, b - side);

	if (sign (dist) != side)
		slack = abs (dist * sqrt (w(1)^2 + w(2)^2));
%		slack = dist / distance (p, w, b);
	else
		slack = 0;
	end;
end;

function dist = distance (p, w, b)
	dist = (p * w + b) / sqrt (w' * w);
end;

function showPlot (w, b, pos, neg)
 	plot (pos(:,1), pos(:,2), "bx");
	plot (neg(:,1), neg(:,2), "ro");

	drawLine (w, b,  0, 'k');
	drawLine (w, b,  1, 'b');
	drawLine (w, b, -1, 'r');
end;

function drawLine (w, b, margin, color)
	x = [0, 10];
	y = (margin - w(1) * x - b) / w(2);

	plot (x, y, color);
end;
