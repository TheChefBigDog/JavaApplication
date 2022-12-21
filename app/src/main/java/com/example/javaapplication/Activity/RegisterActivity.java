package com.example.javaapplication.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import at.favre.lib.crypto.bcrypt.BCrypt;
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
import android.util.Base64;
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
import android.widget.TextView;
import android.widget.Toast;
import com.example.javaapplication.Activity.Adapter.ProvinsiAdapter;
import com.example.javaapplication.Activity.DBHelper.DBHelper;
import com.example.javaapplication.Activity.Model.Data.Province.ListItem;
import com.example.javaapplication.Activity.Model.Data.Province.ProvinceModel;
import com.example.javaapplication.Activity.Services.ProvinceInterface;
import com.example.javaapplication.Activity.Services.ProvinceUtils;
import com.example.javaapplication.R;
import com.example.javaapplication.databinding.ActivityLoginBinding;
import com.example.javaapplication.databinding.ActivityRegisterBinding;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity implements ProvinsiInterface{

    RecyclerView rvProvinsi;
    ProgressBar pbDialog;
    private int CODE_CAMERA_REQUEST = 1;
    SQLiteDatabase database;
    DBHelper dbHelper;
    ProvinceInterface provinceInterface;
    ArrayList<ListItem> provinceItemArrayList = new ArrayList<>();
    ProvinsiAdapter provinsiAdapter;
    String imageString, locationType, postalTypeRegistrasi, lookUpIdRegistrasi, text;
    LinearLayoutManager linearLayoutManager;
    Dialog dialog;
    Animation shake;
    ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setTitle("Register");
        dbHelper = new DBHelper(RegisterActivity.this);
        database = dbHelper.getWritableDatabase();
        binding.etProvince.setKeyListener(null);
        binding.etKabupaten.setKeyListener(null);
        binding.etCity.setKeyListener(null);
        binding.etVillage.setKeyListener(null);
        binding.tvPostalCode.setKeyListener(null);
        shake = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.shake);
        provinceInterface = ProvinceUtils.getListProvince(this);
        setProvinceList(postalTypeRegistrasi, lookUpIdRegistrasi, binding);
        binding.ivCameraTakeAPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, CODE_CAMERA_REQUEST);
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                String names = binding.etRegisterUsername.getText().toString();
                String password = binding.etRegisterPassword.getText().toString();;
                String phonenumber = binding.etRegisterPhoneNumber.getText().toString();
                String provinsi = binding.etProvince.getText().toString();
                String kabupaten = binding.etKabupaten.getText().toString();
                String kota = binding.etCity.getText().toString();
                String village = binding.etVillage.getText().toString();
                String zipCode = binding.tvPostalCode.getText().toString();
                int registerStatus = 1;
                if(dbHelper.getUserByName(names) > 0){
                    Toast.makeText(RegisterActivity.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                }else{
                    dbHelper.addName(names, password , phonenumber, provinsi, kabupaten, kota, village, zipCode, imageString, registerStatus);
                    startActivity(intent);
                    dbHelper.close();
                }
            }
        });
    }

    private void setProvinceList(String postalType, String lookUpId, ActivityRegisterBinding binding) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("135");
        binding.etProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        setKabupatenList(lookUpId, postalType, binding);
    }

    private void setKabupatenList(String selectedID, String selectedPostalType, ActivityRegisterBinding binding) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("135");
        requestLocationBody.setPostalId(selectedID);
        requestLocationBody.setPostalType(selectedPostalType);
            binding.etKabupaten.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationType = "district";
                    if(!binding.etProvince.getText().toString().equals("")){
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
        setKotaList(selectedPostalType, selectedID, binding);

    }


    private void setKotaList(String selectedKabupatenInstansiType, String idKabupatenInstasiSpinner, ActivityRegisterBinding binding) {
                RequestLocationBody requestLocationBody = new RequestLocationBody();
                requestLocationBody.setUsername("15040198");
                requestLocationBody.setVersion("135");
                requestLocationBody.setPostalId(idKabupatenInstasiSpinner);
                requestLocationBody.setPostalType(selectedKabupatenInstansiType);
        binding.etCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationType = "city";
                if(!binding.etKabupaten.getText().toString().equals("")){
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
        setVillageList(idKabupatenInstasiSpinner, selectedKabupatenInstansiType, binding);
    }

    private void setVillageList(String lookUpId, String postalType, ActivityRegisterBinding binding) {
        RequestLocationBody requestLocationBody = new RequestLocationBody();
        requestLocationBody.setUsername("15040198");
        requestLocationBody.setVersion("135");
        requestLocationBody.setPostalId(lookUpId);
        requestLocationBody.setPostalType(postalType);
        binding.etVillage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationType = "village";
                if(!binding.etCity.getText().toString().equals("")){
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
            binding.ivCameraTakeAPicture.setImageBitmap(imgFile);
            Uri tempUri = getImageUri(getApplicationContext(),imgFile);
            File finalFile = new File(getRealPathFromURI(tempUri));
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
        Random rand = new Random();
        int randNo = rand.nextInt(1000);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), photo, "ImageFile_" + randNo, null
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
        switch (locationType) {
            case "province":
                if (binding.etKabupaten.getText().length() != 0 || binding.etCity.getText().length() != 0 || binding.etVillage.getText().length() != 0 || binding.tvPostalCode.getText().length() != 0) {
                    binding.etVillage.setText("");
                    binding.etKabupaten.setText("");
                    binding.etCity.setText("");
                    binding.tvPostalCode.setText("");
                    binding.etKabupaten.startAnimation(shake);
                    binding.etVillage.startAnimation(shake);
                    binding.etCity.startAnimation(shake);
                    binding.tvPostalCode.startAnimation(shake);
                }
                binding.etProvince.setText(provinsi);
                setKabupatenList(lookUpId, postalType, binding);
                break;
//            case "district":
//                if (tvCity.getText().length() != 0 || tvVillage.getText().length() != 0 || tvPostalCode.getText().length() != 0) {
//                    tvVillage.setText("");
//                    tvCity.setText("");
//                    tvPostalCode.setText("");
//                    tvVillage.startAnimation(shake);
//                    tvCity.startAnimation(shake);
//                    tvPostalCode.startAnimation(shake);
//                }
//                tvKabupaten.setText(provinsi);
//                setKotaList(postalType, lookUpId);
//                break;
//            case "city":
//                if (tvVillage.getText().length() != 0 || tvPostalCode.getText().length() != 0) {
//                    tvVillage.setText("");
//                    tvPostalCode.setText("");
//                    tvVillage.startAnimation(shake);
//                    tvPostalCode.startAnimation(shake);
//                }
//                tvCity.setText(provinsi);
//                setVillageList(lookUpId, postalType);
//                break;
//            case "village":
//                tvPostalCode.setText(zipCode);
//                tvVillage.setText(provinsi);
//                break;
        }
//    }SELECT stored_passwd FROM passwd_table WHERE user_name = 'SomeUser' AND crypt('SomePassword', stored_passwd) = stored_passwd;
    }
    private String hashPassword(String parameter1){

//            try {
//                MessageDigest md = MessageDigest.getInstance("MD5");
//                byte[] messageDigest = md.digest(parameter1.getBytes());
//                BigInteger no = new BigInteger(1, messageDigest);
//                md.update(parameter1.getBytes(),0,parameter1.length());
//                String password = "1234";
//                String bcryptHashString = BCrypt.withDefaults().hashToString(12, parameter1.toCharArray());
//                    String hashed = BCrypt.hashpw("123BobbyRyan", BCrypt.gensalt());
//                BCrypt.Result result = BCrypt.verifyer().verify(bcryptHashString.toCharArray(), bcryptHashString);
//                String signature = new BigInteger(1).toString(16);
//                String ps = "UWlUxRKuOpDc02KV6wI1xg";
//                String tmp = Base64.encodeBytes(ps.getBytes());

// decode
//                String ps2 = "UWlUxRKuOpDc02KV6wI1xg=";
//                byte[] tmp2 = Base64.decode(parameter1, 0);
//                String val2 = new String(parameter1, "UTF-8");
//                System.out.println("Signature: "+ val2);
//                String hashtext = no.toString(16);
//                for(int i = 32 ; hashtext.length() < i ; i++){
//                    hashtext = "0" + hashtext;
//                }
                return parameter1;
//        String bcryptHashString = BCrypt.withDefaults().hashToString(12, parameter1.toCharArray());
//        Log.e("TAG", "hashPassword: " + r esult.details);
//            }catch (UnsupportedEncodingException e){
//                throw new RuntimeException(e);
//            }

    }

    private static class HexUtil {

        private static final char[] DIGITS = {
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };

        public static String toHex(byte[] data) {
            final StringBuilder sb = new StringBuilder();
            for (byte d : data) {
                sb.append(DIGITS[(d >>> 4) & 0x0F]);
                sb.append(DIGITS[d & 0x0F]);
            }
            return sb.toString();
        }
    }
}