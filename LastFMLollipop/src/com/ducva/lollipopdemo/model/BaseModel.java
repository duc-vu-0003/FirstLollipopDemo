package com.ducva.lollipopdemo.model;

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
}
