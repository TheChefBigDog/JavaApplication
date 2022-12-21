package com.example.javaapplication.Activity;

import android.content.Context;

import com.example.javaapplication.Activity.Model.Data.LoginModel.LoginModel;
import com.example.javaapplication.Activity.Services.LoginInterface;
import com.example.javaapplication.Activity.Services.LoginUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KitaCobaEncryptDecrptClass {

//    LoginInterface loginInterface;
//    Context context;
//    public KitaCobaEncryptDecrptClass(){
//        loginInterface = LoginUtils.loginInformation(context);
//        String combinationEncrypted = encryptedUpperKeyString() + " - " + encryptedLowerCaseManual();
//        RequestBodyLogin requestBodyLogin = new RequestBodyLogin();
//        requestBodyLogin.setKey(combinationEncrypted);
//        requestBodyLogin.setPassword(encryptStrAndToBase64(encryptedUpperKeyString(), encryptedLowerCaseManual(), "Test"));
//        requestBodyLogin.setUsername("15040198");
//        requestBodyLogin.setVersion("7");
//        loginInterface.postLogin(requestBodyLogin).enqueue(new Callback<LoginModel>() {
//            @Override
//            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
//                if(response.isSuccessful()){
//                    if(response.body().getResponseStatus().equals("OK")){
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginModel> call, Throwable t) {
//
//            }
//        });
//
//
//
//    }
//
//    private String encryptStrAndToBase64(String encryptedUpperKeyString, String encryptedLowerCaseManual, String test) {
//
//
//
//    }
//
//
//    private String encryptedUpperKeyString() {
//        SimpleDateFormat formatter = new SimpleDateFormat("ddMMHHmm");
//        Date date = new Date();
//        String currentDateUpperCase = formatter.format(date)
//                .replace('0', 'A')
//                .replace('1', 'B')
//                .replace('2', 'C')
//                .replace('3', 'D')
//                .replace('4', 'E')
//                .replace('5', 'F')
//                .replace('6', 'G')
//                .replace('7', 'H')
//                .replace('8', 'I')
//                .replace('9', 'J');
//        return currentDateUpperCase;
//    }
//
//    private String encryptedLowerCaseManual(){
//        SimpleDateFormat formatter = new SimpleDateFormat("ddMMHHmm");
//        Date date = new Date();
////        String currentDateLowerCase = new SimpleDateFormat("ddMMHHmm", Locale.getDefault()).format(new Date())
//        String currentDateLowerCase = formatter.format(date)
//                .replace('0', 'a')
//                .replace('1', 'b')
//                .replace('2', 'c')
//                .replace('3', 'd')
//                .replace('4', 'e')
//                .replace('5', 'f')
//                .replace('6', 'g')
//                .replace('7', 'h')
//                .replace('8', 'i')
//                .replace('9', 'j');
//        return currentDateLowerCase;

//    }

}
