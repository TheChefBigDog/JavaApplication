package com.example.javaapplication.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javaapplication.Activity.Adapter.KabupatenAdapter;
import com.example.javaapplication.Activity.Adapter.ProvinsiAdapter;
import com.example.javaapplication.Activity.DBHelper.DBHelper;
import com.example.javaapplication.Activity.Model.Data.Kabupaten.KabupatenModel;
import com.example.javaapplication.Activity.Model.Data.Kabupaten.ListItemKabupaten;
import com.example.javaapplication.Activity.Model.Data.Kota.KotaModel;
import com.example.javaapplication.Activity.Model.Data.Kota.ListKotaItem;
import com.example.javaapplication.Activity.Model.Data.Province.ListItem;
import com.example.javaapplication.Activity.Model.Data.Province.ProvinceModel;
import com.example.javaapplication.Activity.Model.Data.Village.Village;
import com.example.javaapplication.Activity.Model.Data.Village.VillageListItem;
import com.example.javaapplication.Activity.Services.ProvinceInterface;
import com.example.javaapplication.Activity.Services.ProvinceUtils;
import com.example.javaapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements ProvinsiInterface{

    @BindView(R.id.btnRegister) Button btnRegister;
    @BindView(R.id.etRegisterUsername) EditText etRegisterUsername;
    @BindView(R.id.etRegisterPassword) EditText etRegisterPassword;
    @BindView(R.id.etRegisterPhoneNumber) EditText etRegisterPhoneNumber;
    @BindView(R.id.ivCameraTakeAPicture) ImageView ivCameraTakeAPicture;
    @BindView(R.id.etProvince) EditText tvProvince;
    @BindView(R.id.etKabupaten) EditText tvKabupaten;
    @BindView(R.id.etCity) EditText tvCity;
    @BindView(R.id.etVillage) EditText tvVillage;
    @BindView(R.id.tvPostalCode) TextView tvPostalCode;
    RecyclerView rvProvinsi;
    ProgressBar pbDialog;
    private int CODE_CAMERA_REQUEST = 1;
    SQLiteDatabase database;
    DBHelper dbHelper;
    ProvinceInterface provinceInterface;
    ArrayList<ListItem> provinceItemArrayList = new ArrayList<>();
    ArrayList<ListItemKabupaten> kabupatenArrayList;
    ArrayList<ListKotaItem> listKotaItems;
    ArrayList<VillageListItem> listVillageItems;
    ProvinsiAdapter provinsiAdapter;
    String imageString, locationType, postalTypeRegistrasi, lookUpIdRegistrasi;
    LinearLayoutManager linearLayoutManager;
    Dialog dialog;
    Animation shake;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setTitle("Register");
        dbHelper = new DBHelper(RegisterActivity.this);
        database = dbHelper.getWritableDatabase();
        tvProvince.setKeyListener(null);
        tvKabupaten.setKeyListener(null);
        tvCity.setKeyListener(null);
        tvVillage.setKeyListener(null);
        tvPostalCode.setKeyListener(null);
        shake = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.shake);
        provinceInterface = ProvinceUtils.getListProvince(this);
        setProvinceList(postalTypeRegistrasi, lookUpIdRegistrasi);
        ivCameraTakeAPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, CODE_CAMERA_REQUEST);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                String names = etRegisterUsername.getText().toString();
                String password = etRegisterPassword.getText().toString();
                String phonenumber = etRegisterPhoneNumber.getText().toString();
                String provinsi = tvProvince.getText().toString();
                String kabupaten = tvKabupaten.getText().toString();
                String kota = tvCity.getText().toString();
                String village = tvVillage.getText().toString();
                String zipCode = tvPostalCode.getText().toString();
                int registerStatus = 1;
                Cursor c = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME +
                        " WHERE " + DBHelper.NAME_COl + " = '" + names + "' ", null);
                if(c.getCount() > 0){
                    etRegisterUsername.setText("");
                    etRegisterPassword.setText("");
                    etRegisterPhoneNumber.setText("");
                    Toast.makeText(RegisterActivity.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                    c.close();
                }else{
                    dbHelper.addName(names, password , phonenumber, provinsi, kabupaten, kota, village, zipCode, imageString, registerStatus);
                    startActivity(intent);
                    c.close();
                    dbHelper.close();
                }
            }
        });
    }

    private void setProvinceList(String postalType, String lookUpId) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("133");
        tvProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvKabupaten.getText().length() != 0 || tvCity.getText().length() != 0 || tvVillage.getText().length() != 0 || tvPostalCode.getText().length() != 0) {
                        tvKabupaten.setText("");
                        tvVillage.setText("");
                        tvCity.setText("");
                        tvPostalCode.setText("");
                        tvKabupaten.startAnimation(shake);
                        tvVillage.startAnimation(shake);
                        tvCity.startAnimation(shake);
                        tvPostalCode.startAnimation(shake);
                }
                    locationType = "province";
                    dialog = new Dialog(RegisterActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    View viewDialog = inflater.inflate(R.layout.custom_dialog, null);
                    dialog.setContentView(viewDialog);
                    rvProvinsi = viewDialog.findViewById(R.id.rv_provinces);
                    pbDialog = viewDialog.findViewById(R.id.pb_dialog);
                    TextView tvtitle = viewDialog.findViewById(R.id.tv_title);
                    tvtitle.setText("PROVINCES");
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
                        @Override
                        public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getResponseStatus().equals("OK")) {
                                    pbDialog.setVisibility(View.GONE);
                                    provinceItemArrayList = new ArrayList<>();
                                    provinceItemArrayList = response.body().getList();
                                    provinsiAdapter = new ProvinsiAdapter(
                                            provinceItemArrayList,
                                            kabupatenArrayList,
                                            listKotaItems,
                                            listVillageItems,
                                            RegisterActivity.this,
                                            RegisterActivity.this,
                                            dialog, locationType);
                                    rvProvinsi.setAdapter(provinsiAdapter);
                                    linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    rvProvinsi.setLayoutManager(linearLayoutManager);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProvinceModel> call, Throwable t) {
                            pbDialog.setVisibility(View.GONE);
                            dialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
//            }
        });
        setKabupatenList(lookUpId, postalType);
    }

    private void setKabupatenList(String selectedID, String selectedPostalType) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("133");
        requestLocationBody.setPostalId(selectedID);
        requestLocationBody.setPostalType(selectedPostalType);
            tvKabupaten.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvCity.getText().length() != 0 || tvVillage.getText().length() != 0 || tvPostalCode.getText().length() != 0) {
                        tvVillage.setText("");
                        tvCity.setText("");
                        tvPostalCode.setText("");
                        tvVillage.startAnimation(shake);
                        tvCity.startAnimation(shake);
                        tvPostalCode.startAnimation(shake);
                    }
                    locationType = "district";
                    if(!tvProvince.getText().toString().equals("")){
                    final Dialog dialog = new Dialog(RegisterActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    View viewDialog = inflater.inflate(R.layout.custom_dialog, null);
                    dialog.setContentView(viewDialog);
                    RecyclerView rvProvinsi = viewDialog.findViewById(R.id.rv_provinces);
                    ProgressBar pbDialog = viewDialog.findViewById(R.id.pb_dialog);
                    TextView tvtitle = viewDialog.findViewById(R.id.tv_title);
                    tvtitle.setText("DISTRICTS");
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
                        @Override
                        public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                            if (response.isSuccessful()) {
                                if(response.body().getResponseStatus().equals("OK")) {
                                    pbDialog.setVisibility(View.GONE);
                                    provinceItemArrayList = new ArrayList<>();
                                    provinceItemArrayList = response.body().getList();
                                    provinsiAdapter = new ProvinsiAdapter(
                                            provinceItemArrayList,
                                            kabupatenArrayList,
                                            listKotaItems,
                                            listVillageItems,
                                            RegisterActivity.this,
                                            RegisterActivity.this,
                                            dialog, locationType);
                                    rvProvinsi.setAdapter(provinsiAdapter);
                                    linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    rvProvinsi.setLayoutManager(linearLayoutManager);

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProvinceModel> call, Throwable t) {
                            pbDialog.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                 }else{
                        Toast.makeText(RegisterActivity.this, "Province is still empty", Toast.LENGTH_SHORT).show();
              }
            }

            });
        setKotaList(selectedPostalType, selectedID);

    }


    private void setKotaList(String selectedKabupatenInstansiType, String idKabupatenInstasiSpinner) {
                RequestLocationBody requestLocationBody = new RequestLocationBody();
                requestLocationBody.setUsername("15040198");
                requestLocationBody.setVersion("133");
                requestLocationBody.setPostalId(idKabupatenInstasiSpinner);
                requestLocationBody.setPostalType(selectedKabupatenInstansiType);
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationType = "city";
                if (tvVillage.getText().length() != 0 || tvPostalCode.getText().length() != 0) {
                    tvVillage.setText("");
                    tvPostalCode.setText("");
                    tvVillage.startAnimation(shake);
                    tvPostalCode.startAnimation(shake);
                }
                if(!tvKabupaten.getText().toString().equals("")){
                    final Dialog dialog = new Dialog(RegisterActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    View viewDialog = inflater.inflate(R.layout.custom_dialog, null);
                    dialog.setContentView(viewDialog);
                    RecyclerView rvProvinsi = viewDialog.findViewById(R.id.rv_provinces);
                    ProgressBar pbDialog = viewDialog.findViewById(R.id.pb_dialog);
                    TextView tvtitle = viewDialog.findViewById(R.id.tv_title);
                    tvtitle.setText("CITY");
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
                        @Override
                        public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                            if(response.isSuccessful()){
                             if(response.body().getResponseStatus().equals("OK")){
                                 pbDialog.setVisibility(View.GONE);
                                 provinceItemArrayList = new ArrayList<>();
                                 provinceItemArrayList = response.body().getList();
                                 provinsiAdapter = new ProvinsiAdapter(
                                         provinceItemArrayList,
                                         kabupatenArrayList,
                                         listKotaItems,
                                         listVillageItems,
                                         RegisterActivity.this,
                                         RegisterActivity.this,
                                         dialog, locationType);
                                 rvProvinsi.setAdapter(provinsiAdapter);
                                 linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                 rvProvinsi.setLayoutManager(linearLayoutManager);
                             }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProvinceModel> call, Throwable t) {
                            pbDialog.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }else{
                    Toast.makeText(RegisterActivity.this, "District is still empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setVillageList(idKabupatenInstasiSpinner, selectedKabupatenInstansiType);
    }

    private void setVillageList(String lookUpId, String postalType) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("133");
        requestLocationBody.setPostalId(lookUpId);
        requestLocationBody.setPostalType(postalType);
        tvVillage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationType = "village";
                if(!tvCity.getText().toString().equals("")){
                    final Dialog dialog = new Dialog(RegisterActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    View viewDialog = inflater.inflate(R.layout.custom_dialog, null);
                    dialog.setContentView(viewDialog);
                    RecyclerView rvProvinsi = viewDialog.findViewById(R.id.rv_provinces);
                    ProgressBar pbDialog = viewDialog.findViewById(R.id.pb_dialog);
                    TextView tvtitle = viewDialog.findViewById(R.id.tv_title);
                    tvtitle.setText("VILLAGE");
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
                        @Override
                        public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                            if(response.isSuccessful()){
                                if(response.body().getResponseStatus().equals("OK")){
                                    pbDialog.setVisibility(View.GONE);
                                    provinceItemArrayList = new ArrayList<>();
                                    provinceItemArrayList = response.body().getList();
                                    provinsiAdapter = new ProvinsiAdapter(
                                            provinceItemArrayList,
                                            kabupatenArrayList,
                                            listKotaItems,
                                            listVillageItems,
                                            RegisterActivity.this,
                                            RegisterActivity.this,
                                            dialog, locationType);
                                    rvProvinsi.setAdapter(provinsiAdapter);
                                    linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    rvProvinsi.setLayoutManager(linearLayoutManager);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProvinceModel> call, Throwable t) {

                        }
                    });

                    dialog.show();
                }else{
                    Toast.makeText(RegisterActivity.this, "City is still empty", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_CAMERA_REQUEST && resultCode == RESULT_OK){
            Bitmap imgFile = (Bitmap) data.getExtras().get("data");
            ivCameraTakeAPicture.setImageBitmap(imgFile);
            Uri tempUri = getImageUri(getApplicationContext(),imgFile);
            File finalFile = new File(getRealPathFromURI(tempUri));
            Log.e("TAG", "onActivityResult FILE: " +  finalFile);
            Log.e("TAG", "onActivityResult URI: " +  tempUri);
            imageString = String.valueOf(finalFile);
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private Uri getImageUri(Context context, Bitmap photo){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                context.getContentResolver(),
                photo,
                "ImageFile",
                null
        );
        return Uri.parse(path);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    public void getProvinsi(String provinsi, String postalType, String lookUpId, String locationType, String zipCode) {
        postalTypeRegistrasi = postalType;
        lookUpIdRegistrasi = lookUpId;
        switch (locationType){
            case "province":
                tvProvince.setText(provinsi);
                setKabupatenList(lookUpId, postalType);
                break;
            case "district":
                tvKabupaten.setText(provinsi);
                setKotaList(postalType, lookUpId);
                break;
            case "city":
                tvCity.setText(provinsi);
                setVillageList(lookUpId, postalType);
                break;
            case "village":
                tvPostalCode.setText(zipCode);
                tvVillage.setText(provinsi);
                break;
        }
    }


}