package com.example.javaapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javaapplication.Activity.Adapter.ProvinsiAdapter;
import com.example.javaapplication.Activity.DBHelper.DBHelper;
import com.example.javaapplication.Activity.Model.Data.Province.ListItem;
import com.example.javaapplication.Activity.Model.Data.Province.ProvinceModel;
import com.example.javaapplication.Activity.Model.Data.User.UserModel;
import com.example.javaapplication.Activity.Services.ProvinceInterface;
import com.example.javaapplication.Activity.Services.ProvinceUtils;
import com.example.javaapplication.R;

import java.util.ArrayList;

public class UpdateProfileActivityRenew extends AppCompatActivity implements  ProvinsiInterface{

    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.et_phone_number) EditText etPhoneNumber;
    @BindView(R.id.et_provinsi) EditText etProvinsi;
    @BindView(R.id.et_kabupaten) EditText etKabupaten;
    @BindView(R.id.et_kota) EditText etKota;
    @BindView(R.id.et_village) EditText etVillage;
    @BindView(R.id.tv_kode_pos) TextView tvKodePos;
    @BindView(R.id.btn_simpan_edit) Button btnUpdate;
    String user_id, postalType, lookUpId;
    String provinsiPostalType, provinsiLookUpId;
    String kabupatenLookUpId;
    String kotaLookUpId;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sp;
    String locationType;
    ArrayList<ListItem> provinceItemArrayList = new ArrayList<>();
    ArrayList<String> provinceStringItemArrayList = new ArrayList<>();
    ArrayList<String> provinceStringNameItemArrayList = new ArrayList<>();
    ArrayList<String> provinceStringPostalTypeItemArrayList = new ArrayList<>();
    ArrayList<String> kabupatenStringItemArrayList = new ArrayList<>();
    ArrayList<String> kabupatenStringNameItemArrayList = new ArrayList<>();
    ArrayList<String> kabupatenStringPostalTypeItemArrayList = new ArrayList<>();
    ArrayList<String> kotaStringItemArrayList = new ArrayList<>();
    ArrayList<String> kotaStringNameItemArrayList = new ArrayList<>();
    ArrayList<String> kotaStringPostalTypeItemArrayList = new ArrayList<>();
    ArrayList<String> villageStringItemArrayList = new ArrayList<>();
    ArrayList<String> villageStringNameItemArrayList = new ArrayList<>();
    ArrayList<String> villageStringPostalTypeItemArrayList = new ArrayList<>();
    ArrayList<String> villageStringZipCodeItemArrayList = new ArrayList<>();
    ProvinceInterface provinceInterface;
    SQLiteDatabase database;
    DBHelper dbHelper;
    Dialog dialog;
    RecyclerView rvProvinsi;
    ProgressBar pbDialog;
    int index, index2, index3, index4;
    String indexOf, postalTypeProvince;
    String indexOf2, kabupatenPostalType;
    String indexOf3, kotaPostalType;
    UserModel userModel;
    ProvinsiAdapter provinsiAdapter;
    String indexString, indexString2, indexString3, indexString4;
    TextView tvtitle;
    UserModel userId;
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Update Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        provinceInterface = ProvinceUtils.getListProvince(this);
        sp = getApplicationContext().getSharedPreferences("kotPref", MODE_PRIVATE);
        user_id = sp.getString("_userid", "");
        dbHelper = new DBHelper(UpdateProfileActivityRenew.this);
        database = dbHelper.getWritableDatabase();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        etProvinsi.setKeyListener(null);
        etKabupaten.setKeyListener(null);
        etKota.setKeyListener(null);
        etVillage.setKeyListener(null);
        userId = new UserModel();
        if(dbHelper.getUserByID(user_id)){
            userId = dbHelper.returnModelByID(user_id);
            etUsername.setText(userId.getName());
            etPhoneNumber.setText(userId.getPhoneNumber());
            etProvinsi.setText(userId.getProvinsi());
            etKabupaten.setText(userId.getKabupaten());
            etKota.setText(userId.getKota());
            etVillage.setText(userId.getJalan());
            tvKodePos.setText(userId.getZipCode());
//            c.close();
        }else{
        }
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("134");
        provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
            @Override
            public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getResponseStatus().equals("OK")){
                        provinceItemArrayList = new ArrayList<>();
                        provinceItemArrayList = response.body().getList();
                        for (ListItem listItem : provinceItemArrayList){
                            provinceStringItemArrayList.add(listItem.getLookupId());
                            provinceStringNameItemArrayList.add(listItem.getName());
                            provinceStringPostalTypeItemArrayList.add(listItem.getPostalType());
                        }
                        for(int i = 0 ; i < provinceStringNameItemArrayList.size() ; i++){
                            if(provinceStringNameItemArrayList.get(i).equals(userId.getProvinsi())){
                                indexString = provinceStringNameItemArrayList.get(i).toString();
                                indexOf = provinceStringItemArrayList.get(i).toString();
                                postalTypeProvince = provinceStringPostalTypeItemArrayList.get(i).toString();
                            }
                        }
                        setProvinceList(indexOf, postalTypeProvince);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProvinceModel> call, Throwable t) {
                Toast.makeText(UpdateProfileActivityRenew.this, "Server Error", Toast.LENGTH_SHORT).show();

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfileActivityRenew.this, MainActivity.class);
                String name = etUsername.getText().toString();
                String phoneNumber = etPhoneNumber.getText().toString();
                String provinsi = etProvinsi.getText().toString();
                String kabupaten = etKabupaten.getText().toString();
                String kota = etKota.getText().toString();
                String village = etVillage.getText().toString();
                String kodePos = tvKodePos.getText().toString();
                dbHelper.updateUser(user_id, name, phoneNumber, provinsi, kabupaten, kota, village, kodePos);
//                c.close();
                startActivity(intent);
                Toast.makeText(UpdateProfileActivityRenew.this, "Data has been Updated", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void setProvinceList(String indexOf, String postalTypeProvince) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("134");
        provinceStringNameItemArrayList = new ArrayList<>();
        etProvinsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
                    @Override
                    public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                        if(response.isSuccessful()){
                            if(response.body().getResponseStatus().equals("OK")){
                                dialog = new Dialog(UpdateProfileActivityRenew.this);
                                LayoutInflater inflater = getLayoutInflater();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(true);
                                View viewDialog = inflater.inflate(R.layout.custom_dialog, null);
                                dialog.setContentView(viewDialog);
                                rvProvinsi = viewDialog.findViewById(R.id.rv_provinces);
                                pbDialog = viewDialog.findViewById(R.id.pb_dialog);
                                tvtitle = viewDialog.findViewById(R.id.tv_title);
                                String locationType = "province";
                                tvtitle.setText("PROVINCES");
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                pbDialog.setVisibility(View.GONE);
                                provinceItemArrayList = response.body().getList();

                                provinsiAdapter = new ProvinsiAdapter(
                                provinceItemArrayList,
                                UpdateProfileActivityRenew.this,
                                UpdateProfileActivityRenew.this,
                                dialog, locationType);
                                rvProvinsi.setAdapter(provinsiAdapter);
                                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                rvProvinsi.setLayoutManager(linearLayoutManager);
                                  for (ListItem listItem : response.body().getList()){
                                      provinceStringItemArrayList.add(listItem.getLookupId());
                                      provinceStringNameItemArrayList.add(listItem.getName());
                                  }
                                  for(int i = 0 ; i < provinceStringNameItemArrayList.size() ; i++){
                                            if(provinceStringNameItemArrayList.get(i).equals(etProvinsi.getText().toString())){
                                                index = i;
                                            }
                                        }
                                rvProvinsi.getLayoutManager().scrollToPosition(index + 1);
                                provinceStringNameItemArrayList.clear();
                              }
                        }
                        dialog.show();

                    }

                    @Override
                    public void onFailure(Call<ProvinceModel> call, Throwable t) {
                        Toast.makeText(UpdateProfileActivityRenew.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        setKabupatenList(indexOf, postalTypeProvince);
    }

    private void setKabupatenList(String indexOf, String postalTypeProvince) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("134");
        requestLocationBody.setPostalId(indexOf);
        requestLocationBody.setPostalType(postalTypeProvince);
        provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
            @Override
            public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getResponseStatus().equals("OK")){
                        provinceItemArrayList = new ArrayList<>();
                        provinceItemArrayList = response.body().getList();
                        for (ListItem listItem : provinceItemArrayList){
                            kabupatenStringItemArrayList.add(listItem.getLookupId());
                            kabupatenStringNameItemArrayList.add(listItem.getName());
                            kabupatenStringPostalTypeItemArrayList.add(listItem.getPostalType());
                        }
                        for(int i = 0 ; i < kabupatenStringNameItemArrayList.size() ; i++){
                            if(kabupatenStringNameItemArrayList.get(i).equals(userId.getKabupaten())){
                                indexString2 = kabupatenStringNameItemArrayList.get(i).toString();
                                indexOf2 = kabupatenStringItemArrayList.get(i).toString();
                                kabupatenPostalType = kabupatenStringPostalTypeItemArrayList.get(i).toString();
                            }
                        }
//                        etKabupaten.setText(indexString2);
                        setKotaList(indexOf2, kabupatenPostalType);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProvinceModel> call, Throwable t) {
                Toast.makeText(UpdateProfileActivityRenew.this, "Server Error", Toast.LENGTH_SHORT).show();

            }
        });
        kabupatenStringNameItemArrayList = new ArrayList<>();
        etKabupaten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etProvinsi.getText().length() != 0){
                dialog = new Dialog(UpdateProfileActivityRenew.this);
                LayoutInflater inflater = getLayoutInflater();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                View viewDialog = inflater.inflate(R.layout.custom_dialog, null);
                dialog.setContentView(viewDialog);
                rvProvinsi = viewDialog.findViewById(R.id.rv_provinces);
                pbDialog = viewDialog.findViewById(R.id.pb_dialog);
                TextView tvtitle = viewDialog.findViewById(R.id.tv_title);
                locationType = "district";
                tvtitle.setText("DISTRICT");
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
                    @Override
                    public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                        if(response.isSuccessful()){
                            if(response.body().getResponseStatus().equals("OK")){
                                pbDialog.setVisibility(View.GONE);
                                provinceItemArrayList = response.body().getList();

                                provinsiAdapter = new ProvinsiAdapter(
                                        provinceItemArrayList,
                                        UpdateProfileActivityRenew.this,
                                        UpdateProfileActivityRenew.this,
                                        dialog, locationType);
                                rvProvinsi.setAdapter(provinsiAdapter);
                                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                rvProvinsi.setLayoutManager(linearLayoutManager);
                                for (ListItem listItem : response.body().getList()){
                                    kabupatenStringItemArrayList.add(listItem.getLookupId());
                                    kabupatenStringNameItemArrayList.add(listItem.getName());
                                }
                                for(int i = 0 ; i < kabupatenStringNameItemArrayList.size() ; i++){
                                    if(kabupatenStringNameItemArrayList.get(i).equals(etKabupaten.getText().toString())){
                                        index2 = i;
                                    }
                                }
                                rvProvinsi.getLayoutManager().scrollToPosition(index2 + 1);
                                kabupatenStringNameItemArrayList.clear();
                            }
                            dialog.show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ProvinceModel> call, Throwable t) {
                        Toast.makeText(UpdateProfileActivityRenew.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }else{
                    Toast.makeText(UpdateProfileActivityRenew.this, "Please Choose your province first", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void setKotaList(String lookUpId, String postalType) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("134");
        requestLocationBody.setPostalId(lookUpId);
        requestLocationBody.setPostalType(postalType);
        provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
            @Override
            public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getResponseStatus().equals("OK")){
                        provinceItemArrayList = new ArrayList<>();
                        provinceItemArrayList = response.body().getList();
                        for (ListItem listItem : provinceItemArrayList){
                            kotaStringItemArrayList.add(listItem.getLookupId());
                            kotaStringNameItemArrayList.add(listItem.getName());
                            kotaStringPostalTypeItemArrayList.add(listItem.getPostalType());
                        }
                        for(int i = 0 ; i < kotaStringNameItemArrayList.size() ; i++){
                            if(kotaStringNameItemArrayList.get(i).equals(userId.getKota())){
                                indexString3 = kotaStringNameItemArrayList.get(i).toString();
                                indexOf3 = kotaStringItemArrayList.get(i).toString();
                                kotaPostalType = kotaStringPostalTypeItemArrayList.get(i).toString();
                            }
                        }
                        setVillageList(indexOf3, kotaPostalType);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProvinceModel> call, Throwable t) {
                Toast.makeText(UpdateProfileActivityRenew.this, "Server Error", Toast.LENGTH_SHORT).show();

            }
        });


        etKota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(UpdateProfileActivityRenew.this);
                LayoutInflater inflater = getLayoutInflater();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                View viewDialog = inflater.inflate(R.layout.custom_dialog, null);
                dialog.setContentView(viewDialog);
                rvProvinsi = viewDialog.findViewById(R.id.rv_provinces);
                pbDialog = viewDialog.findViewById(R.id.pb_dialog);
                TextView tvtitle = viewDialog.findViewById(R.id.tv_title);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                kotaStringNameItemArrayList = new ArrayList<>();
                locationType = "city";
                tvtitle.setText("CITY");
                provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
                    @Override
                    public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                        if(response.isSuccessful()){
                            if(response.body().getResponseStatus().equals("OK")){
                                pbDialog.setVisibility(View.GONE);
                                provinceItemArrayList = new ArrayList<>();
                                provinceItemArrayList = response.body().getList();
                                kotaStringNameItemArrayList = new ArrayList<>();
                                provinsiAdapter = new ProvinsiAdapter(
                                        provinceItemArrayList,
                                        UpdateProfileActivityRenew.this,
                                        UpdateProfileActivityRenew.this,
                                        dialog, locationType);
                                rvProvinsi.setAdapter(provinsiAdapter);
                                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                rvProvinsi.setLayoutManager(linearLayoutManager);
                                for (ListItem listItem : provinceItemArrayList){
                                    kabupatenStringItemArrayList.add(listItem.getLookupId());
                                    kabupatenStringNameItemArrayList.add(listItem.getName());
                                }
                                for(int i = 0 ; i < kotaStringNameItemArrayList.size() ; i++){
                                    if(kotaStringNameItemArrayList.get(i).equals(etKota.getText().toString())){
                                        index3 = i;
                                    }
                                }
                                rvProvinsi.getLayoutManager().scrollToPosition(index3 + 1);
                                kotaStringNameItemArrayList.clear();
                            }
                            dialog.show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ProvinceModel> call, Throwable t) {
                        Toast.makeText(UpdateProfileActivityRenew.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void setVillageList(String lookUpId, String postalType) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("134");
        requestLocationBody.setPostalId(lookUpId);
        requestLocationBody.setPostalType(postalType);

        etVillage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationType = "village";
                provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
                    @Override
                    public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                        if(response.isSuccessful()){
                            if(response.body().getResponseStatus().equals("OK")){
                                dialog = new Dialog(UpdateProfileActivityRenew.this);
                                LayoutInflater inflater = getLayoutInflater();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(true);
                                View viewDialog = inflater.inflate(R.layout.custom_dialog, null);
                                dialog.setContentView(viewDialog);
                                rvProvinsi = viewDialog.findViewById(R.id.rv_provinces);
                                pbDialog = viewDialog.findViewById(R.id.pb_dialog);
                                TextView tvtitle = viewDialog.findViewById(R.id.tv_title);
                                tvtitle.setText("VILLAGE");
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                villageStringNameItemArrayList = new ArrayList<>();
                                pbDialog.setVisibility(View.GONE);
                                provinceItemArrayList = new ArrayList<>();
                                provinceItemArrayList = response.body().getList();
                                provinsiAdapter = new ProvinsiAdapter(
                                        provinceItemArrayList,
                                        UpdateProfileActivityRenew.this,
                                        UpdateProfileActivityRenew.this,
                                        dialog, locationType);
                                rvProvinsi.setAdapter(provinsiAdapter);
                                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                rvProvinsi.setLayoutManager(linearLayoutManager);
                                for (ListItem listItem : provinceItemArrayList){
                                    villageStringItemArrayList.add(listItem.getLookupId());
                                    villageStringNameItemArrayList.add(listItem.getName());
                                }
                                for(int i = 0 ; i < villageStringNameItemArrayList.size() ; i++){
                                    if(villageStringNameItemArrayList.get(i).equals(etVillage.getText().toString())){
                                        index4 = i;
                                    }
                                }
                                rvProvinsi.getLayoutManager().scrollToPosition(index4 + 1);
                                villageStringNameItemArrayList.clear();
                            }
                            dialog.show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ProvinceModel> call, Throwable t) {
                        Toast.makeText(UpdateProfileActivityRenew.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
    public void getProvinsi(String provinsi, String postalType, String lookUpId, String locationType, String zipCode) {
        switch (locationType){
            case "province":
                if (etKabupaten.getText().length() != 0 || etKota.getText().length() != 0 || etVillage.getText().length() != 0 || tvKodePos.getText().length() != 0) {
                    etKabupaten.setText("");
                    etVillage.setText("");
                    etKota.setText("");
                    tvKodePos.setText("");
                }
                etProvinsi.setText(provinsi);
                setKabupatenList(lookUpId, postalType);
                break;
            case "district":
                if (etKota.getText().length() != 0 || etVillage.getText().length() != 0 || tvKodePos.getText().length() != 0) {
                    etVillage.setText("");
                    etKota.setText("");
                    tvKodePos.setText("");
                }
                etKabupaten.setText(provinsi);
                setKotaList(lookUpId, postalType);
                break;
            case "city":
                if (etVillage.getText().length() != 0 || tvKodePos.getText().length() != 0) {
                    etVillage.setText("");
                    tvKodePos.setText("");
                }
                etKota.setText(provinsi);
                setVillageList(lookUpId, postalType);
                break;
            case "village":
                if (tvKodePos.getText().length() != 0) {
                    tvKodePos.setText("");
                }
                tvKodePos.setText(zipCode);
                etVillage.setText(provinsi);
                break;
        }

    }



}