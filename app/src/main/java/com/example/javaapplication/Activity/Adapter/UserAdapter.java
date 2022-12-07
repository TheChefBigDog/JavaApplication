package com.example.javaapplication.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.javaapplication.Activity.Model.Data.Province.ListItem;
import com.example.javaapplication.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.CommentHolder>{

    ArrayList<ListItem> listItemArrayList;
    Context context;



    @NonNull
    @Override
    public UserAdapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user, parent, false);
        return new UserAdapter.CommentHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull UserAdapter.CommentHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listItemArrayList.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {


        public CommentHolder(@NonNull View itemView) {


            super(itemView);
        }
    }
}
