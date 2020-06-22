package com.fasten.wp4.database.repository;

interface CustomizedDeliveryRepository {
	int[][] retrieveDistanceMatrix(int[] distributionCenters, int[] remoteStations);
	int[][] retrieveTimeMatrix(int[] distributionCenters, int[] remoteStations);
}