package com.example.javaapplication.Activity;

import com.google.gson.annotations.SerializedName;

public class RequestBodyChangePassword{

	@SerializedName("newPasswordConfirmation")
	private String newPasswordConfirmation;

	@SerializedName("oldPassword")
	private String oldPassword;

	@SerializedName("maxRow")
	private String maxRow;

	@SerializedName("newPassword")
	private String newPassword;

	@SerializedName("userId")
	private String userId;

	@SerializedName("version")
	private String version;

	@SerializedName("key")
	private String key;

	@SerializedName("username")
	private String username;

	public void setNewPasswordConfirmation(String newPasswordConfirmation){
		this.newPasswordConfirmation = newPasswordConfirmation;
	}

	public String getNewPasswordConfirmation(){
		return newPasswordConfirmation;
	}

	public void setOldPassword(String oldPassword){
		this.oldPassword = oldPassword;
	}

	public String getOldPassword(){
		return oldPassword;
	}

	public void setMaxRow(String maxRow){
		this.maxRow = maxRow;
	}

	public String getMaxRow(){
		return maxRow;
	}

	public void setNewPassword(String newPassword){
		this.newPassword = newPassword;
	}

	public String getNewPassword(){
		return newPassword;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
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