package com.example.javaapplication.Activity.Model.Data.Province;

import com.google.gson.annotations.SerializedName;

public class ListItem{

	@SerializedName("zipCode")
	private String zipCode;

	@SerializedName("lookupId")
	private String lookupId;

	@SerializedName("postalType")
	private String postalType;

	@SerializedName("name")
	private String name;

	@SerializedName("subdistrictId")
	private String subdistrictId;

	@SerializedName("cityId")
	private String cityId;

	@SerializedName("provinceId")
	private String provinceId;

	@SerializedName("countryId")
	private String countryId;

	public void setZipCode(String zipCode){
		this.zipCode = zipCode;
	}

	public String getZipCode(){
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

	public void setSubdistrictId(String subdistrictId){
		this.subdistrictId = subdistrictId;
	}

	public String getSubdistrictId(){
		return subdistrictId;
	}

	public void setCityId(String cityId){
		this.cityId = cityId;
	}

	public String getCityId(){
		return cityId;
	}

	public void setProvinceId(String provinceId){
		this.provinceId = provinceId;
	}

	public String getProvinceId(){
		return provinceId;
	}

	public void setCountryId(String countryId){
		this.countryId = countryId;
	}

	public String getCountryId(){
		return countryId;
	}
}