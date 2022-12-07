package com.example.javaapplication.Activity;

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
    SharedPreferences pref;
    String name, provinsi, kabupaten, kota, phonenumber, jalan, zipcode, imageString, user_id;
    SQLiteDatabase database;
    DBHelper dbHelper;
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
        Cursor c = database.rawQuery("SELECT id FROM " + DBHelper.TABLE_NAME + " WHERE " + DBHelper.ID_COL, null);
        if(c.getCount() > 0){
            c.moveToFirst();
            UserModel userModel = new UserModel();
            userModel.setName(String.valueOf(c.getInt(c.getColumnIndex(DBHelper.NAME_COl))));
            tvName.setText(userModel.getName());
            c.close();
        }else{

        }
//        provinsi = pref.getString("_provinsi", "");
//        kabupaten = pref.getString("_kabupaten", "");
//        phonenumber = pref.getString("_phonenumber", "");
//        kota = pref.getString("_kota", "");
//        jalan = pref.getString("_jalan", "");
//        zipcode = pref.getString("_zipcode", "");
//        imageString = pref.getString("_imagestring", "");
//        tvPhoneNumber.setText(phonenumber);
//        tvProvinsi.setText(provinsi);
//        tvKabupaten.setText(kabupaten);
//        tvKota.setText(kota);
//        tvJalan.setText(jalan);
//        tvKodePos.setText(zipcode);
        Bitmap bm = BitmapFactory.decodeFile(imageString);
        cvProfile.setImageBitmap(bm);
        updateProfile();
        logOut();
    }

    private void updateProfile() {
        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpdateProfileActivity.class);
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