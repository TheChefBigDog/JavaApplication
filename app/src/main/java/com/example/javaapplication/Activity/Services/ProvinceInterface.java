package com.example.javaapplication.Activity.Services;

import com.example.javaapplication.Activity.Model.Data.Kabupaten.KabupatenModel;
import com.example.javaapplication.Activity.Model.Data.Kota.KotaModel;
import com.example.javaapplication.Activity.Model.Data.Province.ProvinceModel;
import com.example.javaapplication.Activity.Model.Data.Village.Village;
import com.example.javaapplication.Activity.RequestCityBody;
import com.example.javaapplication.Activity.RequestKabupatenBody;
import com.example.javaapplication.Activity.RequestProvinceBody;
import com.example.javaapplication.Activity.RequestVillageBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProvinceInterface {

    @POST("ops2/marketing/lookup/postal")
    Call<ProvinceModel> getProvince(@Body RequestProvinceBody requestProvinceBody);

    @POST("ops2/marketing/lookup/postal")
    Call<KabupatenModel> getKabupaten(@Body RequestKabupatenBody requestKabupatenBody);

    @POST("ops2/marketing/lookup/postal")
    Call<KotaModel> getKota(@Body RequestCityBody requestCityBody);

    @POST("ops2/marketing/lookup/postal")
    Call<Village> getVillage(@Body RequestVillageBody requestVillageBody);

}
