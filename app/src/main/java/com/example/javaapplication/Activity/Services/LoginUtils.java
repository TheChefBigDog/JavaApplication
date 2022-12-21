package com.example.javaapplication.Activity.Services;

import android.content.Context;

public class LoginUtils {
    public static LoginInterface loginInformation(Context context){
        return RetrofitClient.getClient(RetrofitURL.baseUrl, context).create(LoginInterface.class);
    }
}
