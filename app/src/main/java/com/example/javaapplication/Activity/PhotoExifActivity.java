package com.example.javaapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.javaapplication.Activity.DBHelper.DBHelper;
import com.example.javaapplication.Activity.Model.Data.User.UserModel;
import com.example.javaapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.function.Consumer;

public class PhotoExifActivity extends AppCompatActivity {

    SQLiteDatabase database;
    DBHelper dbHelper;
    UserModel userModel;
    SharedPreferences sp;
    String user_id;
    ExifInterface exif;
    String imageString;
    static int CODE_CAMERA_REQUEST = 1;
    static int REQUEST_IMAGE_FROM_GALLERY = 1;
    ImageView c;
    double latLong, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_exif);
        getSupportActionBar().hide();
        dbHelper = new DBHelper(PhotoExifActivity.this);
        database = dbHelper.getWritableDatabase();
        c = findViewById(R.id.iv_profile);
        sp = getApplicationContext().getSharedPreferences("kotPref", MODE_PRIVATE);
        user_id = sp.getString("_userid", "");
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, CODE_CAMERA_REQUEST);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                try {
                    Bitmap imgFile = (Bitmap) data.getExtras().get("data");
                    Uri tempUri = getImageUri(getApplicationContext(), imgFile);
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    exif = new ExifInterface(finalFile.getAbsolutePath());
                    imageString = String.valueOf(finalFile);
                    Log.e("TAG", "onActivityResult: " + imageString);
                    ExifInterface exif = new ExifInterface(imageString);
                    String exifString = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);

                }catch (Exception e){
                    e.printStackTrace();
                }
        }else{
            Log.e("TAG", "onActivityResult: Lower" );
        }

    }

    String dec2DMS(double coord) {
        coord = coord > 0 ? coord : -coord;
        String sOut = Integer.toString((int)coord) + "/1,";
        coord = (coord % 1) * 60;
        sOut = sOut + Integer.toString((int)coord) + "/1,";
        coord = (coord % 1) * 60000;
        sOut = sOut + Integer.toString((int)coord) + "/1000";
        return sOut;
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = this.getContentResolver()
                .query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private Uri getImageUri(Context context, Bitmap photo){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        Random rand = new Random();
        int randNo = rand.nextInt(1000);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), photo, "ImageFile_" + randNo, null
        );
        return Uri.parse(path);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}