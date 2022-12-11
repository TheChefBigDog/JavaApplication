package com.example.javaapplication.Activity.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

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


}
