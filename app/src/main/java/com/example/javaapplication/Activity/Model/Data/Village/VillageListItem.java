package com.example.javaapplication.Activity.Model.Data.Village;

import com.google.gson.annotations.SerializedName;

public class VillageListItem {

	@SerializedName("zipCode")
	private String zipCode;

	@SerializedName("lookupId")
	private String lookupId;

	@SerializedName("postalType")
	private String postalType;

	@SerializedName("name")
	private String name;

	@SerializedName("subdistrictId")
	private int subdistrictId;

	@SerializedName("cityId")
	private int cityId;

	@SerializedName("provinceId")
	private int provinceId;

	@SerializedName("countryId")
	private int countryId;

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

	public void setSubdistrictId(int subdistrictId){
		this.subdistrictId = subdistrictId;
	}

	public int getSubdistrictId(){
		return subdistrictId;
	}

	public void setCityId(int cityId){
		this.cityId = cityId;
	}

	public int getCityId(){
		return cityId;
	}

	public void setProvinceId(int provinceId){
		this.provinceId = provinceId;
	}

	public int getProvinceId(){
		return provinceId;
	}

	public void setCountryId(int countryId){
		this.countryId = countryId;
	}

	public int getCountryId(){
		return countryId;
	}
}