package com.example.javaapplication.Activity.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.javaapplication.Activity.Model.Data.Kabupaten.ListItemKabupaten;
import com.example.javaapplication.Activity.Model.Data.Province.ListItem;
import com.example.javaapplication.Activity.ProvinsiInterface;
import com.example.javaapplication.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KabupatenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<ListItemKabupaten> listItemArrayList;
    Context context;
    private static final int PLACEHOLDER = 0;
    private static final int ITEM = 1;
    ProvinsiInterface pCallbackk;
    Dialog cmDialogFrag;

    public KabupatenAdapter(ArrayList<ListItemKabupaten> listItemArrayList, Context context, ProvinsiInterface provinsiInterface, Dialog cmDialogFrag) {
        this.listItemArrayList = listItemArrayList;
        this.context = context;
        this.pCallbackk = provinsiInterface;
        this.cmDialogFrag = cmDialogFrag;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case PLACEHOLDER :
                View viewItemPlaceholder = inflater.inflate(R.layout.card_placeholder, parent, false);
                viewHolder = new KabupatenAdapter.PlaceHolder(viewItemPlaceholder);
                break;
            case ITEM :
                View viewItemUser = inflater.inflate(R.layout.card_user, parent, false);
                viewHolder = new KabupatenAdapter.KabupatenHolder(viewItemUser);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case PLACEHOLDER:
                final PlaceHolder placeHolder = (PlaceHolder) holder;
                placeHolder.tvCityDialog.setText("Please Choose your Desired Province");
                break;
            case ITEM:
                final KabupatenHolder provinsiHolder = (KabupatenHolder) holder;
                provinsiHolder.tvProvinsiNama.setText(listItemArrayList.get(position).getName());
                provinsiHolder.lnrCvProvinsi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        pCallbackk.getProvinsi(listItemArrayList.get(position).getName(), listItemArrayList.get(position).getPostalType(), listItemArrayList.get(position).getLookupId());
                        cmDialogFrag.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listItemArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return PLACEHOLDER;
        }  else {
            return ITEM;
        }
    }

    public class PlaceHolder extends RecyclerView.ViewHolder {
        TextView tvCityDialog;

        public PlaceHolder(View viewItemPlaceholder) {
            super(viewItemPlaceholder);
            tvCityDialog = itemView.findViewById(R.id.tv_city_dialog);

        }
    }

    public class KabupatenHolder extends RecyclerView.ViewHolder {


        TextView tvProvinsiNama;
        LinearLayout lnrCvProvinsi;

        public KabupatenHolder(View viewItemUser) {
            super(viewItemUser);
            tvProvinsiNama = itemView.findViewById(R.id.tv_nama_provinsi);
            lnrCvProvinsi = itemView.findViewById(R.id.lnr_cv_provinsi);

        }
    }
}
