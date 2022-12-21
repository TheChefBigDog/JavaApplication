package com.example.javaapplication.Activity.Model.Data.LoginModel;

import com.google.gson.annotations.SerializedName;

public class LoginModel{

	@SerializedName("mobileDeviceId")
	private Object mobileDeviceId;

	@SerializedName("responseSlack")
	private Object responseSlack;

	@SerializedName("responseStatus")
	private String responseStatus;

	@SerializedName("responseMessage")
	private String responseMessage;

	@SerializedName("accessToken")
	private String accessToken;

	@SerializedName("user")
	private User user;

	@SerializedName("responseId")
	private String responseId;

	@SerializedName("refreshToken")
	private Object refreshToken;

	public void setMobileDeviceId(Object mobileDeviceId){
		this.mobileDeviceId = mobileDeviceId;
	}

	public Object getMobileDeviceId(){
		return mobileDeviceId;
	}

	public void setResponseSlack(Object responseSlack){
		this.responseSlack = responseSlack;
	}

	public Object getResponseSlack(){
		return responseSlack;
	}

	public void setResponseStatus(String responseStatus){
		this.responseStatus = responseStatus;
	}

	public String getResponseStatus(){
		return responseStatus;
	}

	public void setResponseMessage(String responseMessage){
		this.responseMessage = responseMessage;
	}

	public String getResponseMessage(){
		return responseMessage;
	}

	public void setAccessToken(String accessToken){
		this.accessToken = accessToken;
	}

	public String getAccessToken(){
		return accessToken;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public void setResponseId(String responseId){
		this.responseId = responseId;
	}

	public String getResponseId(){
		return responseId;
	}

	public void setRefreshToken(Object refreshToken){
		this.refreshToken = refreshToken;
	}

	public Object getRefreshToken(){
		return refreshToken;
	}
}