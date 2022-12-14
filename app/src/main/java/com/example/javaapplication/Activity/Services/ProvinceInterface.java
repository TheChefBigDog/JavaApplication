package com.example.javaapplication.Activity.Services;

import com.example.javaapplication.Activity.Model.Data.Province.ProvinceModel;
import com.example.javaapplication.Activity.RequestLocationBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProvinceInterface {

    @POST("ops2/marketing/lookup/postal")
    Call<ProvinceModel> getProvince(@Body RequestLocationBody requestProvinceBody);

}
