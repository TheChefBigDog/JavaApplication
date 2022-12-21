package com.example.javaapplication.Activity.Services;

import com.example.javaapplication.Activity.Model.Data.LoginModel.LoginModel;
import com.example.javaapplication.Activity.Model.Data.OrganizationSetupModel.OrganizationSetupModel;
import com.example.javaapplication.Activity.Model.Data.Province.ProvinceModel;
import com.example.javaapplication.Activity.Model.Data.RefreshToken.RefreshToken;
import com.example.javaapplication.Activity.RequestBodyChangePassword;
import com.example.javaapplication.Activity.RequestBodyLogin;
import com.example.javaapplication.Activity.RequestLocationBody;
import com.example.javaapplication.Activity.ResponseBodyLoginPhoneNumber;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginInterface {

    @POST("ops2/login")
    Call<LoginModel> postLogin(@Body ResponseBodyLoginPhoneNumber requestBodyLogin);


    @POST("ops2/changepassword")
    Call<LoginModel> changepassWord(@Body RequestBodyChangePassword requestBodyChangePassword);

    @POST("ops2/authentication/refreshtoken")
    Call<RefreshToken> refreshTken(@Header("token") String token,
                                   @Body RequestBodyRefreshToken requestBodyRefreshToken);

    @GET("ops2/organizationsetup/read/{version}/{username}")
    Call<OrganizationSetupModel> getOrganizationInfo(@Path("version") String id,
                                                     @Path("username") String username,
                                                     @Header("token") String token);

}
