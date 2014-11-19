package com.ducva.lollipopdemo.model;

import com.ducva.lollipopdemo.api.LastfmConstant;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MyArtist extends BaseModel{
	
	public MyArtist(JsonObject data){
		try {
			this.name = data.get(LastfmConstant.RESPONSE_KEY_FOR_NAME).getAsString();
			this.mID = data.get(LastfmConstant.RESPONSE_KEY_FOR_MBID).getAsString();
			this.playCount = data.get(LastfmConstant.RESPONSE_KEY_FOR_PLAY_COUNT).getAsInt();
			JsonArray images = data.get(LastfmConstant.RESPONSE_KEY_FOR_IMAGE).getAsJsonArray();
			JsonObject image = images.get(3).getAsJsonObject();
			this.imageLink = image.get(LastfmConstant.RESPONSE_KEY_FOR_IMAGE_LINK).getAsString();
		} catch (Exception e) {
		}
	}
}
