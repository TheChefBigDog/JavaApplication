package com.example.javaapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javaapplication.Activity.Adapter.ProvinsiAdapter;
import com.example.javaapplication.Activity.Adapter.UserAdapter;
import com.example.javaapplication.Activity.Model.Data.Province.ListItem;
import com.example.javaapplication.Activity.Model.Data.Province.ProvinceModel;
import com.example.javaapplication.Activity.Services.ProvinceInterface;
import com.example.javaapplication.Activity.Services.ProvinceUtils;
import com.example.javaapplication.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateProfileActivity extends AppCompatActivity {

    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.et_phone_number) EditText etPhoneNumber;
    @BindView(R.id.tv_provinsi) TextView tvProvinsi;
    @BindView(R.id.tv_kabupaten) TextView tvKabupaten;
    @BindView(R.id.tv_kota) TextView tvKota;
    @BindView(R.id.tv_kelurahaan) TextView tvKelurahaan;
    @BindView(R.id.tv_kode_pos) TextView tvKodePos;
    String userName, phoneNumber, provinsi, kabupaten, kota, kelurahaan, kodePos;
    LinearLayoutManager linearLayoutManager;
    ProvinsiAdapter provinsiAdapter;
    SharedPreferences sp;
    ArrayList<ListItem> provinceItemArrayList = new ArrayList<>();
    ProvinceInterface provinceInterface;

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
        userName = sp.getString("_username","");
        phoneNumber = sp.getString("_phonenumber","");
        provinsi = sp.getString("_provinsi","");
        kabupaten = sp.getString("_kabupaten","");
        kota = sp.getString("_kota","");
        kelurahaan = sp.getString("_jalan","");
        kodePos = sp.getString("_zipcode","");
        etUsername.setText(userName);
        etPhoneNumber.setText(phoneNumber);
        tvProvinsi.setText(provinsi);
        tvKabupaten.setText(kabupaten);
        tvKota.setText(kota);
        tvKelurahaan.setText(kelurahaan);
        tvKodePos.setText(kodePos);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        tvProvinsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(UpdateProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                LayoutInflater inflater = getLayoutInflater();
                View viewDialog = inflater.inflate(R.layout.custom_dialog, null);
                dialog.setContentView(viewDialog);
                RecyclerView rvProvinsi = viewDialog.findViewById(R.id.rv_provinces);
                RequestProvinceBody requestProvinceBody = new RequestProvinceBody();
                requestProvinceBody.setUsername("15040198");
                requestProvinceBody.setVersion("133");
//                provinceInterface.getProvince(requestProvinceBody).enqueue(new Callback<ProvinceModel>() {
//                    @Override
//                    public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
//                        if(response.isSuccessful()) {
//                            provinceItemArrayList = new ArrayList<>();
//                            provinceItemArrayList = response.body().getList();
////                            provinsiAdapter = new ProvinsiAdapter(provinceItemArrayList, UpdateProfileActivity.this);
////                            rvProvinsi.setAdapter(provinsiAdapter);
//                            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//                            rvProvinsi.setLayoutManager(linearLayoutManager);
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<ProvinceModel> call, Throwable t) {
//                        Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                    }
//                });




                dialog.show();
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