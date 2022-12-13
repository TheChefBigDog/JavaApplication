package com.example.javaapplication.Activity.DBHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.javaapplication.Activity.Model.Data.User.UserModel;
import com.example.javaapplication.Activity.RegisterActivity;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "Samuel's Database";
    static final int DATABASE_VERSION = 1;
    public static String TABLE_NAME = "user_table";
    public static String ID_COL = "id";
    public static String NAME_COl = "name";
    public static String PASS_COL = "password";
    public static String PHONE_COL = "phone_number";
    public static String PROVINSI_COL = "provinsi";
    public static String KABUPATEN_COL = "kabupaten";
    public static String KOTA_COL = "kota";
    public static String JALAN_COL = "jalan";
    public static String ZIP_CODE_COL = "kode_pos";
    public static String IMAGE_STRING_COL = "image";
    public static String STATUS_COL = "status";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = ("CREATE TABLE " + TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY, " +
                NAME_COl + " TEXT," +
                PASS_COL + " TEXT," +
                PHONE_COL + " TEXT,"+
                PROVINSI_COL + " TEXT,"+
                KABUPATEN_COL + " TEXT,"+
                KOTA_COL + " TEXT,"+
                JALAN_COL + " TEXT,"+
                ZIP_CODE_COL + " TEXT,"+
                IMAGE_STRING_COL + " TEXT,"+
                STATUS_COL + " INTEGER"+
                ")");
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addName(String name, String pass, String phone, String provinsi, String kabupaten, String kota, String jalan, String zipCode, String imageString, int status){
        ContentValues values = new ContentValues();
        values.put(NAME_COl, name);
        values.put(PASS_COL, pass);
        values.put(PHONE_COL, phone);
        values.put(PROVINSI_COL, provinsi);
        values.put(KABUPATEN_COL, kabupaten);
        values.put(KOTA_COL, kota);
        values.put(JALAN_COL, jalan);
        values.put(ZIP_CODE_COL, zipCode);
        values.put(IMAGE_STRING_COL, imageString);
        values.put(STATUS_COL, status);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int getUserByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME +
        " WHERE " + DBHelper.NAME_COl + " = '" + name + "' ", null);
        c.moveToFirst();
        return c.getCount();
    }

    public int getUserByNameAndPassword(String name, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME +
                " WHERE " + DBHelper.NAME_COl + " = '" + name + "'" +
                " AND " + DBHelper.PASS_COL + " = '" + password + "' ", null);
        c.moveToFirst();
        return c.getCount();
    }

    public boolean getUserByID(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME +
                " WHERE " + DBHelper.ID_COL + " = '" + id + "'", null);
        c.moveToFirst();
        return c.moveToFirst();
    }


    @SuppressLint("Range")
    public UserModel returnModel(String name, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME +
                " WHERE " + NAME_COl + " = '" + name + "'" +
                " AND " + PASS_COL + " = '" + password + "' ", null);
        UserModel userModel = new UserModel();
        if (c != null) {
            Log.e("TAG", "Benar: " );
            c.moveToFirst();
            userModel.setId(c.getString(c.getColumnIndex(ID_COL)));

            c.close();
            return userModel;
        }else{
            c.moveToFirst();
            Log.e("TAG", "Salah: " );
            c.close();
            return userModel;
        }
    }

    @SuppressLint("Range")
    public UserModel returnModelByID(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        UserModel userModel = new UserModel();
        Cursor c =  db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME +
                " WHERE " + DBHelper.ID_COL + " = '" + id + "'", null);
        if (c != null) {
            c.moveToFirst();
            userModel.setProvinsi(c.getString(c.getColumnIndex(PROVINSI_COL)));
            userModel.setName(c.getString(c.getColumnIndex(NAME_COl)));
            userModel.setPhoneNumber(c.getString(c.getColumnIndex(PHONE_COL)));
            userModel.setZipCode(c.getString(c.getColumnIndex(ZIP_CODE_COL)));
            userModel.setKota(c.getString(c.getColumnIndex(KOTA_COL)));
            userModel.setKabupaten(c.getString(c.getColumnIndex(KABUPATEN_COL)));
            userModel.setJalan(c.getString(c.getColumnIndex(JALAN_COL)));
            return userModel;
        }else{
            c.moveToFirst();
            return userModel;
        }
    }

    public void updateUser(String id, String name, String phone, String provinsi, String kabupaten, String kota, String jalan, String zipCode){
        ContentValues updateValues = new ContentValues();
        updateValues.put(NAME_COl, name);
        updateValues.put(PHONE_COL, phone);
        updateValues.put(PROVINSI_COL, provinsi);
        updateValues.put(KABUPATEN_COL, kabupaten);
        updateValues.put(KOTA_COL, kota);
        updateValues.put(JALAN_COL, jalan);
        updateValues.put(ZIP_CODE_COL, zipCode);
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, updateValues,"id = ?", new String[]{id});
        db.close();
    }

}
