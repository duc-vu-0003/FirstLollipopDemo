package com.ducva.lollipopdemo.api;

import java.util.ArrayList;

import org.apache.http.Header;

import android.content.Context;

import com.ducva.lollipopdemo.model.BaseModel;
import com.ducva.lollipopdemo.model.MyAlbum;
import com.ducva.lollipopdemo.utils.DebugLog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GetMyAlbums extends BaseRequest {
	
	Context ctx;
	LastfmMyAlbumsOnResult lastfmMyAlbumsOnResult;
	ArrayList<BaseModel> lstAlbums;
	
	public GetMyAlbums(Context ctx) {
		// TODO Auto-generated constructor stub
		this.ctx = ctx;
		lstAlbums = new ArrayList<BaseModel>();
	}

	public void execute(int page) {
		if (!isNetworkConnected(ctx)) {
			return;
		}

		RequestParams params = new RequestParams();
		params.add(LastfmConstant.REQUEST_PARAM_KEY_FOR_PAGE, page + "");

		BaseRequest.getLastfm(LastfmConstant.FUNCTION_GET_MY_ALBUMS, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] header, byte[] content) {
				// TODO Auto-generated method stub
				String result = new String(content);
				DebugLog.e("statusCode: " + statusCode + " content: " + result);

				JsonParser parser = new JsonParser();
				JsonObject response = (JsonObject) parser.parse(result);

				if (response.has(LastfmConstant.RESPONSE_KEY_FOR_ALBUMS)) {
					JsonObject obj = response.get(LastfmConstant.RESPONSE_KEY_FOR_ALBUMS).getAsJsonObject();
					JsonArray array = obj.get(LastfmConstant.RESPONSE_KEY_FOR_ALBUM).getAsJsonArray();
					
					if(array != null && array.size() > 0){
						for (int i = 0; i < array.size(); i++) {
							JsonObject item = array.get(i).getAsJsonObject();
							MyAlbum myAlbum = new MyAlbum(item);
							lstAlbums.add(myAlbum);
						}
					}
					
					if (lastfmMyAlbumsOnResult != null) {
						lastfmMyAlbumsOnResult.onLastfmMyAlbumsResult(true, lstAlbums);
					}
				} else {
					
					if (lastfmMyAlbumsOnResult != null) {
						lastfmMyAlbumsOnResult.onLastfmMyAlbumsResult(true, new ArrayList<BaseModel>());
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] header, byte[] content, Throwable error) {
				// TODO Auto-generated method stub
				if (lastfmMyAlbumsOnResult != null) {
					lastfmMyAlbumsOnResult.onLastfmMyAlbumsResult(false, new ArrayList<BaseModel>());
				}
			}
		});
	}

	public void setLastfmMyAlbumsOnResult(LastfmMyAlbumsOnResult lastfmMyAlbumsOnResult) {
		this.lastfmMyAlbumsOnResult = lastfmMyAlbumsOnResult;
	}

	public interface LastfmMyAlbumsOnResult {
		void onLastfmMyAlbumsResult(boolean result, ArrayList<BaseModel> info);
	}
}
