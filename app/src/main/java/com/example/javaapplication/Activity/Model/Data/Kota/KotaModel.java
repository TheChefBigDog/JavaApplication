package com.example.javaapplication.Activity.Model.Data.Kota;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class KotaModel{

	@SerializedName("responseSlack")
	private String responseSlack;

	@SerializedName("responseStatus")
	private String responseStatus;

	@SerializedName("responseMessage")
	private String responseMessage;

	@SerializedName("list")
	private ArrayList<ListKotaItem> list;

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

	public void setList(ArrayList<ListKotaItem> list){
		this.list = list;
	}

	public ArrayList<ListKotaItem> getList(){
		return list;
	}

	public void setResponseId(String responseId){
		this.responseId = responseId;
	}

	public String getResponseId(){
		return responseId;
	}
}