package com.huahuostudio.sd.httpclient;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ShundaoRestClient {

	//apiµÿ÷∑
	private static final String BASE_URL = "http://sd.huahuostudio.com";
	
	public static final String REPONSE_STATUS = "status";
	public static final String REPONSE_STATUS_OK = "ok";
	public static final String REPONSE_CODE = "code";
	public static final String REPONSE_CONTENT = "content";

	private static AsyncHttpClient client = new AsyncHttpClient();

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	private static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	private static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void processRouteList(RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		post("/api/getroutes", params, responseHandler);
	}
}
