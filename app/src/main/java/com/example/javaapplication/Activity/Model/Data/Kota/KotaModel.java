package com.example.javaapplication.Activity.Model.Data.Kota;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class KotaModel{

	@SerializedName("responseSlack")
	private Object responseSlack;

	@SerializedName("responseStatus")
	private String responseStatus;

	@SerializedName("responseMessage")
	private Object responseMessage;

	@SerializedName("list")
	private List<ListKotaItem> list;

	@SerializedName("responseId")
	private String responseId;

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

	public void setList(List<ListKotaItem> list){
		this.list = list;
	}

	public List<ListKotaItem> getList(){
		return list;
	}

	public void setResponseId(String responseId){
		this.responseId = responseId;
	}

	public String getResponseId(){
		return responseId;
	}
}