package com.example.javaapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.javaapplication.Activity.Model.Data.LoginModel.LoginModel;
import com.example.javaapplication.Activity.Model.Data.OrganizationSetupModel.OrganizationSetupModel;
import com.example.javaapplication.Activity.Model.Data.RefreshToken.RefreshToken;
import com.example.javaapplication.Activity.Services.LoginInterface;
import com.example.javaapplication.Activity.Services.LoginUtils;
import com.example.javaapplication.Activity.Services.RequestBodyRefreshToken;
import com.example.javaapplication.R;
import com.example.javaapplication.databinding.ActivityChangePasswordBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangePasswordActivity extends AppCompatActivity {

    ActivityChangePasswordBinding binding;
    LoginInterface loginInterface;
    String maxRow;
    SharedPreferences sp;
    String spUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        getSupportActionBar().hide();
        loginInterface = LoginUtils.loginInformation(this);
        sp = getApplicationContext().getSharedPreferences("kotPref", MODE_PRIVATE);
        spUsername = sp.getString("_username", "");
        loginInterface.getOrganizationInfo("7", spUsername, ).enqueue(new Callback<OrganizationSetupModel>() {
            @Override
            public void onResponse(Call<OrganizationSetupModel> call, Response<OrganizationSetupModel> response) {

            }

            @Override
            public void onFailure(Call<OrganizationSetupModel> call, Throwable t) {

            }
        });
        /*
        RequestBodyRefreshToken requestBodyRefreshToken = new RequestBodyRefreshToken();
        requestBodyRefreshToken.setApp("SETUP");
        requestBodyRefreshToken.setUsername(sp.getString("_username", ""));
        requestBodyRefreshToken.setVersion("1");
        loginInterface.refreshTken("ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKcWRHa2lPaUp0TWpJd01EQXdNREl3SWl3aWMzVmlJam9pT1RFM09EUXlJaXdpYVdGMElqb3hOamN4TmpBek9EQTJMQ0psZUhBaU9qRTJOekUyTkRFNU9UbDkud3BkZVlnd1M0LWk1cnZfcmY0LTNJVVd0cDhrRHZZWU1laE94cnJzMERKdw==,",
                requestBodyRefreshToken
                ).enqueue(new Callback<RefreshToken>() {
            @Override
            public void onResponse(Call<RefreshToken> call, Response<RefreshToken> response) {
                if(response.isSuccessful()){
                    if(response.body().getResponseStatus().equals("OK")){

                    }
                }
            }
            @Override
            public void onFailure(Call<RefreshToken> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
        */
        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = encryptedUpperKeyString() + "-" + encryptedLowerCaseManual();
                RequestBodyChangePassword requestBodyChangePassword = new RequestBodyChangePassword();
                requestBodyChangePassword.setUsername("");
                requestBodyChangePassword.setKey(key);
//                requestBodyChangePassword.setOldPassword();
//                requestBodyChangePassword.setNewPassword();
//                requestBodyChangePassword.setNewPasswordConfirmation();
//      1          requestBodyChangePassword.setUserId();
//                requestBodyChangePassword.setMaxRow();
//                requestBodyChangePassword.setVersion();
                loginInterface.changepassWord(requestBodyChangePassword).enqueue(new Callback<LoginModel>() {
                    @Override
                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                    }

                    @Override
                    public void onFailure(Call<LoginModel> call, Throwable t) {
                        Toast.makeText(ChangePasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private String encryptedUpperKeyString() {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMHHmm");
        Date date = new Date();
        String currentDateUpperCase = formatter.format(date)
                .replace('0', 'A')
                .replace('1', 'B')
                .replace('2', 'C')
                .replace('3', 'D')
                .replace('4', 'E')
                .replace('5', 'F')
                .replace('6', 'G')
                .replace('7', 'H')
                .replace('8', 'I')
                .replace('9', 'J');
        return currentDateUpperCase;
    }

    private String encryptedLowerCaseManual(){
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMHHmm");
        Date date = new Date();
//        String currentDateLowerCase = new SimpleDateFormat("ddMMHHmm", Locale.getDefault()).format(new Date())
        String currentDateLowerCase = formatter.format(date)
                .replace('0', 'a')
                .replace('1', 'b')
                .replace('2', 'c')
                .replace('3', 'd')
                .replace('4', 'e')
                .replace('5', 'f')
                .replace('6', 'g')
                .replace('7', 'h')
                .replace('8', 'i')
                .replace('9', 'j');
        return currentDateLowerCase;

    }

}