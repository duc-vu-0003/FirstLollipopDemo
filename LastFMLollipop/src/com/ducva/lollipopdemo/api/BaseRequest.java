package com.ducva.lollipopdemo.api;

import com.ducva.lollipopdemo.utils.DebugLog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.net.ConnectivityManager;

public class BaseRequest {

	/** The client. */
	private static AsyncHttpClient client = new AsyncHttpClient();

	/**
	 * Check internet connection
	 * 
	 * @param context
	 *            the context
	 * @return true if Network is connected.
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm
				.getActiveNetworkInfo().isConnected());
	}

	public static void initNetworkConfig() {
		client.setTimeout(30000);
	}

	public static void getLastfm(String function, RequestParams params, AsyncHttpResponseHandler responseHandler) {

		String requestUrl = LastfmConstant.BASE_URL + function;
		
		params.add(LastfmConstant.REQUEST_PARAM_KEY_FOR_API_KEY, LastfmConstant.LAST_FM_API_KEY);
		params.add(LastfmConstant.REQUEST_PARAM_KEY_FOR_USER, LastfmConstant.USER);
		params.add(LastfmConstant.REQUEST_PARAM_KEY_FOR_LIMIT, LastfmConstant.ITEM_PER_PAGE);
		params.add(LastfmConstant.REQUEST_PARAM_KEY_FOR_FORMAT, LastfmConstant.RESULT_TYPE);
		
		DebugLog.e("params: " + params.toString() + "  requestUrl: " + requestUrl);
		client.get(requestUrl, params, responseHandler);

	}
}
