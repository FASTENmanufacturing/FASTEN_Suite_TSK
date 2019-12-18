package com.github.adminfaces.starter.util;

import java.net.URI;
import java.net.URISyntaxException;

import com.fasten.wp4.database.client.invoker.ApiResponse;
import com.fasten.wp4.database.client.model.Prediction;
import com.fasten.wp4.predictive.client.model.ForecastingStudy;

public class ApiUtils {
	
	public static long getIdFromLocationHeader(ApiResponse<Object> apiResponse) throws URISyntaxException {
		URI uri = new URI(apiResponse.getHeaders().get("Location").get(0));
		String path = uri.getPath();
		String idStr = path.substring(path.lastIndexOf('/') + 1);
		long id = Long.parseLong(idStr);
		return id;
	}
	
}