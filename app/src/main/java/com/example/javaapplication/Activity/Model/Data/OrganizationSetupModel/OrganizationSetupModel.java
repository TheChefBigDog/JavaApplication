package com.example.javaapplication.Activity.Model.Data.OrganizationSetupModel;

import com.google.gson.annotations.SerializedName;

public class OrganizationSetupModel{

	@SerializedName("numericUserPass")
	private String numericUserPass;

	@SerializedName("upperCaseLetter")
	private String upperCaseLetter;

	@SerializedName("minLengthPass")
	private int minLengthPass;

	@SerializedName("alphabetUserPass")
	private String alphabetUserPass;

	@SerializedName("responseSlack")
	private Object responseSlack;

	@SerializedName("responseStatus")
	private String responseStatus;

	@SerializedName("responseMessage")
	private Object responseMessage;

	@SerializedName("maxRowPass")
	private int maxRowPass;

	@SerializedName("responseId")
	private String responseId;

	public void setNumericUserPass(String numericUserPass){
		this.numericUserPass = numericUserPass;
	}

	public String getNumericUserPass(){
		return numericUserPass;
	}

	public void setUpperCaseLetter(String upperCaseLetter){
		this.upperCaseLetter = upperCaseLetter;
	}

	public String getUpperCaseLetter(){
		return upperCaseLetter;
	}

	public void setMinLengthPass(int minLengthPass){
		this.minLengthPass = minLengthPass;
	}

	public int getMinLengthPass(){
		return minLengthPass;
	}

	public void setAlphabetUserPass(String alphabetUserPass){
		this.alphabetUserPass = alphabetUserPass;
	}

	public String getAlphabetUserPass(){
		return alphabetUserPass;
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

	public void setResponseMessage(Object responseMessage){
		this.responseMessage = responseMessage;
	}

	public Object getResponseMessage(){
		return responseMessage;
	}

	public void setMaxRowPass(int maxRowPass){
		this.maxRowPass = maxRowPass;
	}

	public int getMaxRowPass(){
		return maxRowPass;
	}

	public void setResponseId(String responseId){
		this.responseId = responseId;
	}

	public String getResponseId(){
		return responseId;
	}
}