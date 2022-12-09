
package com.example.javaapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.javaapplication.Activity.DBHelper.DBHelper;
import com.example.javaapplication.R;

public class SplashScreen extends AppCompatActivity {

    private int RECORD_REQUEST_CODE = 1;
    private SharedPreferences pref;
    private String test;
    private Boolean loginStatus = false;
    private static final String[] PERMISSIONS = {Manifest.permission.CAMERA,
                                                 Manifest.permission.ACCESS_COARSE_LOCATION};
    SQLiteDatabase database;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        dbHelper = new DBHelper(SplashScreen.this);
        database = dbHelper.getReadableDatabase();
        pref = getSharedPreferences("kotPref", MODE_PRIVATE);
        test = pref.getString("_userid", "");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }else{
            if (test.equals("")) {
                delayIntent(false);
            } else {
                delayIntent(true);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_GRANTED
                    ) {
                        if (test.equals("")) {
                            delayIntent(false);
                        } else {
                            delayIntent(true);
                        }
                    }
                } else {
                    ActivityCompat.requestPermissions(this,
                            PERMISSIONS,
                            RECORD_REQUEST_CODE);
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void delayIntent(Boolean loginStatus) {
        if(!loginStatus){
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }, 2000);
        }else{
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }, 2000);
        }


    }


}