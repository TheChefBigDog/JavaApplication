package com.example.javaapplication.Activity;

import com.google.gson.annotations.SerializedName;

public class RequestProvinceBody{
	@SerializedName("username")
	private String username;

	@SerializedName("version")
	private String version;

	public void setVersion(String version){
		this.version = version;
	}

	public String getVersion(){
		return version;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}
}