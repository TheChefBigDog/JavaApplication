package com.example.javaapplication.Activity;

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
import com.example.javaapplication.Activity.Model.Data.Province.ListItem;
import com.example.javaapplication.Activity.Model.Data.Province.ProvinceModel;
import com.example.javaapplication.Activity.Model.Data.User.UserModel;
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
    @BindView(R.id.tv_kota) TextView tvKota;
    @BindView(R.id.tv_kelurahaan) TextView tvKelurahaan;
    @BindView(R.id.tv_kode_pos) TextView tvKodePos;
    String user_id, postalType, lookUpId, kabupaten, kota, kelurahaan, kodePos;
    LinearLayoutManager linearLayoutManager;
    ProvinsiAdapter provinsiAdapter;
    SharedPreferences sp;
    ArrayList<ListItem> provinceItemArrayList = new ArrayList<>();
    ArrayList<ListItemKabupaten> kabupatenArrayList = new ArrayList<>();
    ArrayList<String> provinceStringItemArrayList = new ArrayList<>();
    ArrayList<String> provinceStringNameItemArrayList = new ArrayList<>();
    ArrayList<String> kabupatenStringItemArrayList = new ArrayList<>();
    ArrayList<String> kabupatenStringNameItemArrayList = new ArrayList<>();
    ProvinceInterface provinceInterface;
    SQLiteDatabase database;
    DBHelper dbHelper;
    int index, index2;
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
//        provinsi, kota, kabupaten/kecamatan, kelurahaan,
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
                        provinceItemArrayList.add(listItem);
                        provinceStringItemArrayList.add(provinceModel.getLookupId());
                        provinceStringNameItemArrayList.add(provinceModel.getName());
                    }
                    for(int i = 0; i < provinceStringItemArrayList.size(); i++){
                        if(provinceStringNameItemArrayList.get(i).equals(userModel.getProvinsi())){
                            index = i;
                        }
                    }
                    spProvinsi.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,
                            provinceStringNameItemArrayList) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            spProvinsi.setSelection(index);
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
                    spProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position >= 0){
                                postalType = provinceItemArrayList.get(position).getPostalType();
                                lookUpId = provinceItemArrayList.get(position).getLookupId();
                                Log.e("TAG", "onItemSelected Province: " + postalType + lookUpId );
                                setKabupatenList(postalType, lookUpId);
                            }
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
        Log.e("TAG", "onCreate: " + postalType + lookUpId );
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
                        Log.e("TAG", "onResponse: " + kabupatenStringNameItemArrayList);
                        Log.e("TAG", "onResponse: " + userModel.getKabupaten());
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