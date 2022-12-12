package com.example.javaapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Update;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javaapplication.Activity.Adapter.ProvinsiAdapter;
import com.example.javaapplication.Activity.Adapter.UserAdapter;
import com.example.javaapplication.Activity.DBHelper.DBHelper;
import com.example.javaapplication.Activity.Model.Data.Kabupaten.KabupatenModel;
import com.example.javaapplication.Activity.Model.Data.Kabupaten.ListItemKabupaten;
import com.example.javaapplication.Activity.Model.Data.Kota.KotaModel;
import com.example.javaapplication.Activity.Model.Data.Kota.ListKotaItem;
import com.example.javaapplication.Activity.Model.Data.Province.ListItem;
import com.example.javaapplication.Activity.Model.Data.Province.ProvinceModel;
import com.example.javaapplication.Activity.Model.Data.User.UserModel;
import com.example.javaapplication.Activity.Model.Data.Village.Village;
import com.example.javaapplication.Activity.Model.Data.Village.VillageListItem;
import com.example.javaapplication.Activity.Services.ProvinceInterface;
import com.example.javaapplication.Activity.Services.ProvinceUtils;
import com.example.javaapplication.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateProfileActivity extends AppCompatActivity implements  ProvinsiInterface{

    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.et_phone_number) EditText etPhoneNumber;
    @BindView(R.id.et_provinsi) EditText etProvinsi;
    @BindView(R.id.et_kabupaten) EditText etKabupaten;
    @BindView(R.id.et_kota) EditText etKota;
    @BindView(R.id.et_village) EditText etVillage;
    @BindView(R.id.tv_kode_pos) TextView tvKodePos;
    String user_id, postalType, lookUpId;
    String provinsiPostalType, provinsiLookUpId;
    String kabupatenPostalType, kabupatenLookUpId;
    String kotaPostalType, kotaLookUpId;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sp;
    String locationType, postalTypeRegistrasi, lookUpIdRegistrasi;
    ArrayList<ListItem> provinceItemArrayList = new ArrayList<>();
    ArrayList<String> provinceStringItemArrayList = new ArrayList<>();
    ArrayList<String> provinceStringNameItemArrayList = new ArrayList<>();
    ArrayList<String> provinceStringPostalTypeItemArrayList = new ArrayList<>();
    ArrayList<String> kabupatenStringItemArrayList = new ArrayList<>();
    ArrayList<String> kabupatenStringNameItemArrayList = new ArrayList<>();
    ArrayList<String> kabupatenStringPostalTypeItemArrayList = new ArrayList<>();
    ArrayList<ListItemKabupaten> kabupatenArrayList;
    ArrayList<ListKotaItem> listKotaItems;
    ArrayList<VillageListItem> listVillageItems;
    ArrayList<String> kotaStringItemArrayList = new ArrayList<>();
    ArrayList<String> kotaStringNameItemArrayList = new ArrayList<>();
    ArrayList<String> villageStringItemArrayList = new ArrayList<>();
    ArrayList<String> villageStringNameItemArrayList = new ArrayList<>();
    ProvinceInterface provinceInterface;
    SQLiteDatabase database;
    DBHelper dbHelper;
    Dialog dialog;
    RecyclerView rvProvinsi;
    ProgressBar pbDialog;
    int index, index2, index3, index4;
    String indexOf, postalTypeProvince;
    String indexOf2, postalTypeKabupaten;
    UserModel userModel;
    ProvinsiAdapter provinsiAdapter;
    String indexString, indexString2;
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
        dbHelper = new DBHelper(UpdateProfileActivity.this);
        database = dbHelper.getWritableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME +
                " WHERE " + DBHelper.ID_COL + " = '" + user_id + "'", null);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        etProvinsi.setKeyListener(null);
        etKabupaten.setKeyListener(null);
        etKota.setKeyListener(null);
        etVillage.setKeyListener(null);
        userModel = new UserModel();
        if(c.moveToFirst()){
            userModel.setProvinsi(c.getString(c.getColumnIndex(DBHelper.PROVINSI_COL)));
            userModel.setName(c.getString(c.getColumnIndex(DBHelper.NAME_COl)));
            userModel.setPhoneNumber(c.getString(c.getColumnIndex(DBHelper.PHONE_COL)));
            userModel.setZipCode(c.getString(c.getColumnIndex(DBHelper.ZIP_CODE_COL)));
            userModel.setKota(c.getString(c.getColumnIndex(DBHelper.KOTA_COL)));
            userModel.setKabupaten(c.getString(c.getColumnIndex(DBHelper.KABUPATEN_COL)));
            userModel.setJalan(c.getString(c.getColumnIndex(DBHelper.JALAN_COL)));
            etUsername.setText(userModel.getName());
            etPhoneNumber.setText(userModel.getPhoneNumber());
            etProvinsi.setText(userModel.getProvinsi());
            etKabupaten.setText(userModel.getKabupaten());
            etKota.setText(userModel.getKota());
            etVillage.setText(userModel.getJalan());
            tvKodePos.setText(userModel.getZipCode());
            c.close();
        }
        setProvinceList();
    }

    private void setProvinceList() {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("133");
        provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
            @Override
            public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
//                etProvinsi.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {

                        locationType = "province";
                        dialog = new Dialog(UpdateProfileActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        View viewDialog = inflater.inflate(R.layout.custom_dialog, null);
                        dialog.setContentView(viewDialog);
                        rvProvinsi = viewDialog.findViewById(R.id.rv_provinces);
                        pbDialog = viewDialog.findViewById(R.id.pb_dialog);
                        TextView tvtitle = viewDialog.findViewById(R.id.tv_title);
                        tvtitle.setText("PROVINCES");
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                                        UpdateProfileActivity.this,
                                        UpdateProfileActivity.this,
                                        dialog, locationType);
                                rvProvinsi.setAdapter(provinsiAdapter);
                                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                rvProvinsi.setLayoutManager(linearLayoutManager);
                                for (ListItem listItem : response.body().getList()){
                                    provinceStringItemArrayList.add(listItem.getLookupId());
                                    provinceStringNameItemArrayList.add(listItem.getName());
                                    provinceStringPostalTypeItemArrayList.add(listItem.getPostalType());
                                }
                                for(int i = 0 ; i < provinceStringNameItemArrayList.size() ; i++){
                                    if(provinceStringNameItemArrayList.get(i).equals(userModel.getProvinsi())){
                                        indexString = provinceStringNameItemArrayList.get(i).toString();
                                        indexOf = provinceStringItemArrayList.get(i).toString();
                                        postalTypeProvince = provinceStringPostalTypeItemArrayList.get(i).toString();
                                        index = i;
                                    }
                                }
                                rvProvinsi.getLayoutManager().scrollToPosition(index + 1);
                                provinceStringNameItemArrayList.clear();
                                Log.e("TAG", "setProvinceList: " + lookUpIdRegistrasi + " " + postalTypeRegistrasi + " " + indexOf + 1);
                            }
                        }

                etProvinsi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etKabupaten.getText().length() != 0 || etKota.getText().length() != 0 || etVillage.getText().length() != 0 || tvKodePos.getText().length() != 0) {
                            etKabupaten.setText("");
                            etVillage.setText("");
                            etKota.setText("");
                            tvKodePos.setText("");
                        }
                        setKabupatenList(indexOf, postalTypeProvince);
                        dialog.show();
                    }
                });
                setKabupatenList(indexOf, postalTypeProvince);
            }
            @Override
            public void onFailure(Call<ProvinceModel> call, Throwable t) {
                pbDialog.setVisibility(View.GONE);
                dialog.dismiss();
                Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setKabupatenList(String lookUpId, String postalType) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("133");
        requestLocationBody.setPostalId(lookUpId);
        requestLocationBody.setPostalType(postalType);
        locationType = "district";

        provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
            @Override
            public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getResponseStatus().equals("OK")){
                        dialog = new Dialog(UpdateProfileActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        View viewDialog = inflater.inflate(R.layout.custom_dialog, null);
                        dialog.setContentView(viewDialog);
                        rvProvinsi = viewDialog.findViewById(R.id.rv_provinces);
                        pbDialog = viewDialog.findViewById(R.id.pb_dialog);
                        TextView tvtitle = viewDialog.findViewById(R.id.tv_title);
                        tvtitle.setText("DISTRICTS");
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                                        UpdateProfileActivity.this,
                                        UpdateProfileActivity.this,
                                        dialog, locationType);
                                rvProvinsi.setAdapter(provinsiAdapter);
                                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                rvProvinsi.setLayoutManager(linearLayoutManager);
                                for (ListItem listItem : response.body().getList()){
                                    kabupatenStringItemArrayList.add(listItem.getLookupId());
                                    kabupatenStringNameItemArrayList.add(listItem.getName());
                                    kabupatenStringPostalTypeItemArrayList.add(listItem.getPostalType());
                                }
                                for(int i = 0 ; i < kabupatenStringNameItemArrayList.size() ; i++){
                                    if(kabupatenStringNameItemArrayList.get(i).equals(userModel.getKabupaten())){
                                        indexString2 = kabupatenStringNameItemArrayList.get(i).toString();
                                        indexOf2 = kabupatenStringItemArrayList.get(i).toString();
                                        postalTypeKabupaten = kabupatenStringPostalTypeItemArrayList.get(i).toString();
                                        index2 = i;
                                    }
                                }
                                rvProvinsi.getLayoutManager().scrollToPosition(index2 + 1);
                                kabupatenStringNameItemArrayList.clear();
                            }
                        }

                        etKabupaten.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (etKota.getText().length() != 0 || etVillage.getText().length() != 0 || tvKodePos.getText().length() != 0) {
                                    etKabupaten.setText("");
                                    etVillage.setText("");
                                    etKota.setText("");
                                    tvKodePos.setText("");
                                }
                                dialog.show();
                            }
                        });
                        setKotaList(indexOf2, postalTypeKabupaten);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProvinceModel> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
//        final Dialog dialog = new Dialog(UpdateProfileActivity.this);
//        LayoutInflater inflater = getLayoutInflater();
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        View viewDialog = inflater.inflate(R.layout.custom_dialog, null);
//        dialog.setContentView(viewDialog);
//        rvProvinsi = viewDialog.findViewById(R.id.rv_provinces);
//        pbDialog = viewDialog.findViewById(R.id.pb_dialog);
//        TextView tvtitle = viewDialog.findViewById(R.id.tv_title);
//        tvtitle.setText("DISTRICTS");
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
//            @Override
//            public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
//                if (response.isSuccessful()) {
//                    if(response.body().getResponseStatus().equals("OK")) {
//                        pbDialog.setVisibility(View.GONE);
//                        provinceItemArrayList = new ArrayList<>();
//                        provinceItemArrayList = response.body().getList();
//
//                        for (ListItem listItem : response.body().getList()){
//                            kabupatenStringNameItemArrayList.add(listItem.getName());
//                            kabupatenStringPostalTypeItemArrayList.add(listItem.getPostalType());
//                            kabupatenStringItemArrayList.add(listItem.getLookupId());
//                        }
//                        for(int i = 0 ; i < kabupatenStringNameItemArrayList.size() ; i++){
//                            if(kabupatenStringNameItemArrayList.get(i).equals(userModel.getKabupaten())){
//                                indexString2 = kabupatenStringNameItemArrayList.get(i).toString();
//                                indexOf2 = kabupatenStringItemArrayList.get(i).toString();
//                                postalTypeKabupaten = kabupatenStringPostalTypeItemArrayList.get(i).toString();
//                                index2 = i;
//                            }
//                        }
////                        rvProvinsi.getLayoutManager().scrollToPosition(index2 + 1);
//                        kabupatenStringNameItemArrayList.clear();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ProvinceModel> call, Throwable t) {
//                pbDialog.setVisibility(View.GONE);
//                Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//            }
//        });
//        etKabupaten.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                locationType = "district";
//                if(!etProvinsi.getText().toString().equals("")){
//                    provinsiAdapter = new ProvinsiAdapter(
//                            provinceItemArrayList,
//                            kabupatenArrayList,
//                            listKotaItems,
//                            listVillageItems,
//                            UpdateProfileActivity.this,
//                            UpdateProfileActivity.this,
//                            dialog, locationType);
//
//                    rvProvinsi.setAdapter(provinsiAdapter);
//                    linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//                    rvProvinsi.setLayoutManager(linearLayoutManager);
//                    dialog.show();
//                }else{
//                    Toast.makeText(UpdateProfileActivity.this, "Province is still empty", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void setKotaList(String kabupatenPostalType, String kabupatenLookUpId) {
//        RequestLocationBody requestLocationBody = new RequestLocationBody();
//        requestLocationBody.setUsername("15040198");
//        requestLocationBody.setVersion("133");
//        requestLocationBody.setPostalId(kabupatenLookUpId);
//        requestLocationBody.setPostalType(kabupatenPostalType);
//        etKota.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                locationType = "city";
//                if (etVillage.getText().length() != 0 || tvKodePos.getText().length() != 0) {
//                    etVillage.setText("");
//                    tvKodePos.setText("");
//                }
//                if(!etKabupaten.getText().toString().equals("")){
//                    final Dialog dialog = new Dialog(UpdateProfileActivity.this);
//                    LayoutInflater inflater = getLayoutInflater();
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setCancelable(true);
//                    View viewDialog = inflater.inflate(R.layout.custom_dialog, null);
//                    dialog.setContentView(viewDialog);
//                    RecyclerView rvProvinsi = viewDialog.findViewById(R.id.rv_provinces);
//                    ProgressBar pbDialog = viewDialog.findViewById(R.id.pb_dialog);
//                    TextView tvtitle = viewDialog.findViewById(R.id.tv_title);
//                    tvtitle.setText("CITY");
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                    provinceInterface.getProvince(requestLocationBody).enqueue(new Callback<ProvinceModel>() {
//                        @Override
//                        public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
//                            if(response.isSuccessful()){
//                                if(response.body().getResponseStatus().equals("OK")){
//                                    pbDialog.setVisibility(View.GONE);
//                                    provinceItemArrayList = new ArrayList<>();
//                                    provinceItemArrayList = response.body().getList();
//                                    provinsiAdapter = new ProvinsiAdapter(
//                                            provinceItemArrayList,
//                                            kabupatenArrayList,
//                                            listKotaItems,
//                                            listVillageItems,
//                                            UpdateProfileActivity.this,
//                                            UpdateProfileActivity.this,
//                                            dialog, locationType);
//                                    rvProvinsi.setAdapter(provinsiAdapter);
//                                    linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//                                    rvProvinsi.setLayoutManager(linearLayoutManager);
//                                    for (ListItem listItem : response.body().getList()){
//                                        kotaStringNameItemArrayList.add(listItem.getName());
//                                    }
//                                    for(int i = 0 ; i < kotaStringNameItemArrayList.size() ; i++){
//                                        if(kotaStringNameItemArrayList.get(i).equals(userModel.getKota())){
//                                            index3 = i;
//                                        }
//                                    }
//                                    rvProvinsi.getLayoutManager().scrollToPosition(index3 + 1);
//                                    kotaStringNameItemArrayList.clear();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ProvinceModel> call, Throwable t) {
//                            pbDialog.setVisibility(View.GONE);
//                            Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    dialog.show();
//                }else{
//                    Toast.makeText(UpdateProfileActivity.this, "District is still empty", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void setVillageList(String kotaPostalType, String kotaLookUpId) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("133");
        requestLocationBody.setPostalId(kotaLookUpId);
        requestLocationBody.setPostalType(kotaPostalType);
//        provinceInterface.getVillage(requestLocationBody).enqueue(new Callback<Village>() {
//            @Override
//            public void onResponse(Call<Village> call, Response<Village> response) {
//                villageListItemArray =new ArrayList<>();
//                villageStringItemArrayList =new ArrayList<>();
//                villageStringNameItemArrayList =new ArrayList<>();
//                if(response.isSuccessful()){
//                    if(response.body().getResponseStatus().equals("OK")){
//                        for(VillageListItem villageListItem : response.body().getList()){
//                            VillageListItem villageItemModel = new VillageListItem();
//                            villageItemModel.setLookupId(villageListItem.getLookupId());
//                            villageItemModel.setPostalType(villageListItem.getPostalType());
//                            villageListItemArray.add(villageItemModel);
//                            villageStringItemArrayList.add(villageListItem.getLookupId());
//                            villageStringNameItemArrayList.add(villageListItem.getName());
//                        }
//                        for(int i = 0 ; i < villageStringNameItemArrayList.size() ; i++){
//                            if(villageStringNameItemArrayList.get(i).equals(userModel.getJalan())){
//                                index4 = i;
//                            }
//                        }
//                        spVillage.setSelection(index4);
//                        spVillage.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, villageStringNameItemArrayList){
//                            @Override
//                            public View getView(int position, View convertView, ViewGroup parent) {
//                                if(position >= villageStringNameItemArrayList.size()) {
//                                    position = index4;
//                                }
//                                View v = super.getView(position, convertView, parent);
//                                v.setPadding(v.getPaddingLeft(),15, 0, 15);
//                                ((TextView) v).setGravity(Gravity.CENTER);
//                                ((TextView) v).setTextColor(getResources().getColor(R.color.white));
//                                return v;
//                            }
//
//                            @Override
//                            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                                View v = super.getDropDownView(position, convertView, parent);
//                                ((TextView) v).setGravity(Gravity.CENTER);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                                    ((TextView) v).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                                    ((TextView) v).setTextColor(getResources().getColor(R.color.black));
//                                }
//                                return v;
//                            }{
//                                setDropDownViewResource(android.R.layout
//                                        .simple_spinner_dropdown_item);
//                            }
//                        });
//                        tvKodePos.setText(userModel.getZipCode());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Village> call, Throwable t) {
//
//            }
//        });
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
        postalTypeRegistrasi = postalType;
        lookUpIdRegistrasi = lookUpId;
        switch (locationType){
            case "province":
                etProvinsi.setText(provinsi);
                setKabupatenList(lookUpId, postalType);
                break;
            case "district":
                etKabupaten.setText(provinsi);
                setKotaList(postalType, lookUpId);
                break;
            case "city":
                etKota.setText(provinsi);
                setVillageList(lookUpId, postalType);
                break;
            case "village":
                tvKodePos.setText(zipCode);
                etVillage.setText(provinsi);
                break;
        }
    }
}