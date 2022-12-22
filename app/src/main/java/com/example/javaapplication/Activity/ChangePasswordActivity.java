package com.example.javaapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.javaapplication.Activity.DBHelper.DBHelper;
import com.example.javaapplication.Activity.Model.Data.LoginModel.LoginModel;
import com.example.javaapplication.Activity.Model.Data.OrganizationSetupModel.OrganizationSetupModel;
import com.example.javaapplication.Activity.Model.Data.RefreshToken.RefreshToken;
import com.example.javaapplication.Activity.Model.Data.User.UserModel;
import com.example.javaapplication.Activity.Services.LoginInterface;
import com.example.javaapplication.Activity.Services.LoginUtils;
import com.example.javaapplication.Activity.Services.RequestBodyRefreshToken;
import com.example.javaapplication.R;
import com.example.javaapplication.databinding.ActivityChangePasswordBinding;
import com.example.javaapplication.databinding.CustomProgressDialogBinding;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ChangePasswordActivity extends AppCompatActivity {

    ActivityChangePasswordBinding binding;
    LoginInterface loginInterface;
    SharedPreferences sp;
    Dialog dialog;
    CustomProgressDialogBinding bindingDialog;
    String spUsername, spAccessToken, spUserId, spRefreshToken, stringMinLength, maxRow, minLengthPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        getSupportActionBar().hide();
        loginInterface = LoginUtils.loginInformation(this);
        sp = getApplicationContext().getSharedPreferences("kotPref", MODE_PRIVATE);
        spUserId = sp.getString("_userId", "");
        spUsername = sp.getString("_username", "");
        spAccessToken = sp.getString("_accessToken", "");
        spRefreshToken = sp.getString("_refreshToken", "");
        dialog = new Dialog(ChangePasswordActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bindingDialog = DataBindingUtil.inflate(LayoutInflater.from(ChangePasswordActivity.this),
        R.layout.custom_progress_dialog, null, false);
        dialog.setContentView(bindingDialog.getRoot());
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        RequestBodyRefreshToken requestBodyRefreshToken = new RequestBodyRefreshToken();
        requestBodyRefreshToken.setApp("SETUP");
        requestBodyRefreshToken.setUsername(spUsername);
        requestBodyRefreshToken.setVersion("1");
        getOrganizationInfo(spAccessToken, spUsername,requestBodyRefreshToken);
        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.pbLoading.setVisibility(View.VISIBLE);
                binding.btnChangePassword.setVisibility(View.GONE);
                binding.btnChangePassword.setEnabled(false);
                Log.e("TAG", "onClick: " + minLengthPass);
                if (binding.etNewPassword.length() > Integer.parseInt(minLengthPass) && binding.etConfirmNewPassword.length() > Integer.parseInt(minLengthPass)){
                    try {
                        String key = encryptedUpperKeyString() + "-" + encryptedLowerCaseManual();
                        loginInterface.changepassWord(spAccessToken, requestBodyChangePassword(key)).enqueue(new Callback<LoginModel>() {
                            @Override
                            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                                binding.pbLoading.setVisibility(View.GONE);
                                binding.btnChangePassword.setVisibility(View.VISIBLE);
                                binding.btnChangePassword.setEnabled(true);
                                if (response.isSuccessful()) {
                                    switch (response.body().getResponseStatus()) {
                                        case "OK":
                                            binding.pbLoading.setVisibility(View.GONE);
                                            binding.btnChangePassword.setVisibility(View.VISIBLE);
                                            binding.btnChangePassword.setEnabled(true);
                                            bindingDialog.tvLoading.setText(response.body().getResponseStatus() + "\n" + "Berhasil Ubah Password");
                                            bindingDialog.btnLoading.setVisibility(View.VISIBLE);
                                            bindingDialog.pbDialog.setVisibility(View.GONE);
                                            bindingDialog.ivStatus.setVisibility(View.VISIBLE);
                                            dialog.setCancelable(true);
                                            bindingDialog.ivStatus.setImageResource(R.drawable.circle);
                                            dialog.show();
                                            bindingDialog.btnLoading.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                            break;
                                        case "VALIDATION_FAILED":
                                            try {
                                                if (!response.body().getResponseMessage().equals("SAME_PASSWORD")) {
                                                    methodRecheck(spRefreshToken, bindingDialog, requestBodyChangePassword(key), requestBodyRefreshToken);
                                                } else {
                                                    bindingDialog.tvLoading.setText(response.body().getResponseMessage());
                                                    bindingDialog.btnLoading.setVisibility(View.VISIBLE);
                                                    bindingDialog.pbDialog.setVisibility(View.GONE);
                                                    bindingDialog.ivStatus.setVisibility(View.VISIBLE);
                                                    dialog.setCancelable(true);
                                                    bindingDialog.ivStatus.setImageResource(R.drawable.ic_reject);
                                                    dialog.show();
                                                    bindingDialog.btnLoading.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "TOKEN_EXPIRED":
                                            tokenExpiredMethod(spRefreshToken, spUsername, requestBodyRefreshToken);
                                            break;
                                        default:
                                            Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            break;
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<LoginModel> call, Throwable t) {
                                Toast.makeText(ChangePasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }else{
                    Toast.makeText(ChangePasswordActivity.this, "Must be greater than " + minLengthPass + " words", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    private void tokenExpiredMethod(String spRefreshToken, String spUsername, RequestBodyRefreshToken requestBodyRefreshToken) {
        loginInterface.refreshTken(spRefreshToken, requestBodyRefreshToken).enqueue(new Callback<RefreshToken>() {
            @Override
            public void onResponse(Call<RefreshToken> call, Response<RefreshToken> response) {
                if(response.isSuccessful()){
                    if(response.body().getResponseStatus().equals("OK")){
                        String spNewAccessToken = response.body().getAccessToken();
                            try {
                                String key = encryptedUpperKeyString() + "-" + encryptedLowerCaseManual();
                                loginInterface.changepassWord(spNewAccessToken, requestBodyChangePassword(key)).enqueue(new Callback<LoginModel>() {
                                    @Override
                                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                                        binding.pbLoading.setVisibility(View.GONE);
                                        binding.btnChangePassword.setVisibility(View.VISIBLE);
                                        binding.btnChangePassword.setEnabled(true);
                                        if (response.isSuccessful()) {
                                            switch (response.body().getResponseStatus()) {
                                                case "OK":
                                                    binding.pbLoading.setVisibility(View.GONE);
                                                    binding.btnChangePassword.setVisibility(View.VISIBLE);
                                                    binding.btnChangePassword.setEnabled(true);
                                                    bindingDialog.tvLoading.setText(response.body().getResponseStatus() + "\n" + "Berhasil Ubah Password");
                                                    bindingDialog.btnLoading.setVisibility(View.VISIBLE);
                                                    bindingDialog.pbDialog.setVisibility(View.GONE);
                                                    bindingDialog.ivStatus.setVisibility(View.VISIBLE);
                                                    dialog.setCancelable(true);
                                                    bindingDialog.ivStatus.setImageResource(R.drawable.circle);
                                                    dialog.show();
                                                    bindingDialog.btnLoading.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    break;
                                                case "VALIDATION_FAILED":
                                                    try {
                                                        if (!response.body().getResponseMessage().equals("SAME_PASSWORD")) {
                                                            methodRecheck(spRefreshToken, bindingDialog, requestBodyChangePassword(key), requestBodyRefreshToken);
                                                        } else {
                                                            bindingDialog.tvLoading.setText(response.body().getResponseMessage());
                                                            bindingDialog.btnLoading.setVisibility(View.VISIBLE);
                                                            bindingDialog.pbDialog.setVisibility(View.GONE);
                                                            bindingDialog.ivStatus.setVisibility(View.VISIBLE);
                                                            dialog.setCancelable(true);
                                                            bindingDialog.ivStatus.setImageResource(R.drawable.ic_reject);
                                                            dialog.show();
                                                            bindingDialog.btnLoading.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                        }
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                                case "TOKEN_EXPIRED":
                                                    tokenExpiredMethod(spRefreshToken, spUsername, requestBodyRefreshToken);
                                                    break;
                                                default:
                                                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    break;
                                            }
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<LoginModel> call, Throwable t) {
                                        Toast.makeText(ChangePasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                    }else if(response.body().getResponseStatus().equals("VALIDATION_FAILED")){
                        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<RefreshToken> call, Throwable t) {

            }
        });
    }

    private void getOrganizationInfo(String refreshToken, String username, RequestBodyRefreshToken requestBodyRefreshToken) {
                            loginInterface.getOrganizationInfo("7", username, refreshToken).enqueue(new Callback<OrganizationSetupModel>() {
                            @Override
                            public void onResponse(Call<OrganizationSetupModel> call, Response<OrganizationSetupModel> response) {
                                if(response.isSuccessful()){
                                    if(response.body().getResponseStatus().equals("OK")){
                                        maxRow = response.body().getMaxRowPass();
                                        minLengthPass = response.body().getMinLengthPass();
                                    } else if(response.body().getResponseStatus().equals("VALIDATION_FAILED")){
                                                loginInterface.refreshTken(refreshToken, requestBodyRefreshToken).enqueue(new Callback<RefreshToken>() {
                                                    @Override
                                                    public void onResponse(Call<RefreshToken> call, Response<RefreshToken> response) {
                                                        if(response.isSuccessful()){
                                                            if(response.body().getResponseStatus().equals("OK")){
                                                                String spNewAccessToken = response.body().getAccessToken();
                                                                getOrganizationInfo(spNewAccessToken, spUsername,requestBodyRefreshToken);
                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onFailure(Call<RefreshToken> call, Throwable t) {
                                                        Toast.makeText(ChangePasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }else{
                                        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<OrganizationSetupModel> call, Throwable t) {
                                Toast.makeText(ChangePasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    private void methodRecheck(String spRefreshToken, CustomProgressDialogBinding bindingDialog, RequestBodyChangePassword requestBodyChangePassword, RequestBodyRefreshToken requestBodyRefreshToken) {
        loginInterface.refreshTken(spRefreshToken, requestBodyRefreshToken).enqueue(new Callback<RefreshToken>() {
            @Override
            public void onResponse(Call<RefreshToken> call, Response<RefreshToken> response) {
                if(response.isSuccessful()) {
                    if (response.body().getResponseStatus().equals("OK")) {
                        try{
                            String spNewAccessToken = response.body().getAccessToken();
                            loginInterface.changepassWord(spNewAccessToken, requestBodyChangePassword).enqueue(new Callback<LoginModel>() {
                                @Override
                                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                                    if (response.isSuccessful()) {
                                        switch (response.body().getResponseStatus()) {
                                            case "OK":
                                                binding.pbLoading.setVisibility(View.GONE);
                                                binding.btnChangePassword.setVisibility(View.VISIBLE);
                                                binding.btnChangePassword.setEnabled(true);
                                                bindingDialog.tvLoading.setText(response.body().getResponseStatus() + "\n" + "Berhasil Ubah Password");
                                                bindingDialog.btnLoading.setVisibility(View.VISIBLE);
                                                bindingDialog.pbDialog.setVisibility(View.GONE);
                                                bindingDialog.ivStatus.setVisibility(View.VISIBLE);
                                                dialog.setCancelable(true);
                                                bindingDialog.ivStatus.setImageResource(R.drawable.circle);
                                                dialog.show();
                                                bindingDialog.btnLoading.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                                break;
                                            case "VALIDATION_FAILED":
                                                if(response.body().getResponseMessage().equals("SAME_PASSWORD")){
                                                    bindingDialog.tvLoading.setText(response.body().getResponseStatus());
                                                    bindingDialog.btnLoading.setVisibility(View.VISIBLE);
                                                    bindingDialog.pbDialog.setVisibility(View.GONE);
                                                    bindingDialog.ivStatus.setVisibility(View.VISIBLE);
                                                    dialog.setCancelable(true);
                                                    bindingDialog.ivStatus.setImageResource(R.drawable.ic_reject);
                                                    dialog.show();
                                                    bindingDialog.btnLoading.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }
                                                break;
                                            case "TOKEN_EXPIRED":
                                                tokenExpiredMethod(spRefreshToken, spUsername, requestBodyRefreshToken);
                                                break;
                                            default:
                                                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                break;
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<LoginModel> call, Throwable t) {
                                    Toast.makeText(ChangePasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                    }catch(Exception e){
                            Toast.makeText(ChangePasswordActivity.this, "catch", Toast.LENGTH_SHORT).show();
                    }
                }
                }

                }

            @Override
            public void onFailure(Call<RefreshToken> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

            }

        });

    }

    public static String encryptStrAndToBase64(String keyStr, String enStr) throws Exception {
        byte[] bytes = encrypt(keyStr, keyStr, enStr.getBytes("UTF-8"));
        return new String(Base64.encode(bytes , Base64.DEFAULT), "UTF-8");
    }

    public static byte[] encrypt(String ivStr, String keyStr, byte[] bytes) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(ivStr.getBytes(StandardCharsets.UTF_8));
        byte[] ivBytes = md.digest();

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        sha.update(keyStr.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = sha.digest();
        return encrypt(ivBytes, keyBytes, bytes);
    }


    static byte[] encrypt(byte[] ivBytes, byte[] keyBytes, byte[] bytes) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(bytes);
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


    private RequestBodyChangePassword requestBodyChangePassword(String key) throws Exception {
        try {
            RequestBodyChangePassword requestBodyChangePassword = new RequestBodyChangePassword();
            requestBodyChangePassword.setUsername(spUsername);
            requestBodyChangePassword.setKey(key);
            requestBodyChangePassword.setOldPassword(encryptStrAndToBase64(encryptedLowerCaseManual(), binding.etOldPassword.getText().toString()));
            requestBodyChangePassword.setNewPassword(encryptStrAndToBase64(encryptedLowerCaseManual(), binding.etNewPassword.getText().toString()));
            requestBodyChangePassword.setNewPasswordConfirmation(encryptStrAndToBase64(encryptedLowerCaseManual(), binding.etConfirmNewPassword.getText().toString()));
            requestBodyChangePassword.setUserId(spUserId);
            requestBodyChangePassword.setMaxRow(maxRow);
            requestBodyChangePassword.setVersion("7");
            return requestBodyChangePassword;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}