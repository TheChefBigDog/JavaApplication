package com.example.javaapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.javaapplication.Activity.Adapter.UserAdapter;
import com.example.javaapplication.R;

public class CheckUserActivity extends AppCompatActivity {

    RecyclerView rvCheckUser;
    UserAdapter userAdapter;
    LinearLayoutManager lnrLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user);
        rvCheckUser = findViewById(R.id.rv_list_check_user);
        lnrLayoutManager = new LinearLayoutManager(this);
        userAdapter = new UserAdapter();
        rvCheckUser.setLayoutManager(lnrLayoutManager);
        rvCheckUser.setAdapter(userAdapter);


    }
}