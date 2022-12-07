package com.example.javaapplication.Activity.Model.Data.Province;

import com.google.gson.annotations.SerializedName;

public class ListItem{

	@SerializedName("zipCode")
	private Object zipCode;

	@SerializedName("lookupId")
	private String lookupId;

	@SerializedName("postalType")
	private String postalType;

	@SerializedName("name")
	private String name;

	@SerializedName("subdistrictId")
	private Object subdistrictId;

	@SerializedName("cityId")
	private Object cityId;

	@SerializedName("provinceId")
	private Object provinceId;

	@SerializedName("countryId")
	private int countryId;

	public void setZipCode(Object zipCode){
		this.zipCode = zipCode;
	}

	public Object getZipCode(){
		return zipCode;
	}

	public void setLookupId(String lookupId){
		this.lookupId = lookupId;
	}

	public String getLookupId(){
		return lookupId;
	}

	public void setPostalType(String postalType){
		this.postalType = postalType;
	}

	public String getPostalType(){
		return postalType;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setSubdistrictId(Object subdistrictId){
		this.subdistrictId = subdistrictId;
	}

	public Object getSubdistrictId(){
		return subdistrictId;
	}

	public void setCityId(Object cityId){
		this.cityId = cityId;
	}

	public Object getCityId(){
		return cityId;
	}

	public void setProvinceId(Object provinceId){
		this.provinceId = provinceId;
	}

	public Object getProvinceId(){
		return provinceId;
	}

	public void setCountryId(int countryId){
		this.countryId = countryId;
	}

	public int getCountryId(){
		return countryId;
	}
}