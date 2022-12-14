package com.example.javaapplication.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Update;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javaapplication.Activity.DBHelper.DBHelper;
import com.example.javaapplication.Activity.LoginActivity;
import com.example.javaapplication.Activity.Model.Data.User.UserModel;
import com.example.javaapplication.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvKota) TextView tvKota;
    @BindView(R.id.tvKabupaten) TextView tvKabupaten;
    @BindView(R.id.tvProvinsi) TextView tvProvinsi;
    @BindView(R.id.tvPhoneNumber) TextView tvPhoneNumber;
    @BindView(R.id.tvJalan) TextView tvJalan;
    @BindView(R.id.tvKodePos) TextView tvKodePos;
    @BindView(R.id.btn_logout) Button btnLogout;
    @BindView(R.id.ivProfile) CircleImageView cvProfile;
    @BindView(R.id.btn_post) Button btnPicture;
    SharedPreferences pref;
    String name, provinsi, kabupaten, kota, phonenumber, jalan, zipcode, imageString, user_id;
    SQLiteDatabase database;
    DBHelper dbHelper;
    UserModel userId;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        pref = getApplicationContext().getSharedPreferences("kotPref", MODE_PRIVATE);
        dbHelper = new DBHelper(MainActivity.this);
        database = dbHelper.getWritableDatabase();
        user_id = pref.getString("_userid", "");
        userId = new UserModel();
        if(dbHelper.getUserByID(user_id)){
            userId = dbHelper.returnModelByID(user_id);
            tvName.setText(userId.getName());
            tvPhoneNumber.setText(userId.getPhoneNumber());
            tvProvinsi.setText(userId.getProvinsi());
            tvKabupaten.setText(userId.getKabupaten());
            tvKota.setText(userId.getKota());
            tvJalan.setText(userId.getJalan());
            tvKodePos.setText(userId.getZipCode());
            if(userId.getImageString() != null) {
                Bitmap bm = BitmapFactory.decodeFile(userId.getImageString());
                cvProfile.setImageBitmap(bm);
            }else{
                cvProfile.setCircleBackgroundColor(getApplicationContext().getColor(R.color.primary_color_variant));
                cvProfile.setImageResource(R.drawable.logo_transparent);
            }
        }else{
            Toast.makeText(this, "User is not existed yet", Toast.LENGTH_SHORT).show();
        }
        imagePhotoExif();
        updateProfile();
        logOut();
    }
    private void imagePhotoExif() {
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhotoExifActivity.class);
                startActivity(intent);
            }
        });

    }

    private void updateProfile() {
        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpdateProfileActivityRenew.class);
                startActivity(intent);
            }
        });
    }

    private void logOut() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor versionEditor = pref.edit();
                versionEditor.clear();
                versionEditor.apply();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("HOME", true);
        startActivity(intent);
    }
}