package com.example.javaapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javaapplication.Activity.DBHelper.DBHelper;
import com.example.javaapplication.Activity.Model.Data.User.UserModel;
import com.example.javaapplication.R;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.etUsername) EditText etUsername;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.tvRegister) TextView tvRegister;
    @BindView(R.id.ivEye) ImageView ivEye;
    @BindView(R.id.rlPassowrd) RelativeLayout rlPassword;

     SharedPreferences pref;
     SQLiteDatabase database;
     DBHelper dbHelper;
     int show_stat = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        pref = getSharedPreferences("kotPref", MODE_PRIVATE);
        dbHelper = new DBHelper(LoginActivity.this);
        database = dbHelper.getReadableDatabase();
        ivEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show_stat == 0) {
                    show_stat = 1;
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivEye.setImageResource(R.drawable.ic_hide_password);
                } else {
                    show_stat = 0;
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivEye.setImageResource(R.drawable.ic_show_password);
                }
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                if(etUsername.getText().length() == 0 && etPassword.getText().length() == 0){
                    Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                    etUsername.startAnimation(shake);
                    rlPassword.startAnimation(shake);
                }else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    String name = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    database = dbHelper.getReadableDatabase();
                    try {
                        Cursor c = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME +
                                " WHERE " + DBHelper.NAME_COl + " = '" + name + "'" +
                                " AND " + DBHelper.PASS_COL + " = '" + password + "' ", null);

                        //Kita check kalau user and password match and existed in the row table. (Diambil logicnya dari LoginActivity)
                        if (c.getCount() > 0) {
                            c.moveToFirst();
                            UserModel userModel = new UserModel();
                            userModel.setId(String.valueOf(c.getInt(c.getColumnIndex(DBHelper.ID_COL))));
//                        userModel.setName(c.getString(c.getColumnIndex(DBHelper.NAME_COl)));
//                        userModel.setPhoneNumber(c.getString(c.getColumnIndex(DBHelper.PHONE_COL)));
//                        userModel.setProvinsi(c.getString(c.getColumnIndex(DBHelper.PROVINSI_COL)));
//                        userModel.setKabupaten(c.getString(c.getColumnIndex(DBHelper.KABUPATEN_COL)));
//                        userModel.setKota(c.getString(c.getColumnIndex(DBHelper.KOTA_COL)));
//                        userModel.setJalan(c.getString(c.getColumnIndex(DBHelper.JALAN_COL)));
//                        userModel.setZipCode(c.getString(c.getColumnIndex(DBHelper.ZIP_CODE_COL)));
//                        userModel.setImageString(c.getString(c.getColumnIndex(DBHelper.IMAGE_STRING_COL)));
//                        userModel.setKota(c.getString(c.getColumnIndex(DBHelper.KOTA_COL)));
                            SharedPreferences.Editor prefEditor = pref.edit();
                            prefEditor.putString("_userid", userModel.getId());
//                        prefEditor.putString("_username", userModel.getName());
//                        prefEditor.putString("_phonenumber", userModel.getPhoneNumber());
//                        prefEditor.putString("_provinsi", userModel.getProvinsi());
//                        prefEditor.putString("_kabupaten", userModel.getKabupaten());
//                        prefEditor.putString("_kota", userModel.getKota());
//                        prefEditor.putString("_jalan", userModel.getJalan());
//                        prefEditor.putString("_zipcode", userModel.getZipCode());
//                        prefEditor.putString("_imagestring", userModel.getImageString());
                            prefEditor.apply();
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            c.close();
                            dbHelper.close();
                            startActivity(intent);
                        } else {
                            c.close();
                            Toast.makeText(LoginActivity.this, "User Doesnt Exist", Toast.LENGTH_SHORT).show();
                        }
                    } catch (SQLException e) {
                        Log.e("TAG", "onClick: " + e.getMessage().toString());
                    }
                }
                }


        });

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
}
