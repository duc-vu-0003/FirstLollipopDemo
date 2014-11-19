package com.ducva.lollipopdemo.api;

import java.util.ArrayList;

import org.apache.http.Header;

import android.content.Context;

import com.ducva.lollipopdemo.model.BaseModel;
import com.ducva.lollipopdemo.model.MyArtist;
import com.ducva.lollipopdemo.utils.DebugLog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GetMyArtists extends BaseRequest{
	Context ctx;
	LastfmMyArtistsOnResult lastfmMyArtistsOnResult;
	ArrayList<BaseModel> lstArtists;
	
	public GetMyArtists(Context ctx) {
		// TODO Auto-generated constructor stub
		this.ctx = ctx;
		lstArtists = new ArrayList<BaseModel>();
	}

	public void execute(int page) {
		if (!isNetworkConnected(ctx)) {
			return;
		}

		RequestParams params = new RequestParams();
		params.add(LastfmConstant.REQUEST_PARAM_KEY_FOR_PAGE, page + "");

		BaseRequest.getLastfm(LastfmConstant.FUNCTION_GET_MY_ARTISTS, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] header, byte[] content) {
				// TODO Auto-generated method stub
				String result = new String(content);
				DebugLog.e("statusCode: " + statusCode + " content: " + result);

				JsonParser parser = new JsonParser();
				JsonObject response = (JsonObject) parser.parse(result);

				if (response.has(LastfmConstant.RESPONSE_KEY_FOR_ARTISTS)) {
					JsonObject obj = response.get(LastfmConstant.RESPONSE_KEY_FOR_ARTISTS).getAsJsonObject();
					JsonArray array = obj.get(LastfmConstant.RESPONSE_KEY_FOR_ARTIST).getAsJsonArray();
					
					if(array != null && array.size() > 0){
						for (int i = 0; i < array.size(); i++) {
							JsonObject item = array.get(i).getAsJsonObject();
							MyArtist myArtist = new MyArtist(item);
							lstArtists.add(myArtist);
						}
					}
					
					if (lastfmMyArtistsOnResult != null) {
						lastfmMyArtistsOnResult.onLastfmMyArtistsResult(true, lstArtists);
					}
				} else {
					
					if (lastfmMyArtistsOnResult != null) {
						lastfmMyArtistsOnResult.onLastfmMyArtistsResult(true, new ArrayList<BaseModel>());
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] header, byte[] content, Throwable error) {
				// TODO Auto-generated method stub
				if (lastfmMyArtistsOnResult != null) {
					lastfmMyArtistsOnResult.onLastfmMyArtistsResult(false, new ArrayList<BaseModel>());
				}
			}
		});
	}

	public void setLastfmMyArtistsOnResult(LastfmMyArtistsOnResult lastfmMyArtistsOnResult) {
		this.lastfmMyArtistsOnResult = lastfmMyArtistsOnResult;
	}

	public interface LastfmMyArtistsOnResult {
		void onLastfmMyArtistsResult(boolean result, ArrayList<BaseModel> info);
	}
}
