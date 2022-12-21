package com.example.javaapplication.Activity.Model.Data.RefreshToken;

import com.google.gson.annotations.SerializedName;

public class RefreshToken{

	@SerializedName("responseSlack")
	private Object responseSlack;

	@SerializedName("responseStatus")
	private String responseStatus;

	@SerializedName("responseMessage")
	private Object responseMessage;

	@SerializedName("accessToken")
	private String accessToken;

	@SerializedName("responseId")
	private String responseId;

	@SerializedName("refreshToken")
	private String refreshToken;

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

	public void setResponseMessage(Object responseMessage){
		this.responseMessage = responseMessage;
	}

	public Object getResponseMessage(){
		return responseMessage;
	}

	public void setAccessToken(String accessToken){
		this.accessToken = accessToken;
	}

	public String getAccessToken(){
		return accessToken;
	}

	public void setResponseId(String responseId){
		this.responseId = responseId;
	}

	public String getResponseId(){
		return responseId;
	}

	public void setRefreshToken(String refreshToken){
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken(){
		return refreshToken;
	}
}