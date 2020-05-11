package com.fasten.wp4.fpsot.retryable;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.fasten.wp4.database.client.model.WebHook;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

@Component
public class WebHookNotify {

	private static final Logger logger = LoggerFactory.getLogger(WebHookNotify.class);

	@Retryable(maxAttempts=15, value=IllegalStateException.class, backoff = @Backoff(delay = 30000,multiplier=2))
	public void notifyConsumerService(String url, String json) {

		OkHttpClient client = new OkHttpClient();
		RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
		try {
			Response response = client.newCall(request).execute();
			if(response.code()!=200) {
				throw new IllegalStateException("Server not ready");
			}
		} catch (Throwable t) {
			throw new IllegalStateException("Server not ready");
		}
	}

	@Recover
	public void recover(IllegalStateException e){
		logger.error("Could not send WebHook");
	}

}
