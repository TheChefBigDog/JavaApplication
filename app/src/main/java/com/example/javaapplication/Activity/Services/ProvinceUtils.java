package com.example.javaapplication.Activity.Services;

import android.content.Context;

public class ProvinceUtils {

    public static ProvinceInterface getListProvince(Context context){
        return RetrofitClient.getClient(RetrofitURL.baseUrl, context).create(ProvinceInterface.class);
    }

}
