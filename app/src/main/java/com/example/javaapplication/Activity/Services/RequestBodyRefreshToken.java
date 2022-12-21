package com.example.javaapplication.Activity.Services;

import com.google.gson.annotations.SerializedName;

public class RequestBodyRefreshToken{

	@SerializedName("app")
	private String app;

	@SerializedName("version")
	private String version;

	@SerializedName("username")
	private String username;

	public String getApp(){
		return app;
	}

	public String getVersion(){
		return version;
	}

	public String getUsername(){
		return username;
	}


	public void setApp(String app) {
		this.app = app;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}