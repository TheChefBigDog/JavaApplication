package com.example.javaapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import id.zelory.compressor.Compressor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.camera2.CameraManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javaapplication.Activity.DBHelper.DBHelper;
import com.example.javaapplication.Activity.Model.Data.User.UserModel;
import com.example.javaapplication.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Consumer;

import static java.lang.System.load;

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
    private double latitude = 0.0;
    private double longitude = 0.0;
    float[] latLong = new float[2];
    double longitudeDouble, latitudeDouble;
    private LocationManager locationManager;
    private LocationRequest locationRequest;
    TextView tvLatLong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_exif);
        getSupportActionBar().hide();
        dbHelper = new DBHelper(PhotoExifActivity.this);
        database = dbHelper.getWritableDatabase();
        c = findViewById(R.id.iv_profile);
        tvLatLong = findViewById(R.id.tv_coordinate);
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
//            Bitmap resized = Bitmap.createScaledBitmap(imgFile, 1200, 1200, true);
            Uri tempUri = getImageUri(getApplicationContext(), imgFile);
            File finalFile = new File(getRealPathFromURI(tempUri));

//            c.setImageBitmap(resized);
            InputStream imageStream = getContentResolver().openInputStream(tempUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imageString = String.valueOf(finalFile);
//            encodeImage(selectedImage);
            encodeImageFile(imageString);
//            c.setImageBitmap(imageString);
//            encodeImageFile(String.valueOf(finalFile));

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                Log.e("TAG", "onActivityResult: " + latitude + ", " + longitude);
                tvLatLong.setText(latitude + ", " + longitude);
            } else {
                Toast.makeText(this, "Need permission of map", Toast.LENGTH_LONG).show();
            }
//            exif = new ExifInterface(finalFile.getAbsolutePath());
//            boolean test = exif.getLatLong(latLong);
//            try {
//                int num1Lat = (int)Math.floor(latitude);
//                int num2Lat = (int)Math.floor((latitude - num1Lat) * 60);
//                double num3Lat = (latitude - ((double)num1Lat+((double)num2Lat/60))) * 3600;
//
//                int num1Lon = (int)Math.floor(longitude);
//                int num2Lon = (int)Math.floor((longitude - num1Lon) * 60);
//                double num3Lon = (longitude - ((double)num1Lon+((double)num2Lon/60))) * 3600;
//
//                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,  num1Lat+"/1,"+num2Lat+"/1,"+num3Lat+"/1");
//                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, num1Lon+"/1,"+num2Lon+"/1,"+num3Lon+"/1");
//
//
//                if (latitude > 0) {
//                    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
//                } else {
//                    exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
//                }
//
//                if (longitude > 0) {
//                    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
//                } else {
//                    exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
//                }
//                exif.saveAttributes();
//            } catch (IOException e) {
//                Log.e("PictureActivity", e.getLocalizedMessage());
//            }

//            _getCurrentLocation(lat, lng);
//            exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, gps.convert(latLon.get(0)));
//            exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, gps.convert(latLon.get(0)));
//            exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, gps.convert(latLon.get(1)));
//            exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, gps.convert(latLon.get(1)));
//            exifInterface.saveAttributes();
//            exifInterface.saveAttributes();
//            exifInterface.getAttributeDouble(ExifInterface.TAG_GPS_LATITUDE, ExifInterface.ORIENTATION_UNDEFINED);
//            exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
//            exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//            exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

        }catch (Exception e){
            Log.e("TAG", "onActivityResult: Versi rendah");
            e.printStackTrace();
        }
        }

    }
        //BitmapString
    private String encodeImage(Bitmap selectedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        byte[] decodedByte = Base64.decode(encodedImage, 0);
        Bitmap newBitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        Bitmap resized = Bitmap.createScaledBitmap(newBitmap, 1200, 1300, true);
        c.setImageBitmap(resized);
        return encodedImage;
    }
        //FileString
    private String encodeImageFile(String iamgePath){
        File imagefile = new File(iamgePath);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        byte[] decodedByte = Base64.decode(encImage, 0);
        Bitmap newBitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        Bitmap resized = Bitmap.createScaledBitmap(newBitmap, 1200, 1300, true);
        c.setImageBitmap(resized);
        return encImage;
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