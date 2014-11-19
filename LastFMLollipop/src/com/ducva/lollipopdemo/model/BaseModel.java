package com.ducva.lollipopdemo.model;

import com.ducva.lollipopdemo.api.LastfmConstant;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class BaseModel {
	public String name;
	public String imageLink;
	public String mID;
	public int playCount;

	public BaseModel() {

	}

	public BaseModel(String name, String imageLink, String mID, int playCount) {
		super();
		this.name = name;
		this.imageLink = imageLink;
		this.mID = mID;
		this.playCount = playCount;
	}
	
	public BaseModel(JsonObject data){
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
