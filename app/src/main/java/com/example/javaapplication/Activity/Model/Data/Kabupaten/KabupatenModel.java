package com.example.javaapplication.Activity.Model.Data.Kabupaten;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class KabupatenModel{

	@SerializedName("responseSlack")
	private String responseSlack;

	@SerializedName("responseStatus")
	private String responseStatus;

	@SerializedName("responseMessage")
	private String responseMessage;

	@SerializedName("list")
	private ArrayList<ListItemKabupaten> list;

	@SerializedName("responseId")
	private String responseId;

	public void setResponseSlack(String responseSlack){
		this.responseSlack = responseSlack;
	}

	public String getResponseSlack(){
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

	public void setList(ArrayList<ListItemKabupaten> list){
		this.list = list;
	}

	public ArrayList<ListItemKabupaten> getList(){
		return list;
	}

	public void setResponseId(String responseId){
		this.responseId = responseId;
	}

	public String getResponseId(){
		return responseId;
	}
}