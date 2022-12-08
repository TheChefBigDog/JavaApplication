package com.example.javaapplication.Activity;

import com.google.gson.annotations.SerializedName;

public class RequestLocationBody {
    @SerializedName("username")
    private String username;

    @SerializedName("version")
    private String version;

    @SerializedName("postalId")
    private String postalId;

    @SerializedName("postalType")
    private String postalType;

    public String getPostalId() {
        return postalId;
    }

    public void setPostalId(String postalId) {
        this.postalId = postalId;
    }

    public String getPostalType() {
        return postalType;
    }

    public void setPostalType(String postalType) {
        this.postalType = postalType;
    }

    public void setVersion(String version){
        this.version = version;
    }

    public String getVersion(){
        return version;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }


}
