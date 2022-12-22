package com.example.javaapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.javaapplication.Activity.DBHelper.DBHelper;
import com.example.javaapplication.Activity.Model.Data.LoginModel.LoginModel;
import com.example.javaapplication.Activity.Model.Data.User.UserModel;
import com.example.javaapplication.Activity.Services.LoginInterface;
import com.example.javaapplication.Activity.Services.LoginUtils;
import com.example.javaapplication.R;
import com.example.javaapplication.databinding.ActivityLoginBinding;
import com.example.javaapplication.databinding.CustomProgressDialogBinding;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;
//import com.example.javaapplication.databinding.ActivityMainBinding;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class LoginActivity extends AppCompatActivity {

    SharedPreferences pref;
    SQLiteDatabase database;
    DBHelper dbHelper;
    int show_stat = 0;
    LoginInterface loginInterface;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        getSupportActionBar().hide();
        pref = getSharedPreferences("kotPref", MODE_PRIVATE);
        dbHelper = new DBHelper(LoginActivity.this);
        database = dbHelper.getReadableDatabase();
        loginInterface = LoginUtils.loginInformation(this);
        binding.etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                    binding.etUsername.setText(binding.etUsername.getText().insert(4, "-"));
            }
        });
        binding.ivEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show_stat == 0) {
                    show_stat = 1;
                    binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.ivEye.setImageResource(R.drawable.ic_hide_password);
                } else {
                    show_stat = 0;
                    binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.ivEye.setImageResource(R.drawable.ic_show_password);
                }
            }
        });
        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                binding.pbLoading.setVisibility(View.VISIBLE);
                binding.btnLogin.setVisibility(View.GONE);
                binding.btnLogin.setEnabled(false);
                /*
                if(binding.etUsername.getText().length() == 0 || binding.etPassword.getText().length() == 0){
                    Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                    binding.etUsername.startAnimation(shake);
                    binding.rlPassowrd.startAnimation(shake);
                    binding.btnLogin.setEnabled(true);
                    binding.btnLogin.setVisibility(View.VISIBLE);
                    binding.pbLoading.setVisibility(View.GONE);
                }else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.btnLogin.setEnabled(true);
                            binding.btnLogin.setVisibility(View.VISIBLE);
                            binding.pbLoading.setVisibility(View.GONE);
                            final Dialog dialog = new Dialog(LoginActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            CustomProgressDialogBinding bindingDialog = DataBindingUtil.inflate(LayoutInflater.from(LoginActivity.this),
                                    R.layout.custom_progress_dialog, null, false);
                            dialog.setContentView(bindingDialog.getRoot());
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            try {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                String name = binding.etUsername.getText().toString();
                                String password = binding.etPassword.getText().toString();
                                database = dbHelper.getReadableDatabase();
                                if (dbHelper.getUserByNameAndPassword(name, password) > 0) {
                                        UserModel userId = dbHelper.returnModel(name, password);
                                        SharedPreferences.Editor prefEditor = pref.edit();
                                        prefEditor.putString("_userid", userId.getId());
                                        prefEditor.apply();
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        dbHelper.close();
                                        startActivity(intent);
                                } else {
                                    bindingDialog.tvLoading.setText("User Doesn't exist !");
                                    bindingDialog.btnLoading.setVisibility(View.VISIBLE);
                                    bindingDialog.pbDialog.setVisibility(View.GONE);
                                    bindingDialog.ivStatus.setVisibility(View.VISIBLE);
                                    dialog.setCancelable(true);
                                    bindingDialog.ivStatus.setImageResource(R.drawable.ic_reject);
                                    bindingDialog.btnLoading.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, 1000);
                }
                */

                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                CustomProgressDialogBinding bindingDialog = DataBindingUtil.inflate(LayoutInflater.from(LoginActivity.this),
                        R.layout.custom_progress_dialog, null, false);
                dialog.setContentView(bindingDialog.getRoot());
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                try {
                    String combinationEncrypted = encryptedUpperKeyString() + "-" + encryptedLowerCaseManual();
                    ResponseBodyLoginPhoneNumber requestBodyLogin = new ResponseBodyLoginPhoneNumber();
                    requestBodyLogin.setKey(combinationEncrypted);
                    requestBodyLogin.setHandphone("0877-69242990");
                    requestBodyLogin.setPassword(encryptStrAndToBase64(encryptedUpperKeyString(), encryptedLowerCaseManual(), binding.etPassword.getText().toString()));
                    requestBodyLogin.setUsername("AGC");
                    requestBodyLogin.setVersion("7");
                    loginInterface.postLogin(requestBodyLogin).enqueue(new Callback<LoginModel>() {
                        @Override
                        public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getResponseStatus().equals("OK")) {
                                    binding.btnLogin.setEnabled(true);
                                    binding.btnLogin.setVisibility(View.VISIBLE);
                                    binding.pbLoading.setVisibility(View.GONE);
                                    Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                                    SharedPreferences.Editor prefEditor = pref.edit();
                                    prefEditor.putString("_username", response.body().getUser().getUsername());
                                    prefEditor.putString("_userId", response.body().getUser().getUserId());
                                    prefEditor.putString("_accessToken", response.body().getAccessToken());
                                    prefEditor.putString("_refreshToken", response.body().getRefreshToken());
                                    prefEditor.apply();
                                    startActivity(intent);
//                                    try {
//                                        Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
//                                        String name = response.body().getUser().getName();
//                                        String branchId = response.body().getUser().getBranchId();
//                                        String branch = response.body().getUser().getBranch();
//                                        String provinsi = response.body().getUser().getName();
//                                        String branchType = response.body().getUser().getBranchType();
//                                        String email = response.body().getUser().getEmail();
//                                        String userId = response.body().getUser().getUserId();
//                                        String userType = response.body().getUser().getUserType();
//                                        int employeeId = response.body().getUser().getEmployeeId();
//                                        dbHelper.addName(name, branchId, branch, provinsi, branchType, email, userId, userType, "", employeeId);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
                                } else {
                                    binding.btnLogin.setEnabled(true);
                                    binding.btnLogin.setVisibility(View.VISIBLE);
                                    binding.pbLoading.setVisibility(View.GONE);
                                    bindingDialog.tvLoading.setText(response.body().getResponseMessage());
                                    bindingDialog.btnLoading.setVisibility(View.VISIBLE);
                                    bindingDialog.pbDialog.setVisibility(View.GONE);
                                    bindingDialog.ivStatus.setVisibility(View.VISIBLE);
                                    dialog.setCancelable(true);
                                    bindingDialog.ivStatus.setImageResource(R.drawable.ic_reject);
                                    bindingDialog.btnLoading.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginModel> call, Throwable t) {
                            binding.btnLogin.setEnabled(true);
                            Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch(Exception e){

                }
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("HOME", true);
        startActivity(intent);
    }

    public static String encryptStrAndToBase64(String ivStr, String keyStr, String enStr) throws Exception {
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

    @Override
    protected void onResume() {
        super.onResume();
        binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        binding.ivEye.setImageResource(R.drawable.ic_hide_password);
    }
}
