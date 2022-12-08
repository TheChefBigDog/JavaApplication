package com.example.javaapplication.Activity.Services;

import com.example.javaapplication.Activity.Model.Data.Kabupaten.KabupatenModel;
import com.example.javaapplication.Activity.Model.Data.Kota.KotaModel;
import com.example.javaapplication.Activity.Model.Data.Province.ProvinceModel;
import com.example.javaapplication.Activity.Model.Data.Village.Village;
import com.example.javaapplication.Activity.RequestLocationBody;
import com.example.javaapplication.Activity.RequestVillageBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProvinceInterface {

    @POST("ops2/marketing/lookup/postal")
    Call<ProvinceModel> getProvince(@Body RequestLocationBody requestProvinceBody);

    @POST("ops2/marketing/lookup/postal")
    Call<KabupatenModel> getKabupaten(@Body RequestLocationBody requestLocationBody);

    @POST("ops2/marketing/lookup/postal")
    Call<KotaModel> getKota(@Body RequestLocationBody requestCityBody);

    @POST("ops2/marketing/lookup/postal")
    Call<Village> getVillage(@Body RequestLocationBody requestVillageBody);

}
