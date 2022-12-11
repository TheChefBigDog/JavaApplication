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

public class UpdateProfileActivity extends AppCompatActivity {

    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.et_phone_number) EditText etPhoneNumber;
    @BindView(R.id.sp_provinsi) Spinner spProvinsi;
    @BindView(R.id.sp_kabupaten) Spinner spKabupaten;
    @BindView(R.id.sp_kota) Spinner spKota;
    @BindView(R.id.sp_village) Spinner spVillage;
    @BindView(R.id.tv_kode_pos) TextView tvKodePos;
    String user_id, postalType, lookUpId;
    String provinsiPostalType, provinsiLookUpId;
    String kabupatenPostalType, kabupatenLookUpId;
    String kotaPostalType, kotaLookUpId;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sp;
    ArrayList<ListItem> provinceItemArrayList = new ArrayList<>();
    ArrayList<ListItemKabupaten> kabupatenArrayList = new ArrayList<>();
    ArrayList<ListKotaItem> kotaItemArrayList = new ArrayList<>();
    ArrayList<VillageListItem> villageListItemArray = new ArrayList<>();
    ArrayList<String> provinceStringItemArrayList = new ArrayList<>();
    ArrayList<String> provinceStringNameItemArrayList = new ArrayList<>();
    ArrayList<String> kabupatenStringItemArrayList = new ArrayList<>();
    ArrayList<String> kabupatenStringNameItemArrayList = new ArrayList<>();
    ArrayList<String> kotaStringItemArrayList = new ArrayList<>();
    ArrayList<String> kotaStringNameItemArrayList = new ArrayList<>();
    ArrayList<String> villageStringItemArrayList = new ArrayList<>();
    ArrayList<String> villageStringNameItemArrayList = new ArrayList<>();
    ProvinceInterface provinceInterface;
    SQLiteDatabase database;
    DBHelper dbHelper;
    int index, index2, index3, index4;
    UserModel userModel;
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
        userModel = new UserModel();
        if(c.moveToFirst()){
            userModel.setProvinsi(c.getString(c.getColumnIndex(DBHelper.PROVINSI_COL)));
            userModel.setName(c.getString(c.getColumnIndex(DBHelper.NAME_COl)));
            userModel.setPhoneNumber(c.getString(c.getColumnIndex(DBHelper.PHONE_COL)));
            userModel.setZipCode(c.getString(c.getColumnIndex(DBHelper.ZIP_CODE_COL)));
            userModel.setKota(c.getString(c.getColumnIndex(DBHelper.KOTA_COL)));
            userModel.setKabupaten(c.getString(c.getColumnIndex(DBHelper.KABUPATEN_COL)));
            userModel.setJalan(c.getString(c.getColumnIndex(DBHelper.JALAN_COL)));
            Log.e("TAG", "onCreate: " + userModel.getKabupaten() );
            etUsername.setText(userModel.getName());
            etPhoneNumber.setText(userModel.getPhoneNumber());
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
                if (response.isSuccessful()) {
                    provinceStringItemArrayList = new ArrayList<>();
                    provinceStringNameItemArrayList = new ArrayList<>();
                    provinceItemArrayList = new ArrayList<>();
                    for (ListItem provinceModel : response.body().getList()) {
                        ListItem listItem = new ListItem();
                        listItem.setLookupId(provinceModel.getLookupId());
                        listItem.setPostalType(provinceModel.getPostalType());
                        listItem.setName(provinceModel.getName());
                        provinceItemArrayList.add(listItem);
                        provinceStringItemArrayList.add(provinceModel.getLookupId());
                        provinceStringNameItemArrayList.add(provinceModel.getName());
                    }
                    for(int i = 0; i < provinceStringNameItemArrayList.size(); i++){
                        if(provinceStringNameItemArrayList.get(i).equals(userModel.getProvinsi())){
                            index = i;
                        }
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(UpdateProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, provinceStringNameItemArrayList);
                    spProvinsi.setAdapter(spinnerArrayAdapter);
                    spProvinsi.setSelection(index);

                    spProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ((TextView) view).setTextColor(getResources().getColor(R.color.white));
                                provinsiPostalType = provinceItemArrayList.get(position).getPostalType();
                                provinsiLookUpId = provinceItemArrayList.get(position).getLookupId();
                                setKabupatenList(provinsiPostalType, provinsiLookUpId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<ProvinceModel> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setKabupatenList(String postalType, String lookUpId) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("133");
        requestLocationBody.setPostalId(lookUpId);
        requestLocationBody.setPostalType(postalType);
        provinceInterface.getKabupaten(requestLocationBody).enqueue(new Callback<KabupatenModel>() {
            @Override
            public void onResponse(Call<KabupatenModel> call, Response<KabupatenModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getResponseStatus().equals("OK")){
                        kabupatenStringItemArrayList = new ArrayList<>();
                        kabupatenStringNameItemArrayList = new ArrayList<>();
                        kabupatenArrayList = new ArrayList<>();
                        for (ListItemKabupaten listItemKabupaten : response.body().getList()) {
                            ListItemKabupaten listItem = new ListItemKabupaten();
                            listItem.setLookupId(listItemKabupaten.getLookupId());
                            listItem.setPostalType(listItemKabupaten.getPostalType());
                            kabupatenArrayList.add(listItem);
                            kabupatenStringItemArrayList.add(listItemKabupaten.getLookupId());
                            kabupatenStringNameItemArrayList.add(listItemKabupaten.getName());
                        }
                        for(int i = 0; i < kabupatenStringNameItemArrayList.size(); i++){
                            if(kabupatenStringNameItemArrayList.get(i).equals(userModel.getKabupaten())){
                                index2 = i;
                            }
                        }
                        spKabupaten.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,
                                kabupatenStringNameItemArrayList) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                spKabupaten.setSelection(index2);
                                if(position >= kabupatenStringNameItemArrayList.size()) {
                                    position = index2;
                                }
                                View v = super.getView(position, convertView, parent);
                                v.setPadding(v.getPaddingLeft(),15, 0, 15);
                                ((TextView) v).setGravity(Gravity.CENTER);
                                ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                                return v;
                            }

                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                                View v = super.getDropDownView(position, convertView, parent);
                                ((TextView) v).setGravity(Gravity.CENTER);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    ((TextView) v).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                                }
                                return v;
                            }{
                                setDropDownViewResource(android.R.layout
                                        .simple_spinner_dropdown_item);
                            }



                        });
                        spKabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(position >= 0){
                                    kabupatenPostalType = kabupatenArrayList.get(position).getPostalType();
                                    kabupatenLookUpId = kabupatenArrayList.get(position).getLookupId();
                                    setKotaList(kabupatenPostalType, kabupatenLookUpId);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    }
                }
            }

            @Override
            public void onFailure(Call<KabupatenModel> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setKotaList(String kabupatenPostalType, String kabupatenLookUpId) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("133");
        requestLocationBody.setPostalId(kabupatenLookUpId);
        requestLocationBody.setPostalType(kabupatenPostalType);
        provinceInterface.getKota(requestLocationBody).enqueue(new Callback<KotaModel>() {
            @Override
            public void onResponse(Call<KotaModel> call, Response<KotaModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getResponseStatus().equals("OK")){
                        kotaStringItemArrayList = new ArrayList<>();
                        kotaStringNameItemArrayList = new ArrayList<>();
                        kotaItemArrayList = new ArrayList<>();
                        for(ListKotaItem listKotaItem : response.body().getList()){
                            ListKotaItem listKotaItem1 = new ListKotaItem();
                            listKotaItem1.setLookupId(listKotaItem.getLookupId());
                            listKotaItem1.setPostalType(listKotaItem.getPostalType());
                            kotaItemArrayList.add(listKotaItem1);
                            kotaStringNameItemArrayList.add(listKotaItem.getName());
                            kotaStringItemArrayList.add(listKotaItem.getLookupId());
                        }
                        for(int i = 0; i < kotaStringNameItemArrayList.size(); i++){
                            if(kotaStringNameItemArrayList.get(i).equals(userModel.getKota())){
                                index3 = i;
                            }
                        }
                        spKota.setSelection(index3);
                        spKota.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,
                                kotaStringNameItemArrayList) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                if(position >= kotaStringNameItemArrayList.size()) {
                                    position = index3;
                                }
                                View v = super.getView(position, convertView, parent);
                                v.setPadding(v.getPaddingLeft(),15, 0, 15);
                                ((TextView) v).setGravity(Gravity.CENTER);
                                ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                                return v;
                            }

                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                                View v = super.getDropDownView(position, convertView, parent);
                                ((TextView) v).setGravity(Gravity.CENTER);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    ((TextView) v).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                                }
                                return v;
                            }{
                                setDropDownViewResource(android.R.layout
                                        .simple_spinner_dropdown_item);
                            }
                        });
                        spKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                if(position >= 0){
                                    kotaPostalType = kotaItemArrayList.get(position).getPostalType();
                                    kotaLookUpId = kotaItemArrayList.get(position).getLookupId();
                                    setVillageList(kotaPostalType, kotaLookUpId);

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<KotaModel> call, Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setVillageList(String kotaPostalType, String kotaLookUpId) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("133");
        requestLocationBody.setPostalId(kotaLookUpId);
        requestLocationBody.setPostalType(kotaPostalType);
        provinceInterface.getVillage(requestLocationBody).enqueue(new Callback<Village>() {
            @Override
            public void onResponse(Call<Village> call, Response<Village> response) {
                villageListItemArray =new ArrayList<>();
                villageStringItemArrayList =new ArrayList<>();
                villageStringNameItemArrayList =new ArrayList<>();
                if(response.isSuccessful()){
                    if(response.body().getResponseStatus().equals("OK")){
                        for(VillageListItem villageListItem : response.body().getList()){
                            VillageListItem villageItemModel = new VillageListItem();
                            villageItemModel.setLookupId(villageListItem.getLookupId());
                            villageItemModel.setPostalType(villageListItem.getPostalType());
                            villageListItemArray.add(villageItemModel);
                            villageStringItemArrayList.add(villageListItem.getLookupId());
                            villageStringNameItemArrayList.add(villageListItem.getName());
                        }
                        for(int i = 0 ; i < villageStringNameItemArrayList.size() ; i++){
                            if(villageStringNameItemArrayList.get(i).equals(userModel.getJalan())){
                                index4 = i;
                            }
                        }
                        spVillage.setSelection(index4);
                        spVillage.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, villageStringNameItemArrayList){
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                if(position >= villageStringNameItemArrayList.size()) {
                                    position = index4;
                                }
                                View v = super.getView(position, convertView, parent);
                                v.setPadding(v.getPaddingLeft(),15, 0, 15);
                                ((TextView) v).setGravity(Gravity.CENTER);
                                ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                                return v;
                            }

                            @Override
                            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                View v = super.getDropDownView(position, convertView, parent);
                                ((TextView) v).setGravity(Gravity.CENTER);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    ((TextView) v).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                                }
                                return v;
                            }{
                                setDropDownViewResource(android.R.layout
                                        .simple_spinner_dropdown_item);
                            }
                        });
                        tvKodePos.setText(userModel.getZipCode());
                    }
                }
            }

            @Override
            public void onFailure(Call<Village> call, Throwable t) {

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


}