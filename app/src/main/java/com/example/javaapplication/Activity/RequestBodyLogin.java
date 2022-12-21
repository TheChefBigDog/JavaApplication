package com.example.javaapplication.Activity;

import com.google.gson.annotations.SerializedName;

public class RequestBodyLogin{

	@SerializedName("password")
	private String password;

	@SerializedName("version")
	private String version;

	@SerializedName("key")
	private String key;

	@SerializedName("username")
	private String username;

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setVersion(String version){
		this.version = version;
	}

	public String getVersion(){
		return version;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getKey(){
		return key;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}
}