
package com.example.javaapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.javaapplication.Activity.DBHelper.DBHelper;
import com.example.javaapplication.R;

import java.io.File;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.pb_dialog_splash) ProgressBar pbDialog;
    private int RECORD_REQUEST_CODE = 1;
    private int CODE_CAMERA_REQUEST = 1;
    private SharedPreferences pref;
    private String user_id;
    private Boolean loginStatus = false;
    private static final String[] PERMISSIONS = {Manifest.permission.CAMERA,
                                                 Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                 Manifest.permission.ACCESS_FINE_LOCATION};
    SQLiteDatabase database;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        dbHelper = new DBHelper(SplashScreen.this);
        database = dbHelper.getReadableDatabase();
        pref = getSharedPreferences("kotPref", MODE_PRIVATE);
        user_id = pref.getString("_userid", "");
        if (ContextCompat.checkSelfPermission(this, String.valueOf(PERMISSIONS))
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }else{
            if (user_id.equals("")) {
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
                            PackageManager.PERMISSION_GRANTED) {
                        if (user_id.equals("")) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == CODE_CAMERA_REQUEST && resultCode == RESULT_OK){
//
//        }
    }

    public void delayIntent(Boolean loginStatus) {
        if(!loginStatus){
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                pbDialog.setVisibility(View.GONE);
                startActivity(intent);
                finish();
            }, 2000);
        }else{
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                pbDialog.setVisibility(View.GONE);
                startActivity(intent);
                finish();
            }, 2000);
        }


    }


}