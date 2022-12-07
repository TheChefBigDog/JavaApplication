package com.example.javaapplication.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.btnRegister) Button btnRegister;
    @BindView(R.id.etRegisterUsername) EditText etRegisterUsername;
    @BindView(R.id.etRegisterPassword) EditText etRegisterPassword;
    @BindView(R.id.etRegisterPhoneNumber) EditText etRegisterPhoneNumber;
    @BindView(R.id.ivCameraTakeAPicture) ImageView ivCameraTakeAPicture;
    @BindView(R.id.spProvince) Spinner spProvince;
    @BindView(R.id.spKabupaten) Spinner spKabupaten;
    @BindView(R.id.spCity) Spinner spCity;
    @BindView(R.id.spVillage) Spinner spVillage;
    @BindView(R.id.tvPostalCode) TextView tvPostalCode;
    private int CODE_CAMERA_REQUEST = 1;
    SQLiteDatabase database;
    DBHelper dbHelper;
    ProvinceInterface provinceInterface;
    ArrayList<String> provinceNameList = new ArrayList<>();
    ArrayList<String> kabupatenNamesList = new ArrayList<>();
    ArrayList<String> kotaNamesList = new ArrayList<>();
    ArrayList<String> villageNamesList = new ArrayList<>();
    String selectedProvinseInstansi, idProvinsiInstansiSpinner, selectedInstansiType;
    String selectedKabupatenInstansi, idKabupatenInstasiSpinner, selectedKabupatenInstansiType;
    String selectedCityInstansi, idCityInstasiSpinner, selectedCityInstansiType;
    String selectedVillageInstansi, idVillageInstasiSpinner, selectedVillageInstansiType, selectedZipCode;
    ArrayList<ListItem> provinceModels;
    ArrayList<ListItemKabupaten> kabupatenArrayList;
    ArrayList<VillageListItem> villageListItems;
    ArrayList<ListKotaItem> listKotaItems;
    int index2;
    String imageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setTitle("Register");
        provinceModels = new ArrayList<>();
        provinceNameList = new ArrayList<>();
        dbHelper = new DBHelper(RegisterActivity.this);
        database = dbHelper.getWritableDatabase();
        provinceInterface = ProvinceUtils.getListProvince(this);
        ivCameraTakeAPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, CODE_CAMERA_REQUEST);
            }
        });
        setProvinceList();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                String names = etRegisterUsername.getText().toString();
                String password = etRegisterPassword.getText().toString();
                String phonenumber = etRegisterPhoneNumber.getText().toString();
                String provinsi = spProvince.getSelectedItem().toString();
                String kabupaten = spKabupaten.getSelectedItem().toString();
                String kota = spCity.getSelectedItem().toString();
                String village = spVillage.getSelectedItem().toString();
                String zipCode = tvPostalCode.getText().toString();
                int registerStatus = 1;
                Cursor c = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME +
                        " WHERE " + DBHelper.NAME_COl + " = '" + names + "' ", null);
                //Kita check kalau user and password match and existed in the row table.
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

    private void setProvinceList() {
        RequestProvinceBody requestProvinceBody = new RequestProvinceBody();
        requestProvinceBody.setUsername("15040198");
        requestProvinceBody.setVersion("133");
        provinceInterface.getProvince(requestProvinceBody).enqueue(new Callback<ProvinceModel>() {
            @Override
            public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getResponseStatus().equals("OK"))
                        for(ListItem listItem : response.body().getList()){
                            ListItem provinceModel = new ListItem();
                            provinceModel.setName(listItem.getName());
                            provinceModel.setLookupId(listItem.getLookupId());
                            provinceModel.setPostalType(listItem.getPostalType());
                            provinceModels.add(provinceModel);
                            provinceNameList.add(provinceModel.getName());
                        }
                        setSpinnerProvinceList();
                }
            }

            @Override
            public void onFailure(Call<ProvinceModel> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinnerProvinceList() {
        spProvince.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,
                provinceNameList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(position >= provinceNameList.size()) {
                    position = 0;
                }
                View v = super.getView(position, convertView, parent);
                v.setPadding(v.getPaddingLeft(),15, 0, 15);
                ((TextView) v).setGravity(Gravity.CENTER);
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
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }
        });


        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.black));
                if(position>=0) {
                    selectedInstansiType = provinceModels.get(position).getPostalType();
                    idProvinsiInstansiSpinner = provinceModels.get(position).getLookupId();
                    selectedProvinseInstansi = provinceModels.get(position).getName();
                    setKabupatenList(idProvinsiInstansiSpinner, selectedInstansiType, selectedProvinseInstansi);
                    Log.e("TAG", "onItemSelected: " + idProvinsiInstansiSpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setKabupatenList(String idProvinsiInstansiSpinner, String selectedInstansiType, String selectedProvinseInstansi) {
        RequestKabupatenBody requestKabupatenBody = new RequestKabupatenBody();
        requestKabupatenBody.setUsername("15040198");
        requestKabupatenBody.setVersion("133");
        requestKabupatenBody.setPostalId(idProvinsiInstansiSpinner);
        requestKabupatenBody.setPostalType(selectedInstansiType);
        provinceInterface.getKabupaten(requestKabupatenBody).enqueue(new Callback<KabupatenModel>() {
            @Override
            public void onResponse(Call<KabupatenModel> call, Response<KabupatenModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getResponseStatus().equals("OK")){
                        kabupatenArrayList = new ArrayList<>();
                        kabupatenNamesList = new ArrayList<>();
                        for(ListItemKabupaten listItemKabupaten : response.body().getList()){
                            ListItemKabupaten listItemKabupatenModel = new ListItemKabupaten();
                            listItemKabupatenModel.setName(listItemKabupaten.getName());
                            listItemKabupatenModel.setLookupId(listItemKabupaten.getLookupId());
                            listItemKabupatenModel.setPostalType(listItemKabupaten.getPostalType());

                            kabupatenArrayList.add(listItemKabupaten);
                            kabupatenNamesList.add(listItemKabupaten.getName());
                        }
                        setSpinnerKabupatenList(selectedProvinseInstansi);
                    }
                }
            }

            @Override
            public void onFailure(Call<KabupatenModel> call, Throwable t) {

            }
        });

    }

    private void setSpinnerKabupatenList(String selectedProvinseInstansi) {
        spKabupaten.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,
                kabupatenNamesList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(position >= kabupatenNamesList.size()) {
                    position = 0;
                }
                View v = super.getView(position, convertView, parent);
                v.setPadding(v.getPaddingLeft(),15, 0, 15);
                ((TextView) v).setGravity(Gravity.CENTER);
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
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }
        });
        for(int i = 0; i < kabupatenNamesList.size(); i++){
            if(kabupatenNamesList.get(i).equals(selectedProvinseInstansi)){
                index2 = i;
            }
        }
        spKabupaten.setSelection(index2);
        spKabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.black));
                if(position>=0) {
                    selectedKabupatenInstansiType = kabupatenArrayList.get(position).getPostalType();
                    selectedKabupatenInstansiType = kabupatenArrayList.get(position).getPostalType();
                    idKabupatenInstasiSpinner = kabupatenArrayList.get(position).getLookupId();
                    selectedKabupatenInstansi = kabupatenArrayList.get(position).getName();
                    setKotaList(selectedKabupatenInstansiType, idKabupatenInstasiSpinner);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setKotaList(String selectedKabupatenInstansiType, String idKabupatenInstasiSpinner) {
                RequestCityBody requestCityBody = new RequestCityBody();
                requestCityBody.setUsername("15040198");
                requestCityBody.setVersion("133");
                requestCityBody.setPostalId(idKabupatenInstasiSpinner);
                requestCityBody.setPostalType(selectedKabupatenInstansiType);
                provinceInterface.getKota(requestCityBody).enqueue(new Callback<KotaModel>() {
                    @Override
                    public void onResponse(Call<KotaModel> call, Response<KotaModel> response) {
                        if(response.isSuccessful()){
                            if(response.body().getResponseStatus().equals("OK")){
                                kotaNamesList = new ArrayList<>();
                                listKotaItems = new ArrayList<>();
                                for(ListKotaItem listItemKota : response.body().getList()){
                                    ListKotaItem listItemKotaModel = new ListKotaItem();
                                    listItemKotaModel.setName(listItemKota.getName());
                                    listItemKotaModel.setLookupId(listItemKota.getLookupId());
                                    listItemKotaModel.setPostalType(listItemKota.getPostalType());
                                    listKotaItems.add(listItemKotaModel);
                                    kotaNamesList.add(listItemKota.getName());
                                }

                                setSpinnerKotaList();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<KotaModel> call, Throwable t) {

                    }
                });

    }

    private void setSpinnerKotaList() {
        spCity.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,
                kotaNamesList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(position >= kotaNamesList.size()) {
                    position = 0;
                }
                View v = super.getView(position, convertView, parent);
                v.setPadding(v.getPaddingLeft(),15, 0, 15);
                ((TextView) v).setGravity(Gravity.CENTER);
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
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }
        });


        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.black));
                if(position >= 0){
                    selectedCityInstansi = listKotaItems.get(position).getName();
                    idCityInstasiSpinner = listKotaItems.get(position).getLookupId();
                    selectedCityInstansiType = listKotaItems.get(position).getPostalType();
                    setVillageData(selectedCityInstansi, idCityInstasiSpinner, selectedCityInstansiType);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setVillageData(String selectedCityInstansi, String idCityInstasiSpinner, String selectedCityInstansiType) {
        RequestVillageBody requestVillageBody = new RequestVillageBody();
        requestVillageBody.setUsername("15040198");
        requestVillageBody.setVersion("133");
        requestVillageBody.setPostalId(idCityInstasiSpinner);
        requestVillageBody.setPostalType(selectedCityInstansiType);
        provinceInterface.getVillage(requestVillageBody).enqueue(new Callback<Village>() {
            @Override
            public void onResponse(Call<Village> call, Response<Village> response) {
                if(response.isSuccessful()){
                    if(response.body().getResponseStatus().equals("OK")){
                        villageNamesList = new ArrayList<>();
                        villageListItems = new ArrayList<>();
                        for(VillageListItem villageListItem : response.body().getList()){
                            VillageListItem villageListItemModel = new VillageListItem();
                            villageListItemModel.setLookupId(villageListItem.getLookupId());
                            villageListItemModel.setName(villageListItem.getName());
                            villageListItemModel.setPostalType(villageListItem.getPostalType());
                            villageListItemModel.setZipCode(villageListItem.getZipCode());
                            villageListItems.add(villageListItemModel);
                            villageNamesList.add(villageListItem.getName());
                        }
                        setSpinnerVillage();
                    }
                }
            }

            @Override
            public void onFailure(Call<Village> call, Throwable t) {

            }
        });
    }

    private void setSpinnerVillage() {
        spVillage.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,
                villageNamesList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(position >= villageNamesList.size()) {
                    position = 0;
                }
                View v = super.getView(position, convertView, parent);
                v.setPadding(v.getPaddingLeft(),15, 0, 15);
                ((TextView) v).setGravity(Gravity.CENTER);
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
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }
        });


        spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.black));
                if(position >= 0){
                    selectedVillageInstansi = villageListItems.get(position).getName();
                    idVillageInstasiSpinner = villageListItems.get(position).getLookupId();
                    selectedVillageInstansiType = villageListItems.get(position).getPostalType();
                    selectedZipCode = villageListItems.get(position).getZipCode();
                    tvPostalCode.setText(villageListItems.get(position).getZipCode());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
}