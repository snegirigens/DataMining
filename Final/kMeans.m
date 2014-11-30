function kMeans
	C = [1,1; 4,4];	% Cx2 Centroids
	P = [1,1; 2,1; 2,2; 2,4; 3,3; 4,2; 4,4];	% Px2 Points

	K = zeros (size (C,1), 2);	% Centroids after recalculation

	n = size (P,1);
	k = size (C,1);
	belongsTo1 = zeros (n,1);	% First assignment
	belongsTo2 = zeros (n,1);	% Second assignment

	for i = 1 : n
		belongsTo1 (i) = getCentroid (P (i,:), C);
%		printf ("(%d, %d) belongs to %d\n", P (i,1), P (i,2), belongsTo1 (i));
	end;

	for i = 1 : k
%		printf ("Cluster %d. Centroid = (%d, %d) contains:\n", i, C(i,1), C(i,2));
		cluster = assignToCluster (P, belongsTo1, i);
%		disp (cluster);
		K(i,:) = recalculateCentroid (cluster, C(i,:));
	end;

%	printf ("New centroids:\n");
%	disp (K);
%	printf ("------------------------\n");

	for i = 1 : n
		belongsTo2 (i) = getCentroid (P (i,:), K);
%		printf ("(%d, %d) belongs to %d\n", P (i,1), P (i,2), belongsTo2 (i));
	end;

%	displayPlot (P, K);

	for i = 1 : n
		if (belongsTo1 (i) == belongsTo2 (i))
			printf ("Point %d (%d, %d) belongs to cluster %d\n", i, P (i,1), P (i,2), belongsTo1 (i));
		else
			printf ("Point %d (%d, %d) first belonged to cluster %d, afterwards to %d\n", i, P (i,1), P (i,2), belongsTo1 (i), belongsTo2 (i));
		end;
	end;

	printf ("New centroids:\n");
	disp (K);
end;

function runKMeans (P, C)
	n = size (P,1);		% Number of points
	k = size (C,1);		% Number of centroids (clusters)
	belongsTo1 = zeros (n,1);	% First assignment

	for i = 1 : n
		belongsTo1 (i) = getCentroid (P (i,:), C);
%		printf ("(%d, %d) belongs to %d\n", P (i,1), P (i,2), belongsTo1 (i));
	end;

	for i = 1 : k
%		printf ("Cluster %d. Centroid = (%d, %d) contains:\n", i, C(i,1), C(i,2));
		cluster = assignToCluster (P, belongsTo1, i);
%		disp (cluster);
		K(i,:) = recalculateCentroid (cluster, C(i,:));
	end;

%	printf ("New centroids:\n");
%	disp (K);
%	printf ("------------------------\n");

	for i = 1 : n
		belongsTo2 (i) = getCentroid (P (i,:), K);
%		printf ("(%d, %d) belongs to %d\n", P (i,1), P (i,2), belongsTo2 (i));
	end;

%	displayPlot (P, K);

	for i = 1 : n
		if (belongsTo1 (i) == belongsTo2 (i))
			printf ("Point %d (%d, %d) belongs to cluster %d\n", i, P (i,1), P (i,2), belongsTo1 (i));
		else
			printf ("Point %d (%d, %d) first belonged to cluster %d, afterwards to %d\n", i, P (i,1), P (i,2), belongsTo1 (i), belongsTo2 (i));
		end;
	end;

	printf ("New centroids:\n");
	disp (K);

end;

function c = getCentroid (p, C)	% Find closes centroid C to point p
	n = size (C,1);
	dist = zeros (n,1);

	for i = 1 : size (C,1)
		dist(i) = sqrt ((C(i,1) - p(1))^2 + (C(i,2) - p(2))^2);
	end;

%	printf ("Distance to centroids for point (%d, %d)\n", p(1), p(2));
%	disp (dist);
	[none, c] = min (dist);
end;

function cluster = assignToCluster (P, belongsTo, c)	% Recalculate centroid for cluster c based on assignment of points to cluster "belongsTo"
	cluster = [];

	for i = 1 : size (P,1)
		if (belongsTo(i) == c)
			cluster = [cluster(:,:); P(i,:)];
		end;
	end;
end;

function c = recalculateCentroid (P, original)	% recalculate centroid of points P (belonging to this cluster), or leave original centroid if no points
	if (size(P,1) > 0)
		c = [((max(P(:,1)) - min(P(:,1))) / 2 + min(P(:,1))), ((max(P(:,2)) - min(P(:,2))) / 2 + min(P(:,2)))];
	else
		c = original;
	end;
end;

function displayPlot (P, C)
	figure;
	hold on;
	axis ([0, 150, 0, 150]);

	plot (C(:,1), C(:,2), "bo", 'MarkerSize', 3);
	plot (P(:,1), P(:,2), "bx", 'MarkerSize', 2);
end;
